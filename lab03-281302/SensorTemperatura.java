import java.util.Optional;

public class SensorTemperatura extends Sensor {
    private final RoboTerrestre robo;
    private final Ambiente ambiente;

    public SensorTemperatura(RoboTerrestre robo, Ambiente ambiente) {
        super(10.0);
        this.robo = robo;
        this.ambiente = ambiente;
    }

    @Override
    public void monitorar() {
        Optional<Obstaculo> obstaculoTermico = ambiente.getObstaculos().stream()
                .filter(o -> o.getTipo() == TipoObstaculo.VULCAO || o.getTipo() == TipoObstaculo.GELEIRA)
                .filter(o -> calcularDistancia(robo, o) <= getRaio())
                .findFirst();

        if (obstaculoTermico.isPresent()) {
            Obstaculo obstaculo = obstaculoTermico.get();
            if (obstaculo.getTipo() == TipoObstaculo.VULCAO) {
                System.out.println("Detectado VULCÃO próximo. Temperatura: 70°C");
            } else if (obstaculo.getTipo() == TipoObstaculo.GELEIRA) {
                System.out.println("Detectado GELEIRA próxima. Temperatura: -40°C");
            }
        } else {
            System.out.println("Nenhum obstáculo térmico detectado. Temperatura ambiente: 25°C");
        }
    }

    private double calcularDistancia(RoboTerrestre robo, Obstaculo obstaculo) {
        double centroX = (obstaculo.getX1() + obstaculo.getX2()) / 2.0;
        double centroY = (obstaculo.getY1() + obstaculo.getY2()) / 2.0;
        return Math.sqrt(Math.pow(robo.getX() - centroX, 2) + Math.pow(robo.getY() - centroY, 2));
    }
}