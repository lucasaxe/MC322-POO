package simulador.enums;

/**
 * Define os possíveis estados em que um robô pode se encontrar.
 * Isso ajuda a controlar o comportamento do robô (ex: um robô desligado não pode se mover).
 */
public enum EstadoRobo {
    LIGADO,
    DESLIGADO,
    EM_TAREFA,
    AVARIADO;
}