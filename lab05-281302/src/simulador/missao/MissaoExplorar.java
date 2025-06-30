package simulador.missao;

import simulador.core.Ambiente;
import simulador.entidades.Robo;
import simulador.excecoes.*;
import simulador.interfaces.Missao;
import simulador.utils.Logger;
import java.util.Random;

/**
 * [cite_start]Implementação concreta de uma missão para explorar o ambiente de forma aleatória. [cite: 91]
 */
public class MissaoExplorar implements Missao {
    private final int numeroDePassos;

    public MissaoExplorar(int numeroDePassos) {
        this.numeroDePassos = numeroDePassos;
    }

    @Override
    public void executar(Robo robo, Ambiente ambiente, Logger logger) {
        logger.log("Iniciando Missão de Exploração para o robô " + robo.getNome() + " por " + numeroDePassos + " passos.");
        Random random = new Random();

        for (int i = 0; i < numeroDePassos; i++) {
            try {
                int direcao = random.nextInt(4); // 0: Norte (Y+), 1: Leste (X+), 2: Sul (Y-), 3: Oeste (X-)
                int novoX = robo.getX();
                int novoY = robo.getY();

                switch (direcao) {
                    case 0: novoY++; break;
                    case 1: novoX++; break;
                    case 2: novoY--; break;
                    case 3: novoX--; break;
                }

                robo.solicitarMoverPara(novoX, novoY, robo.getZ());
                logger.log("Passo " + (i + 1) + ": Robô " + robo.getNome() + " moveu-se para (" + robo.getX() + ", " + robo.getY() + ").");

                Thread.sleep(500);

            } catch (RoboDesligadoException | AcaoNaoPermitidaException e) {
                logger.log("ERRO: Missão interrompida. " + e.getMessage());
                break;
            } catch (ColisaoException | ForaDosLimitesException e) {
                logger.log("Passo " + (i + 1) + ": " + robo.getNome() + " encontrou obstáculo/limite. Tentando outra direção.");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.log("ERRO: Missão interrompida.");
                break;
            }
        }
        logger.log("Missão de Exploração para " + robo.getNome() + " concluída.");
    }
}