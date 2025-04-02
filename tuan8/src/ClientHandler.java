import java.io.*;
import java.net.Socket;

class ClientHandler implements Runnable {
    private Socket clientSocket;

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
    }


    public void run() {
        String threadName = Thread.currentThread().getName();
        System.out.println("[" + threadName + "] Bắt đầu xử lý client: " + clientSocket.getInetAddress().getHostAddress());
        try (Socket autoCloseableSocket = this.clientSocket;
             InputStream input = clientSocket.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(input));
             OutputStream output = clientSocket.getOutputStream();
             PrintWriter writer = new PrintWriter(output, true) )
        {
            String clientMessage;
            while ((clientMessage = reader.readLine()) != null) {
                System.out.println("[" + threadName + "] Nhận từ client " + clientSocket.getInetAddress().getHostAddress() + ": " + clientMessage);
                writer.println("[" + threadName + "] Server nhận được: " + clientMessage);
                if ("bye".equalsIgnoreCase(clientMessage.trim())) {
                    System.out.println("[" + threadName + "] Client " + clientSocket.getInetAddress().getHostAddress() + " yêu cầu ngắt kết nối.");
                    break;
                }
            }
        } catch (IOException ex) {
            System.err.println("[" + threadName + "] Lỗi I/O với client " + (clientSocket != null ? clientSocket.getInetAddress().getHostAddress() : "unknown") + ": " + ex.getMessage());
        }
        System.out.println("[" + threadName + "] Kết thúc xử lý client: " + (clientSocket != null ? clientSocket.getInetAddress().getHostAddress() : "unknown"));
    }
}