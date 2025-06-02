package simulador.excecoes;

// Exceção utilizada para indicar que uma tentativa de movimento ou posicionamento resultaria em uma colisão com outra entidade ou com os limites do ambiente.

public class ColisaoException extends Exception {
    public ColisaoException(String mensagem) {
        super(mensagem);
    }
}