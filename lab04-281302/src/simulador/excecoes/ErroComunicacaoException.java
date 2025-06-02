package simulador.excecoes;

// Exceção para sinalizar problemas durante a comunicação entre robôs, como um destinatário que não pode receber mensagens.

public class ErroComunicacaoException extends Exception {
    public ErroComunicacaoException(String mensagem) {
        super(mensagem);
    }
}