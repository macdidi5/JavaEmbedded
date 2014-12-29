package parkingdemo;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class ServoBlaster {
    
    private final String pin;
    private final int start, end;
    private final float scale;
    
    public ServoBlaster(String pin, int start, int end) {
        this.pin = pin;
        this.start = start;
        this.end = end;
        scale = (end - start) / 180F;
    }
    
    // 設定伺服馬達的角度
    public void setAngle(int angle) {
        if (angle >= 0 && angle <= 180) {
            set(pin, Integer.toString(angleToValue(angle)));
        }
    }

    private int angleToValue(int angle) {
        return (int)Math.round(angle * scale) + start;
    }
    
    // 設定伺服馬達
    private static void set(String pin, String value) {
        try (OutputStream out = new FileOutputStream("/dev/servoblaster");
             OutputStreamWriter writer = new OutputStreamWriter(out)) {
            writer.write(pin + "=" + value + "\n");
            writer.flush();
        }
        catch (IOException e) {
            System.out.println("================= " + e);
        }
    }
    
}
