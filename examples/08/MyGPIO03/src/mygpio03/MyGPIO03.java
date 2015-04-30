package mygpio03;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.RaspiPin;

public class MyGPIO03 {

    public static void main(String[] args) {
        
        System.out.println("MyGPIO03 start...");
        
        // 建立GPIO控制物件
        final GpioController gpio = GpioFactory.getInstance();
        
        // 建立控制GPIO_01輸入的物件
        final GpioPinDigitalInput pin01 = 
                gpio.provisionDigitalInputPin(RaspiPin.GPIO_01, 
                        PinPullResistance.PULL_DOWN);
        
        // 建立控制GPIO_02輸入的物件
        final GpioPinDigitalInput pin02 = 
                gpio.provisionDigitalInputPin(RaspiPin.GPIO_02, 
                        PinPullResistance.PULL_DOWN);        
        
        int count = 0;
        
        while (true) {
            // 如果GPIO_02接收到3.3V的電壓
            if (pin02.isHigh()) {
                break;
            }
            
            // 如果GPIO_01接收到3.3V的電壓
            if (pin01.isHigh()) {
                System.out.println("Hello! " + ++count);
            }
            
            delay(500);
        }
        
        System.out.println("MyGPIO03 bye...");
    }
    
    /**
     * 暫停指定的時間（毫秒、1000分之一秒）
     * 
     * @param ms
     */    
    private static void delay(int ms) {
        try {
            Thread.sleep(ms);
        }
        catch (InterruptedException e) {
            System.out.println(e.toString());
        }
    }
    
}
