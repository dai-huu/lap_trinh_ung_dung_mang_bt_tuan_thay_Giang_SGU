
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Scanner;

public class W7_Client {
    private String host;
    private int port;
    private int bufferSize;

    public W7_Client(String host, int port, int bufferSize){
        this.host = host;
        this.port = port;
        this.bufferSize = bufferSize;
    }

    public void start(){
            try(DatagramSocket socket = new DatagramSocket();
            Scanner scanner = new Scanner(System.in)){
                socket.setSoTimeout(5000); // đặt thời gian timeout cho receive là 5s
                InetAddress address = InetAddress.getByName(host);
                while(true){
                    String input = getInput(scanner);
                    sendData(socket, address, input);
                    if(input.equalsIgnoreCase("exit")){
                        System.out.println("Client nhận được yêu cầu kết thúc");
                        break;
                    }
                    String receivedData = receiveData(socket); // nên bắt SocketTimeoutException ở đây
                    System.out.println("Client nhận: " + receivedData);
                }
            }catch(IOException e){
                System.err.println(e.getMessage());
            }
    }
    // Nhận dữ liệu từ bàn phím
    private String getInput(Scanner scanner){
        System.out.print("Nhập dữ liệu: ");
        return scanner.nextLine();
    }
    // Gửi dữ liệu đến Server
    private void sendData(DatagramSocket socket, InetAddress address, String input) throws IOException {
        byte[] dataBytes = input.getBytes(StandardCharsets.UTF_8);
        DatagramPacket packet = new DatagramPacket(dataBytes, dataBytes.length, address, port);
        socket.send(packet);
    }
    // Nhận dữ liệu từ Server
    private String receiveData(DatagramSocket socket) throws IOException {
        DatagramPacket packet = new DatagramPacket(new byte[bufferSize], bufferSize);
        socket.receive(packet);
        byte[] dataBytes = Arrays.copyOf(packet.getData(), packet.getLength());
        return new String(dataBytes, StandardCharsets.UTF_8);
    }

    public static void main(String[] args) {
        W7_Client client = new W7_Client("127.0.0.1", 1234, 1024);
        client.start();
    }
}
