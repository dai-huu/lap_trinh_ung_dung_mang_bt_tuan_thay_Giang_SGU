package Bai3;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class W5_Server {
    private int port;

    // Constructor
    public W5_Server(int port) {
        this.port = port;
    }

    // Khởi tạo server, tạo đối tượng socket tương ứng từng client
    public void start() {
        try (ServerSocket server = new ServerSocket(port)) {
            while (true) {
                System.out.println("Server đang lắng nghe tại port " + port);
                Socket socket = server.accept();
                handleClient(socket);
            }
        } catch (IOException e) {
            System.err.println("Lỗi khởi tạo server socket: " + e.getMessage());
        }
    }

    // Xử lý khi có client kết nối
    private void handleClient(Socket socket) {
        System.out.println("Đã chấp nhận kết nối từ client: " + socket.getRemoteSocketAddress());
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true)) {
            String dataFromClient;
            while ((dataFromClient = reader.readLine()) != null) {
                System.out.println("Server nhận: " + dataFromClient);
                if (dataFromClient.equalsIgnoreCase("bye")) {
                    System.out.println("Server nhận yêu cầu đóng kết nối từ client.");
                    break;
                }
                String response = processData(dataFromClient);
                writer.println(response);
                writer.println("<END>"); // Báo client biết đã kết thúc gửi dữ liệu.
            }
        } catch (IOException e) {
            System.err.println("Lỗi kết nối từ client: " + e.getMessage());
        }
    }

    // Xử lý dữ liệu
    private String processData(String input) {
        double rand_x, rand_y;
        int inside_point=0; 
        Integer n = Integer.parseInt(input);
        for (int i=1; i<=n; i++){
            rand_x = Math.random()*2-1;
            rand_y = Math.random()*2-1;

            if (rand_x*rand_x + rand_y*rand_y <= 1) {
                inside_point++;
            }
        }
        double pi = 4.00*inside_point/n;
        return "pi tính được: " + Double.toString(pi);
    }

    public static void main(String[] args) {
        W5_Server server = new W5_Server(12345);
        server.start();
    }
}
