import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.IOException;

public class AppApp {
    public static void main(String[] args) {
        String url = "https://booking-com15.p.rapidapi.com/api/v1/hotels/searchDestination?query=quangngai";
        try{
            Document doc = Jsoup.connect(url) // kết nối đến API
                    .header("x-rapidapi-key", "06f314f69dmsh8c251b2292d74d3p1e68e1jsn40b7fbb081ce")
                    .header("x-rapidapi-host", "booking-com15.p.rapidapi.com")
                    .method(Connection.Method.GET) // với phương thức GET
                    .ignoreContentType(true) // sử dụng trong trường hợp dữ liệu trả về là JSON
                    .execute() // thực thi request
                    .parse(); // Chuyển dữ liệu của request từ dạng Response -> Document
            JSONObject json = new JSONObject(doc.text()); // chuyển JSON dạng text -> Object nhờ thư viện org.json
            //System.out.println(json.get("country")); // lấy dữ liệu dựa trên thuộc tính của đối tượng json
            System.out.println(json.get("data"));
        } catch(IOException e){
            System.err.println(e.getMessage());
        }

    }
}
