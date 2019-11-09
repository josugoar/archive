import os
import random


def cls():
    os.system('cls' if os.name=='nt' else 'clear')

db = ["hello", "word"]
word_select = db[random.randint(0, len(db) - 1)].lower()

def hang(max_guess):

    cls()
    input("HANGMAN")

    word = len(word_select) * "_"
    correct = 0

    for i in range(0, max_guess):
        output = ""
        cls()
        print(word + "\n")
        print("Guesses left: " + str(max_guess-i))
        guess = input("Enter guess: ").lower()
        for char in range(0, len(word_select)):
            if (word[char] == "_"):
                if (word_select[char] == guess):
                    output += guess
                    correct += 1
                    max_guess += 1
                else:
                    output += "_"
            else:
                output += word[char]
        word = output
        if (correct == len(word_select)):
            cls()
            print(word + "\n\nYou Win!")
            return 1

    cls()
    print("Word: " + word_select + "\n\nYou Lose...")
    return 0

hang(max_guess=5)
