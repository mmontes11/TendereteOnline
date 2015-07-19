package es.udc.pojo.minibank.model.accountservice;

import java.util.List;

import es.udc.pojo.minibank.model.account.Account;

public class AccountBlock {

    private List<Account> accounts;
    private boolean existMoreAccounts;

    public AccountBlock(List<Account> accounts, boolean existMoreAccounts) {
        
        this.accounts = accounts;
        this.existMoreAccounts = existMoreAccounts;

    }
    
    public List<Account> getAccounts() {
        return accounts;
    }
    
    public boolean getExistMoreAccounts() {
        return existMoreAccounts;
    }
    
}
