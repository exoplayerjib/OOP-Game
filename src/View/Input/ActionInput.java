package View.Input;

import Game.Tiles.Units.Players.Player;
import Game.Tiles.Units.Players.Player.Actions;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ActionInput implements InputQuery{
    private final Map<Character,Actions> validInput = new HashMap<>(){{
        put('w',Actions.UP);
        put('a',Actions.LEFT);
        put('s',Actions.DOWN);
        put('d',Actions.RIGHT);
        put('e',Actions.CAST);
        put('q',Actions.STAY);
    }};

    private final Scanner scanner;

    public ActionInput(Scanner scanner){
        this.scanner = scanner;
    }

    public Actions getInput(){
        while (true){
            char input = scanner.next().charAt(0);
            input = Character.toLowerCase(input);
            if (validInput.containsKey(input)){
                return validInput.get(input);
            }
            else {
                System.out.println("Invalid input, please try again!");
            }
        }
    }
}