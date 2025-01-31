## Aplicación de Chat con Cifrado AES

Este proyecto implementa una aplicación de chat simple con un servidor y múltiples clientes. La comunicación entre el servidor y los clientes está asegurada utilizando cifrado AES.

### Características

- **Servidor**: Maneja múltiples conexiones de clientes y retransmite mensajes a todos los clientes conectados.
- **Cliente**: Se conecta al servidor, envía mensajes cifrados y recibe mensajes cifrados del servidor.
- **Cifrado AES**: Asegura que todos los mensajes intercambiados entre el servidor y los clientes estén cifrados y seguros.

### Requisitos Previos

- Java Development Kit (JDK) 8 o superior
- Maven

### Configuración

1. **Clonar el repositorio**:
    ```sh
    git clone <repository-url>
    cd <repository-directory>
    ```

2. **Construir el proyecto**:
    ```sh
    mvn clean install
    ```

### Ejecutar el Servidor

1. Navega al directorio `target`:
    ```sh
    cd target
    ```

2. Ejecuta el servidor:
    ```sh
    java -cp <jar-file-name>.jar pgv.ChatServer
    ```

### Ejecutar los Clientes

1. Abre dos ventanas de terminal o instancias del IDE por separado.

2. En cada terminal, navega al directorio `target`:
    ```sh
    cd target
    ```

3. Ejecuta el cliente en cada terminal:
    ```sh
    java -cp <jar-file-name>.jar pgv.ChatCliente
    ```

### Cómo Funciona

#### Servidor

- El servidor escucha conexiones entrantes de clientes en el puerto `12345`.
- Cuando un cliente se conecta, el servidor inicia un nuevo hilo para manejar la comunicación con ese cliente.
- El servidor recibe mensajes cifrados de los clientes, los descifra y retransmite los mensajes descifrados a todos los clientes conectados.

#### Cliente

- Cada cliente se conecta al servidor en `localhost` y puerto `12345`.
- El cliente lee la entrada del usuario, cifra el mensaje usando AES y envía el mensaje cifrado al servidor.
- El cliente también escucha mensajes entrantes del servidor, los descifra y muestra los mensajes descifrados.

### Cifrado AES

AES (Advanced Encryption Standard) es un algoritmo de cifrado simétrico ampliamente utilizado para asegurar datos. En esta aplicación:

- Se utiliza una clave de 16 caracteres (`1234567890123456`) para el cifrado y descifrado AES.
- Los mensajes se cifran antes de ser enviados por la red y se descifran al recibirlos.
- La clase `Cipher` del paquete `javax.crypto` se utiliza para realizar el cifrado y descifrado.
- Los mensajes cifrados se codifican en Base64 para asegurar que puedan ser transmitidos como texto.

### Ejemplo

1. **Salida del Servidor**:
    ```
    Esperando conexiones...
    Cliente conectado.
    Mensaje Cifrado recibido del cliente: <mensaje-cifrado>
    Mensaje Descifrado: Hola, Mundo!
    ```

2. **Salida del Cliente**:
    ```
    Conectado al servidor.
    Mensaje Original: Hola, Mundo!
    Mensaje Cifrado: <mensaje-cifrado>
    Servidor (descifrado): Hola, Mundo!
    ```

### Notas

- Asegúrate de que el servidor esté en ejecución antes de iniciar los clientes.
- La misma clave de cifrado debe ser utilizada tanto por el servidor como por los clientes para un cifrado y descifrado exitoso.