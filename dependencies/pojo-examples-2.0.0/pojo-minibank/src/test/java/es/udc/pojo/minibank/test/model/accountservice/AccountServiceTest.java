package es.udc.pojo.minibank.test.model.accountservice;

import static es.udc.pojo.minibank.model.util.GlobalNames.SPRING_CONFIG_FILE;
import static es.udc.pojo.minibank.test.util.GlobalNames.SPRING_CONFIG_TEST_FILE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import es.udc.pojo.minibank.model.account.Account;
import es.udc.pojo.minibank.model.accountoperation.AccountOperation;
import es.udc.pojo.minibank.model.accountoperation.AccountOperationDao;
import es.udc.pojo.minibank.model.accountservice.AccountBlock;
import es.udc.pojo.minibank.model.accountservice.AccountService;
import es.udc.pojo.minibank.model.accountservice.InsufficientBalanceException;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { SPRING_CONFIG_FILE, SPRING_CONFIG_TEST_FILE })
@Transactional
public class AccountServiceTest {

    private final long NON_EXISTENT_ACCOUNT_ID = -1;
    private final long NON_EXISTENT_USER_ID = -1;
    private final long NON_EXISTENT_USER_ID2 = -2;

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountOperationDao accountOperationDao;

    @Test
    public void testCreateAccountAndFindAccount()
        throws InstanceNotFoundException {

        Account account = accountService.createAccount(new Account(1, 10));
        Account account2 = accountService.findAccount(account.getAccountId());

        assertEquals(account, account2);

    }

    @Test(expected = InstanceNotFoundException.class)
    public void testFindNonExistentAccount() throws InstanceNotFoundException {

        accountService.findAccount(NON_EXISTENT_ACCOUNT_ID);

    }

    @Test
    public void testAddToAccount() throws InstanceNotFoundException,
            InsufficientBalanceException {

        testAddWithdraw(true);

    }

    @Test(expected = InstanceNotFoundException.class)
    public void testAddToNonExistentAccount() throws InstanceNotFoundException {

        accountService.addToAccount(NON_EXISTENT_ACCOUNT_ID, 10);

    }

    @Test
    public void testWithdrawFromAccount() throws InstanceNotFoundException,
            InsufficientBalanceException {

        testAddWithdraw(false);

    }

    @Test(expected = InstanceNotFoundException.class)
    public void testWithdrawFromNonExistentAccount()
            throws InstanceNotFoundException, InsufficientBalanceException {

        accountService.withdrawFromAccount(NON_EXISTENT_ACCOUNT_ID, 10);

    }

    @Test
    public void testWithdrawWithInsufficientBalance()
            throws InstanceNotFoundException {

        /* Try to withdraw. */
        boolean exceptionCatched = false;
        Account testAccount = createAccount();
        double initialBalance = testAccount.getBalance();

        Calendar startDate = Calendar.getInstance();
        try {
            accountService.withdrawFromAccount(testAccount.getAccountId(),
                    testAccount.getBalance() + 1);
        } catch (InsufficientBalanceException e) {
            exceptionCatched = true;
        }
        Calendar endDate = Calendar.getInstance();

        assertTrue(exceptionCatched);

        /* Check balance has not been modified. */
        testAccount = accountService.findAccount(testAccount.getAccountId());

        assertTrue(testAccount.getBalance() == initialBalance);

        /* Check account operation has not been registered. */
        List<AccountOperation> accountOperations = accountService
                .findAccountOperationsByDate(testAccount.getAccountId(),
                        startDate, endDate, 0, 1);

        assertTrue(accountOperations.size() == 0);

    }

    @Test
    public void testRemoveAccount() throws InstanceNotFoundException {

        boolean exceptionCatched = false;
        Account testAccount = createAccount();

        accountService.removeAccount(testAccount.getAccountId());

        try {
            accountService.findAccount(testAccount.getAccountId());
        } catch (InstanceNotFoundException e) {
            exceptionCatched = true;
        }

        assertTrue(exceptionCatched);

    }

    @Test(expected = InstanceNotFoundException.class)
    public void testRemoveNonExistentAccount() throws InstanceNotFoundException {

        accountService.removeAccount(NON_EXISTENT_ACCOUNT_ID);

    }
    
    @Test
    public void testFindAccountOperationsByDate2()
            throws InstanceNotFoundException {

        double amount = 10;
        int numberOfdaysAgo = 10;
        
        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.DAY_OF_YEAR, -numberOfdaysAgo);

