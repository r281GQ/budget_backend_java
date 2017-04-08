package budget.accessories;

import budget.model.*;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by veghe on 25/11/2016.
 */
public class TestModelRepo {

    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("MM-yyyy");

    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String ROLE_USER = "ROLE_USER";
    public static final String BASIC_USER_EMAIL = "endre@mail.oom";
    public static final String BASIC_USER_NAME = "Endre Vegh";
    public static final String BASIC_USER_PASSWORD = "password" ;

    public static String ACTUAL_DAY_REPRESENTATION;

    public static final String PREVIOUS_DATE_REPRESENTATION = "10-2016";
    public static Date PREVIOUS_DATE;

    public static final String CURRENT_DATE_REPRESENTATION = "11-2016";
    public static Date CURRENT_DATE;

    public static final String NEXT_DATE_REPRESENTATION = "12-2016";
    public static Date NEXT_DATE_DATE;

    static {
        try {
            ACTUAL_DAY_REPRESENTATION = SIMPLE_DATE_FORMAT.format(new Date());

            PREVIOUS_DATE = SIMPLE_DATE_FORMAT.parse(PREVIOUS_DATE_REPRESENTATION);
            CURRENT_DATE = SIMPLE_DATE_FORMAT.parse(CURRENT_DATE_REPRESENTATION);
            NEXT_DATE_DATE = SIMPLE_DATE_FORMAT.parse(NEXT_DATE_REPRESENTATION);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public static final Long BASIC_ID = 1L;

    public static final String BASIC_BUDGET_NAME = "Food";
    public static final BigDecimal BASIC_BUDGET_DEFAULT_ALLOWANCE = new BigDecimal(100.5);

    public static final String BASIC_BUDGET_PERIOD_NAME = "Food";
    public static final BigDecimal BASIC_BUDGET_PERIOD_ALLOWANCE = new BigDecimal(150.5);
    public static final BigDecimal BASIC_BUDGET_PERIOD_BALANCE = new BigDecimal(50.5);

    public static final BigDecimal BASIC_ACCOUNT_BALANCE = new BigDecimal(100);
    public static final Currency BASIC_ACCOUNT_CURRENCY = Currency.GBP;
    public static final String BASIC_ACCOUNT_NAME = "Main";

    public static final EQType BASIC_EQUITY_TYPE = EQType.LIABILITY;
    public static final String BASIC_EQUITY_NAME = "Loan";
    public static final Currency BASIC_EQUITY_CURRENCY = Currency.GBP;
    public static final BigDecimal BASIC_EQUITY_BALANCE = new BigDecimal(100);

    public static final String BASIC_GROUPING_NAME = "Rent";
    public static final Type BASIC_GROUPING_TYPE = Type.EXPENSE;

    public static final String INCOME_GROUPING_NAME = "Salary";
    public static final Type INCOME_GROUPING_TYPE = Type.INCOME;

    public static Grouping initIncomeGroupingForIntegrationTesting(){
        Grouping grouping = initIncomeGrouping();
        grouping.setIdentifier(null);
        return grouping;
    }

    public static Grouping initIncomeGrouping(){
        Grouping grouping = new Grouping();
        grouping.setIdentifier(BASIC_ID);
        grouping.setName(INCOME_GROUPING_NAME);
        grouping.setType(INCOME_GROUPING_TYPE);
        return grouping;
    }

    public static User initBasicUser(){
        User user = new User();
        user.setIdentifier(BASIC_ID);
        user.setRole(ROLE_USER);
        user.setEmail(BASIC_USER_EMAIL);
        user.setName(BASIC_USER_NAME);
        user.setPassword(BASIC_USER_PASSWORD);
        return user;
    }

    public static Period initBasicPeriod() {
        Period period = new Period();
        period.setPeriod(CURRENT_DATE);
        period.setRepresentation(CURRENT_DATE_REPRESENTATION);
        return period;
    }

    public static Period initPreviousPeriod() {
        Period period = new Period();
        period.setPeriod(PREVIOUS_DATE);
        period.setRepresentation(PREVIOUS_DATE_REPRESENTATION);
        return period;
    }

    public static Budget initBasicBudget(){
        Budget budget = new Budget();
        budget.setCurrency(Currency.GBP);
        budget.setIdentifier(BASIC_ID);
        budget.setName(BASIC_BUDGET_NAME);
        budget.setDefaultAllowance(BASIC_BUDGET_DEFAULT_ALLOWANCE);
        return budget;
    }

    public static Budget initBasicBudgetWithDefaultUser(){
        Budget budget = initBasicBudget();
        budget.setUser(initBasicUser());
        return budget;
    }

    public static BudgetPeriod initBasicBudgetPeriod() {
        BudgetPeriod budgetPeriod = new BudgetPeriod();
        budgetPeriod.setAllowance(BASIC_BUDGET_PERIOD_ALLOWANCE);
        budgetPeriod.setBalance(BASIC_BUDGET_PERIOD_BALANCE);
        budgetPeriod.setIdentifier(BASIC_ID);
        budgetPeriod.setName(BASIC_BUDGET_PERIOD_NAME);
        return  budgetPeriod;
    }

    public static BudgetPeriod initBasicBudgetPeriodWithDefaultUserAndPeriodAndBudget() {
        BudgetPeriod budgetPeriod = initBasicBudgetPeriod();
        budgetPeriod.setPeriod(initBasicPeriod());
        budgetPeriod.setUser(initBasicUser());
        budgetPeriod.setBudget(initBasicBudgetWithDefaultUser());
        return  budgetPeriod;
    }

    public static BudgetPeriod initPreviousBudgetPeriodWithDefaultUserAndPeriodAndBudget() {
        BudgetPeriod budgetPeriod = initBasicBudgetPeriod();
        budgetPeriod.setPeriod(initPreviousPeriod());
        budgetPeriod.setIdentifier(2L);
        budgetPeriod.setUser(initBasicUser());
        budgetPeriod.setBudget(initBasicBudgetWithDefaultUser());
        return  budgetPeriod;
    }

    public static Account initBasicAccount(){
        Account account = new Account();
        account.setIdentifier(BASIC_ID);
        account.setName(BASIC_ACCOUNT_NAME);
        account.setBalance(BASIC_ACCOUNT_BALANCE);
        account.setCurrency(BASIC_ACCOUNT_CURRENCY);
        return account;
    }

    public static Account initBasicAccountWithDefaultUser(){
        Account account =initBasicAccount();
        account.setUser(initBasicUser());
        return account;
    }


    public static Equity initBasicEquity(){
        Equity equity = new Equity();
        equity.setIdentifier(BASIC_ID);
        equity.setType(BASIC_EQUITY_TYPE);
        equity.setName(BASIC_EQUITY_NAME);
        equity.setCurrency(BASIC_EQUITY_CURRENCY);
        equity.setBalance(BASIC_EQUITY_BALANCE);
        return equity;
    }

    public static Equity initBasicEquityWithDefaultUser(){
        Equity equity = initBasicEquity();
        equity.setUser(initBasicUser());
        return equity;
    }

    public static Grouping initBasicGrouping(){
        Grouping grouping = new Grouping();
        grouping.setIdentifier(BASIC_ID);
        grouping.setName(BASIC_GROUPING_NAME);
        grouping.setType(BASIC_GROUPING_TYPE);
        return grouping;
    }

    public static Grouping initBasicGroupingWithDefaultUser(){
        Grouping grouping = initBasicGrouping();
        grouping.setUser(initBasicUser());
        return grouping;
    }

    public static Grouping initUpdateGrouping(User user, Grouping grouping){
        Grouping toReturn = initBasicGrouping();
        toReturn.setUser(user);
        toReturn.setIdentifier(grouping.getIdentifier());
        return toReturn;
    }

    public static User initBasicUserForIntegrationTesting(){
        User user = initBasicUser();
        user.setIdentifier(null);
        return user;
    }

    public static Grouping initBasicGroupingForIntegrationTesting(){
        Grouping grouping = initBasicGrouping();
        grouping.setIdentifier(null);
        return grouping;
    }

    public static Account initBasicAccountForIntegrationTesting(){
        Account account = initBasicAccount();
        account.setIdentifier(null);
        return account;
    }

    public static Budget initBasicBudgetForIntegrationTesting(){
        Budget budget = initBasicBudget();
        budget.setIdentifier(null);
        return budget;
    }
}
