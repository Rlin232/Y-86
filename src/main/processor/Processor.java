package src.main.processor;

import src.main.Utilities;

public class Processor {
    String file;
    String pathOut;

    int[] R = new int[8];

    int PC = 0; // Keeps track of which byte we're on
    int index = 0; // Keeps track of which 4-bit word we're on

    boolean cnd = false;

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
        int num = 1;
        while(index < file.length()) {
            Utilities.printHeader(num);
            long[] values = fetch();
            int instruction = (int) values[0];
            values = decode(values);
            values = execute(values);
            values = memory(values);
            writeback(values);
            pcUpdate(values);
            System.out.println("Command " + instruction + " finished.");
            num++;
        }
    }
    
    public static String byteReverse(String n) {
        String out = "";
        for(int i = 0; i < n.length(); i+=2) {
            String token = n.substring(i, i+2);
            out = token + out;
        }
        return out;
    }

    // Reads next 4-bit word
    public int readWord() {
        int word = Character.getNumericValue(file.charAt(index));
        this.index++;
        return word;
    }

    // Reads next 8 bytes
    public long readEight() {
        String value = file.substring(index, index+16);
        index += 16;
        return Long.decode("0x" + byteReverse(value));
    }
    
    public long readEight(int otherIndex) {
        String value = file.substring(otherIndex, otherIndex+16);
        return Long.decode("0x" + byteReverse(value));
    }

    // Writes to memory
    public void write(long i, long val) {
        memory[(int) i] = val;
    }
    public long readEightStack(int i) {
        return memory[(int) i];
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
                System.out.println("Done");
                System.exit(0);
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
        Utilities.printFetch(ifun, icode, rA, rB, valP, valC);
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
                valB = R[4];
                break;
            case 9: // ret
                valA = R[4];
                valB = R[4];
                break;
            case 10: //pushq
                valA = rA;
                valB = R[4]; 
                break;
            case 11: //popq
                valA = R[4];
                valB = R[4]; 
                break;
        }
        Utilities.printDecode(valA, valB, valE);
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
                        else OF = false;
                        if(valB < 0 && valA < 0 && valE > 0) OF = true;
                        else OF = false;
                        break;
                    case 1:
                        valE = valB - valA;
                        // Check for overflow
                        if(valB > 0 && valA < 0 && valE < 0) OF = true;
                        else OF = false;
                        if(valB < 0 && valA > 0 && valE > 0) OF = true;
                        else OF = false;
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
                else 
                    ZF = false;
                if(oldVal < 0) {
                    if(valE > 0)
                        SF = true;
                    else
                        SF = false;
                } else {
                    if(valE < 0)
                        SF = true;
                    else
                        SF = false;
                }
                break;
            case 7: //jxx
                cnd = cond((int) ifun);
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
        Utilities.printExecute(valE, ZF, OF, SF);
        long[] values = {icode, ifun, valC, valP, valA, valB, valE, rA, rB};
        return values;
    }

    public long[] memory(long[] input) {
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
                valM = this.readEightStack((int) valA);
                break;
            case 10: //pushq
                this.write(valE, valA);
                break;
            case 11: //popq
                valM = this.readEight();
                break;
        }
        Utilities.printMemory(valM);
        long[] values = {icode, ifun, valC, valP, valA, valB, valE, rA, rB, valM};
        return values;
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
        Utilities.printWriteback(R);
    }
    public void pcUpdate(long[] input) {
        long icode = input[0];
        long valC = input[2];
        long valP = input[3];
        long valM = input[9];

        switch ((int) icode) {
            case 0: //halt
                break;
            case 1: //nop
                PC ++;
                break;
            case 2: //rrmovq
                PC = (int) valP;
                break;
            case 3: //irmovq
                PC = (int) valP;
                //nothing?
                break;
            case 4: //rmmovq
                PC = (int) valP;
                break;
            case 5: //mrmovq
                PC = (int) valP;
                break;
            case 6: //OPq 
                PC = (int) valP;
                break;
            case 7: //jXX
                PC = cnd ? (int) valC : (int) valP;
                break;
            case 8: //call
                PC = (int) valC;
                break;
            case 9: // ret
                PC = (int) valM;
                break;
            case 10: //pushq
                PC = (int) valP;
                break;
            case 11: //popq
                PC = (int) valP;
                break;
        }
        index = PC*2;
        Utilities.printPC(PC);
    }

    public boolean cond(int op){
        switch((int) op) {
            case 0:
                return true;
            case 1: //le
                return (SF && !OF) || ZF;
            case 2: //l
                return (SF && !OF);
            case 3: //e
                return ZF;
            case 4: //ne
                return !ZF;
            case 5: //ge
                return (!SF || OF) || ZF;
            case 6: //g
                return (!SF || OF);
            default:
                return false;
        }
    }
        
}
