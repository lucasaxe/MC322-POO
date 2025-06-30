package simulador.entidades;

import simulador.core.Ambiente;
import simulador.core.Entidade;
import simulador.enums.EstadoRobo;
import simulador.enums.TipoEntidade;
import simulador.excecoes.*;
import simulador.entidades.ControleMovimento; // Import do subsistema
import simulador.sensores.Sensor;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe base para todos os robôs, agora usando composição para o subsistema de movimento.
 */
public abstract class Robo implements Entidade {
    protected String id;
    protected String nome;
    protected int x, y, z;
    protected EstadoRobo estado;
    protected final List<Sensor> sensores;
    protected ControleMovimento controleMovimento; // Subsistema para movimento

    public Robo(String id, String nome, int x, int y, int z) {
        this.id = id; this.nome = nome;
        this.x = x; this.y = y; this.z = z;
        this.estado = EstadoRobo.DESLIGADO;
        this.sensores = new ArrayList<>();
    }

    /**
     * Inicializa subsistemas que dependem de uma instância do ambiente.
     * Deve ser chamado após o robô ser adicionado ao ambiente.
     */
    public void inicializarSubsistemas(Ambiente ambiente) {
        this.controleMovimento = new ControleMovimento(this, ambiente);
    }

    @Override public int getX() { return this.x; }
    @Override public int getY() { return this.y; }
    @Override public int getZ() { return this.z; }
    @Override public void setPosicao(int x, int y, int z) { this.x = x; this.y = y; this.z = z; }
    @Override public simulador.enums.TipoEntidade getTipo() { return simulador.enums.TipoEntidade.ROBO; }
    @Override public String getDescricao() { return String.format("Robo: %s (ID: %s) em (%d,%d,%d) - Estado: %s", nome, id, x, y, z, estado); }
    @Override public char getRepresentacao() { return nome != null && !nome.isEmpty() ? Character.toUpperCase(nome.charAt(0)) : 'R'; }

    public String getId() { return id; }
    public String getNome() { return nome; }
    public EstadoRobo getEstado() { return estado; }

    public void ligar() { if (this.estado != EstadoRobo.LIGADO) { this.estado = EstadoRobo.LIGADO; System.out.println(nome + " ligado."); } }
    public void desligar() { if (this.estado != EstadoRobo.DESLIGADO) { this.estado = EstadoRobo.DESLIGADO; System.out.println(nome + " desligado."); } }
    public void adicionarSensor(Sensor sensor) { this.sensores.add(sensor); }
    public List<Sensor> getSensores() { return this.sensores; }

    /**
     * Ação padrão do robô, deve ser implementada pelas subclasses.
     */
    public abstract void executarTarefa() throws RoboDesligadoException, AcaoNaoPermitidaException;

    /**
     * O método de mover agora delega para o subsistema de controle de movimento.
     */
    public void solicitarMoverPara(int novoX, int novoY, int novoZ)
            throws RoboDesligadoException, ForaDosLimitesException, ColisaoException, AcaoNaoPermitidaException {
        if (this.controleMovimento == null) throw new IllegalStateException("Subsistema de movimento não inicializado para " + nome);
        this.controleMovimento.moverPara(novoX, novoY, novoZ);
    }

    @Override public String toString() { return getDescricao(); }
}