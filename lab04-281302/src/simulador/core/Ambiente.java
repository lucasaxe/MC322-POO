package simulador.core;

import simulador.entidades.Obstaculo;
import simulador.entidades.Robo;
import simulador.enums.EstadoRobo;
import simulador.enums.TipoEntidade;
import simulador.enums.TipoObstaculo;
import simulador.excecoes.AcaoNaoPermitidaException;
import simulador.excecoes.ColisaoException;
import simulador.excecoes.ForaDosLimitesException;
import simulador.excecoes.RoboDesligadoException;
import simulador.interfaces.Sensoreavel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Representa o espaço tridimensional onde as entidades (robôs, obstáculos) operam.
 * Gerencia a lista de entidades e um mapa 3D que representa a ocupação do espaço.
 */
public class Ambiente {
    private final int dimX, dimY, dimZ;
    private final ArrayList<Entidade> listaDeEntidades;
    private TipoEntidade[][][] mapaOcupacao;

    public Ambiente(int x, int y, int z) {
        if (x <= 0 || y <= 0 || z <= 0) {
            throw new IllegalArgumentException("As dimensões do ambiente devem ser positivas.");
        }

        this.dimX = x;
        this.dimY = y;
        this.dimZ = z;
        this.listaDeEntidades = new ArrayList<>();
        this.inicializarMapaInterno();
    }

    private void inicializarMapaInterno() {
        this.mapaOcupacao = new TipoEntidade[dimX][dimY][dimZ];

        for (int i = 0; i < dimX; i++) {
            for (int j = 0; j < dimY; j++) {
                for (int k = 0; k < dimZ; k++) {
                    mapaOcupacao[i][j][k] = TipoEntidade.VAZIO;
                }
            }
        }
    }

    public synchronized void adicionarEntidade(Entidade ent) throws ColisaoException, ForaDosLimitesException {
        if (ent instanceof Obstaculo) {
            Obstaculo obs = (Obstaculo) ent;

            for (int xCoord = obs.getX1(); xCoord <= obs.getX2(); xCoord++) {
                for (int yCoord = obs.getY1(); yCoord <= obs.getY2(); yCoord++) {
                    int zBaseObs = obs.getZBase();

                    if (!isDentroDosLimites(xCoord, yCoord, zBaseObs)) {
                        throw new ForaDosLimitesException("Obstáculo '" + obs.getDescricao() + "' parcialmente fora dos limites do ambiente.");
                    }
                    if (mapaOcupacao[xCoord][yCoord][zBaseObs] != TipoEntidade.VAZIO && mapaOcupacao[xCoord][yCoord][zBaseObs] != ent.getTipo()) {
                        throw new ColisaoException("Colisão ao adicionar Obstáculo: Posição (" + xCoord + "," + yCoord + "," + zBaseObs + ") já ocupada por " + mapaOcupacao[xCoord][yCoord][zBaseObs] + ".");
                    }

                    if (obs.getTipoEspecifico().getAlturaOuAltitudePadrao() > 0 && obs.getTipoEspecifico() != TipoObstaculo.BURACO) {
                        for (int h = 0; h < obs.getTipoEspecifico().getAlturaOuAltitudePadrao(); h++) {
                            int zCamadaAtual = obs.getZBase() + h;

                            if(isDentroDosLimites(xCoord,yCoord,zCamadaAtual)) {
                                if (mapaOcupacao[xCoord][yCoord][zCamadaAtual] != TipoEntidade.VAZIO && mapaOcupacao[xCoord][yCoord][zCamadaAtual] != ent.getTipo()) {
                                    throw new ColisaoException("Colisão ao adicionar Obstáculo (camada de altura): Posição (" + xCoord + "," + yCoord + "," + zCamadaAtual + ") já ocupada por " + mapaOcupacao[xCoord][yCoord][zCamadaAtual] + ".");
                                }
                            }
                        }
                    }
                }
            }
        }
        else {
            if (!isDentroDosLimites(ent.getX(), ent.getY(), ent.getZ())) {
                throw new ForaDosLimitesException("Entidade '" + ent.getDescricao() + "' está fora dos limites do ambiente.");
            }
            if (mapaOcupacao[ent.getX()][ent.getY()][ent.getZ()] != TipoEntidade.VAZIO) {
                throw new ColisaoException("Colisão ao adicionar Entidade: Posição (" + ent.getX() + "," + ent.getY() + "," + ent.getZ() + ") já ocupada por " + mapaOcupacao[ent.getX()][ent.getY()][ent.getZ()] + ".");
            }
        }

        this.listaDeEntidades.add(ent);
        atualizarMapaParaEntidade(ent, ent.getTipo());
        System.out.println("Entidade adicionada: " + ent.getDescricao());
    }

    private void atualizarMapaParaEntidade(Entidade ent, TipoEntidade tipoParaMarcar) {
        if (ent instanceof Obstaculo) {
            Obstaculo obs = (Obstaculo) ent;

            for (int x = obs.getX1(); x <= obs.getX2(); x++) {
                for (int y = obs.getY1(); y <= obs.getY2(); y++) {
                    int zBaseObs = obs.getZBase();

                    if(isDentroDosLimites(x,y,zBaseObs)) {
                        mapaOcupacao[x][y][zBaseObs] = tipoParaMarcar;
                    }
                    if (obs.getTipoEspecifico().getAlturaOuAltitudePadrao() > 0 && obs.getTipoEspecifico() != TipoObstaculo.BURACO) {
                        for (int h = 0; h < obs.getTipoEspecifico().getAlturaOuAltitudePadrao(); h++) {
                            int zCamadaAtual = obs.getZBase() + h;

                            if(isDentroDosLimites(x,y,zCamadaAtual)) {
                                mapaOcupacao[x][y][zCamadaAtual] = tipoParaMarcar;
                            }
                        }
                    }
                }
            }
        }
        else {
            if (isDentroDosLimites(ent.getX(), ent.getY(), ent.getZ())) {
                mapaOcupacao[ent.getX()][ent.getY()][ent.getZ()] = tipoParaMarcar;
            }
        }
    }

