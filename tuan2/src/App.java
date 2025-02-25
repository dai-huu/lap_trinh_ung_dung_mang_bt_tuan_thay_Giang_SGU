import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Scanner;
import java.util.StringTokenizer;

public class App {
    public static void main(String[] args) throws Exception {
        //bai1();
        //bai2();
        //bai3();
        bai4();
    }

    public static void bai1(){
        Scanner sc = new Scanner(System.in);
        System.out.print("Nhập chuỗi phép tính: ");
        String inputString = sc.nextLine();
        float result = 0;
        try{
            StringTokenizer str = new StringTokenizer(inputString, "+-*/", true);
            if (str.countTokens() != 3) {
                throw new IllegalArgumentException("Error: Dữ liệu nhập vào không đúng định dạng");
            }
            
            String str1 = str.nextToken();
            String op = str.nextToken();
            String str2 = str.nextToken();
            
            try{
                int num1 = Integer.parseInt(str1);
                int num2 = Integer.parseInt(str2);
                switch (op) {
                    case "+" -> result = num1 + num2;
                    case "-" -> result = num1 - num2;
                    case "*" -> result = num1 * num2;
                    case "/" -> {
                        if (num2==0){
                            throw new ArithmeticException("Error: chia cho 0");
                        }
                        result = (float) num1/num2;
                    }                    
                }
                System.out.println("Result: " + result);
            }
            catch (NumberFormatException e){
                System.out.println("Error: Dữ liệu nhập vào không đúng định dạng");
            }
            catch (ArithmeticException e){
                System.out.println("Error: Chia cho 0");
            }
        }
        catch (IllegalArgumentException e){
            System.err.println(e.getMessage());
        }
        
    }

    public static void bai2(){
        String input = "Dai hoc Sai Gon  la mot trong nhung truong dai hoc lau doi nhat sai    gon";
        StringTokenizer str = new StringTokenizer(input, " ");
        LinkedHashMap <String,String> linkedHashMap = new LinkedHashMap<>();
        while (str.hasMoreTokens()){
            String word = str.nextToken();
            linkedHashMap.putIfAbsent(word.toLowerCase(), word);
            //System.out.println(word);
        }

        StringBuilder output = new StringBuilder();
        for (String word : linkedHashMap.values()){
            output.append(word + " ");
        }
        System.out.println(output.toString().trim());
    }

    public static void bai3(){
        File f = new File("tuan2\\src\\dictionary.txt");
        HashMap <String, String> hashMap = new HashMap<>();
        try {
            Scanner sc = new Scanner(f);
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                StringTokenizer stringTokenizer = new StringTokenizer(line, ";");
                hashMap.put(stringTokenizer.nextToken(), stringTokenizer.nextToken());
            }
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        }

        Scanner sc = new Scanner(System.in);
        System.out.print("Nhập từ cần tra: ");
        String word = sc.nextLine();
        String result = "";
        for (String key : hashMap.keySet()){
            if (word.equals(key)){
                result=hashMap.get(key);
                break;
            }
            if (word.equals(hashMap.get(key))){
                result=key;
                break;
            }
        }
        if (result.equals("")){
            System.out.println("Từ cần tìm không có trong từ điển");
        }
        else{
            System.out.println("Kết quả tra được: " + result);
        }
    }

    public static void bai4(){
        while (true) {
            Scanner inputSc = new Scanner(System.in);
            System.out.print("Nhập số tiền cần rút (hoặc exit): ");
            String inputString = inputSc.nextLine();
            if (inputString.equals("exit")){
                break;
            }
            Integer input = Integer.parseInt(inputString);
            LinkedHashMap <Integer,Integer> currentMap = new LinkedHashMap<>();
            File f = new File("tuan2\\src\\banking.txt");
            try (Scanner sc = new Scanner(f)) {
                while (sc.hasNextLine()) {
                    String line = sc.nextLine();
                    StringTokenizer stringTokenizer = new StringTokenizer(line, ";");
                    currentMap.put(Integer.parseInt(stringTokenizer.nextToken()), Integer.parseInt(stringTokenizer.nextToken()));
                }
                
            } catch (FileNotFoundException e) {
                System.err.println(e.getMessage());
            }

            Integer current = 0;
            for (Integer key : currentMap.keySet()){
                current+=key*currentMap.get(key);
            }

            if (current < input){
                System.out.println("Error: Số tiền muốn rút lớn hơn số tiền ATM hiện đang cất giữ !");
            }
            else{
                LinkedHashMap <Integer,Integer> outputMap = new LinkedHashMap<>();
                for (Integer key : currentMap.keySet()){
                    //int value = key;
                    //int quantity = currentMap.get(key);
                    if (currentMap.get(key)==0){
                        continue;
                    }
                    if ((input%key)==0){                    
                        if ((input/key) <= currentMap.get(key)){
                            outputMap.put(key, input/key);
                            currentMap.put(key, currentMap.get(key)-input/key);
                            input=0;
                            break;
                        }
                        else{
                            outputMap.put(key, currentMap.get(key));
                            input=input-key*currentMap.get(key);
                            currentMap.put(key, 0);
                        }
                    }
                    else{
                        if ((input/key)>0){
                            outputMap.put(key, input/key);
                            currentMap.put(key, currentMap.get(key)-input/key);
                            input=input-key*(input/key);                        
                        }
                    }
                }
                if (input==0){
                    System.out.println("Kết quả:");
                    for (Integer key : outputMap.keySet()){
                        System.out.println(key + " " + outputMap.get(key));
                    }
                    try (BufferedWriter writer = new BufferedWriter(new FileWriter(f))) {
                        for (Integer key : currentMap.keySet()){
                            writer.write(key + ";" + currentMap.get(key) + "\n");                        
                        }
                    } catch (IOException e) {
                        System.err.println(e.getMessage());
                    }
                }
                else{
                    System.out.println("Error: Không thỏa yêu cầu rút tiền hiện tại ");
                }
            }
        }
    }
}
