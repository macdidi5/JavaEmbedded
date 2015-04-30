package dmsdemo02;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.RaspiPin;

public class DMSDemo02 {

    public static void main(String[] args) {
        System.out.println("DMSDemo02 start...");

        // 建立GPIO控制物件
        GpioController gpio = GpioFactory.getInstance();

        // 建立GPIO_04輸入針腳物件
        final GpioPinDigitalInput serialDataOutput = 
                gpio.provisionDigitalInputPin(RaspiPin.GPIO_04);

        // 建立控制GPIO_05、GPIO_01、GPIO_06輸出物件
        final GpioPinDigitalOutput serialDataInput = 
                gpio.provisionDigitalOutputPin(RaspiPin.GPIO_05);
        final GpioPinDigitalOutput serialClock = 
                gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01);
        final GpioPinDigitalOutput chipSelect = 
                gpio.provisionDigitalOutputPin(RaspiPin.GPIO_06);

        // 建立MCP3008物件
        final MCP3008 mcp3008 = new MCP3008(
                serialClock, serialDataOutput, serialDataInput, chipSelect);

        int counter = 0;

        while (true) {
            // 讀取連接到MCP3008 0號通道的紅外線測距模組
            float adcValue = mcp3008.readVoltage(
                    MCP3008.MCP3008Channels.CH_00.getChannel());

            // 轉換為距離（公分）
            double distance = 12.3F * Math.pow(adcValue, -1);

            if (distance < 5) {
                counter++;

                System.out.println("Approaching... " + counter);

                if (counter > 2) {
                   break; 
                }
            }

            System.out.println("Distance: " + 
                    String.format("%2.2f", distance));

            delay(1000);
        }

        gpio.shutdown();
        System.exit(0);
    }

    /**
     * 暫停指定的時間（毫秒、1000分之一秒）
     * 
     * @param ms
     */    
    public static void delay(int ms) {
        try {
            Thread.sleep(ms);
        }
        catch (InterruptedException e) {
            System.out.println("================= " + e);
        }
    }    

}
