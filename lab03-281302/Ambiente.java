import java.util.ArrayList;

public class Ambiente {
    private final int comprimento, largura, altura;
    private final ArrayList<Robo> robos;
    private final ArrayList<Obstaculo> obstaculos;

    public Ambiente(int comprimento, int largura, int altura) {
        this.comprimento = comprimento;
        this.largura = largura;
        this.altura = altura;
        this.robos = new ArrayList<>();
        this.obstaculos = new ArrayList<>();
    }

    public void adicionarRobo(Robo r) {
        robos.add(r);
    }

    public void removerRobo(Robo r) {
        robos.remove(r);
    }

    public boolean dentroDosLimites(int x, int y, int z) {
        return x >= 0 && x <= comprimento && y >= 0 && y <= largura && z >= 0 && z <= altura;
    }

    public void detectarColisoes() {
        System.out.println();
        boolean colisao = false;
        for (Robo robo : robos) {
            for (Obstaculo obstaculo : obstaculos) {
                if (robo.getX() >= obstaculo.getX1() && robo.getX() <= obstaculo.getX2() &&
                        robo.getY() >= obstaculo.getY1() && robo.getY() <= obstaculo.getY2() &&
                        robo.getZ() <= obstaculo.getZ() && obstaculo.getTipo().isBloqueiaPassagem()) {
                    System.out.println("Colisão detectada entre " + robo + " e " + obstaculo);
                    colisao = true;
                }
            }
        }
        if(!colisao) {
            System.out.println("Nenhuma colisão detectada");
        }
    }

    //getters
    public int getComprimento() { return largura; }
    public int getLargura() { return altura; }
    public int getAltura() { return altura; }
    public ArrayList<Robo> getRobos() { return robos; }
    public ArrayList<Obstaculo> getObstaculos() { return obstaculos; }
}