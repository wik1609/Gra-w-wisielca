import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Random;

public class Hangman { //klasa główna Hangman
    //Deklaracja stałych dla nazw plikow
    private static final String EASY_WORDS_FILE = "easy_words.txt";
    private static final String MEDIUM_WORDS_FILE = "medium_words.txt";
    private static final String HARD_WORDS_FILE = "hard_words.txt";
    //Deklaracja stałych dla maksymalnej liczby prób w danym poziomie trudności
    private static final int EASY_TRIES = 8;
    private static final int MEDIUM_TRIES = 10;
    private static final int HARD_TRIES = 15;
    private static final String STATS_FILE = "hangman_stats.txt";//Deklaracja stałej dla pliku ze statystykami gry

    public static void main(String[] args) { //Metoda, która uruchamia program
        Scanner scanner = new Scanner(System.in);//obiekt Scanner do odczytu wejścia użytownika
        //Pętla wyświetlająca menu gry i wybór gracza
        while (true) {
            System.out.println("Choose an option: 1. Play Game 2. Add Words 3. Edit Words 4. New Game 5. Exit"); //Wyświetlanie menu głównego gry z wyborem
            int choice = scanner.nextInt();
            scanner.nextLine();  // nowa linia po wprowadzeniu liczby
            //Wybór opcji z menu
            switch (choice) {
                case 1:
                    playGame(scanner); //rozpoczęcie gry
                    break;
                case 2:
                    addWords(scanner); //dodawanie nowych słów
                    break;
                case 3:
                    editWords(scanner);//edytowanie słów
                    break;
                case 4:
                    newGame(); //rozpoczecie nowej gry i reset statystyk
                    break;
                case 5:
                    System.out.println("Exiting the game."); //wyjście z programu
                    return;
                default:
                    System.out.println("Invalid choice. Please try again."); //nieprawidłowy wybór
            }
        }
    }
    //Metoda do rozpoczęcia gry
    private static void playGame(Scanner scanner) {
        System.out.println("Choose difficulty: 1. Easy 2. Medium 3. Hard"); //Wybór poziomu trudności gry
        int difficulty = scanner.nextInt();
        scanner.nextLine();  //nowa linia

        String wordFile; //Ustawianie pliku z odpowiednimi słowami oraz liczby prób na podstawie trudności gry
        int maxTries;
        switch (difficulty) {
            case 1:
                wordFile = EASY_WORDS_FILE;
                maxTries = EASY_TRIES;
                break;
            case 2:
                wordFile = MEDIUM_WORDS_FILE;
                maxTries = MEDIUM_TRIES;
                break;
            case 3:
                wordFile = HARD_WORDS_FILE;
                maxTries = HARD_TRIES;
                break;
            default:
                System.out.println("Invalid choice. Defaulting to Medium difficulty.");
                wordFile = MEDIUM_WORDS_FILE;
                maxTries = MEDIUM_TRIES;
                break;
        }

        System.out.println("Choose an option: 1. Use existing words 2. Add new words");//Wybór słów w zależności czy chcemy użyc istniejącego słowa z bazdy czy dodac nowe
        int wordChoice = scanner.nextInt();
        scanner.nextLine();  //nowa linia

        if (wordChoice == 2) {
            addWords(scanner, wordFile); //Dodanie nowych słów
        }

        String[] words = readWordsFromFile(wordFile); //Odczytywanie słów z pliku
        if (words.length == 0) {
            System.out.println("No words available. Please add words first.");
            return;
        }

        Random random = new Random(); //losowanie słowa z listy
        String word = words[random.nextInt(words.length)];
        char[] guessedWord = new char[word.length()];
        for (int i = 0; i < guessedWord.length; i++) {
            guessedWord[i] = '_'; //słowo zgadywane wyświetlane jest dzięki znakowi podkreślenia
        }

        int tries = 0;
        int successfulGuesses = 0;
        boolean wordGuessed = false;

        while (tries < maxTries && !wordGuessed) { //Pętla, w której zgadujemy litery
            System.out.println("Current word: " + new String(guessedWord));
            System.out.println("Tries left: " + (maxTries - tries));
            System.out.print("Guess a letter: ");
            char guess = scanner.nextLine().charAt(0);

            boolean correctGuess = false;
            for (int i = 0; i < word.length(); i++) {
                if (word.charAt(i) == guess) {
                    if (guessedWord[i] == '_') {
                        guessedWord[i] = guess;
                        correctGuess = true;
                        successfulGuesses++;
                    }
                }
            }

            if (!correctGuess) {
                tries++;
            }

            wordGuessed = true;
            for (char c : guessedWord) {
                if (c == '_') {
                    wordGuessed = false;
                    break;
                }
            }

            displayHangman(tries, maxTries); //wyświetlanie stanu wisielca
        }

        if (wordGuessed) { //Wypisanie wyniku gry
            System.out.println("Congratulations! You guessed the word: " + word);
            System.out.println("You guessed the word in " + successfulGuesses + " attempts.");
            updateStats(true); //Aktualizacja statystyk w przypadku wygranej
        } else {
            System.out.println("Game over! The word was: " + word);
            updateStats(false); //Aktualizacja statystyk w przypadku porażki
        }

        displayStats(); //Wyświetlanie statystyk
    }

