package mygpio;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

public class MyGPIO {

    public static void main(String[] args) {
        
        // 建立GPIO控制物件
        final GpioController gpio = GpioFactory.getInstance();
        // 建立控制GPIO_01輸出的物件
        final GpioPinDigitalOutput pin = gpio.provisionDigitalOutputPin(
                        RaspiPin.GPIO_01, "My LED", PinState.LOW);
        
        System.out.println("LED ON...");
        
        // 設定GPIO_01的狀態，設定為true表示這個針腳會輸出3.3V的電壓
        pin.setState(true);
        delay(3000);
        
        System.out.println("LED OFF...");
        
        // 設定GPIO_01的狀態，設定為fasle表示這個針腳不會輸電壓
        pin.setState(false);
        
        System.out.println("Bye...");
        
        // 最後記的要關閉GPIO
        gpio.shutdown();
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
