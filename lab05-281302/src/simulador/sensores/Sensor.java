package simulador.sensores;

/**
 * Classe base abstrata para todos os tipos de sensores.
 * Define características comuns, como o raio de alcance, e um método
 * que as classes filhas (sensores específicos) devem implementar para definir seu comportamento.
 */
public abstract class Sensor {
    protected final double raioDeAlcance;

    public Sensor(double raio) {
        this.raioDeAlcance = raio;
    }

    public abstract void monitorar();

    public double getRaioDeAlcance() {
        return raioDeAlcance;
    }
}