package dcmotordemo02;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.RaspiPin;

public class DCMotorDemo02 {

    public static void main(String[] args) {
        // 建立GPIO控制物件
        final GpioController gpio = GpioFactory.getInstance();

        // 建立控制直流馬達用的GPIO輸出針腳物件
        final GpioPinDigitalOutput pin00 = 
                gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00);
        final GpioPinDigitalOutput pin01 = 
                gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01);

        pin01.low();

        // 順時鐘方向旋轉，由慢變快
        for (int i = 0; i < 100; i++) {
            pin00.high();
            delay(i);
            pin00.low();
            delay(100 - i);
        }

        delay(1000);

        pin00.low();

        // 逆時鐘方向旋轉，由慢變快
        for (int i = 0; i < 100; i++) {
            pin01.high();
            delay(i);
            pin01.low();
            delay(100 - i);
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
