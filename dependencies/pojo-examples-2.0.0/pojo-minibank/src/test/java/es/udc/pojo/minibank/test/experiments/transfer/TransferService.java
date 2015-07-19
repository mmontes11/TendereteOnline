package es.udc.pojo.minibank.test.experiments.transfer;

import es.udc.pojo.minibank.model.accountservice.InsufficientBalanceException;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

public interface TransferService {

    public void transfer(long sourceAccountId,
        long destinationAccountId, double amount)
        throws InstanceNotFoundException, InsufficientBalanceException;
               
}
