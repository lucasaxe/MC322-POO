public class RoboTerrestre extends Robo {
    public RoboTerrestre(String nome, int x, int y, int z) {
        super(nome, x, y, 0);
    }

    @Override
    public void mover(int dx, int dy, int dz) {
        x += dx;
        y += dy;
        System.out.println("Robo terrestre " + nome + " movido para (" + x + "," + y + "," + z +")");
    }
}
