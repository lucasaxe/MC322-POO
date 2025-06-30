package simulador.entidades.robos;

import simulador.entidades.Robo;
import simulador.enums.EstadoRobo;
import simulador.excecoes.RoboDesligadoException;
import simulador.excecoes.AcaoNaoPermitidaException;
import simulador.interfaces.Sensoreavel;
import simulador.sensores.Sensor;

public class RoboAereo extends Robo implements Sensoreavel {
    private int altitudeCruzeiro;

    public RoboAereo(String id, String nome, int x, int y, int z, int altitudeCruzeiro) {
        super(id, nome, x, y, z);
        this.altitudeCruzeiro = altitudeCruzeiro;
    }

    public int getAltitudeCruzeiro() {
        return this.altitudeCruzeiro;
    }

    @Override
    public void executarTarefa() throws RoboDesligadoException, AcaoNaoPermitidaException {
        if (this.estado == EstadoRobo.DESLIGADO) {
            throw new RoboDesligadoException(nome + " está desligado.");
        }

        this.estado = EstadoRobo.EM_TAREFA;
        System.out.println(nome + " (Aéreo) realizando voo de reconhecimento em altitude " + this.z +
                ". Altitude de cruzeiro alvo: " + getAltitudeCruzeiro() + "m.");
        try { Thread.sleep(1000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        this.estado = EstadoRobo.LIGADO;
    }

    @Override
    public void acionarSensores() throws RoboDesligadoException {
        if (this.estado == EstadoRobo.DESLIGADO) {
            throw new RoboDesligadoException(nome + " está desligado.");
        }
        if (sensores.isEmpty()) {
            System.out.println(nome + " (Aéreo) não possui sensores.");
            return;
        }
        System.out.println(nome + " (Aéreo) acionando seus sensores:");
        for (Sensor s : sensores) {
            s.monitorar();
        }
    }
}