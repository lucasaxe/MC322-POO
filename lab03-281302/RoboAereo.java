public class RoboAereo extends Robo {
    public RoboAereo(String nome, int x, int y, int z) {
        super(nome, x, y, z);
    }

    @Override
    public void mover(int dx, int dy, int dz) {
        x += dx;
        y += dy;
        z += dz;
        System.out.println("Robo aereo movido para (" + x + "," + y + "," + z + ")");
    }
}
