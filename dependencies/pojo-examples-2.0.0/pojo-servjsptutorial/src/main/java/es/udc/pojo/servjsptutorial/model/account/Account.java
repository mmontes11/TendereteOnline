package es.udc.pojo.servjsptutorial.model.account;

public class Account {

    private Long accountId;
    private Long userId;
    private double balance;

    public Account() {}


    public Account(long userId, double balance) {

        /**
         * NOTE: "accountId" *must* be left as "null" since its value
         * is automatically generated.
         */
        this.userId = userId;
        this.balance = balance;

    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public boolean equals(Object obj) {

        if ((obj==null) || !(obj instanceof Account)) {
            return false;
        }

        Account theOther = (Account) obj;

        return accountId.equals(theOther.accountId) &&
        	userId.equals(theOther.userId) &&
            balance==theOther.balance;
    }

}
