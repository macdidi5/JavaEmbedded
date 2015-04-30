package dmsdemo01;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.RaspiPin;

public class DMSDemo01 {

    public static void main(String[] args) {
        // 建立GPIO控制物件
        final GpioController gpio = GpioFactory.getInstance();

        // 建立控制GPIO_01輸入的物件，紅外線測距模組的輸出針腳
        final GpioPinDigitalInput pin01 = 
                gpio.provisionDigitalInputPin(RaspiPin.GPIO_01, 
                        PinPullResistance.PULL_DOWN);

        // 建立控制GPIO_04，LED
        final GpioPinDigitalOutput pin04 = 
                gpio.provisionDigitalOutputPin(RaspiPin.GPIO_04);

        int counter = 0;

        while (true) {
            // 讀取紅外線測距模組的輸出針腳的狀態
            boolean isHigh = pin01.isHigh();

            // 如果有物體靠近
            if (isHigh) {
                counter++;

                System.out.println("Approaching... " + counter);

                if (counter > 2) {
                   break; 
                }
            }

            // 設定LED針腳的狀態與紅外線測距模組的輸出針腳一樣
            pin04.setState(isHigh);
            delay(500);
        }

        gpio.shutdown();
        System.exit(0);
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
