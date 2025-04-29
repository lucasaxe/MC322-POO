import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

public class Main{
    public static void main(String[] args){
        //criando o ambiente
        Ambiente ambiente = new Ambiente(100, 100, 100);

        //adicionando obstaculos
        ambiente.getObstaculos().add(new Obstaculo(10, 12, 10, 12, TipoObstaculo.ARVORE));
        ambiente.getObstaculos().add(new Obstaculo(30, 40, 60, 70, TipoObstaculo.PREDIO));
        ambiente.getObstaculos().add(new Obstaculo(5, 5, 70, 70, TipoObstaculo.BURACO));

        ambiente.getObstaculos().add(new Obstaculo(30, 32, 30, 32, TipoObstaculo.PASSAROS));
        ambiente.getObstaculos().add(new Obstaculo(60, 60, 75, 75, TipoObstaculo.BALAO));
        ambiente.getObstaculos().add(new Obstaculo(80, 85, 50, 60, TipoObstaculo.NUVEM));

        ambiente.getObstaculos().add(new Obstaculo(40, 45, 40, 45, TipoObstaculo.VULCAO));
        ambiente.getObstaculos().add(new Obstaculo(90, 95, 10, 15, TipoObstaculo.GELEIRA));

        //criando robos
        RoboTerrestre roboTerrestre = new RoboTerrestre("Relampago McQueen", 0, 0, 40); //z sempre setado em 0 em terrestres
        RoboEscavador escavador = new RoboEscavador("Escavador Brabo", 40, 40, 0, 5, ambiente);
        RoboCaminhao caminhao = new RoboCaminhao("Caminhoneiro", 75, 75, 0, 0);

        RoboAereo roboAereo = new RoboAereo("Tom Cruise", 0, 0, 20);
        RoboAviao aviao = new RoboAviao("Jato do XMEN", 20, 20, 80, 150, "Nova York");
        RoboDrone drone = new RoboDrone("Drone Espiao", 60, 60, 20, 40, 120);

        //adicionando sensores
        roboTerrestre.adicionarSensor(new SensorTemperatura(roboTerrestre, ambiente));
        roboTerrestre.adicionarSensor(new SensorBuracos(roboTerrestre, ambiente));

        escavador.adicionarSensor(new SensorBuracos(escavador, ambiente));
        caminhao.adicionarSensor(new SensorTemperatura(caminhao, ambiente));

        roboAereo.adicionarSensor(new SensorAltitude(roboAereo));
        roboAereo.adicionarSensor(new SensorProximidadeAereo(roboAereo, ambiente));

        drone.adicionarSensor(new SensorAltitude(drone));
        aviao.adicionarSensor(new SensorProximidadeAereo(aviao, ambiente));


        //testes de adicionar e remover robos do ambiente
        RoboTerrestre roboTerrestre1 = new RoboTerrestre("AAAAAAAAA", 0, 0, 0); //z sempre setado em 0 em terrestres
        RoboAereo roboAereo1 = new RoboAereo("BBBBBBBB", 0, 0, 90);
        ambiente.adicionarRobo(roboTerrestre1);
        ambiente.adicionarRobo(roboAereo1);
        ambiente.removerRobo(roboTerrestre1);
        ambiente.removerRobo(roboAereo1);

        //adicionando os robos ao ambiente
        ambiente.adicionarRobo(roboTerrestre);
        ambiente.adicionarRobo(escavador);
        ambiente.adicionarRobo(caminhao);

        ambiente.adicionarRobo(roboAereo);
        ambiente.adicionarRobo(aviao);
        ambiente.adicionarRobo(drone);

        //começo do menu
        Scanner scanner = new Scanner(System.in);
        int opcao;

        do {
            System.out.print("\n———————————————————————————————————————————————————————————");
            System.out.println("\n                          MENU");
            System.out.println("1. Visualizar status do ambiente");
            System.out.println("2. Mover robô");
            System.out.println("3. Ativar sensor");
            System.out.println("4. Detectar colisões");
            System.out.println("5. Funcionalidades específicas");
            System.out.println("0. Sair");
            System.out.println("\n———————————————————————————————————————————————————————————");
            System.out.print("Escolha uma opção: ");

            opcao = scanner.nextInt();

            switch (opcao) {
                case 1:
                    visualizarStatus(ambiente);
                    break;

                case 2:
                    moverRobo(ambiente, scanner);
                    break;

                case 3:
                    ativarSensor(ambiente, scanner);
                    break;

                case 4:
                    ambiente.detectarColisoes();
                    break;

                case 5:
                    funcionalidadesEspecificas(ambiente, scanner);
                    break;

                case 0:
                    System.out.println("Encerrando simulador...");
                    break;

                default:
                    System.out.println("Opção inválida!");
            }
        } while (opcao != 0);

        scanner.close();
    }

