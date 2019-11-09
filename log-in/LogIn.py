import pickle as pk
#UNDONE: Delete output as mains progress
#UNDONE: Import ASCII libary for UI
class user:
    #Active user
        #__init__(): initialize user with username, password and contact values
        #changePassword(): change user password
    def __init__(self, username, password):
        self.username = username
        self.password = password

def checkFile():
    #Check file
        #f_check: open or create file and close it
    try:
        f_check = open("pk_database.txt")
    except FileNotFoundError:
        f_check = open("pk_database.txt", "x")
    finally:
        f_check.close()

def readLines():
    #Read lines
        #user_inf: file content dictionary (pickle)
    global user_inf
    try:
        user_inf = pk.load(open("pk_database.txt", "rb"))
    except EOFError:
        user_inf = dict()

def endl(endL):
    #End line
    if(endL == True):
        print("")

def exception(error):
    #Exception
    if(error == True):
        print("Error: not matching input")
    if(current_main == 1):
        main_1(True)
    elif(current_main == 2):
        main_2(True)

def main_1(endL):
    #Main 1
    global current_main
    current_main = 1
    endl(endL)
    checkFile()
    readLines()
    select_1 = input("[1] Sign in\n[2] Create account\n[3] Exit\nSelect option: ")
    if(select_1 == "1"):
        signIn()
    elif(select_1 == "2"):
        createAccount()
    elif(select_1 == "3"):
        exit()
    else:
        exception(True)

def main_2(endL):
    #Main 2
    global current_main
    current_main = 2
    endl(endL)
    checkFile()
    readLines()
    select_2 = input("[1] Change password\n[2] Log out\nSelect option: ")
    if(select_2 == "1"):
        changePassword()
    elif(select_2 == "2"):
        main_1(True)
    else:
        exception(True)

def changePassword():
    #Change password
    password = input("\nCHANGE PASSWORD\nEnter previous password: ")
    if(password == currentUser.password):
        new_password = input("Enter new password: ")
        if(new_password == password):
            print("Error: new password matches previous password")
            goBack()
            changePassword()
        conf_password = input("Confirm password: ")
        if(new_password == conf_password):
            currentUser.password = new_password
            user_inf[currentUser.username] = new_password
            pk.dump(user_inf, open("pk_database.txt", "wb"), pk.HIGHEST_PROTOCOL)
            main_2(True)
        else:
            print("Error: not matching passwords")
            goBack()
            changePassword()
    else:
        print("Error: invalid password")
        goBack()
        changePassword()

def createAccount():
    #Create account
    username = input("\nUSERNAME\nInput username: ")
    if username in user_inf:
        print("Error: user already exists")
        goBack()
        createAccount()
    password = input("PASSWORD\nInput password: ")
    conf_password = input("Confirm password: ")
    if(password == conf_password):
        user_inf[username] = password
        pk.dump(user_inf, open("pk_database.txt", "wb"), pk.HIGHEST_PROTOCOL)
        main_1(True)
    print("Error: not matching passwords")
    goBack()
    createAccount()

def signIn():
    #Sign in
        #username: inputed username
        #password: inputed password
    global currentUser
    username = input("\nSIGN IN\nUsername\nInput username: ")
    if username in user_inf:
        password = input("PASSWORD\nInput password: ")
        if password in user_inf[username]:
            currentUser = user(username, password)
            print("\n-----Access granted-----")
            main_2(True)
        print("Error: invalid password")
        goBack()
        signIn()
    print("Error: user not found")
    goBack()
    signIn()

def goBack():
    back = input("\n[1] Continue\n[2] Go back\nSelect option: ")
    if(back == "2"):
        exception(False)
    elif(back != "1"):
        print("Error: not matching input")
        goBack()

main_1(False)
