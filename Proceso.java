public class Proceso {
    public final String id;
    private final int rafaga;
    private int rafagaRestante;
    private final int prioridad;
    private int tiempoFinalizacion;
    private int tiempoEspera;
    private int tiempoRetorno;

    public Proceso(String id, int rafaga, int prioridad) {
        this.id = id;
        this.rafaga = rafaga;
        this.rafagaRestante = rafaga;
        this.prioridad = prioridad;
    }

    public Proceso(Proceso otro) {
        this.id = otro.id;
        this.rafaga = otro.rafaga;
        this.rafagaRestante = otro.rafaga;
        this.prioridad = otro.prioridad;
    }

    public String getId() {
        return id;
    }

    public int getRafaga() {
        return rafaga;
    }

    public int getRafagaRestante() {
        return rafagaRestante;
    }

    public void setRafagaRestante(int rafagaRestante) {
        this.rafagaRestante = rafagaRestante;
    }

    public int getPrioridad() {
        return prioridad;
    }

    public int getTiempoFinalizacion() {
        return tiempoFinalizacion;
    }

    public void setTiempoFinalizacion(int tiempoFinalizacion) {
        this.tiempoFinalizacion = tiempoFinalizacion;
    }

    public int getTiempoEspera() {
        return tiempoEspera;
    }

    public void setTiempoEspera(int tiempoEspera) {
        this.tiempoEspera = tiempoEspera;
    }

    public int getTiempoRetorno() {
        return tiempoRetorno;
    }

    public void setTiempoRetorno(int tiempoRetorno) {
        this.tiempoRetorno = tiempoRetorno;
    }
}
