package bai3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

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
                        String result = phanTich(Integer.parseInt(data));

                        writer.println("Kết quả: " + result + "\n"); // Gửi phản hồi cho client
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

    public static String phanTich(int n){
        //System.out.println("Viết chương trình nhận vào n là một số nguyên dương và phân tích thành tích các thừa số nguyên tố \n Input: 1122334455\n Output: 3^1x5^1x11^1x6802027^1");
        if (n<1){
            //System.out.println("ERROR!!! n phai la so nguyen duong");
            return "ERROR!!! n phai la so nguyen duong";
        }
        if (n==1){
            //System.out.println("Result: 1");
            return "Result: 1";
        }
        HashMap<Integer, Integer> primeCount = new HashMap<Integer, Integer>();
        int count=0;
        for (int i=2; i<=n; i++){
            if (n%i==0){
                n=n/i;
                //count+=1;
                //primeCount.put(i,count);
                primeCount.put(i,primeCount.getOrDefault(i,0)+1);
                i-=1;
            }
            //else
                //count=0;
        }
        StringBuilder result = new StringBuilder();
        System.out.print("Result: ");
        count=0;
        for (int key : primeCount.keySet()){
            count+=1;
            if (count==primeCount.size()){
                System.out.print(key+"^"+primeCount.get(key));
                result.append(key + "^" + primeCount.get(key));
                break;
            }
            //System.out.print(key+"^"+primeCount.get(key)+"x");
            result.append(key+"^"+primeCount.get(key)+"x");
        }
        return result.toString();
    }
}
