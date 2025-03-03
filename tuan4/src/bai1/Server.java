package bai1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
        int port = 12345;
        try(ServerSocket server = new ServerSocket(port)){ // Khởi tạo ServerSocket
            // Server lặp liên tục để lắng nghe và chấp nhận kết nối
            while(true){
                System.out.println("Server đang lắng nghe tại cổng " + port);
                try (Socket client = server.accept();
                     BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
                     PrintWriter writer = new PrintWriter(new OutputStreamWriter(client.getOutputStream()), true)
                ){
                    String data;
                    while((data=reader.readLine())!=null){ // Nhận dữ liệu liên tục từ client
                        System.out.println("Server nhận: " + data);
                        if("bye".equalsIgnoreCase(data)){
                            System.out.println("Client yêu cầu đóng kết nối.");
                            break;
                        }
                        
                        StringBuilder reversedStr2 = new StringBuilder(data);
/*                         for (int i = 0; i < data.length(); i++) {
                            reversedStr = data.charAt(i) + reversedStr;
                        } */
                        //writer.println("Chuỗi đảo ngược của " + data + " là: " + reversedStr); // Gửi phản hồi cho client
                        writer.println("Chuỗi đảo ngược của " + data + " là: " + reversedStr2.reverse()); // Gửi phản hồi cho client
                    }
                    System.out.println("Server đã đóng kết nối đến client.");
                } catch(IOException e){
                    System.err.println("Lỗi kết nối đến Client: " + e.getMessage());
                }
            }
        } catch(IOException e){
            System.err.println("Lỗi tạo ServerSocket: " + e. getMessage());
        }
    }
}
