import java.io.*;

public class Projeto {

    // Método para criar o arquivo videos.csv
    public static void criarVideosCSV() throws IOException {
        String[][] arquivos = {
                { "archive/GBvideos.csv", "GB" },
                { "archive/CAvideos.csv", "CA" },
                { "archive/MXvideos.csv", "MX" },
                { "archive/INvideos.csv", "IN" },
                { "archive/DEvideos.csv", "DE" },
                { "archive/RUvideos.csv", "RU" },
                { "archive/JPvideos.csv", "JP" },
                { "archive/FRvideos.csv", "FR" },
                { "archive/USvideos.csv", "US" },
                { "archive/KRvideos.csv", "KR" }
        };

        BufferedWriter bw = new BufferedWriter(new FileWriter("videos.csv"));
        bw.write("video_id,trending_date,title,channel_title,category_id,publish_time,tags,views,likes,dislikes,comment_count,thumbnail_link,comments_disabled,ratings_disabled,video_error_or_removed,description,countries");
        bw.newLine();

        for (String[] arquivo : arquivos) {
            BufferedReader br = new BufferedReader(new FileReader(arquivo[0]));
            String linha;
            boolean primeiraLinha = true;

            while ((linha = br.readLine()) != null) {
                if (primeiraLinha) {
                    primeiraLinha = false;
                    continue;
                }

                String[] dados = linha.split(",", -1);
                if (dados.length < 15) continue;

                String linhaDeSaida = String.join(",", dados) + "," + arquivo[1];
                bw.write(linhaDeSaida);
                bw.newLine();
            }
            br.close();
        }
        bw.close();
    }

    // Método para criar videos_T1.csv com data formatada
    public static void criarVideosT1CSV() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("videos.csv"));
        BufferedWriter bw = new BufferedWriter(new FileWriter("videos_T1.csv"));

        String linha;
        boolean isPrimeiraLinha = true;

        while ((linha = br.readLine()) != null) {
            String[] campos = linha.split(",");
            if (campos.length < 12) continue;

            if (isPrimeiraLinha) {
                bw.write(linha + ",trending_full_date\n");
                isPrimeiraLinha = false;
            } else {
                String trendingDate = campos[1]; // Formato: AA.DD.MM
                String[] dataPartes = trendingDate.split("\\.");
                if (dataPartes.length == 3) {
                    String ano = "20" + dataPartes[0];
                    String mes = dataPartes[2];
                    String dia = dataPartes[1];
                    String dataFormatada = dia + "/" + mes + "/" + ano;
                    bw.write(linha + "," + dataFormatada + "\n");
                }
            }
        }
        br.close();
        bw.close();
    }

    // Método para criar videos_TSS.csv filtrando títulos específicos
    public static void criarVideosTSSCSV() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("videos_T1.csv"));
        BufferedWriter bw = new BufferedWriter(new FileWriter("videos_TSS.csv"));

        String linha;
        boolean primeiraLinha = true;

        while ((linha = br.readLine()) != null) {
            if (primeiraLinha) {
                primeiraLinha = false;
                bw.write(linha);
                bw.newLine();
                continue;
            }

            String[] dados = linha.split(",", -1);
            if (dados.length < 17) continue;

            String title = dados[2];
            if (title.contains("Trailers") || title.contains("Shows") || title.contains("Shorts")) {
                bw.write(linha);
                bw.newLine();
            }
        }
        br.close();
        bw.close();
    }

    // Método para criar videos_T2.csv filtrando vídeos com dislikes maiores que likes
    public static void criarVideosT2CSV() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("videos_T1.csv"));
        BufferedWriter bw = new BufferedWriter(new FileWriter("videos_T2.csv"));

        String linha;
        boolean isPrimeiraLinha = true;

        while ((linha = br.readLine()) != null) {
            String[] campos = linha.split(",");

            if (isPrimeiraLinha) {
                bw.write(linha + "\n");
                isPrimeiraLinha = false;
            } else {
                try {
                    int likes = Integer.parseInt(campos[8]);
                    int dislikes = Integer.parseInt(campos[9]);
                    if (dislikes > likes) {
                        bw.write(linha + "\n");
                    }
                } catch (NumberFormatException e) {
                    continue;
                }
            }
        }
        br.close();
        bw.close();
    }

    public static void main(String[] args) {
        try {
            criarVideosCSV();
            System.out.println("Arquivo videos.csv criado com sucesso!");

            criarVideosT1CSV();
            System.out.println("Arquivo videos_T1.csv criado com sucesso!");

            criarVideosTSSCSV();
            System.out.println("Arquivo videos_TSS.csv criado com sucesso!");

            criarVideosT2CSV();
            System.out.println("Arquivo videos_T2.csv criado com sucesso!");
        } catch (IOException e) {
            System.err.println("Erro ao processar os arquivos CSV: " + e.getMessage());
        }
    }
}
