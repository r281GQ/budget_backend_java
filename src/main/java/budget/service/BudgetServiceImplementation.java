package budget.service;

import budget.accessories.validators.ValidationService;
import budget.controller.exceptions.InvalidDataProvidedException;
import budget.controller.exceptions.ResourceNotFoundException;
import budget.model.Budget;
import budget.model.User;
import budget.repository.interfaces.BudgetRepository;
import budget.service.interfaces.BudgetService;
import budget.service.interfaces.CrossEffectManager;
import budget.service.interfaces.DefaultValueProviderService;
import budget.service.interfaces.DeleteHelperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by veghe on 11/09/2016.
 */

@Service
@Transactional
public class BudgetServiceImplementation implements BudgetService {

    @Autowired
    private DeleteHelperService deleteHelperService;

    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private ValidationService validationService;

    @Autowired
    private CrossEffectManager crossEffectManager;

    @Autowired
    private DefaultValueProviderService defaultValueProviderService;

    @Override
    public Budget getById(long id) {
        Budget budget = budgetRepository.get(id);
        if (budget == null) {
            budget = new Budget();
            budget.setIdentifier(id);
            throw new ResourceNotFoundException(budget);
        }
        return budget;
    }

    @Override
    public void delete(Budget budget) {
        if (budget == null) {
            budget = new Budget();
            budget.setIdentifier(0L);
            throw new ResourceNotFoundException(budget);
        }
        if (budget.getIdentifier() == null || budgetRepository.get(budget.getIdentifier()) == null)
            throw new ResourceNotFoundException(budget);
        deleteHelperService.deleteBudget(budget);
    }

    @Override
    public void create(Budget budget) {
        if (budget == null) {
            budget = new Budget();
            budget.setIdentifier(0L);
            throw new InvalidDataProvidedException(defaultValueProviderService.getExceptionMessages(InvalidType.NO_CONTENT_PROVIDED), budget);
        }
        if (budget.getIdentifier() != null)
            throw new InvalidDataProvidedException(defaultValueProviderService.getExceptionMessages(InvalidType.ID_PRESENT_ON_CREATION), budget);

        setUser(budget);

        budgetRepository.create(budget);
    }

    private void setUser(Budget budget) {
        crossEffectManager.getUserById(budget.getUser().getIdentifier());
    }

    @Override
    public void update(Budget budget) {
        setUser(budget);
        processUpdate(budget);

        crossEffectManager.updateBudget(budget);
    }

    @Override
    public void updateWithoutReCalculation(Budget budget) throws InvalidDataProvidedException {
        processUpdate(budget);

        crossEffectManager.updateBudgetWithoutReCalculation(budget);
    }

    @Override
    public List<Budget> getByUser(User user) {
        return budgetRepository.findByUser(user);
    }

    private void processUpdate(Budget budget) {
        if (budget.getIdentifier() == null || getById(budget.getIdentifier()) == null)
            throw new ResourceNotFoundException(budget);

        if (!validationService.isUpdateAble(budget))
            throw new InvalidDataProvidedException(defaultValueProviderService.getExceptionMessages(InvalidType.INVALIDATION_BUDGET), budget);
    }
}
