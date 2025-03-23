package Bai2;

import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Scanner;

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
        String response = countNum(receivedData);
        byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);
        DatagramPacket packet = new DatagramPacket(responseBytes, responseBytes.length, receivePacket.getAddress(), receivePacket.getPort());
        socket.send(packet);
    }

    //tính số lượng các số mà tổng các chữ số bằng nn
    public String countNum(String n){
        String result = "";
        
        File file = new File("src\\Bai2\\data.txt");
        try (Scanner sc = new Scanner (file)) {
            int count = 0;
            while (sc.hasNextLine()){
                int num = Integer.parseInt(sc.nextLine());
                int sum=0;
                while (num != 0){
                    sum+=num%10;
                    num=num/10;                    
                }
                if (sum == Integer.parseInt(n)){
                    count++;
                }
            }
            result=String.valueOf(count);
        } catch (Exception e) {
            System.out.println("Loi: " + e.getMessage());
        }
        return result;
    }

    public static void main(String[] args) {
        W7_Server server = new W7_Server(1234, 1024);
        server.start();
    }
}
