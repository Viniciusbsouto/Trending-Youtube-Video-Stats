package ordenation;

import java.io.*;
import java.util.*;

public class OrdenacaoChannel {

    public static void main(String[] args) {
        String baseDir = System.getProperty("user.dir") + File.separator;
        String pastaSaida = baseDir + "Ordenação Canais" + File.separator;

        // Cria a pasta de saída se ela não existir
        new File(pastaSaida).mkdirs();

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
            Video[] videos = lerVideos(baseDir + nomeArquivo);
            System.out.println("Total de vídeos lidos: " + videos.length);

            if (videos.length == 0) {
                System.out.println("Nenhum vídeo encontrado em " + nomeArquivo);
                continue;
            }

            // QuickSort
            System.out.println("Iniciando QuickSort para " + caso);
            Video[] copiaQuick = Arrays.copyOf(videos, videos.length);
            long inicioQuick = System.currentTimeMillis();
            quickSort(copiaQuick, 0, copiaQuick.length - 1);
            long fimQuick = System.currentTimeMillis();
            System.out.println("QuickSort (" + caso + ") concluído em "
                    + (fimQuick - inicioQuick) + " ms.");
            salvarVideos(copiaQuick, pastaSaida
                    + "\\videos_T1_channel_title_quickSort_" + caso + ".csv");

            // MergeSort
            System.out.println("Iniciando MergeSort para " + caso);
            Video[] copiaMerge = Arrays.copyOf(videos, videos.length);
            long inicioMerge = System.currentTimeMillis();
            Video[] ordenadosMerge = mergeSort(copiaMerge);
            long fimMerge = System.currentTimeMillis();
            System.out.println("MergeSort (" + caso + ") concluído em "
                    + (fimMerge - inicioMerge) + " ms.");
            salvarVideos(ordenadosMerge, pastaSaida
                    + "\\videos_T1_channel_title_mergeSort_" + caso + ".csv");

            // QuickSort Mediana de Três
            System.out.println("Iniciando QuickSort (Mediana de 3) para " + caso);
            Video[] copiaQuickMediana3 = Arrays.copyOf(videos, videos.length);
            long inicioQuickM3 = System.currentTimeMillis();
            quickSortMediana3(copiaQuickMediana3, 0, copiaQuickMediana3.length - 1);
            long fimQuickM3 = System.currentTimeMillis();
            System.out.println("QuickSort (Mediana de 3) (" + caso
                    + ") concluído em " + (fimQuickM3 - inicioQuickM3) + " ms.");
            salvarVideos(copiaQuickMediana3, pastaSaida
                    + "\\videos_T1_channel_title_quickSortMediana3_" + caso + ".csv");

            // HeapSort
            System.out.println("Iniciando HeapSort para " + caso);
            Video[] copiaHeap = Arrays.copyOf(videos, videos.length);
            long inicioHeap = System.currentTimeMillis();
            heapSort(copiaHeap);
            long fimHeap = System.currentTimeMillis();
            System.out.println("HeapSort (" + caso + ") concluído em "
                    + (fimHeap - inicioHeap) + " ms.");
            salvarVideos(copiaHeap, pastaSaida
                    + "\\videos_T1_channel_title_heapSort_" + caso + ".csv");

            // Insertion Sort
            System.out.println("Iniciando InsertionSort para " + caso);
            Video[] copiaInsertion = Arrays.copyOf(videos, videos.length);
            long inicioInsertion = System.currentTimeMillis();
            insertionSort(copiaInsertion);
            long fimInsertion = System.currentTimeMillis();
            System.out.println("InsertionSort (" + caso
                    + ") concluído em " + (fimInsertion - inicioInsertion) + " ms.");
            salvarVideos(copiaInsertion, pastaSaida
                    + "\\videos_T1_channel_title_insertionSort_" + caso + ".csv");

            // Selection Sort
            System.out.println("Iniciando SelectionSort para " + caso);
            Video[] copiaSelection = Arrays.copyOf(videos, videos.length);
            long inicioSelection = System.currentTimeMillis();
            selectionSort(copiaSelection);
            long fimSelection = System.currentTimeMillis();
            System.out.println("SelectionSort (" + caso
                    + ") concluído em " + (fimSelection - inicioSelection) + " ms.");
            salvarVideos(copiaSelection, pastaSaida
                    + "\\videos_T1_channel_title_selectionSort_" + caso + ".csv");
        }

