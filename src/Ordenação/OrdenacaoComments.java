package ordenation;

import java.io.*;
import java.util.*;

public class OrdenacaoComments {

    public static void main(String[] args) {
        String baseDir = "C:\\Users\\vinic\\OneDrive\\Área de Trabalho\\Projeto Java\\";
        String pastaSaida = baseDir + "ordenação Comments";

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

            // // QuickSort
            System.out.println("Ordenando com QuickSort...");
            List<Video> copiaQuick = new ArrayList<>(videos);
            long inicioQuick = System.currentTimeMillis();
            quickSort(copiaQuick, 0, copiaQuick.size() - 1);
            long fimQuick = System.currentTimeMillis();
            System.out.println("QuickSort (" + caso + ") concluído em " + (fimQuick -
                    inicioQuick) + " ms.");
            salvarVideos(copiaQuick, pastaSaida + "\\quickSort_" + caso + ".csv");

            // // MergeSort
            System.out.println("Ordenando com MergeSort...");
            long inicioMerge = System.currentTimeMillis();
            List<Video> ordenadosMerge = mergeSort(new ArrayList<>(videos));
            long fimMerge = System.currentTimeMillis();
            System.out.println("MergeSort (" + caso + ") concluído em " + (fimMerge -
                    inicioMerge) + " ms.");
            salvarVideos(ordenadosMerge, pastaSaida + "\\mergeSort_" + caso + ".csv");

            // // HeapSort
            System.out.println("Ordenando com HeapSort...");
            List<Video> copiaHeap = new ArrayList<>(videos);
            long inicioHeap = System.currentTimeMillis();
            heapSort(copiaHeap);
            long fimHeap = System.currentTimeMillis();
            System.out.println("HeapSort (" + caso + ") concluído em " + (fimHeap -
                    inicioHeap) + " ms.");
            salvarVideos(copiaHeap, pastaSaida + "\\heapSort_" + caso + ".csv");

            // QuickSort com mediana de três
            System.out.println("Ordenando com QuickSort (mediana de 3)...");
            List<Video> copiaQuick3 = new ArrayList<>(videos);
            long inicioQuick3 = System.currentTimeMillis();
            quickSortMedianaDeTres(copiaQuick3, 0, copiaQuick3.size() - 1);
            long fimQuick3 = System.currentTimeMillis();
            System.out.println(
                    "QuickSort (mediana de 3 - " + caso + ") concluído em " + (fimQuick3 -
                            inicioQuick3) + " ms.");
            salvarVideos(copiaQuick3, pastaSaida + "\\quickSortMediana3_" + caso +
                    ".csv");

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
            // long inicioCounting = System.currentTimeMillis();
            // List<Video> copiaCounting = new ArrayList<>(videos);
            // countingSort(copiaCounting);
            // long fimCounting = System.currentTimeMillis();
            // System.out.println("Counting Sort (" + caso + ") concluído em " +
            // (fimCounting - inicioCounting) + " ms.");
            // salvarVideos(copiaCounting, pastaSaida + "\\countingSort_" + caso + ".csv");

            // Selection Sort
            // System.out.println("Ordenando com Selection Sort...");
            // long inicioSelection = System.currentTimeMillis();
            // List<Video> copiaSelection = new ArrayList<>(videos);
            // selectionSort(copiaSelection);
            // long fimSelection = System.currentTimeMillis();
            // System.out
            // .println("Selection Sort (" + caso + ") concluído em " + (fimSelection -
            // inicioSelection) + " ms.");
            // salvarVideos(copiaSelection, pastaSaida + "\\selectionSort_" + caso +
            // ".csv");

            System.out.println("Finalizado processamento de: " + caso);
        }

