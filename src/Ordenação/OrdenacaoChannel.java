package ordenation;

import java.io.*;
import java.util.*;

public class OrdenacaoChannel {

    public static void main(String[] args) {
        String baseDir = "C:\\Users\\vinic\\OneDrive\\Área de Trabalho\\Projeto Java\\";
        String pastaSaida = baseDir + "Ordenação Canais";

        new File(pastaSaida).mkdirs(); // cria a pasta se não existir

        String[] nomesArquivos = {
                "Canais Ordenados\\videos_T1_channel_title_crescente.csv",
                "Canais Ordenados\\videos_T1_channel_title_decrescente.csv",
                "videos_T1.csv"
        };

        String[] casos = { "melhorCaso", "piorCaso", "medioCaso" };

        for (int i = 0; i < nomesArquivos.length; i++) {
            String nomeArquivo = nomesArquivos[i];
            String caso = casos[i];

            System.out.println("Lendo vídeos de: " + nomeArquivo);
            List<Video> videos = lerVideos(baseDir + nomeArquivo);
            System.out.println("Total de vídeos lidos: " + videos.size());

            if (videos.isEmpty()) {
                System.out.println("Nenhum vídeo encontrado em " + nomeArquivo);
                continue;
            }

            // QuickSort
            List<Video> copiaQuick = new ArrayList<>(videos);
            System.out.println("Iniciando QuickSort para " + caso);
            long inicioQuick = System.currentTimeMillis();
            quickSort(copiaQuick, 0, copiaQuick.size() - 1);
            long fimQuick = System.currentTimeMillis();
            System.out.println("QuickSort (" + caso + ") concluído em " + (fimQuick -
                    inicioQuick) + " ms.");
            salvarVideos(copiaQuick, pastaSaida + "\\videos_T1_channel_title_quickSort_"
                    + caso + ".csv");

            // MergeSort
            List<Video> copiaMerge = new ArrayList<>(videos);
            System.out.println("Iniciando MergeSort para " + caso);
            long inicioMerge = System.currentTimeMillis();
            List<Video> ordenadosMerge = mergeSort(copiaMerge);
            long fimMerge = System.currentTimeMillis();
            System.out.println("MergeSort (" + caso + ") concluído em " + (fimMerge -
                    inicioMerge) + " ms.");
            salvarVideos(ordenadosMerge, pastaSaida +
                    "\\videos_T1_channel_title_mergeSort_" + caso + ".csv");

            // QuickSort com Mediana de 3
            List<Video> copiaQuickMediana3 = new ArrayList<>(videos);
            System.out.println("Iniciando QuickSort (Mediana de 3) para " + caso);
            long inicioQuickM3 = System.currentTimeMillis();
            quickSortMediana3(copiaQuickMediana3, 0, copiaQuickMediana3.size() - 1);
            long fimQuickM3 = System.currentTimeMillis();
            System.out.println(
                    "QuickSort (Mediana de 3) (" + caso + ") concluído em " + (fimQuickM3 -
                            inicioQuickM3) + " ms.");
            salvarVideos(copiaQuickMediana3,
                    pastaSaida + "\\videos_T1_channel_title_quickSortMediana3_" + caso + ".csv");

            // HeapSort
            List<Video> copiaHeap = new ArrayList<>(videos);
            System.out.println("Iniciando HeapSort para " + caso);
            long inicioHeap = System.currentTimeMillis();
            heapSort(copiaHeap);
            long fimHeap = System.currentTimeMillis();
            System.out.println("HeapSort (" + caso + ") concluído em " + (fimHeap -
                    inicioHeap) + " ms.");
            salvarVideos(copiaHeap, pastaSaida + "\\videos_T1_channel_title_heapSort_" +
                    caso + ".csv");

            // InsertionSort
            // List<Video> copiaInsertion = new ArrayList<>(videos);
            // System.out.println("Iniciando InsertionSort para " + caso);
            // long inicioInsertion = System.currentTimeMillis();
            // insertionSort(copiaInsertion);
            // long fimInsertion = System.currentTimeMillis();
            // System.out
            // .println("InsertionSort (" + caso + ") concluído em " + (fimInsertion -
            // inicioInsertion) + " ms.");
            // salvarVideos(copiaInsertion, pastaSaida +
            // "\\videos_T1_channel_title_insertionSort_" + caso + ".csv");

            // SelectionSort
            // List<Video> copiaSelection = new ArrayList<>(videos);
            // System.out.println("Iniciando SelectionSort para " + caso);
            // long inicioSelection = System.currentTimeMillis();
            // selectionSort(copiaSelection);
            // long fimSelection = System.currentTimeMillis();
            // System.out
            // .println("SelectionSort (" + caso + ") concluído em " + (fimSelection -
            // inicioSelection) + " ms.");
            // salvarVideos(copiaSelection, pastaSaida +
            // "\\videos_T1_channel_title_selectionSort_" + caso + ".csv");

        }

        System.out.println("\nProcessamento completo!");
    }

