package budget.service;

import budget.accessories.validators.ValidationService;
import budget.controller.exceptions.InvalidDataProvidedException;
import budget.controller.exceptions.ResourceNotFoundException;
import budget.model.*;
import budget.repository.interfaces.TransactionRepository;
import budget.service.interfaces.CrossEffectManager;
import budget.service.interfaces.DefaultValueProviderService;
import budget.service.interfaces.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by veghe on 14/08/2016.
 */
@Service
@Transactional
public class TransactionServiceImplementation implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private CrossEffectManager crossEffectManager;

    @Autowired
    private ValidationService validationService;

    @Autowired
    private DefaultValueProviderService defaultValueProviderService;

    @Override
    public void create(Transaction transaction) {
        if(transaction == null){
            transaction = new Transaction();
//            transaction.setIdentifier(0L);
            throw new InvalidDataProvidedException(defaultValueProviderService.getExceptionMessages(InvalidType.NO_CONTENT_PROVIDED), transaction);
        }
        if (transaction.getIdentifier() != null)
            throw new InvalidDataProvidedException(defaultValueProviderService.getExceptionMessages(InvalidType.ID_PRESENT_ON_CREATION), transaction);
        crossEffectManager.createTransaction(transaction);
    }

    @Override
    public void update(Transaction transaction) {
        if(transaction == null){
            transaction = new Transaction();
            transaction.setIdentifier(0L);
            throw new ResourceNotFoundException(transaction);
        }
        Transaction intermediate = transactionRepository.get(transaction.getIdentifier());
        if (intermediate == null)
            throw new ResourceNotFoundException(transaction);
        transaction.setCreationDate(intermediate.getCreationDate());

        Transaction forDeletion = new Transaction();
        forDeletion.setIdentifier(transaction.getIdentifier());

        if(!validationService.isUpdateAble(transaction))
            throw new InvalidDataProvidedException(defaultValueProviderService.getExceptionMessages(InvalidType.INVALIDATION_TRANSACTION), transaction);

        crossEffectManager.deleteTransaction(forDeletion);
        transaction.setIdentifier(null);
        crossEffectManager.createTransaction(transaction);
    }

    @Override
    public List<Transaction> getByUser(User user) {
        return transactionRepository.findByUser(user);
    }

    @Override
    public Transaction get(long id) {
        Transaction transaction = transactionRepository.get(id);
        if (transaction == null) {
            transaction = new Transaction();
            transaction.setIdentifier(id);
            throw new ResourceNotFoundException(transaction);
        }
        return transaction;
    }

    @Override
    public List<Transaction> getByAccount(Account account) {
        return transactionRepository.findByAccount(account);
    }

    @Override
    public List<Transaction> getByEquity(Equity equity) {
        return transactionRepository.findByEquity(equity);
    }

    @Override
    public List<Transaction> getByGrouping(Grouping grouping) {
        return transactionRepository.findByGrouping(grouping);
    }

    @Override
    public List<Transaction> getByBudget(Budget budget) {
        return transactionRepository.findByBudget(budget);
    }

    @Override
    public List<Transaction> getByBudgetPeriod(BudgetPeriod budgetPeriod) {
        return transactionRepository.findBudgetPeriod(budgetPeriod);
    }

    @Override
    public void delete(Transaction transaction) {
        if(transaction == null){
            transaction = new Transaction();
            transaction.setIdentifier(0L);
            throw new ResourceNotFoundException(transaction);
        }
        if (transactionRepository.get(transaction.getIdentifier()) == null)
            throw new ResourceNotFoundException(transaction);
        crossEffectManager.deleteTransaction(transaction);
    }
}

