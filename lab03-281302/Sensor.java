public abstract class Sensor {
    protected final double raio;

    public Sensor(double raio) {
        this.raio = raio;
    }

    public abstract void monitorar();

    public double getRaio() {
        return raio;
    }
}

