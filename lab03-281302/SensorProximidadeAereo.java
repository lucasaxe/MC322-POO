import java.util.List;

public class SensorProximidadeAereo extends Sensor {
    private final RoboAereo robo;
    private final Ambiente ambiente;

    public SensorProximidadeAereo(RoboAereo robo, Ambiente ambiente) {
        super(15);
        this.robo = robo;
        this.ambiente = ambiente;
    }

    @Override
    public void monitorar() {
        List<Robo> robosProximos = ambiente.getRobos().stream()
                .filter(r -> r != robo && r instanceof RoboAereo)
                .filter(r -> calcularDistancia(robo, (RoboAereo) r) <= getRaio())
                .toList();

        if(robosProximos.isEmpty()){
            System.out.println("Nenhum robô aéreo próximo detectado");
        }
        else{
            System.out.println("Robô(s) aéreo(s) próximo(s) detectado(s):");
            robosProximos.forEach(r -> System.out.println("- " + r + " (distância: " +
                    String.format("%.2f", calcularDistancia(robo, (RoboAereo) r)) + "m)"));
        }
    }

    private double calcularDistancia(RoboAereo r1, RoboAereo r2) {
        return Math.sqrt(Math.pow(r1.getX() - r2.getX(), 2) + Math.pow(r1.getY() - r2.getY(), 2) + Math.pow(r1.getZ() - r2.getZ(), 2));
    }
}