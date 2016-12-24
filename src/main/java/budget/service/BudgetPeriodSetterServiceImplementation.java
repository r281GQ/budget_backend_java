package budget.service;

import budget.model.Budget;
import budget.model.BudgetPeriod;
import budget.model.Transaction;
import budget.repository.interfaces.BudgetPeriodRepository;
import budget.repository.interfaces.BudgetRepository;
import budget.repository.interfaces.TransactionRepository;
import budget.service.interfaces.BudgetPeriodSetterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

/**
 * Created by veghe on 05/12/2016.
 */
@Service
@Transactional
public class BudgetPeriodSetterServiceImplementation implements BudgetPeriodSetterService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private BudgetPeriodRepository budgetPeriodRepository;

    @Autowired
    private BudgetRepository budgetRepository;

    @Override
    public void updateBudgetPeriodOnTransactionCreation(Transaction transaction) {
        setBudgetPeriod(transaction);
        calculateBudgetPeriodBalances(transaction.getBudget());
    }

    @Override
    public void updateBudgetPeriodOnTransactionDeletion(Transaction transaction) {
        calculateBudgetPeriodBalances(transaction.getBudget());
    }

    @Override
    public void updateBudgetPeriodOnBudgetUpdate(Budget budget) {
        for (BudgetPeriod budgetPeriod : budgetPeriodRepository.findByBudget(budget)) {
            budgetPeriod.setName(budget.getName() + " " + budgetPeriod.getPeriod().getRepresentation());
            budgetPeriod.setAllowance(budget.getDefaultAllowance());
            budgetPeriodRepository.update(budgetPeriod);
        }
        calculateBudgetPeriodBalances(budget);
    }

    @Override
    public void updateBudgetPeriodOnBudgetUpdateWithoutReCalculation(Budget budget) {
        for (BudgetPeriod budgetPeriod : budgetPeriodRepository.findByBudget(budget)) {
            budgetPeriod.setName(budget.getName() + " " + budgetPeriod.getPeriod().getRepresentation());
            budgetPeriodRepository.update(budgetPeriod);
        }
    }

    @Override
    public void updateBudgetPeriod(BudgetPeriod budgetPeriod) {
        if (budgetPeriodRepository.get(budgetPeriod.getIdentifier()).getAllowance().doubleValue() != budgetPeriod.getAllowance().doubleValue())
            calculateBudgetPeriodBalances(budgetPeriod.getBudget());
        budgetPeriodRepository.update(budgetPeriod);
    }

    private void setBudgetPeriod(Transaction transaction) {
        BudgetPeriod budgetPeriod = budgetPeriodRepository.findByBudgetAndPeriod(transaction.getBudget(), transaction.getPeriod());

        transaction.setBudgetPeriod(budgetPeriod == null ? createBudgetPeriodTemplate(transaction) : budgetPeriod);
        transactionRepository.update(transaction);
    }

    private BudgetPeriod createBudgetPeriodTemplate(Transaction transaction) {
        BudgetPeriod budgetPeriod = assignValues(transaction);

        budgetPeriodRepository.create(budgetPeriod);
        budgetPeriod = budgetPeriodRepository.findByBudgetAndPeriod(transaction.getBudget(), transaction.getPeriod());

        return budgetPeriod;
    }

    private BudgetPeriod assignValues(Transaction transaction) {
        BudgetPeriod budgetPeriod = new BudgetPeriod();
        budgetPeriod.setName(transaction.getBudget().getName() + " " + transaction.getPeriod().getRepresentation());
        budgetPeriod.setBudget(transaction.getBudget());
        budgetPeriod.setUser(transaction.getBudget().getUser());
        budgetPeriod.setPeriod(transaction.getPeriod());
        budgetPeriod.setAllowance(transaction.getBudget().getDefaultAllowance());
        budgetPeriod.setBalance(budgetPeriod.getAllowance());
        return budgetPeriod;
    }

    private void calculateBudgetPeriodBalances(Budget budget) {
        List<BudgetPeriod> orderedBudgetPeriods = budgetPeriodRepository.findByBudgetOrderedByDate(budget);
        calculateBalanceForEachBPs(assignTransactionsToCorrespondingBPs(orderedBudgetPeriods));
    }

    private void calculateBalanceForEachBPs(NavigableMap<BudgetPeriod, List<Transaction>> budgetPeriodTransactionListDictionary) {
        BigDecimal leftOver = new BigDecimal(0);

        for (Map.Entry<BudgetPeriod, List<Transaction>> entry : budgetPeriodTransactionListDictionary.entrySet()) {
            BigDecimal balance = entry.getKey().getAllowance().add(leftOver).subtract(calculateSumOfTransactions(entry));
            setBalanceForCurrentBudgetPeriod(entry, balance);
            leftOver = balance;
            budgetPeriodRepository.update(entry.getKey());
        }
    }

    private void setBalanceForCurrentBudgetPeriod(Map.Entry<BudgetPeriod, List<Transaction>> entry, BigDecimal balance) {
        entry.getKey().setBalance(balance);
    }

    private BigDecimal calculateSumOfTransactions(Map.Entry<BudgetPeriod, List<Transaction>> dictionaryEntry) {
        BigDecimal sumOfAmountOfTransactions = new BigDecimal(0);

        for (Transaction transaction : dictionaryEntry.getValue()) {
            sumOfAmountOfTransactions = (transaction.getAmountAtTheMomentOfTransactionForBudget().add(sumOfAmountOfTransactions));
        }
        return sumOfAmountOfTransactions;
    }

    private NavigableMap<BudgetPeriod, List<Transaction>> assignTransactionsToCorrespondingBPs(List<BudgetPeriod> orderedBudgetPeriods) {
        NavigableMap<BudgetPeriod, List<Transaction>> budgetPeriodTransactionListDictionary = new TreeMap<>();

        for (BudgetPeriod budgetPeriod : orderedBudgetPeriods) {
            List<Transaction> transactionList = transactionRepository.findBudgetPeriod(budgetPeriod);
            budgetPeriodTransactionListDictionary.put(budgetPeriod, transactionList);
        }
        return budgetPeriodTransactionListDictionary;
    }
}
