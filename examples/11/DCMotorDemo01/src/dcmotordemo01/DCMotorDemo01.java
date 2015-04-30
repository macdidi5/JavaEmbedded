package dcmotordemo01;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.RaspiPin;

public class DCMotorDemo01 {

    public static void main(String[] args) {
        // 建立GPIO控制物件
        final GpioController gpio = GpioFactory.getInstance();

        // 建立控制直流馬達用的GPIO輸出針腳物件
        final GpioPinDigitalOutput pin00 = 
                gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00);
        final GpioPinDigitalOutput pin01 = 
                gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01);

        // 順時鐘方向旋轉
        pin00.high();
        pin01.low();
        delay(2000);

        // 停止
        pin00.low();
        pin01.low();
        delay(2000);

        // 逆時鐘方向旋轉
        pin00.low();
        pin01.high();
        delay(2000);

        // 停止
        pin00.low();
        pin01.low();

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
