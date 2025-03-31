public class RoboTerrestre extends Robo{
    protected int velocidadeMaxima;

    public RoboTerrestre(String nome, int posicaoX, int posicaoY, int velocidadeMaxima){
        super(nome, posicaoX, posicaoY);
        this.velocidadeMaxima = velocidadeMaxima;
    }

    @Override
    public void mover(int deltaX, int deltaY){
        int velocidade = (int) Math.sqrt(deltaX*deltaX + deltaY*deltaY);

        if(velocidade <= velocidadeMaxima){
            super.mover(deltaX, deltaY);
        }
        else{
            System.out.println(nome + " -> movimento excede velocidade maxima.");
        }
    }

    //subclasse 1
    public static class RoboEscavador extends RoboTerrestre{
        private int profundidadeEscavacao;

        public RoboEscavador(String nome, int posicaoX, int posicaoY, int velocidadeMaxima, int profundidadeEscavacao){
            super(nome, posicaoX, posicaoY, velocidadeMaxima);
            this.profundidadeEscavacao = profundidadeEscavacao;
        }

        public void escavar(){
            System.out.println(nome + " esta escavando a " + profundidadeEscavacao + " metros de profundidade.");
        }
    }
    
    //subclasse 2
    public static class RoboCaminhao extends RoboTerrestre{
        private int pesoCarga;

        public RoboCaminhao(String nome, int posicaoX, int posicaoY, int velocidadeMaxima, int pesoCarga){
            super(nome, posicaoX, posicaoY, velocidadeMaxima);
            this.pesoCarga = pesoCarga;
        }

        public void moverComCarga(int deltaX, int deltaY){
            int velocidadeReduzida = velocidadeMaxima - pesoCarga/10;
            
            if(velocidadeReduzida > 0){
                int velocidade = (int) Math.sqrt(deltaX*deltaX + deltaY*deltaY);

                if(velocidade <= velocidadeReduzida){
                    super.mover(deltaX, deltaY);
                }
            }
        }
    }
}