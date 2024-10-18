import java.io.IOException;
import java.io.*;
import java.net.*;

public class MultithreadingServer {
    public static void main(String[] args) throws IOException {
        int puerto = 8080;
        ServerSocket serverSocket = new ServerSocket(puerto);
        System.out.println("Servidor corriendo en el puerto: " + puerto);

        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("¡Alguien se ha conectado!");
            new ClientHandler(clientSocket).start();  // Iniciar un hilo para cada cliente
        }
    }
}

class ClientHandler extends Thread {
    private Socket clientSocket;

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
    }

    public void run() {
        try {
            BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter output = new PrintWriter(clientSocket.getOutputStream(), true);
            RandomAccessFile raf = new RandomAccessFile("DataExtraida.txt","rw");
            raf.seek(raf.length());
            String line;
            int contentLength = 0;

            // Leer las cabeceras HTTP y extraer Content-Length
            while (!(line = input.readLine()).isEmpty()) {
                System.out.println(line);
                if (line.startsWith("Content-Length:")) {
                    //obtenemos la longitud del contenido
                    contentLength = Integer.parseInt(line.substring("Content-Length:".length()).trim()); 
                }
            }
            // Leer el cuerpo de la solicitud POST basado en Content-Length
            char[] body = new char[contentLength];
            input.read(body, 0, contentLength);
            String postData = new String(body);

            System.out.println("Datos POST recibidos: " + postData);
            raf.writeBytes(postData+"\n");
            // Enviar respuesta al cliente
            output.println("HTTP/1.1 200 OK");
            output.println("Content-Type: text/html");
            output.println("");
            output.println("Datos recibidos: " + postData);

            // Ahora nos encargaremos de escribir estos datos en un TXT
            input.close();
            output.close();
            raf.close();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
