package pgv;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.*;
import java.util.Base64;

public class ChatCliente {
    private static SecretKeySpec secretKey;

    public static void main(String[] args) throws Exception {
        // Establecer la clave AES
        String key = "1234567890123456"; // Clave de 16 caracteres
        secretKey = new SecretKeySpec(key.getBytes(), "AES");

        // Conexión con el servidor
        Socket socket = new Socket("localhost", 12345);
        System.out.println("Conectado al servidor.");

        // Crear los streams de entrada y salida
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        DataInputStream in = new DataInputStream(socket.getInputStream());

        // Hilo para recibir mensajes
        Thread receiverThread = new Thread(() -> {
            try {
                String receivedMessage;
                while ((receivedMessage = in.readUTF()) != null) {
                    System.out.println("Servidor (descifrado): " + decrypt(receivedMessage));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        receiverThread.start();

        // Enviar mensajes
        String message;
        while (true) {
            message = reader.readLine();
            System.out.println("\nMensaje Original: " + message);
            String encryptedMessage = encrypt(message);
            System.out.println("Mensaje Cifrado: " + encryptedMessage);
            out.writeUTF(encryptedMessage);
        }
    }

    // Método para cifrar el mensaje
    public static String encrypt(String message) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedMessage = cipher.doFinal(message.getBytes());
        return Base64.getEncoder().encodeToString(encryptedMessage); // Representación Base64
    }

    // Método para descifrar el mensaje
    public static String decrypt(String encryptedMessage) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decodedMessage = Base64.getDecoder().decode(encryptedMessage); // Decodificar Base64
        byte[] decryptedMessage = cipher.doFinal(decodedMessage);
        return new String(decryptedMessage);
    }
}
