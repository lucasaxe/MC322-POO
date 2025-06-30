package simulador.entidades;

import simulador.core.Entidade;
import simulador.enums.TipoEntidade;
import simulador.enums.TipoObstaculo;

/**
 * Representa um obstáculo fixo no ambiente do simulador.
 * Implementa a interface Entidade, pois é um objeto que ocupa espaço.
 */
public class Obstaculo implements Entidade {
    private final int x1, y1, x2, y2;
    private final int zBase;
    private final TipoObstaculo tipoEspecifico;

    public Obstaculo(int x1, int y1, int x2, int y2, int zBase, TipoObstaculo tipo) {
        // Garante que x1 <= x2 e y1 <= y2 para a área.
        this.x1 = Math.min(x1, x2);
        this.y1 = Math.min(y1, y2);
        this.x2 = Math.max(x1, x2);
        this.y2 = Math.max(y1, y2);
        this.zBase = zBase;
        this.tipoEspecifico = tipo;
    }

    @Override public int getX() { return this.x1; }
    @Override public int getY() { return this.y1; }
    @Override public int getZ() { return this.zBase; }

    @Override
    public void setPosicao(int x, int y, int z) {
        System.out.println("Aviso: Obstáculos são fixos. " + getDescricao() + " não foi movido.");
    }

    @Override public TipoEntidade getTipo() { return TipoEntidade.OBSTACULO; }

    @Override
    public String getDescricao() {
        return String.format("Obstaculo: %s em área (%d,%d) a (%d,%d) no nível Z=%d (Alt/AltPadrão: %d, Bloqueia: %b)",
                tipoEspecifico.name(), x1, y1, x2, y2, zBase,
                tipoEspecifico.getAlturaOuAltitudePadrao(), tipoEspecifico.isBloqueiaPassagem());
    }

    @Override
    public char getRepresentacao() {
        // Usa a primeira letra do nome do tipo de obstáculo para representação visual.
        return tipoEspecifico.name().charAt(0);
    }

    public int getX1() { return x1; }
    public int getY1() { return y1; }
    public int getX2() { return x2; }
    public int getY2() { return y2; }
    public int getZBase() { return zBase; }
    public TipoObstaculo getTipoEspecifico() { return tipoEspecifico; }

    @Override
    public String toString() {
        return getDescricao();
    }
}