package parkingdemo;

import com.pi4j.io.gpio.GpioPinDigitalOutput;

/**
 * LED閃爍執行緒類別
 * @author macdidi5
 */
public class LedBlinker extends Thread {
    
    // 控制LED的GPIO針腳
    private final GpioPinDigitalOutput pin;
    
    // 是否結束
    private boolean exit = false;
    
    // 是否閃爍
    private boolean blink = false;
    
    /**
     * 使用指定的GPIO針腳建立LedBlinker物件
     * @param pin
     */
    public LedBlinker(GpioPinDigitalOutput pin) {
        this.pin = pin;
    }
    
    @Override
    public void run() {
        while (!exit) {
            // 如果在沒有閃爍的狀態
            if (!blink) {
                synchronized(this) {
                    try {
                        // 等候
                        wait();
                    }
                    catch (InterruptedException e) {
                        System.out.println("============ " + e.toString());
                    }
                }
            }
            
            // 如果還沒有結束
            if (!exit) {
                // 輸出高電壓0.2秒
                pin.pulse(200, true);
                delay(200);
            }
        }
    }
    
    /**
     * 開始閃爍
     */
    public synchronized void startBlink() {
        blink = true;
        notify();
    }
    
    /**
     * 停止閃爍
     */
    public void stopBlink() {
        blink = false;
    }
    
    /**
     * 結束
     */
    public synchronized void exit() {
        exit = true;
        notify();
    }
    
    /**
     * 暫停指定的時間（毫秒、1000分之一秒）
     * 
     * @param ms
     */
    public static void delay(int ms) {
        try {
            Thread.sleep(ms);
        }
        catch (InterruptedException e) {
            System.out.println("============ " + e.toString());
        }        
    }
    
}
