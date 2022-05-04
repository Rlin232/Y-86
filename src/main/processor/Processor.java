package src.main.processor;

public class Processor {
    String file;
    String pathOut;

    int PC = 0; // Keeps track of which byte we're on
    int index = 0; // Keeps track of which 4-bit word we're on
    int rsp = 128; //not sure if this is the right stack pointer

    public Processor(String file, String pathOut) {
        this.file = file;
        this.pathOut = pathOut;
    }

    public void process() {
        // Read through string until end, running through each step

    }

    // Reads next 4-bit word
    public int readWord() {
        int word = Character.getNumericValue(file.charAt(index));
        this.index++;
        return word;
    }

    // Reads next 8 bytes
    public long readEight() {
        String value = file.substring(index, index+8);
        index += 8;
        return Long.parseLong(value);
    }
    
    public long[] fetch() {
        int ifun = -1;
        long valC = -1;
        int valP = -1;
        int valA = -1;
        int valB = -1;
        int valE = -1;
        int rA = -1;
        int rB = -1;
        int instruction = this.readWord();
        
        // The default operations
        ifun = this.readWord();
        
        switch (instruction) {
            case 0:
                break;
            case 1:
                break;
            case 2:
                rA = readWord();
                rB = readWord();
                valP = PC + 2;
                index += 4;
                break;
            case 3:
                rA = readWord();
                rB = readWord();
                valC = this.readEight();
                valP = PC + 10;
                break;
            case 4:
                rA = readWord();
                rB = readWord();
                valC = this.readEight();
                valP = PC + 10;
                break;
            case 5:
                rA = readWord();
                rB = readWord();
                valC = this.readEight();
                valP = PC + 10;
                break;
            case 6:
                rA = readWord();
                rB = readWord();
                valP = PC + 2;
                break;
            case 7:
                valC = this.readEight();
                valP = PC + 9;
                break;
            case 8:
                valC = this.readEight();
                valP = PC + 9;
                break;
            case 9:
                valP = PC + 1;
                break;
            case 10:
                rA = readWord();
                rB = readWord();
                valP = PC + 2;
                break;
            case 11:
                rA = readWord();
                rB = readWord();
                valP = PC + 2;
                break;
        }
    
        long[] values = {ifun, valC, valP, valA, valB, valE, rA, rB};
        return values;
    }
    public long[] decode(long[] input) {
        // reads up to two operands from the register file 
        long ifun = input[0];
        long valC = input[1];
        long valP = input[2];
        long valA = input[3];
        long valB = input[4];
        long valE = input[5];
        long rA = input[6];
        long rB = input[7];

        switch ((int) ifun) {
            case 0: //halt
                break;
            case 1: //nop
                break;
            case 2: //rrmovq
                valA = rA;
                break;
            case 3: //irmovq
                //nothing?
                break;
            case 4: //rmmovq
                valA = rA; 
                valB = rB;
                break;
            case 5: //mrmovq
                valB = rB;
                break;
            case 6: //OPq // valA <- R[rA]
                valA = rA; 
                valB = rB; 
                break;
            case 7: //jXX
                break;
            case 8: //call
                valB = rsp;
                break;
            case 9: // ret
                valA = rsp;
                valB = rsp;
                break;
            case 10: //pushq
                valA = rA;
                valB = rsp; 
                break;
            case 11: //popq
                valA = rsp;
                valB = rsp; 
                break;
        }
        long[] values = {ifun, valC, valP, valA, valB, valE, rA, rB};
        return values;
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
    public void pcUpdate(int valP) {
        PC = valP;
        index = PC*2;
    }
}
