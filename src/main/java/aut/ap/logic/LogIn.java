package aut.ap.logic;

import aut.ap.entities.User;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.Scanner;

public final class LogIn {
    private LogIn () {}

    public static SessionFactory sessionFactory;

    private static void setUpSessionFactory () {
        sessionFactory = new Configuration()
                .configure("hibernate.cfg.xml")
                .buildSessionFactory();
    }

    private static void closeSessionFactory () {
        sessionFactory.close();
    }

    public static void logIn() {
        setUpSessionFactory();

        Scanner scanner = new Scanner(System.in);

        String email;
        String password;

        while (true) {
            System.out.print("Email: ");
            email = scanner.nextLine();

            System.out.print("Password: ");
            password = scanner.nextLine();

            String finalEmail = email;
            String finalPassword = password;

            User user = sessionFactory.fromTransaction(session -> session.createNativeQuery(
                    "SELECT * FROM users WHERE users.email = :value1 and users.password = :value2", User.class
                    ).setParameter("value1", finalEmail).setParameter("value2", finalPassword).uniqueResult()
            );

            if (user != null) {
                System.out.println("Welcome, " + user.getFirstName() + " " + user.getLastName() + "!");
                break;
            }

            System.out.println("Your email or password is not correct, do want to try again? (yes/no)");
            String answer = scanner.nextLine();

            if (answer.equalsIgnoreCase("no")) {
                System.out.println("by...");
                break;
            }
        }

        closeSessionFactory();
    }
}
