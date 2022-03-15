package src.main;

import java.nio.file.Path;

public class Assembler {
    Memory memory;
    String[][] tokens;
    
    Path pathout;

    public Assembler(String[][] tokens, Path pathout) {
        this.memory = new Memory();
        this.tokens = tokens;

        this.pathout = pathout;
    }

    public void assemble() {
        
        if(this.tokens == null)
            return;
        for(int i = 0; i < tokens.length; i++) {
            // If it's just a method head, no need to do anything
            if(tokens[i][0].endsWith(":\\s+") || tokens[i][0].equals("")) {
                continue;
            }

            switch(tokens[i][0]) {
                // Do something based on each possible argument
            }
        }
    }
}
