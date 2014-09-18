package ultrasounddemo02;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

public class UltraSoundDemo02 {

    public static void main(String[] args) {
        final GpioController gpio = GpioFactory.getInstance();

        final GpioPinDigitalOutput trigger = gpio.provisionDigitalOutputPin(
                        RaspiPin.GPIO_04, "Trig", PinState.LOW);

        final GpioPinDigitalInput echo = 
                gpio.provisionDigitalInputPin(RaspiPin.GPIO_05);

        final GpioPinDigitalOutput led = gpio.provisionDigitalOutputPin(
                        RaspiPin.GPIO_06, "LED", PinState.LOW);        

         Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                trigger.setState(false);
                led.setState(false);

                gpio.shutdown();
            }
         });

         while (true) {
             float distance = getDistance(trigger, echo);
             System.out.printf("Distance: %f cm%n", distance);

             if (distance < 5) {
                 led.setState(true);
             }
             else {
                 led.setState(false);
             }

             delay(1000);
         }

    }

    public static void trigger(GpioPinDigitalOutput pin) {
        pin.setState(true);
        delay(0, 10000);
        pin.setState(false);
    }

    public static boolean echoHigh(GpioPinDigitalInput pin) {
        for (int i = 0; i < 5000; i++) {
            if (pin.getState().isHigh()) {
                return true;
            }
        }

        return false;
    }

    public static boolean echoLow(GpioPinDigitalInput pin) {
        for (int i = 0; i < 5000; i++) {
            if (pin.getState().isLow()) {
                return true;
            }
        }

        return false;
    }

    public static float getDistance(GpioPinDigitalOutput pinTrig, 
            GpioPinDigitalInput pinEcho) {
        float result = 0.0F;

        trigger(pinTrig);

        if (!echoHigh(pinEcho)) {
            return -1;
        }

        long start = System.nanoTime();

        if (!echoLow(pinEcho)) {
            return -1;
        }

        long end = System.nanoTime();

        long pulse = end - start;

        result = pulse / 1_000_000_000F;
        result = result * 340 * 100 / 2;

        return result;
    }

    private static void delay(int ms) {
        try {
            Thread.sleep(ms);
        }
        catch (InterruptedException e) {
            System.out.println(e.toString());
        }
    }    

    private static void delay(int ms, int ns) {
        try {
            Thread.sleep(ms, ns);
        }
        catch (InterruptedException e) {
            System.out.println(e.toString());
        }
    }        

}
