package budget.service;

import budget.model.Transaction;
import budget.service.interfaces.CurrencySetterService;
import budget.service.interfaces.ExchangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Created by veghe on 06/12/2016.
 */
@Service
public class CurrencySetterServiceImplementation implements CurrencySetterService {

    @Autowired
    private ExchangeService exchangeService;

    @Override
    public void setValues(Transaction transaction) {
        transaction.setAmountAtTheMomentOfTransactionForAccount(calculateAgainstAccount(transaction));
        if (hasEquity(transaction))
            transaction.setAmountAtTheMomentOfTransactionForEQ(calculateAgainstEquity(transaction));
        if (hasBudget(transaction))
            transaction.setAmountAtTheMomentOfTransactionForBudget(calculateAgainstBudget(transaction));
    }

    private boolean hasBudget(Transaction transaction) {
        return transaction.getBudget() != null;
    }

    private BigDecimal calculateAgainstBudget(Transaction transaction) {
        return transaction.getCurrency().equals(transaction.getBudget().getCurrency()) ? transaction.getAmount() : transaction.getAmount().multiply(exchangeService.getRate("" + transaction.getCurrency() + transaction.getBudget().getCurrency()));
    }

    private boolean hasEquity(Transaction transaction) {
        return transaction.getEquity() != null;
    }

    private BigDecimal calculateAgainstEquity(Transaction transaction) {
        return transaction.getCurrency().equals(transaction.getEquity().getCurrency()) ? transaction.getAmount() : transaction.getAmount().multiply(exchangeService.getRate("" + transaction.getCurrency() + transaction.getEquity().getCurrency()));
    }

    private BigDecimal calculateAgainstAccount(Transaction transaction) {
        return transaction.getCurrency().equals(transaction.getAccount().getCurrency()) ? transaction.getAmount() : transaction.getAmount().multiply(exchangeService.getRate("" + transaction.getCurrency() + transaction.getAccount().getCurrency()));
    }
}
