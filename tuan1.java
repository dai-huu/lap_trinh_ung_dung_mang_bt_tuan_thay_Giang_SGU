/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */

/**
 *
 * @author ADMIN
 */

import java.util.Scanner;
import java.util.HashMap;

public class tuan1 {

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        // TODO code application logic here
        
        Scanner reader = new Scanner(System.in);
        System.out.print("Nhap so nguyen duong n: ");
        try {
            int n = reader.nextInt();
            bai1(n);
            bai2(n);
            bai3(n);
            bai4(n);
        }
        catch (java.util.InputMismatchException e){
            System.out.print("ERROR!!! Nhap sai!!!");
        }
        
    }
    
    /*Bài 1: 
    Viết chương trình nhận vào n là một số nguyên dương và xuất ra tổng S=1+2+...+n. 
        Input: 100 
        Output: 5050  
    */
    public static void bai1 (int n){
        System.out.println("Bài 1: Viết chương trình nhận vào n là một số nguyên dương và xuất ra tổng S=1+2+...+n. \n Input: 100 \n Output: 5050 ");
        if (n<1){
            System.out.println("ERROR!!! n phai la so nguyen duong");
            return;
        }
        long sum=0;

        //System.out.println(n);
        for (int i=1; i<=n; i++){
            sum+=i;
        }
        System.out.println("Tong S = "+sum);
    }
    
    /*Bài 2:
    Viết chương trình nhận vào n >=3 và xuất ra tổng các ước số của n. 
        Input: 12  
        Output: 16 
    */
    public static void bai2 (int n){
        System.out.println("Bài 2: Viết chương trình nhận vào n >=3 và xuất ra tổng các ước số của n. \n Input: 12\n Output: 16 ");
        if (n<3){
            System.out.println("ERROR!!! n phai >= 3");
            return;
        }
        long sum=0;

        for (int i=1; i<=n; i++){
            if (n%i==0)
                sum+=i;
        }
        System.out.println("Tong cac uoc so cua n: " + sum);
    }
    
    /*Bài 3:
    Viết chương trình nhận vào n>=3 và xuất ra tổng các số nguyên tố <=n
        Input: 12
        Output: 28
    */
    public static boolean isPrime(int n){
        if (n<2)
            return false;
        for (int i=2; i<=Math.sqrt(n); i++){
            if (n%i==0)
                return false; 
        }
        return true;
    }
    
    public static void bai3(int n){
        System.out.println("Bài 3: Viết chương trình nhận vào n>=3 và xuất ra tổng các số nguyên tố <=n \n Input: 12 \n Output: 28");
        if (n<3){
            System.out.println("ERROR!!! n phai >= 3");
            return;
        }
        long sum=0;
        for (int i=3; i<=n; i++){
            if (isPrime(i)){
                sum+=i;
                //System.out.print(i + " ");
            }
        }
        System.out.println("S = " + sum);
    }
    
    /*Bài 4:
    Viết chương trình nhận vào n là một số nguyên dương và phân tích thành tích các thừa số nguyên tố
        Input: 1122334455
        Output: 3^1x5^1x11^1x6802027^1
    */
    public static void bai4(int n){
        System.out.println("Viết chương trình nhận vào n là một số nguyên dương và phân tích thành tích các thừa số nguyên tố \n Input: 1122334455\n Output: 3^1x5^1x11^1x6802027^1");
        if (n<1){
            System.out.println("ERROR!!! n phai la so nguyen duong");
            return;
        }
        if (n==1){
            System.out.println("Result: 1");
            return;
        }
        HashMap<Integer, Integer> primeCount = new HashMap<Integer, Integer>();
        int count=0;
        for (int i=2; i<=n; i++){
            if (isPrime(i)){
                if (n%i==0){
                    n=n/i;
                    count+=1;
                    primeCount.put(i,count);
                    i-=1;
                }
                else
                    count=0;
            }
        }
        System.out.print("Result: ");
        count=0;
        for (int key : primeCount.keySet()){
            count+=1;
            if (count==primeCount.size()){
                System.out.print(key+"^"+primeCount.get(key));
                break;
            }
            System.out.print(key+"^"+primeCount.get(key)+"x");
        }
    }
    
}
