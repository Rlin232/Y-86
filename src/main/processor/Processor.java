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

    long[] memory = new long[1024];

    public Processor(String file, String pathOut) {
        this.file = file;
        this.pathOut = pathOut;
    }

    public void process() {
        // Read through string until end, running through each step
        while(index < file.length()) {
            long[] values = fetch();
            values = decode(values);
            values = execute(values);
            values = memory(values);
            writeback(values);
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

    // Writes to memory
    public void write(long i, long val) {
        int in = (int)i;
        memory[in] = val;
    }
    
    public long[] fetch() {
        System.out.println("fetch in progress");
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
        System.out.println("icode = "+icode);
        System.out.println("ifun = "+ifun);
        System.out.println("valC = "+valC);
        System.out.println("valP = "+valP);
        System.out.println("valA = "+valA);
        System.out.println("valB = "+valB);
        System.out.println("valE = "+valE);
        System.out.println("rA = "+rA);
        System.out.println("rB = "+rB);
        long[] values = {icode, ifun, valC, valP, valA, valB, valE, rA, rB};
        return values;
    }
    public long[] decode(long[] input) {
        // reads up to two operands from the register file 
        System.out.println("decode in progress");
        
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
        System.out.println("icode = "+icode);
        System.out.println("ifun = "+ifun);
        System.out.println("valC = "+valC);
        System.out.println("valP = "+valP);
        System.out.println("valA = "+valA);
        System.out.println("valB = "+valB);
        System.out.println("valE = "+valE);
        System.out.println("rA = "+rA);
        System.out.println("rB = "+rB);
        long[] values = {icode, ifun, valC, valP, valA, valB, valE, rA, rB};
        return values;
    }

    public long[] execute(long[] input) {
        System.out.println("execute in progress");
        long icode = input[0];
        long ifun = input[1];
        long valC = input[2];
        long valP = input[3];
        long valA = input[4];
        long valB = input[5];
        long valE = input[6];
        long rA = input[7];
        long rB = input[8];

        ZF = false;
        OF = false;
        SF = false;

        switch((int) icode) {
            case 0: //halt
                break;
            case 1: //nop
                break;
            case 2: //rrmovq
                valE = valB + valC;
                break;
            case 3: //irmovq
                valE = 0 + valC;
                break;
            case 4: //rmmovq
                valE = valB + valC;
                break;
            case 5: //mrmovq
                valE = valB + valC;
                break;
            case 6: //OPq // valA <- R[rA]
                long oldVal = valE;
                switch((int) ifun) {
                    case 0:
                        valE = valB + valA;
                        // Check for overflow
                        if(valB > 0 && valA > 0 && valE < 0) OF = true;
                        if(valB < 0 && valA < 0 && valE > 0) OF = true;
                        break;
                    case 1:
                        valE = valB - valA;
                        // Check for overflow
                        if(valB > 0 && valA < 0 && valE < 0) OF = true;
                        if(valB < 0 && valA > 0 && valE > 0) OF = true;
                        break;
                    case 2:
                        valE = valB & valA;
                        break;
                    case 3:
                        valE = valB ^ valA;
                        break;
                }
                if(valE == 0)
                    ZF = true;
                if(oldVal < 0) {
                    if(valE > 0)
                        SF = true;
                } else {
                    if(valE < 0)
                        SF = true;
                }
                
                break;
            case 7: //jxx
                break;
            case 8: //call
                valE = valB - 8;
                break;
            case 9: //return
                valE = valB + 8;
                break;
            case 10: //pushq
                valE = valB - 8;
                break;
            case 11: //popq
                valE = valB + 8;
                break;
        }
        
        System.out.println("icode = "+icode);
        System.out.println("ifun = "+ifun);
        System.out.println("valC = "+valC);
        System.out.println("valP = "+valP);
        System.out.println("valA = "+valA);
        System.out.println("valB = "+valB);
        System.out.println("valE = "+valE);
        System.out.println("rA = "+rA);
        System.out.println("rB = "+rB);
        long[] values = {icode, ifun, valC, valP, valA, valB, valE, rA, rB};
        return values;
    }

    public long[] memory(long[] input) {
        System.out.println("memory in progress");
        // Update memory
        long icode = input[0];
        long ifun = input[1];
        long valC = input[2];
        long valP = input[3];
        long valA = input[4];
        long valB = input[5];
        long valE = input[6];
        long rA = input[7];
        long rB = input[8];
        long valM = -1;

        switch ((int) icode) {
            case 0: //halt
                break;
            case 1: //nop
                break;
            case 2: //rrmovq
                break;
            case 3: //irmovq
                break;
            case 4: //rmmovq
                //M8[valE] <- valA
                this.write(valE, valA);
                break;
            case 5: //mrmovq
                //valM <- M8[valE]
                valM = this.readEight();
                break;
            case 6: //OPq 
                break;
            case 7: //jXX
                break;
            case 8: //call
                this.write(valE, valP);
                break;
            case 9: // ret
                valM = this.readEight();
                break;
            case 10: //pushq
                this.write(valE, valA);
                break;
            case 11: //popq
                valM = this.readEight();
                break;
        }
        System.out.println("icode = "+icode);
        System.out.println("ifun = "+ifun);
        System.out.println("valC = "+valC);
        System.out.println("valP = "+valP);
        System.out.println("valA = "+valA);
        System.out.println("valB = "+valB);
        System.out.println("valE = "+valE);
        System.out.println("rA = "+rA);
        System.out.println("rB = "+rB);
        System.out.println("valM = "+valM);
        long[] values = {ifun, valC, valP, valA, valB, valE, rA, rB, valM};
        return values;
    }
    public void writeback(long[] input) {
        // Update registers
        System.out.println("writeback in progress");
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
        System.out.println("icode = "+icode);
        System.out.println("ifun = "+ifun);
        System.out.println("valC = "+valC);
        System.out.println("valP = "+valP);
        System.out.println("valA = "+valA);
        System.out.println("valB = "+valB);
        System.out.println("valE = "+valE);
        System.out.println("rA = "+rA);
        System.out.println("rB = "+rB);
        System.out.println("valM = "+valM);
    }
    public void pcUpdate(int valP) {
        System.out.println("pcUpdate in progress");
        PC = valP;
        index = PC*2;
        System.out.println("PC = "+PC);
        System.out.println("index = "+index);
    }
        
}
