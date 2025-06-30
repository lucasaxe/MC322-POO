package simulador.entidades;

import simulador.core.Ambiente;
import simulador.excecoes.*;

/**
 * Subsistema responsável por controlar a movimentação de um robô.
 * Encapsula a lógica de como um robô se move, interagindo com o ambiente.
 */
public class ControleMovimento {
    private final Robo robo;
    private final Ambiente ambiente;

    public ControleMovimento(Robo robo, Ambiente ambiente) {
        this.robo = robo;
        this.ambiente = ambiente;
    }

    public void moverPara(int novoX, int novoY, int novoZ)
            throws RoboDesligadoException, ForaDosLimitesException, ColisaoException, AcaoNaoPermitidaException {
        // Delega a validação e execução do movimento para o ambiente.
        ambiente.moverEntidade(robo, novoX, novoY, novoZ);
    }
}