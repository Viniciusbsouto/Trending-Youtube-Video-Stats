import java.io.*;
import java.util.*;

public class ordenationComments {

    // Classe auxiliar para armazenar cada linha com seu campo comment_count
    static class Linha {
        int commentCount;
        String linhaCompleta;

        Linha(int commentCount, String linhaCompleta) {
            this.commentCount = commentCount;
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

            // Verifica se tem campos suficientes
            if (campos.size() > 10) { // comment_count é o campo 11 (índice 10)
                String countStr = campos.get(10).trim();

                try {
                    int commentCount = Integer.parseInt(countStr);

                    String descricao = campos.size() > 14 ? campos.get(14) : "";

                    // Regex para detectar caracteres invisíveis ou de formatação incomuns
                    String regexCaracteresInvalidos = ".*[\\u00AD\\u200B-\\u200D\\uFEFF\\u2028\\u2029\\u00A0].*";

                    if (!descricao.matches(regexCaracteresInvalidos)) {
                        linhas.add(new Linha(commentCount, linha));
                    }

                } catch (NumberFormatException e) {
                    // ignora linhas com comment_count inválido
                }
            }
        }
        br.close();

        System.out.println("Total de linhas válidas lidas: " + linhas.size());

        Linha[] linhasArray = linhas.toArray(new Linha[0]);

        Linha[] linhasCrescente = Arrays.copyOf(linhasArray, linhasArray.length);
        Linha[] linhasDecrescente = Arrays.copyOf(linhasArray, linhasArray.length);

        Arrays.sort(linhasCrescente, Comparator.comparingInt(l -> l.commentCount));
        Arrays.sort(linhasDecrescente, Comparator.comparingInt((Linha l) -> l.commentCount).reversed());

        // Criação da pasta de saída
        File pastaSaida = new File(System.getProperty("user.dir") + File.separator + "Comentarios Ordenados");
        if (!pastaSaida.exists()) {
            pastaSaida.mkdirs();
        }

        // Escreve os arquivos
        escreverArquivo(
                new File(pastaSaida, "videos_T1_comment_count_crescente.csv"),
                cabecalho, linhasCrescente);

        escreverArquivo(
                new File(pastaSaida, "videos_T1_comment_count_decrescente.csv"),
                cabecalho, linhasDecrescente);

        System.out.println("Arquivos gerados com sucesso na pasta: " + pastaSaida.getAbsolutePath());
    }

    // Método para parsear a linha de CSV corretamente
    private static List<String> parseCSVLine(String linha) {
        List<String> resultado = new ArrayList<>();
        StringBuilder atual = new StringBuilder();
        boolean dentroDeAspas = false;

        for (int i = 0; i < linha.length(); i++) {
            char c = linha.charAt(i);

            if (c == '\"') {
                dentroDeAspas = !dentroDeAspas;
            } else if (c == ',' && !dentroDeAspas) {
                resultado.add(atual.toString());
                atual.setLength(0);
            } else {
                atual.append(c);
            }
        }
        resultado.add(atual.toString());
        return resultado;
    }

    // Método para escrever no arquivo
    private static void escreverArquivo(File caminhoArquivo, String cabecalho, Linha[] linhas) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(caminhoArquivo));
        bw.write(cabecalho);
        bw.newLine();
        for (Linha l : linhas) {
            bw.write(l.linhaCompleta);
            bw.newLine();
        }
        bw.close();
        System.out.println("Arquivo " + caminhoArquivo.getName() + " escrito com sucesso!");
    }

    public static void main(String[] args) {
        try {
            gerarArquivosOrdenados();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
