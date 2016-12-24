package budget.service.interfaces;

import budget.controller.exceptions.InvalidDataProvidedException;
import budget.model.Transaction;

/**
 * Created by veghe on 04/12/2016.
 */
public interface EquitySetterService {
    void updateEquityOnTransactionCreation(Transaction transaction) throws InvalidDataProvidedException;

    void updateEquityOnTransactionDeletion(Transaction transaction) throws InvalidDataProvidedException;
}


