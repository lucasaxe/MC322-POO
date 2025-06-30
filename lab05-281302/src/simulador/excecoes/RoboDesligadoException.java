package simulador.excecoes;

// Exceção específica para quando uma ação é tentada em um robô que está desligado e, portanto, não pode executá-la.

public class RoboDesligadoException extends Exception {
    public RoboDesligadoException(String mensagem) {
        super(mensagem);
    }
}