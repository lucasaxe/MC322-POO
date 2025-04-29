import java.util.Scanner;

public class RoboDrone extends RoboAereo implements FuncionalidadesEspecificas {
    private int altitudeMaxima, quantidadeBateria;

    public RoboDrone(String nome, int x, int y, int z, int altitudeMaxima, int quantidadeBateria) {
        super(nome, x, y, z);
        this.altitudeMaxima = altitudeMaxima;
        this.quantidadeBateria = quantidadeBateria;
    }

    public void verificarBateria() {
        System.out.println(nome + " tem " + quantidadeBateria + " minutos de bateria restantes");
    }

    public void verificarAltitude() {
        System.out.println(nome + " sobe até " + altitudeMaxima + " metros");
    }

    @Override
    public void mover(int dx, int dy, int dz) {
        if(z + dz > altitudeMaxima){
            super.mover(dx, dy, altitudeMaxima - z);
            System.out.println("O Robô Drone só consegue subir até "+ altitudeMaxima + " metros");
        }else {
            super.mover(dx, dy, dz);
        }

    }

    @Override
    public void mostrarMenu() {
        System.out.println("\n             FUNCIONALIDADES DO DRONE");
        System.out.println("1. Verificar bateria");
        System.out.println("2. Verificar a altitude máxima");
    }

    @Override
    public void executarAcao(int opcao, Scanner scanner) {
        switch (opcao) {
            case 1:
                verificarBateria();
                break;
            case 2:
                verificarAltitude();
                break;
            default:
                System.out.println("Opção inválida!");
        }
    }

    @Override
    public String getTipo() {
        return "Drone";
    }
}