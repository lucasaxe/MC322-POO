package simulador.entidades;

import simulador.core.Ambiente;
import simulador.core.Entidade;
import simulador.enums.EstadoRobo;
import simulador.enums.TipoEntidade;
import simulador.excecoes.*;
import simulador.sensores.Sensor;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe base para todos os robôs do simulador.
 * Sendo 'abstract', não podemos criar um "Robo" genérico, apenas seus tipos especializados
 * (como RoboTerrestre, RoboAereo).
 * Implementa 'Entidade' porque um robô é algo que existe no ambiente.
 */
public abstract class Robo implements Entidade {
    protected String id;
    protected String nome;
    protected int x, y, z;
    protected EstadoRobo estado;
    protected final ArrayList<Sensor> sensores;

    public Robo(String id, String nome, int x, int y, int z) {
        this.id = id;
        this.nome = nome;
        this.x = x;
        this.y = y;
        this.z = z;
        this.estado = EstadoRobo.DESLIGADO;
        this.sensores = new ArrayList<>();
    }

    @Override public int getX() { return this.x; }
    @Override public int getY() { return this.y; }
    @Override public int getZ() { return this.z; }

    @Override
    public void setPosicao(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override public TipoEntidade getTipo() { return TipoEntidade.ROBO; }

    @Override
    public String getDescricao() {
        return String.format("Robo: %s (ID: %s, Tipo: %s) em (%d,%d,%d) - Estado: %s", nome, id, getClass().getSimpleName(), x, y, z, estado);
    }

    @Override
    public char getRepresentacao() {
        return nome != null && !nome.isEmpty() ? Character.toUpperCase(nome.charAt(0)) : 'R';
    }

    public String getId() { return id; }
    public String getNome() { return nome; }
    public EstadoRobo getEstado() { return estado; }

    public void ligar() {
        if (this.estado != EstadoRobo.LIGADO) {
            this.estado = EstadoRobo.LIGADO;
            System.out.println(nome + " ligado.");
        }
        else {
            System.out.println(nome + " já está ligado.");
        }
    }

    public void desligar() {
        if (this.estado != EstadoRobo.DESLIGADO) {
            this.estado = EstadoRobo.DESLIGADO;
            System.out.println(nome + " desligado.");
        }
        else {
            System.out.println(nome + " já está desligado.");
        }
    }

    /**
     * Método abstrato para ações específicas de cada tipo de robô.
     * Cada subclasse (RoboTerrestre, RoboDrone, etc.) deve fornecer sua própria
     * implementação do que significa "executar tarefa".
     */
    public abstract void executarTarefa() throws RoboDesligadoException, AcaoNaoPermitidaException;

    // Gerenciamento de sensores
    public void adicionarSensor(Sensor sensor) { this.sensores.add(sensor); }
    public boolean possuiSensores() { return !this.sensores.isEmpty(); }
    public List<Sensor> getSensores() { return this.sensores; }

    /**
     * Solicita ao Ambiente para mover o robô para uma nova posição.
     * O robô não altera diretamente o mapa; ele pede ao Ambiente.
     * A classe Ambiente validará o movimento (limites, colisões).
     */
    public void solicitarMoverPara(int novoX, int novoY, int novoZ, Ambiente ambiente) throws RoboDesligadoException, ForaDosLimitesException, ColisaoException, AcaoNaoPermitidaException {
        if (this.estado == EstadoRobo.DESLIGADO) {
            throw new RoboDesligadoException(nome + " está desligado, não pode mover.");
        }

        ambiente.moverEntidade(this, novoX, novoY, novoZ);
    }

    @Override
    public String toString() {
        return getDescricao();
    }
}