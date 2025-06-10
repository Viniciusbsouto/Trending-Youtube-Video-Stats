import java.io.*;
import java.util.*;

public class ordenationChannel {

    // Classe auxiliar para armazenar cada linha com seu campo channel_title
    static class Linha {
        String channelTitle;
        String linhaCompleta;

        Linha(String channelTitle, String linhaCompleta) {
            this.channelTitle = channelTitle;
            this.linhaCompleta = linhaCompleta;
        }
    }

    public static void gerarArquivosOrdenados() throws IOException {
        BufferedReader br = new BufferedReader(
                new FileReader("videos_T1.csv"));
        List<Linha> linhas = new ArrayList<>();

        String cabecalho = br.readLine(); // salva o cabeçalho
        String linha;
        while ((linha = br.readLine()) != null) {
            List<String> campos = parseCSVLine(linha);

            if (campos.size() > 14) {
                String descricao = campos.get(14);
                String regexCaracteresInvalidos = ".*[\\u00AD\\u200B-\\u200D\\uFEFF\\u2028\\u2029\\u00A0].*";

                if (!descricao.matches(regexCaracteresInvalidos)) {
                    linhas.add(new Linha(campos.get(3), linha)); // Channel Title é o campo 4 (índice 3)
                }
            }
        }
        br.close();

        System.out.println("Total de linhas válidas lidas: " + linhas.size());

        Linha[] linhasArray = linhas.toArray(new Linha[0]);

        Linha[] linhasCrescente = Arrays.copyOf(linhasArray, linhasArray.length);
        Linha[] linhasDecrescente = Arrays.copyOf(linhasArray, linhasArray.length);

        Arrays.sort(linhasCrescente, Comparator.comparing(l -> l.channelTitle.toLowerCase()));
        Arrays.sort(linhasDecrescente, Comparator.comparing((Linha l) -> l.channelTitle.toLowerCase()).reversed());

        // Caminho da pasta de saída
        String pastaSaida = System.getProperty("user.dir") + File.separator + "Canais Ordenados";
        // Cria a pasta se não existir
        File pasta = new File(pastaSaida);
        if (!pasta.exists()) {
            pasta.mkdirs();
        }

        escreverArquivo(
                pastaSaida + "\\videos_T1_channel_title_crescente.csv",
                cabecalho, linhasCrescente);
        escreverArquivo(
                pastaSaida + "\\videos_T1_channel_title_decrescente.csv",
                cabecalho, linhasDecrescente);

        System.out.println("Arquivos gerados com sucesso em: " + pastaSaida);
    }

    // Método para parsear a linha de CSV corretamente (respeitando vírgulas dentro
    // de aspas)
    private static List<String> parseCSVLine(String linha) {
        List<String> resultado = new ArrayList<>();
        StringBuilder atual = new StringBuilder();
        boolean dentroDeAspas = false;

        for (int i = 0; i < linha.length(); i++) {
            char c = linha.charAt(i);

            if (c == '\"') {
                dentroDeAspas = !dentroDeAspas; // alterna se está dentro de aspas
            } else if (c == ',' && !dentroDeAspas) {
                resultado.add(atual.toString());
                atual.setLength(0); // limpa o buffer
            } else {
                atual.append(c);
            }
        }
        resultado.add(atual.toString()); // adiciona o último campo
        return resultado;
    }

    // Método para escrever no arquivo
    private static void escreverArquivo(String caminho, String cabecalho, Linha[] linhas) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(caminho));
        bw.write(cabecalho);
        bw.newLine();
        for (Linha l : linhas) {
            bw.write(l.linhaCompleta);
            bw.newLine();
        }
        bw.close();
        System.out.println("Arquivo " + caminho + " escrito com sucesso!");
    }

    public static void main(String[] args) {
        try {
            gerarArquivosOrdenados();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}