        System.out.println("\n=== Processamento completo! ===");
    }

    private static List<Video> lerVideos(String caminho) {
        List<Video> videos = new ArrayList<>();
        int totalLidas = 0, ignoradas = 0, validas = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(caminho))) {
            br.readLine(); // ignora cabeçalho
            String linha;
            while ((linha = br.readLine()) != null) {
                totalLidas++;
                List<String> campos = parseCSVLine(linha);

                if (campos.size() > 10 && !campos.get(10).trim().isEmpty()) {
                    try {
                        int commentCount = Integer.parseInt(campos.get(10).trim());

                        String descricao = campos.size() > 14 ? campos.get(14) : "";
                        String regexCaracteresInvalidos = ".*[\\u00AD\\u200B-\\u200D\\uFEFF\\u2028\\u2029\\u00A0].*";

                        if (!descricao.matches(regexCaracteresInvalidos)) {
                            videos.add(new Video(linha, commentCount));
                            validas++;
                        } else {
                            ignoradas++;
                        }
                    } catch (NumberFormatException e) {
                        ignoradas++;
                    }
                } else {
                    ignoradas++;
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao ler arquivo: " + e.getMessage());
        }

        System.out.println("Linhas lidas: " + totalLidas);
        System.out.println("Linhas válidas com comment_count: " + validas);
        System.out.println("Linhas ignoradas: " + ignoradas);

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
        Collections.swap(lista, pivoIndex, fim); // coloca o pivô no fim

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

        Video a = lista.get(inicio);
        Video b = lista.get(meio);
        Video c = lista.get(fim);

        int medianaIndex;
        if ((a.compareTo(b) <= 0 && b.compareTo(c) <= 0) || (c.compareTo(b) <= 0 && b.compareTo(a) <= 0)) {
            medianaIndex = meio;
        } else if ((b.compareTo(a) <= 0 && a.compareTo(c) <= 0) || (c.compareTo(a) <= 0 && a.compareTo(b) <= 0)) {
            medianaIndex = inicio;
        } else {
            medianaIndex = fim;
        }

        Collections.swap(lista, medianaIndex, fim);

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

    private static void insertionSort(List<Video> lista) {
        for (int i = 1; i < lista.size(); i++) {
            Video chave = lista.get(i);
            int j = i - 1;

            // Move os elementos maiores que a chave uma posição à frente
            while (j >= 0 && lista.get(j).compareTo(chave) > 0) {
                lista.set(j + 1, lista.get(j));
                j--;
            }
            lista.set(j + 1, chave);
        }
    }

    private static void countingSort(List<Video> lista) {
        if (lista.isEmpty())
            return;

        // Encontrar o maior valor de commentCount
        int max = lista.get(0).commentCount;
        for (Video v : lista) {
            max = Math.max(max, v.commentCount);
        }

        // Inicializar o array de contagem
        int[] count = new int[max + 1];

        // Contar as ocorrências de cada valor
        for (Video v : lista) {
            count[v.commentCount]++;
        }

        // Reconstruir a lista ordenada
        List<Video> resultado = new ArrayList<>();
        for (int i = 0; i <= max; i++) {
            for (int j = 0; j < count[i]; j++) {
                for (Video v : lista) {
                    if (v.commentCount == i) {
                        resultado.add(v);
                        break;
                    }
                }
            }
        }

        // Atualizar a lista com a ordenação
        lista.clear();
        lista.addAll(resultado);
    }

    private static void selectionSort(List<Video> lista) {
        int n = lista.size();
        for (int i = 0; i < n - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < n; j++) {
                if (lista.get(j).commentCount < lista.get(minIndex).commentCount) {
                    minIndex = j;
                }
            }
            // Troca o elemento mínimo com o elemento atual
            Collections.swap(lista, i, minIndex);
        }
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

    static class Video implements Comparable<Video> {
        private final String linhaCompleta;
        public final int commentCount;

        public Video(String linhaCompleta, int commentCount) {
            this.linhaCompleta = linhaCompleta;
            this.commentCount = commentCount;
        }

        public String getLinhaCompleta() {
            return linhaCompleta;
        }

        @Override
        public int compareTo(Video outro) {
            return Integer.compare(this.commentCount, outro.commentCount);
        }
    }
}
