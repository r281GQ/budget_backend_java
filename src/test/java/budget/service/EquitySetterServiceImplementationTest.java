package budget.service;

import budget.accessories.TestAccessories;
import budget.accessories.TestModelRepo;
import budget.accessories.builder.TransactionBuilder;
import budget.controller.exceptions.InvalidDataProvidedException;
import budget.model.*;
import budget.repository.interfaces.EquityRepository;
import budget.service.interfaces.DefaultValueProviderService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.math.BigDecimal;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by veghe on 05/12/2016.
 */
public class EquitySetterServiceImplementationTest {

    private User user;

    private Account account;

    private Period period;

    private Grouping grouping;

    private Transaction transaction;

    private Equity equity;

    @Mock
    private EquityRepository equityRepository;

    @Mock
    private DefaultValueProviderService defaultValueProviderService;

    @InjectMocks
    private EquitySetterServiceImplementation equitySetterServiceImplementation = new EquitySetterServiceImplementation();

    @Before
    public void setUp(){

        grouping = TestModelRepo.initBasicGroupingWithDefaultUser();

        equity = TestModelRepo.initBasicEquityWithDefaultUser();

        user = TestModelRepo.initBasicUser();

        account = TestModelRepo.initBasicAccount();

        period = TestModelRepo.initBasicPeriod();

        transaction = TransactionBuilder.initialize(user, account)
                .setAmount(50)
                .setCurrency(Currency.GBP)
                .setGrouping(grouping)
                .setIdentifier(1L)
                .setName("Loan")
                .setPeriod(period)
                .setEquity(equity)
                .setAmountAtTheMomentOfTransactionForAccount(50)
                .setAmountAtTheMomentOfTransactionForEQ(50)
                    .build();

        initMocks(this);
    }

    @Test
    public void shouldLowerEQTo50OnCreationWithExpense(){
        equitySetterServiceImplementation.updateEquityOnTransactionCreation(transaction);
        assertThat(equity.getBalance().doubleValue(), is(50.0));
        verify(equityRepository).update(equity);
    }

    @Test
    public void shouldIncreaseEQTo150OnCreationWithExpense(){
        equity.setType(EQType.ASSET);
        equitySetterServiceImplementation.updateEquityOnTransactionCreation(transaction);
        assertThat(equity.getBalance().doubleValue(), is(150.0));
        verify(equityRepository).update(equity);
    }

    @Test
    public void shouldLowerEQTo50OnCreationWithIncome(){

        grouping.setType(Type.INCOME);

        equity.setType(EQType.ASSET);

        equitySetterServiceImplementation.updateEquityOnTransactionCreation(transaction);
        assertThat(equity.getBalance().doubleValue(), is(50.0));
        verify(equityRepository).update(equity);
    }

    @Test
    public void shouldIncreaseEQTo150OnCreationWithIncome(){

        grouping.setType(Type.INCOME);

        equitySetterServiceImplementation.updateEquityOnTransactionCreation(transaction);
        assertThat(equity.getBalance().doubleValue(), is(150.0));
        verify(equityRepository).update(equity);
    }

    @Test
    public void shouldIncreaseEQTo150OnDeletionWithExpense(){
        equitySetterServiceImplementation.updateEquityOnTransactionDeletion(transaction);
        assertThat(equity.getBalance().doubleValue(), is(150.0));
        verify(equityRepository).update(equity);
    }

    @Test
    public void shouldLowerEQTo50OnDeletionWithExpense(){
        equity.setType(EQType.ASSET);
        equitySetterServiceImplementation.updateEquityOnTransactionDeletion(transaction);
        assertThat(equity.getBalance().doubleValue(), is(50.0));
        verify(equityRepository).update(equity);
    }

    @Test
    public void shouldLowerEQTo50OnDeletionWithIncome(){

        grouping.setType(Type.INCOME);

        equity.setType(EQType.ASSET);

        equitySetterServiceImplementation.updateEquityOnTransactionDeletion(transaction);
        assertThat(equity.getBalance().doubleValue(), is(150.0));
        verify(equityRepository).update(equity);
    }

    @Test
    public void shouldIncreaseEQTo150OnDeletionWithIncome(){

        grouping.setType(Type.INCOME);

        equitySetterServiceImplementation.updateEquityOnTransactionDeletion(transaction);
        assertThat(equity.getBalance().doubleValue(), is(50.0));
        verify(equityRepository).update(equity);
    }

    @Test
    public void shouldThrowRunTimeExceptionWhenExpenseCreationIsMoreThanCurrentLiability(){

        transaction.setAmountAtTheMomentOfTransactionForEQ(new BigDecimal(200));

        try {
            equitySetterServiceImplementation.updateEquityOnTransactionCreation(transaction);
            fail(TestAccessories.NO_EXCEPTION_THROWN);
        }catch (InvalidDataProvidedException e){

        }catch (RuntimeException e){
            fail(TestAccessories.WRONG_EXCEPTION + e.getMessage());
        }
        verify(equityRepository, times(0)).update(equity);
    }

    @Test
    public void shouldThrowRunTimeExceptionWhenIncomeCreationIsMoreThanCurrentAsset(){
        equity.setType(EQType.ASSET);
        grouping.setType(Type.INCOME);

        transaction.setAmountAtTheMomentOfTransactionForEQ(new BigDecimal(200));

        try {
            equitySetterServiceImplementation.updateEquityOnTransactionCreation(transaction);
            fail(TestAccessories.NO_EXCEPTION_THROWN);
        }catch (InvalidDataProvidedException e) {

        }catch (RuntimeException e){
            fail(TestAccessories.WRONG_EXCEPTION + e.getClass());
        }
        verify(equityRepository, times(0)).update(equity);
    }

    @Test
    public void shouldThrowRunTimeExceptionWhenExpenseDeletionIsMoreThanCurrentAsset(){
        grouping.setType(Type.INCOME);
        transaction.setAmountAtTheMomentOfTransactionForEQ(new BigDecimal(200));

        try {
            equitySetterServiceImplementation.updateEquityOnTransactionDeletion(transaction);
            fail(TestAccessories.NO_EXCEPTION_THROWN);
        }catch (InvalidDataProvidedException e) {

        }catch (RuntimeException e){
            fail(TestAccessories.WRONG_EXCEPTION + e.getClass());
        }
        verify(equityRepository, times(0)).update(equity);
    }

    @Test
    public void shouldThrowRunTimeExceptionWhenIncomeDeletionIsMoreThanCurrentAsset2(){
        equity.setType(EQType.ASSET);
        transaction.setAmountAtTheMomentOfTransactionForEQ(new BigDecimal(200));

        try {
            equitySetterServiceImplementation.updateEquityOnTransactionDeletion(transaction);
            fail(TestAccessories.NO_EXCEPTION_THROWN);
        }catch (InvalidDataProvidedException e) {

        }catch (RuntimeException e){
            fail(TestAccessories.WRONG_EXCEPTION + e.getClass());
        }
        verify(equityRepository, times(0)).update(equity);
    }
}