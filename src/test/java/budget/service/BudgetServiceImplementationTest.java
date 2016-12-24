package budget.service;

import budget.accessories.TestAccessories;
import budget.accessories.TestModelRepo;
import budget.accessories.validators.ValidationService;
import budget.controller.exceptions.InvalidDataProvidedException;
import budget.controller.exceptions.ResourceNotFoundException;
import budget.model.Budget;
import budget.repository.interfaces.BudgetRepository;
import budget.service.interfaces.CrossEffectManager;
import budget.service.interfaces.DefaultValueProviderService;
import budget.service.interfaces.DeleteHelperService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

/**
 * Created by veghe on 18/08/2016.
 */
public class BudgetServiceImplementationTest {

    @Mock
    private BudgetRepository budgetRepository;

    @Mock
    private DeleteHelperService deleteHelperService;

    @Mock
    private ValidationService validationService;

    @Mock
    private CrossEffectManager crossEffectManager;

    @Mock
    private DefaultValueProviderService defaultValueProviderService;

    @InjectMocks
    private BudgetServiceImplementation budgetServiceImplementation = new BudgetServiceImplementation();

    private Budget budget;

    @Before
    public void setUp() {
        budget = TestModelRepo.initBasicBudgetWithDefaultUser();
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldReturnbudgetOnGet (){
        when(budgetRepository.get(TestModelRepo.BASIC_ID)).thenReturn(budget);

        budgetServiceImplementation.getById(TestModelRepo.BASIC_ID);

        verify(budgetRepository).get(TestModelRepo.BASIC_ID);
    }

    @Test
    public void shouldThrowResourceNotFoundOnGet (){
        when(budgetRepository.get(TestModelRepo.BASIC_ID)).thenReturn(null);

        try {
            budgetServiceImplementation.getById(TestModelRepo.BASIC_ID);
            fail(TestAccessories.NO_EXCEPTION_THROWN);
        }catch (ResourceNotFoundException e){

        }catch (Exception e){
            fail(TestAccessories.WRONG_EXCEPTION + e.getClass().getTypeName());
        }
        verify(budgetRepository).get(TestModelRepo.BASIC_ID);
    }

    @Test
    public void shouldThrowResourceNotFoundOnUpdate (){
        when(budgetRepository.get(TestModelRepo.BASIC_ID)).thenReturn(null);

        try {
            budgetServiceImplementation.update(budget);
            fail(TestAccessories.NO_EXCEPTION_THROWN);
        }catch (ResourceNotFoundException e){

        }catch (Exception e){
            fail(TestAccessories.WRONG_EXCEPTION + e.getClass().getTypeName());
        }
        verify(budgetRepository).get(TestModelRepo.BASIC_ID);
        verify(crossEffectManager, times(0)).updateBudget(budget);
    }

    @Test
    public void shouldThrowInvalidDataOnUpdate (){
        when(budgetRepository.get(TestModelRepo.BASIC_ID)).thenReturn(budget);
        when(validationService.isUpdateAble(budget)).thenReturn(false);

        try {
            budgetServiceImplementation.update(budget);
            fail(TestAccessories.NO_EXCEPTION_THROWN);
        }catch (InvalidDataProvidedException e){

        }catch (Exception e){
            fail(TestAccessories.WRONG_EXCEPTION + e.getClass().getTypeName());
        }
        verify(budgetRepository).get(TestModelRepo.BASIC_ID);
        verify(crossEffectManager, times(0)).updateBudget(budget);
    }

    @Test
    public void shouldUpdate (){
        when(budgetRepository.get(TestModelRepo.BASIC_ID)).thenReturn(budget);
        when(validationService.isUpdateAble(budget)).thenReturn(true);

        budgetServiceImplementation.update(budget);

        verify(budgetRepository).get(TestModelRepo.BASIC_ID);
        verify(crossEffectManager, times(1)).updateBudget(budget);
    }

    @Test
    public void shouldUpdateWOReCalculation (){
        when(budgetRepository.get(TestModelRepo.BASIC_ID)).thenReturn(budget);
        when(validationService.isUpdateAble(budget)).thenReturn(true);

        budgetServiceImplementation.updateWithoutReCalculation(budget);

        verify(budgetRepository).get(TestModelRepo.BASIC_ID);
        verify(crossEffectManager, times(1)).updateBudgetWithoutReCalculation(budget);
    }
}
