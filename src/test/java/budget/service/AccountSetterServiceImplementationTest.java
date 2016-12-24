package budget.service;

import budget.accessories.TestAccessories;
import budget.accessories.TestModelRepo;
import budget.accessories.builder.TransactionBuilder;
import budget.controller.exceptions.InvalidDataProvidedException;
import budget.model.*;
import budget.repository.interfaces.AccountRepository;
import budget.service.interfaces.DefaultValueProviderService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.math.BigDecimal;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by veghe on 04/12/2016.
 */
public class AccountSetterServiceImplementationTest {

    private Transaction transaction;

    private User user;

    private Account account;

    private Period period;

    private Grouping grouping;

//    @Rule
//    public ExpectedException expectedException = ExpectedException.none();

    @Mock
    private DefaultValueProviderService defaultValueProviderService;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountSetterServiceImplementation accountSetterServiceImplementation = new AccountSetterServiceImplementation();

    @Before
    public void setUp(){

        grouping = TestModelRepo.initBasicGroupingWithDefaultUser();

        user = TestModelRepo.initBasicUser();

        account = TestModelRepo.initBasicAccount();

        period = TestModelRepo.initBasicPeriod();

        transaction = TransactionBuilder.initialize(user, account)
                .setAmount(50)
                .setCurrency(Currency.GBP)
                .setGrouping(grouping)
                .setIdentifier(1L)
                .setName("Rent")
                .setPeriod(period)
                .setAmountAtTheMomentOfTransactionForAccount(50)
                    .build();

        initMocks(this);
    }

    @Test
    public void shouldUpdateAccountBalanceTo50WithExpenseOnCreation(){
        accountSetterServiceImplementation.updateAccountOnTransactionCreation(transaction);
        assertThat(account.getBalance().doubleValue(), is(50.0));
        verify(accountRepository).update(account);
    }

    @Test
    public void shouldUpdateAccountBalanceTo150WithIncome(){

        grouping.setName("Income");
        grouping.setType(Type.INCOME);

        accountSetterServiceImplementation.updateAccountOnTransactionCreation(transaction);
        assertThat(account.getBalance().doubleValue(), is(150.0));
        verify(accountRepository).update(account);
    }

    @Test
    public void shouldThrowInvalidDataWhenThereAreNotEnoughBalance(){
        transaction.setAmountAtTheMomentOfTransactionForAccount(new BigDecimal(150));
        try {
            accountSetterServiceImplementation.updateAccountOnTransactionCreation(transaction);
            fail(TestAccessories.NO_EXCEPTION_THROWN);
        }catch (InvalidDataProvidedException e){

        }catch (Exception e){
            fail(TestAccessories.WRONG_EXCEPTION + e.getClass().getTypeName());
        }
    }

    @Test
    public void shouldUpdateAccountBalanceTo50WithIncomeOnDeletion(){

        grouping.setName("Income");
        grouping.setType(Type.INCOME);

        accountSetterServiceImplementation.updateAccountOnTransactionDeletion(transaction);
        assertThat(account.getBalance().doubleValue(), is(50.0));
        verify(accountRepository).update(account);
    }

    @Test
    public void shouldUpdateAccountBalanceTo150WithExpenseOnDeletion(){
        accountSetterServiceImplementation.updateAccountOnTransactionDeletion(transaction);
        assertThat(account.getBalance().doubleValue(), is(150.0));
        verify(accountRepository).update(account);
    }

    @Test
    public void shouldThrowInvalidDataWhenThereAreWouldNotBeEnoughBalanceAfterDeletion(){
        transaction.setAmountAtTheMomentOfTransactionForAccount(new BigDecimal(150));

        try {
            accountSetterServiceImplementation.updateAccountOnTransactionCreation(transaction);
            fail(TestAccessories.NO_EXCEPTION_THROWN);
        }catch (InvalidDataProvidedException e){

        }catch (Exception e){
            fail(TestAccessories.WRONG_EXCEPTION + e.getClass().getTypeName());
        }
    }
}