package bai2;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        String host = "localhost";
        int port = 12345;
        try (Socket socket = new Socket(host, port)){
            // Gắn kết Stream
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()),true);
                 Scanner scanner = new Scanner(System.in)) {
                // Nhận dữ liệu từ người dùng
                String userInput;
                while(true){
                    System.out.print("Nhập số n nguyên dương: ");
                    userInput = scanner.nextLine();
                    writer.println(userInput);
                    if("bye".equalsIgnoreCase(userInput))
                        break;
                    String response = reader.readLine();
                    System.out.println(response);
                }
                System.out.println("Client yêu cầu đóng kết nối.");
            } catch (IOException e){
                System.err.println("Lỗi gắn kết stream: " + e.getMessage());
            }
        } catch(IOException e){
            System.err.println("Lỗi kết nối đến Server: " + e.getMessage());
        }
    }
}
