package es.udc.pojo.jdbctutorial;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public final class InsertExample {

    public static void main (String[] args) {    
            
        try (Connection connection = ConnectionManager.getConnection()) {
            
            /* Create data for some accounts. */
            String[] accountIdentifiers = new String[] {"user-1", "user-2", 
            	"user-3"};
            double[] balances = new double[] {100.0, 200.0, 300.0};
            
            /* Create "preparedStatement". */
            String queryString = "INSERT INTO TutAccount " +
                "(accId, balance) VALUES (?, ?)";                    
            PreparedStatement preparedStatement = 
                connection.prepareStatement(queryString);
            
            /* Insert the accounts in database. */
            for (int i=0; i<accountIdentifiers.length; i++) {
                
                /* Fill "preparedStatement". */    
                preparedStatement.setString(1, accountIdentifiers[i]);
                preparedStatement.setDouble(2, balances[i]);

                /* Execute query. */                    
                int insertedRows = preparedStatement.executeUpdate();
                
                if (insertedRows != 1) {
                    throw new SQLException(accountIdentifiers[i] + 
                        ": problems when inserting !!!!");
                }
                
            }
            
            System.out.println("Accounts inserted");
        
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
                
    }    
    
}
