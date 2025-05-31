package aut.ap.logic;

import aut.ap.entities.User;
import aut.ap.exceptions.DuplicateEmailException;
import aut.ap.exceptions.WeakPasswordException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.Scanner;

public final class SignUp {
    private SignUp () {}

    public static SessionFactory sessionFactory;

    private static void setUpSessionFactory () {
        sessionFactory = new Configuration()
                .configure("hibernate.cfg.xml")
                .buildSessionFactory();
    }

    private static void closeSessionFactory () {
        sessionFactory.close();
    }

    public static void signUP () {
        setUpSessionFactory();

        Scanner scanner = new Scanner(System.in);

        String firstName;
        String lastName;
        int age;
        String email;
        String password;

        System.out.print("First Name: ");
        firstName = scanner.nextLine();

        System.out.print("Last Name: ");
        lastName = scanner.nextLine();

        System.out.print("Age: ");
        age = scanner.nextInt();
        scanner.nextLine();

        while (true) {
            try {
                System.out.print("Email: ");
                email = scanner.nextLine();

                String finalEmail = email;
                User user = sessionFactory.fromTransaction(session ->
                        (User) session.createNativeQuery(
                        "SELECT * FROM users WHERE users.email = :value", User.class
                        ).setParameter("value", finalEmail).uniqueResult()
                );

                if (user != null)
                    throw new DuplicateEmailException("An account with this email already exists!");

                break;

            } catch (DuplicateEmailException e) {
                System.err.println(e.getMessage());
                System.out.println("Try again.");
            }
        }

        while (true) {
            try {
                System.out.print("Password: ");
                password = scanner.nextLine();

                if (password.length() < 8)
                    throw new WeakPasswordException("Weak password, Password must be at least 8 characters long.");

                break;

            } catch (WeakPasswordException e) {
                System.err.println(e.getMessage());
                System.out.println("Try again.");
            }
        }

        User user = new User(firstName, lastName, age, email, password);

        sessionFactory.inTransaction(session -> {
            session.persist(user);
        });

        System.out.println("You have successfully registered.");

        closeSessionFactory();
    }
}
