package Service;





import java.util.List;



import DAO.AccountDAO;
import DAO.MessageDAO;
import Model.Message;

public class MessageService {
    

private static final MessageDAO messageDAO = new MessageDAO();

    public static Message createMessage(int posted_by, String message_text, long time_posted_epoch) {
        // Validate messageText length and postedBy existence as needed
        if (message_text == null || message_text.trim().isEmpty() || message_text.length() > 255) {
            return null; // or throw an exception
        }

        if (!AccountDAO.accountExists(posted_by)) {
            return null; // or throw an exception
        }
        Message newMessage = MessageDAO.createMessage(posted_by, message_text, time_posted_epoch);
    
    if (newMessage == null) {
        return null; // Failed to create the message
    }
    
    return newMessage;
       
        // Create and return the message
        //return MessageDAO.createMessage(posted_by, message_text, time_posted_epoch);
    }
   
    

    
    public static  Message getMessageById(int messageId) {
        // Retrieve a message by its ID
        return MessageDAO.getMessageById(messageId);
    }
    

   

   
    public static Message updateMessage(int messageId, Message updatedMessage) {
        // Your implementation here to update the message identified by messageId
        // You may need to interact with the database to update the message
        // Retrieve the existing message from the database
        Message existingMessage = MessageDAO.getMessageById(messageId);

        // Check if the message exists
        if (existingMessage == null) {
            // Message not found
            return null;  // updatemessagemessagenotfound
        }

        // Check if the updated message text is not blank and not too long
        String newMessageText = updatedMessage.getMessage_text();
        if (newMessageText == null || newMessageText.trim().isEmpty()) {
            return null;  // updatemessagemessagestringempty
        }

        if (newMessageText.length() > 255) {
            return null;  // updatemessagemessagetoolong
        }

       // Update the message text
       existingMessage.setMessage_text(newMessageText);

       // Update the message in the database
       boolean updateSuccessful = MessageDAO.updateMessage(existingMessage);

       if (updateSuccessful) {
           return existingMessage;  // updatemessagesuccessful
       } else {
           return null;  // Handle update failure if necessary
       }
    }
    
public static List<Message> getMessagesByUser(int userId) {
        // Retrieve messages posted by a specific user
        return messageDAO.retrieveMessagesByUser(userId);
    }
   

    public static boolean deleteMessageByMessageId(int messageId) {
            return MessageDAO.deleteMessageByMessageId(messageId);
        }




    public static List<Message> getAllMessages() {
        return messageDAO.retrieveAllMessages();
    }

   


   
}

