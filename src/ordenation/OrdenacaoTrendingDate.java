package ordenation;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class OrdenacaoTrendingDate {

    public static void main(String[] args) {
        String baseDir = "C:\\Users\\vinic\\OneDrive\\Área de Trabalho\\Projeto Java\\";
        String pastaSaida = baseDir + "Ordenação Datas";

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

            Video[] videos = lerVideos(baseDir + nomeArquivo);

            if (videos.length == 0) {
                System.out.println("Nenhum vídeo encontrado para " + caso + ", pulando...");
                continue;
            }
            System.out.println("Total de vídeos lidos: " + videos.length);

            // QuickSort
            System.out.println("Ordenando com QuickSort...");
            Video[] copiaQuick = Arrays.copyOf(videos, videos.length);
            long inicioQuick = System.currentTimeMillis();
            quickSort(copiaQuick, 0, copiaQuick.length - 1);
            long fimQuick = System.currentTimeMillis();
            System.out.println("QuickSort (" + caso + ") concluído em " + (fimQuick - inicioQuick) + " ms.");
            salvarVideos(copiaQuick, pastaSaida + "\\quickSort_" + caso + ".csv");

            // MergeSort
            System.out.println("Ordenando com MergeSort...");
            Video[] copiaMerge = Arrays.copyOf(videos, videos.length);
            long inicioMerge = System.currentTimeMillis();
            Video[] ordenadosMerge = mergeSort(copiaMerge);
            long fimMerge = System.currentTimeMillis();
            System.out.println("MergeSort (" + caso + ") concluído em " + (fimMerge -
                    inicioMerge) + " ms.");
            salvarVideos(ordenadosMerge, pastaSaida + "\\mergeSort_" + caso + ".csv");

            /// HeapSort
            System.out.println("Ordenando com HeapSort...");
            Video[] copiaHeap = Arrays.copyOf(videos, videos.length);
            long inicioHeap = System.currentTimeMillis();
            heapSort(copiaHeap);
            long fimHeap = System.currentTimeMillis();
            System.out.println("HeapSort (" + caso + ") concluído em " + (fimHeap -
                    inicioHeap) + " ms.");
            salvarVideos(copiaHeap, pastaSaida + "\\heapSort_" + caso + ".csv");

            // QuickSort com Mediana de Três
            System.out.println("Ordenando com QuickSort (Mediana de Três)...");
            Video[] copiaQuickMediana = Arrays.copyOf(videos, videos.length);
            long inicioQuickMediana = System.currentTimeMillis();
            quickSortMedianaDeTres(copiaQuickMediana, 0, copiaQuickMediana.length - 1);
            long fimQuickMediana = System.currentTimeMillis();
            System.out.println("QuickSort Mediana de Três (" + caso + ") concluído em "
                    + (fimQuickMediana - inicioQuickMediana) + " ms.");
            salvarVideos(copiaQuickMediana, pastaSaida + "\\quickSortMediana3_" + caso +
                    ".csv");

            // Selection Sort
            System.out.println("Ordenando com Selection Sort...");
            Video[] copiaSelection = Arrays.copyOf(videos, videos.length);
            long inicioSelection = System.currentTimeMillis();
            selectionSort(copiaSelection);
            long fimSelection = System.currentTimeMillis();
            System.out
                    .println("Selection Sort (" + caso + ") concluído em " + (fimSelection -
                            inicioSelection) + " ms.");
            salvarVideos(copiaSelection, pastaSaida + "\\selectionSort_" + caso +
                    ".csv");

            // Insertion Sort
            System.out.println("Ordenando com Insertion Sort...");
            Video[] copiaInsertion = Arrays.copyOf(videos, videos.length);
            long inicioInsertion = System.currentTimeMillis();
            insertionSort(copiaInsertion);
            long fimInsertion = System.currentTimeMillis();
            System.out
                    .println("Insertion Sort (" + caso + ") concluído em " + (fimInsertion -
                            inicioInsertion) + " ms.");
            salvarVideos(copiaInsertion, pastaSaida + "\\insertionSort_" + caso +
                    ".csv");

            // Counting Sort
            System.out.println("Ordenando com Counting Sort...");
            Video[] copiaCounting = Arrays.copyOf(videos, videos.length);
            long inicioCounting = System.currentTimeMillis();
            countingSort(copiaCounting);
            long fimCounting = System.currentTimeMillis();
            System.out.println("Counting Sort (" + caso + ") concluído em " +
                    (fimCounting - inicioCounting) + " ms.");
            salvarVideos(copiaCounting, pastaSaida + "\\countingSort_" + caso + ".csv");

            System.out.println("Finalizado processamento de: " + caso);
        }

        System.out.println("\n=== Processamento completo! ===");
    }

    // Demais métodos utilitários e algoritmos permanecem abaixo

    private static Video[] lerVideos(String caminho) {
        int totalLidas = 0, validas = 0;
        int capacity = 1000; // capacidade inicial
        Video[] videosBuffer = new Video[capacity];

        try (BufferedReader br = new BufferedReader(new FileReader(caminho))) {
            br.readLine(); // ignora cabeçalho
            String linha;
            while ((linha = br.readLine()) != null) {
                totalLidas++;
                // Usa o parseCSVLine que retorna um array de String
                String[] campos = parseCSVLine(linha);

                if (campos.length > 17) {
                    String dataTexto = campos[17].trim();
                    try {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                        LocalDate dataTrending = LocalDate.parse(dataTexto, formatter);

                        // Se necessário, redimensiona o array
                        if (validas >= videosBuffer.length) {
                            Video[] temp = new Video[videosBuffer.length * 2];
                            System.arraycopy(videosBuffer, 0, temp, 0, videosBuffer.length);
                            videosBuffer = temp;
                        }

                        // Adiciona o vídeo válido ao array
                        videosBuffer[validas++] = new Video(linha, dataTrending);
                    } catch (Exception e) {
                        // Ignora erros de conversão de data
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao ler arquivo: " + e.getMessage());
        }

        System.out.println("Linhas lidas: " + totalLidas);
        System.out.println("Linhas válidas com data convertida: " + validas);

        // Cria o array final com o tamanho exato do número de vídeos válidos
        Video[] videos = new Video[validas];
        System.arraycopy(videosBuffer, 0, videos, 0, validas);
        return videos;
    }

    private static String[] parseCSVLine(String linha) {
        // Primeiramente, conta quantos campos existem na linha (cada vírgula fora de
        // aspas indica um novo campo)
        int fieldCount = 1;
        boolean dentroDeAspas = false;
        for (int i = 0; i < linha.length(); i++) {
            char c = linha.charAt(i);
            if (c == '\"') {
                dentroDeAspas = !dentroDeAspas;
            } else if (c == ',' && !dentroDeAspas) {
                fieldCount++;
            }
        }

        // Aloca o array com o número exato de campos
        String[] resultado = new String[fieldCount];
        int index = 0;
        StringBuilder atual = new StringBuilder();
        dentroDeAspas = false;

        // Percorre a linha para construir os campos
        for (int i = 0; i < linha.length(); i++) {
            char c = linha.charAt(i);
            if (c == '\"') {
                dentroDeAspas = !dentroDeAspas;
            } else if (c == ',' && !dentroDeAspas) {
                resultado[index++] = atual.toString();
                atual.setLength(0);
            } else {
                atual.append(c);
            }
        }
        // Adiciona o último campo
        resultado[index] = atual.toString();

        return resultado;
    }

    private static void salvarVideos(Video[] videos, String caminho) {
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

    private static void quickSort(Video[] array, int low, int high) {
        while (low < high) {
            int p = particionar(array, low, high);
            if (p - low < high - p) {
                quickSort(array, low, p - 1);
                low = p + 1;
            } else {
                quickSort(array, p + 1, high);
                high = p - 1;
            }
        }
    }

    private static int particionar(Video[] array, int low, int high) {
        // Seleciona um pivô aleatório entre low e high
        int pivotIndex = new Random().nextInt(high - low + 1) + low;
        swap(array, pivotIndex, high);

        Video pivot = array[high];
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (array[j].compareTo(pivot) <= 0) {
                i++;
                swap(array, i, j);
            }
        }
        swap(array, i + 1, high);
        return i + 1;
    }

    private static void swap(Video[] array, int i, int j) {
        Video temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    // MergeSort que recebe e retorna um array de Video
    private static Video[] mergeSort(Video[] array) {
        if (array.length <= 1) {
            return array;
        }

        int mid = array.length / 2;
        // Divide o array usando copyOfRange
        Video[] left = Arrays.copyOfRange(array, 0, mid);
        Video[] right = Arrays.copyOfRange(array, mid, array.length);

        left = mergeSort(left);
        right = mergeSort(right);

        return merge(left, right);
    }

    private static Video[] merge(Video[] left, Video[] right) {
        int leftLen = left.length;
        int rightLen = right.length;
        Video[] merged = new Video[leftLen + rightLen];

        int i = 0, j = 0, k = 0;
        while (i < leftLen && j < rightLen) {
            if (left[i].compareTo(right[j]) <= 0) {
                merged[k++] = left[i++];
            } else {
                merged[k++] = right[j++];
            }
        }

        while (i < leftLen) {
            merged[k++] = left[i++];
        }

        while (j < rightLen) {
            merged[k++] = right[j++];
        }

        return merged;
    }

    private static void heapSort(Video[] array) {
        int n = array.length;
        // Constrói o heap máximo
        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(array, n, i);
        }
        // Extrai os elementos do heap, um por um
        for (int i = n - 1; i > 0; i--) {
            swap(array, 0, i);
            heapify(array, i, 0);
        }
    }

    private static void heapify(Video[] array, int n, int i) {
        int largest = i; // Inicializa o maior como raiz
        int left = 2 * i + 1;
        int right = 2 * i + 2;

        // Se o filho da esquerda for maior que a raiz
        if (left < n && array[left].compareTo(array[largest]) > 0) {
            largest = left;
        }

        // Se o filho da direita for maior que o maior atual
        if (right < n && array[right].compareTo(array[largest]) > 0) {
            largest = right;
        }

        // Se o maior não for a raiz
        if (largest != i) {
            swap(array, i, largest);
            // Recursivamente, heapifica a subárvore afetada
            heapify(array, n, largest);
        }
    }

    private static void quickSortMedianaDeTres(Video[] array, int inicio, int fim) {
        while (inicio < fim) {
            int p = particionarMedianaDeTres(array, inicio, fim);
            if (p - inicio < fim - p) {
                quickSortMedianaDeTres(array, inicio, p - 1);
                inicio = p + 1;
            } else {
                quickSortMedianaDeTres(array, p + 1, fim);
                fim = p - 1;
            }
        }
    }

    private static int particionarMedianaDeTres(Video[] array, int inicio, int fim) {
        int meio = (inicio + fim) / 2;

        // Mediana de três: ordena inicio, meio e fim
        if (array[meio].compareTo(array[inicio]) < 0)
            swap(array, inicio, meio);
        if (array[fim].compareTo(array[inicio]) < 0)
            swap(array, inicio, fim);
        if (array[fim].compareTo(array[meio]) < 0)
            swap(array, meio, fim);

        // Usa o meio como pivô (agora os 3 elementos estão ordenados)
        swap(array, meio, fim);
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

    private static void selectionSort(Video[] array) {
        int n = array.length;
        for (int i = 0; i < n - 1; i++) {
            int indiceMin = i;
            for (int j = i + 1; j < n; j++) {
                if (array[j].compareTo(array[indiceMin]) < 0) {
                    indiceMin = j;
                }
            }
            if (indiceMin != i) {
                swap(array, i, indiceMin);
            }
        }
    }

    private static void insertionSort(Video[] array) {
        for (int i = 1; i < array.length; i++) {
            Video atual = array[i];
            int j = i - 1;
            while (j >= 0 && array[j].compareTo(atual) > 0) {
                array[j + 1] = array[j];
                j--;
            }
            array[j + 1] = atual;
        }
    }

    private static void countingSort(Video[] array) {
        if (array.length == 0) {
            return;
        }

        // Obtém o menor e maior valor de trendingFullDate convertidos para epoch day.
        long minLong = array[0].getTrendingFullDate().toEpochDay();
        long maxLong = minLong;
        for (int i = 1; i < array.length; i++) {
            long day = array[i].getTrendingFullDate().toEpochDay();
            if (day < minLong) {
                minLong = day;
            }
            if (day > maxLong) {
                maxLong = day;
            }
        }

        // Converte para int (supondo que o intervalo de datas seja razoável).
        int min = (int) minLong;
        int max = (int) maxLong;
        int range = max - min + 1;

        // Array de contagem: para cada valor dentro do intervalo [min, max]
        int[] count = new int[range];
        for (Video v : array) {
            int index = (int) (v.getTrendingFullDate().toEpochDay() - min);
            count[index]++;
        }

        // Transforma count em somas acumuladas (prefix sums)
        for (int i = 1; i < range; i++) {
            count[i] += count[i - 1];
        }

        // Cria o array de saída com os elementos ordenados
        Video[] output = new Video[array.length];

        // Coloca cada elemento na posição correta, garantindo a estabilidade
        // Percorremos de trás para frente para manter a ordenação estável
        for (int i = array.length - 1; i >= 0; i--) {
            int index = (int) (array[i].getTrendingFullDate().toEpochDay() - min);
            int pos = count[index] - 1;
            output[pos] = array[i];
            count[index]--;
        }

        // Copia os elementos ordenados de volta para o array original
        System.arraycopy(output, 0, array, 0, array.length);
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
