package simulador.excecoes;

// Exceção para indicar que um robô não possui os recursos necessários (como bateria, espaço de carga, etc.) para realizar uma determinada ação.

public class RecursoInsuficienteException extends Exception {
    public RecursoInsuficienteException(String mensagem) {
        super(mensagem);
    }
}