import java.io.*;
import java.util.*;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Main {
    private static int life=0;
    private static final File scoreFile=new File("high-score.txt");

    public static void main(String[] args) throws FileNotFoundException {
        String filename=args[0];
        File file=new File(filename);

        int highScore = readHighScore();

        int numWords = amountOfNum();

        setLife(numWords);

        Set<String> wordsList = readInputWord(file, numWords);

        String pickedWords = pickRandomWords(wordsList);

//        System.out.println(pickedWords);

        List<String> stringList=initList(numWords);

        gameLogic(stringList,highScore,pickedWords);
    }

    private static int readHighScore() throws FileNotFoundException {
        Scanner readerF= new Scanner(scoreFile);
        int high_score=0;
        while (readerF.hasNextInt()) {
            high_score = readerF.nextInt();
        }
        System.out.println("Your high score: "+high_score);
        return high_score;
    }

    private static int amountOfNum() {
        System.out.print("Input number of word you want: ");
        Scanner neededWords=new Scanner(System.in);
        return neededWords.nextInt();
    }

    private static void setLife(int numWords){
        life=numWords*2;
        System.out.println("Based on your choice, your life is: "+life);
    }

    private static Set<String> readInputWord(File file, int numWords) throws FileNotFoundException {
        Set<String> wordsList=new HashSet<>();
        Scanner reader =new Scanner(file);
        while (reader.hasNextLine()) {
            String words=reader.nextLine().toLowerCase();
            if(words.length()==numWords) {
                wordsList.add(words);
            }
        }
        reader.close();
        return wordsList;
    }

    private static String pickRandomWords(Set<String> wordsList) {
        String pickedWords = null;
        int size=wordsList.size();
        int random=new Random().nextInt(size);
        int a=0;
        for(String words:wordsList){
            if(a==random){
                pickedWords=words;
            }
            a++;
        }
        return pickedWords;
    }

    private static List<String> initList(int num) {
        final List<String> l = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            l.add("");
        }
        return l;
    }

    private static void gameLogic(List<String> stringList,int highScore, String pickedWords) {
        while (true) {
            System.out.print("Guess the word: ");
            Scanner scanner=new Scanner(System.in);
            String guess=scanner.nextLine();
            if(guess.length()!=1){
                System.out.println("please just input 1 word");
            }
            checkWord(stringList,pickedWords,guess);

            boolean fullList = noEmptyList(stringList, highScore, pickedWords);
            boolean gameOver = gameOver(pickedWords);

            if(fullList||gameOver){
                break;
            }
            printStatus(stringList);
        }
    }

    private static boolean noEmptyList(List<String> stringList,int highScore, String pickedWords) {
        if(!stringList.contains("")) {
            writeHighScore(highScore,pickedWords);
            return true;
        }else {
            return false;
        }
    }

    private static boolean gameOver(String pickedWords) {
        if(life==0){
            System.out.println("You lose, the word is: "+pickedWords);
            return true;
        } else {
            return false;
        }
    }

    private static void checkWord(List<String> stringList, String pickedWords, String guess) {
        if(pickedWords.contains(guess)){
            matchAndUpdateStringList(stringList,pickedWords,guess);
        } else {
            life--;
        }
    }

    private static void matchAndUpdateStringList(List<String>stringList, String pickedWords, String guess) {
        if(!stringList.contains(guess)) {
            // the word is not inside the list yet
            List<Integer> collect = Pattern.compile(Pattern.quote(guess))
                    .matcher(pickedWords)
                    .results()
                    .map(MatchResult::start)
                    .collect(Collectors.toList());
            for(Integer c:collect){
                stringList.set(c,guess);
            }
        }else{
            System.out.println("Invalid action, word already inputted");
            life --;
        }
    }

    private static void writeHighScore(int score, String pickedWords) {
        System.out.println("congrats you guessed the word: "+pickedWords);
        try {
            FileWriter fileWriter=new FileWriter("high-score.txt");
            if(life>score){
                fileWriter.write(Integer.toString(life));
                System.out.println("Congrats new high score: "+life);
            }
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void printStatus(List<String> stringList) {
        System.out.println(stringList);
        System.out.println("your life now is: "+life);
        System.out.println("*****************************************************");
    }
}
