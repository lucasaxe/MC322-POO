public class Main{
    public static void atualizarPosicao(Robo robo, Ambiente ambiente, int deltaX, int deltaY){
        robo.mover(deltaX, deltaY);

        if(ambiente.dentroDosLimites(robo.roboPosicaoX(), robo.roboPosicaoY())){
            robo.exibirPosicao();
        }
        else{
            System.out.println("A posicao ("+robo.roboPosicaoX()+","+robo.roboPosicaoY()+") do robo "+robo.pegarNome()+" está fora dos limites do ambiente.");
        }
    }

    public static void main(String[] args){
        Ambiente ambiente = new Ambiente(100, 100);

        Robo roboA = new Robo("lucas", 2, 3);
        Robo roboB = new Robo("joao", 5, 5);

        roboA.exibirPosicao();
        roboB.exibirPosicao();
        System.out.println();

        atualizarPosicao(roboA, ambiente, 3, 4);
        atualizarPosicao(roboB, ambiente, -6, 0);
        System.out.println();

        roboA.exibirPosicao();
        roboB.exibirPosicao();
        System.out.println();
    }
}
