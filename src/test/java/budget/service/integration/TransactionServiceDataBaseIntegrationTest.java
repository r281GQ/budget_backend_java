package budget.service.integration;

import budget.accessories.TestModelRepo;
import budget.accessories.builder.TransactionBuilder;
import budget.config.TestingDataBaseConfig;
import budget.model.*;
import budget.repository.interfaces.BudgetPeriodRepository;
import budget.repository.interfaces.TransactionRepository;
import budget.service.interfaces.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static budget.accessories.ExceptionCatchers.catchResourceNotFound;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by veghe on 16/12/2016.
 */@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestingDataBaseConfig.class)
@ActiveProfiles("testing")
@WebAppConfiguration
@Transactional
public class TransactionServiceDataBaseIntegrationTest {

    private User user;

    private User injectAbleUser;

    private Account account;

    private Account injectAbleAccount;

    private Grouping grouping;

    private Grouping incomeGrouping;

    private Grouping injectAbleGrouping;

    private Grouping injectAbleIncomeGrouping;

    private Budget budget;

    private Budget injectAbleBudget;

    private Transaction transaction;

    private Transaction previousTransaction;

    private BudgetPeriod previousBudgetPeriod;

    @Autowired
    private UserService userService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private GroupingService groupingService;

    @Autowired
    private BudgetPeriodRepository budgetPeriodRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private BudgetService budgetService;

    @Before
    public void setUp(){
        user = TestModelRepo.initBasicUserForIntegrationTesting();

        grouping = TestModelRepo.initBasicGroupingForIntegrationTesting();

        account = TestModelRepo.initBasicAccountForIntegrationTesting();

        incomeGrouping = TestModelRepo.initIncomeGroupingForIntegrationTesting();

        budget = TestModelRepo.initBasicBudgetForIntegrationTesting();

        userService.create(user);

        account.setUser(user);
        budget.setUser(user);
        grouping.setUser(user);
        incomeGrouping.setUser(user);

        accountService.create(account);
        groupingService.create(grouping);
        groupingService.create(incomeGrouping);
        budgetService.create(budget);

        injectAbleAccount = new Account();
        injectAbleAccount.setIdentifier(account.getIdentifier());

        injectAbleBudget = new Budget();
        injectAbleBudget.setIdentifier(budget.getIdentifier());

        injectAbleGrouping = new Grouping();
        injectAbleGrouping.setIdentifier(grouping.getIdentifier());

        injectAbleIncomeGrouping = new Grouping();
        injectAbleIncomeGrouping.setIdentifier(incomeGrouping.getIdentifier());

        injectAbleUser = new User();
        injectAbleUser.setIdentifier(user.getIdentifier());

        transaction = TransactionBuilder.initialize(user, account).
                setAmount(50).
                setGrouping(grouping).
                setCurrency(TestModelRepo.BASIC_ACCOUNT_CURRENCY).
                setName("Rent").
                    build();


        previousTransaction = TransactionBuilder.initialize(user, account).
                setAmount(5).
                setGrouping(grouping).
                setCurrency(TestModelRepo.BASIC_ACCOUNT_CURRENCY).
                setName("Rent").
                setBudget(budget).
                    build();

        previousTransaction.setAmountAtTheMomentOfTransactionForBudget(new BigDecimal(5));
        previousTransaction.setAmountAtTheMomentOfTransactionForAccount(new BigDecimal(5));
        previousTransaction.setCreationDate(TestModelRepo.PREVIOUS_DATE);

        setInjectAblesForTransaction();
    }

