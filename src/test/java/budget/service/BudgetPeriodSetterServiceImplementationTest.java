package budget.service;

import budget.accessories.TestModelRepo;
import budget.accessories.builder.TransactionBuilder;
import budget.model.*;
import budget.repository.interfaces.BudgetPeriodRepository;
import budget.repository.interfaces.TransactionRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by veghe on 05/12/2016.
 */
public class BudgetPeriodSetterServiceImplementationTest {

    private User user;

    private Account account;

    private Period period;

    private Grouping grouping;

    private Transaction transaction;

    private Budget budget;

    private BudgetPeriod budgetPeriod;

    private BudgetPeriod previousBudgetPeriod;

    @Mock
    private BudgetPeriodRepository budgetPeriodRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private BudgetPeriodSetterServiceImplementation budgetPeriodSetterServiceImplementation = new BudgetPeriodSetterServiceImplementation();

    @Before
    public void setUp(){

        budget = TestModelRepo.initBasicBudgetWithDefaultUser();

        budgetPeriod = TestModelRepo.initBasicBudgetPeriodWithDefaultUserAndPeriodAndBudget();

        previousBudgetPeriod = TestModelRepo.initPreviousBudgetPeriodWithDefaultUserAndPeriodAndBudget();

        grouping = TestModelRepo.initBasicGroupingWithDefaultUser();

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
                .setBudget(budget)
                .setAmountAtTheMomentOfTransactionForAccount(50)
                .setAmountAtTheMomentOfTransactionForBudget(50)
                    .build();

        initMocks(this);
    }

    @Test
    public void shouldSetNewBudgetPeriodOnTransactionCreation(){

        Budget budget = transaction.getBudget();
        BudgetPeriod budgetPeriodsMethodCreationMock = new BudgetPeriod();
        budgetPeriodsMethodCreationMock.setName(budget.getName());
        budgetPeriodsMethodCreationMock.setBudget(budget);
        budgetPeriodsMethodCreationMock.setUser(budget.getUser());
        budgetPeriodsMethodCreationMock.setPeriod(transaction.getPeriod());
        budgetPeriodsMethodCreationMock.setAllowance(budget.getDefaultAllowance());
        budgetPeriodsMethodCreationMock.setBalance(budgetPeriod.getAllowance());

        when(budgetPeriodRepository.findByBudgetAndPeriod(budget, period)).thenReturn(null, budgetPeriodsMethodCreationMock );
        budgetPeriodSetterServiceImplementation.updateBudgetPeriodOnTransactionCreation(transaction);

        assertThat(transaction.getBudgetPeriod(), is(not(nullValue())));
        assertThat(transaction.getBudgetPeriod().getPeriod(), is(TestModelRepo.initBasicPeriod()));
        assertThat(transaction.getBudgetPeriod().getAllowance().doubleValue(), is(100.5));
        assertThat(transaction.getBudgetPeriod().getBudget(), is(budget));
        assertThat(transaction.getBudgetPeriod().getIdentifier(), is(nullValue()));

        verify(budgetPeriodRepository, times(2)).findByBudgetAndPeriod(budget, period);
        verify(transactionRepository, times(1)).update(transaction);
    }

    @Test
    public void shouldUseExistingBudgetPeriodOnTransactionCreation(){
        budgetPeriod.setAllowance(new BigDecimal(600));

        when(budgetPeriodRepository.findByBudgetAndPeriod(budget, period)).thenReturn(budgetPeriod );
        budgetPeriodSetterServiceImplementation.updateBudgetPeriodOnTransactionCreation(transaction);

        assertThat(transaction.getBudgetPeriod(), is(not(nullValue())));
        assertThat(transaction.getBudgetPeriod(), is(budgetPeriod));
        assertThat(transaction.getBudgetPeriod().getPeriod(), is(TestModelRepo.initBasicPeriod()));
        assertThat(transaction.getBudgetPeriod().getAllowance().doubleValue(), is(600.0));
        assertThat(transaction.getBudgetPeriod().getBudget(), is(budget));

        verify(budgetPeriodRepository, times(1)).findByBudgetAndPeriod(budget, period);
        verify(transactionRepository, times(1)).update(transaction);
    }

    @Test
    public void shouldHaveEffectOnlyOnCurrentBudgetPeriod(){
        transaction.setBudgetPeriod(budgetPeriod);

        when(budgetPeriodRepository.findByBudgetAndPeriod(budget, period)).thenReturn(budgetPeriod);

        when(budgetPeriodRepository.findByBudgetOrderedByDate(budget)).thenReturn(new ArrayList<>(Arrays.asList( budgetPeriod)));

        when(transactionRepository.findByBudget(budget)).thenReturn(new ArrayList<>(Arrays.asList(transaction)));
        when(transactionRepository.findBudgetPeriod(budgetPeriod)).thenReturn(new ArrayList<>(Arrays.asList(transaction)));

        budgetPeriodSetterServiceImplementation.updateBudgetPeriodOnTransactionCreation(transaction);
        assertThat(transaction.getBudgetPeriod().getAllowance().doubleValue(), is(150.5));
        assertThat(transaction.getBudgetPeriod().getBalance().doubleValue(), is(100.5));
    }

