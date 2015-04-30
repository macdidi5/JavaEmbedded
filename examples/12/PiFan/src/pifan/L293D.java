package pifan;

import com.pi4j.io.gpio.GpioPinDigitalOutput;

public class L293D {

    private GpioPinDigitalOutput leftPin01, leftPin02;
    private GpioPinDigitalOutput rightPin01, rightPin02;

    public L293D(GpioPinDigitalOutput... pins) {
        if (pins != null && (pins.length == 2 || pins.length == 4)) {
            if (pins.length >= 2) {
                leftPin01 = pins[0];
                leftPin02 = pins[1];
            }

            if (pins.length == 4) {
                rightPin01 = pins[2];
                rightPin02 = pins[3];
            }            
        }
        else {
            throw new IllegalArgumentException();
        }
    }

    public void leftForward() {
        leftPin01.setState(true);
        leftPin02.setState(false);
    }

    public void leftBackward() {
        leftPin01.setState(false);
        leftPin02.setState(true);
    }

    public void leftStop() {
        leftPin01.setState(false);
        leftPin02.setState(false);
    }

    public void rightForward() {
        rightPin01.setState(true);
        rightPin02.setState(false);
    }

    public void rightBackward() {
        rightPin01.setState(false);
        rightPin02.setState(true);
    }

    public void rightStop() {
        rightPin01.setState(false);
        rightPin02.setState(false);
    }

}
