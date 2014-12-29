package parkingdemo;

import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;

public class UltraSoundSensor {

    // 觸發超音波感應器針腳物件
    private final GpioPinDigitalOutput pinTrig;
    // 超音波感應器回應針腳物件
    private final GpioPinDigitalInput pinEcho;

    // 是否結束距離範圍偵測
    private boolean exit;

    // 距離偵測判斷次數
    private static final int CHECK = 5;

    public UltraSoundSensor(GpioPinDigitalOutput pinTrig, 
            GpioPinDigitalInput pinEcho) {
        this.pinTrig = pinTrig;
        this.pinEcho = pinEcho;
    }

    // 設定監聽物件與判斷的距離
    public void setListener(final UltraSensorListener listener,
            final int range) {
        exit = false;

        new Thread() {
            @Override
            public void run() {
                // 否在距離範圍內
                boolean touch = false;
                int distance = -1;

                while (!exit) {
                    // 讀取檢查後的距離
                    distance = (int)getDistance();

                    // 如果傳回的正確的距離
                    if (distance != -1) {
                        // 判斷是否在範圍內或範圍外
                        if (touch ? distance > range : distance <= range) {
                            // 如果在範圍內
                            if (touch) {
                                // 通知監聽物件已離開範圍距離
                                listener.onLeaving(distance);
                            }
                            // 如果在範圍外
                            else {
                                // 通知監聽物件已進入範圍距離
                                listener.onApproaching(distance);
                            }

                            // 重設範圍內或範圍外狀態
                            touch = !touch;
                        }
                    }

                    delay(100);
                }
            }

        }.start();
    }

    // 移除監聽物件
    public void removeListener() {
        exit = true;
    }

    // 傳回檢查後的距離
    public float getDistance() {
        float result = -1F;
        float oldValue = 0.0F;
        int counter = 0, leave = 0;

        oldValue = detect();

        // 連續讀取相同距離或還沒有超過100次
        while (counter < CHECK && leave < 100) {
            result = detect();

            // 兩次讀取的距離差異不超過1
            if (oldValue - result < 1) {
                oldValue = result;
                counter++;
            }
            else {
                counter = 0;
            }

            leave++;
        }

        return result;
    }

    // 暫停指定的時間（毫秒、1000分之一秒）    
    public static void delay(int ms) {
        try {
            Thread.sleep(ms);
        }
        catch (InterruptedException e) {
            System.out.println("============ " + e.toString());
        }        
    }

    // 暫停指定的時間（毫秒，奈秒）       
    public static void delay(int ms, int ns) {
        try {
            Thread.sleep(ms, ns);
        }
        catch (InterruptedException e) {
            System.out.println("============ " + e.toString());
        }        
    }

    // 觸發超音波感應器
    private void trigger() {
        // 設定為高電壓
        pinTrig.setState(true);
        // 維持10000奈秒
        delay(0, 10000);
        // 設定為低電壓
        pinTrig.setState(false);
    }

    // 判斷超音波感應器是否回應高電壓，表示感應器發射超音波
    private boolean echoHigh() {
        // 測試五千次
        for (int i = 0; i < 5000; i++) {
            // 如果是高電壓
            if (pinEcho.isHigh()) {
                return true;
            }
        }

        return false;
    }

    // 判斷超音波感應器是否回應低電壓，表示感應器接收到傳回的超音波
    private boolean echoLow() {
        // 測試五千次
        for (int i = 0; i < 5000; i++) {
            // 如果是低電壓
            if (pinEcho.isLow()) {
                return true;
            }
        }

        return false;
    }

    // 使用超音波感應器傳回測試距離
    private float detect() {
        // 送出觸發訊號給超音波測距模組
        trigger();

        // 判斷超音波測距模組是否回應高電壓
        if (!echoHigh()) {
            return -1;
        }

        // 記錄時間（奈秒）
        long start = System.nanoTime();

        // 判斷超音波測距模組是否回應低電壓
        if (!echoLow()) {
            return -1;
        }

        // 記錄時間（奈秒）
        long end = System.nanoTime();

        // 計算超音波發射與接收的時間（奈秒）
        long pulse = end - start;

        // 計算距離（公分）
        float result = pulse / 1_000_000_000F;
        result = result * 340 * 100 / 2;

        return result; 
    }

    // 超音波感應器監聽介面
    public interface UltraSensorListener {
        public void onApproaching(int distance);
        public void onLeaving(int distance);
    }

}