    private static List<Video> lerVideos(String caminho) {
        List<Video> videos = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(caminho))) {
            br.readLine(); // ignora cabeçalho
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] campos = linha.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                if (campos.length > 3 && !campos[3].trim().isEmpty()) {
                    String canal = campos[3].trim().replaceAll("^\"|\"$", "");
                    videos.add(new Video(linha, canal));
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao ler arquivo: " + e.getMessage());
        }
        return videos;
    }

    private static void salvarVideos(List<Video> videos, String caminho) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(caminho))) {
            // cabeçalho
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
        if (inicio < fim) {
            int p = particionar(lista, inicio, fim);
            quickSort(lista, inicio, p - 1);
            quickSort(lista, p + 1, fim);
        }
    }

    private static int particionar(List<Video> lista, int inicio, int fim) {
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

    private static void quickSortMediana3(List<Video> lista, int inicio, int fim) {
        if (inicio < fim) {
            int p = particionarMediana3(lista, inicio, fim);
            quickSortMediana3(lista, inicio, p - 1);
            quickSortMediana3(lista, p + 1, fim);
        }
    }

    private static int particionarMediana3(List<Video> lista, int inicio, int fim) {
        int meio = (inicio + fim) / 2;

        // Ordena início, meio e fim para encontrar a mediana
        if (lista.get(meio).compareTo(lista.get(inicio)) < 0)
            Collections.swap(lista, inicio, meio);
        if (lista.get(fim).compareTo(lista.get(inicio)) < 0)
            Collections.swap(lista, inicio, fim);
        if (lista.get(fim).compareTo(lista.get(meio)) < 0)
            Collections.swap(lista, meio, fim);

        // Move a mediana para fim - 1 e usa como pivô
        Collections.swap(lista, meio, fim - 1);
        Video pivo = lista.get(fim - 1);

        int i = inicio;
        int j = fim - 1;

        while (true) {
            while (lista.get(++i).compareTo(pivo) < 0)
                ;
            while (lista.get(--j).compareTo(pivo) > 0)
                ;
            if (i >= j)
                break;
            Collections.swap(lista, i, j);
        }

        Collections.swap(lista, i, fim - 1); // coloca pivô na posição correta
        return i;
    }

    private static void heapSort(List<Video> lista) {
        int n = lista.size();

        // Constrói o heap (reorganiza o array)
        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(lista, n, i);
        }

        // Extrai os elementos do heap um por um
        for (int i = n - 1; i > 0; i--) {
            Collections.swap(lista, 0, i); // move raiz para o final
            heapify(lista, i, 0); // chama heapify na heap reduzida
        }
    }

    private static void heapify(List<Video> lista, int n, int i) {
        int maior = i; // raiz
        int esquerda = 2 * i + 1;
        int direita = 2 * i + 2;

        // Se filho da esquerda é maior que raiz
        if (esquerda < n && lista.get(esquerda).compareTo(lista.get(maior)) > 0) {
            maior = esquerda;
        }

        // Se filho da direita é maior que maior até agora
        if (direita < n && lista.get(direita).compareTo(lista.get(maior)) > 0) {
            maior = direita;
        }

        // Se maior não for raiz
        if (maior != i) {
            Collections.swap(lista, i, maior);
            heapify(lista, n, maior);
        }
    }

    private static void insertionSort(List<Video> lista) {
        int n = lista.size();

        // Percorre a lista a partir do segundo elemento (índice 1)
        for (int i = 1; i < n; i++) {
            Video chave = lista.get(i); // O vídeo que será inserido na posição correta
            int j = i - 1;

            // Move os elementos da lista que são maiores que a chave para uma posição à
            // frente
            while (j >= 0 && lista.get(j).compareTo(chave) > 0) {
                lista.set(j + 1, lista.get(j)); // Move o elemento uma posição à frente
                j--;
            }

            // Coloca a chave na posição correta
            lista.set(j + 1, chave);
        }
    }

    private static void selectionSort(List<Video> lista) {
        int n = lista.size();
        for (int i = 0; i < n - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < n; j++) {
                if (lista.get(j).compareTo(lista.get(minIndex)) < 0) {
                    minIndex = j;
                }
            }
            Collections.swap(lista, i, minIndex);
        }
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

    static class Video implements Comparable<Video> {
        private final String linhaCompleta;
        public final String canal;

        public Video(String linhaCompleta, String canal) {
            this.linhaCompleta = linhaCompleta;
            this.canal = canal;
        }

        public String getLinhaCompleta() {
            return linhaCompleta;
        }

        @Override
        public int compareTo(Video outro) {
            return this.canal.compareToIgnoreCase(outro.canal);
        }
    }
}
