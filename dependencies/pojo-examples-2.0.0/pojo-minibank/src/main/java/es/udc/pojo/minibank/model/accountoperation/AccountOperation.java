package es.udc.pojo.minibank.model.accountoperation;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Immutable;

import es.udc.pojo.minibank.model.account.Account;

@Entity
@Immutable
@Table(name="AccountOp")
public class AccountOperation {
/**
 * NOTE: this entity class does not contain a "version" property since its
 *       instances are never updated after being persisted.
 */

	public enum Type {ADD, WITHDRAW};

    private Long accountOperationId;
    private Account account;
    private Calendar date;
    private Type type;
    private double amount;

    public AccountOperation() {}

    public AccountOperation(Account account, Calendar date, Type type,
        double amount) {

        /**
         * NOTE: "accountOperationId" *must* be left as "null" since
         * its value is automatically generated.
         */
        this.account = account;
        this.date = date;
        this.type = type;
        this.amount = amount;

    }

    @Column(name="accOpId")
    @SequenceGenerator(                      // It only takes effect
         name="AccountOperationIdGenerator", // for databases providing
         sequenceName="AccountOpSeq")        // identifier generators.
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO,
                    generator="AccountOperationIdGenerator")
    public Long getAccountOperationId() {
        return accountOperationId;
    }

    public void setAccountOperationId(Long accountOperationId) {
        this.accountOperationId = accountOperationId;
    }

    @ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="accId")
    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    @Temporal(TemporalType.TIMESTAMP)
    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

}
