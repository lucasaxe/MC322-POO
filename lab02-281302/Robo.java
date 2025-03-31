public class Robo{
    protected String nome, direcao;
    protected int posicaoX, posicaoY;

    public Robo(String nome, int posicaoX, int posicaoY){
        this.nome = nome;
        this.posicaoX = posicaoX;
        this.posicaoY = posicaoY;
        this.direcao = "N";
    }

    public void mover(int deltaX, int deltaY){
        this.posicaoX += deltaX;
        this.posicaoY += deltaY;
    }

    public void identificarObstaculo(){
        System.out.println(nome + " esta verificando obstaculos ao redor.");
    }

    public void exibirPosicao(){
        System.out.println(nome + " esta na posicao (" + posicaoX + ", " + posicaoY + ")");
    }

    //getters e setters
    public String getNome(){
        return nome;
    }

    public String getDirecao(){
        return direcao;
    }

    public int getPosicaoX(){
        return posicaoX;
    }

    public int getPosicaoY(){
        return posicaoY;
    }

    public void setDirecao(String direcao){
        this.direcao = direcao;
    }
}