        /* Create the test account and two operations that should not be 
           found. */
        Account testAccount = createAccount();
        createAccountOperation(testAccount,-numberOfdaysAgo-1,
        		AccountOperation.Type.ADD,amount);
        createAccountOperation(testAccount,1,AccountOperation.Type.ADD,amount);
        
        /* Create the operations that should be found (one operation per day
           from 'numberOfdaysAgo' days ago to the current day) and store them 
           in the correct order.  */
        int numberOfOperations = numberOfdaysAgo + 1;
        List<AccountOperation> expectedOperations = 
        	new ArrayList<AccountOperation>(); 
        for (int i = numberOfdaysAgo; i >= 0; i--) {
        	expectedOperations.add(createAccountOperation(
        			testAccount,-i,AccountOperation.Type.ADD,amount));
        }
        
        /* Create another account and add some operations to it. */
        Account otherAccount = createAccount();
        createAccountOperation(otherAccount,0,AccountOperation.Type.ADD,amount);
        createAccountOperation(otherAccount,-1,AccountOperation.Type.ADD,amount);

        Calendar endDate = Calendar.getInstance();

        /* Check operations with correct date range and order. */
        List<AccountOperation> accountOperations;
        int count = numberOfdaysAgo;
        int startIndex = 0;
        int resultIndex = 0;

        do {

            accountOperations = accountService.findAccountOperationsByDate(
                testAccount.getAccountId(), startDate, endDate, startIndex,
                count);
            assertTrue(accountOperations.size() <= count);
            for (AccountOperation accountOperation : accountOperations) {
                assertTrue(accountOperation == 
                	expectedOperations.get(resultIndex++));
            }
            startIndex += count;

        } while (accountOperations.size() == count);