    private static void visualizarStatus(Ambiente ambiente) {
        System.out.println("\n                        AMBIENTE");
        System.out.println("Dimensões: " + ambiente.getComprimento() + "x" + ambiente.getLargura() + "x" + ambiente.getAltura());
        System.out.println("\nRobôs no ambiente:");
        ambiente.getRobos().forEach(System.out::println);
        System.out.println("\nObstáculos no ambiente:");
        ambiente.getObstaculos().forEach(System.out::println);
    }

    private static void moverRobo(Ambiente ambiente, Scanner scanner) {
        System.out.println("\n              SELECIONE O ROBÔ PARA MOVER");
        List<Robo> robos = ambiente.getRobos();

        for (int i = 0; i < robos.size(); i++) {
            Robo robo = robos.get(i);
            System.out.println(i+1 + ". " +robo.getNome() + " (" +robo.getClass().getSimpleName() +") está na posição (" + robo.getX() + "," + robo.getY() + ")" );
        }

        System.out.print("\nEscolha o robô: ");
        int escolha = scanner.nextInt() - 1;

        if (escolha < 0 || escolha >= robos.size()) {
            System.out.println("Opção inválida!");
            return;
        }

        Robo robo = robos.get(escolha);

        System.out.print("Digite o movimento em X: ");
        int dx = scanner.nextInt();
        System.out.print("Digite o movimento em Y: ");
        int dy = scanner.nextInt();

        int dz = 0;

        if (robo instanceof RoboAereo) {
            System.out.print("Digite o movimento em Z: ");
            dz = scanner.nextInt();
        }

        if (ambiente.dentroDosLimites(robo.getX() + dx, robo.getY() + dy, robo.getZ() + dz)) {
            robo.mover(dx, dy, dz);
        } else {
            System.out.println("Movimento fora dos limites do ambiente!");
        }
    }

    private static void ativarSensor(Ambiente ambiente, Scanner scanner) {
        System.out.println("\n       SELECIONE O ROBÔ PARA ATIVAR SENSORES");

        List<Robo> robosComSensores = new ArrayList<>();
        for (Robo robo : ambiente.getRobos()) {
            if (robo.possuiSensores()) {
                robosComSensores.add(robo);
            }
        }

        if (robosComSensores.isEmpty()) {
            System.out.println("Nenhum robô com sensores encontrado!");
            return;
        }

        for (int i = 0; i < robosComSensores.size(); i++) {
            Robo robo = robosComSensores.get(i);
            System.out.println(i+1 + ". " +robo.getNome() + "(" +robo.getClass().getSimpleName() +")");
        }

        System.out.print("\nEscolha o robô: ");
        int escolhaRobo = scanner.nextInt() - 1;

        if (escolhaRobo < 0 || escolhaRobo >= robosComSensores.size()) {
            System.out.println("Opção inválida!");
            return;
        }

        Robo robo = robosComSensores.get(escolhaRobo);
        List<Sensor> sensores = robo.getSensores();

        if (sensores.isEmpty()) {
            System.out.println("Este robô não possui sensores!");
            return;
        }

        System.out.println("\n               SENSORES DISPONÍVEIS");
        for (int i = 0; i < sensores.size(); i++) {
            System.out.println((i + 1) + ". " + sensores.get(i).getClass().getSimpleName());
        }

        System.out.print("\nEscolha o sensor para ativar: ");
        int sensorEscolhido = scanner.nextInt() - 1;

        if (sensorEscolhido < 0 || sensorEscolhido >= sensores.size()) {
            System.out.println("Opção inválida!");
            return;
        }

        sensores.get(sensorEscolhido).monitorar();
    }

    private static void funcionalidadesEspecificas(Ambiente ambiente, Scanner scanner) {
        System.out.println("\n            FUNCIONALIDADES ESPECÍFICAS");
        List<Robo> robos = ambiente.getRobos();

        int count = 1;
        for (Robo robo : robos) {
            if (robo instanceof FuncionalidadesEspecificas) {
                System.out.println(count + ". " + robo.getNome() + " (" + ((FuncionalidadesEspecificas)robo).getTipo() + ")");
                count++;
            }
        }

        if (count == 1) {
            System.out.println("Nenhum robô com funcionalidades específicas encontrado!");
            return;
        }

        System.out.print("\nEscolha o robô: ");
        int escolha = scanner.nextInt() - 1;

        //encontrar o robô selecionado
        Robo roboSelecionado = null;
        int current = 0;
        for (Robo robo : robos) {
            if (robo instanceof FuncionalidadesEspecificas) {
                if (current == escolha) {
                    roboSelecionado = robo;
                    break;
                }
                current++;
            }
        }

        if (roboSelecionado == null) {
            System.out.println("Opção inválida!");
            return;
        }

        FuncionalidadesEspecificas funcionalidade = (FuncionalidadesEspecificas) roboSelecionado;

        funcionalidade.mostrarMenu();
        System.out.print("\nEscolha: ");

        int opcao = scanner.nextInt();
        funcionalidade.executarAcao(opcao, scanner);
    }
}