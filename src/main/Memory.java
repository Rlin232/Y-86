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
            if(this.bytes.get(j) != null) {
                this.bytes.set(j, 0);
            }
            this.index = i;
        }
    }
    public void write(int entry) {
        this.bytes.set(this.index, entry);
        this.index++;
    }

    public String toString() {
        String output = "";
        for(Integer e : bytes) {
            output += Utilities.toHex(e) + " ";
        }
        return output;
    }
}
