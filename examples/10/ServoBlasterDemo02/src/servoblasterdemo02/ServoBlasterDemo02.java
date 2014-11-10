package servoblasterdemo02;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class ServoBlasterDemo02 {

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

        for (int t = 0; t < 3; t++) {
            for (int i = 70; i <= 250; i+= 10) {
                System.out.println(i + "...");
                set("P1-12", Integer.toString(i));
                delay(20);
            }

            for (int i = 250; i >= 70; i-= 10) {
                System.out.println(i + "...");
                set("P1-12", Integer.toString(i));
                delay(20);
            }

        }

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
