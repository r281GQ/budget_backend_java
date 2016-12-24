package budget.service.interfaces;

import budget.controller.exceptions.InvalidDataProvidedException;
import budget.model.Transaction;

/**
 * Created by veghe on 04/12/2016.
 */
public interface AccountSetterService {
    void updateAccountOnTransactionCreation(Transaction transaction) throws InvalidDataProvidedException;

    void updateAccountOnTransactionDeletion(Transaction transaction) throws InvalidDataProvidedException;
}
