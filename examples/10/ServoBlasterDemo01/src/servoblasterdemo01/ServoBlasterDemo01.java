package servoblasterdemo01;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class ServoBlasterDemo01 {
    
    public static void set(String pin, String value) {
        try (OutputStream out = new FileOutputStream("/dev/servoblaster");
             OutputStreamWriter writer = new OutputStreamWriter(out)) {
            writer.write(pin + "=" + value + "\n");
            writer.flush();
        }
        catch (IOException e) {
            System.out.println("================= " + e);
        }
    }
    
    public static void main(String[] args) {
        
        set("P1-12", "70");
        delay(1000);
        
        set("P1-12", "150");
        delay(1000);
        
        set("P1-12", "250");
        delay(1000);
        
    }
    
    public static void delay(int ms) {
        try {
            Thread.sleep(ms);
        }
        catch (InterruptedException e) {
            System.out.println("================= " + e);
        }
    }
    
}
