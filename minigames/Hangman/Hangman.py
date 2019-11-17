import os
import time
import random
import string

# Word database
db = ["hello", "word"]

# Clear output
def cls():
    os.system("cls")

# Check given letter position in selected word (colored animation)
def checkLetter(guess, word, output, correct=None):
    temp_output = ""
    # Range word
    for char in range(0, len(word)):
        # Unknown letter
        if (output[char] == "_"):
            # Correct letter
            if (guess == word[char]):
                print("\33[92m" + guess + "\033[0m", end="", flush=True)
                temp_output += guess
                correct += 1
            # Incorrect letter
            else:
                print("\33[91m" + "_" + "\033[0m", end="", flush=True)
                temp_output += "_"
        # Guessed letter
        else:
            print("\33[92m" + output[char] + "\033[0m", end="", flush=True)
            temp_output += output[char]
        # Stop between letter check
        time.sleep(0.25)
    return temp_output, correct

# Main hangman
def hang(max_guess):
    # Intro
    cls()
    print("\33[44m" + "HANGMAN" + "\033[0m")
    input("\33[5m" + "Press enter to continue" + "\033[0m")
    # Select random word from database
    word = random.choice(db).upper()
    # Initialize empty output
    output = len(word) * "_"
    correct = 0
    incorrect = 0
    # End when incorrect count reaches maximum guesses
    while incorrect < max_guess:
        cls()
        # Win
        if (correct == len(word)):
            print("\33[42m" + "You win!" + "\033[0m")
            return 1
        # Guess input
        print("{}\nGuesses left: {}".format(output, max_guess - incorrect))
        guess = input("Enter guess: ").upper()
        cls()
        # Valid input (letter)
        if guess.isalpha():
            print("Guess: " + guess)
            # Correct guess
            if guess in word:
                output, correct = checkLetter(guess, word, output, correct)
                print("\033[92m" + "\nCorrect!" + "\033[0m")
            # Incorrect guess
            else:
                checkLetter(guess, word, output)
                print("\033[91m" + "\nIncorrect..." + "\033[0m")
                incorrect += 1
        # Invalid input
        else:
            print("\33[91m" + "Error: input must be a letter" + "\033[0m")
        time.sleep(1)
    # Loose
    time.sleep(1)
    cls()
    print("\33[41m" + "You loose..." + "\033[0m")
    return 0

hang(max_guess=5)
