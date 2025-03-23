package Bai4;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class W7_Server {
    private int port;
    private int bufferSize;
    private DatagramPacket receivePacket; // chia sẻ giữa sendData và receivedData
    private int randomNumber;
    private long startTime;
    private int count;
    

    public W7_Server(int port, int bufferSize) {
        this.port = port;
        this.bufferSize = bufferSize;
    }

    public void start(){
        renew();
        try(DatagramSocket socket = new DatagramSocket(port)){
            while(true){
                System.out.println("Số cần đoán: " + this.randomNumber);
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

    private void renew(){
        this.randomNumber = (int)(Math.random() * 501); // 0 to 500
        this.startTime = System.currentTimeMillis();
        count=1;
    }

    public String processInput(String input){
        String result=""; 
        int guess=Integer.parseInt(input);  
        if (guess==this.randomNumber){
           long endTime = System.currentTimeMillis(); 
           result+="Bạn đã đoán đúng!!!\n\tSố lần đoán: "+count+"\n\tThời gian đoán: "+(endTime - startTime)+"ms";
           renew();
        }
        else if (guess > this.randomNumber){
            result+="Số bạn vừa đoán lớn hơn số cần tìm";
            count++;
        }
        else {
            result+="Số bạn vừa đoán nhỏ hơn số tìm";
            count++;
        }
        return result;
    }

    public static void main(String[] args) {
        W7_Server server = new W7_Server(1234, 1024);
        server.start();
    }
}
