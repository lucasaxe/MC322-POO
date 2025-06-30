package simulador.sensores;

import simulador.core.Ambiente;
import simulador.entidades.Obstaculo;
import simulador.entidades.robos.RoboTerrestre;
import simulador.enums.EstadoRobo;
import simulador.enums.TipoObstaculo;
import java.util.Optional;

/**
 * Sensor para RoboTerrestre, capaz de detectar fontes térmicas extremas
 * como Vulcões ou Geleiras nas proximidades.
 */
public class SensorTemperatura extends Sensor {
    private final RoboTerrestre roboAnexado;
    private final Ambiente ambienteObservado;

    public SensorTemperatura(RoboTerrestre robo, Ambiente ambiente) {
        super(30.0);
        this.roboAnexado = robo;
        this.ambienteObservado = ambiente;
    }

    /**
     * Lógica de monitoramento: procura por Vulcões ou Geleiras perto do robô.
     */
    @Override
    public void monitorar() {
        if (roboAnexado.getEstado() == EstadoRobo.DESLIGADO) {
            System.out.println("Sensor de Temperatura (" + roboAnexado.getNome() + "): Robô está desligado.");
            return;
        }

        Optional<Obstaculo> fonteTermicaEncontrada = ambienteObservado.getObstaculosList().stream()
                .filter(obs -> obs.getTipoEspecifico() == TipoObstaculo.VULCAO || obs.getTipoEspecifico() == TipoObstaculo.GELEIRA)
                .filter(ft -> calcularDistanciaAoObstaculo(ft) <= getRaioDeAlcance()).findFirst();

        if (fonteTermicaEncontrada.isPresent()) {
            Obstaculo ft = fonteTermicaEncontrada.get();
            String tipoFonte = ft.getTipoEspecifico().name();
            double temperaturaEstimada = (ft.getTipoEspecifico() == TipoObstaculo.VULCAO) ? 700.0 : -50.0; // Temperatura simulada.

            System.out.printf("Sensor de Temperatura (%s): Alerta! %s próximo detectado em (%d,%d) a %.1fm! Temperatura estimada: %.0f°C\n",
                    roboAnexado.getNome(), tipoFonte, ft.getX(), ft.getY(), calcularDistanciaAoObstaculo(ft), temperaturaEstimada);
        }
        else {
            System.out.println("Sensor de Temperatura (" + roboAnexado.getNome() + "): Nenhuma fonte térmica extrema detectada nas proximidades. " + "Temperatura ambiente estimada: 25°C.");
        }
    }

    private double calcularDistanciaAoObstaculo(Obstaculo obs) {
        double deltaX = roboAnexado.getX() - obs.getX();
        double deltaY = roboAnexado.getY() - obs.getY();
        return Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }
}