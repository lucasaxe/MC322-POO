public class Main{
    public static void main(String[] args){
        Ambiente ambiente = new Ambiente(100, 100);
        
        RoboTerrestre roboTerrestre = new RoboTerrestre("Robo Terrestre", 10, 10, 5);
        RoboTerrestre.RoboEscavador roboEscavador = new RoboTerrestre.RoboEscavador("Robo Escavador", 30, 30, 2, 5);
        RoboTerrestre.RoboCaminhao roboCaminhao = new RoboTerrestre.RoboCaminhao("Robo Caminhao", 20, 20, 3, 2);
        
        RoboAereo roboAereo = new RoboAereo("Robo Aereo", 40, 40, 100);
        RoboAereo.RoboAviao roboAviao = new RoboAereo.RoboAviao("Robo Aviao", 60, 60, 10000, 50, "Dubai");
        RoboAereo.RoboDrone roboDrone = new RoboAereo.RoboDrone("Robo Drone", 50, 50, 150, 30);
        

        //adicionando robos ao ambiente
        ambiente.adicionarRobo(roboTerrestre);
        ambiente.adicionarRobo(roboEscavador);
        ambiente.adicionarRobo(roboCaminhao);
        ambiente.adicionarRobo(roboAereo);
        ambiente.adicionarRobo(roboAviao);
        ambiente.adicionarRobo(roboDrone);
        
        System.out.println();
        
        //testando metodos dos robos
        ambiente.moverRobo(roboTerrestre, 2, 3); //movimento válido
        ambiente.moverRobo(roboTerrestre, 10, 10); //excede a velocidade maxima
        
        roboEscavador.escavar(); //metodo específico
        roboCaminhao.moverComCarga(1, 1); //movimento
        
        roboAereo.subir(50);
        roboAereo.subir(60); //tentativa de exceder altitude máxima

        roboAviao.voarEmCruzeiro(500);
        roboDrone.verificarBateria();
        
        System.out.println();

        //exibe as posicoes finais
        ambiente.exibirPosicoes(); 
    }
}