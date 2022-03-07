package src.main;

import java.nio.file.Path;

public class Assembler {
    String[][] tokens;
    Path pathout;

    public Assembler(String[][] tokens, Path pathout) {
        this.tokens = tokens;
        this.pathout = pathout;
    }

    public void assemble() {
        int currentPos = 0x000;
        
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
