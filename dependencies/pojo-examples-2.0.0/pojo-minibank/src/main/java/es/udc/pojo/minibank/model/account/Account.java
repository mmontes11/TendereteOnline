package es.udc.pojo.minibank.model.account;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Version;

@Entity
public class Account {

    private Long accountId;
    private Long userId;
    private double balance;
    private long version;

    public Account() {}


    public Account(long userId, double balance) {

        /**
         * NOTE: "accountId" *must* be left as "null" since its value
         * is automatically generated.
         */
        this.userId = userId;
        this.balance = balance;

    }

    @Column(name="accId")
    @SequenceGenerator(             // It only takes effect for
         name="AccountIdGenerator", // databases providing identifier
         sequenceName="AccountSeq") // generators.
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO,
                    generator="AccountIdGenerator")
    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    @Column(name="usrId")
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

    @Version
    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

}
