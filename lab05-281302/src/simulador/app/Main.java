package simulador.app;

import simulador.core.Ambiente;
import simulador.entidades.Robo;
import simulador.entidades.AgenteInteligente;
import simulador.excecoes.*;
import simulador.interfaces.Comunicavel;
import simulador.interfaces.FuncionalidadesEspecificas;
import simulador.interfaces.Missao;
import simulador.interfaces.Sensoreavel;
import simulador.missao.MissaoBuscarPonto;
import simulador.missao.MissaoExplorar;
import simulador.utils.CentralComunicacao;
import simulador.utils.LeitorConfiguracao;
import simulador.utils.Logger;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * Classe principal para inicializar e interagir com o simulador,
 * combinando controle manual e missões autônomas.
 */
public class Main {
    private static Ambiente ambienteSimulado;
    private static CentralComunicacao centralDeMsgs;
    private static Scanner leitor = new Scanner(System.in);
    private static Logger logger = new Logger();

    public static void main(String[] args) {
        ambienteSimulado = new Ambiente(20, 20, 10);
        centralDeMsgs = new CentralComunicacao();

        LeitorConfiguracao.carregar("config.txt", ambienteSimulado);

        for(Robo r : ambienteSimulado.getRobos()){
            r.inicializarSubsistemas(ambienteSimulado);
        }

        loopMenuPrincipal();
        leitor.close();
        System.out.println("Simulação finalizada.");
    }

