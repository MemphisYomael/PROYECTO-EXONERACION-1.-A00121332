import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Planificador {
    public ResultadoPlanificacion fcfs(List<Proceso> procesosOriginales) {
        List<Proceso> procesos = copiarProcesos(procesosOriginales);
        List<BloqueGantt> bloques = ejecutarNoExpropiativo(procesos);
        calcularTiempos(procesos);
        return new ResultadoPlanificacion(procesos, bloques);
    }

    public ResultadoPlanificacion sjf(List<Proceso> procesosOriginales) {
        List<Proceso> procesos = copiarProcesos(procesosOriginales);
        procesos.sort(Comparator.comparingInt(Proceso::getRafaga));
        List<BloqueGantt> bloques = ejecutarNoExpropiativo(procesos);
        calcularTiempos(procesos);
        return new ResultadoPlanificacion(procesos, bloques);
    }

    public ResultadoPlanificacion srtf(List<Proceso> procesosOriginales) {
        List<Proceso> procesos = copiarProcesos(procesosOriginales);
        List<Proceso> pendientes = new ArrayList<>(procesos);
        List<BloqueGantt> bloques = new ArrayList<>();
        int tiempo = 0;

        while (!pendientes.isEmpty()) {
            Proceso actual = pendientes.stream()
                    .min(Comparator.comparingInt(Proceso::getRafagaRestante))
                    .orElseThrow();

            int inicio = tiempo;
            while (actual.getRafagaRestante() > 0) {
                actual.setRafagaRestante(actual.getRafagaRestante() - 1);
                tiempo++;
            }

            actual.setTiempoFinalizacion(tiempo);
            pendientes.remove(actual);
            bloques.add(new BloqueGantt(actual.getId(), inicio, tiempo));
        }

        calcularTiempos(procesos);
        return new ResultadoPlanificacion(procesos, bloques);
    }

    public ResultadoPlanificacion priority(List<Proceso> procesosOriginales) {
        List<Proceso> procesos = copiarProcesos(procesosOriginales);
        procesos.sort(Comparator.comparingInt(Proceso::getPrioridad));
        List<BloqueGantt> bloques = ejecutarNoExpropiativo(procesos);
        calcularTiempos(procesos);
        return new ResultadoPlanificacion(procesos, bloques);
    }

    public ResultadoPlanificacion roundRobin(List<Proceso> procesosOriginales, int quantum) {
        List<Proceso> procesos = copiarProcesos(procesosOriginales);
        Queue<Proceso> cola = new LinkedList<>(procesos);
        List<BloqueGantt> bloques = new ArrayList<>();
        int tiempo = 0;

        while (!cola.isEmpty()) {
            Proceso actual = cola.poll();
            int inicio = tiempo;
            int tiempoEjecutado = Math.min(quantum, actual.getRafagaRestante());

            tiempo += tiempoEjecutado;
            actual.setRafagaRestante(actual.getRafagaRestante() - tiempoEjecutado);
            bloques.add(new BloqueGantt(actual.getId(), inicio, tiempo));

            if (actual.getRafagaRestante() > 0) {
                cola.offer(actual);
            } else {
                actual.setTiempoFinalizacion(tiempo);
            }
        }

        calcularTiempos(procesos);
        return new ResultadoPlanificacion(procesos, bloques);
    }

    private List<BloqueGantt> ejecutarNoExpropiativo(List<Proceso> procesos) {
        List<BloqueGantt> bloques = new ArrayList<>();
        int tiempo = 0;

        for (Proceso proceso : procesos) {
            int inicio = tiempo;
            tiempo += proceso.getRafaga();
            proceso.setRafagaRestante(0);
            proceso.setTiempoFinalizacion(tiempo);
            bloques.add(new BloqueGantt(proceso.getId(), inicio, tiempo));
        }

        return bloques;
    }

    private List<Proceso> copiarProcesos(List<Proceso> procesosOriginales) {
        List<Proceso> copia = new ArrayList<>();
        for (Proceso proceso : procesosOriginales) {
            copia.add(new Proceso(proceso));
        }
        return copia;
    }

    private void calcularTiempos(List<Proceso> procesos) {
        for (Proceso proceso : procesos) {
            proceso.setTiempoRetorno(proceso.getTiempoFinalizacion());
            proceso.setTiempoEspera(proceso.getTiempoRetorno() - proceso.getRafaga());
        }
    }
}
