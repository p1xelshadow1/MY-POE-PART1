/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.poepart1;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Random;

public class Message {

    // ----- details of ONE message -----
    public String messageID;
    public int numMessagesSent;
    public String recipient;
    public String messageText;
    public String messageHash;

    // ----- arrays shared by the whole program (Part 3) -----
    public static ArrayList<Message> sentMessages = new ArrayList<>();
    public static ArrayList<Message> disregardedMessages = new ArrayList<>();
    public static ArrayList<Message> storedMessages = new ArrayList<>();
    public static ArrayList<String> messageIDs = new ArrayList<>();
    public static ArrayList<String> messageHashes = new ArrayList<>();

    private static final String JSON_FILE = "messages.json";

    public Message(String messageID, int numMessagesSent, String recipient, String messageText) {
        this.messageID = messageID;
        this.numMessagesSent = numMessagesSent;
        this.recipient = recipient;
        this.messageText = messageText;
        this.messageHash = createMessageHash();
    }

    // ===================== Part 2 methods =====================

    // Random 10-digit ID, kept as a String so leading zeros are not lost
    public static String generateMessageID() {
        Random rand = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            sb.append(rand.nextInt(10));
        }
        return sb.toString();
    }

    public String messageIDCreated() {
        return "Message ID generated: " + messageID;
    }

    // ID must be no more than ten characters
    public boolean checkMessageID() {
        return messageID != null && messageID.length() <= 10;
    }

    // Same rule as Part 1: +27 followed by up to 10 digits
    public static boolean isValidCell(String cell) {
        return cell != null && cell.matches("^\\+27[0-9]{1,10}$");
    }

    public String checkRecipientCell() {
        if (isValidCell(recipient)) {
            return "Cell phone number successfully captured.";
        } else {
            return "Cell phone number is incorrectly formatted or does not "
                    + "contain an international code. Please correct the number and try again.";
        }
    }

    public String checkMessageLength() {
        if (messageText.length() <= 250) {
            return "Message ready to send.";
        } else {
            int excess = messageText.length() - 250;
            return "Message exceeds 250 characters by " + excess + "; please reduce the size.";
        }
    }

    // first two ID digits : message number : FIRSTWORDLASTWORD (all caps)
    public String createMessageHash() {
        String firstTwo = messageID.substring(0, 2);
        String[] words = messageText.trim().split("\\s+");
        String first = words[0].replaceAll("[^A-Za-z0-9]", "");
        String last = words[words.length - 1].replaceAll("[^A-Za-z0-9]", "");
        return (firstTwo + ":" + numMessagesSent + ":" + first + last).toUpperCase();
    }

    // 1 = send, 2 = disregard, 3 = store
    public String SentMessage(int choice) {
        // every message's ID and hash go into their own arrays
        if (choice >= 1 && choice <= 3) {
            messageIDs.add(messageID);
            messageHashes.add(messageHash);
        }

        switch (choice) {
            case 1:
                sentMessages.add(this);
                return "Message successfully sent.";
            case 2:
                disregardedMessages.add(this);
                return "Press 0 to delete the message.";
            case 3:
                storeMessage();
                return "Message successfully stored.";
            default:
                return "Invalid option.";
        }
    }

    // Details of one message in the required order
    public String getDetails() {
        return "Message ID: " + messageID + "\n"
                + "Message Hash: " + messageHash + "\n"
                + "Recipient: " + recipient + "\n"
                + "Message: " + messageText + "\n";
    }

    // All messages sent while the program is running
    public static String printMessages() {
        StringBuilder sb = new StringBuilder();
        for (Message m : sentMessages) {
            sb.append(m.getDetails()).append("\n");
        }
        return sb.toString();
    }

    public static int returnTotalMessages() {
        return sentMessages.size();
    }

    // ===================== JSON storage =====================

    // Adds this message to the stored array and saves the array to messages.json
    public void storeMessage() {
        storedMessages.add(this);
        saveStoredMessages();
    }

    // Writes the whole stored array to messages.json using Gson
    public static void saveStoredMessages() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter(JSON_FILE)) {
            gson.toJson(storedMessages, writer);
        } catch (IOException e) {
            System.out.println("Could not save messages: " + e.getMessage());
        }
    }

    // Reads messages.json back into the stored array.
    // Reading the JSON array with Gson is adapted from the Gson User Guide
    // (Google, n.d.), "Array Examples": https://github.com/google/gson/blob/main/UserGuide.md
    public static void loadStoredMessages() {
        storedMessages.clear();
        File file = new File(JSON_FILE);
        if (!file.exists()) {
            return;   // nothing stored yet
        }
        try (Reader reader = new FileReader(file)) {
            Gson gson = new Gson();
            Message[] fromFile = gson.fromJson(reader, Message[].class);
            if (fromFile != null) {
                for (Message m : fromFile) {
                    storedMessages.add(m);
                }
            }
        } catch (IOException e) {
            System.out.println("Could not read messages: " + e.getMessage());
        }
    }

    // ===================== Part 3 methods =====================

    // Joins the sent, disregarded and stored arrays so we can search them all
    private static ArrayList<Message> allMessages() {
        ArrayList<Message> all = new ArrayList<>();
        all.addAll(sentMessages);
        all.addAll(disregardedMessages);
        all.addAll(storedMessages);
        return all;
    }

    // 2a: sender (the logged-in user) and recipient of every stored message
    public static String displaySenderAndRecipient(String sender) {
        if (storedMessages.isEmpty()) {
            return "No stored messages.";
        }
        StringBuilder sb = new StringBuilder();
        for (Message m : storedMessages) {
            sb.append("Sender: ").append(sender)
              .append(" | Recipient: ").append(m.recipient).append("\n");
        }
        return sb.toString();
    }

    // 2b: the longest message in a list (empty String if the list is empty)
    public static String longestMessage(ArrayList<Message> list) {
        String longest = "";
        for (Message m : list) {
            if (m.messageText.length() > longest.length()) {
                longest = m.messageText;
            }
        }
        return longest;
    }

    public static String longestStoredMessage() {
        return longestMessage(storedMessages);
    }

    // 2c: find a message by its ID (null if not found)
    public static Message findByMessageID(String id) {
        for (Message m : allMessages()) {
            if (m.messageID.equals(id)) {
                return m;
            }
        }
        return null;
    }

    public static String searchByMessageID(String id) {
        Message m = findByMessageID(id);
        if (m == null) {
            return "Message ID not found.";
        }
        return m.messageText;
    }

    // 2d: all sent or stored messages for one recipient
    public static ArrayList<String> searchByRecipient(String recipient) {
        ArrayList<String> results = new ArrayList<>();
        for (Message m : sentMessages) {
            if (m.recipient.equals(recipient)) {
                results.add(m.messageText);
            }
        }
        for (Message m : storedMessages) {
            if (m.recipient.equals(recipient)) {
                results.add(m.messageText);
            }
        }
        return results;
    }

    // 2e: delete a message using its hash
    public static String deleteByHash(String hash) {
        Message found = null;
        for (Message m : allMessages()) {
            if (m.messageHash.equals(hash)) {
                found = m;
                break;
            }
        }
        if (found == null) {
            return "Message hash not found.";
        }

        String text = found.messageText;
        sentMessages.removeIf(m -> m.messageHash.equals(hash));
        disregardedMessages.removeIf(m -> m.messageHash.equals(hash));
        boolean wasStored = storedMessages.removeIf(m -> m.messageHash.equals(hash));

        // keep the ID and hash arrays in step
        int index = messageHashes.indexOf(hash);
        if (index >= 0) {
            messageHashes.remove(index);
            messageIDs.remove(index);
        }

        if (wasStored) {
            saveStoredMessages();   // keep messages.json up to date
        }
        return "Message: \"" + text + "\" successfully deleted.";
    }

    // 2f: report with the full details of every message in a list
    public static String displayReport(ArrayList<Message> list) {
        if (list.isEmpty()) {
            return "No messages to display.";
        }
        StringBuilder sb = new StringBuilder();
        for (Message m : list) {
            sb.append(m.getDetails()).append("\n");
        }
        return sb.toString();
    }

    // Used by the unit tests to start clean
    public static void resetMessages() {
        sentMessages.clear();
        disregardedMessages.clear();
        storedMessages.clear();
        messageIDs.clear();
        messageHashes.clear();
    }
}