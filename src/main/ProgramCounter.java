package src.main;

import java.util.HashMap;
import java.util.Map;

public class ProgramCounter {
    int[] addresses;
    Map<String, Integer> headerLocations;

    public static Map<String, Integer> offsets = new HashMap<String, Integer>() {{
        put("halt", 0x1);
        put("nop", 0x1);

        put("rrmovq", 0x2);
        put("irmovq", 0x6);
        put("rmmovq", 0x6);
        put("mrmovq", 0x6);

        put("addq", 0x2);
        put("subq", 0x2);
        put("andq", 0x2);
        put("xorq", 0x2);

        put("jmp", 0x5);
        put("jle", 0x5);
        put("jl", 0x5);
        put("je", 0x5);
        put("jne", 0x5);
        put("jge", 0x5);
        put("jg", 0x5);

        put("cmovle", 0x2);
        put("cmovl", 0x2);
        put("cmove", 0x2);
        put("cmovne", 0x2);
        put("cmovge", 0x2);
        put("cmovg", 0x2);

        put("call", 0x5);

        put("ret", 0x1);
        
        put("pushq", 0x2);
        put("popq", 0x2);
    }};

    String[][] tokens;

    public ProgramCounter(String[][] tokens) {
        this.addresses = new int[tokens.length];
        headerLocations = new HashMap<String, Integer>();
        this.tokens = tokens;
    }

    public void increment(int i, int offset) {
        int newAddress = addresses[i-1] + offset;
        for(int j = i; j < addresses.length; j++) {
            addresses[j] = newAddress;
        }
    }
    public void setAddresses(int i, int arg) {
        for(int j = i; j < addresses.length; j++) {
            addresses[j] = arg;
        }
    }

    public void parse() {
        for(int i = 0; i < tokens.length; i++) {
            String instruction = tokens[i][0];
            if(offsets.containsKey(instruction)) {
                increment(i + 1, offsets.get(instruction));
            } else if(instruction.startsWith(".")) {
                switch(instruction) {
                    case ".pos":
                        this.setAddresses(i + 1, Integer.decode(tokens[i][1]));
                        break;
                    case ".align":
                        int remainder = addresses[i] % Integer.parseInt(tokens[i][1]);
                        this.increment(i + 1, Integer.decode(Integer.toHexString(remainder)));
                        break;
                    case ".quad":
                        this.increment(i + 1, 0x8);
                        break;
                    case ".long":
                        this.increment(i + 1, 0x4);
                        break;
                }
            } else if(instruction.trim().endsWith(":")) {
                headerLocations.put(instruction.trim().split(":")[0], Integer.decode("0x" + Integer.toHexString(addresses[i])));
            }
        }
    }

    public int getAddress(int i) {
        return addresses[i + 1];
    }
    public int getAddress(String key) {
        return headerLocations.get(key);
    }
    public boolean containsKey(String key) {
        return headerLocations.containsKey(key);
    }
}
