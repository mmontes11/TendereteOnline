package es.udc.pojo.jdbctutorial;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public final class SelectExample {

    public static void main (String[] args) {    

        try (Connection connection = ConnectionManager.getConnection()) {
            
            /* Create "preparedStatement". */
            String queryString = "SELECT accId, balance FROM TutAccount";
            PreparedStatement preparedStatement = connection.prepareStatement(queryString);
            
            /* Execute query. */
            ResultSet resultSet = preparedStatement.executeQuery();
                        
            /* Iterate over matched rows. */
            while (resultSet.next()) {
                String accountIdentifier = resultSet.getString(1);
                double balance = resultSet.getDouble(2);
                System.out.println("accountIdentifier = " + accountIdentifier +                
                    " | balance =  " + balance);
            }
                    
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
                
    }    
    
}