    private static void addWords(Scanner scanner) { //Wybór poziomu trudności dla dodawanych słów
        System.out.println("Choose difficulty to add words: 1. Easy 2. Medium 3. Hard");
        int difficulty = scanner.nextInt();
        scanner.nextLine();  //nowa linia

        String wordFile; //ustawianie odpowiedniego pliku dla poziomu trudności
        switch (difficulty) {
            case 1:
                wordFile = EASY_WORDS_FILE;
                break;
            case 2:
                wordFile = MEDIUM_WORDS_FILE;
                break;
            case 3:
                wordFile = HARD_WORDS_FILE;
                break;
            default:
                System.out.println("Invalid choice.");
                return;
        }

        addWords(scanner, wordFile); //dodawanie nowych słów
    }
    //dodawanie nowych słów do wskazanego pliku
    private static void addWords(Scanner scanner, String wordFile) {
        System.out.print("Enter the words to add (separated by spaces or commas): ");
        String input = scanner.nextLine().trim();
        String[] newWords = input.split("[,\\s]+"); //wprowadzanie słów odbywa się dzięki spacji

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(wordFile, true))) {
            for (String word : newWords) {
                writer.write(word);
                writer.newLine();
            }
            System.out.println("Words added successfully.");
        } catch (IOException e) {
            System.out.println("Error adding words: " + e.getMessage());
        }
    }
    //Metoda edytowania słów w grze
    private static void editWords(Scanner scanner) {
        System.out.println("Choose difficulty to edit words: 1. Easy 2. Medium 3. Hard"); //wybor poziomu trudnosci dla edycji slow
        int difficulty = scanner.nextInt();
        scanner.nextLine();  //nowa linia

        String wordFile; //ustawianie pliku na podstawie poziomu trudności
        switch (difficulty) {
            case 1:
                wordFile = EASY_WORDS_FILE;
                break;
            case 2:
                wordFile = MEDIUM_WORDS_FILE;
                break;
            case 3:
                wordFile = HARD_WORDS_FILE;
                break;
            default:
                System.out.println("Invalid choice.");
                return;
        }
        //Odczytywanie słów z pliku do listy
        List<String> words = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(wordFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                words.add(line);
            }
        } catch (IOException e) {
            System.out.println("Error reading words: " + e.getMessage());
            return;
        }

        if (words.isEmpty()) {
            System.out.println("No words found for the chosen difficulty.");
            return;
        }

        System.out.println("Current words:"); //Wyświetlanie obecnych słów
        for (int i = 0; i < words.size(); i++) {
            System.out.println((i + 1) + ". " + words.get(i));
        }

        System.out.println("Choose an option: 1. Edit Word 2. Delete Word 3. Exit"); //Wybór opcji edytowania lub usunięcia słowa
        int choice = scanner.nextInt();
        scanner.nextLine();  // nowa linia

        switch (choice) {
            case 1:
                System.out.print("Enter the number of the word to edit: "); //Edytowanie wybranego słowa
                int editIndex = scanner.nextInt() - 1;
                scanner.nextLine();  //nowa linia
                if (editIndex < 0 || editIndex >= words.size()) {
                    System.out.println("Invalid choice.");
                    return;
                }
                System.out.print("Enter the new word: ");
                words.set(editIndex, scanner.nextLine().trim());
                break;
            case 2:
                System.out.print("Enter the number of the word to delete: "); //Usunięcie wybranego słowa
                int deleteIndex = scanner.nextInt() - 1;
                scanner.nextLine();  //nowa linia
                if (deleteIndex < 0 || deleteIndex >= words.size()) {
                    System.out.println("Invalid choice.");
                    return;
                }
                words.remove(deleteIndex);
                break;
            case 3:
                return;
            default:
                System.out.println("Invalid choice.");
                return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(wordFile))) { //zapisywanie nowych słów do pliku
            for (String word : words) {
                writer.write(word);
                writer.newLine();
            }
            System.out.println("Words updated successfully.");
        } catch (IOException e) {
            System.out.println("Error updating words: " + e.getMessage());
        }
    }

    private static void newGame() { //reset statystyk gry
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(STATS_FILE))) {
            writer.write("0\n0\n");  // resetowanie wygranych i przegranych do 0
            System.out.println("Statistics reset successfully. Starting a new game session.");
        } catch (IOException e) {
            System.out.println("Error resetting stats: " + e.getMessage());
        }
    }

    private static void displayHangman(int tries, int maxTries) { //Wyświetlanie stanu wisielca zależne od liczby ruchów
        String[] stages = {
            """
               --------
               |      |
               |      
               |    
               |      
               |     
               -
            """,
            """
               --------
               |      |
               |      O
               |    
               |      
               |     
               -
            """,
            """
               --------
               |      |
               |      O
               |      |
               |      |
               |     
               -
            """,
            """
               --------
               |      |
               |      O
               |     \\|
               |      |
               |     
               -
            """,
            """
               --------
               |      |
               |      O
               |     \\|/
               |      |
               |     
               -
            """,
            """
               --------
               |      |
               |      O
               |     \\|/
               |      |
               |     / 
               -
            """,
            """
               --------
               |      |
               |      O
               |     \\|/
               |      |
               |     / \\
               -
            """
        };

        int stageIndex = (int) ((double) tries / maxTries * (stages.length - 1));
        System.out.println(stages[stageIndex]);
    }

    private static int[] readStats() { //Odczytywanie z pliku liczby wygranych i przegranych gier
        int[] stats = {0, 0};
        try (BufferedReader reader = new BufferedReader(new FileReader(STATS_FILE))) {
            stats[0] = Integer.parseInt(reader.readLine());
            stats[1] = Integer.parseInt(reader.readLine());
        } catch (IOException | NumberFormatException e) {
            System.out.println("No previous stats found. Starting fresh.");
        }
        return stats;
    }

    private static void updateStats(boolean win) { //Aktualizacja statystyk gry; wygrane i przegrane
        int[] stats = readStats();
        if (win) {
            stats[0]++;
        } else {
            stats[1]++;
        }
        saveStats(stats[0], stats[1]);
    }

    private static void displayStats() { //wyświetlanie statystyk gry
        int[] stats = readStats();
        System.out.println("Wins: " + stats[0] + " | Losses: " + stats[1]);
    }
    //Zapisywanie statystyk gry do pliku
    private static void saveStats(int wins, int losses) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(STATS_FILE))) {
            writer.write(wins + "\n");
            writer.write(losses + "\n");
        } catch (IOException e) {
            System.out.println("Error saving stats: " + e.getMessage());
        }
    }

    private static String[] readWordsFromFile(String filename) { //Odczytywanie słów z pliku
        List<String> words = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                words.add(line);
            }
        } catch (IOException e) {
            System.out.println("Error reading words: " + e.getMessage());
        }
        return words.toArray(new String[0]);
    }
}
