package Bai2;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.StringTokenizer;

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
        if (input.equalsIgnoreCase("hello")){
            try {
                InetAddress localInet = InetAddress.getLocalHost();
                
                URI uri = new URI("https://checkip.amazonaws.com");
                URL ip = uri.toURL();
    
                try (BufferedReader br = new BufferedReader(new InputStreamReader(ip.openStream()))) {
                    String publicIP = br.readLine();
                    result = "Private: " + localInet.getHostAddress() + "\nPublic: " + publicIP;
                    System.out.println(result);
                }
            } catch (UnknownHostException e) {
                System.err.println("Không thể lấy địa chỉ IP cục bộ: " + e.getMessage());
            } catch (URISyntaxException e) {
                System.err.println("URL không hợp lệ: " + e.getMessage());
            } catch (IOException e) {
                System.err.println("Lỗi khi lấy IP công khai: " + e.getMessage());
            }
        }
        else{
            StringTokenizer stringTokenizer = new StringTokenizer(input, " ");
            if (stringTokenizer.nextToken().toString().equals("req")){
                String ip = stringTokenizer.nextToken();
                String url = "http://ip-api.com/json/"+ip+"?fields=status,message,continent,country,city,query";
                try{
                    Document doc = Jsoup.connect(url) // kết nối đến API
                            .method(Connection.Method.GET) // với phương thức GET
                            .ignoreContentType(true) // sử dụng trong trường hợp dữ liệu trả về là JSON
                            .execute() // thực thi request
                            .parse(); // Chuyển dữ liệu của request từ dạng Response -> Document
                    JSONObject json = new JSONObject(doc.text()); // chuyển JSON dạng text -> Object nhờ thư viện org.json
                    if (json.get("status").equals("success")){
                        String city = json.get("city").toString(); // lấy dữ liệu dựa trên thuộc tính của đối tượng json
                        String country = json.get("country").toString();
                        String continent =  json.get("continent").toString();
                        result = "Thành phố: "+city+ "\nQuốc gia: "+country+ "\nChâu lục: "+continent;
                    }
                    else{
                        String message = json.get("message").toString();
                        switch (message) {
                            case "private range" 
                                -> result = "IP trong private range";
                            case "reserved range"
                                -> result = "IP trong reserved range";
                            case "invalid query"
                                -> result = "IP không hợp lệ";
                        }
                    }
                } catch(IOException e){
                    System.err.println(e.getMessage());
                }
            }
            else{
                return "Error: Không đúng cú pháp";
            }
        }
        return result;
    }





    public static void main(String[] args) {
        W5_Server server = new W5_Server(12345);
        server.start();
    }
}