    @Test
    public void shouldCreate(){
        transaction = TransactionBuilder.initialize(user, account).
                setAmount(50).
                setGrouping(grouping).
                setCurrency(TestModelRepo.BASIC_ACCOUNT_CURRENCY).
                setName("Rent").
                build();


        transactionService.create(transaction);

        assertThat(transaction.getAmountAtTheMomentOfTransactionForAccount().doubleValue(),is(50.0));

        double expectedValue = TestModelRepo.BASIC_ACCOUNT_BALANCE.subtract(transaction.getAmountAtTheMomentOfTransactionForAccount()).doubleValue();

        assertThat(account.getBalance().doubleValue(),is(expectedValue));

        assertThat(transaction.getUser().getName(), is(TestModelRepo.BASIC_USER_NAME));

        assertThat(transaction.getGrouping().getName(), is(TestModelRepo.BASIC_GROUPING_NAME));

        assertThat(transaction.getAccount().getName(), is(TestModelRepo.BASIC_ACCOUNT_NAME));

        assertThat(transactionService.getByUser(user), hasItem(transaction));
        assertThat(transactionService.getByAccount(account), hasItem(transaction));
        assertThat(transactionService.getByGrouping(grouping), hasItem(transaction));
    }

    @Test
    public void shouldCreateWithOnlyIdentifiers(){
        transactionService.create(transaction);

        assertThat(transaction.getAmountAtTheMomentOfTransactionForAccount().doubleValue(),is(50.0));

        double expectedValue = TestModelRepo.BASIC_ACCOUNT_BALANCE.subtract(transaction.getAmountAtTheMomentOfTransactionForAccount()).doubleValue();

        assertThat(account.getBalance().doubleValue(),is(expectedValue));

        assertThat(transaction.getUser().getName(), is(TestModelRepo.BASIC_USER_NAME));

        assertThat(transaction.getGrouping().getName(), is(TestModelRepo.BASIC_GROUPING_NAME));

        assertThat(transaction.getAccount().getName(), is(TestModelRepo.BASIC_ACCOUNT_NAME));

        assertThat(transactionService.getByUser(user), hasItem(transaction));
        assertThat(transactionService.getByAccount(account), hasItem(transaction));
        assertThat(transactionService.getByGrouping(grouping), hasItem(transaction));
    }

    @Test
    public void shouldDeleteTransaction(){
        transactionService.create(transaction);

        transactionService.delete(transaction);

        assertThat(transactionService.getByUser(user), not(hasItem(transaction)));
        assertThat(transactionService.getByAccount(account), not(hasItem(transaction)));
        assertThat(transactionService.getByGrouping(grouping), not(hasItem(transaction)));
    }

    @Test
    public void shouldReverseAccountChangesOnDeletion(){
        transactionService.create(transaction);

        transactionService.delete(transaction);

        assertThat(accountService.getById(account.getIdentifier()).getBalance().doubleValue(), is(TestModelRepo.BASIC_ACCOUNT_BALANCE.doubleValue()));
    }

    @Test
    public void shouldIncreaseBalanceWhenUpdate(){
        transactionService.create(transaction);
        assertThat(account.getBalance().doubleValue(),is(TestModelRepo.BASIC_ACCOUNT_BALANCE.subtract(transaction.getAmountAtTheMomentOfTransactionForAccount()).doubleValue()));

        Transaction toUpdate = TransactionBuilder.initialize(injectAbleUser, injectAbleAccount).
                setAmount(50).
                setIdentifier(transaction.getIdentifier()).
                setGrouping(injectAbleIncomeGrouping).
                setCurrency(TestModelRepo.BASIC_ACCOUNT_CURRENCY).
                setName("Salary").
                    build();

        transactionService.update(toUpdate);

        assertThat(account.getBalance().doubleValue(),is(TestModelRepo.BASIC_ACCOUNT_BALANCE.add(transaction.getAmountAtTheMomentOfTransactionForAccount()).doubleValue()));

        assertThat(transactionService.getByAccount(account), not(hasItem(transaction)));
        assertThat(transactionService.getByAccount(account), (hasItem(toUpdate)));
    }

