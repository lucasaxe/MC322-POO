package simulador.interfaces;

import simulador.core.Entidade;
import simulador.excecoes.AcaoNaoPermitidaException;
import simulador.excecoes.RoboDesligadoException;

/**
 * Interface para robôs com capacidades defensivas, como proteger uma área ou ativar escudos.
 * Esta é uma das interfaces funcionais adicionais sugeridas.
 */
public interface Defensor {
    void ativarModoDefesa(Entidade alvo) throws RoboDesligadoException, AcaoNaoPermitidaException;

    void desativarModoDefesa() throws RoboDesligadoException;

    String getStatusDefesa();
}