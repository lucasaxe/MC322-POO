package simulador.entidades.robos;

import simulador.core.Ambiente;
import simulador.entidades.Robo;
import simulador.enums.EstadoRobo;
import simulador.excecoes.AcaoNaoPermitidaException;
import simulador.excecoes.ColisaoException;
import simulador.excecoes.ForaDosLimitesException;
import simulador.excecoes.RoboDesligadoException;
import simulador.interfaces.Sensoreavel;
import simulador.sensores.Sensor;

/**
 * Representa um robô que opera em terra firme.
 * Sua coordenada Z é geralmente fixa (normalmente 0).
 * Herda de Robo e pode implementar interfaces como Sensoreavel.
 */
public class RoboTerrestre extends Robo implements Sensoreavel {
    private int velocidadeEmTerra;

    public RoboTerrestre(String id, String nome, int x, int y, int velocidadeEmTerra) {
        super(id, nome, x, y, 0);
        this.velocidadeEmTerra = velocidadeEmTerra;
    }

    @Override
    public void executarTarefa() throws RoboDesligadoException {
        if (this.estado == EstadoRobo.DESLIGADO) {
            throw new RoboDesligadoException(nome + " está desligado e não pode executar tarefa.");
        }

        this.estado = EstadoRobo.EM_TAREFA;

        System.out.println(nome + " (Terrestre) está patrulhando o perímetro em Z=" + this.z + " com velocidade máxima de " + this.velocidadeEmTerra + " unidades/tempo.");

        try { Thread.sleep(1000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }

        this.estado = EstadoRobo.LIGADO;
    }

    @Override
    public void acionarSensores() throws RoboDesligadoException {
        if (this.estado == EstadoRobo.DESLIGADO) {
            throw new RoboDesligadoException(nome + " está desligado, sensores inoperantes.");
        }
        if (sensores.isEmpty()) {
            System.out.println(nome + " (Terrestre) não possui sensores instalados.");
            return;
        }
        System.out.println(nome + " (Terrestre) acionando seus sensores:");
        for (Sensor s : sensores) {
            s.monitorar(); // Cada sensor executa sua lógica de monitoramento.
        }
    }

    @Override
    public void solicitarMoverPara(int novoX, int novoY, int novoZ, Ambiente ambiente) throws RoboDesligadoException, ForaDosLimitesException, ColisaoException, AcaoNaoPermitidaException {
        if (this.estado == EstadoRobo.DESLIGADO) {
            throw new RoboDesligadoException(nome + " está desligado, não pode mover.");
        }

        ambiente.moverEntidade(this, novoX, novoY, this.z);
    }
}