package simulador.entidades.robos;

import simulador.core.Ambiente;
import simulador.enums.EstadoRobo;
import simulador.excecoes.AcaoNaoPermitidaException;
import simulador.excecoes.ErroComunicacaoException;
import simulador.excecoes.RoboDesligadoException;
import simulador.interfaces.Comunicavel;
import simulador.interfaces.FuncionalidadesEspecificas;
import simulador.utils.CentralComunicacao;

import java.util.Scanner;

/**
 * Representa um avião robótico, um tipo especializado de RoboAereo.
 * Possui funcionalidades como transporte de passageiros e comunicação.
 * Implementa Comunicavel para interações e FuncionalidadesEspecificas para seu menu.
 */
public class RoboAviao extends RoboAereo implements Comunicavel, FuncionalidadesEspecificas {
    private int passageirosAbordo;
    private final int capacidadeMaxPassageiros;
    private String destinoAtual;

    public RoboAviao(String id, String nome, int x, int y, int z, int altitudeCruzeiro, int capacidadeMaxPassageiros, String destinoInicial) {
        super(id, nome, x, y, z, altitudeCruzeiro);
        this.capacidadeMaxPassageiros = capacidadeMaxPassageiros;
        this.passageirosAbordo = 0;
        this.destinoAtual = destinoInicial;
    }

    /** Tenta embarcar passageiros, verificando a capacidade. */
    public void embarcar(int numPassageiros) throws AcaoNaoPermitidaException {
        if (this.estado == EstadoRobo.DESLIGADO) {
            throw new AcaoNaoPermitidaException(nome + " está desligado, não pode embarcar passageiros.");
        }
        if (passageirosAbordo + numPassageiros > capacidadeMaxPassageiros) {
            throw new AcaoNaoPermitidaException("Capacidade máxima (" + capacidadeMaxPassageiros + " passageiros) seria excedida.");
        }

        passageirosAbordo += numPassageiros;
        System.out.println(numPassageiros + " passageiros embarcaram no " + nome + ". Total a bordo: " + passageirosAbordo);
    }

    public void definirDestino(String novoDestino) {
        if (this.estado == EstadoRobo.DESLIGADO) {
            System.out.println(nome + " está desligado. Destino não pode ser alterado até ser ligado.");
            return;
        }

        this.destinoAtual = novoDestino;
        System.out.println(nome + " agora tem como destino: " + novoDestino);
    }

    @Override
    public void executarTarefa() throws RoboDesligadoException {
        if (this.estado == EstadoRobo.DESLIGADO) throw new RoboDesligadoException(nome + " (Avião) está desligado.");

        this.estado = EstadoRobo.EM_TAREFA;
        System.out.println(nome + " (Avião) iniciando procedimento de voo para " + destinoAtual + " com " + passageirosAbordo + " passageiros. Altitude de cruzeiro: " + getAltitudeCruzeiro() + "m.");
        this.estado = EstadoRobo.LIGADO;
    }

    // Implementação da interface Comunicavel
    @Override
    public String getIdComunicacao() {
        return "AVIAO_" + this.id;
    }

    @Override
    public void enviarMensagem(Comunicavel destinatario, String mensagem, CentralComunicacao central)
            throws RoboDesligadoException, ErroComunicacaoException {
        if (this.estado == EstadoRobo.DESLIGADO) throw new RoboDesligadoException(nome + " (Avião) está desligado.");
        if (destinatario == null) throw new ErroComunicacaoException("Destinatário da mensagem é nulo.");

        String msgFormatada = String.format("[Mensagem de %s para %s]: %s", getIdComunicacao(), destinatario.getIdComunicacao(), mensagem);

        System.out.println(msgFormatada);

        central.registrarMensagem(getIdComunicacao(), destinatario.getIdComunicacao(), mensagem);
        destinatario.receberMensagem(getIdComunicacao(), mensagem);
    }

    @Override
    public void receberMensagem(String remetenteId, String mensagem) throws RoboDesligadoException {
        if (this.estado == EstadoRobo.DESLIGADO) throw new RoboDesligadoException(nome + " (Avião) está desligado.");

        System.out.println("[" + getIdComunicacao() + " recebeu mensagem de " + remetenteId + "]: " + mensagem);
    }

    @Override public String getNomeMenuFuncionalidades() { return "Operações de Voo do Avião " + nome; }

    @Override
    public void apresentarMenuFuncionalidades() {
        System.out.println("1. Embarcar Passageiros");
        System.out.println("2. Definir Destino do Voo");
        System.out.println("3. Fazer Anúncio aos Passageiros (simulado via comunicação)");
        System.out.println("4. Ver Status Detalhado do Voo");
    }

    @Override
    public void executarFuncionalidade(int opcao, Scanner scanner, Ambiente ambiente, CentralComunicacao central) {
        try {
            if (this.estado == EstadoRobo.DESLIGADO) {
                throw new RoboDesligadoException(nome + " (Avião) está desligado. Ligue-o para usar funcionalidades.");
            }

            switch (opcao) {
                case 1:
                    System.out.print("Número de passageiros para embarcar: ");
                    int numPass = scanner.nextInt(); scanner.nextLine(); // Consome o newline.
                    embarcar(numPass);

                    break;
                case 2:
                    System.out.print("Digite o novo destino: ");
                    String novoDest = scanner.nextLine();
                    definirDestino(novoDest);

                    break;
                case 3:
                    System.out.print("Digite o anúncio do comandante: ");
                    String anuncio = scanner.nextLine();
                    enviarMensagem(this, "[Anúncio do Comandante " + nome + "]: " + anuncio, central);

                    break;
                case 4:
                    System.out.printf("Status do Voo de %s: Destino Atual: %s, Passageiros a Bordo: %d/%d, Altitude Atual: %dm\n", nome, destinoAtual, passageirosAbordo, capacidadeMaxPassageiros, this.z);

                    break;
                default:
                    System.out.println("Opção inválida para as funcionalidades do avião.");
            }
        }
        catch (RoboDesligadoException | AcaoNaoPermitidaException | ErroComunicacaoException e) {
            System.err.println("Erro na operação do avião: " + e.getMessage());
        }
    }
}