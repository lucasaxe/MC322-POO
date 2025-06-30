package simulador.sensores;

import simulador.entidades.robos.RoboAereo;
import simulador.enums.EstadoRobo;

/**
 * Um tipo de Sensor especializado em monitorar a altitude de um RoboAereo.
 */
public class SensorAltitude extends Sensor {
    private final RoboAereo roboAnexado;

    public SensorAltitude(RoboAereo robo) {
        super(1000);
        this.roboAnexado = robo;
    }

    @Override
    public void monitorar() {
        if (roboAnexado.getEstado() == EstadoRobo.DESLIGADO) {
            System.out.println("Sensor de Altitude (" + roboAnexado.getNome() + "): Robô está desligado. Leitura de altitude não disponível.");
            return;
        }

        double altitudeAtual = roboAnexado.getZ();
        System.out.println("Sensor de Altitude (" + roboAnexado.getNome() + "): Altitude atual = " + altitudeAtual + "m.");

        if (altitudeAtual > this.raioDeAlcance) {
            System.out.println(" -> Aviso: Altitude (" + altitudeAtual + "m) excede o alcance operacional ideal do sensor (" + this.raioDeAlcance + "m). Leituras podem ser imprecisas.");
        }
    }
}