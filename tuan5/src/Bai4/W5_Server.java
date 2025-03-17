package Bai4;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

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
        //String url = "https://masothue.com/Search/?q=079204012542&type=auto&force-search=1";
        String url = "https://masothue.com/Search/?q="+input+"&type=auto&force-search=1";
        try{
            Document doc = Jsoup.connect(url) // kết nối đến API
                    .method(Connection.Method.GET) // với phương thức GET
                    .execute() // thực thi request
                    .parse(); // Chuyển dữ liệu của request từ dạng Response -> Document

            Elements table = doc.getElementsByClass("table-taxinfo");

            result = doc.getElementsByClass("h1").first().text()+"\n";

            //Cắt chữ đầu tiên ra trong "Tra cứu..." của h1
            if (result.substring(0, 3).equals("Tra"))
                return "Không tìm thấy thông tin";

            // Lấy tất cả hàng trong bảng
            Elements rows = table.select("tr");
            //result+="\nHọ tên: " + rows.get(0).text() + "\n";
            for (int i=1; i<=6; i++){
                result+=rows.get(i).select("td").get(0).text() + ": " + 
                        rows.get(i).select("td").get(1).text() + "\n";
            }
            result+=rows.get(7).text()+"\n";
        } catch(IOException e){
            System.err.println("Loi"+e.getMessage());
        } catch(java.lang.NullPointerException e){
            System.err.println(e.getMessage());
            return "Không tìm thấy thông tin\n";
        }

        return result;
    }

    public static void main(String[] args) {
        W5_Server server = new W5_Server(12345);
        server.start();
    }
}
