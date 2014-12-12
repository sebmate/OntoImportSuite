
public class Wait {

    public static void oneSec() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }
    }

    public static void manySec(long s) {
        try {
            Thread.sleep(s * 1000);
        } catch (InterruptedException e) {
        }
    }
}
