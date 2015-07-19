package es.udc.pojo.minibank.model.accountservice;

@SuppressWarnings("serial")
public class InsufficientBalanceException extends Exception {

    private long accountIdentifier;
    private double currentBalance;
    private double amountToWithdraw;

    public InsufficientBalanceException(long accountIdentifier,
        double currentBalance, double amountToWithdraw) {
        
        super("Insufficient balance exception => " +
            "accountIdentifier = " + accountIdentifier + " | " +
            "currentBalance = " + currentBalance + " | " +
            "amountToWithdraw = " + amountToWithdraw);
            
        this.accountIdentifier = accountIdentifier;
        this.currentBalance = currentBalance;
        this.amountToWithdraw = amountToWithdraw;
        
    }
    
    public long getAccountIdentifier() {
        return accountIdentifier;
    }
    
    public double getCurrentBalance() {
        return currentBalance;
    }
    
    public double getAmountToWithdraw() {
        return amountToWithdraw;
    }

}
