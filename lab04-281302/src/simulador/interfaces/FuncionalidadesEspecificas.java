package simulador.interfaces;

import java.util.Scanner;
import simulador.core.Ambiente;
import simulador.utils.CentralComunicacao;

/**
 * Interface (original da sua base) para robôs que oferecem um menu interativo
 * com ações que são particulares a eles.
 * Esta pode ser considerada uma das "interfaces funcionais" do laboratório.
 */
public interface FuncionalidadesEspecificas {
    String getNomeMenuFuncionalidades();

    void apresentarMenuFuncionalidades();

    void executarFuncionalidade(int opcao, Scanner scanner, Ambiente ambiente, CentralComunicacao central);
}