    @Test
    public void shouldHaveEffectOnlyOnCurrentBudgetPeriodWithTwoTransactions(){

        Transaction otherTransaction = TransactionBuilder.initialize(user, account)
                .setAmount(75)
                .setCurrency(Currency.GBP)
                .setGrouping(grouping)
                .setIdentifier(1L)
                .setName("Loan 2nd installment")
                .setPeriod(period)
                .setBudget(budget)
                .setAmountAtTheMomentOfTransactionForAccount(75)
                .setAmountAtTheMomentOfTransactionForBudget(75)
                    .build();

        transaction.setBudgetPeriod(budgetPeriod);
        otherTransaction.setBudgetPeriod(budgetPeriod);

        when(budgetPeriodRepository.findByBudgetAndPeriod(budget, period)).thenReturn(budgetPeriod);

        when(budgetPeriodRepository.findByBudgetOrderedByDate(budget)).thenReturn(new ArrayList<>(Arrays.asList( budgetPeriod)));

        when(transactionRepository.findByBudget(budget)).thenReturn(new ArrayList<>(Arrays.asList(transaction, otherTransaction)));
        when(transactionRepository.findBudgetPeriod(budgetPeriod)).thenReturn(new ArrayList<>(Arrays.asList(transaction, otherTransaction)));

        budgetPeriodSetterServiceImplementation.updateBudgetPeriodOnTransactionCreation(transaction);
        assertThat(transaction.getBudgetPeriod().getAllowance().doubleValue(), is(150.5));
        assertThat(transaction.getBudgetPeriod().getBalance().doubleValue(), is(25.5));
    }

    @Test
    public void shouldHaveEffectOnTwoBudgetPeriodWithTwoTransactionsDifferentPeriods(){

        Transaction otherTransaction = TransactionBuilder.initialize(user, account)
                .setAmount(75)
                .setCurrency(Currency.GBP)
                .setGrouping(grouping)
                .setIdentifier(1L)
                .setName("Loan 2nd installment")
                .setPeriod(TestModelRepo.initPreviousPeriod())
                .setBudget(budget)
                .setAmountAtTheMomentOfTransactionForAccount(75)
                .setAmountAtTheMomentOfTransactionForBudget(75)
                .build();

        transaction.setBudgetPeriod(budgetPeriod);
        otherTransaction.setBudgetPeriod(previousBudgetPeriod);

        when(budgetPeriodRepository.findByBudgetAndPeriod(budget, period)).thenReturn(budgetPeriod);
        when(budgetPeriodRepository.findByBudgetAndPeriod(budget, TestModelRepo.initPreviousPeriod())).thenReturn(budgetPeriod);

        when(budgetPeriodRepository.findByBudgetOrderedByDate(budget)).thenReturn(new ArrayList<>(Arrays.asList(previousBudgetPeriod, budgetPeriod)));

        when(transactionRepository.findBudgetPeriod(budgetPeriod)).thenReturn(new ArrayList<>(Arrays.asList(transaction)));
        when(transactionRepository.findBudgetPeriod(previousBudgetPeriod)).thenReturn(new ArrayList<>(Arrays.asList(otherTransaction)));

        budgetPeriodSetterServiceImplementation.updateBudgetPeriodOnTransactionCreation(transaction);

        assertThat(transaction.getBudgetPeriod().getAllowance().doubleValue(), is(150.5));
        assertThat(otherTransaction.getBudgetPeriod().getAllowance().doubleValue(), is(150.5));
        assertThat(transaction.getBudgetPeriod().getBalance().doubleValue(), is(176.0));
        assertThat(otherTransaction.getBudgetPeriod().getBalance().doubleValue(), is(75.5));
    }

