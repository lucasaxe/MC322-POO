public enum TipoObstaculo {
    //obstaculos terrestres
    ARVORE(5, true),
    PREDIO(10, true),
    BURACO(0, false),

    //obstaculos aereos
    PASSAROS(70, true),
    BALAO(60, true),
    NUVEM(90, false),

    //obstaculos terrestres e aereos
    VULCAO(80, true),
    GELEIRA(60, true),
    OUTRO(-1, false);

    private final int alturaPadrao;
    private final boolean bloqueiaPassagem;

    TipoObstaculo(int alturaPadrao, boolean bloqueiaPassagem) {
        this.alturaPadrao = alturaPadrao;
        this.bloqueiaPassagem = bloqueiaPassagem;
    }

    public int getAlturaPadrao() {
        return alturaPadrao;
    }

    public boolean isBloqueiaPassagem() {
        return bloqueiaPassagem;
    }
}