package ordenation;

import java.io.*;
import java.util.*;

public class OrdenacaoComments {

    public static void main(String[] args) {
        String baseDir = System.getProperty("user.dir") + File.separator;
        String pastaSaida = baseDir + "Ordenação Comments" + File.separator;

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
            quickSortArray(copiaQuick, 0, copiaQuick.length - 1);

            long fimQuick = System.currentTimeMillis();
            System.out.println("QuickSort (" + caso + ") concluído em " + (fimQuick -
                    inicioQuick) + " ms.");
            salvarVideos(copiaQuick, pastaSaida + "\\quickSort_" + caso + ".csv"); //

            // MergeSort
            System.out.println("Ordenando com MergeSort...");
            Video[] copiaMerge = Arrays.copyOf(videos, videos.length);
            long inicioMerge = System.currentTimeMillis();
            Video[] ordenadosMerge = mergeSort(copiaMerge);
            long fimMerge = System.currentTimeMillis();
            System.out.println("MergeSort (" + caso + ") concluído em " + (fimMerge -
                    inicioMerge) + " ms.");
            salvarVideos(ordenadosMerge, pastaSaida + "\\mergeSort_" + caso + ".csv");

            // HeapSort
            System.out.println("Ordenando com HeapSort...");
            Video[] copiaHeap = Arrays.copyOf(videos, videos.length);
            long inicioHeap = System.currentTimeMillis();
            heapSort(copiaHeap);
            long fimHeap = System.currentTimeMillis();
            System.out.println("HeapSort (" + caso + ") concluído em " + (fimHeap -
                    inicioHeap) + " ms.");
            salvarVideos(copiaHeap, pastaSaida + "\\heapSort_" + caso + ".csv");

            // QuickSort com mediana de três
            System.out.println("Ordenando com QuickSort (mediana de 3)...");
            Video[] copiaQuick3 = Arrays.copyOf(videos, videos.length);
            long inicioQuick3 = System.currentTimeMillis();
            quickSortMedianaDeTres(copiaQuick3, 0, copiaQuick3.length - 1);
            long fimQuick3 = System.currentTimeMillis();
            System.out.println(
                    "QuickSort (mediana de 3 - " + caso + ") concluído em " + (fimQuick3 -
                            inicioQuick3) + " ms.");

            salvarVideos(copiaQuick3, pastaSaida + "\\quickSortMediana3_" + caso +
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

            // Selection Sort
            System.out.println("Ordenando com Selection Sort...");
            long inicioSelection = System.currentTimeMillis();
            // Cria uma cópia do array de vídeos
            Video[] copiaSelection = Arrays.copyOf(videos, videos.length);
            selectionSort(copiaSelection);
            long fimSelection = System.currentTimeMillis();
            System.out
                    .println("Selection Sort (" + caso + ") concluído em " + (fimSelection - inicioSelection) + " ms.");
            salvarVideos(copiaSelection, pastaSaida + "\\selectionSort_" + caso + ".csv");

        }

        System.out.println("\n=== Processamento completo! ===");
    }

