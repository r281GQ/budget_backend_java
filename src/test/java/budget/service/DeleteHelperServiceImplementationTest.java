package budget.service;

import budget.accessories.TestModelRepo;
import budget.accessories.builder.TransactionBuilder;
import budget.model.*;
import budget.repository.interfaces.*;
import budget.service.interfaces.CrossEffectManager;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.Arrays;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by veghe on 18/11/2016.
 */
public class DeleteHelperServiceImplementationTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private EquityRepository equityRepository;

    @Mock
    private CrossEffectManager crossEffectManager;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private GroupingRepository groupingRepository;

    @Mock
    private BudgetRepository budgetRepository;

    @Mock
    private BudgetPeriodRepository budgetPeriodRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private DeleteHelperServiceImplementation deleteHelperServiceImplementation = new DeleteHelperServiceImplementation();

    private User user;

    private Account account;

    private Transaction transaction;

    private Grouping grouping;

    private Period period;

    private Budget budget;

    private Equity equity;

    private BudgetPeriod budgetPeriod;

    @Before
    public void setUp() {

        user = TestModelRepo.initBasicUser();

        equity = TestModelRepo.initBasicEquityWithDefaultUser();

        account = TestModelRepo.initBasicAccount();

        grouping = TestModelRepo.initBasicGroupingWithDefaultUser();

        budgetPeriod = TestModelRepo.initBasicBudgetPeriodWithDefaultUserAndPeriodAndBudget();

        budget = TestModelRepo.initBasicBudgetWithDefaultUser();

        transaction = TransactionBuilder.initialize(user, account)
                .setAmount(50)
                .setCurrency(Currency.GBP)
                .setGrouping(grouping)
                .setIdentifier(1L)
                .setName("Loan")
                .setPeriod(period)
                .setBudget(budget)
                .setAmountAtTheMomentOfTransactionForAccount(50)
                .build();

        account = TestModelRepo.initBasicAccount();

        initMocks(this);
    }

    @Test
    public void shouldDeleteTransaction() {
        deleteHelperServiceImplementation.deleteTransaction(transaction);
        verify(crossEffectManager).deleteTransaction(transaction);
    }

    @Test
    public void shouldDeleteEquityWithAllTransactions() {
        when(transactionRepository.findByEquity(equity)).thenReturn(new ArrayList<>(Arrays.asList(transaction)));

        deleteHelperServiceImplementation.deleteEquity(equity);

        verify(crossEffectManager).deleteTransaction(transaction);
        verify(equityRepository).delete(equity.getIdentifier());
    }

    @Test
    public void shouldDeleteAccountWithAllTransactions() {
        when(transactionRepository.findByAccount(account)).thenReturn(new ArrayList<>(Arrays.asList(transaction)));

        deleteHelperServiceImplementation.deleteAccount(account);

        verify(crossEffectManager).deleteTransaction(transaction);
        verify(accountRepository).delete(account.getIdentifier());
    }

    @Test
    public void shouldDeleteGroupingWithAllTransactions() {
        when(transactionRepository.findByGrouping(grouping)).thenReturn(new ArrayList<>(Arrays.asList(transaction)));

        deleteHelperServiceImplementation.deleteGrouping(grouping);

        verify(crossEffectManager).deleteTransaction(transaction);
        verify(groupingRepository).delete(grouping.getIdentifier());
    }

    @Test
    public void shouldDeleteTransactionsAndBudgetPeriods() {
        when(transactionRepository.findByBudget(budget)).thenReturn(new ArrayList<>(Arrays.asList(transaction)));

        when(budgetPeriodRepository.findByBudget(budget)).thenReturn(new ArrayList<>(Arrays.asList(budgetPeriod)));

        deleteHelperServiceImplementation.deleteBudget(budget);

        verify(crossEffectManager).deleteTransaction(transaction);
        verify(budgetPeriodRepository).delete(budgetPeriod.getIdentifier());
        verify(budgetRepository).delete(budget.getIdentifier());
    }

    @Test
    public void shouldDeleteEverything() {

        when(accountRepository.findByUser(user)).thenReturn(new ArrayList<>(Arrays.asList(account)));
        when(equityRepository.findByUser(user)).thenReturn(new ArrayList<>(Arrays.asList(equity)));
        when(groupingRepository.findByUser(user)).thenReturn(new ArrayList<>(Arrays.asList(grouping)));
        when(budgetRepository.findByUser(user)).thenReturn(new ArrayList<>(Arrays.asList(budget)));

        when(transactionRepository.findByEquity(equity)).thenReturn(new ArrayList<>(Arrays.asList(transaction)));
        when(transactionRepository.findByAccount(account)).thenReturn(new ArrayList<>(Arrays.asList(transaction)));
        when(transactionRepository.findByGrouping(grouping)).thenReturn(new ArrayList<>(Arrays.asList(transaction)));
        when(transactionRepository.findByBudget(budget)).thenReturn(new ArrayList<>(Arrays.asList(transaction)));
        when(budgetPeriodRepository.findByBudget(budget)).thenReturn(new ArrayList<>(Arrays.asList(budgetPeriod)));

        deleteHelperServiceImplementation.deleteUser(user);

        verify(accountRepository).delete(account.getIdentifier());
        verify(groupingRepository).delete(grouping.getIdentifier());
        verify(budgetPeriodRepository).delete(budgetPeriod.getIdentifier());
        verify(budgetRepository).delete(budget.getIdentifier());
        verify(equityRepository).delete(equity.getIdentifier());

        verify(crossEffectManager, times(4)).deleteTransaction(transaction);

        verify(userRepository).delete(user.getIdentifier());
    }
}