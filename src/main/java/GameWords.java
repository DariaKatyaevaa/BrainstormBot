import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GameWords {
    String path;
    String[] wordList;
    String word;
    String mixedWord;
    String rowForCommit;

    public GameWords() throws FileNotFoundException {
        path = "C:\\Users\\daria\\IdeaProjects\\GameBot\\content\\words";
        wordList = new Scanner(new File(path)).useDelimiter("\\Z").next().split("\n");
        word = getRandomWord(wordList);
        mixedWord = shuffle(word);
    }


    private String shuffle(String word) {
        List<Character> characters = new ArrayList<Character>();
        for (char c : word.toCharArray()) {
            characters.add(c);
        }
        StringBuilder mixedWord = new StringBuilder(word.length());
        while (characters.size() != 0) {
            int randPicker = (int) (Math.random() * characters.size());
            mixedWord.append(characters.remove(randPicker));
        }
        return mixedWord.toString();
    }

    private String getRandomWord(String[] words) {
        int randPicker = (int) (Math.random() * words.length);
        return words[randPicker];
    }
}
