import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class ordenationTrendingDate {

    static class Linha {
        Date trendingFullDate;
        String linhaCompleta;

        Linha(Date trendingFullDate, String linhaCompleta) {
            this.trendingFullDate = trendingFullDate;
            this.linhaCompleta = linhaCompleta;
        }
    }

    public static void main(String[] args) {
        try {
            ordenarPorTrendingDate();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void ordenarPorTrendingDate() throws IOException {
        BufferedReader br = new BufferedReader(
                new FileReader("videos_T1.csv"));
        String cabecalho = br.readLine(); // lê o cabeçalho

        List<Linha> linhasValidas = new ArrayList<>();
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");

        String linha;
        int totalLidas = 0;
        int validas = 0;
        int invalidas = 0;

        while ((linha = br.readLine()) != null) {
            totalLidas++;
            List<String> campos = parseCSVLine(linha);

            if (campos.size() <= 17) {
                invalidas++;
                continue;
            }

            try {
                String dataStr = campos.get(17).trim();
                Date data = formato.parse(dataStr);
                linhasValidas.add(new Linha(data, linha));
                validas++;
            } catch (Exception e) {
                invalidas++;
            }
        }

        br.close();

        System.out.println("Total de linhas lidas: " + totalLidas);
        System.out.println("Linhas válidas: " + validas);
        System.out.println("Linhas inválidas: " + invalidas);

        // Ordenações
        Linha[] crescentes = linhasValidas.toArray(new Linha[0]);
        Linha[] decrescentes = Arrays.copyOf(crescentes, crescentes.length);

        Arrays.sort(crescentes, Comparator.comparing(l -> l.trendingFullDate));
        Arrays.sort(decrescentes, (a, b) -> b.trendingFullDate.compareTo(a.trendingFullDate));

        File pastaSaida = new File(System.getProperty("user.dir") + File.separator + "Datas Ordenadas");
        if (!pastaSaida.exists()) {
            pastaSaida.mkdirs();
        }

        escreverArquivo(new File(pastaSaida, "videos_T1_trending_full_date_crescente.csv"), cabecalho, crescentes);
        escreverArquivo(new File(pastaSaida, "videos_T1_trending_full_date_decrescente.csv"), cabecalho, decrescentes);

        System.out.println("Arquivos gerados com sucesso em: " + pastaSaida.getAbsolutePath());
    }

    private static void escreverArquivo(File arquivo, String cabecalho, Linha[] linhasOrdenadas) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(arquivo));
        bw.write(cabecalho);
        bw.newLine();

        for (Linha l : linhasOrdenadas) {
            bw.write(l.linhaCompleta);
            bw.newLine();
        }

        bw.close();
        System.out.println("Arquivo " + arquivo.getName() + " escrito.");
    }

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
}
