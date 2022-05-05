package src.main;

public class Utilities {
    public static String toHex(int value) {
        return String.format("%02X ", value);
    }
    public static int merge(int a, int b) {
        return (a << 4) + b;
    }
    
    public static void printHeader(int num) {
        System.out.println("+---------------------------------+");
        System.out.println("Loop " + num + ":");
        System.out.println("+---------------------------------+");
    }
    public static void printFetch(long ifun, long icode, long rA, long rB, long valP, long valC) {
        System.out.println("+---------------------+");
        System.out.println("|Fetch                |");
        System.out.println("+---------------------+");
        System.out.println("| ifun: " + ifun);
        System.out.println("| icode: " + icode);
        System.out.println("| rA: " + rA);
        System.out.println("| rB: " + rB);
        System.out.println("| valP: " + valP);
        System.out.println("| valC: " + valC);
    }
    public static void printDecode(long valA, long valB, long valE) {
        System.out.println("+---------------------+");
        System.out.println("|Decode               |");
        System.out.println("+---------------------+");
        System.out.println("| valA: " + valA);
        System.out.println("| valB: " + valB);
        System.out.println("| valE: " + valE);
    }
    public static void printExecute(long valE, boolean ZF, boolean OF, boolean SF) {
        System.out.println("+---------------------+");
        System.out.println("|Execute              |");
        System.out.println("+---------------------+");
        System.out.println("| valE: " + valE);
        System.out.println("| ZF: " + ZF);
        System.out.println("| OF: " + OF);
        System.out.println("| SF: " + SF);
    }
    public static void printMemory(long valM) {
        System.out.println("+---------------------+");
        System.out.println("|Memory               |");
        System.out.println("+---------------------+");
        System.out.println("| valM: " + valM);
    }
    public static void printWriteback(int[] R) {
        System.out.println("+---------------------+");
        System.out.println("|Writeback            |");
        System.out.println("+---------------------+");
        System.out.println("| %rax: " + R[0]);
        System.out.println("| %rcx: " + R[1]);
        System.out.println("| %rdx: " + R[2]);
        System.out.println("| %rbx: " + R[3]);
        System.out.println("| %rsp: " + R[4]);
        System.out.println("| %rbp: " + R[5]);
        System.out.println("| %rsi: " + R[6]);
        System.out.println("| %rdi: " + R[7]);
    }
    public static void printPC(int PC) {
        System.out.println("+---------------------+");
        System.out.println("|PC Update            |");
        System.out.println("+---------------------+");
        System.out.println("| PC: " + PC);
    }

}
