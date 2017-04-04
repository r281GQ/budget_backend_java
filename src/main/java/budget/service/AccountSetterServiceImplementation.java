package budget.service;

import budget.controller.exceptions.InvalidDataProvidedException;
import budget.model.Transaction;
import budget.model.Type;
import budget.repository.interfaces.AccountRepository;
import budget.service.interfaces.AccountSetterService;
import budget.service.interfaces.DefaultValueProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by veghe on 04/12/2016.
 */
@Service
public class AccountSetterServiceImplementation implements AccountSetterService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private DefaultValueProviderService defaultValueProviderService;

    @Override
    public void updateAccountOnTransactionCreation(final Transaction transaction) {
        if (isIncome(transaction)) {
            addAmount(transaction);
        } else {
            balanceCheck(transaction);
            subtractAmount(transaction);
        }
        accountRepository.update(transaction.getAccount());
    }

    @Override
    public void updateAccountOnTransactionDeletion(final Transaction transaction) {
        if (!isIncome(transaction)) {
            addAmount(transaction);
        } else {
            balanceCheck(transaction);
            subtractAmount(transaction);
        }
        accountRepository.update(transaction.getAccount());
    }

    private void balanceCheck(final Transaction transaction) {
        if (transaction.getAmountAtTheMomentOfTransactionForAccount().doubleValue() > transaction.getAccount().getBalance().doubleValue())
            throw new InvalidDataProvidedException(defaultValueProviderService.getExceptionMessages(InvalidType.INSUFFICIENT_BALANCE), transaction.getAccount());
    }

    private void subtractAmount(final Transaction transaction) {
        transaction.getAccount().setBalance(transaction.getAccount().getBalance().subtract(transaction.getAmountAtTheMomentOfTransactionForAccount()));
    }

    private void addAmount(final Transaction transaction) {
        transaction.getAccount().setBalance(transaction.getAccount().getBalance().add(transaction.getAmountAtTheMomentOfTransactionForAccount()));
    }

    private boolean isIncome(final Transaction transaction) {
        return transaction.getGrouping().getType().equals(Type.INCOME);
    }
}
