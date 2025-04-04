package Bai2;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class ReadRandomNumbers {
    static ArrayList <Integer> numbers = new ArrayList<>();
    public static void main(String[] args) {
        String filename = "src\\Bai2\\data.txt";

        try (Scanner scanner = new Scanner(new File(filename))) {
            while (scanner.hasNextInt()) {
                Integer number = scanner.nextInt();
                numbers.add(number);
            }
            numbers.sort(null);
            System.out.println("Đã xếp xong");
        } catch (FileNotFoundException e) {
            System.err.println("Không tìm thấy file: " + filename);
        }
    }
}