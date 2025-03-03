package bai2;

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

                        Integer num = Integer.parseInt(data);
                        if (isPerfect(num)){
                            writer.println(data + " là số hoàn hảo"); // Gửi phản hồi cho client
                        }
                        else{
                            Integer perfectNum = num+1;
                            while (!isPerfect(perfectNum)){
                                perfectNum+=1;
                            }
                            writer.println("Số hoàn hảo lớn hơn gần nhất với " + data + " là " + perfectNum); // Gửi phản hồi cho client
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

    public static boolean isPerfect(int n)
    {
        if (n < 1)
            return false;

        int sum = 1;
        for (int i = 1; i < n/2; i++) {
            if (n % i == 0) {
                sum += i;
            }
        }
 
        if (sum == n)
            return true;
        return false;
    }
}
