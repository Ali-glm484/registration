package aut.ap;

import aut.ap.logic.LogIn;
import aut.ap.logic.SignUp;

import java.util.Scanner;

public class Main {


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("[L]og in, [S]ign up: ");
        String answer = scanner.next();

        if (answer.equalsIgnoreCase("l"))
            LogIn.logIn();
        else
            SignUp.signUP();
    }
}