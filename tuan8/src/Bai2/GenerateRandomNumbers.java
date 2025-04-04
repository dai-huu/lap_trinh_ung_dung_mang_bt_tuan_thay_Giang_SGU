package Bai2;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class GenerateRandomNumbers {

    public static void main(String[] args) {
        int numberOfNumbers = 1000000;
        String filename = "src\\Bai2\\data.txt";
        Random random = new Random();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (int i = 0; i < numberOfNumbers; i++) {
                int randomNumber = random.nextInt(); // Sinh số nguyên ngẫu nhiên (bao gồm cả âm và dương)
                writer.write(Integer.toString(randomNumber));
                writer.newLine(); // Thêm dòng mới sau mỗi số
            }
            System.out.println("Đã ghi " + numberOfNumbers + " số ngẫu nhiên vào file " + filename);
        } catch (IOException e) {
            System.err.println("Lỗi khi ghi vào file: " + e.getMessage());
        }
    }
}
