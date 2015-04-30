package pifan;

import com.pi4j.component.motor.impl.GpioStepperMotorComponent;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

public class PiFan {

    // 28BYJ-48、四相步進馬達
    // 單步、4 steps
    public static final byte[] SINGLE_STEP = { 
        (byte) 0b0001, 
        (byte) 0b0010, 
        (byte) 0b0100, 
        (byte) 0b1000 };

    // 4-Step sequence: 32 * 63.68395 = 2037.8864 (2038)
    public static final int STEPS_PER_REV = 2038;

    // 搖桿訊號與MCP3008對應的通道
    private static final int Y_CHANNEL = 
            MCP3008.MCP3008Channels.CH_00.getChannel();
    private static final int X_CHANNEL = 
            MCP3008.MCP3008Channels.CH_01.getChannel();
    private static final int BUTTON_CHANNEL = 
            MCP3008.MCP3008Channels.CH_02.getChannel();

    private static final int CENTER = 1023 / 2;

    public static void main(String[] args) {

        System.out.println("PiFan Start...");

        // 建立GPIO控制物件
        final GpioController gpio = GpioFactory.getInstance();

        // 建立控制步進馬達的GPIO輸出物件
        final GpioPinDigitalOutput[] pins = {
                gpio.provisionDigitalOutputPin(RaspiPin.GPIO_03, PinState.LOW),
                gpio.provisionDigitalOutputPin(RaspiPin.GPIO_12, PinState.LOW),
                gpio.provisionDigitalOutputPin(RaspiPin.GPIO_13, PinState.LOW),
                gpio.provisionDigitalOutputPin(RaspiPin.GPIO_14, PinState.LOW)};

        // 建立步進馬達物件
        GpioStepperMotorComponent motor = 
                new GpioStepperMotorComponent(pins);

        // 設定每一步的間隔時間
        motor.setStepInterval(0, 500);

        // 設定運作模式
        motor.setStepSequence(SINGLE_STEP);
        motor.setStepsPerRevolution(STEPS_PER_REV);

        // 建立MCP3008需要的GPIO針腳物件
        final GpioPinDigitalInput serialDataOutput = 
                gpio.provisionDigitalInputPin(RaspiPin.GPIO_04);
        final GpioPinDigitalOutput serialDataInput = 
                gpio.provisionDigitalOutputPin(RaspiPin.GPIO_05);
        final GpioPinDigitalOutput serialClock = 
                gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01);
        final GpioPinDigitalOutput chipSelect = 
                gpio.provisionDigitalOutputPin(RaspiPin.GPIO_06);

        // 建立MCP3008物件
        final MCP3008 mcp3008 = new MCP3008(
                serialClock, serialDataOutput, serialDataInput, chipSelect);

        // 建立控制直流馬達用的GPIO輸出針腳物件
        final GpioPinDigitalOutput pin00 = 
                gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00);
        final GpioPinDigitalOutput pin02 = 
                gpio.provisionDigitalOutputPin(RaspiPin.GPIO_02);

        // 建立控制直流馬達的L293D物件
        L293D l293d = new L293D(pin00, pin02);

        while (true) {

            // 讀取搖桿的值
            int buttonValue = mcp3008.read(BUTTON_CHANNEL);
            int xValue = mcp3008.read(X_CHANNEL);
            int yValue = mcp3008.read(Y_CHANNEL);

            // 判斷搖桿前、後控制直流馬達（風扇）運轉的方向
            if (yValue < 100) {
                l293d.leftStop();
                delay(500);
                l293d.leftForward();
            }
            else if (yValue > 900) {
                l293d.leftStop();
                delay(500);
                l293d.leftBackward();
            }

            // 判斷搖桿左、右控制步進馬達運轉的方向
            if (xValue < (CENTER - 250)) {
                motor.reverse();
            }
            else if (xValue > (CENTER + 250)) {
                motor.forward();
            }
            else {
                motor.stop();
            }

            // 判斷搖桿是否按下按鈕
            if (buttonValue > 1000) {
                l293d.leftStop();
                motor.stop();
                break;
            }

            delay(200);
        }

        System.out.println("PiFan Bye...");
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
