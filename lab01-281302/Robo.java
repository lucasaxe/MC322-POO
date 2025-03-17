class Robo {
    private String nome;
    private int posicaoX, posicaoY;

    public Robo(String nome, int posicaoX, int posicaoY){
        this.nome = nome;
        this.posicaoX = posicaoX;
        this.posicaoY = posicaoY;
    }

    public void mover(int deltaX, int deltaY){
        this.posicaoX += deltaX;
        this.posicaoY += deltaY;
    }

    public void exibirPosicao(){
        System.out.println("A posicao do robo " + nome + " é: (" + posicaoX + ", " + posicaoY + ")");
    }

    public String pegarNome(){
        return nome;
    }

    public int roboPosicaoX(){
        return posicaoX;
    }

    public int roboPosicaoY(){
        return posicaoY;
    }
}