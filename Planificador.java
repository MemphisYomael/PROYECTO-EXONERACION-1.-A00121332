import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Planificador {
    public ResultadoPlanificacion fcfs(List<Proceso> procesosOriginales) {
        List<Proceso> procesos = copiarProcesos(procesosOriginales);
        List<BloqueGantt> bloques = ejecutarProcesosEnOrden(procesos);
        calcularTiempos(procesos);
        return new ResultadoPlanificacion(procesos, bloques);
    }

    public ResultadoPlanificacion sjf(List<Proceso> procesosOriginales) {
        List<Proceso> procesos = copiarProcesos(procesosOriginales);
        procesos.sort(Comparator.comparingInt(Proceso::getRafaga));
        List<BloqueGantt> bloques = ejecutarProcesosEnOrden(procesos);
        calcularTiempos(procesos);
        return new ResultadoPlanificacion(procesos, bloques);
    }


    public ResultadoPlanificacion srtf(List<Proceso> procesosOriginales) {
        List<Proceso> procesos = copiarProcesos(procesosOriginales);
        List<Proceso> pendientes = new ArrayList<>(procesos);
        List<BloqueGantt> bloques = new ArrayList<>();
        int tiempo = 0;
        int tiempoSecuencia = 0;

        int contador = 0;
        boolean bloquearSecuencia = false;
        while (!pendientes.isEmpty()) {

            Proceso actual = pendientes.stream()
                    .min(Comparator.comparingInt(Proceso::getRafagaRestante))
                    .orElseThrow();
            Proceso secuencia = pendientes.getFirst();

            if(!bloquearSecuencia) {
                int inicioSecuencia = tiempoSecuencia;
                secuencia.setRafagaRestante(secuencia.getRafagaRestante() - 1);
                tiempoSecuencia--;

                bloques.add(new BloqueGantt(secuencia.getId(), inicioSecuencia, tiempoSecuencia));

            }


            if(actual != secuencia){
                bloquearSecuencia = true;
            }

            int inicio = tiempo;

            actual.setRafagaRestante(actual.getRafagaRestante() - 1);
            tiempo++;

            bloques.add(new BloqueGantt(actual.getId(), inicio, tiempo));

            if (actual.getRafagaRestante() == 0) {
                actual.setTiempoFinalizacion(tiempo);
                pendientes.remove(actual);
                bloquearSecuencia =false;
            }
        }

        calcularTiempos(procesos);
        return new ResultadoPlanificacion(procesos, bloques);
    }


    public ResultadoPlanificacion priority(List<Proceso> procesosOriginales) {
        List<Proceso> procesos = copiarProcesos(procesosOriginales);
        procesos.sort(Comparator.comparingInt(Proceso::getPrioridad));
        List<BloqueGantt> bloques = ejecutarProcesosEnOrden(procesos);
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

    private List<BloqueGantt> ejecutarProcesosEnOrden(List<Proceso> procesos) {
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
