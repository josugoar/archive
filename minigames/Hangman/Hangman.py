import os
import time
import random
import string

db = ["hello", "word"]
def cls():
    os.system("cls")
def checkLetter(guess, word, output, correct=None):
    temp_output = ""
    print("Guess: " + guess)
    for char in range(0, len(word)):
        if (output[char] == "_"):
            if (guess == word[char]):
                print("\33[92m" + guess + "\033[0m", end="", flush=True)
                temp_output += guess
                correct += 1
            else:
                print("\33[91m" + "_" + "\033[0m", end="", flush=True)
                temp_output += "_"
        else:
            print("\33[92m" + output[char] + "\033[0m", end="", flush=True)
            temp_output += output[char]
        time.sleep(0.25)
    return temp_output, correct
def hang(max_guess):
    cls()
    print("\33[44m" + "HANGMAN" + "\033[0m")
    input("\33[5m" + "Press enter to continue" + "\033[0m")
    word = random.choice(db).upper()
    output = len(word) * "_"
    correct = 0
    incorrect = 0
    while incorrect < max_guess:
        cls()
        if (correct == len(word)):
            print("\33[42m" + "You win!" + "\033[0m")
            return 1
        print("{}\nGuesses left: {}".format(output, max_guess - incorrect))
        guess = input("Enter guess: ").upper()
        cls()
        if guess.isalpha():
            if guess in word:
                output, correct = checkLetter(guess, word, output, correct)
                print("\033[92m" + "\nCorrect!" + "\033[0m")
            else:
                checkLetter(guess, word, output)
                print("\033[91m" + "\nIncorrect..." + "\033[0m")
                incorrect += 1
        else:
            print("\33[91m" + "Error: input must be a letter" + "\033[0m")
        time.sleep(1)
    time.sleep(1)
    cls()
    print("\33[41m" + "You loose..." + "\033[0m")
    return 0
hang(max_guess=5)
