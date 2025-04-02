import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class Ex25_ServerMultithread {
    private static final int PORT = 12345;
    private static final int N_THREADS = 10;

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(N_THREADS);
        System.out.println("ThreadPool Server đang khởi tạo với " + N_THREADS + " thread...");

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server đang lắng nghe trên cổng " + PORT + "...");

            // Shutdown hook để đóng ExecutorService khi ứng dụng thoát (Ctrl+C)
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                System.out.println("Đang shutdown ExecutorService...");
                shutdownExecutor(executorService);
                System.out.println("ExecutorService đã shutdown.");
            }));

            while (!executorService.isShutdown()) {
                try {
                    System.out.println("Đang chờ client kết nối...");
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("Client mới đã kết nối: " + clientSocket.getInetAddress().getHostAddress());

                    ClientHandler clientHandler = new ClientHandler(clientSocket);
                    executorService.execute(clientHandler);
                } catch (IOException e) {
                    if (executorService.isShutdown()) {
                        System.out.println("Server socket đã đóng do executor shutdown.");
                        break;
                    }
                    System.err.println("Lỗi khi chấp nhận kết nối client: " + e.getMessage());
                }
            }
        } catch (IOException ex) {
            System.err.println("Không thể khởi động server trên cổng " + PORT + ": " + ex.getMessage());
            shutdownExecutor(executorService);
        } finally {
            if (!executorService.isTerminated()) {
                System.out.println("Thực hiện shutdown cuối cùng cho ExecutorService...");
                shutdownExecutor(executorService);
            }
            System.out.println("Server đã dừng.");
        }
    }

    // Phương thức shutdown ExecutorService an toàn
    private static void shutdownExecutor(ExecutorService executor) {
        executor.shutdown(); // Ngừng nhận task mới
        try {
            // Đợi 60s cho các task hiện tại hoàn thành
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                executor.shutdownNow(); // Hủy các task đang chạy nếu quá thời gian chờ
                // Đợi tiếp 60s để các task bị hủy phản hồi
                if (!executor.awaitTermination(60, TimeUnit.SECONDS))
                    System.err.println("ExecutorService không thể dừng hẳn.");
            }
        } catch (InterruptedException ie) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
