package parkingdemo;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.RaspiPin;

public class ParkingDemo {
    
    // 是否已經進入
    private static boolean isEnter = false;
    
    // 超音波感應器偵測的距離
    private static final int RANGE = 15;
    
    // 伺服馬達使用的針腳
    private static final String SERVO_PIN = "P1-12";
    
    private static ServoBlaster servoBlaster;

    public static void main(String[] args) {
        
        // 建立GPIO控制物件
        final GpioController gpio = GpioFactory.getInstance();

        // 建立GPIO_07輸出針腳物件，控制入口LED
        final GpioPinDigitalOutput pinLedOne = 
                gpio.provisionDigitalOutputPin(RaspiPin.GPIO_07);
        
        // 建立GPIO_00輸出針腳物件，控制出口LED
        final GpioPinDigitalOutput pinLedTwo = 
                gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00);
        
        // 建立與啟動LED閃爍物件
        final LedBlinker blinkOne = new LedBlinker(pinLedOne);
        final LedBlinker blinkTwo = new LedBlinker(pinLedTwo);
        blinkOne.start();
        blinkTwo.start();
        
        //  建立超音波感應器監聽物件、One
        UltraSoundSensor.UltraSensorListener listenerOne = 
                new UltraSoundSensor.UltraSensorListener() {
            @Override
            public void onApproaching(int distance) {
                System.out.println("USS One Approach.");
                
                // 如果還沒有進入
                if (!isEnter) {
                    isEnter = true;
                    blinkOne.startBlink();
                    openGate();
                    blinkOne.stopBlink();
                }
            }

            @Override
            public void onLeaving(int distance) {
                System.out.println("USS One Leave.");
            }
        };
        
        //  建立超音波感應器監聽物件、Two
        UltraSoundSensor.UltraSensorListener listenerTwo = 
                new UltraSoundSensor.UltraSensorListener() {
            @Override
            public void onApproaching(int distance) {
                System.out.println("USS Two Approach.");
                
                // 如果已經進入
                if (isEnter) {
                    isEnter = false;
                    blinkTwo.startBlink();
                    closeGate();
                    blinkTwo.stopBlink();
                }
            }

            @Override
            public void onLeaving(int distance) {
                System.out.println("USS Two Leave.");
            }
        };
        
        // 建立GPIO_02輸出針腳物件，控制入口超音波感應器偵測的Trig針腳
        final GpioPinDigitalOutput pinTrigOne = 
                gpio.provisionDigitalOutputPin(RaspiPin.GPIO_02);
        // 建立GPIO_03輸入針腳物件，讀取入口超音波感應器偵測的Echo針腳
        final GpioPinDigitalInput pinEchoOne = 
                gpio.provisionDigitalInputPin(RaspiPin.GPIO_03);
        
        // 建立入口超音波感應器物件
        UltraSoundSensor ussOne = 
                new UltraSoundSensor(pinTrigOne, pinEchoOne);
        
        // 建立GPIO_04輸出針腳物件，控制出口超音波感應器偵測的Trig針腳
        final GpioPinDigitalOutput pinTrigTwo = 
                gpio.provisionDigitalOutputPin(RaspiPin.GPIO_04);
        // 建立GPIO_05輸入針腳物件，讀取出口超音波感應器偵測的Echo針腳
        final GpioPinDigitalInput pinEchoTwo = 
                gpio.provisionDigitalInputPin(RaspiPin.GPIO_05);
        
        // 建立出口超音波感應器物件
        UltraSoundSensor ussTwo = new UltraSoundSensor(pinTrigTwo, pinEchoTwo);
        
        // 註冊超音波感應器堅聽事件
        ussOne.setListener(listenerOne, 10);
        ussTwo.setListener(listenerTwo, 10);
        
        // 建立伺服馬達控制物件，針腳與起始值要自己調整
        // 第二個餐數，62：MG995、MG996。70：MG90S、SG90
        servoBlaster = new ServoBlaster(SERVO_PIN, 62, 250);
        
        // 結束應用程式
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                // 關閉LED閃爍物件
                blinkOne.exit();
                blinkTwo.exit();
                // 停止超音波感應器物件
                ussOne.removeListener();
                ussTwo.removeListener();
                System.out.println("Bye...");
            }
        });
        
        System.out.println("Welcome to Ultra Sound Parking Demo!");
        
        servoBlaster.setAngle(0);
        
        while (true) {
            UltraSoundSensor.delay(500);
        }
        
    }
    
    // 開啟閘門
    private static void openGate() {
        System.out.println("Open gate...");

        for (int i = 0; i <= 90; i++) {
            servoBlaster.setAngle(i);
            UltraSoundSensor.delay(20);
        }

        System.out.println("Done.");
    }

    // 關閉閘門
    private static void closeGate() {
        System.out.println("Close gate...");

        for (int i = 90; i >= 0; i--) {
            servoBlaster.setAngle(i);
            UltraSoundSensor.delay(20);
        }

        System.out.println("Done.");        
    }
    
}
