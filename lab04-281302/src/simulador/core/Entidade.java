package simulador.core;

import simulador.enums.TipoEntidade;

/**
 * Define o contrato básico para qualquer "coisa" que pode existir no ambiente.
 * Garante que todos os objetos no ambiente (robôs, obstáculos) tenham uma forma padronizada
 * de informar sua posição, tipo, e como devem ser representados.
 */
public interface Entidade {
    int getX();
    int getY();
    int getZ();

    void setPosicao(int x, int y, int z);

    TipoEntidade getTipo();
    String getDescricao();
    char getRepresentacao();
}