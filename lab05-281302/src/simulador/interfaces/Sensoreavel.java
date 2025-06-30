package simulador.interfaces;

import simulador.excecoes.RoboDesligadoException;

/**
 * Interface para entidades (geralmente robôs) que possuem sensores
 * e podem, portanto, "sentir" o ambiente.
 * Define o contrato para a ação de acionar esses sensores.
 */
public interface Sensoreavel {
    void acionarSensores() throws RoboDesligadoException;
}