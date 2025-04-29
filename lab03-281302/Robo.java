import java.util.ArrayList;

public abstract class Robo {
    protected int x, y, z;
    protected String nome;
    protected final ArrayList<Sensor> sensores;

    public Robo(String nome, int x, int y, int z) {
        this.nome = nome;
        this.x = x;
        this.y = y;
        this.z = z;
        this.sensores = new ArrayList<>();
    }

    public void adicionarSensor(Sensor sensor) {
        sensores.add(sensor);
    }

    public boolean possuiSensores() {
        return !this.sensores.isEmpty();
    }

    public abstract void mover(int dx, int dy, int dz);

    //getters
    public String getNome() { return nome; }
    public int getX() { return x; }
    public int getY() { return y; }
    public int getZ() { return z; }
    public ArrayList<Sensor> getSensores() { return sensores; }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "| " + nome +" está na posição (" + x + "," + y + "," + z +")";
    }
}

