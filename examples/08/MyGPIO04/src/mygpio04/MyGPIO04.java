package mygpio04;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPin;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

public class MyGPIO04 {

    public static void main(String[] args) {

        System.out.println("MyGPIO04 start...");

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

        // 宣告與建立GPIO_01使用的監聽物件
        GpioPinListenerDigital pin01Listener = new GpioPinListenerDigital() {

            private int count = 0;

            @Override
            public void handleGpioPinDigitalStateChangeEvent(
                    GpioPinDigitalStateChangeEvent event) {

                // 如果GPIO_01接收到3.3V的電壓
                if (event.getState().isHigh()) {
                    System.out.println("Hello! " + ++count);
                }

            }

        };

        // 註冊GPIO_01的監聽物件
        pin01.addListener(pin01Listener);


        // 宣告與建立GPIO_02使用的監聽物件
        GpioPinListenerDigital pin02Listener = new GpioPinListenerDigital() {

            @Override
            public void handleGpioPinDigitalStateChangeEvent(
                    GpioPinDigitalStateChangeEvent event) {
                // 讀取發生事件的GPIO針腳與狀態
                GpioPin pin = event.getPin();
                PinState state = event.getState();
                System.out.println("handleGpioPinDigitalStateChangeEvent: ");
                System.out.println("\t" + pin.getName());
                System.out.println("\t" + state.getName());

                // 如果GPIO_02接收到3.3V的電壓
                if (state.isHigh()) {
                    gpio.shutdown();
                    System.out.println("MyGPIO04 bye...");
                    System.exit(0);
                }

            }

        };

        // 註冊GPIO_02的監聽物件
        pin02.addListener(pin02Listener);

        // 記得要加入這個迴圈，不然程式執行以後就結束了
        while (true) {
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