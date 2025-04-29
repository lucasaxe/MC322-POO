import java.util.Scanner;

public class RoboAviao extends RoboAereo implements FuncionalidadesEspecificas {
    private int quantidadePassageiros;
    private String destino;

    public RoboAviao(String nome, int x, int y, int z, int quantidadePassageiros, String destino) {
        super(nome, x, y, z);
        this.quantidadePassageiros = quantidadePassageiros;
        this.destino = destino;
    }

    public void embarcarPassageiros(int quantidade) {
        quantidadePassageiros += quantidade;
        System.out.println(quantidade + " passageiros embarcaram. Total: " + quantidadePassageiros);
    }

    @Override
    public void mostrarMenu() {
        System.out.println("\n             FUNCIONALIDADES DO AVIÃO");
        System.out.println("1. Embarcar passageiros");
        System.out.println("2. Ver informações de voo");
    }

    @Override
    public void executarAcao(int opcao, Scanner scanner) {
        switch (opcao) {
            case 1:
                System.out.print("Quantos passageiros vão embarcar? ");
                int passageiros = scanner.nextInt();
                embarcarPassageiros(passageiros);
                break;
            case 2:
                System.out.println("Destino: " + destino);
                System.out.println("Passageiros: " + quantidadePassageiros);
                break;
            default:
                System.out.println("Opção inválida!");
        }
    }

    @Override
    public String getTipo() {
        return "Avião";
    }
}