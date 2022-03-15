package src.main;

public class Utilities {
    public static String toHex(int value) {
        return String.format("%02X ", value);
    }
    public static int merge(int a, int b) {
        return (a << 4) + b;
    }
}
