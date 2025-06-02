package simulador.sensores;

import simulador.core.Ambiente;
import simulador.entidades.robos.RoboAereo;
import simulador.enums.EstadoRobo;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Sensor para um RoboAereo detectar a presença de outros RoboAereos próximos.
 */
public class SensorProximidadeAereo extends Sensor {
    private final RoboAereo roboAnexado;
    private final Ambiente ambienteObservado;

    public SensorProximidadeAereo(RoboAereo robo, Ambiente ambiente) {
        super(50.0);
        this.roboAnexado = robo;
        this.ambienteObservado = ambiente;
    }

    /**
     * Lógica de monitoramento: busca por outros robôs aéreos dentro do raio de alcance.
     */
    @Override
    public void monitorar() {
        if (roboAnexado.getEstado() == EstadoRobo.DESLIGADO) {
            System.out.println("Sensor de Proximidade Aérea (" + roboAnexado.getNome() + "): Robô está desligado.");
            return;
        }

        List<RoboAereo> outrosRobosAereosProximos = ambienteObservado.getRobos().stream().filter(r -> r instanceof RoboAereo && r != roboAnexado)
                .map(r -> (RoboAereo) r).filter(outroAereo -> calcularDistancia3D(outroAereo) <= getRaioDeAlcance()).collect(Collectors.toList());

        if (outrosRobosAereosProximos.isEmpty()) {
            System.out.println("Sensor de Proximidade Aérea (" + roboAnexado.getNome() + "): Nenhum outro robô aéreo detectado por perto.");
        }
        else {
            System.out.println("Sensor de Proximidade Aérea (" + roboAnexado.getNome() + ") detectou os seguintes robôs aéreos próximos:");
            for (RoboAereo outro : outrosRobosAereosProximos) {
                System.out.printf(" - Robô '%s' (ID: %s) em (%d,%d,%d), a uma distância de %.1fm.\n", outro.getNome(), outro.getId(), outro.getX(),
                        outro.getY(), outro.getZ(), calcularDistancia3D(outro));
            }
        }
    }

    private double calcularDistancia3D(RoboAereo outroRoboAereo) {
        double deltaX = roboAnexado.getX() - outroRoboAereo.getX();
        double deltaY = roboAnexado.getY() - outroRoboAereo.getY();
        double deltaZ = roboAnexado.getZ() - outroRoboAereo.getZ();
        return Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ);
    }
}