    private static Video[] lerVideos(String caminho) {
        int totalLidas = 0, ignoradas = 0, validas = 0;

        // Primeira passagem: contar os vídeos válidos
        try (BufferedReader br = new BufferedReader(new FileReader(caminho))) {
            br.readLine(); // ignora cabeçalho
            String linha;
            while ((linha = br.readLine()) != null) {
                totalLidas++;
                // Aqui, usamos parseCSVLine para separar os campos
                String[] campos = parseCSVLine(linha);

                if (campos.length > 10 && !campos[10].trim().isEmpty()) {
                    try {
                        String descricao = (campos.length > 14 ? campos[14] : "");
                        String regexCaracteresInvalidos = ".*[\\u00AD\\u200B-\\u200D\\uFEFF\\u2028\\u2029\\u00A0].*";

                        if (!descricao.matches(regexCaracteresInvalidos)) {
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

        // Cria um array com o tamanho exato dos vídeos válidos
        Video[] videos = new Video[validas];

        // Segunda passagem: preencher o array com os vídeos válidos
        int index = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(caminho))) {
            br.readLine(); // ignora cabeçalho
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] campos = parseCSVLine(linha);

                if (campos.length > 10 && !campos[10].trim().isEmpty()) {
                    try {
                        int commentCount = Integer.parseInt(campos[10].trim());
                        String descricao = (campos.length > 14 ? campos[14] : "");
                        String regexCaracteresInvalidos = ".*[\\u00AD\\u200B-\\u200D\\uFEFF\\u2028\\u2029\\u00A0].*";

                        if (!descricao.matches(regexCaracteresInvalidos)) {
                            videos[index++] = new Video(linha, commentCount);
                        }
                    } catch (NumberFormatException e) {
                        // Ignora linhas com problemas de formatação numérica
                    }
                }

            }
        } catch (IOException e) {
            System.out.println("Erro ao ler arquivo: " + e.getMessage());
        }

        return videos;
    }

    private static String[] parseCSVLine(String linha) {
        // Primeiro: conta quantos campos existem na linha.
        // Cada vírgula fora de aspas indica um separador.
        int fieldCount = 1; // Começa com 1 campo
        boolean dentroDeAspas = false;
        for (int i = 0; i < linha.length(); i++) {
            char c = linha.charAt(i);
            if (c == '\"') {
                dentroDeAspas = !dentroDeAspas;
            } else if (c == ',' && !dentroDeAspas) {
                fieldCount++;
            }
        }

        // Cria o array com o número exato de campos
        String[] resultado = new String[fieldCount];
        int index = 0;
        StringBuilder atual = new StringBuilder();
        dentroDeAspas = false;

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

    private static void quickSortArray(Video[] array, int inicio, int fim) {
        while (inicio < fim) {
            int p = particionarArray(array, inicio, fim);
            if (p - inicio < fim - p) {
                quickSortArray(array, inicio, p - 1);
                inicio = p + 1;
            } else {
                quickSortArray(array, p + 1, fim);
                fim = p - 1;
            }
        }
    }

    private static int particionarArray(Video[] array, int inicio, int fim) {
        // Escolha aleatória do pivô (evita pior caso)
        int pivoIndex = new Random().nextInt(fim - inicio + 1) + inicio;

        // Troca manual do pivô para o fim (sem Collections.swap)
        Video temp = array[pivoIndex];
        array[pivoIndex] = array[fim];
        array[fim] = temp;

        Video pivo = array[fim];
        int i = inicio - 1;
        for (int j = inicio; j < fim; j++) {
            if (array[j].compareTo(pivo) <= 0) {
                i++;
                // Troca manual (sem Collections.swap)
                temp = array[i];
                array[i] = array[j];
                array[j] = temp;
            }
        }

        // Reposiciona o pivô
        temp = array[i + 1];
        array[i + 1] = array[fim];
        array[fim] = temp;

        return i + 1;
    }

    private static Video[] mergeSort(Video[] array) {
        // Caso base: array com 0 ou 1 elemento já está ordenado
        if (array.length <= 1) {
            return array;
        }

        int meio = array.length / 2;
        // Cria os arrays para as metades esquerda e direita
        Video[] esquerda = Arrays.copyOfRange(array, 0, meio);
        Video[] direita = Arrays.copyOfRange(array, meio, array.length);

        // Ordena recursivamente cada metade
        esquerda = mergeSort(esquerda);
        direita = mergeSort(direita);

        // Junta os arrays ordenados e retorna o resultado
        return merge(esquerda, direita);
    }

    private static Video[] merge(Video[] esquerda, Video[] direita) {
        Video[] resultado = new Video[esquerda.length + direita.length];
        int i = 0, j = 0, k = 0;

        // Mescla os arrays enquanto houver elementos em ambos
        while (i < esquerda.length && j < direita.length) {
            if (esquerda[i].compareTo(direita[j]) <= 0) {
                resultado[k++] = esquerda[i++];
            } else {
                resultado[k++] = direita[j++];
            }
        }

        // Copia os elementos restantes, se houver, de 'esquerda'
        while (i < esquerda.length) {
            resultado[k++] = esquerda[i++];
        }

        // Copia os elementos restantes, se houver, de 'direita'
        while (j < direita.length) {
            resultado[k++] = direita[j++];
        }

        return resultado;
    }

    private static void quickSortMedianaDeTres(Video[] array, int inicio, int fim) {
        // Caso base: se não há elementos ou há apenas um, retorna.
        if (inicio >= fim) {
            return;
        }

        // Se o subarray tiver apenas 2 elementos, ordena diretamente.
        if (fim - inicio + 1 == 2) {
            if (array[inicio].compareTo(array[fim]) > 0) {
                swap(array, inicio, fim);
            }
            return;
        }

        // Para subarrays com 3 ou mais elementos, aplica o particionamento com mediana
        // de três.
        int p = particionarMedianaDeTres(array, inicio, fim);
        quickSortMedianaDeTres(array, inicio, p - 1);
        quickSortMedianaDeTres(array, p + 1, fim);
    }

    private static int particionarMedianaDeTres(Video[] array, int inicio, int fim) {
        int meio = inicio + (fim - inicio) / 2;

        // Ordena os três elementos: início, meio e fim.
        if (array[inicio].compareTo(array[meio]) > 0) {
            swap(array, inicio, meio);
        }
        if (array[inicio].compareTo(array[fim]) > 0) {
            swap(array, inicio, fim);
        }
        if (array[meio].compareTo(array[fim]) > 0) {
            swap(array, meio, fim);
        }

        // Move a mediana (o elemento de valor médio) para a penúltima posição.
        swap(array, meio, fim - 1);
        Video pivo = array[fim - 1];

        int i = inicio;
        int j = fim - 1;

        // Particiona o array: elementos menores que o pivô à esquerda, maiores à
        // direita.
        while (true) {
            while (array[++i].compareTo(pivo) < 0) {
                // Apenas avança i
            }
            while (array[--j].compareTo(pivo) > 0) {
                // Apenas recua j
            }
            if (i >= j) {
                break;
            }
            swap(array, i, j);
        }

        // Coloca o pivô em sua posição final.
        swap(array, i, fim - 1);
        return i;
    }

    // Método auxiliar para trocar elementos do array.
    private static void swap(Video[] array, int i, int j) {
        Video temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    // Método Insertion Sort trabalhando com array
    private static void insertionSort(Video[] array) {
        for (int i = 1; i < array.length; i++) {
            Video chave = array[i];
            int j = i - 1;
            // Move os elementos maiores que 'chave' uma posição à frente
            while (j >= 0 && array[j].compareTo(chave) > 0) {
                array[j + 1] = array[j];
                j--;
            }
            array[j + 1] = chave;
        }
    }

    private static void countingSort(Video[] array) {
        if (array == null || array.length == 0) {
            
            return;
        }

        int n = array.length;

        // Encontra o maior valor de commentCount no array
        int max = array[0].commentCount;
        for (int i = 1; i < n; i++) {
            if (array[i].commentCount > max) {
                max = array[i].commentCount;
            }
        }

        // Inicializa o array de contagem
        int[] count = new int[max + 1];
        for (int i = 0; i < n; i++) {
            count[array[i].commentCount]++;
        }

        // Converte o array de contagem para contagens acumuladas (prefix sum)
        for (int i = 1; i < count.length; i++) {
            count[i] += count[i - 1];
        }

        // Cria um array auxiliar para armazenar o resultado ordenado
        Video[] output = new Video[n];

        // Preenche o array de saída de forma estável
        // Itera de trás para frente para garantir a estabilidade
        for (int i = n - 1; i >= 0; i--) {
            int index = count[array[i].commentCount] - 1;
            output[index] = array[i];
            count[array[i].commentCount]--;
        }

        // Copia o array ordenado de volta para o array original
        for (int i = 0; i < n; i++) {
            array[i] = output[i];
        }
    }

    private static void selectionSort(Video[] array) {
        int n = array.length;
        for (int i = 0; i < n - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < n; j++) {
                if (array[j].commentCount < array[minIndex].commentCount) {
                    minIndex = j;
                }
            }
            // Troca o elemento mínimo com o elemento da posição i
            swap(array, i, minIndex);
        }
    }

    private static void heapSort(Video[] array) {
        int n = array.length;

        // Constrói o heap (rearranja o array)
        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(array, n, i);
        }

        // Extrai elementos do heap um por um
        for (int i = n - 1; i > 0; i--) {
            swap(array, 0, i);
            heapify(array, i, 0);
        }
    }

    private static void heapify(Video[] array, int n, int i) {
        int largest = i;
        int left = 2 * i + 1;
        int right = 2 * i + 2;

        // Se o filho da esquerda existe e é maior que o nó raiz
        if (left < n && array[left].compareTo(array[largest]) > 0) {
            largest = left;
        }

        // Se o filho da direita existe e é maior que o maior atual
        if (right < n && array[right].compareTo(array[largest]) > 0) {
            largest = right;
        }

        // Se o maior não é o nó raiz
        if (largest != i) {
            swap(array, i, largest);
            heapify(array, n, largest);
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
