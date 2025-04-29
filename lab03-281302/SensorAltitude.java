public class SensorAltitude extends Sensor {
    private final RoboAereo robo;

    public SensorAltitude(RoboAereo robo) {
        super(90);
        this.robo = robo;
    }

    @Override
    public void monitorar() {
        double altitudeAtual = robo.getZ();

        if (altitudeAtual > getRaio()) {
            System.out.println("O sensor de altitude não conseguiu verificar, o robô aéreo está acima de " + getRaio() + "m");
        } else {
            System.out.println("Altitude atual: " + altitudeAtual + " metros");
        }
    }
}