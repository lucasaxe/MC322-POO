package simulador.missao;

import simulador.core.Ambiente;
import simulador.entidades.Robo;
import simulador.excecoes.*;
import simulador.interfaces.Missao;
import simulador.utils.Logger;

/**
 * Implementação concreta de uma missão para mover um robô até um ponto de destino específico.
 */
public class MissaoBuscarPonto implements Missao {
    private final int xDestino, yDestino, zDestino;

    public MissaoBuscarPonto(int x, int y, int z) {
        this.xDestino = x; this.yDestino = y; this.zDestino = z;
    }

    @Override
    public void executar(Robo robo, Ambiente ambiente, Logger logger) {
        logger.log("Iniciando Missão Buscar Ponto para " + robo.getNome() + ". Destino: (" + xDestino + ", " + yDestino + ", " + zDestino + ").");

        while (robo.getX() != xDestino || robo.getY() != yDestino || robo.getZ() != zDestino) {
            try {
                int novoX = robo.getX() + Integer.compare(xDestino, robo.getX());
                int novoY = robo.getY() + Integer.compare(yDestino, robo.getY());
                int novoZ = robo.getZ() + Integer.compare(zDestino, robo.getZ());

                robo.solicitarMoverPara(novoX, novoY, novoZ);
                logger.log("Robô " + robo.getNome() + " moveu-se para (" + robo.getX() + ", " + robo.getY() + ", " + robo.getZ() + ").");

                Thread.sleep(500);

            } catch (RoboDesligadoException | AcaoNaoPermitidaException e) {
                logger.log("ERRO: Missão interrompida. " + e.getMessage());
                break;
            } catch (ColisaoException | ForaDosLimitesException e) {
                logger.log("ERRO: Missão falhou. Rota direta bloqueada. (" + e.getMessage() + ")");
                break;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.log("ERRO: Missão foi interrompida.");
                break;
            }
        }

        if (robo.getX() == xDestino && robo.getY() == yDestino && robo.getZ() == zDestino) {
            logger.log("Missão Buscar Ponto para " + robo.getNome() + " concluída com sucesso.");
        } else {
            logger.log("Missão Buscar Ponto para " + robo.getNome() + " finalizada sem atingir o destino.");
        }
    }
}