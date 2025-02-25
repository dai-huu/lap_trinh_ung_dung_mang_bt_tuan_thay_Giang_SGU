import java.net.*;
import java.util.*;

public class NetworkScannerWithInterface {
    public static void main(String[] args) {
        try {
            // Liệt kê tất cả các giao diện mạng
            List<NetworkInterface> interfaces = getNetworkInterfaces();
            if (interfaces.isEmpty()) {
                System.out.println("Không tìm thấy giao diện mạng nào.");
                return;
            }

            // Hiển thị danh sách giao diện mạng
            System.out.println("🔍 Danh sách giao diện mạng:");
            for (int i = 0; i < interfaces.size(); i++) {
                System.out.println((i + 1) + ". " + interfaces.get(i).getDisplayName());
            }

            // Người dùng chọn giao diện mạng
            Scanner scanner = new Scanner(System.in);
            System.out.print("\nChọn số của giao diện mạng: ");
            int choice = scanner.nextInt();
            if (choice < 1 || choice > interfaces.size()) {
                System.out.println("Lựa chọn không hợp lệ.");
                return;
            }
            NetworkInterface selectedInterface = interfaces.get(choice - 1);

            // Lấy subnet từ giao diện đã chọn
            String subnet = getSubnetFromInterface(selectedInterface);
            if (subnet == null) {
                System.out.println("Không thể lấy subnet từ giao diện mạng này.");
                return;
            }

            // Quét thiết bị trong mạng
            System.out.println("\n🔍 Đang quét mạng: " + subnet + ".x...");
            List<String> activeDevices = new ArrayList<>();
            for (int i = 1; i <= 254; i++) {
                String host = subnet + "." + i;
                if (isReachable(host)) {
                    activeDevices.add(host);
                    System.out.println("✅ Online: " + host);
                }
            }

            // Hiển thị kết quả
            System.out.println("\n📋 Thiết bị đang hoạt động trên mạng " + subnet + ".x:");
            for (String ip : activeDevices) {
                System.out.println(ip);
            }

        } catch (Exception e) {
            System.out.println("Lỗi: " + e.getMessage());
        }
    }

    // Lấy danh sách các giao diện mạng đang hoạt động
    public static List<NetworkInterface> getNetworkInterfaces() throws SocketException {
        List<NetworkInterface> interfaceList = new ArrayList<>();
        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
        while (interfaces.hasMoreElements()) {
            NetworkInterface networkInterface = interfaces.nextElement();
            if (!networkInterface.isUp() || networkInterface.isLoopback() || networkInterface.isVirtual()) {
                continue;
            }
            interfaceList.add(networkInterface);
        }
        return interfaceList;
    }

    // Lấy subnet từ giao diện mạng đã chọn
    public static String getSubnetFromInterface(NetworkInterface networkInterface) throws SocketException {
        Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
        while (addresses.hasMoreElements()) {
            InetAddress address = addresses.nextElement();
            if (address instanceof Inet4Address && !address.isLoopbackAddress()) {
                String ip = address.getHostAddress();
                return ip.substring(0, ip.lastIndexOf(".")); // Lấy subnet (ví dụ: 192.168.1)
            }
        }
        return null; // Không tìm thấy subnet hợp lệ
    }

    // Kiểm tra xem IP có đang online không (ping)
    public static boolean isReachable(String ip) {
        try {
            InetAddress address = InetAddress.getByName(ip);
            return address.isReachable(10); // Timeout 500ms
        } catch (Exception e) {
            return false;
        }
    }
}
