package View.Parser;

import Game.Callbacks.MessageCallback;
import Game.Level;
import Game.Tiles.Units.Players.Player;
import View.Input.InputQuery;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.stream.Collectors;

public class LevelLoader {
    private final Player player;
    private final TileFactory tileFactory;
    private final MessageCallback messageCallback;
    private final InputQuery inputQuery;
    private final List<Path> levelFiles;
    private List<Level> levelList;

    public LevelLoader(Path dirPath,
                       Player player,
                       TileFactory tileFactory,
                       MessageCallback messageCallback,
                       InputQuery inputQuery) throws IOException {

        this.player = player;
        this.tileFactory = tileFactory;
        this.messageCallback = messageCallback;
        this.inputQuery = inputQuery;
        levelFiles = Files.list(dirPath).filter(Files::isRegularFile).filter(e -> e.startsWith("level")).collect(Collectors.toList());
    }

    public void initialize(){
        levelList = levelFiles.stream().map(e -> {
                        try {
                            return new Level(e, player, tileFactory, messageCallback, inputQuery);
                        }
                        catch (IOException ex) {
                            messageCallback.send("Error while loading file: " + e.toString() + "!");
                            System.exit(-1);
                            return null;
                        }
                        }).collect(Collectors.toList());
    }

    public void run(){
        for (Level level : levelList) {
            level.run();
        }
        messageCallback.send("Congrats, all levels have been cleared!");
    }
}