    @Test
    public void shouldDecreaseBalanceOnUpdate(){
        transactionService.create(transaction);

        assertThat(account.getBalance().doubleValue(),is(TestModelRepo.BASIC_ACCOUNT_BALANCE.subtract(transaction.getAmountAtTheMomentOfTransactionForAccount()).doubleValue()));

        Transaction toUpdate = TransactionBuilder.initialize(injectAbleUser, injectAbleAccount).
                setAmount(50).
                setIdentifier(transaction.getIdentifier()).
                setGrouping(injectAbleGrouping).
                setCurrency(Currency.EUR).
                setName("Salary").
                build();
        transactionService.update(toUpdate);

        assertThat(accountService.getById(account.getIdentifier()).getBalance().doubleValue(),is(0.0));
    }

    @Test
    public void shouldGetBudgetBackOnCreation (){
        transaction.setBudget(injectAbleBudget);
        transactionService.create(transaction);

        assertThat(transactionService.get(transaction.getIdentifier()).getBudget().getName(), is(TestModelRepo.BASIC_BUDGET_NAME));
    }

    @Test
    public void shouldCreateBudgetPeriodOnTransactionCreation (){
        transaction.setBudget(injectAbleBudget);
        transactionService.create(transaction);

        assertThat(transactionService.get(transaction.getIdentifier()).getBudgetPeriod(), is(not(nullValue())));
    }

    @Test
    public void shouldSetCurrentDateForBudgetPeriod (){
        transaction.setBudget(injectAbleBudget);
        transactionService.create(transaction);

        assertThat(transactionService.get(transaction.getIdentifier()).getBudgetPeriod().getPeriod().getRepresentation(), is(TestModelRepo.ACTUAL_DAY_REPRESENTATION));
    }

    @Test
    public void shouldSetCorrectAmountForBudgetperiod(){
        transaction.setBudget(injectAbleBudget);
        transactionService.create(transaction);

        assertThat(transactionService.get(transaction.getIdentifier()).getBudgetPeriod().getBalance().doubleValue(), is(50.5));
    }

    @Test
    public void shouldSetCorrectAmountsForConsecutiveBudgetPeriods(){
        transaction.setBudget(injectAbleBudget);
        initPreviousTransactionAndBudgetPeriod();

        transactionService.create(transaction);

        assertThat(transactionService.get(transaction.getIdentifier()).getBudgetPeriod().getBalance().doubleValue(), is(146.0));
        assertThat(transactionService.get(previousTransaction.getIdentifier()).getBudgetPeriod().getBalance().doubleValue(), is(95.5));
    }

    @Test
    public void shouldUpdateBothBudgetPeriodsWhenTransactionAmountChanged(){
        transaction.setBudget(injectAbleBudget);
        initPreviousTransactionAndBudgetPeriod();
        transactionService.create(transaction);

        Transaction toUpdate = TransactionBuilder.initialize(injectAbleUser, injectAbleAccount).
                setIdentifier(transaction.getIdentifier())
                .setBudget(injectAbleBudget)
                .setCurrency(Currency.GBP)
                .setAmount(10)
                .setGrouping(injectAbleGrouping)
                .setName("updated transaction with different amount")
                    .build();

        transactionService.update(toUpdate);

        catchResourceNotFound(()->transactionService.get(transaction.getIdentifier()));
        assertThat(transactionService.get(previousTransaction.getIdentifier()).getBudgetPeriod().getBalance().doubleValue(), is(95.5));
        assertThat(transactionService.get(toUpdate.getIdentifier()).getBudgetPeriod().getBalance().doubleValue(), is(186.0));
    }

