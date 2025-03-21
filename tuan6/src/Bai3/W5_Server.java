package Bai3;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

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
        String result = "";
        String url = "http://api.mathjs.org/v4/?expr="+URLEncoder.encode(input, StandardCharsets.UTF_8);
        try{
            Document doc = Jsoup.connect(url) // kết nối đến API
                    .method(Connection.Method.GET) // với phương thức GET
                    //.ignoreContentType(true) // sử dụng trong trường hợp dữ liệu trả về là JSON
                    .execute() // thực thi request
                    .parse(); // Chuyển dữ liệu của request từ dạng Response -> Document

            //JSONObject json = new JSONObject(doc.text()); // chuyển JSON dạng text -> Object nhờ thư viện org.json
            //System.out.println(json.get("country")); // lấy dữ liệu dựa trên thuộc tính của đối tượng json
            //System.out.println(doc.body().text());
            result=doc.body().text();
        } catch(IOException e){
            System.err.println("Loi: "+e.getMessage());
            return "Lỗi: không đúng format";
        }
        return result;
    }

    public static void main(String[] args) {
        W5_Server server = new W5_Server(12345);
        server.start();
    }
}
