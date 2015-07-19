package es.udc.pojo.minibank.test.experiments.spring;

import static es.udc.pojo.minibank.model.util.GlobalNames.SPRING_CONFIG_FILE;
import static es.udc.pojo.minibank.test.util.GlobalNames.SPRING_CONFIG_TEST_FILE;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import es.udc.pojo.minibank.model.accountservice.AccountService;
import es.udc.pojo.minibank.model.accountservice.InsufficientBalanceException;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

public class WithdrawFromAccount {

    private final static String USAGE_MESSAGE = "Usage: WithdrawFromAccount <accountId:long> <amount:double>";

    public static void main(String[] args) {

        long accountId = -1;
        double amount = -1;
        if (args.length == 2) {
            try {
                accountId = Long.parseLong(args[0]);
                amount = Double.parseDouble(args[1]);
            } catch (NumberFormatException e) {
                System.err.println("Error: " + USAGE_MESSAGE);
                System.exit(-1);
            }
        } else {
            System.err.println("Error: " + USAGE_MESSAGE);
            System.exit(-1);
        }

        try {

            /* Get service object. */
            ApplicationContext ctx = new ClassPathXmlApplicationContext(
                new String[] {SPRING_CONFIG_FILE, SPRING_CONFIG_TEST_FILE});
            AccountService accountService = ctx.getBean(AccountService.class);

            accountService.withdrawFromAccount(accountId, amount);

        } catch (InstanceNotFoundException e) {
            e.printStackTrace();
        } catch (InsufficientBalanceException e) {
            e.printStackTrace();
        }
    }

}
