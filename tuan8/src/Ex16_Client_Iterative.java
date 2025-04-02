import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Ex16_Client_Iterative {
    public static void main(String[] args) {
        String hostname = "localhost";
        int port = 12345;
        try (
                Socket socket = new Socket(hostname, port);
                PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                Scanner scanner = new Scanner(System.in)
        ) {
            System.out.println("Đã kết nối tới server. Nhập tin nhắn (nhập 'bye' để thoát):");
            String userInput;
            do {
                System.out.print("Client: ");
                userInput = scanner.nextLine();
                writer.println(userInput);
                String serverResponse = reader.readLine();
                System.out.println(serverResponse);
            } while (!"bye".equalsIgnoreCase(userInput.trim()));
        } catch (UnknownHostException ex) {
            System.out.println("Không tìm thấy server: " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("Lỗi I/O: " + ex.getMessage());
        }
    }
}