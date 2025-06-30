package simulador.entidades.robos;

import simulador.core.Ambiente;
import simulador.core.Entidade;
import simulador.entidades.Obstaculo;
import simulador.enums.EstadoRobo;
import simulador.enums.TipoEntidade;
import simulador.enums.TipoObstaculo;
import simulador.excecoes.*;
import simulador.interfaces.Coletor;
import simulador.interfaces.FuncionalidadesEspecificas;
import simulador.utils.CentralComunicacao;

import java.util.Iterator;
import java.util.Scanner;

/**
 * Representa um robô terrestre especializado em escavação.
 * Pode modificar o terreno criando ou removendo obstáculos do tipo BURACO.
 * Implementa Coletor (para o material escavado) e FuncionalidadesEspecificas.
 */
public class RoboEscavador extends RoboTerrestre implements FuncionalidadesEspecificas, Coletor {
    private int profundidadeEscavacaoPadrao;
    private int materialEscavadoKg;
    private final int capacidadeMaxMaterialKg = 500;

    public RoboEscavador(String id, String nome, int x, int y, int velocidade, int profundidadeEscavacao) {
        super(id, nome, x, y, velocidade);
        this.profundidadeEscavacaoPadrao = profundidadeEscavacao;
        this.materialEscavadoKg = 0;
    }

    public void escavarNaPosicaoAtual(Ambiente ambiente) throws RoboDesligadoException, ColisaoException, ForaDosLimitesException, AcaoNaoPermitidaException {
        if (this.estado == EstadoRobo.DESLIGADO) throw new RoboDesligadoException(nome + " (Escavador) está desligado.");

        System.out.println(nome + " iniciando escavação na posição (" + x + "," + y + ") até " + profundidadeEscavacaoPadrao + "m de profundidade (simulada).");

        Iterator<Entidade> it = ambiente.getEntidadesInternalListForModification().iterator();
        while (it.hasNext()) {
            Entidade ent = it.next();

            if (ent instanceof Obstaculo) {
                Obstaculo obs = (Obstaculo) ent;

                if (obs.getTipoEspecifico() == TipoObstaculo.BURACO && obs.getX() == this.x && obs.getY() == this.y && obs.getZBase() == 0) {
                    if(ambiente.isDentroDosLimites(this.x, this.y, 0)) {
                        ambiente.getMapaRawView()[this.x][this.y][0] = TipoEntidade.VAZIO;
                    }

                    it.remove();
                    System.out.println("Buraco preexistente em (" + this.x + "," + this.y + ") foi removido/substituído.");

                    break;
                }
            }
        }

        Obstaculo novoBuraco = new Obstaculo(this.x, this.y, this.x, this.y, 0, TipoObstaculo.BURACO);
        ambiente.adicionarEntidade(novoBuraco);

        System.out.println("Escavação concluída. Novo buraco criado/atualizado em (" + x + "," + y + ").");

        coletarItem("Terra/Entulho", 50);
    }

    @Override
    public void executarTarefa() throws RoboDesligadoException {
        if (this.estado == EstadoRobo.DESLIGADO) throw new RoboDesligadoException(nome + " (Escavador) está desligado.");

        this.estado = EstadoRobo.EM_TAREFA;
        System.out.println(nome + " (Escavador) preparando para iniciar uma tarefa de escavação programada.");

        try {
            coletarItem("Amostra de rocha", 10);
        }
        catch (Exception e) {
            System.err.println("Falha na coleta durante tarefa do escavador: " + e.getMessage());
        }

        this.estado = EstadoRobo.LIGADO;
    }

    @Override
    public void coletarItem(String itemDescricao, int quantidadeKg) throws RoboDesligadoException, AcaoNaoPermitidaException {
        if (this.estado == EstadoRobo.DESLIGADO) throw new RoboDesligadoException(nome + " (Escavador) está desligado.");

        if (materialEscavadoKg + quantidadeKg > capacidadeMaxMaterialKg) {
            throw new AcaoNaoPermitidaException("Caçamba do escavador " + nome + " está cheia (Capacidade: " + capacidadeMaxMaterialKg + "kg).");
        }

        materialEscavadoKg += quantidadeKg;
        System.out.println(nome + " (Escavador) coletou " + quantidadeKg + "kg de '" + itemDescricao + "'. Total na caçamba: " + materialEscavadoKg + "kg.");
    }

    @Override
    public void descarregarItensColetados() throws RoboDesligadoException {
        if (this.estado == EstadoRobo.DESLIGADO) throw new RoboDesligadoException(nome + " (Escavador) está desligado.");

        System.out.println(nome + " (Escavador) descarregou " + materialEscavadoKg + "kg de material escavado.");
        materialEscavadoKg = 0;
    }

    @Override
    public String getInventario() {
        return String.format("Escavador %s - Material na Caçamba: %dkg / Capacidade Máxima: %dkg.", nome, materialEscavadoKg, capacidadeMaxMaterialKg);
    }

    @Override public String getNomeMenuFuncionalidades() { return "Operações de Escavação de " + nome; }

    @Override
    public void apresentarMenuFuncionalidades() {
        System.out.println("1. Escavar na Posição Atual");
        System.out.println("2. Descarregar Material Coletado da Caçamba");
        System.out.println("3. Verificar Nível de Material na Caçamba");
    }

    @Override
    public void executarFuncionalidade(int opcao, Scanner scanner, Ambiente ambiente, CentralComunicacao central) {
        try {
            if (this.estado == EstadoRobo.DESLIGADO) {
                throw new RoboDesligadoException(nome + " (Escavador) está desligado.");
            }
            switch (opcao) {
                case 1:
                    escavarNaPosicaoAtual(ambiente);

                    break;
                case 2:
                    descarregarItensColetados();

                    break;
                case 3:
                    System.out.println(getInventario());

                    break;
                default:
                    System.out.println("Opção inválida para as funcionalidades do escavador.");
            }
        }
        catch (RoboDesligadoException | ColisaoException | ForaDosLimitesException | AcaoNaoPermitidaException e) {
            System.err.println("Erro durante operação do escavador: " + e.getMessage());
        }
    }
}