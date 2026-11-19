/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.poepart1;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
/**
 *
 * @author Student
 */


public class MessageTest {

    private Message message1;
    private Message message2;

    @BeforeEach
    public void setUp() {
        Message.resetMessages();
        message1 = new Message("0012345678", 0, "+27718693002",
                "Hi Mike, can you join us for dinner tonight?");
        message2 = new Message("0198765432", 1, "08575975889",
                "Hi Keegan, did you receive the payment?");
    }

    // ================= Part 2 =================

    @Test
    public void testMessageLength_Success() {
        assertEquals("Message ready to send.", message1.checkMessageLength());
    }

    @Test
    public void testMessageLength_Failure() {
        String longText = new String(new char[255]).replace('\0', 'a');
        Message longMsg = new Message("0012345678", 0, "+27718693002", longText);
        assertEquals("Message exceeds 250 characters by 5; please reduce the size.",
                longMsg.checkMessageLength());
    }

    @Test
    public void testRecipientCell_Success() {
        assertEquals("Cell phone number successfully captured.", message1.checkRecipientCell());
    }

    @Test
    public void testRecipientCell_Failure() {
        assertEquals("Cell phone number is incorrectly formatted or does not "
                + "contain an international code. Please correct the number and try again.",
                message2.checkRecipientCell());
    }

    @Test
    public void testMessageHash_Correct() {
        assertEquals("00:0:HITONIGHT", message1.createMessageHash());

        // remaining hashes tested in a loop
        String[] ids = {"0012345678", "0198765432", "0011111111"};
        int[] nums = {0, 1, 0};
        String[] texts = {
            "Hi Mike, can you join us for dinner tonight?",
            "Hi Keegan, did you receive the payment?",
            "Hi, thanks"
        };
        String[] expected = {"00:0:HITONIGHT", "01:1:HIPAYMENT", "00:0:HITHANKS"};

        for (int i = 0; i < texts.length; i++) {
            Message m = new Message(ids[i], nums[i], "+27718693002", texts[i]);
            assertEquals(expected[i], m.createMessageHash());
        }
    }

    @Test
    public void testMessageID_Created() {
        assertTrue(message1.checkMessageID());
        assertEquals("Message ID generated: 0012345678", message1.messageIDCreated());
        assertEquals(10, Message.generateMessageID().length());
    }

    @Test
    public void testMessageSent_Send() {
        assertEquals("Message successfully sent.", message1.SentMessage(1));
    }

    @Test
    public void testMessageSent_Disregard() {
        assertEquals("Press 0 to delete the message.", message2.SentMessage(2));
    }

    @Test
    public void testMessageSent_Store() {
        assertEquals("Message successfully stored.", message1.SentMessage(3));
    }

    @Test
    public void testReturnTotalMessages() {
        message1.SentMessage(1);   // sent
        message2.SentMessage(2);   // disregarded, so not counted
        assertEquals(1, Message.returnTotalMessages());
    }

    // ================= Part 3 =================

    private Message t1, t2, t3, t4, t5;

    // Loads the 5 test messages from the brief into the arrays
    private void loadTestData() {
        t1 = new Message("0012345678", 0, "+27834557896", "Did you get the cake?");
        t2 = new Message("0198765432", 1, "+27838884567",
                "Where are you? You are late! I have asked you to be on time.");
        t3 = new Message("0211111111", 2, "+27834484567", "Yohoooo, I am at your gate.");
        t4 = new Message("0838884567", 3, "0838884567", "It is dinner time!");
        t5 = new Message("0511111111", 4, "+27838884567", "Ok, I am leaving without you.");

        t1.SentMessage(1);   // Sent
        t2.SentMessage(3);   // Stored
        t3.SentMessage(2);   // Disregard
        t4.SentMessage(1);   // Sent
        t5.SentMessage(3);   // Stored
    }

    @Test
    public void testSentArrayPopulated() {
        loadTestData();
        assertEquals("Did you get the cake?", Message.sentMessages.get(0).messageText);
        assertEquals("It is dinner time!", Message.sentMessages.get(1).messageText);
        assertEquals(2, Message.sentMessages.size());
        assertEquals(5, Message.messageIDs.size());
        assertEquals(5, Message.messageHashes.size());
    }

    @Test
    public void testLongestMessage() {
        loadTestData();
        assertEquals("Where are you? You are late! I have asked you to be on time.",
                Message.longestStoredMessage());
    }

    @Test
    public void testSearchByMessageID() {
        loadTestData();
        assertEquals("It is dinner time!", Message.searchByMessageID("0838884567"));
    }

    @Test
    public void testSearchByRecipient() {
        loadTestData();
        assertEquals(Arrays.asList(
                "Where are you? You are late! I have asked you to be on time.",
                "Ok, I am leaving without you."),
                Message.searchByRecipient("+27838884567"));
    }

    @Test
    public void testDeleteByHash() {
        loadTestData();
        assertEquals("Message: \"Where are you? You are late! I have asked you to be on time.\" successfully deleted.",
                Message.deleteByHash("01:1:WHERETIME"));
        assertEquals(1, Message.storedMessages.size());
    }

    @Test
    public void testDisplayReport() {
        loadTestData();
        String expected =
                "Message ID: 0012345678\nMessage Hash: 00:0:DIDCAKE\n"
                + "Recipient: +27834557896\nMessage: Did you get the cake?\n\n"
                + "Message ID: 0838884567\nMessage Hash: 08:3:ITTIME\n"
                + "Recipient: 0838884567\nMessage: It is dinner time!\n\n";
        assertEquals(expected, Message.displayReport(Message.sentMessages));
    }

    @Test
    public void testReadJSONIntoArray() {
        loadTestData();               // stores messages 2 and 5 in messages.json
        Message.loadStoredMessages(); // read them back from the file
        assertEquals(2, Message.storedMessages.size());
        assertEquals("Ok, I am leaving without you.", Message.storedMessages.get(1).messageText);
    }
}


