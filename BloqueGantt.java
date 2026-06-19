public class BloqueGantt {
    private final String idProceso;
    private final int inicio;
    private final int fin;

    public BloqueGantt(String idProceso, int inicio, int fin) {
        this.idProceso = idProceso;
        this.inicio = inicio;
        this.fin = fin;
    }

    public String getIdProceso() {
        return idProceso;
    }

    public int getInicio() {
        return inicio;
    }

    public int getFin() {
        return fin;
    }
}
