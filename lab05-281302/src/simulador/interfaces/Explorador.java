package simulador.interfaces;

import simulador.excecoes.AcaoNaoPermitidaException;
import simulador.excecoes.RoboDesligadoException;

/**
 * Interface para robôs que possuem a capacidade de explorar o ambiente ao seu redor.
 * Define comportamentos relacionados à exploração e obtenção de informações da área.
 */
public interface Explorador {
    void explorarRedondezas(int raio) throws RoboDesligadoException, AcaoNaoPermitidaException;

    String getRelatorioExploracaoSimplificado() throws RoboDesligadoException;
}