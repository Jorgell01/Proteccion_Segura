package pgv;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.*;
import java.util.Base64;

public class ChatServer {
    private static SecretKeySpec secretKey;

    public static void main(String[] args) throws Exception {
        // Establecer la clave AES
        String key = "1234567890123456"; // Clave de 16 caracteres
        secretKey = new SecretKeySpec(key.getBytes(), "AES");

        // Configuración del servidor
        ServerSocket serverSocket = new ServerSocket(12345);
        System.out.println("Esperando conexión...");
        Socket socket = serverSocket.accept(); // Esperar a que un cliente se conecte
        System.out.println("Cliente conectado.");

        // Crear los streams de entrada y salida
        DataInputStream in = new DataInputStream(socket.getInputStream());
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());

        // Recibir mensajes
        String receivedMessage;
        while (true) {
            receivedMessage = in.readUTF(); // Leer mensaje cifrado del cliente
            System.out.println("\nMensaje Cifrado recibido del cliente: " + receivedMessage);
            String decryptedMessage = decrypt(receivedMessage); // Descifrar el mensaje
            System.out.println("Mensaje Descifrado: " + decryptedMessage);

            // Responder con un mensaje cifrado
            String response = "Respuesta del servidor";
            System.out.println("Mensaje Original de Respuesta: " + response);
            String encryptedResponse = encrypt(response); // Cifrar el mensaje de respuesta
            System.out.println("Mensaje Cifrado de Respuesta: " + encryptedResponse);
            out.writeUTF(encryptedResponse); // Enviar mensaje cifrado al cliente
        }
    }

    // Método para cifrar el mensaje
    public static String encrypt(String message) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey); // Inicializar el cifrador en modo cifrado
        byte[] encryptedMessage = cipher.doFinal(message.getBytes()); // Cifrar el mensaje
        return Base64.getEncoder().encodeToString(encryptedMessage); // Convertir a Base64 para enviar
    }

    // Método para descifrar el mensaje
    public static String decrypt(String encryptedMessage) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey); // Inicializar el cifrador en modo descifrado
        byte[] decodedMessage = Base64.getDecoder().decode(encryptedMessage); // Decodificar de Base64
        byte[] decryptedMessage = cipher.doFinal(decodedMessage); // Descifrar el mensaje
        return new String(decryptedMessage); // Convertir a cadena de texto
    }
}