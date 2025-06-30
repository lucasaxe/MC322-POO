package simulador.entidades.robos;

import simulador.core.Ambiente;
import simulador.entidades.AgenteInteligente;
import simulador.excecoes.AcaoNaoPermitidaException;
import simulador.excecoes.RoboDesligadoException;
import simulador.utils.Logger;

/**
 * Um exemplo de robô autônomo concreto. [cite_start]Herda de AgenteInteligente. [cite: 83]
 */
public class RoboExplorador extends AgenteInteligente {

    public RoboExplorador(String id, String nome, int x, int y) {
        super(id, nome, x, y, 0); // É um tipo de agente terrestre
    }

    /**
     * Inicia a execução da missão atribuída, delegando a lógica para o objeto Missao.
     */
    @Override
    public void executarMissao(Ambiente ambiente, Logger logger) {
        if (!temMissao()) {
            System.out.println("Robô " + getNome() + " não tem nenhuma missão para executar.");
            logger.log("Tentativa de executar missão para " + getNome() + ", mas nenhuma missão foi atribuída.");
            return;
        }

        System.out.println("Robô " + getNome() + " iniciando execução de missão...");

        // Garante que os subsistemas estejam prontos antes de executar a missão
        if (this.controleMovimento == null) {
            inicializarSubsistemas(ambiente);
        }

        missao.executar(this, ambiente, logger);
    }

    @Override
    public void executarTarefa() throws RoboDesligadoException, AcaoNaoPermitidaException {
        if (this.estado == simulador.enums.EstadoRobo.DESLIGADO) throw new RoboDesligadoException(nome + " está desligado.");
        System.out.println(nome + " (Explorador) executando tarefa padrão de escaneamento local.");
    }
}