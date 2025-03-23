package Bai3;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class W7_Server {
    private int port;
    private int bufferSize;
    private DatagramPacket receivePacket; // chia sẻ giữa sendData và receivedData

    public W7_Server(int port, int bufferSize) {
        this.port = port;
        this.bufferSize = bufferSize;
    }

    public void start(){
        try(DatagramSocket socket = new DatagramSocket(port)){
            while(true){
                String receivedData = receivedData(socket); // nên bắt SocketTimeoutException ở đây
                System.out.println("Server nhận: " + receivedData);
                if(receivedData.equalsIgnoreCase("exit")){
                    System.out.println("Server đóng kết nối.");
                    break;
                }
                sendData(socket, receivedData);
            }
        }catch(IOException e){
            System.err.println(e.getMessage());
        }
    }

    private String receivedData(DatagramSocket socket) throws IOException {
        receivePacket = new DatagramPacket(new byte[bufferSize], bufferSize);
        socket.receive(receivePacket);
        byte[] receivedBytes = Arrays.copyOf(receivePacket.getData(), receivePacket.getLength());
        return new String(receivedBytes, StandardCharsets.UTF_8);
    }

    private void sendData(DatagramSocket socket, String receivedData) throws IOException {
        String response = processInput(receivedData);
        byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);
        DatagramPacket packet = new DatagramPacket(responseBytes, responseBytes.length, receivePacket.getAddress(), receivePacket.getPort());
        socket.send(packet);
    }

    public String convert(String x, String y, String number){
        String result = "";
        String url = "https://networkcalc.com/api/binary/"+number+"?"+"from="+x+"&to="+y;
        try{
            Document doc = Jsoup.connect(url) // kết nối đến API
                    .method(Connection.Method.GET) // với phương thức GET
                    .ignoreContentType(true) // sử dụng trong trường hợp dữ liệu trả về là JSON
                    .execute() // thực thi request
                    .parse(); // Chuyển dữ liệu của request từ dạng Response -> Document
            JSONObject json = new JSONObject(doc.text()); // chuyển JSON dạng text -> Object nhờ thư viện org.json
            //System.out.println(json.get("converted")); // lấy dữ liệu dựa trên thuộc tính của đối tượng json
/*             if (json.get("status").toString().equals("OK"))
                result = json.get("converted").toString();
            else 
                result = "Lỗi dữ liệu nhập vào";   */              

            result=json.get("converted").toString();
        } catch(IOException e){
            System.err.println("Lõi: "+e.getMessage());
            result= "Lỗi dữ liệu nhập vào";
        }
        return result;
    }

    public String processInput(String input){
        String result="";
        try{
            String data[]=input.split(":");
            //cơ số cần chuyển sang
            String base[]=data[1].split(",");
            if (base.length==1){
                result+=convert(data[0],data[1] , data[2]);
            }
            else{
                String convert1 = convert(data[0],base[0] , data[2]);
                String convert2 = convert(data[0],base[1] , data[2]);
                result+="\n\tChuyển sang cơ số "+base[0]+": "+convert1+
                        "\n\tChuyển sang cơ số "+base[1]+": "+convert2+"\n";
            }
        }
        catch (java.lang.ArrayIndexOutOfBoundsException e){
            System.out.println("Loi "+e.getMessage());
            result="Lỗi cú pháp";
        } 
        return result;
    }

    public static void main(String[] args) {
        W7_Server server = new W7_Server(1234, 1024);
        server.start();
    }
}
