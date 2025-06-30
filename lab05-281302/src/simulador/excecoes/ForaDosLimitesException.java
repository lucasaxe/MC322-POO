package simulador.excecoes;

// Exceção lançada quando uma entidade tenta se mover para uma posição ou ser colocada fora das dimensões válidas do ambiente.

public class ForaDosLimitesException extends Exception {
    public ForaDosLimitesException(String mensagem) {
        super(mensagem);
    }
}