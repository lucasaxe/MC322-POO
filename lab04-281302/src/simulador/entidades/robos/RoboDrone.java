package simulador.entidades.robos;

import simulador.core.Ambiente;
import simulador.enums.EstadoRobo;
import simulador.excecoes.*;
import simulador.interfaces.Explorador;
import simulador.interfaces.FuncionalidadesEspecificas;
import simulador.utils.CentralComunicacao;

import java.util.Scanner;

/**
 * Representa um drone, um tipo de RoboAereo ágil, mas com limitações de bateria e altitude máxima.
 * Implementa Explorador para realizar reconhecimento e FuncionalidadesEspecificas para seu menu.
 */
public class RoboDrone extends RoboAereo implements FuncionalidadesEspecificas, Explorador {
    private final int tetoDeVoo;
    private int nivelBateria;

    public RoboDrone(String id, String nome, int x, int y, int z, int altitudeCruzeiro,
                     int tetoDeVoo, int bateriaInicial) {
        super(id, nome, x, y, z, altitudeCruzeiro);
        this.tetoDeVoo = tetoDeVoo;
        this.nivelBateria = Math.min(Math.max(bateriaInicial, 0), 100);

        if (this.z > this.tetoDeVoo) {
            System.out.println("Aviso: Altitude inicial do Drone " + nome + " (" + this.z + "m) excede o teto de voo (" +
                    this.tetoDeVoo + "m). Ajustando para o teto.");
            this.z = this.tetoDeVoo;
        }
    }

    /** Simula o consumo de bateria e desliga o drone se a bateria acabar. */
    private void consumirBateria(int percentualConsumido) {
        if (this.estado == EstadoRobo.DESLIGADO) return;

        this.nivelBateria -= percentualConsumido;

        if (this.nivelBateria < 0) {
            this.nivelBateria = 0;
        }

        System.out.println("Drone " + nome + " - Bateria: " + nivelBateria + "%");

        if (this.nivelBateria == 0) {
            System.out.println("ALERTA CRÍTICO: Bateria do drone " + nome + " esgotada! Desligando automaticamente...");
            desligar();
        }
    }

    @Override
    public void executarTarefa() throws RoboDesligadoException, AcaoNaoPermitidaException {
        if (this.estado == EstadoRobo.DESLIGADO) throw new RoboDesligadoException(nome + " (Drone) está desligado.");

        if (nivelBateria < 10) {
            throw new AcaoNaoPermitidaException("Bateria do drone " + nome + " (" + nivelBateria + "%) muito baixa para executar tarefa.");
        }

        this.estado = EstadoRobo.EM_TAREFA;
        System.out.println(nome + " (Drone) iniciando voo de vigilância e patrulha.");
        consumirBateria(15);
        this.estado = EstadoRobo.LIGADO;
    }

    /**
     * Sobrescreve para incluir verificações de teto de voo e consumo de bateria.
     */
    @Override
    public void solicitarMoverPara(int novoX, int novoY, int novoZ, Ambiente ambiente) throws RoboDesligadoException, ForaDosLimitesException, ColisaoException, AcaoNaoPermitidaException {
        if (this.estado == EstadoRobo.DESLIGADO) throw new RoboDesligadoException(nome + " (Drone) está desligado.");

        if (nivelBateria < 2) { // Requer um mínimo de bateria para se mover.
            throw new AcaoNaoPermitidaException("Bateria do drone " + nome + " (" + nivelBateria + "%) muito baixa para mover.");
        }

        int zDestinoAjustado = novoZ;

        if (zDestinoAjustado > tetoDeVoo) {
            System.out.println("Drone " + nome + " não pode exceder seu teto de voo (" + tetoDeVoo + "m). Altitude ajustada.");
            zDestinoAjustado = tetoDeVoo;
        }
        if (zDestinoAjustado < 0) {
            System.out.println("Drone " + nome + " não pode voar abaixo do nível do solo (Z=0). Altitude ajustada.");
            zDestinoAjustado = 0;
        }

        super.solicitarMoverPara(novoX, novoY, zDestinoAjustado, ambiente);
        consumirBateria(3);
    }

    @Override
    public void explorarRedondezas(int raio) throws RoboDesligadoException, AcaoNaoPermitidaException {
        if (this.estado == EstadoRobo.DESLIGADO) throw new RoboDesligadoException(nome + " (Drone) está desligado.");

        if (nivelBateria < 25) { // Exploração consome mais bateria.
            throw new AcaoNaoPermitidaException("Bateria do drone " + nome + " (" + nivelBateria + "%) muito baixa para iniciar exploração.");
        }

        System.out.println("Drone " + nome + " iniciando exploração em um raio de " + raio + "m ao redor de (" + x + "," + y + "," + z + ").");
        consumirBateria(20);
        System.out.println("Exploração concluída por " + nome + ".");
    }

    @Override
    public String getRelatorioExploracaoSimplificado() throws RoboDesligadoException {
        if (this.estado == EstadoRobo.DESLIGADO) throw new RoboDesligadoException(nome + " (Drone) está desligado.");

        return "Relatório de Exploração do Drone " + nome + ": Área ao redor de (" + x + "," + y + "," + z + ") verificada. " + "Nada de extraordinário detectado (simulação). Bateria restante: " + nivelBateria + "%.";
    }

    @Override public String getNomeMenuFuncionalidades() { return "Controles Avançados do Drone " + nome; }

    @Override
    public void apresentarMenuFuncionalidades() {
        System.out.println("1. Verificar Nível da Bateria");
        System.out.println("2. Verificar Teto de Voo Máximo");
        System.out.println("3. Iniciar Exploração de Área Próxima");
    }

    @Override
    public void executarFuncionalidade(int opcao, Scanner scanner, Ambiente ambiente, CentralComunicacao central) {
        try {
            if (this.estado == EstadoRobo.DESLIGADO) {
                throw new RoboDesligadoException(nome + " (Drone) está desligado.");
            }
            switch (opcao) {
                case 1:
                    System.out.println("Nível da bateria do Drone " + nome + ": " + nivelBateria + "%");

                    break;
                case 2:
                    System.out.println("Teto de voo máximo do Drone " + nome + ": " + tetoDeVoo + "m");

                    break;
                case 3:
                    System.out.print("Digite o raio de exploração (em metros): ");

                    int raioExploracao = scanner.nextInt(); scanner.nextLine();

                    explorarRedondezas(raioExploracao);
                    System.out.println(getRelatorioExploracaoSimplificado());

                    break;
                default:
                    System.out.println("Opção inválida para as funcionalidades do drone.");
            }
        }
        catch (RoboDesligadoException | AcaoNaoPermitidaException e) {
            System.err.println("Erro na operação do drone: " + e.getMessage());
        }
    }
}