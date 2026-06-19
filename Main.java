import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner SCANNER = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Simulador de Algoritmos de Planificacion de Procesos");
        System.out.println("Todos los procesos llegan a la cola de listos en el instante 0.");
        System.out.println();

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

        System.out.println();
        System.out.println("Resultado para " + nombreAlgoritmo);
        imprimirGantt(resultado.getBloques());
        imprimirTabla(resultado.getProcesos());
    }

    private static List<Proceso> leerProcesos() {
        int cantidad = leerEnteroPositivo("Ingrese la cantidad de procesos: ");
        List<Proceso> procesos = new ArrayList<>();

        for (int i = 1; i <= cantidad; i++) {
            System.out.println();
            System.out.println("Proceso " + i);
            System.out.print("ID del proceso (Enter para P" + i + "): ");
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
        System.out.println();
        System.out.println("Seleccione el algoritmo:");
        System.out.println("1. FCFS");
        System.out.println("2. SJF");
        System.out.println("3. SRTF");
        System.out.println("4. Priority Schedule");
        System.out.println("5. Quantum / Round Robin");

        while (true) {
            int opcion = leerEnteroPositivo("Opcion: ");
            if (opcion >= 1 && opcion <= 5) {
                return opcion;
            }
            System.out.println("La opcion debe estar entre 1 y 5.");
        }
    }

    private static int leerEnteroPositivo(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String entrada = SCANNER.nextLine().trim();
            try {
                int valor = Integer.parseInt(entrada);
                if (valor > 0) {
                    return valor;
                }
                System.out.println("Debe ingresar un numero entero positivo.");
            } catch (NumberFormatException e) {
                System.out.println("Entrada invalida. Ingrese un numero entero positivo.");
            }
        }
    }

    private static void imprimirGantt(List<BloqueGantt> bloques) {
        System.out.println();
        System.out.println("Diagrama de Gantt:");

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

        System.out.println(lineaProcesos);
        System.out.println(lineaTiempos);
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

        System.out.println();
        System.out.printf("%-10s %-10s %-10s %-14s %-10s %-10s%n",
                "Proceso", "Prioridad", "Rafaga", "Finalizacion", "Espera", "Retorno");
        System.out.println("--------------------------------------------------------------------");

        for (Proceso proceso : ordenados) {
            totalEspera += proceso.getTiempoEspera();
            totalRetorno += proceso.getTiempoRetorno();
            System.out.printf("%-10s %-10d %-10d %-14d %-10d %-10d%n",
                    proceso.getId(),
                    proceso.getPrioridad(),
                    proceso.getRafaga(),
                    proceso.getTiempoFinalizacion(),
                    proceso.getTiempoEspera(),
                    proceso.getTiempoRetorno());
        }

        System.out.println();
        System.out.printf("Tiempo promedio de espera: %.2f%n", totalEspera / ordenados.size());
        System.out.printf("Tiempo promedio de retorno: %.2f%n", totalRetorno / ordenados.size());
    }
}
