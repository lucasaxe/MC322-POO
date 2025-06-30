package simulador.interfaces;

import simulador.core.Ambiente;
import simulador.entidades.Robo;
import simulador.utils.Logger;

/**
 * [cite_start]Interface que define o contrato para qualquer missão autônoma [cite: 87]
 * que um AgenteInteligente possa executar.
 */
public interface Missao {
    /**
     * [cite_start]O método principal que contém a lógica da missão. [cite: 89, 90]
     * @param robo O robô que está executando a missão.
     * @param ambiente O ambiente onde a missão ocorre.
     * @param logger O logger para registrar os eventos da missão.
     */
    void executar(Robo robo, Ambiente ambiente, Logger logger);
}