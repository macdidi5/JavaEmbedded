package pifan;

import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;

public class MCP3008 {

    private static final boolean DEBUG = true;

    /**
     * MCP3008的八個輸入埠
     */
    public enum MCP3008Channels {

        /**
         * MCP3008的八個輸入埠
         */
        CH_00(0), CH_01(1), CH_02(2), CH_03(3),
        CH_04(4), CH_05(5), CH_06(6), CH_07(7);

        private int channel;

        private MCP3008Channels(int channel) {
            this.channel = channel;
        }

        public int getChannel() {
            return channel;
        }

    }

    // Serial data out
    private GpioPinDigitalInput serialDataOutput = null;

    // Serial data in、Serial clock、Chip select
    private GpioPinDigitalOutput serialDataInput = null;
    private GpioPinDigitalOutput serialClock = null;
    private GpioPinDigitalOutput chipSelect = null;

    public MCP3008(GpioPinDigitalOutput serialClock, 
            GpioPinDigitalInput serialDataOutput,
            GpioPinDigitalOutput serialDataInput,
            GpioPinDigitalOutput chipSelect) {
        this.serialClock = serialClock;
        this.serialDataOutput = serialDataOutput;
        this.serialDataInput = serialDataInput;
        this.chipSelect = chipSelect;
    }

    /**
     * 讀取指定輸入埠的資料
     * 
     * @param channel 輸入埠
     * @return 讀取的資料
     */
    public int read(int channel) {
        chipSelect.high();
        serialClock.low();
        chipSelect.low();

        int adccommand = channel;

        // 0x18 => 00011000
        adccommand |= 0x18; 

        adccommand <<= 3;

        // 傳送讀取的輸入埠給MCP3008
        for (int i = 0; i < 5; i++) { 
            // 0x80 => 0&10000000
            if ((adccommand & 0x80) != 0x0) {
                serialDataInput.high();
            } 
            else {
                serialDataInput.low();
            }

            adccommand <<= 1;

            tickPin(serialClock);
        }

        int adcOut = 0;

        // 讀取指定輸入埠的資料
        for (int i = 0; i < 12; i++) {
            tickPin(serialClock);
            adcOut <<= 1;

            if (serialDataOutput.isHigh()) {
                adcOut |= 0x1;
            }
        }

        chipSelect.high();

        // 移除第一個位元
        adcOut >>= 1; 

        return adcOut;
    }

    /**
     * 讀取指定輸入埠的資料
     * 
     * @param channel 輸入埠
     * @return 讀取的資料（電壓）
     */    
    public float readVoltage(int channel) {
        float result = -1;
        int value = read(channel);

        result = value * 3.3F / 1023;

        return result;
    }

    private void tickPin(GpioPinDigitalOutput pin) {
        pin.high();
        pin.low();
    }

}
