package com.essenceglobal.hangman;

import java.io.*;
import java.util.*;

public class Hangman {


    public Hangman() throws IOException {
    }

    public String playGame(String[] simulatedUserInput, String randomWord){
        return "Program Output";
    }

    public String selectARandomWord(String word) {
        return word;
    }

    public String displayCharsInTheWordToTheUser(String word) {

        System.out.println("Display Word"+word);
        return "----------";

    }

   public String printCorrectlyOrIncorrectlyGuessedCharactersToConsole(char[] randomWordCharArray, String guessedChar) {

        return "Output to Console";

    }

    public String printCorrectlyOrIncorrectlyGuessedWordToConsole(String guessedWord, String randomWord){
        return "guessed word";
    }
    public String returnWordFromGuessedChars(String[] charsArr){
        return "word from chars";
    }

    public void flagIncorrectInput(String singleStringChar){
    }

    public String flagIfPlayerAlreadyGuessedCharacterOrWord(Set<String> guessedCharOrWord, String currentGuess){
            return "Already Guessed Char";
    }

    public int increaseTheNumberOfAttemptsWithEveryGuessOfCharOrWord(Set<String> existingCharOrWordSetString, String charOrWord){
        return 0;
    }

    public int decreaseTheNumberOfAttemptsAvailableWithEveryGuessOfCharOrWord(Set<String>existingCharOrWordSetString, String charOrWord){
        return 0;
    }






}




