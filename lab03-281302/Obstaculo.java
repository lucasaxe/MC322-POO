public class Obstaculo {
    private final int x1, y1, x2, y2, z;
    private final TipoObstaculo tipo;

    public Obstaculo(int x1, int x2, int y1, int y2, TipoObstaculo tipo) {
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
        this.tipo = tipo;
        this.z = tipo.getAlturaPadrao() == -1 ? 0 : tipo.getAlturaPadrao();
    }

    //getters
    public int getX1() { return x1; }
    public int getX2() { return x2; }
    public int getY1() { return y1; }
    public int getY2() { return y2; }
    public int getZ() { return z; }
    public TipoObstaculo getTipo() { return tipo; }

    @Override
    public String toString() {
        return tipo + " (" + x1 + ":" + x2 + ", " + y1 + ":" + y2 + ", " + z + ")";
    }
}