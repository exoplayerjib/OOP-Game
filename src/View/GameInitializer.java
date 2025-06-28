package View;

import Game.Tiles.Units.Players.Player;
import View.Input.ActionInput;
import View.Input.InputQuery;
import View.Parser.LevelLoader;
import View.Parser.TileFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;

public class GameInitializer {
    private final Path dirPath;
    private LevelLoader levelLoader;
    private final TileFactory tileFactory;
    private final Scanner scanner;
    private final InputQuery inputQuery;

    public GameInitializer(Path dirPath){
        this.dirPath = dirPath;
        tileFactory = new TileFactory();
        scanner = new Scanner(System.in);
        this.inputQuery = new ActionInput(scanner);
    }

    public int choosePlayer(){
        while (true) {
            sendMessage("Choose a player:");
            List<Player> players = tileFactory.getPlayerList();
            int index = 1;
            for (Player p : players) {
                sendMessage(String.format("%d. %s", ++index, p.description()));
            }
            try {
                int selection = Integer.parseInt(scanner.next()) -1;
                if (selection >= 0 && selection < players.size()) {
                    sendMessage("You have chosen " + players.get(selection).getName());
                    return selection;
                }
            }
            catch (NumberFormatException ex){
                sendMessage("Not a number, please try again!");
            }
        }
    }

    public void initialize() throws IOException {
        Player player = tileFactory.getPlayerList().get(choosePlayer());
        levelLoader = new LevelLoader(dirPath, player,tileFactory,this::sendMessage,inputQuery);
        levelLoader.initialize();
    }

    public void run(){
        levelLoader.run();
    }


    public void sendMessage(String message){
        System.out.println(message);
    }
}