    @Test
    public void shouldUpdateBothBudgetPeriodsWhenPreviousTransactionAmountChanged(){
        transaction.setBudget(injectAbleBudget);
        initPreviousTransactionAndBudgetPeriod();
        transactionService.create(transaction);

        Transaction toUpdate = TransactionBuilder.initialize(injectAbleUser, injectAbleAccount).
                setIdentifier(previousTransaction.getIdentifier())
                .setBudget(injectAbleBudget)
                .setCurrency(Currency.GBP)
                .setAmount(10)
                .setGrouping(injectAbleGrouping)
                .setCreationDate(previousTransaction.getCreationDate())
                .setName("updated previous transaction with different amount")
                    .build();

        transactionService.update(toUpdate);

        catchResourceNotFound(()->transactionService.get(previousTransaction.getIdentifier()));

        assertThat(transactionService.get(transaction.getIdentifier()).getBudgetPeriod().getBalance().doubleValue(), is(141.0));

        assertThat(transactionService.get(toUpdate.getIdentifier()).getBudgetPeriod().getBalance().doubleValue(), is(90.5));
    }

    @Test
    public void shouldUpdateBothBudgetPeriodsWhenPreviousTransactionCurrencyChanged(){
        transaction.setBudget(injectAbleBudget);
        initPreviousTransactionAndBudgetPeriod();
        transactionService.create(transaction);

        Transaction toUpdate = TransactionBuilder.initialize(injectAbleUser, injectAbleAccount)
                .setIdentifier(previousTransaction.getIdentifier())
                .setBudget(injectAbleBudget)
                .setCurrency(Currency.EUR)
                .setAmount(5)
                .setGrouping(injectAbleGrouping)
                .setCreationDate(previousTransaction.getCreationDate())
                .setName("updated previous transaction with different currency")
                    .build();

        transactionService.update(toUpdate);

        catchResourceNotFound(()->transactionService.get(previousTransaction.getIdentifier()));
        assertThat(transactionService.get(transaction.getIdentifier()).getBudgetPeriod().getBalance().doubleValue(), is(141.0));
        assertThat(transactionService.get(toUpdate.getIdentifier()).getBudgetPeriod().getBalance().doubleValue(), is(90.5));
    }

    @Test
    public void shouldUpdateBothBudgetPeriodsWhenPreviousTransactionRemovedFromBudget(){
        transaction.setBudget(injectAbleBudget);
        initPreviousTransactionAndBudgetPeriod();
        transactionService.create(transaction);

        Transaction toUpdate = TransactionBuilder.initialize(injectAbleUser, injectAbleAccount)
                .setIdentifier(previousTransaction.getIdentifier())
                .setCurrency(Currency.GBP)
                .setAmount(5)
                .setGrouping(injectAbleGrouping)
                .setCreationDate(previousTransaction.getCreationDate())
                .setName("updated previous transaction with no budget")
                .build();

        transactionService.update(toUpdate);

        catchResourceNotFound(()->transactionService.get(previousTransaction.getIdentifier()));
        assertThat(transactionService.get(transaction.getIdentifier()).getBudgetPeriod().getBalance().doubleValue(), is(151.0));
        assertThat(transactionService.get(toUpdate.getIdentifier()).getBudgetPeriod(), is(nullValue()));
    }

    private void initPreviousTransactionAndBudgetPeriod() {
        previousBudgetPeriod = TestModelRepo.initBasicBudgetPeriod();
        previousBudgetPeriod.setIdentifier(null);
        previousBudgetPeriod.setUser(user);
        previousBudgetPeriod.setBudget(budget);
        previousBudgetPeriod.setName(budget.getName());
        previousBudgetPeriod.setAllowance(budget.getDefaultAllowance());
        previousBudgetPeriod.setPeriod(TestModelRepo.initPreviousPeriod());

        budgetPeriodRepository.create(previousBudgetPeriod);

        previousTransaction.setBudgetPeriod(previousBudgetPeriod);
        transactionRepository.create(previousTransaction);
    }

    private void setInjectAblesForTransaction() {
        transaction.setUser(injectAbleUser);
        transaction.setAccount(injectAbleAccount);
        transaction.setGrouping(injectAbleGrouping);
    }

    private void setInjectAblesForPreviousTransaction() {
        previousTransaction.setUser(injectAbleUser);
        previousTransaction.setAccount(injectAbleAccount);
        previousTransaction.setGrouping(injectAbleGrouping);
    }
}
