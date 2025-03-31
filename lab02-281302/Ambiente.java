import java.util.ArrayList;

public class Ambiente{
    private int largura, altura;
    private ArrayList<Robo> robos;

    public Ambiente(int largura, int altura){
        this.largura = largura;
        this.altura = altura;
        this.robos = new ArrayList<>();
    }

    public void adicionarRobo(Robo r){
        robos.add(r);
        System.out.println(r.getNome() + " adicionado ao ambiente.");
    }

    public boolean dentroDosLimites(Robo r, int x, int y){
        boolean dentro = (x >= 0 && x < largura && y >= 0 && y < altura); // verifica se a posicao esta dentro dos limites do ambiente
        
        if(r instanceof RoboAereo){
            RoboAereo aereo = (RoboAereo) r;
            if(aereo.getAltitude() > aereo.getAltitudeMaxima()){
                dentro = false;
            }
        }

        return dentro;
    }

    public void moverRobo(Robo r, int deltaX, int deltaY){
        int novaX = r.getPosicaoX() + deltaX;
        int novaY = r.getPosicaoY() + deltaY;
        
        if(dentroDosLimites(r, novaX, novaY)){
            r.mover(deltaX, deltaY);
        }
        else{
            System.out.println(r.getNome() + "-> movimento fora dos limites do ambiente.");
        }
    }

    public void exibirPosicoes(){
        for(Robo r : robos){
            r.exibirPosicao();
        }
    }
}