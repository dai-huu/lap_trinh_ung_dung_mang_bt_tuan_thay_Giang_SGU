import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;

public class App {
    public static void main(String[] args) throws Exception {
        //bai1();
        //bai2();
        //bai3();
        bai4();
    }

    public static void bai1(){
        String domain = "";
        while (true){
            try {
                Scanner reader = new Scanner(System.in);
                System.out.print("Nhap domain (hoac exit): ");
                domain = reader.nextLine();
                if (domain.equals("exit")){
                    break;
                }
                InetAddress inet = InetAddress.getByName(domain);
                System.out.println("Domain " + domain + " có IP: " + inet.getHostAddress()); 
            } catch (UnknownHostException e) {
                //System.err.println(e.getMessage());
                System.out.println("Domain " + domain + " không ton tai");
            }
        }
    }

    public static void bai2(){
        File file = new File("tuan3\\src\\input.txt");
        try (Scanner sc = new Scanner(file)){
            while (sc.hasNextLine()){    
                String domain = sc.nextLine().trim();            
                try {                
                    InetAddress inet = InetAddress.getByName(domain);
                    System.out.println(domain + ": " + inet.getHostAddress());
                } catch (UnknownHostException e) {
                    System.out.println(domain + " khong ton tai");
                }
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public static void bai3(){
        File f = new File("tuan3\\src\\IPtext.txt");
        try (Scanner sc = new Scanner(f)) {
            while(sc.hasNextLine()){
                String ip = sc.nextLine();
                try {
                    InetAddress inet = InetAddress.getByName(ip);
                    if (inet.isReachable(2500)){
                        System.out.println("IP " + ip + " is reachable");
                    }
                    else{
                        System.out.println("IP " + ip + " is not reachable");
                    }
                } catch (UnknownHostException e) {
                    //e.printStackTrace();
                    System.err.println(e.getMessage());
                } catch (IOException e) {
                    //e.printStackTrace();
                    System.err.println(e.getMessage());
                }
            }
        } catch (FileNotFoundException e) {            
            //e.printStackTrace();
            System.err.println(e.getMessage());
        }
    }

    public static void bai4(){
        try {
            InetAddress localInet = InetAddress.getLocalHost();
            String localIP = localInet.getHostAddress();
            //System.out.println(localInet.getHostName());
            String net = localIP.substring(0, localIP.lastIndexOf(".")+1);
            //ArrayList <String> onlineIPs = new ArrayList<>();
            for (int i=1; i<=254; i++){
                String ip = net + i;
                InetAddress inet = InetAddress.getByName(ip);
                System.out.print(ip);
                if (inet.isReachable(200)){
                    //onlineIPs.add(ip);
                    System.out.println(" ==> is online");
                }
                else System.out.println();
            }
/*             System.out.println("Các IP thuộc đường mạng " + net + "x đang có các thiết bị online sau: ");
            for (String ip : onlineIPs){
                System.out.println(ip);
            } */
        } catch (UnknownHostException e) {
            System.err.println(e.getMessage());
            //e.printStackTrace();
        } catch (IOException e) {
            System.err.println(e.getMessage());
            //e.printStackTrace();
        }
        
    }
}
