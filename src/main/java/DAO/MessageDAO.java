package DAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Model.Message;
import Util.ConnectionUtil;

public class MessageDAO {
    public static Message createMessage(int posted_by, String message_text, long time_posted_epoch) {
        try (Connection connection = ConnectionUtil.getConnection()) {
            String sql = "INSERT INTO Message(posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setInt(1, posted_by);
                preparedStatement.setString(2, message_text);
                preparedStatement.setLong(3, time_posted_epoch);


                int affectedRows = preparedStatement.executeUpdate();

                if (affectedRows > 0) {
                    try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            int message_id = generatedKeys.getInt(1);
                            return MessageDAO.getMessageById(message_id);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public List<Message> retrieveAllMessages() {
        try (Connection connection = ConnectionUtil.getConnection()) {
            String sql = "SELECT * FROM Message";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                ResultSet resultSet = preparedStatement.executeQuery();
                List<Message> messages = new ArrayList<>();

                while (resultSet.next()) {
                    int messageId = resultSet.getInt("message_id");
                    int postedBy = resultSet.getInt("posted_by");
                    String messageText = resultSet.getString("message_text");
                    long timePostedEpoch = resultSet.getLong("time_posted_epoch");

                    messages.add(new Message(messageId, postedBy, messageText, timePostedEpoch));
                }
                return messages;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public static  Message retrieveMessageById(int messageId) {
        try (Connection connection = ConnectionUtil.getConnection()) {
            String sql = "SELECT * FROM Message WHERE message_id=?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, messageId);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    int postedBy = resultSet.getInt("posted_by");
                    String messageText = resultSet.getString("message_text");
                    long timePostedEpoch = resultSet.getLong("time_posted_epoch");

                    return new Message(messageId, postedBy, messageText, timePostedEpoch);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static boolean updateMessage(Message updatedMessage) {
        try (Connection connection = ConnectionUtil.getConnection()) {
            String sql = "UPDATE Message SET message_text = ? WHERE message_id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, updatedMessage.getMessage_text());
                preparedStatement.setInt(2, updatedMessage.getMessage_id());

                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0){
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
        return false; // Update failed
    }
    
    public static boolean deleteMessageByMessageId(int messageId) {
        try (Connection connection = ConnectionUtil.getConnection()) {
            String sql = "DELETE FROM Message WHERE message_id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, messageId);

                int affectedRows = preparedStatement.executeUpdate();

                return affectedRows > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Message> retrieveMessagesByUser(int userId) {
        try (Connection connection = ConnectionUtil.getConnection()) {
            String sql = "SELECT * FROM Message WHERE posted_by=?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, userId);
                ResultSet resultSet = preparedStatement.executeQuery();

                List<Message> messages = new ArrayList<>();

                while (resultSet.next()) {
                    int messageId = resultSet.getInt("message_id");
                    String messageText = resultSet.getString("message_text");
                    long timePostedEpoch = resultSet.getLong("time_posted_epoch");

                    messages.add(new Message(messageId, userId, messageText, timePostedEpoch));
                }
                return messages;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
    public static Message getMessageById(int messageId) {
        try (Connection connection = ConnectionUtil.getConnection()) {
            String sql = "SELECT * FROM Message WHERE message_id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, messageId);
    
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    // Create a Message object based on retrieved data
                    Message message = new Message();
                    message.setMessage_id(resultSet.getInt("message_id"));
                    message.setPosted_by(resultSet.getInt("posted_by"));
                    message.setMessage_text(resultSet.getString("message_text"));
                    message.setTime_posted_epoch(resultSet.getLong("time_posted_epoch"));
    
                    return message; // Return the retrieved message object
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null if message retrieval failed or not found
    }

   

    


}

