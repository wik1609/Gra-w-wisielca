# Gra-w-wisielca
1.Tytuł projektu: Gra w wisielca
2.Spis treści: 
*Zasady gry - Hangman polega na odgadnięciu wylosowanego wcześniej słowa poprzez wprowadzanie pojedynczych liter, jeśli litera zostanie odgadnięta to zostanie ona zapisana w przynależnym jej miejscu. Jeśli jednak nie odgadniemy odpowiedniej litery gra zaczyna rysować na ekranie WISIELCA. Każde słowo ma ograniczoną ilość liter do odgadnięcia. 
*Funkcje gry:
-Interaktywne menu - pozwala wybrać jedną spośród pięciu dostępnych opcji: Start gry,Dodawanie słów, Edytowanie słow, Rozpoczęcie nowej gry oraz Wyjście z gry
-Wybór poziomu trudności dostępnych słów podczas gry lub w przypadku dodawania ich do bazy danych
-Przebieg rozgrywki tzn. bieżące zgadywanie liter i rysowanie WISIELCA
-Wyświetlanie statystyk gry: ilośc prób oraz ilość wygranych
-Pliki tekstowe: pliki txt o nazwach easy_words, medium_words, hard_words, hangman_stats - są to odpowiedniki baz danych, które obsługują dostępne w grze słowa i ich poziom trudności oraz statystyki gry
3.Lista i krótki opis zaimplementowanych w projekcie funkcjonalności:
TXT: 
-easy_words.txt: Przechowuje słowa dla łatwego poziomu trudności.
-medium_words.txt: Przechowuje słowa dla średniego poziomu trudności.
-hard_words.txt: Przechowuje słowa dla trudnego poziomu trudności.
-hangman_stats.txt: Przechowuje statystyki gry (wygrane i przegrane).
-Metoda main: metoda, które uruchamia grę i z menu pozwala wybrać kilka dostępnych opcji dzięki pętli
-Metoda playGame: metoda pozwalająca rozpocząć grę i wybrać poziom trudności oraz wylosować słowo, wyświetla też statystyki i na bieżąco aktualizuje wybrane litery 
-Metoda addWords: dodaje nowe słowa do plików tekstowych
-Metoda editWords: edytuje słowa w plikach tekstowych
-Metoda newGame: pozwala rozpocząć nową grę
-Metoda displayHangman: wyświetla WISIELCA
-Metody do obsługi statystyk (readStats, updateStats, displayStats, saveStats): kolejno metody pozwalają na odczytywanie, aktualizowanie, wyświetlanie i zapisywanie statystyk
-Metoda readWordsFromFile: odczytuje słowa z pliku tekstowego do tablicy
Instrukcja obsługi:
1. Kompilacja gry - w terminalu należy wpisać polecenie "javac Hangman.java" po czym gra skompiluje się
2. Uruchomienie gry (Run java lub w terminalu należy wpisać "java Hangman") - po uruchomeiniu gry wyświetli się menu główne z pięcioma opcjami do wyboru 
3. Menu główne - opcje gry wybieramy przyciskamu numerycznymi na klawiaturze od 1 do 5 po czym klikamy "enter"
- opcja 1. Start game - rozpoczyna grę
- opcja 2. Add words - dodaje nowe słowa do bazdy danych
- opcja 3. Edit words - pozwala edytować lub usuwać słowa z bazdy danych
- opcja 4. New Game - rozpoczyna nową grę i resetuje statystki
- opcja 5. Exit - kończy działanie programu

