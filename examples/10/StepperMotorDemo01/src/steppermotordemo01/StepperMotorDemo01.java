package steppermotordemo01;

import com.pi4j.component.motor.impl.GpioStepperMotorComponent;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

public class StepperMotorDemo01 {
    // 28BYJ-48、四相步進馬達
    // 單步、4 steps
    public static final byte[] SINGLE_STEP = { 
        (byte) 0b0001, 
        (byte) 0b0010, 
        (byte) 0b0100, 
        (byte) 0b1000 };

    // 雙步、4 steps
    public static final byte[] DOUBLE_STEP = { 
        (byte) 0b0011, 
        (byte) 0b0110, 
        (byte) 0b1100, 
        (byte) 0b1001 };

    // 半步、8 steps
    public static final byte[] HALF_STEP = { 
        (byte) 0b0001, 
        (byte) 0b0011,
        (byte) 0b0010,
        (byte) 0b0110, 
        (byte) 0b0100,
        (byte) 0b1100, 
        (byte) 0b1000,  
        (byte) 0b1001 };

    // 4-Step sequence: 32 * 63.68395 = 2037.8864 (2038)
    public static final int STEPS_PER_REV = 2038;

    public static void main(String[] args) {
        System.out.println("StepperMotorDemo01 Start...");

        // 建立GPIO控制物件
        final GpioController gpio = GpioFactory.getInstance();

        // 建立控制步進馬達的GPIO輸出物件
        final GpioPinDigitalOutput[] pins = {
                gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00, PinState.LOW),
                gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01, PinState.LOW),
                gpio.provisionDigitalOutputPin(RaspiPin.GPIO_02, PinState.LOW),
                gpio.provisionDigitalOutputPin(RaspiPin.GPIO_03, PinState.LOW)};

        // 結束時把輸出針腳設定為低電壓
        gpio.setShutdownOptions(true, PinState.LOW, pins);

        // 建立步進馬達物件
        GpioStepperMotorComponent motor = 
                new GpioStepperMotorComponent(pins);

        // 設定每一步的間隔時間
        motor.setStepInterval(0, 500);

        // 設定運作模式
        motor.setStepSequence(SINGLE_STEP);
        motor.setStepsPerRevolution(STEPS_PER_REV);

        // 正向轉一圈
        motor.rotate(1);
        delay(500);

        // 反向轉一圈
        motor.rotate(-1);
        delay(500);

        gpio.shutdown();
        System.out.println("StepperMotorDemo01 End...");
        System.exit(0);
    }

    private static double angleToRev(int angle) {
        return angle / 360.0;
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
