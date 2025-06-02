package simulador.app;

import simulador.core.Ambiente;
import simulador.entidades.Obstaculo;
import simulador.entidades.Robo;
import simulador.entidades.robos.*;
import simulador.enums.EstadoRobo;
import simulador.enums.TipoObstaculo;
import simulador.excecoes.*;
import simulador.interfaces.*;
import simulador.sensores.*;
import simulador.utils.CentralComunicacao;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.InputMismatchException;
import java.util.stream.Collectors;

/**
 * Classe principal que inicia e gerencia o simulador de robôs.
 * É responsável por:
 * 1. Inicializar o ambiente e as entidades (robôs, obstáculos).
 * 2. Apresentar um menu interativo para o usuário.
 * 3. Processar as ações do usuário e interagir com o simulador.
 */
public class Main {
    private static Ambiente ambienteSimulado;
    private static CentralComunicacao centralDeMsgs;
    private static Scanner leitor = new Scanner(System.in);

    public static void main(String[] args) {
        ambienteSimulado = new Ambiente(15, 10, 5);
        centralDeMsgs = new CentralComunicacao();

        configurarCenarioInicial();

        loopMenuPrincipal();

        leitor.close();
        System.out.println("Simulação finalizada.");
    }

    /**
     * Cria e adiciona os robôs e obstáculos iniciais ao ambiente.
     */
    private static void configurarCenarioInicial() {
        System.out.println("Configurando o cenário inicial do simulador...");
        try {
            ambienteSimulado.adicionarEntidade(new Obstaculo(2, 2, 2, 2, 0, TipoObstaculo.ARVORE)); // Obstáculo pontual
            ambienteSimulado.adicionarEntidade(new Obstaculo(5, 3, 6, 4, 0, TipoObstaculo.PREDIO)); // Obstáculo com área 2x2 na base Z=0
            ambienteSimulado.adicionarEntidade(new Obstaculo(8, 1, 8, 1, 2, TipoObstaculo.BALAO)); // Obstáculo aéreo na altitude Z=2
            ambienteSimulado.adicionarEntidade(new Obstaculo(1, 5, 1, 5, 0, TipoObstaculo.BURACO)); // Um buraco no chão

            RoboTerrestre rt1 = new RoboTerrestre("RT01", "Fiat", 0, 0, 60);
            RoboEscavador esc1 = new RoboEscavador("ES01", "Bob", 3, 1, 40, 3); // profEscavacao=3
            RoboDrone dr1 = new RoboDrone("DR01", "Espiao", 1, 1, 2, 2, 4, 90); // altCruzeiro=2, tetoVoo=4, bateria=90%
            RoboAviao av1 = new RoboAviao("AV01", "Daora", 7, 7, 3, 3, 50, "Base Sul"); // altCruzeiro=3, capPass=50
            RoboCaminhao cm1 = new RoboCaminhao("CM01", "Thomas", 4,0,50, 1500); // capCarga=1500kg

            ambienteSimulado.adicionarEntidade(rt1);
            ambienteSimulado.adicionarEntidade(esc1);
            ambienteSimulado.adicionarEntidade(dr1);
            ambienteSimulado.adicionarEntidade(av1);
            ambienteSimulado.adicionarEntidade(cm1);

            rt1.adicionarSensor(new SensorBuracos(rt1, ambienteSimulado));
            dr1.adicionarSensor(new SensorAltitude(dr1));
            dr1.adicionarSensor(new SensorProximidadeAereo(dr1, ambienteSimulado));
            av1.adicionarSensor(new SensorAltitude(av1));
            esc1.adicionarSensor(new SensorTemperatura(esc1, ambienteSimulado));

            System.out.println("Cenário inicial configurado.");

        }
        catch (ColisaoException | ForaDosLimitesException e) {
            System.err.println("ERRO durante a configuração do cenário: " + e.getMessage());
        }
    }

