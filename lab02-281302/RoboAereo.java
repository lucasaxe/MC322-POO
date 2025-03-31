public class RoboAereo extends Robo{
    protected int altitude, altitudeMaxima;

    public RoboAereo(String nome, int posicaoX, int posicaoY, int altitudeMaxima){
        super(nome, posicaoX, posicaoY);
        this.altitude = 0;
        this.altitudeMaxima = altitudeMaxima;
    }

    public void subir(int metros){
        if(altitude + metros <= altitudeMaxima){
            altitude += metros;
            System.out.println(nome + " subiu para " + altitude + " metros.");
        }
        else{
            System.out.println(nome + " -> altitude maxima excedida.");
        }
    }

    public void descer(int metros){
        if(altitude - metros >= 0){
            altitude -= metros;
            System.out.println(nome + " desceu para " + altitude + " metros.");
        }
        else{
            System.out.println(nome + " -> nao pode descer abaixo do solo.");
        }
    }

    @Override
    public void exibirPosicao(){
        System.out.println(nome + " esta na posicao (" + posicaoX + ", " + posicaoY + ") a " + altitude + " metros de altitude.");
    }

    //getters aereos
    public int getAltitude(){
        return altitude;
    }

    public int getAltitudeMaxima(){
        return altitudeMaxima;
    }

    //subclasse 1
    public static class RoboAviao extends RoboAereo{
        private int quantidadePassageiros;
        private String destino;

        public RoboAviao(String nome, int posicaoX, int posicaoY, int altitudeMaxima, int quantidadePassageiros, String destino){
            super(nome, posicaoX, posicaoY, altitudeMaxima);
            this.quantidadePassageiros = quantidadePassageiros;
            this.destino = destino;
        }

        public void voarEmCruzeiro(int distancia){
            System.out.println(nome + " esta com " + quantidadePassageiros + " passageiros, indo para " + destino + ".");
        }
    }

    //subclasse 2
    public static class RoboDrone extends RoboAereo{
        private int quantidadeBateria;

        public RoboDrone(String nome, int posicaoX, int posicaoY, int altitudeMaxima, int quantidadeBateria){
            super(nome, posicaoX, posicaoY, altitudeMaxima);
            this.quantidadeBateria = quantidadeBateria;
        }

        public void verificarBateria(){
            System.out.println(nome + " tem " + quantidadeBateria + " minutos de bateria restantes.");
        }
    }
}