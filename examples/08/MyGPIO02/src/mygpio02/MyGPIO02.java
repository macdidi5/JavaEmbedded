package mygpio02;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.RaspiPin;

public class MyGPIO02 {

    public static void main(String[] args) {
        
        System.out.println("MyGPIO02 start...");
        
        // 建立GPIO控制物件
        final GpioController gpio = GpioFactory.getInstance();
        
        // 建立控制GPIO_01輸入的物件
        final GpioPinDigitalInput pin01 = 
                gpio.provisionDigitalInputPin(RaspiPin.GPIO_01, 
                        PinPullResistance.PULL_DOWN);
        
        int count = 0;
        
        while (true) {
            // 如果GPIO_01接收到3.3V的電壓
            if (pin01.isHigh()) {
                System.out.println("Hello! " + ++count);
            }
            
            delay(500);
        }

    }
    
    private static void delay(int ms) {
        try {
            Thread.sleep(ms);
        }
        catch (InterruptedException e) {
            System.out.println(e.toString());
        }
    }
    
}
