package simulador.entidades;

import simulador.core.Ambiente;
import simulador.enums.EstadoRobo;
import simulador.excecoes.ErroComunicacaoException;
import simulador.excecoes.RoboDesligadoException;
import simulador.interfaces.Comunicavel; // Importa a interface
import simulador.interfaces.Missao;
import simulador.utils.CentralComunicacao;
import simulador.utils.Logger;

/**
 * Classe abstrata para robôs autônomos.
 * Agora, também implementa a interface Comunicavel, dando a todos os agentes
 * a capacidade de enviar e receber mensagens.
 */
public abstract class AgenteInteligente extends Robo implements Comunicavel { // Adiciona "implements Comunicavel"
    protected Missao missao;

    public AgenteInteligente(String id, String nome, int x, int y, int z) {
        super(id, nome, x, y, z);
    }

    public void definirMissao(Missao m) {
        this.missao = m;
        System.out.println("Missão do tipo " + m.getClass().getSimpleName() + " atribuída ao robô " + getNome());
    }

    public boolean temMissao() {
        return this.missao != null;
    }

    public abstract void executarMissao(Ambiente ambiente, Logger logger);

    // --- IMPLEMENTAÇÃO DOS MÉTODOS DA INTERFACE COMUNICAVEL ---

    @Override
    public String getIdComunicacao() {
        // Gera um ID de comunicação único para o agente.
        return "AGENTE-" + this.id;
    }

    @Override
    public void enviarMensagem(Comunicavel destinatario, String mensagem, CentralComunicacao central)
            throws RoboDesligadoException, ErroComunicacaoException {
        if (this.estado == EstadoRobo.DESLIGADO) {
            throw new RoboDesligadoException(nome + " está desligado e não pode enviar mensagens.");
        }
        if (destinatario == null) {
            throw new ErroComunicacaoException("O destinatário da mensagem é nulo.");
        }

        // Formata a mensagem e a exibe no console.
        String msgFormatada = String.format("[De %s para %s]: %s", this.getIdComunicacao(), destinatario.getIdComunicacao(), mensagem);
        System.out.println(msgFormatada);

        // Registra a mensagem na central de comunicação.
        central.registrarMensagem(this.getIdComunicacao(), destinatario.getIdComunicacao(), mensagem);

        // Envia a mensagem para o destinatário.
        destinatario.receberMensagem(this.getIdComunicacao(), mensagem);
    }

    @Override
    public void receberMensagem(String remetenteId, String mensagem) throws RoboDesligadoException {
        if (this.estado == EstadoRobo.DESLIGADO) {
            throw new RoboDesligadoException(nome + " está desligado e não pode receber mensagens.");
        }
        System.out.println("[" + this.getIdComunicacao() + " recebeu de " + remetenteId + "]: " + mensagem);
    }
}