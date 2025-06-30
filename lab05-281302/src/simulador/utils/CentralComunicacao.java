package simulador.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Responsável por registrar o histórico de mensagens trocadas entre robôs Comunicaveis. [cite: 33]
 * Funciona como um log centralizado de comunicação.
 */
public class CentralComunicacao {
    private final List<String> historicoMensagens;

    public CentralComunicacao() {
        this.historicoMensagens = new ArrayList<>();
    }

    public synchronized void registrarMensagem(String remetenteId, String destinatarioId, String msg) {
        String logFormatado = String.format("De: %s | Para: %s | Mensagem: %s", remetenteId, (destinatarioId != null ? destinatarioId : "Broadcast"), msg);
        this.historicoMensagens.add(logFormatado);
    }

    public void exibirMensagens() {
        if (historicoMensagens.isEmpty()) {
            System.out.println("\n[Central de Comunicação] Nenhum histórico de mensagens para exibir.");
            return;
        }
        System.out.println("\n--- Histórico de Mensagens da Central ---");
        for (String log : historicoMensagens) {
            System.out.println(log);
        }
        System.out.println("-----------------------------------------");
    }
}