import java.util.Scanner;

public class RoboEscavador extends RoboTerrestre implements FuncionalidadesEspecificas {
    private int profundidadeEscavacao;
    private Ambiente ambiente;

    public RoboEscavador(String nome, int x, int y, int velocidadeMaxima, int profundidadeEscavacao, Ambiente ambiente) {
        super(nome, x, y, velocidadeMaxima);
        this.profundidadeEscavacao = profundidadeEscavacao;
        this.ambiente = ambiente;
    }

    public void escavar() {
        ambiente.getObstaculos().removeIf(obstaculo ->
                obstaculo.getTipo() == TipoObstaculo.BURACO &&
                        this.getX() >= obstaculo.getX1() &&
                        this.getX() <= obstaculo.getX2() &&
                        this.getY() >= obstaculo.getY1() &&
                        this.getY() <= obstaculo.getY2()
        );

        System.out.println(nome + " está escavando a " + profundidadeEscavacao + " metros de profundidade.");

        Obstaculo buraco = new Obstaculo(
                this.getX(), this.getX(), // x1, x2 (extensao eixo x)
                this.getY(), this.getY(), // y1, y2 (extensao eixo y)
                TipoObstaculo.BURACO      // tipo do obstáculo (do seu enum)
        );

        ambiente.getObstaculos().add(buraco);
        System.out.println("Buraco criado em (" + getX() + "," + getY() + ")");
    }

    @Override
    public void mostrarMenu() {
        System.out.println("\n            FUNCIONALIDADES DO ESCAVADOR");
        System.out.println("1. Escavar");
        System.out.println("2. Ver profundidade máxima");
    }

    @Override
    public void executarAcao(int opcao, Scanner scanner) {
        switch (opcao) {
            case 1:
                escavar();
                break;
            case 2:
                System.out.println("Profundidade máxima: " + profundidadeEscavacao + "m");
                break;
            default:
                System.out.println("Opção inválida!");
        }
    }

    @Override
    public String getTipo() {
        return "Escavador";
    }
}