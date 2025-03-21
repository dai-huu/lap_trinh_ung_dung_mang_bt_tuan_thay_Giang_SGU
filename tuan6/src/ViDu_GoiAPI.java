import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.IOException;

public class ViDu_GoiAPI {
    public static void main(String[] args) {
        String url = "http://ip-api.com/json/200.100.200.200?fields=status,message,continent,country,region,regionName,city,zip,lat,lon,timezone,currency,isp,org,as,query";
        try{
            Document doc = Jsoup.connect(url) // kết nối đến API
                    .method(Connection.Method.GET) // với phương thức GET
                    .ignoreContentType(true) // sử dụng trong trường hợp dữ liệu trả về là JSON
                    .execute() // thực thi request
                    .parse(); // Chuyển dữ liệu của request từ dạng Response -> Document
            JSONObject json = new JSONObject(doc.text()); // chuyển JSON dạng text -> Object nhờ thư viện org.json
            System.out.println(json.get("country")); // lấy dữ liệu dựa trên thuộc tính của đối tượng json
        } catch(IOException e){
            System.err.println(e.getMessage());
        }
    }
}