        System.out.println("\nProcessamento completo!");
    }

    private static Video[] lerVideos(String caminho) {
        int count = 0;
        // Primeira passagem: contar linhas válidas
        try (BufferedReader br = new BufferedReader(new FileReader(caminho))) {
            br.readLine();
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] campos = linha.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                if (campos.length > 3 && !campos[3].trim().isEmpty()) {
                    count++;
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao ler arquivo: " + e.getMessage());
        }

        // Aloca o array com o tamanho exato
        Video[] videos = new Video[count];
        int index = 0;
        // Segunda passagem: ler e armazenar os vídeos
        try (BufferedReader br = new BufferedReader(new FileReader(caminho))) {
            br.readLine(); // ignora cabeçalho
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] campos = linha.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                if (campos.length > 3 && !campos[3].trim().isEmpty()) {
                    String canal = campos[3].trim().replaceAll("^\"|\"$", "");
                    videos[index++] = new Video(linha, canal);
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao ler arquivo: " + e.getMessage());
        }
        return videos;
    }

    private static void salvarVideos(Video[] videos, String caminho) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(caminho))) {
            // Cabeçalho do arquivo CSV
            bw.write(
                    "video_id,trending_date,title,channel_title,category_id,publish_time,tags,views,likes,dislikes,comment_count,thumbnail_link,comments_disabled,ratings_disabled,video_error_or_removed,description,countries,trending_full_date");
            bw.newLine();

            // Percorre o array e escreve cada linha no arquivo
            for (Video v : videos) {
                bw.write(v.getLinhaCompleta());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Erro ao salvar arquivo: " + e.getMessage());
        }
    }

    private static void quickSort(Video[] array, int inicio, int fim) {
        if (inicio < fim) {
            int p = particionar(array, inicio, fim);
            quickSort(array, inicio, p - 1);
            quickSort(array, p + 1, fim);
        }
    }

    private static int particionar(Video[] array, int inicio, int fim) {
        Video pivo = array[fim];
        int i = inicio - 1;
        for (int j = inicio; j < fim; j++) {
            if (array[j].compareTo(pivo) <= 0) {
                i++;
                swap(array, i, j);
            }
        }
        swap(array, i + 1, fim);
        return i + 1;
    }

    private static void swap(Video[] array, int i, int j) {
        Video temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    private static Video[] mergeSort(Video[] array) {
        if (array.length <= 1)
            return array;

        int meio = array.length / 2;
        Video[] esquerda = mergeSort(Arrays.copyOfRange(array, 0, meio));
        Video[] direita = mergeSort(Arrays.copyOfRange(array, meio, array.length));

        return merge(esquerda, direita);
    }

    private static Video[] merge(Video[] esquerda, Video[] direita) {
        int i = 0, j = 0, k = 0;
        Video[] resultado = new Video[esquerda.length + direita.length];

        while (i < esquerda.length && j < direita.length) {
            if (esquerda[i].compareTo(direita[j]) <= 0) {
                resultado[k++] = esquerda[i++];
            } else {
                resultado[k++] = direita[j++];
            }
        }

        while (i < esquerda.length) {
            resultado[k++] = esquerda[i++];
        }

        while (j < direita.length) {
            resultado[k++] = direita[j++];
        }

        return resultado;
    }

    private static void quickSortMediana3(Video[] array, int inicio, int fim) {
        if (inicio < fim) {
            int p = particionarMediana3(array, inicio, fim);
            quickSortMediana3(array, inicio, p - 1);
            quickSortMediana3(array, p + 1, fim);
        }
    }

    private static int particionarMediana3(Video[] array, int inicio, int fim) {
        int meio = (inicio + fim) / 2;

        // Ordena início, meio e fim para encontrar a mediana
        if (array[meio].compareTo(array[inicio]) < 0)
            swap(array, inicio, meio);
        if (array[fim].compareTo(array[inicio]) < 0)
            swap(array, inicio, fim);
        if (array[fim].compareTo(array[meio]) < 0)
            swap(array, meio, fim);

        // Move a mediana para fim - 1 e usa como pivô
        swap(array, meio, fim - 1);
        Video pivo = array[fim - 1];

        int i = inicio;
        int j = fim - 1;

        while (true) {
            while (array[++i].compareTo(pivo) < 0)
                ;
            while (array[--j].compareTo(pivo) > 0)
                ;
            if (i >= j)
                break;
            swap(array, i, j);
        }

        swap(array, i, fim - 1); // coloca pivô na posição correta
        return i;
    }

    private static void heapSort(Video[] array) {
        int n = array.length;

        // Constrói o heap (reorganiza o array)
        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(array, n, i);
        }

        // Extrai os elementos do heap um por um
        for (int i = n - 1; i > 0; i--) {
            swap(array, 0, i); // move raiz para o final
            heapify(array, i, 0); // chama heapify na heap reduzida
        }
    }

    private static void heapify(Video[] array, int n, int i) {
        int maior = i; // raiz
        int esquerda = 2 * i + 1;
        int direita = 2 * i + 2;

        // Se filho da esquerda é maior que raiz
        if (esquerda < n && array[esquerda].compareTo(array[maior]) > 0) {
            maior = esquerda;
        }

        // Se filho da direita é maior que maior até agora
        if (direita < n && array[direita].compareTo(array[maior]) > 0) {
            maior = direita;
        }

        // Se maior não for raiz
        if (maior != i) {
            swap(array, i, maior);
            heapify(array, n, maior);
        }
    }

    private static void insertionSort(Video[] array) {
        int n = array.length;

        // Percorre o array a partir do segundo elemento (índice 1)
        for (int i = 1; i < n; i++) {
            Video chave = array[i]; // O vídeo que será inserido na posição correta
            int j = i - 1;

            // Move os elementos do array que são maiores que a chave para uma posição à
            // frente
            while (j >= 0 && array[j].compareTo(chave) > 0) {
                array[j + 1] = array[j]; // Move o elemento uma posição à frente
                j--;
            }

            // Coloca a chave na posição correta
            array[j + 1] = chave;
        }
    }

    private static void selectionSort(Video[] array) {
        int n = array.length;
        for (int i = 0; i < n - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < n; j++) {
                if (array[j].compareTo(array[minIndex]) < 0) {
                    minIndex = j;
                }
            }
            swap(array, i, minIndex);
        }
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
