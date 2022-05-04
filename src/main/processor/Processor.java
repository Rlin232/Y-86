package src.main.processor;

public class Processor {
    String file;
    String pathOut;

    public Processor(String file, String pathOut) {
        this.file = file;
        this.pathOut = pathOut;
    }

    public void process() {
        // Read through string until end, running through each step
        
    }
    public int[] fetch() {
        return new int[6];
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
