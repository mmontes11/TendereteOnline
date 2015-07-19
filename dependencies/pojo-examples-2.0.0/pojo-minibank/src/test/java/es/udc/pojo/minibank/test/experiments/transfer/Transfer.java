package es.udc.pojo.minibank.test.experiments.transfer;

import static es.udc.pojo.minibank.model.util.GlobalNames.SPRING_CONFIG_FILE;
import static es.udc.pojo.minibank.test.util.GlobalNames.SPRING_CONFIG_TEST_FILE;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import es.udc.pojo.minibank.model.accountservice.AccountService;

/**
 * This class allows to experiment with concurrent transactions by using a
 * bank transference as an example. The <code>main</code> method requires two
 * account identifiers as arguments. The accounts must exist in the database
 * and their balance must be greater or equal than 10. The transference only
 * modifies account balances (it does not create any account operation).
 */

public class Transfer {

    public static void main(String[] args) {

       try {

            /* Get service objects. */
            ApplicationContext ctx = new ClassPathXmlApplicationContext(
                new String[] {SPRING_CONFIG_FILE, SPRING_CONFIG_TEST_FILE});
            AccountService accountService = ctx.getBean(AccountService.class);
            TransferService transferService =
                ctx.getBean(TransferService.class);

            /* Get account identifiers. */
            long sourceAccountId = new Long(args[0]);
            long destinationAccountId = new Long(args[1]);

            /* Make transference. */
            transferService.transfer(sourceAccountId, destinationAccountId, 10);

            /* Print accounts. */
            System.out.println("*** After updating balances ****");
            System.out.println(sourceAccountId + " -> balance = " +
                accountService.findAccount(sourceAccountId).getBalance());
            System.out.println(destinationAccountId + " -> balance = " +
                accountService.findAccount(destinationAccountId).getBalance());

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
