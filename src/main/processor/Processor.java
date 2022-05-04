package src.main.processor;

public class Processor {
    String file;
    String pathOut;

    int PC = 0; // Keeps track of which byte we're on
    int index = 0; // Keeps track of which 4-bit word we're on

    public Processor(String file, String pathOut) {
        this.file = file;
        this.pathOut = pathOut;
    }

    public void process() {
        // Read through string until end, running through each step

    }
    public int[] fetch() {
        int ifun = -1;
        int valC = -1;
        int valP = -1;
        int valA = -1;
        int valB = -1;
        int valE = -1;
        int rA = -1;
        int rB = -1;
        int instruction = Character.getNumericValue(file.charAt(index));
        switch (instruction) {
            case 0:
                break;
            case 1:
                ifun = Character.getNumericValue(file.charAt(index + 1));
                rA = Character.getNumericValue(file.charAt(index + 2));
                rA = Character.getNumericValue(file.charAt(index + 3));
                valP = PC + 2;
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                break;
            case 6:
                break;
            case 7:
                break;
            case 8:
                break;
            case 9:
                break;
            case 10:
                break;
            case 11:
                break;
        }
        int[] values = {ifun, valC, valP, valA, valB, valE, rA, rB};
        return values;
    }
    public int[] decode() {
        return new int[3];
    }
    public int[] execute() {
        return new int[2];
    }
    public int memory() {
        // Update memory
        return 0;
    }
    public void writeback() {
        // Update registers
    }
    public int pcUpdate() {
        return 0;
    }
}
