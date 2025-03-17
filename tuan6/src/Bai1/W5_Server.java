package Bai1;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

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
        String url = input;
       
        //https://tiki.vn/api/v2/reviews?limit=5&include=comments,contribute_info,attribute_vote_summary&sort=score%7Cdesc,id%7Cdesc,stars%7Call&page=1&spid=58453179&product_id=58453178&seller_id=72479

        try{
            String product_id = url.substring(url.lastIndexOf("-p")+2, url.indexOf(".html"));
            System.out.println(product_id);
            String product_API = "https://tiki.vn/api/v2/products/"+product_id;
            String review_API = "https://tiki.vn/api/v2/reviews?limit=20&product_id="+product_id;

            Document product_Doc = Jsoup.connect(product_API) // kết nối đến API
                    .method(Connection.Method.GET) // với phương thức GET
                    .ignoreContentType(true) // sử dụng trong trường hợp dữ liệu trả về là JSON
                    .execute() // thực thi request
                    .parse(); // Chuyển dữ liệu của request từ dạng Response -> Document

            Document review_Doc = Jsoup.connect(review_API) // kết nối đến API
                    .method(Connection.Method.GET) // với phương thức GET
                    .ignoreContentType(true) // sử dụng trong trường hợp dữ liệu trả về là JSON
                    .execute() // thực thi request
                    .parse(); // Chuyển dữ liệu của request từ dạng Response -> Document

            //System.out.println(json.get("name")); // lấy dữ liệu dựa trên thuộc tính của đối tượng json

            JSONObject product_json = new JSONObject(product_Doc.text()); // chuyển JSON dạng text -> Object nhờ thư viện org.json
            String product_name = product_json.get("name").toString();
            String product_price = product_json.get("price").toString();

            JSONObject review_json = new JSONObject(review_Doc.text());
            String reviews_count = review_json.get("reviews_count").toString();
            String rating_average = review_json.get("rating_average").toString();
            JSONArray data_ReviewJsonArray = new JSONArray(review_json.get("data").toString());
            
            //System.out.println(data_ReviewJsonArray.getJSONObject(1).get("content"));

            result+="Tên SP: "+product_name+
                    "\nGiá: "+product_price+
                    "\nSản phẩm có "+reviews_count+" review với điểm trung bình: "+rating_average+
                    "\nChi tiết nội dung một số review:\n";
            int count=0;
            for (int i=0; i<data_ReviewJsonArray.length(); i++){
                String content = data_ReviewJsonArray.getJSONObject(i).get("content").toString();
                if (content.equals(""))
                    continue;
                result+="\t+ "+content+"\n";
                count++;
                if (count==10)
                    break;
            }
        } catch(IOException e){
            System.err.println("Loi"+e.getMessage());
        } catch(java.lang.StringIndexOutOfBoundsException e){
            return "Lỗi tra cứu thông tin, vui lòng thử lại";
        }
        return result;
    }

    public static void main(String[] args) {
        W5_Server server = new W5_Server(12345);
        server.start();
    }
}
