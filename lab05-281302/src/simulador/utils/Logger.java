package simulador.utils;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * [cite_start]Classe utilitária para registrar logs de execução em um arquivo de texto. [cite: 76]
 */
public class Logger {
    private PrintWriter writer;
    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

    public void iniciarLog(String nomeArquivo) {
        try {
            this.writer = new PrintWriter(new FileWriter(nomeArquivo, false)); // false para sobrescrever o arquivo
            log("Log de Missão Iniciado");
        } catch (IOException e) {
            System.err.println("Erro ao iniciar o logger: " + e.getMessage());
        }
    }

    public void log(String mensagem) {
        if (writer != null) {
            String logComTimestamp = "[" + dtf.format(LocalDateTime.now()) + "] " + mensagem;
            writer.println(logComTimestamp);
            System.out.println("LOG -> " + mensagem); // Feedback no console
        }
    }

    public void fecharLog() {
        if (writer != null) {
            log("Log de Missão Finalizado");
            writer.flush();
            writer.close();
            writer = null;
        }
    }
}