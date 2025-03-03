package bai4;

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
                    int randomNum = (int)(Math.random() * 101); // 0 to 100
                    long startTime = System.currentTimeMillis();
                    System.out.println("Số cần đoán: "+randomNum);
                    String data;
                    int count=1;
                    while((data=reader.readLine())!=null){ // Nhận dữ liệu liên tục từ client
                        System.out.println("Server nhận: " + data);
                        if("bye".equalsIgnoreCase(data)){
                            System.out.println("Client yêu cầu đóng kết nối.");
                            break;
                        }
                        
                        if(Integer.parseInt(data)!=randomNum){
                            if (Integer.parseInt(data)<randomNum){
                                writer.println("Số bạn đoán nhỏ hơn số n"); // Gửi phản hồi cho client
                            }
                            else writer.println("Số bạn đoán lớn hơn số n"); // Gửi phản hồi cho client
                            count+=1;
                        }
                        else {
                            long endTime = System.currentTimeMillis();
                            writer.println("Số bạn đoán đã đúng. Số lần đoán: " + count + ". Tổng thời gian đoán: " + (endTime - startTime) + " milliseconds" ); // Gửi phản hồi cho client
                            //break;
                        }                        
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
