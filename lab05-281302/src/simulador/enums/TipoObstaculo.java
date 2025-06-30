package simulador.enums;

/**
 * Especifica os diferentes tipos de obstáculos que podem existir,
 * cada um com suas características, como altura e se bloqueia a passagem.
 */
public enum TipoObstaculo {
    ARVORE(5, true),       // Uma árvore com 5 unidades de altura, bloqueia passagem.
    PREDIO(10, true),      // Um prédio com 10 unidades de altura (da base ao topo), bloqueia.
    BURACO(0, false),      // Um buraco no nível do chão (z=0), não bloqueia quem está acima, mas é um perigo.

    PASSAROS(70, true),    // Um bando de pássaros na altitude 70, pode bloquear temporariamente.
    BALAO(60, true),       // Um balão na altitude 60, bloqueia.
    NUVEM(90, false),      // Uma nuvem na altitude 90, não bloqueia (pode afetar sensores, por exemplo).

    VULCAO(80, true),      // Um vulcão com pico a 80m de altura, bloqueia.
    GELEIRA(60, true),     // Uma geleira com pico a 60m de altura, bloqueia.

    OUTRO(-1, false);      // Um obstáculo não especificado, altura não definida, não bloqueia por padrão.

    private final int alturaOuAltitudePadrao;
    private final boolean efetivamenteBloqueia;

    TipoObstaculo(int alturaOuAltitude, boolean bloqueia) {
        this.alturaOuAltitudePadrao = alturaOuAltitude;
        this.efetivamenteBloqueia = bloqueia;
    }

    public int getAlturaOuAltitudePadrao() {
        return alturaOuAltitudePadrao;
    }
    public boolean isBloqueiaPassagem() {
        return efetivamenteBloqueia;
    }
}