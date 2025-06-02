package simulador.interfaces;

import simulador.utils.CentralComunicacao;
import simulador.excecoes.ErroComunicacaoException;
import simulador.excecoes.RoboDesligadoException;

/**
 * Interface para robôs que podem enviar e receber mensagens.
 * Define o contrato para funcionalidades de comunicação.
 */
public interface Comunicavel {
    void enviarMensagem(Comunicavel destinatario, String mensagem, CentralComunicacao central) throws RoboDesligadoException, ErroComunicacaoException; // [cite: 38]

    void receberMensagem(String remetenteId, String mensagem) throws RoboDesligadoException; // [cite: 38]

    String getIdComunicacao();
}