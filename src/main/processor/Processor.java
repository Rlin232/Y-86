package src.main.processor;

public class Processor {
    String file;
    String pathOut;

    int[] R = new int[8];

    int PC = 0; // Keeps track of which byte we're on
    int index = 0; // Keeps track of which 4-bit word we're on
    int rsp = 128; //not sure if this is the right stack pointer

    boolean ZF = false;
    boolean SF = false;
    boolean OF = false;

    public Processor(String file, String pathOut) {
        this.file = file;
        this.pathOut = pathOut;
    }

    public void process() {
        // Read through string until end, running through each step
        while(index < file.length()) {
            long[] values = fetch();
            values = decode(values);
            execute();
            memory();
            writeback();
            pcUpdate((int) values[2]);
        }
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
        int icode = this.readWord();
        int ifun = -1;
        long valC = -1;
        int valP = -1;
        int valA = -1;
        int valB = -1;
        int valE = -1;
        int rA = -1;
        int rB = -1;
        
        // The default operations
        ifun = this.readWord();
        
        switch (icode) {
            case 0: //halt
                break;
            case 1: //nop
                break;
            case 2: //rrmovq
                rA = readWord();
                rB = readWord();
                valP = PC + 2;
                index += 4;
                break;
            case 3: //irmovq
                rA = readWord();
                rB = readWord();
                valC = this.readEight();
                valP = PC + 10;
                break;
            case 4: //rmmovq
                rA = readWord();
                rB = readWord();
                valC = this.readEight();
                valP = PC + 10;
                break;
            case 5: //mrmovq
                rA = readWord();
                rB = readWord();
                valC = this.readEight();
                valP = PC + 10;
                break;
            case 6: //OPq
                rA = readWord();
                rB = readWord();
                valP = PC + 2;
                break;
            case 7: //jXX
                valC = this.readEight();
                valP = PC + 9;
                break;
            case 8: //call
                valC = this.readEight();
                valP = PC + 9;
                break;
            case 9: //ret
                valP = PC + 1;
                break;
            case 10: //pushq
                rA = readWord();
                rB = readWord();
                valP = PC + 2;
                break;
            case 11: //popq
                rA = readWord();
                rB = readWord();
                valP = PC + 2;
                break;
        }
    
        long[] values = {icode, ifun, valC, valP, valA, valB, valE, rA, rB};
        return values;
    }
    public long[] decode(long[] input) {
        // reads up to two operands from the register file 
        long icode = input[0];
        long ifun = input[1];
        long valC = input[2];
        long valP = input[3];
        long valA = input[4];
        long valB = input[5];
        long valE = input[6];
        long rA = input[7];
        long rB = input[8];

        switch ((int) icode) {
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
        long[] values = {icode, ifun, valC, valP, valA, valB, valE, rA, rB};
        return values;
    }

    public long[] execute(long[] input) {
        long icode = input[0];
        long ifun = input[1];
        long valC = input[2];
        long valP = input[3];
        long valA = input[4];
        long valB = input[5];
        long valE = input[6];
        long rA = input[7];
        long rB = input[8];

        switch ((int) icode) {
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
        long[] values = {icode, ifun, valC, valP, valA, valB, valE, rA, rB};
        return values;
    }
    public int memory() {
        // Update memory
        return 0;
    }
    public void writeback(long[] input) {
        // Update registers
        long icode = input[0];
        long ifun = input[1];
        long valC = input[2];
        long valP = input[3];
        long valA = input[4];
        long valB = input[5];
        long valE = input[6];
        long rA = input[7];
        long rB = input[8];
        long valM = input[9];

        switch ((int) icode) {
            case 0: //halt
                break;
            case 1: //nop
                break;
            case 2: //rrmovq
                R[(int) rB] = (int) valE;
                break;
            case 3: //irmovq
                R[(int) rB] = (int) valE;
                break;
            case 4: //rmmovq
                break;
            case 5: //mrmovq
                R[(int) rA] = (int) valM;
                break;
            case 6: //OPq
                R[(int) rB] = (int) valE;
                break;
            case 7: //jXX
                break;
            case 8: //call
                R[4] = (int) valE;
                break;
            case 9: // ret
                R[4] = (int) valE;
                break;
            case 10: //pushq
                R[4] = (int) valE;
                R[(int) rA] = (int) valM;
                break;
            case 11: //popq
                break;
        }
        long[] values = {icode, ifun, valC, valP, valA, valB, valE, rA, rB};
    }
    public void pcUpdate(int valP) {
        PC = valP;
        index = PC*2;
    }
}
