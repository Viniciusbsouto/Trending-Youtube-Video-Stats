package ordenation;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class OrdenacaoTrendingDate {

    public static void main(String[] args) {
        String baseDir = "C:\\Users\\vinic\\OneDrive\\Área de Trabalho\\Projeto Java\\";
        String pastaSaida = baseDir + "ordenação Datas";

        new File(pastaSaida).mkdirs();

        String[] nomesArquivos = {
                "Comentarios Ordenados\\videos_T1_comment_count_crescente.csv",
                "Comentarios Ordenados\\videos_T1_comment_count_decrescente.csv",
                "videos_T1.csv"
        };

        String[] casos = { "melhorCaso", "piorCaso", "medioCaso" };

        for (int i = 0; i < nomesArquivos.length; i++) {
            String nomeArquivo = nomesArquivos[i];
            String caso = casos[i];

            System.out.println("\n=== Processando " + caso + " ===");
            System.out.println("Lendo vídeos do arquivo: " + nomeArquivo);

            List<Video> videos = lerVideos(baseDir + nomeArquivo);
            if (videos.isEmpty()) {
                System.out.println("Nenhum vídeo encontrado para " + caso + ", pulando...");
                continue;
            }
            System.out.println("Total de vídeos lidos: " + videos.size());

            // QuickSort
            System.out.println("Ordenando com QuickSort...");
            List<Video> copiaQuick = new ArrayList<>(videos);
            long inicioQuick = System.currentTimeMillis();
            quickSort(copiaQuick, 0, copiaQuick.size() - 1);
            long fimQuick = System.currentTimeMillis();
            System.out.println("QuickSort (" + caso + ") concluído em " + (fimQuick -
                    inicioQuick) + " ms.");
            salvarVideos(copiaQuick, pastaSaida + "\\quickSort_" + caso + ".csv");

            // MergeSort
            System.out.println("Ordenando com MergeSort...");
            long inicioMerge = System.currentTimeMillis();
            List<Video> ordenadosMerge = mergeSort(new ArrayList<>(videos));
            long fimMerge = System.currentTimeMillis();
            System.out.println("MergeSort (" + caso + ") concluído em " + (fimMerge -
                    inicioMerge) + " ms.");
            salvarVideos(ordenadosMerge, pastaSaida + "\\mergeSort_" + caso + ".csv");

            // HeapSort
            System.out.println("Ordenando com HeapSort...");
            List<Video> copiaHeap = new ArrayList<>(videos);
            long inicioHeap = System.currentTimeMillis();
            heapSort(copiaHeap);
            long fimHeap = System.currentTimeMillis();
            System.out.println("HeapSort (" + caso + ") concluído em " + (fimHeap -
                    inicioHeap) + " ms.");
            salvarVideos(copiaHeap, pastaSaida + "\\heapSort_" + caso + ".csv");

            // QuickSort com Mediana de Três
            System.out.println("Ordenando com QuickSort (Mediana de Três)...");
            List<Video> copiaQuickMediana = new ArrayList<>(videos);
            long inicioQuickMediana = System.currentTimeMillis();
            quickSortMedianaDeTres(copiaQuickMediana, 0, copiaQuickMediana.size() - 1);
            long fimQuickMediana = System.currentTimeMillis();
            System.out.println("QuickSort Mediana de Três (" + caso + ") concluído em "
                    + (fimQuickMediana - inicioQuickMediana) + " ms.");
            salvarVideos(copiaQuickMediana, pastaSaida + "\\quickSortMediana3_" + caso + ".csv");

            // Selection Sort
            // System.out.println("Ordenando com Selection Sort...");
            // List<Video> copiaSelection = new ArrayList<>(videos);
            // long inicioSelection = System.currentTimeMillis();
            // selectionSort(copiaSelection);
            // long fimSelection = System.currentTimeMillis();
            // System.out
            // .println("Selection Sort (" + caso + ") concluído em " + (fimSelection -
            // inicioSelection) + " ms.");
            // salvarVideos(copiaSelection, pastaSaida + "\\selectionSort_" + caso +
            // ".csv");

            // Insertion Sort
            // System.out.println("Ordenando com Insertion Sort...");
            // List<Video> copiaInsertion = new ArrayList<>(videos);
            // long inicioInsertion = System.currentTimeMillis();
            // insertionSort(copiaInsertion);
            // long fimInsertion = System.currentTimeMillis();
            // System.out
            // .println("Insertion Sort (" + caso + ") concluído em " + (fimInsertion -
            // inicioInsertion) + " ms.");
            // salvarVideos(copiaInsertion, pastaSaida + "\\insertionSort_" + caso +
            // ".csv");

            // Counting Sort
            // System.out.println("Ordenando com Counting Sort...");
            // List<Video> copiaCounting = new ArrayList<>(videos);
            // long inicioCounting = System.currentTimeMillis();
            // countingSort(copiaCounting);
            // long fimCounting = System.currentTimeMillis();
            // System.out.println("Counting Sort (" + caso + ") concluído em " +
            // (fimCounting - inicioCounting) + " ms.");
            // salvarVideos(copiaCounting, pastaSaida + "\\countingSort_" + caso + ".csv");

            System.out.println("Finalizado processamento de: " + caso);
        }

        System.out.println("\n=== Processamento completo! ===");
    }

    // Demais métodos utilitários e algoritmos permanecem abaixo

    private static List<Video> lerVideos(String caminho) {
        List<Video> videos = new ArrayList<>();
        int totalLidas = 0, validas = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(caminho))) {
            br.readLine(); // ignora cabeçalho
            String linha;
            while ((linha = br.readLine()) != null) {
                totalLidas++;
                List<String> campos = parseCSVLine(linha);

                if (campos.size() > 17) {
                    String dataTexto = campos.get(17).trim();
                    try {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                        LocalDate dataTrending = LocalDate.parse(dataTexto, formatter);
                        videos.add(new Video(linha, dataTrending));
                        validas++;
                    } catch (Exception e) {
                        // System.out.println(
                        // "⚠️ Erro ao processar data na linha " + totalLidas + ": \"" + dataTexto +
                        // "\"");
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao ler arquivo: " + e.getMessage());
        }

        System.out.println("Linhas lidas: " + totalLidas);
        System.out.println("Linhas válidas com data convertida: " + validas);

        return videos;
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

    private static void salvarVideos(List<Video> videos, String caminho) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(caminho))) {
            bw.write(
                    "video_id,trending_date,title,channel_title,category_id,publish_time,tags,views,likes,dislikes,comment_count,thumbnail_link,comments_disabled,ratings_disabled,video_error_or_removed,description,countries,trending_full_date");
            bw.newLine();
            for (Video v : videos) {
                bw.write(v.getLinhaCompleta());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Erro ao salvar arquivo: " + e.getMessage());
        }
    }

    private static void quickSort(List<Video> lista, int inicio, int fim) {
        while (inicio < fim) {
            int p = particionar(lista, inicio, fim);
            if (p - inicio < fim - p) {
                quickSort(lista, inicio, p - 1);
                inicio = p + 1;
            } else {
                quickSort(lista, p + 1, fim);
                fim = p - 1;
            }
        }
    }

    private static int particionar(List<Video> lista, int inicio, int fim) {
        int pivoIndex = new Random().nextInt(fim - inicio + 1) + inicio;
        Collections.swap(lista, pivoIndex, fim);

        Video pivo = lista.get(fim);
        int i = inicio - 1;
        for (int j = inicio; j < fim; j++) {
            if (lista.get(j).compareTo(pivo) <= 0) {
                i++;
                Collections.swap(lista, i, j);
            }
        }
        Collections.swap(lista, i + 1, fim);
        return i + 1;
    }

    private static List<Video> mergeSort(List<Video> lista) {
        if (lista.size() <= 1)
            return lista;

        int meio = lista.size() / 2;
        List<Video> esquerda = mergeSort(new ArrayList<>(lista.subList(0, meio)));
        List<Video> direita = mergeSort(new ArrayList<>(lista.subList(meio, lista.size())));

        return merge(esquerda, direita);
    }

    private static List<Video> merge(List<Video> esquerda, List<Video> direita) {
        List<Video> resultado = new ArrayList<>();
        int i = 0, j = 0;
        while (i < esquerda.size() && j < direita.size()) {
            if (esquerda.get(i).compareTo(direita.get(j)) <= 0) {
                resultado.add(esquerda.get(i++));
            } else {
                resultado.add(direita.get(j++));
            }
        }
        resultado.addAll(esquerda.subList(i, esquerda.size()));
        resultado.addAll(direita.subList(j, direita.size()));
        return resultado;
    }

    private static void heapSort(List<Video> lista) {
        int n = lista.size();
        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(lista, n, i);
        }
        for (int i = n - 1; i > 0; i--) {
            Collections.swap(lista, 0, i);
            heapify(lista, i, 0);
        }
    }

    private static void heapify(List<Video> lista, int n, int i) {
        int maior = i;
        int esquerda = 2 * i + 1;
        int direita = 2 * i + 2;

        if (esquerda < n && lista.get(esquerda).compareTo(lista.get(maior)) > 0) {
            maior = esquerda;
        }

        if (direita < n && lista.get(direita).compareTo(lista.get(maior)) > 0) {
            maior = direita;
        }

        if (maior != i) {
            Collections.swap(lista, i, maior);
            heapify(lista, n, maior);
        }
    }

    private static void quickSortMedianaDeTres(List<Video> lista, int inicio, int fim) {
        while (inicio < fim) {
            int p = particionarMedianaDeTres(lista, inicio, fim);
            if (p - inicio < fim - p) {
                quickSortMedianaDeTres(lista, inicio, p - 1);
                inicio = p + 1;
            } else {
                quickSortMedianaDeTres(lista, p + 1, fim);
                fim = p - 1;
            }
        }
    }

    private static int particionarMedianaDeTres(List<Video> lista, int inicio, int fim) {
        int meio = (inicio + fim) / 2;

        // Mediana de três: ordena início, meio e fim
        if (lista.get(meio).compareTo(lista.get(inicio)) < 0)
            Collections.swap(lista, inicio, meio);
        if (lista.get(fim).compareTo(lista.get(inicio)) < 0)
            Collections.swap(lista, inicio, fim);
        if (lista.get(fim).compareTo(lista.get(meio)) < 0)
            Collections.swap(lista, meio, fim);

        // Usa o meio como pivô (agora está ordenado)
        Collections.swap(lista, meio, fim);
        Video pivo = lista.get(fim);

        int i = inicio - 1;
        for (int j = inicio; j < fim; j++) {
            if (lista.get(j).compareTo(pivo) <= 0) {
                i++;
                Collections.swap(lista, i, j);
            }
        }

        Collections.swap(lista, i + 1, fim);
        return i + 1;
    }

    private static void selectionSort(List<Video> lista) {
        int n = lista.size();
        for (int i = 0; i < n - 1; i++) {
            int indiceMin = i;
            for (int j = i + 1; j < n; j++) {
                if (lista.get(j).compareTo(lista.get(indiceMin)) < 0) {
                    indiceMin = j;
                }
            }
            if (indiceMin != i) {
                Collections.swap(lista, i, indiceMin);
            }
        }
    }

    private static void insertionSort(List<Video> lista) {
        for (int i = 1; i < lista.size(); i++) {
            Video atual = lista.get(i);
            int j = i - 1;

            while (j >= 0 && lista.get(j).compareTo(atual) > 0) {
                lista.set(j + 1, lista.get(j));
                j--;
            }
            lista.set(j + 1, atual);
        }
    }

    private static void countingSort(List<Video> lista) {
        if (lista.isEmpty())
            return;

        long min = lista.stream().mapToLong(v -> v.getTrendingFullDate().toEpochDay()).min().orElse(0);
        long max = lista.stream().mapToLong(v -> v.getTrendingFullDate().toEpochDay()).max().orElse(0);

        int tamanho = (int) (max - min + 1);
        List<List<Video>> contagem = new ArrayList<>(Collections.nCopies(tamanho, null));

        for (int i = 0; i < tamanho; i++) {
            contagem.set(i, new ArrayList<>());
        }

        for (Video v : lista) {
            int index = (int) (v.getTrendingFullDate().toEpochDay() - min);
            contagem.get(index).add(v);
        }

        lista.clear();
        for (List<Video> bucket : contagem) {
            lista.addAll(bucket);
        }
    }

    static class Video implements Comparable<Video> {
        private final String linhaCompleta;
        private final LocalDate trendingFullDate;

        public Video(String linhaCompleta, LocalDate trendingFullDate) {
            this.linhaCompleta = linhaCompleta;
            this.trendingFullDate = trendingFullDate;
        }

        public String getLinhaCompleta() {
            return linhaCompleta;
        }

        public LocalDate getTrendingFullDate() {
            return trendingFullDate;
        }

        @Override
        public int compareTo(Video outro) {
            return this.trendingFullDate.compareTo(outro.trendingFullDate);
        }
    }

}
