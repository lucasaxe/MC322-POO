package simulador.interfaces;

import simulador.excecoes.AcaoNaoPermitidaException;
import simulador.excecoes.RecursoInsuficienteException;
import simulador.excecoes.RoboDesligadoException;

/**
 * Interface para robôs que têm a capacidade de coletar itens ou recursos do ambiente.
 * Define as ações básicas de coleta e gerenciamento de inventário.
 */
public interface Coletor {
    void coletarItem(String item, int quantidade) throws RoboDesligadoException, AcaoNaoPermitidaException, RecursoInsuficienteException;

    void descarregarItensColetados() throws RoboDesligadoException;

    String getInventario();
}