    @Test
    public void shouldHaveEffectOnTwoBudgetPeriodWithTwoTransactionsDifferentPeriodsAddOneMoreToPrevious(){

        Transaction otherTransaction = TransactionBuilder.initialize(user, account)
                .setAmount(75)
                .setCurrency(Currency.GBP)
                .setGrouping(grouping)
                .setIdentifier(2L)
                .setName("Loan 2nd installment")
                .setPeriod(TestModelRepo.initPreviousPeriod())
                .setBudget(budget)
                .setAmountAtTheMomentOfTransactionForAccount(75)
                .setAmountAtTheMomentOfTransactionForBudget(75)
                .build();

        Transaction previousOther = TransactionBuilder.initialize(user, account)
                .setAmount(20)
                .setCurrency(Currency.GBP)
                .setGrouping(grouping)
                .setIdentifier(3L)
                .setName("Loan 3nd installment")
                .setPeriod(TestModelRepo.initPreviousPeriod())
                .setBudget(budget)
                .setAmountAtTheMomentOfTransactionForAccount(20)
                .setAmountAtTheMomentOfTransactionForBudget(20)
                .build();

        transaction.setBudgetPeriod(budgetPeriod);
        otherTransaction.setBudgetPeriod(previousBudgetPeriod);
        previousOther.setBudgetPeriod(previousBudgetPeriod);

        when(budgetPeriodRepository.findByBudgetAndPeriod(budget, period)).thenReturn(budgetPeriod);
        when(budgetPeriodRepository.findByBudgetAndPeriod(budget, TestModelRepo.initPreviousPeriod())).thenReturn(budgetPeriod);

        when(budgetPeriodRepository.findByBudgetOrderedByDate(budget)).thenReturn(new ArrayList<>(Arrays.asList(previousBudgetPeriod, budgetPeriod)));

        when(transactionRepository.findBudgetPeriod(budgetPeriod)).thenReturn(new ArrayList<>(Arrays.asList(transaction)));
        when(transactionRepository.findBudgetPeriod(previousBudgetPeriod)).thenReturn(new ArrayList<>(Arrays.asList(otherTransaction, previousOther)));

        budgetPeriodSetterServiceImplementation.updateBudgetPeriodOnTransactionCreation(transaction);
        assertThat(transaction.getBudgetPeriod().getAllowance().doubleValue(), is(150.5));
        assertThat(otherTransaction.getBudgetPeriod().getAllowance().doubleValue(), is(150.5));
        assertThat(transaction.getBudgetPeriod().getBalance().doubleValue(), is(156.0));
        assertThat(otherTransaction.getBudgetPeriod().getBalance().doubleValue(), is(55.5));
    }

    @Test
    public void shouldSetBalancesBackOnDeletion(){

        budgetPeriod.setBalance(new BigDecimal(156.0));
        previousBudgetPeriod.setBalance(new BigDecimal(55.5));

        Transaction otherTransaction = TransactionBuilder.initialize(user, account)
                .setAmount(75)
                .setCurrency(Currency.GBP)
                .setGrouping(grouping)
                .setIdentifier(2L)
                .setName("Loan 2nd installment")
                .setPeriod(TestModelRepo.initPreviousPeriod())
                .setBudget(budget)
                .setAmountAtTheMomentOfTransactionForAccount(75)
                .setAmountAtTheMomentOfTransactionForBudget(75)
                .build();

        Transaction previousOther = TransactionBuilder.initialize(user, account)
                .setAmount(20)
                .setCurrency(Currency.GBP)
                .setGrouping(grouping)
                .setIdentifier(3L)
                .setName("Loan 3nd installment")
                .setPeriod(TestModelRepo.initPreviousPeriod())
                .setBudget(budget)
                .setAmountAtTheMomentOfTransactionForAccount(20)
                .setAmountAtTheMomentOfTransactionForBudget(20)
                .build();

        transaction.setBudgetPeriod(budgetPeriod);
        otherTransaction.setBudgetPeriod(previousBudgetPeriod);
        previousOther.setBudgetPeriod(previousBudgetPeriod);

        assertThat(transaction.getBudgetPeriod().getBalance().doubleValue(), is(156.0));
        assertThat(otherTransaction.getBudgetPeriod().getBalance().doubleValue(), is(55.5));

        transaction.setBudgetPeriod(budgetPeriod);
        otherTransaction.setBudgetPeriod(previousBudgetPeriod);
        previousOther.setBudgetPeriod(previousBudgetPeriod);

        when(budgetPeriodRepository.findByBudgetOrderedByDate(budget)).thenReturn(new ArrayList<>(Arrays.asList(previousBudgetPeriod, budgetPeriod)));

        when(transactionRepository.findBudgetPeriod(budgetPeriod)).thenReturn(new ArrayList<>(Arrays.asList(transaction)));
        when(transactionRepository.findBudgetPeriod(previousBudgetPeriod)).thenReturn(new ArrayList<>(Arrays.asList( previousOther)));

        budgetPeriodSetterServiceImplementation.updateBudgetPeriodOnTransactionDeletion(transaction);
        assertThat(transaction.getBudgetPeriod().getAllowance().doubleValue(), is(150.5));
        assertThat(otherTransaction.getBudgetPeriod().getAllowance().doubleValue(), is(150.5));
        assertThat(budgetPeriod.getBalance().doubleValue(), is(231.0));
        assertThat(previousBudgetPeriod.getBalance().doubleValue(), is(130.5));
    }

