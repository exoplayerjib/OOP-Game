package View;

import java.nio.file.Path;

public class GameMain {
    public static void main(String[] args){
        if (args.length == 0){
            System.out.println("Please provide a directory to load the level from!");
            System.exit(1);
        }
        try {

            Path path = Path.of(args[0]);
            GameInitializer game = new GameInitializer(path);
            game.initialize();
            game.run();
        }
        catch (Exception ex){
            System.out.println("Error while loading level: " + ex.getMessage());
            System.exit(1);
        }
    }
}