        assertTrue(numberOfOperations == startIndex - count
                + accountOperations.size());

    }
    
    @Test
    public void testFindAccountOperationsByDateWithoutOperations()
            throws InstanceNotFoundException {

        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.DAY_OF_YEAR, -2);
        Calendar endDate = Calendar.getInstance();

        Account testAccount = createAccount();

        List<AccountOperation> accountOperations =
            accountService.findAccountOperationsByDate(
                testAccount.getAccountId(), startDate, endDate, 0, 1);

        assertTrue(accountOperations.size() == 0);

    }

    @Test(expected = InstanceNotFoundException.class)
    public void testFindAccountOperationsFromNonExistentAccount()
            throws InstanceNotFoundException {

        accountService.findAccountOperationsByDate(NON_EXISTENT_ACCOUNT_ID,
                Calendar.getInstance(), Calendar.getInstance(), 0, 1);

    }

    @Test
    public void testGetNumberOfAccountOperations()
            throws InstanceNotFoundException {

        double amount = 10;
        
        int numberOfdaysAgo = 10;        
        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.DAY_OF_YEAR, -numberOfdaysAgo);

        /* Create the test account and two operations that should not be 
           found. */
        Account testAccount = createAccount();
        createAccountOperation(testAccount,-numberOfdaysAgo-1,
        		AccountOperation.Type.ADD,amount);
        createAccountOperation(testAccount,1,AccountOperation.Type.ADD,amount);
        
        /* Create the operations that should be found. One operation per day
           from 'numberOfdaysAgo' days ago to the current day. */
        int numberOfOperations = numberOfdaysAgo + 1;
        for (int i = numberOfdaysAgo; i >=0 ; i--) {
            createAccountOperation(testAccount,-i,
            		AccountOperation.Type.ADD,amount);
        }
        
        /* Create another account and add some operations to it. */
        Account otherAccount = createAccount();
        createAccountOperation(otherAccount,0,AccountOperation.Type.ADD,amount);
        createAccountOperation(otherAccount,-1,AccountOperation.Type.ADD,amount);

        Calendar endDate = Calendar.getInstance();

        /* Check number of operations. */
        int returnedNumber = accountService.getNumberOfAccountOperations(
            testAccount.getAccountId(), startDate, endDate);

        assertTrue(returnedNumber == numberOfOperations);

    }

    @Test
    public void testGetNumberOfAccountOperationsWithoutOperations()
            throws InstanceNotFoundException {

        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.DAY_OF_YEAR, -2);
        Calendar endDate = Calendar.getInstance();

        Account testAccount = createAccount();

        int numberOfOperations = accountService.getNumberOfAccountOperations(
            testAccount.getAccountId(), startDate, endDate);

        assertTrue(numberOfOperations == 0);

    }

    @Test(expected = InstanceNotFoundException.class)
    public void testGetNumberOfAccountOperationsFromNonExistentAccount()
            throws InstanceNotFoundException {

        accountService.getNumberOfAccountOperations(NON_EXISTENT_ACCOUNT_ID,
                Calendar.getInstance(), Calendar.getInstance());

    }

    @Test
    public void testFindAccountsByUserIdWithoutAccounts() {
        AccountBlock accountBlock;

        accountBlock = accountService.findAccountsByUserId(
                NON_EXISTENT_USER_ID, 0, 1);

        assertTrue(accountBlock.getAccounts().size() == 0
                && !accountBlock.getExistMoreAccounts());
    }

    
    @Test
    public void testFindAccountsByUserId() {

        long userId1 = NON_EXISTENT_USER_ID;
        long userId2 = NON_EXISTENT_USER_ID2;        
        double balance = 10;

        /* Create the accounts that should be found and store them in the 
           correct order (we assume that the account identifiers are generated
           in ascending order; in other case the list should be ordered by 
           the account identifiers). */
        int numberOfAccounts = 11;
        List<Account> expectedAccounts = new ArrayList<Account>();
        for (int i = 0; i < numberOfAccounts; i++) {
        	expectedAccounts.add(createAccount(new Account(userId1, balance)));
        }

        /* Create an account that should not be found. */
        createAccount(new Account(userId2, balance));

        AccountBlock accountBlock;
        int count = 10;
        int startIndex = 0;

        /* Check account retrieval. */
        short resultIndex = 0;
        do {

            accountBlock = accountService.findAccountsByUserId(userId1,
                    startIndex, count);
            assertTrue(accountBlock.getAccounts().size() <= count);
            for (Account account : accountBlock.getAccounts()) {
                assertTrue(account == expectedAccounts.get(resultIndex++));
            }
            startIndex += count;

        } while (accountBlock.getExistMoreAccounts());

        assertTrue(numberOfAccounts == startIndex - count
                + accountBlock.getAccounts().size());

    }    
    
    @Test
    public void testTransfer() throws InstanceNotFoundException,
            InsufficientBalanceException {

        /* Perform transference. */
        double initialBalance = 10;
        double amount = 5;
        Account sourceAccount = createAccount(
            new Account(new Long(1), initialBalance));
        Account destinationAccount = createAccount(
            new Account(new Long(1), initialBalance));

        Calendar startDate = Calendar.getInstance();
        accountService.transfer(sourceAccount.getAccountId(),
                destinationAccount.getAccountId(), amount);
        Calendar endDate = Calendar.getInstance();

        sourceAccount = accountService
                .findAccount(sourceAccount.getAccountId());
        destinationAccount = accountService.findAccount(destinationAccount
                .getAccountId());

        /* Check balances. */
        assertTrue(sourceAccount.getBalance() == initialBalance - amount);
        assertTrue(destinationAccount.getBalance() == initialBalance + amount);

        /* Check account operations. */
        List<AccountOperation> sourceAccountOperations =
            accountService.findAccountOperationsByDate(
                sourceAccount.getAccountId(), startDate, endDate, 0, 2);

        assertTrue(sourceAccountOperations.size() == 1);

        AccountOperation sourceAccountOperation =
            sourceAccountOperations.get(0);

        assertTrue(sourceAccountOperation.getAmount() == amount
                && sourceAccountOperation.getType() == AccountOperation.Type.WITHDRAW);

        List<AccountOperation> destinationAccountOperations =
            accountService.findAccountOperationsByDate(
                destinationAccount.getAccountId(), startDate, endDate, 0, 2);

        assertTrue(destinationAccountOperations.size() == 1);

        AccountOperation destinationAccountOperation =
            destinationAccountOperations.get(0);

        assertTrue(destinationAccountOperation.getAmount() == amount
                && destinationAccountOperation.getType() == AccountOperation.Type.ADD);

    }

    @Test
    public void testTransferWithInsufficientBalance()
            throws InstanceNotFoundException, InsufficientBalanceException {

        /* Try to do the transference. */
        double initialBalance = 10;
        Account sourceAccount = createAccount(
            new Account(new Long(1), initialBalance));
        Account destinationAccount = createAccount(
            new Account(new Long(1), initialBalance));
        boolean exceptionCatched = false;

        Calendar startDate = Calendar.getInstance();
        try {
            accountService.transfer(sourceAccount.getAccountId(),
                    destinationAccount.getAccountId(), initialBalance + 1);
        } catch (InsufficientBalanceException e) {
            exceptionCatched = true;
        }
        Calendar endDate = Calendar.getInstance();

        assertTrue(exceptionCatched);

        /* Check balances have not been modified. */
        sourceAccount = accountService
                .findAccount(sourceAccount.getAccountId());
        destinationAccount = accountService.findAccount(destinationAccount
                .getAccountId());

        assertTrue(sourceAccount.getBalance() == initialBalance);
        assertTrue(destinationAccount.getBalance() == initialBalance);

        /* Check account operations have not been registered. */
        List<AccountOperation> sourceAccountOperations = accountService
                .findAccountOperationsByDate(sourceAccount.getAccountId(),
                        startDate, endDate, 0, 1);

        assertTrue(sourceAccountOperations.size() == 0);

        List<AccountOperation> destinationAccountOperations = accountService
                .findAccountOperationsByDate(destinationAccount.getAccountId(),
                        startDate, endDate, 0, 1);

        assertTrue(destinationAccountOperations.size() == 0);

    }

    @Test(expected = InstanceNotFoundException.class)
    public void testTransferFromNonExistentAccount()
            throws InstanceNotFoundException, InsufficientBalanceException {

        Account testAccount = createAccount();

        accountService.transfer(NON_EXISTENT_ACCOUNT_ID,
            testAccount.getAccountId(), 1);

    }

    @Test(expected = InstanceNotFoundException.class)
    public void testTransferToNonExistentAccount()
            throws InstanceNotFoundException, InsufficientBalanceException {

        Account testAccount = createAccount();

        accountService.transfer(testAccount.getAccountId(),
            NON_EXISTENT_ACCOUNT_ID, 1);

    }

    /**
     * It creates and saves a valid account with a positive balance (greater
     * than 0). Caller methods must not assume a particular value for the
     * balance (other than being positive).
     */
    private Account createAccount() {
        return accountService.createAccount(new Account(new Long(1), 10));
    }

    private Account createAccount(Account account) {
        return accountService.createAccount(account);
    }

    /**
     * It creates and saves an account operation using the DAO. Caller methods
     * must assume that the balance of the account is not modified.
     * @param account Account for which the operation will be created
     * @param daysAfter Number of days that will be added to the current date
     *  to calculate the account operation date 
     * @param type Operation type
     * @param amount Operation amount
     */
    private AccountOperation createAccountOperation(Account account, 
    		int daysToAdd, AccountOperation.Type type, double amount) { 
        Calendar date = Calendar.getInstance();
        date.add(Calendar.DAY_OF_YEAR, daysToAdd);
        AccountOperation accountOp = new AccountOperation(
        		account,date,type,amount); 
        accountOperationDao.save(accountOp);
        return accountOp;
    }
    
    private void testAddWithdraw(boolean add) throws InstanceNotFoundException,
            InsufficientBalanceException {

        /* Perform operation. */
        double amount = 5;
        double newBalance;
        Account testAccount = createAccount();

        if (add) {
            newBalance = testAccount.getBalance() + amount;
        } else {
            newBalance = testAccount.getBalance() - amount;
        }

        Calendar startDate = Calendar.getInstance();
        if (add) {
            accountService.addToAccount(testAccount.getAccountId(), amount);
        } else {
            accountService.withdrawFromAccount(testAccount.getAccountId(),
                    amount);
        }
        Calendar endDate = Calendar.getInstance();

        /* Check new balance. */
        testAccount = accountService.findAccount(testAccount.getAccountId());

        assertTrue(newBalance == testAccount.getBalance());

        /* Check account operation. */
        List<AccountOperation> accountOperations = accountService
                .findAccountOperationsByDate(testAccount.getAccountId(),
                        startDate, endDate, 0, 2);

        assertTrue(accountOperations.size() == 1);

        AccountOperation accountOperation = accountOperations.get(0);

        if (add) {
            assertEquals(AccountOperation.Type.ADD, accountOperation.getType());
        } else {
            assertEquals(AccountOperation.Type.WITHDRAW, accountOperation
                    .getType());
        }

        assertTrue(amount == accountOperation.getAmount());

    }

}