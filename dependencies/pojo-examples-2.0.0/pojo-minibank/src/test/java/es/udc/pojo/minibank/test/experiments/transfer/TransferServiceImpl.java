package es.udc.pojo.minibank.test.experiments.transfer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.pojo.minibank.model.account.Account;
import es.udc.pojo.minibank.model.account.AccountDao;
import es.udc.pojo.minibank.model.accountservice.InsufficientBalanceException;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@Service("transferService")
@Transactional
public class TransferServiceImpl implements TransferService {

    @Autowired
	private AccountDao accountDao;

    public void transfer(long sourceAccountId,
        long destinationAccountId, double amount)
        throws InstanceNotFoundException, InsufficientBalanceException {

        /* Read balances. */
        System.out.println("*** Before reading balance for account " +
            sourceAccountId + " ***");
        waitForIntro();
        Account sourceAccount = accountDao.find(sourceAccountId);

        System.out.println("*** Before reading balance for account " +
            destinationAccountId + " ***");
        waitForIntro();
        Account destinationAccount = accountDao.find(destinationAccountId);

        System.out.println("*** Balances have been read ***");
        System.out.println(sourceAccountId + " -> balance = " +
        	sourceAccount.getBalance());
        System.out.println(destinationAccountId + " -> balance = " +
        	destinationAccount.getBalance());

        /* Check if transfer is possible. */
        if (sourceAccount.getBalance() < amount) {
            throw new InsufficientBalanceException(sourceAccountId,
            	sourceAccount.getBalance(), amount);
        }

        /* Compute new balances. */
        double sourceAccountNewBalance = sourceAccount.getBalance() - amount;
        double destinationAccountNewBalance =
        	destinationAccount.getBalance() + amount;

        /* Update accounts. */
        System.out.println("*** Before udpating balances ***");
        System.out.println(sourceAccountId + " -> newBalance = " +
            sourceAccountNewBalance);
        System.out.println(destinationAccountId +
            " -> newBalance = " + destinationAccountNewBalance);
        waitForIntro();

        sourceAccount.setBalance(sourceAccountNewBalance);
        destinationAccount.setBalance(destinationAccountNewBalance);

    }

    private static void waitForIntro() {

        System.out.println("Press <intro> to continue");

        try {

	        BufferedReader bufferedReader =
	            new BufferedReader(new InputStreamReader(System.in));
	        bufferedReader.readLine();

        } catch (IOException e) {
        	throw new RuntimeException(e);
        }


    }


}
