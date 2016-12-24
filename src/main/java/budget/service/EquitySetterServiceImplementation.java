package budget.service;

import budget.controller.exceptions.InvalidDataProvidedException;
import budget.model.EQType;
import budget.model.Transaction;
import budget.model.Type;
import budget.repository.interfaces.EquityRepository;
import budget.service.interfaces.DefaultValueProviderService;
import budget.service.interfaces.EquitySetterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by veghe on 04/12/2016.
 */
@Service
public class EquitySetterServiceImplementation implements EquitySetterService {

    @Autowired
    private EquityRepository equityRepository;

    @Autowired
    private DefaultValueProviderService defaultValueProviderService;

    @Override
    public void updateEquityOnTransactionCreation(final Transaction transaction) {
        if (isExpenseANDLiabilityORAssetANDIncome(transaction)) {
            balanceCheck(transaction);
            subtractFromEquity(transaction);
        } else
            addToEquity(transaction);
        equityRepository.update(transaction.getEquity());
    }

    @Override
    public void updateEquityOnTransactionDeletion(final Transaction transaction) {
        if (!isExpenseANDLiabilityORAssetANDIncome(transaction)) {
            balanceCheck(transaction);
            subtractFromEquity(transaction);
        } else
            addToEquity(transaction);
        equityRepository.update(transaction.getEquity());
    }

    private void balanceCheck(Transaction transaction) {
        if (transaction.getAmountAtTheMomentOfTransactionForEQ().doubleValue() > transaction.getEquity().getBalance().doubleValue())
            throw new InvalidDataProvidedException(defaultValueProviderService.getExceptionMessages(InvalidType.INSUFFICIENT_BALANCE), transaction.getEquity());
    }

    private boolean isExpenseANDLiabilityORAssetANDIncome(Transaction transaction) {
        return isExpenseAndLiability(transaction) || isIncomeAndAsset(transaction);
    }

    private boolean isIncomeAndAsset(Transaction transaction) {
        return !isLiability(transaction) && !isTransactionExpense(transaction);
    }

    private boolean isExpenseAndLiability(Transaction transaction) {
        return isLiability(transaction) && isTransactionExpense(transaction);
    }

    private boolean isTransactionExpense(Transaction transaction) {
        return transaction.getGrouping().getType().equals(Type.EXPENSE);
    }

    private boolean isLiability(Transaction transaction) {
        return transaction.getEquity().getType().equals(EQType.LIABILITY);
    }

    private void addToEquity(Transaction transaction) {
        transaction.getEquity().setBalance(transaction.getEquity().getBalance().add(transaction.getAmountAtTheMomentOfTransactionForEQ()));
    }

    private void subtractFromEquity(Transaction transaction) {
        transaction.getEquity().setBalance(transaction.getEquity().getBalance().subtract(transaction.getAmountAtTheMomentOfTransactionForEQ()));
    }
}
