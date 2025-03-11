package Bai1;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Scanner;
import java.util.StringTokenizer;

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
        File f = new File("src\\Bai1\\dictionary.txt");
        HashMap <String, String> hashMap = new HashMap<>();
        try (Scanner sc = new Scanner(f);){            
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                StringTokenizer stringTokenizer = new StringTokenizer(line, ";");
                hashMap.put(stringTokenizer.nextToken(), stringTokenizer.nextToken());
            }
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        }

        String result = "";
        for (String key : hashMap.keySet()){
            if (input.equals(key)){
                result=hashMap.get(key);
                break;
            }
            if (input.equals(hashMap.get(key))){
                result=key;
                break;
            }
        }
        if (result.equals("")){
           return "Từ cần tìm không có trong từ điển";
        }
        else{
            return "Kết quả tra được: " + result;
        }
    }

    public static void main(String[] args) {
        W5_Server server = new W5_Server(12345);
        server.start();
    }
}