    @Test
    public void shouldSetAllowanceAndNameAndBalancesOfTheBudgetPeriodOnBudgetUpdate(){
        Transaction otherTransaction = TransactionBuilder.initialize(user, account)
                .setAmount(75)
                .setCurrency(Currency.GBP)
                .setGrouping(grouping)
                .setIdentifier(2L)
                .setName("Loan 2nd installment")
                .setPeriod(TestModelRepo.initPreviousPeriod())
                .setBudget(budget)
                .setAmountAtTheMomentOfTransactionForAccount(75)
                .setAmountAtTheMomentOfTransactionForBudget(75)
                .build();

//        Transaction previousOther = TransactionBuilder.initialize(user, account)
//                .setAmount(20)
//                .setCurrency(Currency.GBP)
//                .setGrouping(grouping)
//                .setIdentifier(3L)
//                .setName("Loan 3nd installment")
//                .setPeriod(TestModelRepo.initPreviousPeriod())
//                .setBudget(budget)
//                .setAmountAtTheMomentOfTransactionForAccount(20)
//                .build();

        transaction.setBudgetPeriod(budgetPeriod);
        otherTransaction.setBudgetPeriod(previousBudgetPeriod);
//        previousOther.setBudgetPeriod(previousBudgetPeriod);
        budgetPeriod.setAllowance(new BigDecimal(1000));
        budgetPeriod.setBalance(new BigDecimal(555));

        previousBudgetPeriod.setAllowance(new BigDecimal(3));
        previousBudgetPeriod.setAllowance(new BigDecimal(3));

        budget.setName("new name");

        budget.setDefaultAllowance(new BigDecimal(2000));

        assertThat(budgetPeriod.getName(), is("Food"));
        assertThat(budgetPeriod.getAllowance().doubleValue(), is(1000.0));
        assertThat(budgetPeriod.getBalance().doubleValue(), is(555.0));

        when(budgetPeriodRepository.findByBudget(budget)).thenReturn(new ArrayList<>(Arrays.asList(budgetPeriod, previousBudgetPeriod)));

        when(budgetPeriodRepository.findByBudgetOrderedByDate(budget)).thenReturn(new ArrayList<>(Arrays.asList(previousBudgetPeriod, budgetPeriod)));

        when(transactionRepository.findBudgetPeriod(budgetPeriod)).thenReturn(new ArrayList<>(Arrays.asList(transaction)));

        when(transactionRepository.findBudgetPeriod(previousBudgetPeriod)).thenReturn(new ArrayList<>(Arrays.asList(otherTransaction)));


        budgetPeriodSetterServiceImplementation.updateBudgetPeriodOnBudgetUpdate(budget);

        assertThat(budgetPeriod.getName(), is("new name 11-2016"));
        assertThat(budgetPeriod.getAllowance().doubleValue(), is(2000.0));
        assertThat(budgetPeriod.getBalance().doubleValue(), is(3875.0));

        assertThat(previousBudgetPeriod.getName(), is("new name 10-2016"));
        assertThat(previousBudgetPeriod.getAllowance().doubleValue(), is(2000.0));
        assertThat(previousBudgetPeriod.getBalance().doubleValue(), is(1925.0));
    }

    @Test
    public void shouldSetOnlyTheNameOfTheBudgetPeriodOnBudgetUpdateWOReCalculation(){

        budgetPeriod.setAllowance(new BigDecimal(1000));
        budgetPeriod.setBalance(new BigDecimal(555));

        budget.setName("new name");

        budget.setDefaultAllowance(new BigDecimal(2000));

        assertThat(budgetPeriod.getName(), is("Food"));
        assertThat(budgetPeriod.getAllowance().doubleValue(), is(1000.0));
        assertThat(budgetPeriod.getBalance().doubleValue(), is(555.0));

        when(budgetPeriodRepository.findByBudget(budget)).thenReturn(new ArrayList<>(Arrays.asList(budgetPeriod)));

        when(budgetPeriodRepository.findByBudgetOrderedByDate(budget)).thenReturn(new ArrayList<>(Arrays.asList(budgetPeriod)));

        when(transactionRepository.findBudgetPeriod(budgetPeriod)).thenReturn(new ArrayList<>(Arrays.asList(transaction)));

        budgetPeriodSetterServiceImplementation.updateBudgetPeriodOnBudgetUpdateWithoutReCalculation(budget);

        assertThat(budgetPeriod.getName(), is("new name 11-2016"));
        assertThat(budgetPeriod.getAllowance().doubleValue(), is(1000.0));
        assertThat(budgetPeriod.getBalance().doubleValue(), is(555.0));
    }
}