package src.main;

import java.io.IOException;
import java.nio.file.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class Driver {
    public static void main(String[] args) {
        Path pathIn = Paths.get("/Users/admin/Desktop/Ryan/OHS/2021-2022/Computer Systems/Y-86/input/in.txt"); // Input file
        Path pathOut = Paths.get(""); // Output file

        String[][] tokens = tokenize(pathIn);

        Assembler assembler = new Assembler(tokens, pathOut);
        assembler.assemble();
        System.out.println(Utilities.toHex(100));
    }

    public static String[][] tokenize(Path path) {
        Supplier<Stream<String>> streamSupplier = () -> {
            try {
                return Files.lines(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        };

        int numLines = (int) streamSupplier.get().count();

        String[][] tokens = new String[numLines][];
        
        AtomicInteger i = new AtomicInteger();
        Consumer<String> consumer = (String s) -> {
            // First deal with comments and trim whitespaces
            s = s.split("#")[0];
            s = s.trim();

            // Tokenize
            tokens[i.getAndIncrement()] = s.split("\\s+"); 
            
            // Question: Will we ever have >2 tokens? wouldn't it just be command and then arg?
        }; 
        streamSupplier.get().forEach(consumer);

        return tokens;
    }
}
