package simulador.excecoes;

// Exceção lançada quando um robô tenta realizar uma ação que não é permitida pelo seu estado atual, tipo, ou pelas regras do simulador.

public class AcaoNaoPermitidaException extends Exception {
    public AcaoNaoPermitidaException(String mensagem) {
        super(mensagem);
    }
}