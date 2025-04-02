package Bai1;

public class pi_MonteCarlo extends Thread{
    public static int inside_point=0;

    private static synchronized void incrementCount() {
        inside_point ++;
    }

    private void cal_inside_point() {
        double rand_x, rand_y;
        int local_count=0;
        for (int i=1; i<=1000000; i++){
            rand_x = Math.random()*2-1;
            rand_y = Math.random()*2-1;

            if (rand_x*rand_x + rand_y*rand_y <= 1) {
                incrementCount();
                //inside_point++;
                local_count++;
                
            }
        }
        System.out.println(local_count);
    }

    public void run(){
        cal_inside_point();
        //System.out.println(inside_point);
        
    }

    public static double cal_pi(){
        return 4.0*inside_point/2000000;
    }

    
}
