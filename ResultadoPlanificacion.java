import java.util.List;

public class ResultadoPlanificacion {
    private final List<Proceso> procesos;
    private final List<BloqueGantt> bloques;

    public ResultadoPlanificacion(List<Proceso> procesos, List<BloqueGantt> bloques) {
        this.procesos = procesos;
        this.bloques = bloques;
    }

    public List<Proceso> getProcesos() {
        return procesos;
    }

    public List<BloqueGantt> getBloques() {
        return bloques;
    }
}
