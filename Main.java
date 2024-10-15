import java.util.PriorityQueue;
import java.util.HashMap;
import java.util.Map;

// Nodo del árbol de Huffman
class HuffmanNode {
    char c;
    int frequency;
    HuffmanNode left;
    HuffmanNode right;

    // Constructor para un nodo de hoja (carácter)
    HuffmanNode(char c, int frequency) {
        this.c = c;
        this.frequency = frequency;
        left = null;
        right = null;
    }

    // Constructor para un nodo intermedio (sin carácter)
    HuffmanNode(int frequency, HuffmanNode left, HuffmanNode right) {
        this.c = '\0';  // Nodo intermedio no tiene carácter
        this.frequency = frequency;
        this.left = left;
        this.right = right;
    }
}

public class Main {

    // Función para generar el árbol de Huffman
    public static HuffmanNode buildHuffmanTree(Map<Character, Integer> freqMap) {
        // Cola de prioridad para construir el árbol
        PriorityQueue<HuffmanNode> queue = new PriorityQueue<>((a, b) -> a.frequency - b.frequency);

        // Crear un nodo para cada carácter y añadirlo a la cola de prioridad
        for (Map.Entry<Character, Integer> entry : freqMap.entrySet()) {
            queue.add(new HuffmanNode(entry.getKey(), entry.getValue()));
        }

        // Construir el árbol de Huffman combinando los nodos de menor frecuencia
        while (queue.size() > 1) {
            HuffmanNode left = queue.poll();  // Nodo de menor frecuencia
            HuffmanNode right = queue.poll(); // Segundo nodo de menor frecuencia

            // Crear un nuevo nodo intermedio con la suma de las frecuencias
            HuffmanNode newNode = new HuffmanNode(left.frequency + right.frequency, left, right);
            queue.add(newNode);
        }

        // El último nodo en la cola es la raíz del árbol de Huffman
        return queue.poll();
    }

    // Función para generar los códigos de Huffman
    public static void generateCodes(HuffmanNode root, String code, Map<Character, String> huffmanCode) {
        if (root == null) {
            return;
        }

        // Si es una hoja, guarda el código del carácter
        if (root.left == null && root.right == null) {
            huffmanCode.put(root.c, code);
        }

        // Recorrer el árbol a la izquierda y a la derecha
        generateCodes(root.left, code + "0", huffmanCode);
        generateCodes(root.right, code + "1", huffmanCode);
    }

    // Función para codificar el mensaje utilizando los códigos de Huffman
    public static String encode(String message, Map<Character, String> huffmanCode) {
        StringBuilder encodedMessage = new StringBuilder();
        for (char c : message.toCharArray()) {
            encodedMessage.append(huffmanCode.get(c));
        }
        return encodedMessage.toString();
    }

    // Función para decodificar un mensaje codificado utilizando el árbol de Huffman
    public static String decode(String encodedMessage, HuffmanNode root) {
        StringBuilder decodedMessage = new StringBuilder();
        HuffmanNode current = root;

        for (char bit : encodedMessage.toCharArray()) {
            if (bit == '0') {
                current = current.left;
            } else {
                current = current.right;
            }

            // Si llegamos a una hoja, recuperamos el carácter
            if (current.left == null && current.right == null) {
                decodedMessage.append(current.c);
                current = root;
            }
        }

        return decodedMessage.toString();
    }

    public static void main(String[] args) {
        // Ejemplo de entrada
        String message = "huffman algorithm";

        // Contar la frecuencia de cada carácter en el mensaje
        Map<Character, Integer> freqMap = new HashMap<>();
        for (char c : message.toCharArray()) {
            freqMap.put(c, freqMap.getOrDefault(c, 0) + 1);
        }

        // Construir el árbol de Huffman
        HuffmanNode root = buildHuffmanTree(freqMap);

        // Generar los códigos de Huffman
        Map<Character, String> huffmanCode = new HashMap<>();
        generateCodes(root, "", huffmanCode);

        // Mostrar los códigos de Huffman
        System.out.println("Códigos de Huffman: " + huffmanCode);

        // Codificar el mensaje
        String encodedMessage = encode(message, huffmanCode);
        System.out.println("Mensaje codificado: " + encodedMessage);

        // Decodificar el mensaje
        String decodedMessage = decode(encodedMessage, root);
        System.out.println("Mensaje decodificado: " + decodedMessage);
    }
}
