package simulador.entidades.robos;

import simulador.core.Ambiente;
import simulador.enums.EstadoRobo;
import simulador.excecoes.AcaoNaoPermitidaException;
import simulador.excecoes.RoboDesligadoException;
import simulador.interfaces.Coletor;
import simulador.interfaces.FuncionalidadesEspecificas;
import simulador.utils.CentralComunicacao;

import java.util.Scanner;

/**
 * Representa um robô caminhão, especializado no transporte de cargas.
 * É um RoboTerrestre que implementa Coletor (para gerenciar sua carga) e FuncionalidadesEspecificas.
 */
public class RoboCaminhao extends RoboTerrestre implements FuncionalidadesEspecificas, Coletor {
    private int cargaAtualKg;
    private final int capacidadeMaxCargaKg;

    public RoboCaminhao(String id, String nome, int x, int y, int velocidade, int capacidadeMaxCargaKg) {
        super(id, nome, x, y, velocidade);
        this.capacidadeMaxCargaKg = capacidadeMaxCargaKg;
        this.cargaAtualKg = 0;
    }

    @Override
    public void executarTarefa() throws RoboDesligadoException {
        if (this.estado == EstadoRobo.DESLIGADO) throw new RoboDesligadoException(nome + " (Caminhão) está desligado.");

        this.estado = EstadoRobo.EM_TAREFA;
        System.out.println(nome + " (Caminhão) iniciando rota de transporte com " + cargaAtualKg + "kg de carga.");

        if (cargaAtualKg > 0) {
            descarregarItensColetados();
        }

        this.estado = EstadoRobo.LIGADO;
    }

    @Override
    public void coletarItem(String itemDescricao, int quantidadeKg) throws RoboDesligadoException, AcaoNaoPermitidaException {
        if (this.estado == EstadoRobo.DESLIGADO) throw new RoboDesligadoException(nome + " (Caminhão) está desligado.");

        if (cargaAtualKg + quantidadeKg > capacidadeMaxCargaKg) {
            throw new AcaoNaoPermitidaException("Capacidade de carga (" + capacidadeMaxCargaKg + "kg) do caminhão " + nome + " seria excedida.");
        }

        cargaAtualKg += quantidadeKg;
        System.out.println(nome + " carregou " + quantidadeKg + "kg de '" + itemDescricao + "'. Carga atual: " + cargaAtualKg + "kg.");
    }

    @Override
    public void executarFuncionalidade(int opcao, Scanner scanner, Ambiente ambiente, CentralComunicacao central) {
        try {
            if (this.estado == EstadoRobo.DESLIGADO) throw new RoboDesligadoException(nome + " desligado.");
            switch (opcao) {
                case 1:
                    System.out.print("Descrição da carga: "); String item = scanner.nextLine();
                    System.out.print("Peso (kg): "); int peso = scanner.nextInt(); scanner.nextLine();
                    coletarItem(item, peso);

                    break;
                case 2:
                    descarregarItensColetados();

                    break;
                case 3:
                    System.out.println(getInventario());

                    break;
                default:
                    System.out.println("Opção de carga inválida.");
            }
        }
        catch (RoboDesligadoException | AcaoNaoPermitidaException e) {
            System.err.println("Erro na operação do caminhão: " + e.getMessage());
        }
    }

    @Override
    public void descarregarItensColetados() throws RoboDesligadoException {
        if (this.estado == EstadoRobo.DESLIGADO) throw new RoboDesligadoException(nome + " (Caminhão) está desligado.");

        System.out.println(nome + " descarregou " + cargaAtualKg + "kg de sua carga.");
        cargaAtualKg = 0;
    }

    @Override
    public String getInventario() {
        return String.format("Caminhão %s - Carga Atual: %dkg / Capacidade Máxima: %dkg. Conteúdo: Carga geral.", nome, cargaAtualKg, capacidadeMaxCargaKg);
    }

    @Override public String getNomeMenuFuncionalidades() { return "Gerenciamento de Carga do Caminhão " + nome; }

    @Override
    public void apresentarMenuFuncionalidades() {
        System.out.println("1. Carregar Carga");
        System.out.println("2. Descarregar Toda a Carga");
        System.out.println("3. Verificar Inventário (Carga Atual e Capacidade)");
    }
}