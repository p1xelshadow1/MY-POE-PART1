/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.poepart1;

import java.util.ArrayList;
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
                runQuickChat(scanner, firstname + " " + lastname);
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

    // Main QuickChat menu (Part 2 + the new "Stored Messages" option 4)
    public static void runQuickChat(Scanner scanner, String senderName) {
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
            System.out.println("4) Stored Messages");
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
                case 4:
                    storedMessagesMenu(scanner, senderName);
                    break;
                default:
                    System.out.println("Invalid option, please choose 1, 2, 3 or 4.");
            }
        } while (choice != 3);
    }

    // Option 4: works with the arrays, reading stored messages from messages.json first
    public static void storedMessagesMenu(Scanner scanner, String senderName) {
        Message.loadStoredMessages();

        int choice;
        do {
            System.out.println("\nStored Messages");
            System.out.println("1) Display sender and recipient of all stored messages");
            System.out.println("2) Display the longest stored message");
            System.out.println("3) Search for a message ID");
            System.out.println("4) Search for all messages stored for a recipient");
            System.out.println("5) Delete a message using its hash");
            System.out.println("6) Display a report of all stored messages");
            System.out.println("7) Back to main menu");
            choice = readInt(scanner);

            switch (choice) {
                case 1:
                    System.out.println(Message.displaySenderAndRecipient(senderName));
                    break;
                case 2: {
                    String longest = Message.longestStoredMessage();
                    if (longest.isEmpty()) {
                        System.out.println("No stored messages.");
                    } else {
                        System.out.println("Longest stored message: " + longest);
                    }
                    break;
                }
                case 3: {
                    System.out.println("Enter the message ID:");
                    Message found = Message.findByMessageID(scanner.nextLine().trim());
                    if (found == null) {
                        System.out.println("Message ID not found.");
                    } else {
                        System.out.println("Recipient: " + found.recipient);
                        System.out.println("Message: " + found.messageText);
                    }
                    break;
                }
                case 4: {
                    System.out.println("Enter the recipient's cell number:");
                    ArrayList<String> results = Message.searchByRecipient(scanner.nextLine().trim());
                    if (results.isEmpty()) {
                        System.out.println("No messages found for that recipient.");
                    } else {
                        for (String text : results) {
                            System.out.println("\"" + text + "\"");
                        }
                    }
                    break;
                }
                case 5:
                    System.out.println("Enter the message hash:");
                    System.out.println(Message.deleteByHash(scanner.nextLine().trim()));
                    break;
                case 6:
                    System.out.println(Message.displayReport(Message.storedMessages));
                    break;
                case 7:
                    break;
                default:
                    System.out.println("Invalid option, please choose 1 to 7.");
            }
        } while (choice != 7);
    }

    // Collects the details of one message, then lets the user send, disregard or store it
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