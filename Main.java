import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

import static java.lang.System.out;

public class Main {

    private static final Scanner SCANNER = new Scanner(System.in);

    public static void main(String[] args) {
        out.println("Simulador de Algoritmos de Planificacion de Procesos");
        out.println("Todos los procesos llegan a la cola de listos en el instante 0.");
        out.println();

        List<Proceso> procesos = leerProcesos();
        int opcion = leerOpcionMenu();

        Planificador planificador = new Planificador();
        ResultadoPlanificacion resultado;
        String nombreAlgoritmo;

        switch (opcion) {
            case 1:
                resultado = planificador.fcfs(procesos);
                nombreAlgoritmo = "FCFS";
                break;
            case 2:
                resultado = planificador.sjf(procesos);
                nombreAlgoritmo = "SJF";
                break;
            case 3:
                resultado = planificador.srtf(procesos);
                nombreAlgoritmo = "SRTF";
                break;
            case 4:
                resultado = planificador.priority(procesos);
                nombreAlgoritmo = "Priority Schedule";
                break;
            case 5:
                int quantum = leerEnteroPositivo("Ingrese el quantum: ");
                resultado = planificador.roundRobin(procesos, quantum);
                nombreAlgoritmo = "Round Robin";
                break;
            default:
                throw new IllegalStateException("Opcion no valida");
        }

        out.println();
        out.println("Resultado para " + nombreAlgoritmo);
        imprimirGantt(resultado.getBloques());
        imprimirTabla(resultado.getProcesos());
    }

    private static List<Proceso> leerProcesos() {
        int cantidad = leerEnteroPositivo("Ingrese la cantidad de procesos: ");
        List<Proceso> procesos = new ArrayList<>();

        for (int i = 1; i <= cantidad; i++) {
            out.println();
            out.println("Proceso " + i);
            out.print("ID del proceso (Enter para P" + i + "): ");
            String id = SCANNER.nextLine().trim();
            if (id.isEmpty()) {
                id = "P" + i;
            }

            int rafaga = leerEnteroPositivo("Rafaga CPU de " + id + ": ");
            int prioridad = leerEnteroPositivo("Prioridad de " + id + " (menor numero = mayor prioridad): ");
            procesos.add(new Proceso(id, rafaga, prioridad));
        }

        return procesos;
    }

    private static int leerOpcionMenu() {
        out.println();
        out.println("Seleccione el algoritmo:");
        out.println("1. FCFS");
        out.println("2. SJF");
        out.println("3. SRTF");
        out.println("4. Priority Schedule");
        out.println("5. Quantum / Round Robin");

        while (true) {
            int opcion = leerEnteroPositivo("Opcion: ");
            if (opcion >= 1 && opcion <= 5) {
                return opcion;
            }
            out.println("La opcion debe estar entre 1 y 5.");
        }
    }

    private static int leerEnteroPositivo(String mensaje) {
        while (true) {
            out.print(mensaje);
            String entrada = SCANNER.nextLine().trim();
            try {
                int valor = Integer.parseInt(entrada);
                if (valor > 0) {
                    return valor;
                }
                out.println("Debe ingresar un numero entero positivo.");
            } catch (NumberFormatException e) {
                out.println("Entrada invalida. Ingrese un numero entero positivo.");
            }
        }
    }

    private static void imprimirGantt(List<BloqueGantt> bloques) {
        out.println();
        out.println("Diagrama de Gantt:");

        StringBuilder lineaProcesos = new StringBuilder();
        StringBuilder lineaTiempos = new StringBuilder();

        for (BloqueGantt bloque : bloques) {
            String etiqueta = " " + bloque.getIdProceso() + " ";
            int ancho = Math.max(etiqueta.length(), String.valueOf(bloque.getFin()).length() + 2);
            lineaProcesos.append("|").append(centrar(etiqueta.trim(), ancho));

            if (lineaTiempos.length() == 0) {
                lineaTiempos.append(bloque.getInicio());
            }
            int espacios = ancho + 1 - String.valueOf(bloque.getFin()).length();
            lineaTiempos.append(" ".repeat(Math.max(1, espacios))).append(bloque.getFin());
        }
        lineaProcesos.append("|");

        out.println(lineaProcesos);
        out.println(lineaTiempos);
    }

    private static String centrar(String texto, int ancho) {
        int espacios = ancho - texto.length();
        int izquierda = espacios / 2;
        int derecha = espacios - izquierda;
        return " ".repeat(izquierda) + texto + " ".repeat(derecha);
    }

    private static void imprimirTabla(List<Proceso> procesos) {
        List<Proceso> ordenados = new ArrayList<>(procesos);
        ordenados.sort(Comparator.comparing(Proceso::getId));

        double totalEspera = 0;
        double totalRetorno = 0;

        out.println();
        out.printf("%-10s %-10s %-10s %-14s %-10s %-10s%n",
                "Proceso", "Prioridad", "Rafaga", "Finalizacion", "Espera", "Retorno");
        out.println("--------------------------------------------------------------------");

        for (Proceso proceso : ordenados) {
            totalEspera += proceso.getTiempoEspera();
            totalRetorno += proceso.getTiempoRetorno();
            out.printf("%-10s %-10d %-10d %-14d %-10d %-10d%n",
                    proceso.getId(),
                    proceso.getPrioridad(),
                    proceso.getRafaga(),
                    proceso.getTiempoFinalizacion(),
                    proceso.getTiempoEspera(),
                    proceso.getTiempoRetorno());
        }

        out.println();
        out.printf("Tiempo promedio de espera: %.2f%n", totalEspera / ordenados.size());
        out.printf("Tiempo promedio de retorno: %.2f%n", totalRetorno / ordenados.size());
    }
}
