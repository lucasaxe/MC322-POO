package simulador.sensores;

import simulador.core.Ambiente;
import simulador.entidades.Obstaculo;
import simulador.entidades.robos.RoboTerrestre;
import simulador.enums.EstadoRobo;
import simulador.enums.TipoObstaculo;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Um Sensor especializado para RoboTerrestre, projetado para detectar
 * obstáculos do tipo BURACO nas proximidades.
 */
public class SensorBuracos extends Sensor {
    private final RoboTerrestre roboAnexado;
    private final Ambiente ambienteObservado;

    public SensorBuracos(RoboTerrestre robo, Ambiente ambiente) {
        super(5.0);
        this.roboAnexado = robo;
        this.ambienteObservado = ambiente;
    }

    /**
     * Lógica de monitoramento: procura por buracos perto do robô.
     */
    @Override
    public void monitorar() {
        if (roboAnexado.getEstado() == EstadoRobo.DESLIGADO) {
            System.out.println("Sensor de Buracos (" + roboAnexado.getNome() + "): Robô está desligado.");
            return;
        }

        List<Obstaculo> buracosProximos = ambienteObservado.getObstaculosList().stream().filter(obs -> obs.getTipoEspecifico() == TipoObstaculo.BURACO)
                .filter(buraco -> calcularDistanciaAoObstaculo(buraco) <= getRaioDeAlcance()).collect(Collectors.toList());

        if (buracosProximos.isEmpty()) {
            System.out.println("Sensor de Buracos (" + roboAnexado.getNome() + "): Nenhum buraco detectado nas proximidades (raio de " + getRaioDeAlcance() + "m).");
        }
        else {
            System.out.println("Sensor de Buracos (" + roboAnexado.getNome() + ") detectou os seguintes buracos próximos:");

            for (Obstaculo b : buracosProximos) {
                System.out.printf(" - Buraco encontrado em (%d,%d) a uma distância de %.1fm.\n", b.getX(), b.getY(), calcularDistanciaAoObstaculo(b));
            }
        }
    }

    private double calcularDistanciaAoObstaculo(Obstaculo obs) {
        double deltaX = roboAnexado.getX() - obs.getX();
        double deltaY = roboAnexado.getY() - obs.getY();
        return Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }
}