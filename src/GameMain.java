import java.io.IOException;
import java.nio.file.*;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Scanner;

public class GameMain {
    public static void main(String[] args) throws IOException {
        Path path = Path.of(args[0]);
        System.out.println("Welcome to our game");

        List<Path> content = Files.list(path).filter(Files::isRegularFile).collect(Collectors.toList());
        Iterator<Path> iter = content.iterator();
        Scanner myscanner = new Scanner(System.in);
        while (myscanner.nextInt() != 0 && iter.hasNext()){
            Path curr = iter.next();
            System.out.println(curr);
            System.out.println(Files.readString(curr));
        }

    }
}
