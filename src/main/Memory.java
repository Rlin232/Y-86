package src.main;

import java.util.*;

public class Memory {
    public int index = 0;
    public List<Integer> bytes;

    public Memory() {
        this.bytes = new ArrayList<Integer>();
    }

    public void seek(int i) {
        for(int j = this.index; j < i; j++) {
            if(this.bytes.size() <= j) {
                this.bytes.add(j, 0);
            }
        }
        this.index = i;
    }
    public void write(int entry) {
        this.bytes.add(this.index, entry);

        this.index++;
    }

    public void writeLong(long entry) {
        long temp = entry;

        ArrayList<Integer> digits = new ArrayList<Integer>();

        while(temp > 0){
            digits.add(0, (int) temp % 256);
            temp /= 256;
        }
        write(0);
        for(Integer digit : digits) {
            write(digit);
        }
    }

    public String toString() {
        String output = "";
        for(Integer e : bytes) {
            output += Utilities.toHex(e);
        }
        return output;
    }
}
