package Controller;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.get("example-endpoint", this::exampleHandler);
        app.post("/register", this::registerHandler);
        app.post("/login", this::loginHandler);

        app.post("/messages", this::createMessageHandler);
        app.get("/messages", this::retrieveAllMessagesHandler);
        app.get("/messages/{messageId}", this::retrieveMessageByMessageIdHandler);
        app.delete("/messages/{messageId}", this::deleteMessageByMessageIdHandler);
        app.patch("/messages/{messageId}", this::updateMessageTextHandler);

        app.get("/accounts/{accountId}/messages", this::retrieveMessagesByUserHandler);


        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }
    private void registerHandler(Context context)throws JsonProcessingException {
        ObjectMapper mapper=new ObjectMapper();
    
        Account account =mapper.readValue(context.body(),Account.class);
        Object createdAccount=AccountService.registerAccount(account.getUsername(),account.getPassword());
        
      if(createdAccount !=null){
     context.json(mapper.writeValueAsString(createdAccount));
     
        }else{
          context.status(400);
        }
      }

    private void loginHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class);
        Object loggedInAccount = AccountService.login(account.getUsername(), account.getPassword());

        if (loggedInAccount != null) {
            context.json(mapper.writeValueAsString(loggedInAccount)).status(200);
        } else {
            context.status(401);
        }
    }

    private void createMessageHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(context.body(), Message.class);
        Object createdMessage = MessageService.createMessage(
                message.getPosted_by(),
                message.getMessage_text(),
                message.getTime_posted_epoch());

        if (createdMessage != null) {
            context.json(mapper.writeValueAsString(createdMessage)).status(200);
        } else {
            context.status(400);
        }
    }

    private void retrieveAllMessagesHandler(Context context) {
        List<Message> messages = MessageService.getAllMessages();
        context.json(messages).status(200);
    }

    private void retrieveMessageByMessageIdHandler(Context context) {
        int messageId = context.pathParamAsClass("messageId", Integer.class).get();
        Message message = MessageService.getMessageById(messageId);

        if (message != null) {
            context.json(message).status(200);
        } else {
            context.status(200); // Assuming 200 for not found, as mentioned in the user story
        }
    }

    private void deleteMessageByMessageIdHandler(Context context) {

        int messageId = context.pathParamAsClass("messageId", Integer.class).get();
        boolean messageDeleted = MessageService.deleteMessageByMessageId(messageId);

        if (messageDeleted) {
            context.status(200);
        } else {
            context.status(200);
        }
    }
    
    private void updateMessageTextHandler(Context context) throws JsonProcessingException {
        int messageId = context.pathParamAsClass("messageId", Integer.class).get();
        ObjectMapper mapper = new ObjectMapper();
        Message updatedMessage = mapper.readValue(context.body(), Message.class);

        Message result = MessageService.updateMessage(messageId, updatedMessage);

        if (result != null) {
            context.json(result).status(200);
        } else {
            context.status(400);
        }
    }
    
    private void retrieveMessagesByUserHandler(Context context) {
        int accountId = context.pathParamAsClass("accountId", Integer.class).get();
        List<Message> messages = MessageService.getMessagesByUser(accountId);
       
        context.json(messages).status(200);
    
    }




}