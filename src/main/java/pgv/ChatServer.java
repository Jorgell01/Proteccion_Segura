package pgv;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.*;
import java.util.Base64;
import java.util.concurrent.CopyOnWriteArrayList;

public class ChatServer {
    private static SecretKeySpec secretKey;
    private static CopyOnWriteArrayList<ClientHandler> clients = new CopyOnWriteArrayList<>();

    public static void main(String[] args) throws Exception {
        // Establecer la clave AES
        String key = "1234567890123456"; // Clave de 16 caracteres
        secretKey = new SecretKeySpec(key.getBytes(), "AES");

        // Configuración del servidor
        ServerSocket serverSocket = new ServerSocket(12345);
        System.out.println("Esperando conexiones...");

        while (true) {
            Socket socket = serverSocket.accept(); // Esperar a que un cliente se conecte
            System.out.println("Cliente conectado.");
            ClientHandler clientHandler = new ClientHandler(socket);
            clients.add(clientHandler);
            new Thread(clientHandler).start(); // Iniciar un nuevo hilo para manejar la comunicación con el cliente
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

    // Clase para manejar la comunicación con un cliente
    static class ClientHandler implements Runnable {
        private Socket socket;
        private DataInputStream in;
        private DataOutputStream out;

        public ClientHandler(Socket socket) throws IOException {
            this.socket = socket;
            this.in = new DataInputStream(socket.getInputStream());
            this.out = new DataOutputStream(socket.getOutputStream());
        }

        @Override
        public void run() {
            try {
                String receivedMessage;
                while (true) {
                    receivedMessage = in.readUTF(); // Leer mensaje cifrado del cliente
                    System.out.println("\nMensaje Cifrado recibido del cliente: " + receivedMessage);
                    String decryptedMessage = decrypt(receivedMessage); // Descifrar el mensaje
                    System.out.println("Mensaje Descifrado: " + decryptedMessage);

                    // Retransmitir el mensaje descifrado a todos los clientes conectados
                    for (ClientHandler client : clients) {
                        if (client != this) {
                            client.sendMessage(decryptedMessage);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                clients.remove(this);
            }
        }

        // Método para enviar un mensaje cifrado al cliente
        public void sendMessage(String message) throws Exception {
            String encryptedMessage = encrypt(message); // Cifrar el mensaje
            out.writeUTF(encryptedMessage); // Enviar mensaje cifrado al cliente
        }
    }
}