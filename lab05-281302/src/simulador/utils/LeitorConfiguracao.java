package simulador.utils;

import simulador.core.Ambiente;
import simulador.entidades.Obstaculo;
import simulador.entidades.AgenteInteligente;
import simulador.entidades.Robo;
import simulador.enums.TipoObstaculo;
import simulador.interfaces.Missao;
import simulador.missao.MissaoBuscarPonto;
import simulador.missao.MissaoExplorar;
import simulador.entidades.robos.RoboExplorador; // Exemplo de robô concreto

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Classe utilitária para ler um arquivo de configuração e inicializar o simulador.
 */
public class LeitorConfiguracao {

    public static void carregar(String nomeArquivo, Ambiente ambiente) {
        System.out.println("Carregando configuração de: " + nomeArquivo);
        try (BufferedReader br = new BufferedReader(new FileReader(nomeArquivo))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                if (linha.trim().isEmpty() || linha.trim().startsWith("#")) {
                    continue;
                }
                processarLinha(linha, ambiente);
            }
            System.out.println("Configuração carregada com sucesso.");
        } catch (IOException e) {
            System.err.println("AVISO: Arquivo de configuração '" + nomeArquivo + "' não encontrado ou não pode ser lido. O simulador iniciará vazio.");
        } catch (Exception e) {
            System.err.println("Erro ao processar o arquivo de configuração: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void processarLinha(String linha, Ambiente ambiente) throws Exception {
        String[] partes = linha.split("\\s+");
        if (partes.length == 0) return;

        String comando = partes[0].toUpperCase();

        try {
            switch (comando) {
                case "AMBIENTE":
                    // A instância do ambiente já é criada no Main. Esta linha pode ser usada para validação ou ignorada.
                    System.out.println("Comando AMBIENTE lido (dimensões definidas na criação inicial).");
                    break;

                case "ROBO":
                    // Formato: ROBO <Tipo> <ID> <Nome> <X> <Y> [Z]
                    if (partes.length < 6) throw new IllegalArgumentException("Formato inválido para ROBO: " + linha);
                    String tipoRobo = partes[1];
                    String id = partes[2];
                    String nome = partes[3];
                    int x = Integer.parseInt(partes[4]);
                    int y = Integer.parseInt(partes[5]);
                    int z = (partes.length > 6) ? Integer.parseInt(partes[6]) : 0;

                    Robo novoRobo = criarRobo(tipoRobo, id, nome, x, y, z);
                    if (novoRobo != null) {
                        ambiente.adicionarEntidade(novoRobo);
                        // Importante: inicializar subsistemas que dependem do ambiente
                        novoRobo.inicializarSubsistemas(ambiente);
                    }
                    break;

                case "OBSTACULO":
                    // Formato: OBSTACULO <Tipo> <X> <Y> <Z>
                    if (partes.length < 5) throw new IllegalArgumentException("Formato inválido para OBSTACULO: " + linha);
                    TipoObstaculo tipoObs = TipoObstaculo.valueOf(partes[1].toUpperCase());
                    int ox = Integer.parseInt(partes[2]);
                    int oy = Integer.parseInt(partes[3]);
                    int oz = Integer.parseInt(partes[4]);
                    ambiente.adicionarEntidade(new Obstaculo(ox, oy, ox, oy, oz, tipoObs));
                    break;

                case "MISSAO":
                    // Formato: MISSAO <ID_Robo> <TIPO_MISSAO> [params...]
                    if (partes.length < 3) throw new IllegalArgumentException("Formato inválido para MISSAO: " + linha);
                    String idRoboMissao = partes[1];
                    String tipoMissao = partes[2].toUpperCase();

                    AgenteInteligente agente = encontrarAgentePorId(idRoboMissao, ambiente);
                    if (agente == null) {
                        System.err.println("AVISO: Robô com ID '" + idRoboMissao + "' para missão não é um AgenteInteligente ou não foi encontrado.");
                        return;
                    }

                    Missao missao = criarMissao(tipoMissao, partes);
                    if (missao != null) {
                        agente.definirMissao(missao);
                    }
                    break;

                default:
                    System.err.println("Comando desconhecido no arquivo de configuração: " + comando);
            }
        } catch (NumberFormatException e) {
            throw new Exception("Erro de formato numérico na linha: " + linha, e);
        } catch (IllegalArgumentException e) {
            throw new Exception("Tipo desconhecido (Robô, Obstáculo, Missão) ou formato incorreto na linha: " + linha, e);
        }
    }

    private static Robo criarRobo(String tipo, String id, String nome, int x, int y, int z) {
        if (tipo.equalsIgnoreCase("EXPLORADOR")) {
            return new RoboExplorador(id, nome, x, y);
        }
        // Adicione outros tipos de robôs aqui conforme implementa-los como Agentes Inteligentes.
        // Exemplo:
        // if (tipo.equalsIgnoreCase("DRONEINTELIGENTE")) {
        //     return new RoboDroneInteligente(id, nome, x, y, z);
        // }
        System.err.println("AVISO: Tipo de robô '" + tipo + "' não suportado pelo leitor de configuração.");
        return null;
    }

    private static AgenteInteligente encontrarAgentePorId(String id, Ambiente ambiente) {
        for (Robo robo : ambiente.getRobos()) {
            if (robo.getId().equalsIgnoreCase(id) && robo instanceof AgenteInteligente) {
                return (AgenteInteligente) robo;
            }
        }
        return null;
    }

    private static Missao criarMissao(String tipoMissao, String[] params) {
        try {
            switch (tipoMissao) {
                case "EXPLORAR":
                    // Formato: MISSAO <ID> EXPLORAR [num_passos]
                    int passos = (params.length > 3) ? Integer.parseInt(params[3]) : 20; // 20 passos por padrão
                    return new MissaoExplorar(passos);
                case "BUSCARPONTO":
                    // Formato: MISSAO <ID> BUSCARPONTO <X> <Y> <Z>
                    if (params.length < 6) throw new IllegalArgumentException("Parâmetros insuficientes para BUSCARPONTO.");
                    int x = Integer.parseInt(params[3]);
                    int y = Integer.parseInt(params[4]);
                    int z = Integer.parseInt(params[5]);
                    return new MissaoBuscarPonto(x, y, z);
                default:
                    System.err.println("AVISO: Tipo de missão desconhecido: " + tipoMissao);
                    return null;
            }
        } catch (NumberFormatException e) {
            System.err.println("AVISO: Erro de formato numérico nos parâmetros da missão: " + String.join(" ", params));
            return null;
        }
    }
}