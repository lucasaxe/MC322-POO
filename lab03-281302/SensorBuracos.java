import java.util.List;

public class SensorBuracos extends Sensor {
    private final RoboTerrestre robo;
    private final Ambiente ambiente;

    public SensorBuracos(RoboTerrestre robo, Ambiente ambiente) {
        super(5);
        this.robo = robo;
        this.ambiente = ambiente;
    }

    @Override
    public void monitorar() {
        List<Obstaculo> buracosProximos = ambiente.getObstaculos().stream()
                .filter(o -> o.getTipo() == TipoObstaculo.BURACO)
                .filter(o -> calcularDistancia(robo, o) <= getRaio())
                .toList();

        if (buracosProximos.isEmpty()) {
            System.out.println("Nenhum buraco detectado nas proximidades");
        } else {
            System.out.println("Buraco(s) detectado(s) nas seguintes coordenadas:");
            buracosProximos.forEach(b -> {
                double centroX = (b.getX1() + b.getX2()) / 2.0;
                double centroY = (b.getY1() + b.getY2()) / 2.0;
                System.out.printf("- (%.1f, %.1f)%n", centroX, centroY);
            });
        }
    }

    private double calcularDistancia(RoboTerrestre robo, Obstaculo obstaculo) {
        double centroX = (obstaculo.getX1() + obstaculo.getX2()) / 2.0;
        double centroY = (obstaculo.getY1() + obstaculo.getY2()) / 2.0;
        return Math.sqrt(Math.pow(robo.getX() - centroX, 2) + Math.pow(robo.getY() - centroY, 2));
    }
}