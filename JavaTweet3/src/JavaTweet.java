import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import java.util.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JavaTweet {
    public static void main(String[] args) {
        //calls buildTweetList to create String List of valid tweets by @elonmusk and @kanyewest
        List<String> kanyeTweetList = buildTweetList("C:/Users/Erin/IdeaProjects/JavaTweet3" +
                "/files/kanye.json");
        List<String> elonTweetList = buildTweetList("C:/Users/Erin/IdeaProjects/JavaTweet3/" +
                "files/" +
                "/elon.json");

        boolean playAgain;
        int gamesPlayed = 0;
        int gamesWon = 0;

        //calls initializeGame as many times as user elects to keep playing
        System.out.println("Welcome to TweetGuesser. Ready to play?\n");
        do {
            gamesWon += initializeGame(kanyeTweetList, elonTweetList);
            gamesPlayed++;

            // allows user to choose whether or not to play again
            Scanner scnr = new Scanner(System.in); // creates Scanner obj to get user input
            System.out.println("Want to play again? (y/n)");

            String again = scnr.next().toLowerCase();

            // makes sure user response is valid, if not prompts user to try again
            while (!again.equals("y") && !again.equals("n")) {
                System.out.println("Invalid response. Please enter 'y' or 'n'");
                again = scnr.next().toLowerCase();
            }
            playAgain = again.equals("y");
        } while (playAgain);

        // shows user their statistics
        System.out.println("You have played " + gamesPlayed + " game(s).");
        System.out.println("You have won " + gamesWon + " game(s). Well done!");
    }

    /**
     * initializeGame(): decides which user's timeline to select tweet from, passes choice to
     * playGame()
     * @param kanyeList : string List of valid tweets by Kanye
     * @param elonList : string List of valid tweets by Elon
     * @return int 1 if the player won the round, 0 if not
     */
    private static int initializeGame(List<String> kanyeList, List<String> elonList) {
        int won;

        // selects either 0 or 1 randomly
        Random r = new Random();
        int myRandom = r.nextInt(2);

        // if 1 is picked, a Kanye tweet is selected. Otherwise, an Elon tweet is selected.
        if (myRandom > 0) {
            won = playGame(kanyeList, true);
        } else {
            won = playGame(elonList, false);
        }
        return won; // returns value from playGame() indicating whether or not user won the round
    }

    /**
     * playGame(): selects random tweet from provided list, allows user to guess which user it
     * belongs to. Tells user if they are right or wrong.
     * @param tweetList : String list of valid tweets by user
     * @param isKanye : true if the tweet was written by Kanye
     * @return int 1 if user won the round, 0 if not
     */
    private static int playGame(List<String> tweetList, boolean isKanye) {
        // selects a random tweet from provided list
        Random rnd = new Random();
        int rndTweetNum = rnd.nextInt(tweetList.size());
        System.out.println("Your tweet is: \"" + tweetList.get(rndTweetNum) + "\"\n");

        Scanner myScan = new Scanner(System.in); // creates Scanner obj to get user input
        System.out.println("Whose tweet is this? (Kanye/Elon)");

        String userGuess = myScan.next().toLowerCase();

        // makes sure user guess is valid, if not prompts user to try again
        while (!userGuess.equals("elon") && !userGuess.equals("kanye")) {
            System.out.println("Invalid response. Please guess 'Kanye' or 'Elon'.");
            userGuess = myScan.next().toLowerCase();
        }
        System.out.println("You have guessed: " + userGuess.toUpperCase());

        // tells user whether or not they have guessed correctly
        if (userGuess.equals("kanye")) {
            if (isKanye) {
                System.out.println("Congratulations! You're right!");
                return 1; // returns 1 if user is correct
            } else {
                System.out.println("Nope, this one was Elon.");
            }
        } else {
            if (isKanye) {
                System.out.println("Nope, this one was Kanye.");
            } else {
                System.out.println("Congratulations! You're right!");
                return 1; // returns 1 if user is correct
            }
        }
        return 0; // returns 0 if user was incorrect
    }

    /**
     * buildTweetList() : builds String List of tweets by calling parseText() to parse JSON file
     * @param fileName : name of JSON file containing Tweets
     * @return String List of valid tweets from JSON file
     */
    private static List<String> buildTweetList(String fileName) {
        //Creates JSON parser Object
        JSONParser jsonParser = new JSONParser();
        List<String> tweetList = new ArrayList<>();

        //Reads and parses file
        try(FileReader reader = new FileReader(fileName))
        {
            //parsing contents of JSON file
            Object obj = jsonParser.parse(reader);
            JSONArray tweetArrayObj = (JSONArray) obj;

            //if valid tweet is returned by parseText, it is added to tweetList
            for (Object o : tweetArrayObj) {
                String newTweet = parseText((JSONObject) o);
                if (!newTweet.isEmpty()) {
                    tweetList.add(newTweet);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ParseException p) {
            p.printStackTrace();
        } catch (IOException i) {
            i.printStackTrace();
        }
        return tweetList;
    }

    /**
     * parseText() : parses text of Tweets from JSON object, returns text if Tweet does not
     * reference another user or link to a video/picture
     * @param list : Tweet object
     * @return blank String if tweet is invalid, text of tweet otherwise
     */
    private static String parseText(JSONObject list) {
        // parses text out of JSON file
        String text = (String) list.get("text");
        if (!text.contains("https") && !text.contains("@")) {
            return text;
        }
        return "";
    }
}
