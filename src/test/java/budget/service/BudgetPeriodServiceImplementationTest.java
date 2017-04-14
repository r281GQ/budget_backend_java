package budget.service;

import budget.accessories.TestAccessories;
import budget.accessories.TestModelRepo;
import budget.accessories.validators.ValidationService;
import budget.controller.exceptions.InvalidDataProvidedException;
import budget.controller.exceptions.ResourceNotFoundException;
import budget.model.BudgetPeriod;
import budget.repository.interfaces.BudgetPeriodRepository;
import budget.service.interfaces.CrossEffectManager;
import budget.service.interfaces.DefaultValueProviderService;
import budget.service.interfaces.DeleteHelperService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by veghe on 18/08/2016.
 */
public class BudgetPeriodServiceImplementationTest {

    @Mock
    private DeleteHelperService deleteHelperService;

    @Mock
    private BudgetPeriodRepository budgetPeriodRepository;

    @Mock
    private CrossEffectManager crossEffectManager;

    @Mock
    private ValidationService validationService;

    @Mock
    private DefaultValueProviderService defaultValueProviderService;

    @InjectMocks
    private BudgetPeriodServiceImplementation budgetPeriodServiceImplementation = new BudgetPeriodServiceImplementation();

    private BudgetPeriod budgetPeriod;

    @Before
    public void setUp(){
        initMocks(this);

        budgetPeriod = TestModelRepo.initBasicBudgetPeriodWithDefaultUserAndPeriodAndBudget();
    }

    @Test
    public void shouldReturnBudgetPeriodOnGet (){
        when(budgetPeriodRepository.get(TestModelRepo.BASIC_ID)).thenReturn(budgetPeriod);

        budgetPeriodServiceImplementation.getById(TestModelRepo.BASIC_ID);

        verify(budgetPeriodRepository).get(TestModelRepo.BASIC_ID);
    }

    @Test
    public void shouldThrowResourceNotFoundOnGet (){
        when(budgetPeriodRepository.get(TestModelRepo.BASIC_ID)).thenReturn(null);

        try {
            budgetPeriodServiceImplementation.getById(TestModelRepo.BASIC_ID);
            fail(TestAccessories.NO_EXCEPTION_THROWN);
        }catch (ResourceNotFoundException e){

        }catch (Exception e){
            fail(TestAccessories.WRONG_EXCEPTION + e.getClass().getTypeName());
        }
        verify(budgetPeriodRepository).get(TestModelRepo.BASIC_ID);
    }

    @Test
    public void shouldThrowResourceNotFoundOnUpdate (){
        when(budgetPeriodRepository.get(TestModelRepo.BASIC_ID)).thenReturn(null);

        try {
            budgetPeriodServiceImplementation.update(budgetPeriod);
            fail(TestAccessories.NO_EXCEPTION_THROWN);
        }catch (ResourceNotFoundException e){

        }catch (Exception e){
            fail(TestAccessories.WRONG_EXCEPTION + e.getClass().getTypeName());
        }
        verify(budgetPeriodRepository).get(TestModelRepo.BASIC_ID);
        verify(crossEffectManager, times(0)).updateBudgetPeriod(budgetPeriod);
    }

    @Test
    public void shouldThrowInvalidDataOnUpdate (){
        when(budgetPeriodRepository.get(TestModelRepo.BASIC_ID)).thenReturn(budgetPeriod);
        when(validationService.isUpdateAble(budgetPeriod)).thenReturn(false);

        try {
            budgetPeriodServiceImplementation.update(budgetPeriod);
            fail(TestAccessories.NO_EXCEPTION_THROWN);
        }catch (InvalidDataProvidedException e){

        }catch (Exception e){
            fail(TestAccessories.WRONG_EXCEPTION + e.getClass().getTypeName());
        }
        verify(budgetPeriodRepository, times(2)).get(TestModelRepo.BASIC_ID);
        verify(crossEffectManager, times(0)).updateBudgetPeriod(budgetPeriod);
    }

    @Test
    public void shouldUpdate (){
        when(budgetPeriodRepository.get(TestModelRepo.BASIC_ID)).thenReturn(budgetPeriod);
        when(validationService.isUpdateAble(budgetPeriod)).thenReturn(true);

        budgetPeriodServiceImplementation.update(budgetPeriod);

        verify(budgetPeriodRepository, times(2)).get(TestModelRepo.BASIC_ID);
        verify(crossEffectManager, times(1)).updateBudgetPeriod(budgetPeriod);
    }
}