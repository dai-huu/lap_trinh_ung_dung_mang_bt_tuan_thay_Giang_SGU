package Bai1;

public class bai1 {
    public static void main(String[] args) {
        pi_MonteCarlo p1 = new pi_MonteCarlo();
        pi_MonteCarlo p2 = new pi_MonteCarlo(); 
        p1.start();
        p2.start();
        
        try {
            p1.join();
            p2.join();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        

        System.out.println(pi_MonteCarlo.inside_point);
        System.out.println("pi: "+pi_MonteCarlo.cal_pi());  
    }
}
