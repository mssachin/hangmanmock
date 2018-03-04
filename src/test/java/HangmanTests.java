import com.essenceglobal.hangman.Hangman;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;


import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.Matchers.anyString;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Hangman.class)
public class HangmanTests {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Mock
    private  Hangman hangmanMock;

    @Before
    public void setUp() throws Exception {
        hangmanMock = PowerMockito.mock(Hangman.class);

    }

    /**
     * This is a test which checks based on user input of characters if there is a match or not.
     * In both the cases it produces appropriate messages.
     *
     * @throws Exception
     */
    @Test
    public void testPlayGameUserExhaustsAllCharacterGuessAndBothGuessesAndDoesNotGuessTheWord() throws Exception {
        PowerMockito.doAnswer(new Answer<String>() {
            public String answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                String[] userInput = (String[]) args[0];
                String randomWord = (String) args[1];
                int countOfCharMatch =0;
                for (String userInputChar:userInput) {
                    String input = userInputChar;
                    if(randomWord.contains(input)){
                        countOfCharMatch++;
                    }
                }
                if((countOfCharMatch == randomWord.length())){
                    return "Congratulations you found the word";
                }else{
                    return "Game over there is no match";
                }

            }
        }).when(hangmanMock).playGame(Matchers.any(), anyString());

       String[] userInput = {"a", "b", "c", "d", "e", "f", "g", "h", "i"} ;
       String randomWord = "random";
       String[] userInput1 = {"a", "b", "c", "e", "c", "j", "s", "g", "s"} ;
       String randomWord1 = "access";

