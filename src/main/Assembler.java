package src.main;

import java.io.*;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class Assembler {
    Memory memory;
    ProgramCounter programCounter;
    String[][] tokens;
    
    Path pathout;
    int alignment = 8;

    public Assembler(String[][] tokens, Path pathout) {
        this.memory = new Memory();
        this.programCounter = new ProgramCounter(tokens);
        this.tokens = tokens;

        this.pathout = pathout;

        this.programCounter.parse();
    }

    public void assemble() throws CommandException, IOException {
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
            put("%rax", 0);
            put("%rcx", 1);
            put("%rdx", 2);
            put("%rbx", 3);
            put("%rsp", 4);
            put("%rbp", 5);
            put("%rsi", 6);
            put("%rdi", 7);
        }};
        
        if(this.tokens == null)
            return;
        for(int i = 0; i < tokens.length; i++) {
            int startIndex = this.memory.index;
            //Separating the arguments (if any)
            String[] arguments = {};
            if(tokens[i].length > 1) {
                arguments = tokens[i][1].split(",");
                for(int j = 0; j < arguments.length; j++) {
                    String arg = arguments[j].trim();
                    if(arg.charAt(0) == '%') {
                        registers.get(tokens[i][1]);
                    }
                    else if(arg.charAt(0) == '$') {
                        
                    }
                    else if(programCounter.getAddress(arg)) {
                       
                    }
                    else {
                        throw new CommandException("Argument has no address", null);
                    }
                }

            }
            //make argument an integer array then extract just the address - store in a local variable
            //1. try to get it from the register - check if start with %
            //2. try to get it from PC
            //3. probably a constant, if not with $
            //not valid

            
            // If it's just a method head, no need to do anything
            if(tokens[i][0].trim().endsWith(":") || tokens[i][0].equals("")) {
                continue;
            }

            // If it's not a valid command, throw an error
            if(!commands.contains(tokens[i][0]) && !tokens[i][0].startsWith(".")) 
                throw new CommandException("Command not found", null);

            // Just write basic commands
            if(basic.get(tokens[i][0]) != null) {
                memory.write(basic.get(tokens[i][0]));
            }

            // One-arg commands
            if(oneArg.get(tokens[i][0]) != null) {
                memory.write(oneArg.get(tokens[i][0]));
                memory.write(Utilities.merge(
                    registers.get(tokens[i][1]), 0xf
                ));
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
                    registers.get(arguments[0].trim()),
                    registers.get(arguments[1].trim())
                ));
            }

            // Jumps
            if(tokens[i][0].startsWith("j")) {
                String condition = tokens[i][0].substring(1);
                this.memory.write(Utilities.merge(
                    0x7, conditionals.get(condition)
                ));
                memory.write(programCounter.getAddress(tokens[i][1]));
            }

            // Moves
            if(tokens[i][0].equals("irmovq")) {
                // Still have to write down the stack stuff, but for now just writing down register and corr instruction mapping
                memory.write(0x30);
                memory.write(Utilities.merge(0xf, registers.get(arguments[1].trim())));
            }
            //register --> memory
            if(tokens[i][0].equals("rmmovq")) {
                memory.write(0x40);
                memory.write(Utilities.merge(registers.get(arguments[0]), registers.get(arguments[1])));
            }
            //memory --> register
            if(tokens[i][0].equals("mrmovq")) {
                memory.write(0x50);
                memory.write(Utilities.merge(registers.get(arguments[1]), registers.get(arguments[0])));
            }

            // Calls
            if(tokens[i][0].equals("call")) {
                // No clue atm
                memory.write(0x80);
                memory.write(programCounter.getAddress(tokens[i][1]));
            }

            // Directives
            if(tokens[i][0].startsWith(".")) {
                switch(tokens[i][0]) {
                    case ".pos":
                        this.memory.seek(Integer.decode(tokens[i][1]));
                        break;
                    case ".align":
                        this.alignment = Integer.decode(tokens[i][1]);
                        break;
                    case ".long":
                    case ".quad":
                        int n = Integer.parseInt(tokens[i][1]);
                        this.memory.write(n); // Might need to be reformatted to a long idk
                        this.memory.seek(this.memory.index + (this.alignment - 8));
                        break;
                }
                continue;
            }
        
            // Padding
            if(!tokens[i][0].startsWith("."))
                this.memory.seek(startIndex + ProgramCounter.offsets.get(tokens[i][0]));
        }
        this.writeOutput();
    }

    public void writeOutput() throws IOException {
        Writer output = new BufferedWriter(new FileWriter(this.pathout.toFile()));
        output.write(this.memory.toString());
        output.close();
    }

}

class CommandException extends Exception {
    public CommandException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}
