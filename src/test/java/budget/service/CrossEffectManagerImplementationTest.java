package budget.service;

import budget.accessories.TestModelRepo;
import budget.accessories.builder.TransactionBuilder;
import budget.model.*;
import budget.repository.interfaces.*;
import budget.service.interfaces.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by veghe on 04/12/2016.
 */
public class CrossEffectManagerImplementationTest {

    @Mock
    private PeriodCreatorService periodCreatorService;

    @Mock
    private BudgetRepository budgetRepository;

    @Mock
    private BudgetPeriodRepository budgetPeriodRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private EquityRepository equityRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private CurrencySetterService currencySetterService;

    @Mock
    private EquitySetterService equitySetterService;

    @Mock
    private BudgetPeriodSetterService budgetPeriodSetterService;

    @Mock
    private AccountSetterService accountSetterService;

    @Mock
    private GroupingRepository groupingRepository;

    @Mock
    private UserRepository userRepository;


    @InjectMocks
    private CrossEffectManagerImplementation crossEffectManagerImplementation = new CrossEffectManagerImplementation();

    private Transaction transaction;

    private Account account;

    private User user;

    private Grouping grouping;

    private Equity equity;

    private Budget budget;

    private BudgetPeriod budgetPeriod;

    private Period period;

    @Before
    public void setUp(){
        grouping = new Grouping();
        grouping.setIdentifier(1l);
        grouping.setName("Rent");
        grouping.setType(Type.EXPENSE);
        grouping.setUser(user);

        period = TestModelRepo.initBasicPeriod();

        user = TestModelRepo.initBasicUser();

        account = TestModelRepo.initBasicAccount();

        account.setUser(user);

        budget = TestModelRepo.initBasicBudgetWithDefaultUser();

        budget.setUser(user);
        budget.setBudgetPeriods(new ArrayList<>(Arrays.asList(budgetPeriod)));

        budgetPeriod = TestModelRepo.initBasicBudgetPeriodWithDefaultUserAndPeriodAndBudget();

        equity = new Equity();

        equity.setName("Main debt");
        equity.setUser(user);
        equity.setIdentifier(1l);
        equity.setBalance(new BigDecimal(400));
        equity.setCurrency(Currency.GBP);
        equity.setType(EQType.LIABILITY);

        transaction = TransactionBuilder.initialize(user, account)
                .setAmount(100)
                .setBudget(budget)
                .setBudgetPeriod(budgetPeriod)
                .setCurrency(Currency.GBP)
                .setGrouping(grouping)
                .setIdentifier(1l)
                .setName("Rent")
                .setPeriod(period)
                .setEquity(equity)
                    .build();

        initMocks(this);
    }

    @Test
    public void shouldInvokeMethodsOnDeletion(){
        when(transactionRepository.get(TestModelRepo.BASIC_ID)).thenReturn(transaction);
        when(budgetRepository.get(any(Long.class))).thenReturn(budget);
        when(transactionRepository.findByBudget(budget)).thenReturn(new ArrayList<>(Arrays.asList(transaction)));
        when(transactionRepository.findBudgetPeriod(budgetPeriod)).thenReturn(new ArrayList<>(Arrays.asList(transaction)));
        transaction.setAmountAtTheMomentOfTransactionForAccount(new BigDecimal(100));
        transaction.setAmountAtTheMomentOfTransactionForEQ(new BigDecimal(100));
        crossEffectManagerImplementation.deleteTransaction(transaction);
        verify(accountSetterService).updateAccountOnTransactionDeletion(transaction);
        verify(equitySetterService).updateEquityOnTransactionDeletion(transaction);
        verify(budgetPeriodSetterService).updateBudgetPeriodOnTransactionDeletion(transaction);
    }

    @Test
    public void shouldInvokeMethodsOnCreation(){
        when(userRepository.get(transaction.getUser().getIdentifier())).thenReturn(user);
        when(groupingRepository.get(transaction.getUser().getIdentifier())).thenReturn(grouping);
        when(equityRepository.get(transaction.getEquity().getIdentifier())).thenReturn(equity);
        when(budgetRepository.get(any(Long.class))).thenReturn(budget);
        when(periodCreatorService.createPeriod(any(Date.class))).thenReturn(period);
        when(transactionRepository.findByBudget(budget)).thenReturn(new ArrayList<>(Arrays.asList(transaction)));
        when(transactionRepository.findBudgetPeriod(budgetPeriod)).thenReturn(new ArrayList<>(Arrays.asList(transaction)));
        transaction.setAmountAtTheMomentOfTransactionForAccount(new BigDecimal(100));
        transaction.setAmountAtTheMomentOfTransactionForEQ(new BigDecimal(100));
        crossEffectManagerImplementation.createTransaction(transaction);
        verify(accountSetterService).updateAccountOnTransactionCreation(transaction);
        verify(equitySetterService).updateEquityOnTransactionCreation(transaction);
        verify(budgetPeriodSetterService).updateBudgetPeriodOnTransactionCreation(transaction);
    }

    @Test
    public void shouldReturnCurrentDate(){

        when(userRepository.get(transaction.getUser().getIdentifier())).thenReturn(user);
        when(equityRepository.get(transaction.getEquity().getIdentifier())).thenReturn(equity);
        when(budgetRepository.get(any(Long.class))).thenReturn(budget);
        transaction = new Transaction();
        transaction.setGrouping(grouping);
        transaction.setAccount(account);
        transaction.setUser(user);

        crossEffectManagerImplementation.createTransaction(transaction);
        assertThat(transaction.getCreationDate().getDay(), is(new Date().getDay()));
    }

    @Test
    public void shouldInvokePeriodCreatorServiceMethod(){

        when(userRepository.get(transaction.getUser().getIdentifier())).thenReturn(user);
        when(equityRepository.get(transaction.getEquity().getIdentifier())).thenReturn(equity);
        when(budgetRepository.get(any(Long.class))).thenReturn(budget);
        ArgumentCaptor <Date> dateArgumentCaptor = ArgumentCaptor.forClass(Date.class);

        when(periodCreatorService.createPeriod(dateArgumentCaptor.capture())).thenReturn(period);
        crossEffectManagerImplementation.createTransaction(transaction);
        verify(periodCreatorService).createPeriod(dateArgumentCaptor.getValue());
    }

    @Test
    public void shouldInvokeEquitySetterService(){

        when(userRepository.get(transaction.getUser().getIdentifier())).thenReturn(user);
        when(equityRepository.get(transaction.getEquity().getIdentifier())).thenReturn(equity);
        when(budgetRepository.get(any(Long.class))).thenReturn(budget);
        crossEffectManagerImplementation.createTransaction(transaction);
        verify(equitySetterService).updateEquityOnTransactionCreation(transaction);
    }

    @Test
    public void shouldInvokeBudgetPeriodSetterService(){


        when(userRepository.get(transaction.getUser().getIdentifier())).thenReturn(user);
        when(equityRepository.get(transaction.getEquity().getIdentifier())).thenReturn(equity);
        when(budgetRepository.get(any(Long.class))).thenReturn(budget);
        when(budgetRepository.get(budget.getIdentifier())).thenReturn(budget);

        crossEffectManagerImplementation.createTransaction(transaction);
        verify(budgetPeriodSetterService).updateBudgetPeriodOnTransactionCreation(transaction);
    }
}