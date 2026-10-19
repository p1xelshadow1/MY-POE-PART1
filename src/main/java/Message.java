/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Student
 */
public class Message {
    
    public String messageID;
    public int numMessagesSent;
    public String recipient;
    public String messageText;
    public String messageHash;

    public Message(String messageID, int numMessagesSent, String recipient, String messageText) {
        this.messageID = messageID;
        this.numMessagesSent = numMessagesSent;
        this.recipient = recipient;
        this.messageText = messageText;
        // messageHash is NOT passed in: it gets created from the other fields later
    }
}
    

