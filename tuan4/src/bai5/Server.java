package bai5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.StringTokenizer;

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

                        writer.println(tinhToan(data)); // Gửi phản hồi cho client
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

    public static String tinhToan(String string){
        float result = 0;

        StringTokenizer str = new StringTokenizer(string, "+-*/", true);
        if (str.countTokens() != 3) {
            //throw new IllegalArgumentException("Error: Dữ liệu nhập vào không đúng định dạng");
            return "Error: Dữ liệu nhập vào không đúng định dạng";
        }
        
        String str1 = str.nextToken();
        String op = str.nextToken();
        String str2 = str.nextToken();
        
        try{
            int num1 = Integer.parseInt(str1);
            int num2 = Integer.parseInt(str2);
            switch (op) {
                case "+" -> result = num1 + num2;
                case "-" -> result = num1 - num2;
                case "*" -> result = num1 * num2;
                case "/" -> {
                    if (num2==0){
                        return "Error: chia cho 0";
                    }
                    result = (float) num1/num2;
                }                    
            }
            return "Result: " + result;
        }
        catch (NumberFormatException e){
            //System.out.println("Error: Dữ liệu nhập vào không đúng định dạng");
            return "Error: Dữ liệu nhập vào không đúng định dạng";
        }
        catch (ArithmeticException e){
            //System.out.println("Error: Chia cho 0");
            return "Error: Chia cho 0";
        }
    }
}