    /**
     Controla o loop do menu principal, permitindo ao usuário escolher ações.
     */
    private static void loopMenuPrincipal() {
        int escolhaUsuario;
        do {
            System.out.println("\n====== MENU PRINCIPAL DO SIMULADOR ======");
            System.out.println("1. Visualizar Mapa do Ambiente");
            System.out.println("2. Listar Robôs (por tipo ou estado)");
            System.out.println("3. Interagir com um Robô Específico");
            System.out.println("4. Ativar Sensores de Todos os Robôs");
            System.out.println("5. Ver Histórico de Comunicação");
            System.out.println("0. Sair da Simulação");
            System.out.print("Sua escolha: ");

            try {
                escolhaUsuario = leitor.nextInt();
                leitor.nextLine();

                switch (escolhaUsuario) {
                    case 1: ambienteSimulado.visualizarMapaConsole(); break;
                    case 2: menuListarRobos(); break;
                    case 3: menuInteracaoComRoboEscolhido(); break;
                    case 4: ambienteSimulado.ativarSensoresGlobais(); break;
                    case 5: centralDeMsgs.exibirMensagens(); break;
                    case 0: System.out.println("Encerrando o simulador..."); break;
                    default: System.out.println("Opção inválida. Por favor, tente novamente.");
                }
            }
            catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Por favor, digite um número correspondente à opção do menu.");
                leitor.nextLine();
                escolhaUsuario = -1;
            }
        } while (escolhaUsuario != 0);
    }

    /**
     * Submenu para listar robôs com diferentes filtros.
     */
    private static void menuListarRobos() {
        List<Robo> todosOsRobos = ambienteSimulado.getRobos();

        if (todosOsRobos.isEmpty()) {
            System.out.println("Atualmente, não há robôs no ambiente.");
            return;
        }

        System.out.println("\n--- Opções de Listagem de Robôs ---");
        System.out.println("1. Listar Todos os Robôs");
        System.out.println("2. Listar Robôs por Tipo (ex: RoboDrone, RoboTerrestre)");
        System.out.println("3. Listar Robôs por Estado (LIGADO, DESLIGADO, etc.)");
        System.out.print("Escolha o tipo de listagem: ");

        int tipoListagem = leitor.nextInt();
        leitor.nextLine();

        List<Robo> robosParaExibir = new ArrayList<>();
        switch (tipoListagem) {
            case 1:
                robosParaExibir.addAll(todosOsRobos);

                break;
            case 2:
                System.out.print("Digite o nome da classe do tipo do robô (ex: RoboTerrestre, RoboAviao): ");
                String nomeDaClasse = leitor.nextLine();

                robosParaExibir = todosOsRobos.stream().filter(r -> r.getClass().getSimpleName().equalsIgnoreCase(nomeDaClasse)).collect(Collectors.toList());

                break;
            case 3:
                System.out.print("Digite o estado desejado (LIGADO, DESLIGADO, EM_TAREFA, AVARIADO): ");
                String estadoInput = leitor.nextLine().toUpperCase();

                try {
                    EstadoRobo estadoEscolhido = EstadoRobo.valueOf(estadoInput);
                    robosParaExibir = todosOsRobos.stream().filter(r -> r.getEstado() == estadoEscolhido).collect(Collectors.toList());
                }
                catch (IllegalArgumentException e) {
                    System.out.println("Estado '" + estadoInput + "' não reconhecido. Mostrando todos os robôs por padrão.");
                    robosParaExibir.addAll(todosOsRobos);
                }

                break;
            default:
                System.out.println("Opção de listagem inválida. Mostrando todos os robôs.");
                robosParaExibir.addAll(todosOsRobos);
        }

        if (robosParaExibir.isEmpty()) {
            System.out.println("Nenhum robô encontrado com os critérios de listagem fornecidos.");
        }
        else {
            robosParaExibir.sort(Comparator.comparing(Robo::getNome));

            System.out.println("\n--- Robôs Encontrados ---");

            for (Robo r : robosParaExibir) {
                System.out.println(r.getDescricao());
            }
        }
    }

    /**
     * Permite ao usuário escolher um robô da lista para interagir.
     * @param proposito Ação para a qual o robô está sendo escolhido (ex: "controlar", "enviar mensagem").
     * @return O Robô selecionado, ou null se a seleção for cancelada ou inválida.
     */
    private static Robo escolherRoboDaLista(String proposito) {
        List<Robo> robosDisponiveis = ambienteSimulado.getRobos();

        if (robosDisponiveis.isEmpty()) {
            System.out.println("Nenhum robô disponível no ambiente para " + proposito + ".");
            return null;
        }

        System.out.println("\n--- Escolha um Robô para " + proposito + " ---");

        for (int i = 0; i < robosDisponiveis.size(); i++) {
            Robo r = robosDisponiveis.get(i);
            System.out.printf("%d. %s (ID: %s, Tipo: %s, Estado: %s)\n", i + 1, r.getNome(), r.getId(), r.getClass().getSimpleName(), r.getEstado());
        }

        System.out.print("Digite o número do robô (ou 0 para cancelar/voltar): ");
        int numeroEscolhido = leitor.nextInt();
        leitor.nextLine();

        if (numeroEscolhido > 0 && numeroEscolhido <= robosDisponiveis.size()) {
            return robosDisponiveis.get(numeroEscolhido - 1);
        }

        System.out.println("Seleção cancelada ou inválida.");

        return null;
    }

    /**
     * Submenu para interagir com um robô específico escolhido pelo usuário.
     */
    private static void menuInteracaoComRoboEscolhido() {
        Robo roboEscolhido = escolherRoboDaLista("interagir");

        if (roboEscolhido == null) {
            return;
        }

        int escolhaAcaoRobo;
        do {
            System.out.println("\n--- Ações para o Robô: " + roboEscolhido.getNome() + " (ID: " + roboEscolhido.getId() + ") ---");
            System.out.println("1. Visualizar Status Detalhado do Robô");
            System.out.println("2. Ligar Robô");
            System.out.println("3. Desligar Robô");
            System.out.println("4. Mover Robô (Frente, Trás, Esquerda, Direita, Cima, Baixo)");
            System.out.println("5. Executar Tarefa Principal do Robô");

            int proximaOpcaoDinamica = 6;

            if (roboEscolhido instanceof Sensoreavel) {
                System.out.println(proximaOpcaoDinamica++ + ". Acionar Sensores do Robô"); // (Leitura de sensores)
            }
            if (roboEscolhido instanceof Comunicavel) {
                System.out.println(proximaOpcaoDinamica++ + ". Enviar Mensagem"); // (Comunicação)
            }
            if (roboEscolhido instanceof FuncionalidadesEspecificas) {
                System.out.println(proximaOpcaoDinamica++ + ". Acessar Menu de Funcionalidades Específicas"); // (Outras funcionalidades)
            }

            System.out.println("0. Voltar ao Menu Principal");
            System.out.print("Escolha a ação para o robô: ");
            escolhaAcaoRobo = leitor.nextInt();
            leitor.nextLine();

            try {
                int optCont = 6;

                if (escolhaAcaoRobo == 1) {
                    System.out.println(roboEscolhido.getDescricao());
                }
                else if (escolhaAcaoRobo == 2) {
                    roboEscolhido.ligar();
                }
                else if (escolhaAcaoRobo == 3) {
                    roboEscolhido.desligar();
                }
                else if (escolhaAcaoRobo == 4) {
                    menuMoverRoboInterativo(roboEscolhido);
                }
                else if (escolhaAcaoRobo == 5) {
                    roboEscolhido.executarTarefa();
                }
                else if (roboEscolhido instanceof Sensoreavel && escolhaAcaoRobo == optCont++) {
                    ((Sensoreavel)roboEscolhido).acionarSensores();
                }
                else if (roboEscolhido instanceof Comunicavel && escolhaAcaoRobo == optCont++) {
                    menuEnviarMensagemInterativa((Comunicavel)roboEscolhido);
                }
                else if (roboEscolhido instanceof FuncionalidadesEspecificas && escolhaAcaoRobo == optCont++) {
                    FuncionalidadesEspecificas fe = (FuncionalidadesEspecificas) roboEscolhido;
                    System.out.println("\n--- " + fe.getNomeMenuFuncionalidades() + " ---");
                    fe.apresentarMenuFuncionalidades();
                    System.out.print("Escolha a funcionalidade específica: ");
                    int opEsp = leitor.nextInt(); leitor.nextLine(); // Consome newline
                    fe.executarFuncionalidade(opEsp, leitor, ambienteSimulado, centralDeMsgs);
                }
                else if (escolhaAcaoRobo == 0) {
                    System.out.println("Retornando ao menu principal...");
                }
                else {
                    if (escolhaAcaoRobo >= 6 && escolhaAcaoRobo < optCont) {
                        System.out.println("Ação não aplicável para este robô ou opção inválida.");
                    }
                    else if (escolhaAcaoRobo !=0 ) {
                        System.out.println("Opção desconhecida para o robô.");
                    }
                }
            }
            catch (Exception e) {
                System.err.println("ERRO ao executar ação no robô: " + e.getMessage());
            }
        } while (escolhaAcaoRobo != 0);
    }

    /**
     * Submenu para controlar a movimentação básica de um robô.
     * @param robo O robô a ser movido.
     */
    private static void menuMoverRoboInterativo(Robo robo) {
        System.out.println("Movimentar Robô: " + robo.getNome() + " | Posição Atual: (" + robo.getX() + "," + robo.getY() + "," + robo.getZ() + ")");
        System.out.println("Comandos: F (Frente, Y+), T (Trás, Y-), D (Direita, X+), E (Esquerda, X-), C (Cima, Z+), B (Baixo, Z-), S (Sair do movimento)");
        System.out.print("Digite o comando de movimento: ");

        String comando = leitor.nextLine().toUpperCase();

        int deltaX = 0, deltaY = 0, deltaZ = 0;

        switch (comando) {
            case "F": deltaY = 1; break;
            case "T": deltaY = -1; break;
            case "D": deltaX = 1; break;
            case "E": deltaX = -1; break;
            case "C": deltaZ = 1; break;
            case "B": deltaZ = -1; break;
            case "S": System.out.println("Movimentação cancelada."); return;
            default: System.out.println("Comando de movimento inválido."); return;
        }

        try {
            int proximaX = robo.getX() + deltaX;
            int proximaY = robo.getY() + deltaY;
            int proximaZ = robo.getZ() + deltaZ;

            if (robo instanceof RoboTerrestre && deltaZ != 0) {
                System.out.println("Robôs terrestres como " + robo.getNome() + " operam em Z fixo. Movimento vertical será ignorado ou ajustado pela classe do robô.");
                proximaZ = robo.getZ();
            }

            robo.solicitarMoverPara(proximaX, proximaY, proximaZ, ambienteSimulado);
            System.out.println(robo.getNome() + " movido para nova posição: (" + robo.getX() + "," + robo.getY() + "," + robo.getZ() + ")");
        }
        catch (RoboDesligadoException | ForaDosLimitesException | ColisaoException | AcaoNaoPermitidaException e) {
            System.err.println("Falha ao mover o robô: " + e.getMessage());
        }
    }

    /**
     * Submenu para enviar uma mensagem de um robô (remetente) para outro (destinatário).
     * @param remetente O robô que implementa Comunicavel e que enviará a mensagem.
     */
    private static void menuEnviarMensagemInterativa(Comunicavel remetente) {
        Robo roboRemetente = (Robo) remetente;
        System.out.println("\n--- Enviar Mensagem de " + roboRemetente.getNome() + " ---");

        List<Robo> potenciaisDestinatarios = ambienteSimulado.getRobos().stream().filter(r -> r instanceof Comunicavel && r != roboRemetente).collect(Collectors.toList());

        if (potenciaisDestinatarios.isEmpty()) {
            System.out.println("Nenhum outro robô comunicável disponível no ambiente para receber mensagens.");
            return;
        }

        System.out.println("Escolha o destinatário da mensagem:");

        for (int i = 0; i < potenciaisDestinatarios.size(); i++) {
            Robo rDest = potenciaisDestinatarios.get(i);
            System.out.printf("%d. %s (ID de Comunicação: %s)\n", i + 1, rDest.getNome(), ((Comunicavel)rDest).getIdComunicacao());
        }

        System.out.print("Digite o número do destinatário (ou 0 para cancelar): ");

        int numeroDestinatario = leitor.nextInt();
        leitor.nextLine();

        if (numeroDestinatario > 0 && numeroDestinatario <= potenciaisDestinatarios.size()) {
            Comunicavel destinatarioEscolhido = (Comunicavel) potenciaisDestinatarios.get(numeroDestinatario - 1);
            System.out.print("Digite a mensagem a ser enviada: ");
            String textoDaMensagem = leitor.nextLine();
            try {
                remetente.enviarMensagem(destinatarioEscolhido, textoDaMensagem, centralDeMsgs);
                System.out.println("Mensagem enviada com sucesso!");
            }
            catch (RoboDesligadoException | ErroComunicacaoException e) {
                System.err.println("Falha ao enviar a mensagem: " + e.getMessage());
            }
        }
        else {
            System.out.println("Envio de mensagem cancelado.");
        }
    }
}