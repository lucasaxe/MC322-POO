package simulador.enums;

/**
 * Define os diferentes "tipos" de coisas que podem existir no nosso ambiente de simulação.
 * Usamos isso para saber se algo é um robô, um obstáculo, ou apenas um espaço vazio.
 */
public enum TipoEntidade {
    VAZIO,
    ROBO,
    OBSTACULO,
    DESCONHECIDO;
}