    public boolean isDentroDosLimites(int x, int y, int z) {
        return x >= 0 && x < dimX && y >= 0 && y < dimY && z >= 0 && z < dimZ;
    }

    public synchronized void moverEntidade(Entidade ent, int novoX, int novoY, int novoZ) throws ColisaoException, ForaDosLimitesException, RoboDesligadoException, AcaoNaoPermitidaException {
        if (ent instanceof Obstaculo) {
            throw new AcaoNaoPermitidaException("Obstáculos são entidades fixas e não podem ser movidos dinamicamente.");
        }

        if (ent instanceof Robo) {
            Robo robo = (Robo) ent;

            if (robo.getEstado() == EstadoRobo.DESLIGADO) {
                throw new RoboDesligadoException("O robô '" + robo.getNome() + "' está desligado e não pode se mover.");
            }
        }

        if (!isDentroDosLimites(novoX, novoY, novoZ)) {
            throw new ForaDosLimitesException("O destino do movimento (" + novoX + "," + novoY + "," + novoZ + ") está fora dos limites do ambiente.");
        }

        boolean mesmaPosicaoAtual = (ent.getX() == novoX && ent.getY() == novoY && ent.getZ() == novoZ);
        if (!mesmaPosicaoAtual && mapaOcupacao[novoX][novoY][novoZ] != TipoEntidade.VAZIO) {
            throw new ColisaoException("Falha ao mover '" + ent.getDescricao() + "'. A posição de destino (" +
                    novoX + "," + novoY + "," + novoZ + ") já está ocupada por " + mapaOcupacao[novoX][novoY][novoZ] + ".");
        }

        if (isDentroDosLimites(ent.getX(), ent.getY(), ent.getZ())) {
            mapaOcupacao[ent.getX()][ent.getY()][ent.getZ()] = TipoEntidade.VAZIO;
        }

        ent.setPosicao(novoX, novoY, novoZ);

        mapaOcupacao[novoX][novoY][novoZ] = ent.getTipo();
        System.out.println("Entidade '" + ent.getDescricao().split(" \\(")[0] + "' movida para (" + novoX + "," + novoY + "," + novoZ + ").");
    }

    public void visualizarMapaConsole() {
        System.out.println("\n--- Mapa do Ambiente (Visão Superior, Camada Z=0 aproximada) ---");
        System.out.print("   ");

        for (int j = 0; j < dimY; j++) {
            System.out.printf("%2d ", j);
        }
        System.out.println();

        for (int i = 0; i < dimX; i++) {
            System.out.printf("%2d|", i);

            for (int j = 0; j < dimY; j++) {
                char charParaMostrar = '.';

                TipoEntidade tipoNaCamada0 = mapaOcupacao[i][j][0];

                if (tipoNaCamada0 != TipoEntidade.VAZIO) {
                    boolean entidadeEspecificaEncontrada = false;

                    for (Entidade e : listaDeEntidades) {
                        if (e instanceof Obstaculo) {
                            Obstaculo obs = (Obstaculo) e;
                            if (i >= obs.getX1() && i <= obs.getX2() && j >= obs.getY1() && j <= obs.getY2() && obs.getZBase() <= 0 && (obs.getZBase() + obs.getTipoEspecifico().getAlturaOuAltitudePadrao()) > 0) {
                                charParaMostrar = e.getRepresentacao();
                                entidadeEspecificaEncontrada = true;

                                break;
                            }
                        }
                        else if (e.getX() == i && e.getY() == j && e.getZ() == 0) {
                            charParaMostrar = e.getRepresentacao();
                            entidadeEspecificaEncontrada = true;

                            break;
                        }
                    }
                    if (!entidadeEspecificaEncontrada) {
                        charParaMostrar = Character.toLowerCase(tipoNaCamada0.name().charAt(0));
                    }
                }
                System.out.printf("[%c]", charParaMostrar);
            }
            System.out.println();
        }
        System.out.println("---------------------------------------------------");
    }

    public void ativarSensoresGlobais() {
        System.out.println("\n--- Ativando Sensores de Entidades no Ambiente ---");

        boolean algumSensorFoiAtivado = false;

        for (Entidade ent : listaDeEntidades) {
            if (ent instanceof Sensoreavel) {
                try {
                    ((Sensoreavel) ent).acionarSensores();
                    algumSensorFoiAtivado = true;
                }
                catch (RoboDesligadoException e) {
                    System.err.println("Falha ao ativar sensor da entidade '" + ent.getDescricao().split(" \\(")[0] + "': " + e.getMessage());
                }
            }
        }
        if (!algumSensorFoiAtivado) {
            System.out.println("Nenhuma entidade com sensores (que pudessem ser ativados) foi encontrada no ambiente.");
        }
        System.out.println("--------------------------------------------------");
    }

    public List<Robo> getRobos() {
        return listaDeEntidades.stream().filter(e -> e instanceof Robo).map(e -> (Robo) e).collect(Collectors.toList());
    }

    public List<Obstaculo> getObstaculosList() {
        return listaDeEntidades.stream().filter(e -> e instanceof Obstaculo).map(e -> (Obstaculo) e).collect(Collectors.toList());
    }

    public ArrayList<Entidade> getEntidadesInternalListForModification() {
        return this.listaDeEntidades;
    }

    public TipoEntidade[][][] getMapaRawView() {
        return this.mapaOcupacao;
    }
}