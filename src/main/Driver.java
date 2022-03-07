package src.main;

import java.io.IOException;
import java.nio.file.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class Driver {
    public static void main(String[] args) {
        Path path = Paths.get("/Users/admin/Desktop/Ryan/OHS/2021-2022/Computer Systems/Y-86/input/in.txt");

        String[][] tokens = tokenize(path);

        for(int j = 0; j < tokens.length; j++) {
            for(int k = 0; k < tokens[j].length; k++) {
                System.out.print(tokens[j][k] + " ");
            }
            System.out.println();
        }
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
        }; 
        streamSupplier.get().forEach(consumer);

        return tokens;
    }
}
