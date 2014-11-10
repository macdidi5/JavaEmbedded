import static java.lang.Thread.sleep;

public class TestThread {

    public static void main(String[] args) throws InterruptedException {
        runThread("Simon");
        Thread.sleep(1000 * 3);
        runThread("Mary");
        Thread.sleep(1000 * 3);
        runThread("John");
    }

    public static void runThread(String name) {
        int count = (int)(Math.random() * 80) + 20;

        Thread[] ts = new Thread[count];

        for (int c = 0; c < ts.length; c++) {
            ts[c] = new Thread() {
                public void run() {
                    System.out.println(getName() + ": start...");

                    for (int i = 0; i < 100; i++) {
                        try {
                            sleep((int)(Math.random() * 1000));
                        }
                        catch (InterruptedException e) {

                        }
                    }

                    System.out.println(getName() + ": end...");
                }
            };

            ts[c].setName(name + "(" + c + ")");

            ts[c].start();
        }
    }
}