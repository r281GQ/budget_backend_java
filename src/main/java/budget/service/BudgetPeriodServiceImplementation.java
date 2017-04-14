package budget.service;

import budget.accessories.validators.ValidationService;
import budget.controller.exceptions.InvalidDataProvidedException;
import budget.controller.exceptions.ResourceNotFoundException;
import budget.model.Budget;
import budget.model.BudgetPeriod;
import budget.model.Period;
import budget.model.User;
import budget.repository.interfaces.BudgetPeriodRepository;
import budget.service.interfaces.BudgetPeriodService;
import budget.service.interfaces.CrossEffectManager;
import budget.service.interfaces.DefaultValueProviderService;
import budget.service.interfaces.DeleteHelperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by veghe on 18/08/2016.
 */
@Service
@Transactional
public class BudgetPeriodServiceImplementation implements BudgetPeriodService {

    @Autowired
    private BudgetPeriodRepository budgetPeriodRepository;

    @Autowired
    private DeleteHelperService deleteHelperService;

    @Autowired
    private ValidationService validationService;

    @Autowired
    private CrossEffectManager crossEffectManager;

    @Autowired
    private DefaultValueProviderService defaultValueProviderService;

    @Autowired
    private SimpleDateFormat df;

    @Override
    public BudgetPeriod getById(long id) {
        BudgetPeriod budgetPeriod = budgetPeriodRepository.get(id);
        if (budgetPeriod == null) {
            budgetPeriod = new BudgetPeriod();
            budgetPeriod.setIdentifier(id);
            throw new ResourceNotFoundException(budgetPeriod);
        }
        return budgetPeriod;
    }

    @Override
    public void update(BudgetPeriod budgetPeriod) {
        if (budgetPeriod == null) {
            budgetPeriod = new BudgetPeriod();
            budgetPeriod.setIdentifier(0L);
            throw new ResourceNotFoundException(budgetPeriod);
        }

        if (budgetPeriod.getIdentifier() == null || budgetPeriodRepository.get(budgetPeriod.getIdentifier()) == null)
            throw new ResourceNotFoundException(budgetPeriod);

        setUser(budgetPeriod);
        setBudget(budgetPeriod);

        budgetPeriod.setPeriod(((budgetPeriodRepository.get(budgetPeriod.getIdentifier()).getPeriod())));

        if (!validationService.isUpdateAble(budgetPeriod))
            throw new InvalidDataProvidedException(defaultValueProviderService.getExceptionMessages(InvalidType.INVALIDATION_BUDGETPERIOD), budgetPeriod);
        crossEffectManager.updateBudgetPeriod(budgetPeriod);
    }

    private void setBudget(BudgetPeriod budgetPeriod) {
        budgetPeriod.setUser(crossEffectManager.getUserById(budgetPeriod.getUser().getIdentifier()));
    }

    private void setUser(BudgetPeriod budgetPeriod) {
        budgetPeriod.setBudget(crossEffectManager.getBudgetById(budgetPeriod.getBudget().getIdentifier()));
    }

    @Override
    public List<BudgetPeriod> getByBudget(Budget budget) {
        return budgetPeriodRepository.findByBudget(budget);
    }

    @Override
    public List<BudgetPeriod> getByUser(User user) {
        return budgetPeriodRepository.findByUser(user);
    }

    @Override
    public BudgetPeriod getByBudgetAndPeriod(Budget budget, Period period) {
        return budgetPeriodRepository.findByBudgetAndPeriod(budget, period);
    }
}
