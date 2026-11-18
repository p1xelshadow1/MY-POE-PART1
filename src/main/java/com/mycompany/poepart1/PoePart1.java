/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.poepart3;

import java.util.Scanner;

public class PoePart1 {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("enter your firstname:");
        String firstname = scanner.nextLine();

        System.out.println("enter your lastname:");
        String lastname = scanner.nextLine();

        System.out.println("enter your username:");
        String username = scanner.nextLine();

        System.out.println("enter your SAcellnum:");
        String SAcellnum = scanner.nextLine();

        System.out.println("enter your password:");
        String password = scanner.nextLine();

        Login newUser = new Login(firstname, lastname, SAcellnum, username, password);
        System.out.println(newUser.registerUser());

        if (newUser.checkUserName() && newUser.checkCellPhoneNumber() && newUser.checkPasswordComplexity()) {

            System.out.println("Enter your username to login:");
            String loginUsername = scanner.nextLine();

            System.out.println("Enter your password to login:");
            String loginPassword = scanner.nextLine();

            boolean loggedIn = newUser.loginUser(loginUsername, loginPassword);
            System.out.println(newUser.returnLoginStatus(loginUsername, loginPassword));

            if (loggedIn) {
                runQuickChat(scanner);
            }
        } else {
            System.out.println("Registration failed - please correct your details before logging in.");
        }
    }

    // Reads a whole number; returns 0 if the input is not a number
    private static int readInt(Scanner scanner) {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public static void runQuickChat(Scanner scanner) {
        System.out.println("Welcome to QuickChat.");

        int maxMessages = 0;
        while (maxMessages <= 0) {
            System.out.println("How many messages do you wish to enter?");
            maxMessages = readInt(scanner);
            if (maxMessages <= 0) {
                System.out.println("Please enter a whole number greater than 0.");
            }
        }

        int entered = 0;
        int choice;

        do {
            System.out.println("\n1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Quit");
            choice = readInt(scanner);

            switch (choice) {
                case 1:
                    if (entered >= maxMessages) {
                        System.out.println("You have reached your limit of " + maxMessages + " messages.");
                    } else {
                        enterMessage(scanner, entered);
                        entered++;
                        if (entered == maxMessages) {
                            System.out.println("Total messages sent: " + Message.returnTotalMessages());
                        }
                    }
                    break;
                case 2:
                    System.out.println("Coming Soon.");
                    break;
                case 3:
                    System.out.println("Total messages sent: " + Message.returnTotalMessages());
                    break;
                default:
                    System.out.println("Invalid option, please choose 1, 2 or 3.");
            }
        } while (choice != 3);
    }

    public static void enterMessage(Scanner scanner, int messageNumber) {
        // recipient (repeat until valid)
        String recipient;
        while (true) {
            System.out.println("Enter the recipient's cell number (with international code):");
            recipient = scanner.nextLine().trim();
            if (Message.isValidCell(recipient)) {
                System.out.println("Cell phone number successfully captured.");
                break;
            }
            System.out.println("Cell phone number is incorrectly formatted or does not "
                    + "contain an international code. Please correct the number and try again.");
        }

        // message text (repeat until 250 characters or fewer)
        String text;
        while (true) {
            System.out.println("Enter your message:");
            text = scanner.nextLine();
            if (text.length() <= 250) {
                System.out.println("Message sent");
                break;
            }
            System.out.println("Please enter a message of less than 250 characters.");
        }

        Message msg = new Message(Message.generateMessageID(), messageNumber, recipient, text);
        System.out.println(msg.messageIDCreated());

        // send / disregard / store (repeat until valid)
        int option = 0;
        while (option < 1 || option > 3) {
            System.out.println("Choose an option:");
            System.out.println("1) Send Message");
            System.out.println("2) Disregard Message");
            System.out.println("3) Store Message to send later");
            option = readInt(scanner);
        }

        System.out.println(msg.SentMessage(option));

        if (option == 1) {
            System.out.println(msg.getDetails());
        } else if (option == 2) {
            if (scanner.nextLine().trim().equals("0")) {
                System.out.println("Message deleted.");
            }
        }
    }
}