        Assert.assertEquals("Game over there is no match", hangmanMock.playGame(userInput, randomWord));
        Assert.assertEquals("Congratulations you found the word", hangmanMock.playGame(userInput1, randomWord1));

    }

    /**
     * Simple test to mock user cannot enter more than 9 guesses
     * @throws Exception
     */
    @Test
    public void testPlayGameUserCannotEnterMoreThan9Guesses() throws Exception {
        PowerMockito.doAnswer(new Answer<String>() {
            public String answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                String[] userInput = (String[]) args[0];
                String randomWord = (String) args[1];
                if(userInput.length>9){
                    return "Cannot Enter More Than 9 Guesses";
                }

                return "No Joy";
            }
        }).when(hangmanMock).playGame(Matchers.any(), anyString());

        String[] userInput = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j"} ;
        String[] userInput1 = {"a", "b", "c", "d", "e", "f", "g", "h", "i"} ;
        Assert.assertEquals("Cannot Enter More Than 9 Guesses", hangmanMock.playGame(userInput, ""));
        Assert.assertEquals("No Joy", hangmanMock.playGame(userInput1, ""));
    }

    /**
     * If a user enters a word while playing the game the system exits and game is over test.
     * @throws Exception
     */
    @Test
    public void testPlayGameUserGuessesTheWordAfterASeriesOfCharGuesses() throws Exception {
        PowerMockito.doAnswer(new Answer<String>() {
            public String answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                String[] userInput = (String[]) args[0];
                String randomWord = (String) args[1];
                for (String userInputChar:userInput) {
                    String input = userInputChar;
                    if(randomWord.equalsIgnoreCase(input)){
                        return "Congratulations you found the word";
                    }
                }

                return "No Joy";

            }
        }).when(hangmanMock).playGame(Matchers.any(), anyString());

        String[] userInput = {"a", "b", "c", "d", "e", "f", "day", "h", "i", "j"} ;
        String randomWord = "day";
        Assert.assertEquals("Congratulations you found the word", hangmanMock.playGame(userInput, randomWord));



    }

    /**
     * This test mocks the generation of a random word but returning an single word
     */
    @Test
    public void testGenerationOfRandomWord(){
        PowerMockito.when(hangmanMock.selectARandomWord(anyString())).thenReturn("access");
        Assert.assertEquals("access", hangmanMock.selectARandomWord("access"));

    }

    /**
     * This test validates that when a user selects a character and if its there in the random word
     * then the system prints the missing blanks in the word.
     * e.g
     * Word: access
     * Char: c
     * output:  - c c - - -
     */
    @Test
    public void testPrintCorrectlyGuessedCharsToConSole(){
        PowerMockito.doAnswer(new Answer<String>() {
            public String answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                char[] inputArr = (char[]) args[0];
                String guessedCar = (String) args[1];
                String[] guessedChars = new String[inputArr.length];
                Arrays.fill(guessedChars, " - ");
                for (int i = 0; i < inputArr.length; i++) {
                    char charInRandomWord = inputArr[i];
                    if (String.valueOf(charInRandomWord).equals(guessedCar)) {
                        guessedChars[i] = String.valueOf(charInRandomWord);
                    }

                }
                System.out.println();
                StringBuilder stringBuilder = new StringBuilder();
                for (String character : guessedChars) {
                    stringBuilder.append(character);
                }

                return stringBuilder.toString();
            }

        }).when(hangmanMock).printCorrectlyOrIncorrectlyGuessedCharactersToConsole(anyString().toCharArray(), anyString());
        String word = "access";
        Assert.assertEquals(" - cc -  -  - ", hangmanMock.printCorrectlyOrIncorrectlyGuessedCharactersToConsole(word.toCharArray(), "c"));
        Assert.assertEquals(" -  -  -  -  -  - ", hangmanMock.printCorrectlyOrIncorrectlyGuessedCharactersToConsole(word.toCharArray(), "z"));

    }

    /**
     * This test validates that a random word is displayed as blanks to the user
     * e.g day = - - -
     */
    @Test
    public void testDisplayCharsInTheWordToTheUser(){
        PowerMockito.doAnswer(new Answer<String>() {
            public String answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                String inputWord = (String) args[0];
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < inputWord.length(); i++) {
                    stringBuilder.append(" - ");
                }
               return stringBuilder.toString();
            }

        }).when(hangmanMock).displayCharsInTheWordToTheUser(anyString());
        String word = "popular";

        Assert.assertEquals(" -  -  -  -  -  -  - ", hangmanMock.displayCharsInTheWordToTheUser(word));
    }

    /**
     * This test validates the scenario where at any given time the game should check based on the chars guessed if the
     * word is guessed correctly so far or not. In order to achieve this this methods returns the current word.
     * e.g.
     * Word: day
     * guessed char: y
     * output: --y
     */
    @Test
    public void testReturnWordFromGuessedChars(){
        PowerMockito.doAnswer(new Answer<String>() {
            public String answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                String[] guessedWordArr = (String[]) args[0];
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < guessedWordArr.length; i++) {
                    stringBuilder.append(guessedWordArr[i]);
                }
                return stringBuilder.toString();
            }

        }).when(hangmanMock).returnWordFromGuessedChars(Matchers.any());
        String [] guessedCharsArr = {"a", "-", "-", "e", "-", "-"};

        Assert.assertEquals("a--e--", hangmanMock.returnWordFromGuessedChars(guessedCharsArr));
    }

    /**
     * This test validates incorrect input from user throws IllegalArgumentException and its message.
     */
    @Test
    public void testFlagIncorrectInput(){
       PowerMockito.doThrow(new IllegalArgumentException("Only Uppercase and Lowercase alphabets are allowed")).when(hangmanMock).flagIncorrectInput(anyString());
       thrown.expect(IllegalArgumentException.class);
       thrown.expectMessage("Only Uppercase and Lowercase alphabets are allowed");
       hangmanMock.flagIncorrectInput("*");
       hangmanMock.flagIncorrectInput("aa");
       hangmanMock.flagIncorrectInput(":@");
       hangmanMock.flagIncorrectInput(null);
       hangmanMock.flagIncorrectInput(" ");
    }

    /**
     * When the user enters a word in the game it needs to be validated against a random word to be found.
     * This method checks if the entered word matches with that of the random word.
     * returns appropriate messages in each scenario.
     */

    @Test
    public void testPrintCorrectlyOrIncorrectlyGuessedWordToConsole(){

        PowerMockito.doAnswer(new Answer<String>() {
            public String answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                String guessedWord = (String) args[0];
                String actualWord = (String) args[1];
                if(guessedWord.equalsIgnoreCase(actualWord)){
                   return "You have guessed the word correctly";
                }else{
                   return "You have guessed the word incorrectly";
                }

            }

        }).when(hangmanMock).printCorrectlyOrIncorrectlyGuessedWordToConsole(anyString(), anyString());

        Assert.assertEquals("You have guessed the word correctly", hangmanMock.printCorrectlyOrIncorrectlyGuessedWordToConsole("poplar", "poplar"));
        Assert.assertEquals("You have guessed the word incorrectly", hangmanMock.printCorrectlyOrIncorrectlyGuessedWordToConsole("poplar", "popular"));

    }

    /**
     * In the event that a user enters an already guessed character or word this method is to check
     * that user is prompted with appropriate message.
     */

    @Test
    public void testFlagIfPlayerAlreadyGuessedCharacterOrWord(){
        PowerMockito.doAnswer(new Answer<String>() {
            public String answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                Set<String> guessedWordsOrCharsSet = (Set<String>) args[0];
                String currentGuessedWordOrChar = (String) args[1];
                if(guessedWordsOrCharsSet.contains(currentGuessedWordOrChar.toLowerCase())){
                    return "You have guessed the word or character already";
                }else{
                    return "New word or char";
                }

            }

        }).when(hangmanMock).flagIfPlayerAlreadyGuessedCharacterOrWord(Matchers.anySet(), anyString());

        Set<String> testSet = new HashSet<>();
        testSet.add("s");
        testSet.add("popular");
        Assert.assertEquals("You have guessed the word or character already", hangmanMock.flagIfPlayerAlreadyGuessedCharacterOrWord(testSet, "S"));
        Assert.assertEquals("You have guessed the word or character already", hangmanMock.flagIfPlayerAlreadyGuessedCharacterOrWord(testSet, "popular"));
        Assert.assertEquals("New word or char", hangmanMock.flagIfPlayerAlreadyGuessedCharacterOrWord(testSet, "poplar"));
        Assert.assertEquals("New word or char", hangmanMock.flagIfPlayerAlreadyGuessedCharacterOrWord(testSet, "z"));
    }

    /**
     * This method checks the increment of number of attempts with every guess of a word or char.
     * This method also incorporates the scenario where attempts are not increased in the event the user enters an already
     * guessed char or word.
     */

    @Test
    public void testIncreaseTheNumberOfAttemptsWithEveryGuessOfCharOrWord(){

        PowerMockito.doAnswer(new Answer<Integer>() {
            public Integer answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                Set<String> guessedWordsOrCharsSet = (Set<String>) args[0];
                String currentGuessedWordOrChar = (String) args[1];
                Integer numberOfAttempts =0;
                if(!guessedWordsOrCharsSet.contains(currentGuessedWordOrChar.toLowerCase())){
                 return   ++numberOfAttempts;
                }
                return numberOfAttempts;
            }

        }).when(hangmanMock).increaseTheNumberOfAttemptsWithEveryGuessOfCharOrWord(Matchers.anySet(), anyString());
        Set<String> testSet = new HashSet<>();
        testSet.add("s");
        testSet.add("popular");
        Assert.assertEquals(0, hangmanMock.increaseTheNumberOfAttemptsWithEveryGuessOfCharOrWord(testSet, "S"));
        Assert.assertEquals(1, hangmanMock.increaseTheNumberOfAttemptsWithEveryGuessOfCharOrWord(testSet, "x"));
        Assert.assertEquals(0, hangmanMock.increaseTheNumberOfAttemptsWithEveryGuessOfCharOrWord(testSet, "popular"));
        Assert.assertEquals(1, hangmanMock.increaseTheNumberOfAttemptsWithEveryGuessOfCharOrWord(testSet, "open"));

    }

    /**
     * This method checks the decrement of number of attempts available with every guess of a word or char.
     * This method also incorporates the scenario where attempts are not decremented in the event the user enters an already
     * guessed char or word.
     */
    @Test
    public void testDecreaseTheNumberOfAttemptsAvailableWithEveryGuessOfCharOrWord(){

        PowerMockito.doAnswer(new Answer<Integer>() {
            public Integer answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                Set<String> guessedWordsOrCharsSet = (Set<String>) args[0];
                String currentGuessedWordOrChar = (String) args[1];
                Integer numberOfAttemptsLeft = 9;
                if(!guessedWordsOrCharsSet.contains(currentGuessedWordOrChar.toLowerCase())){
                    return   --numberOfAttemptsLeft;
                }
                return numberOfAttemptsLeft;
            }

        }).when(hangmanMock).decreaseTheNumberOfAttemptsAvailableWithEveryGuessOfCharOrWord(Matchers.anySet(), anyString());
        Set<String> testSet = new HashSet<>();
        testSet.add("s");
        testSet.add("popular");
        Assert.assertEquals(9, hangmanMock.decreaseTheNumberOfAttemptsAvailableWithEveryGuessOfCharOrWord(testSet, "S"));
        Assert.assertEquals(8, hangmanMock.decreaseTheNumberOfAttemptsAvailableWithEveryGuessOfCharOrWord(testSet, "x"));
        Assert.assertEquals(9, hangmanMock.decreaseTheNumberOfAttemptsAvailableWithEveryGuessOfCharOrWord(testSet, "popular"));
        Assert.assertEquals(8, hangmanMock.decreaseTheNumberOfAttemptsAvailableWithEveryGuessOfCharOrWord(testSet, "open"));

    }










}
