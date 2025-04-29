import java.util.Scanner;

public class RoboCaminhao extends RoboTerrestre implements FuncionalidadesEspecificas {
    private int pesoCarga;

    public RoboCaminhao(String nome, int x, int y, int velocidadeMaxima, int pesoCarga) {
        super(nome, x, y, velocidadeMaxima);
        this.pesoCarga = pesoCarga;
    }

    public void carregar(int peso) {
        this.pesoCarga += peso;
        System.out.println(nome + " carregou " + peso + " kg");
    }


    @Override
    public void mover(int dx, int dy, int dz) {
        if(pesoCarga>20){
            super.mover(dx*10/pesoCarga, dy*10/pesoCarga, dz*10/pesoCarga);
        }else {
            super.mover(dx / 2, dy / 2, dz / 2);
        }
        System.out.println("O Robô Caminhão se move com velocidade reduzida");
    }

    @Override
    public void mostrarMenu() {
        System.out.println("\n            FUNCIONALIDADES DO CAMINHÃO");
        System.out.println("1. Carregar peso");
        System.out.println("2. Ver carga atual");
    }

    @Override
    public void executarAcao(int opcao, Scanner scanner) {
        switch (opcao) {
            case 1:
                System.out.print("Digite o peso a carregar (kg): ");
                int peso = scanner.nextInt();
                carregar(peso);
                break;
            case 2:
                System.out.println("Carga atual: " + pesoCarga + "kg");
                break;
            default:
                System.out.println("Opção inválida!");
        }
    }

    @Override
    public String getTipo() {
        return "Caminhão";
    }
}