    private static void loopMenuPrincipal() {
        int escolhaUsuario;
        do {
            System.out.println("\n====== MENU PRINCIPAL ======");
            System.out.println("1. Visualizar Mapa do Ambiente");
            System.out.println("2. Listar Robôs e Missões");
            System.out.println("--- Controle Autônomo ---");
            System.out.println("3. Atribuir Missão a um Agente");
            System.out.println("4. Executar Missão de um Agente");
            System.out.println("--- Controle Manual ---");
            System.out.println("5. Interagir Manualmente com um Robô"); // OPÇÃO REINTEGRADA
            System.out.println("6. Ver Histórico de Comunicação");
            System.out.println("0. Sair");
            System.out.print("Sua escolha: ");

            try {
                escolhaUsuario = leitor.nextInt(); leitor.nextLine();
                switch (escolhaUsuario) {
                    case 1: ambienteSimulado.visualizarMapaConsole(); break;
                    case 2: listarRobos(); break;
                    case 3: menuAtribuirMissao(); break;
                    case 4: menuExecutarMissao(); break;
                    case 5: menuInteracaoComRoboEscolhido(); break; // CHAMADA DO MENU MANUAL
                    case 6: centralDeMsgs.exibirMensagens(); break;
                    case 0: System.out.println("Encerrando..."); break;
                    default: System.out.println("Opção inválida.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Digite um número.");
                leitor.nextLine();
                escolhaUsuario = -1;
            }
        } while (escolhaUsuario != 0);
    }

    // (Os métodos menuAtribuirMissao e menuExecutarMissao permanecem os mesmos)
    // ...

    /**
     * NOVO: Menu para interação manual, baseado no do Lab 4.
     */
    private static void menuInteracaoComRoboEscolhido() {
        System.out.println("\n--- Interação Manual ---");
        Robo roboEscolhido = escolherRoboDaLista("interagir");
        if (roboEscolhido == null) return;

        int escolhaAcao;
        do {
            System.out.println("\n--- Ações para: " + roboEscolhido.getNome() + " ---");
            System.out.println("1. Ligar Robô");
            System.out.println("2. Desligar Robô");
            System.out.println("3. Mover Robô");
            System.out.println("4. Enviar Mensagem (se Comunicável)");
            System.out.println("5. Acionar Sensores (se Sensoreável)");
            System.out.println("6. Acessar Funcionalidades Específicas");
            System.out.println("0. Voltar ao Menu Principal");
            System.out.print("Escolha a ação: ");
            escolhaAcao = leitor.nextInt(); leitor.nextLine();

            try {
                switch (escolhaAcao) {
                    case 1: roboEscolhido.ligar(); break;
                    case 2: roboEscolhido.desligar(); break;
                    case 3: menuMoverRoboInterativo(roboEscolhido); break;
                    case 4:
                        if (roboEscolhido instanceof Comunicavel) {
                            menuEnviarMensagemInterativa((Comunicavel) roboEscolhido);
                        } else {
                            System.out.println("Este robô não tem capacidade de comunicação.");
                        }
                        break;
                    case 5:
                        if (roboEscolhido instanceof Sensoreavel) {
                            ((Sensoreavel) roboEscolhido).acionarSensores();
                        } else {
                            System.out.println("Este robô não possui sensores para acionar.");
                        }
                        break;
                    case 6:
                        if (roboEscolhido instanceof FuncionalidadesEspecificas) {
                            FuncionalidadesEspecificas fe = (FuncionalidadesEspecificas) roboEscolhido;
                            System.out.println("\n--- " + fe.getNomeMenuFuncionalidades() + " ---");
                            fe.apresentarMenuFuncionalidades();
                            System.out.print("Escolha a funcionalidade: ");
                            int opEsp = leitor.nextInt(); leitor.nextLine();
                            fe.executarFuncionalidade(opEsp, leitor, ambienteSimulado, centralDeMsgs);
                        } else {
                            System.out.println("Este robô não possui funcionalidades específicas.");
                        }
                        break;
                    case 0: break;
                    default: System.out.println("Opção inválida.");
                }
            } catch (Exception e) {
                System.err.println("ERRO ao executar ação no robô: " + e.getMessage());
            }
        } while (escolhaAcao != 0);
    }

    private static void menuMoverRoboInterativo(Robo robo) throws RoboDesligadoException, ForaDosLimitesException, ColisaoException, AcaoNaoPermitidaException {
        System.out.println("Comandos de Movimento: F (Frente), T (Trás), D (Direita), E (Esquerda), C (Cima), B (Baixo)");
        System.out.print("Digite o comando: ");
        String comando = leitor.nextLine().toUpperCase();
        int deltaX = 0, deltaY = 0, deltaZ = 0;
        switch (comando) {
            case "F": deltaY = 1; break;
            case "T": deltaY = -1; break;
            case "D": deltaX = 1; break;
            case "E": deltaX = -1; break;
            case "C": deltaZ = 1; break;
            case "B": deltaZ = -1; break;
            default: System.out.println("Comando inválido."); return;
        }
        robo.solicitarMoverPara(robo.getX() + deltaX, robo.getY() + deltaY, robo.getZ() + deltaZ);
        System.out.println(robo.getNome() + " movido para nova posição.");
    }

    private static void menuEnviarMensagemInterativa(Comunicavel remetente) throws RoboDesligadoException, ErroComunicacaoException {
        Robo roboRemetente = (Robo) remetente;
        System.out.println("\n--- Enviar Mensagem de " + roboRemetente.getNome() + " ---");

        List<Robo> potenciaisDestinatarios = ambienteSimulado.getRobos().stream()
                .filter(r -> r instanceof Comunicavel && r != roboRemetente)
                .collect(Collectors.toList());

        if (potenciaisDestinatarios.isEmpty()) {
            System.out.println("Nenhum outro robô comunicável disponível.");
            return;
        }

        System.out.println("Escolha o destinatário:");
        for (int i = 0; i < potenciaisDestinatarios.size(); i++) {
            System.out.printf("%d. %s\n", i + 1, potenciaisDestinatarios.get(i).getNome());
        }
        System.out.print("Número do destinatário: ");
        int numDest = leitor.nextInt(); leitor.nextLine();

        if (numDest > 0 && numDest <= potenciaisDestinatarios.size()) {
            Comunicavel destinatario = (Comunicavel) potenciaisDestinatarios.get(numDest - 1);
            System.out.print("Digite a mensagem: ");
            String texto = leitor.nextLine();
            remetente.enviarMensagem(destinatario, texto, centralDeMsgs);
            System.out.println("Mensagem enviada com sucesso!");
        } else {
            System.out.println("Seleção inválida.");
        }
    }

    private static Robo escolherRoboDaLista(String proposito) {
        List<Robo> robos = ambienteSimulado.getRobos();
        if (robos.isEmpty()) {
            System.out.println("Nenhum robô no ambiente para " + proposito + ".");
            return null;
        }

        System.out.println("Selecione um Robô para " + proposito + ":");
        for (int i = 0; i < robos.size(); i++) {
            System.out.printf("%d. %s (ID: %s)\n", i + 1, robos.get(i).getNome(), robos.get(i).getId());
        }
        System.out.print("Escolha o número do robô (ou 0 para cancelar): ");
        int escolha = leitor.nextInt(); leitor.nextLine();

        if (escolha > 0 && escolha <= robos.size()) {
            return robos.get(escolha - 1);
        }
        return null;
    }

    // O método listarRobos() pode ser mantido como antes.
    private static void listarRobos() {
        List<Robo> robos = ambienteSimulado.getRobos();
        if(robos.isEmpty()){
            System.out.println("Nenhum robô no ambiente.");
            return;
        }
        System.out.println("\n--- Robôs no Ambiente ---");
        for(Robo r : robos){
            System.out.println(r.getDescricao());
            if(r instanceof AgenteInteligente && ((AgenteInteligente) r).temMissao()){
                System.out.println("  -> Missão atribuída!");
            }
        }
    }

    // Adicione estes métodos à sua classe simulador.app.Main

    /**
     * Apresenta um submenu para atribuir uma missão a um robô do tipo AgenteInteligente.
     */
    private static void menuAtribuirMissao() {
        System.out.println("\n--- Atribuir Missão a um Agente ---");
        AgenteInteligente agente = escolherAgenteDaLista();
        if (agente == null) {
            return; // Retorna se nenhum agente for escolhido ou se não houver agentes.
        }

        System.out.println("Escolha a missão para " + agente.getNome() + ":");
        System.out.println("1. Explorar Ambiente Aleatoriamente");
        System.out.println("2. Buscar Ponto Específico");
        System.out.print("Opção de missão: ");
        int escolhaMissao = leitor.nextInt();
        leitor.nextLine();

        Missao novaMissao = null;
        try {
            switch (escolhaMissao) {
                case 1:
                    System.out.print("Digite o número de passos para a exploração: ");
                    int passos = leitor.nextInt();
                    leitor.nextLine();
                    novaMissao = new MissaoExplorar(passos);
                    break;
                case 2:
                    System.out.print("Digite a coordenada X de destino: ");
                    int x = leitor.nextInt();
                    System.out.print("Digite a coordenada Y de destino: ");
                    int y = leitor.nextInt();
                    System.out.print("Digite a coordenada Z de destino: ");
                    int z = leitor.nextInt();
                    leitor.nextLine();
                    novaMissao = new MissaoBuscarPonto(x, y, z);
                    break;
                default:
                    System.out.println("Tipo de missão inválido.");
            }
        } catch (InputMismatchException e) {
            System.err.println("Entrada inválida. Por favor, digite um número.");
            leitor.nextLine(); // Limpa o buffer
        }

        if (novaMissao != null) {
            agente.definirMissao(novaMissao);
        }
    }

    /**
     * Apresenta um submenu para comandar um AgenteInteligente a executar sua missão atribuída.
     */
    private static void menuExecutarMissao() {
        System.out.println("\n--- Executar Missão de um Agente ---");
        AgenteInteligente agente = escolherAgenteDaLista();
        if (agente == null) {
            return;
        }

        if (!agente.temMissao()) {
            System.out.println("O robô " + agente.getNome() + " não tem uma missão atribuída. Atribua uma missão primeiro no menu.");
            return;
        }

        // Cria um nome de arquivo de log único para esta execução de missão.
        String nomeArquivoLog = "log_missao_" + agente.getId() + "_" + System.currentTimeMillis() + ".txt";
        logger.iniciarLog(nomeArquivoLog);

        try {
            // Garante que o robô esteja ligado antes de iniciar a missão.
            agente.ligar();
            // A execução da missão é "bloqueante" para este menu simples, ou seja, o programa espera ela terminar.
            agente.executarMissao(ambienteSimulado, logger);
        } finally {
            // Garante que o logger seja fechado mesmo que ocorra uma exceção na missão.
            logger.fecharLog();
            System.out.println("Log da missão salvo em: " + nomeArquivoLog);
        }
    }

    /**
     * Método auxiliar para listar e permitir a seleção apenas de robôs que são Agentes Inteligentes.
     * @return O AgenteInteligente selecionado pelo usuário, ou null.
     */
    private static AgenteInteligente escolherAgenteDaLista() {
        List<Robo> todosRobos = ambienteSimulado.getRobos();
        List<AgenteInteligente> agentes = new ArrayList<>();
        // Filtra a lista de todos os robôs para encontrar apenas os que são Agentes Inteligentes.
        for (Robo robo : todosRobos) {
            if (robo instanceof AgenteInteligente) {
                agentes.add((AgenteInteligente) robo);
            }
        }

        if (agentes.isEmpty()) {
            System.out.println("Nenhum Agente Inteligente disponível no ambiente para receber missões.");
            return null;
        }

        System.out.println("Selecione um Agente Inteligente:");
        for (int i = 0; i < agentes.size(); i++) {
            System.out.printf("%d. %s (ID: %s)\n", i + 1, agentes.get(i).getNome(), agentes.get(i).getId());
        }
        System.out.print("Escolha o número do agente (ou 0 para cancelar): ");
        int escolha = leitor.nextInt();
        leitor.nextLine();

        if (escolha > 0 && escolha <= agentes.size()) {
            return agentes.get(escolha - 1);
        }
        System.out.println("Nenhuma seleção feita.");
        return null;
    }

}