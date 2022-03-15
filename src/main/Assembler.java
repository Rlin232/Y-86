package src.main;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class Assembler {
    Memory memory;
    String[][] tokens;
    
    Path pathout;

    public Assembler(String[][] tokens, Path pathout) {
        this.memory = new Memory();
        this.tokens = tokens;

        this.pathout = pathout;
    }

    public void assemble() throws CommandException {
        // Mapping controls
        String commands = "halt nop rrmovq irmovq rmmovq mrmovq addq";
        commands += "subq andq xorq jmp jle jl je jne jge jg rrmovq cmovle cmovl cmove";
        commands += "cmovne cmovge cmovg call ret pushq popq";

        Map<String, Integer> basic = new HashMap<String, Integer>() {{
            put("halt", 0x00);
            put("nop", 0x10);
            put("ret", 0x90);
        }};

        Map<String, Integer> oneArg = new HashMap<String, Integer>() {{
            put("pushq", 0xA0);
            put("popq", 0xB0);
        }};

        Map<String, Integer> simpleTwoArg = new HashMap<String, Integer>() {{
            put("addq", 0x60);
            put("subq", 0x61);
            put("andq", 0x62);
            put("xorq", 0x63);
            put("rrmovq", 0x20);
        }};

        Map<String, Integer> conditionals = new HashMap<String, Integer>() {{
            put("mp", 0);
            put("le", 1);
            put("l", 2);
            put("e", 3);
            put("ne", 4);
            put("ge", 5);
            put("g", 6);
        }};

        Map<String, Integer> registers = new HashMap<String, Integer>() {{
            put("rax", 0);
            put("rcx", 1);
            put("rdx", 2);
            put("rbx", 3);
            put("rsp", 4);
            put("rbp", 5);
            put("rsi", 6);
            put("rdi", 7);
        }};
        
        if(this.tokens == null)
            return;
        for(int i = 0; i < tokens.length; i++) {
            // If it's just a method head, no need to do anything
            if(tokens[i][0].endsWith(":\\s+") || tokens[i][0].equals("")) {
                continue;
            }

            // If it's not a valid command, throw an error
            if(commands.contains(tokens[i][0])) 
                throw new CommandException("Command not found", null);

            // Just write basic commands
            if(basic.get(tokens[i][0]) != null) {
                memory.write(basic.get(tokens[i][0]));
                continue;
            }

            // One-arg commands
            if(tokens[i].length == 2) {
                memory.write(oneArg.get(tokens[i][0]));
                memory.write(Utilities.merge(
                    registers.get(tokens[i][1]), 0xf
                ));
                continue;
            }

            // Two-arg commands
            if(tokens[i].length == 3) {
                if(tokens[i][0].startsWith("cmov")) {
                    String condition = tokens[i][0].substring(4);
                    memory.write(Utilities.merge(
                        0x2, 
                        conditionals.get(condition)
                    ));
                } else {
                    memory.write(simpleTwoArg.get(tokens[i][0]));
                }
                memory.write(Utilities.merge(
                    registers.get(tokens[i][1]),
                    registers.get(tokens[i][2])
                ));
                continue;
            }
        }
    }

}

class CommandException extends Exception {
    public CommandException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}
