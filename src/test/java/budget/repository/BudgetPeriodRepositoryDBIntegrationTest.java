package budget.repository;

/**
 * Created by veghe on 08/12/2016.
 */

import budget.accessories.TestAccessories;
import budget.accessories.TestModelRepo;
import budget.config.TestingDataBaseConfig;
import budget.model.Budget;
import budget.model.BudgetPeriod;
import budget.model.User;
import budget.repository.interfaces.BudgetPeriodRepository;
import budget.repository.interfaces.BudgetRepository;
import budget.repository.interfaces.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

/**
 * Created by veghe on 08/12/2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestingDataBaseConfig.class)
@WebAppConfiguration
@Transactional
@ActiveProfiles("testing")
public class BudgetPeriodRepositoryDBIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private BudgetPeriodRepository budgetPeriodRepository;

    private User user;

    private Budget budget;

    private BudgetPeriod budgetPeriod;

    private BudgetPeriod previousPeriod;

    private BudgetPeriod nextPeriod;

    private BudgetPeriod toUpdate;

    @Before
    public void setUp(){
        user = TestModelRepo.initBasicUserForIntegrationTesting();
        userRepository.create(user);

        budget = TestModelRepo.initBasicBudgetForIntegrationTesting();
        budget.setUser(user);

        budgetRepository.create(budget);

        budgetPeriod = TestModelRepo.initBasicBudgetPeriodWithDefaultUserAndPeriodAndBudget();
        toUpdate = TestModelRepo.initBasicBudgetPeriodWithDefaultUserAndPeriodAndBudget();
        previousPeriod = TestModelRepo.initBasicBudgetPeriodWithDefaultUserAndPeriodAndBudget();

        previousPeriod.setUser(user);
        previousPeriod.setBudget(budget);
        previousPeriod.setPeriod(TestModelRepo.initPreviousPeriod());
        previousPeriod.setIdentifier(null);

        toUpdate.setBudget(budget);
        toUpdate.setUser(user);

        budgetPeriod.setIdentifier(null);
        budgetPeriod.setBudget(budget);
        budgetPeriod.setUser(user);
    }

    @Test
    public void shouldCreateBudgetPeriod(){
        budgetPeriodRepository.create(budgetPeriod);
    }

    @Test
    public void shouldGetBudgetPeriod(){
        budgetPeriodRepository.create(budgetPeriod);

        assertThat(budgetPeriodRepository.get(budgetPeriod.getIdentifier()).getPeriod(), is(TestModelRepo.initBasicPeriod()));
    }

    @Test
    public void shouldDelete(){
        budgetPeriodRepository.create(budgetPeriod);

        budgetPeriodRepository.delete(budgetPeriod.getIdentifier());

        assertThat(budgetPeriodRepository.get(budgetPeriod.getIdentifier()), is(nullValue()));
    }

    @Test
    public void shouldNotBeAbleToDelete(){
        try {
            budgetPeriodRepository.delete(budgetPeriod.getIdentifier());
            fail(TestAccessories.NO_EXCEPTION_THROWN);
        }catch (Exception e){}
    }

    @Test
    public void shouldBeAbleToUpdate(){
        budgetPeriodRepository.create(budgetPeriod);

        toUpdate.setIdentifier(budgetPeriod.getIdentifier());
        toUpdate.setName("New Name");

        budgetPeriodRepository.update(toUpdate);

        assertThat(budgetPeriodRepository.get(budgetPeriod.getIdentifier()).getName(), is("New Name"));
    }

    @Test
    public void shouldReturnBudgetPeriodByBudgetAndPeriod(){
        budgetPeriodRepository.create(budgetPeriod);
        budgetPeriodRepository.create(previousPeriod);

        assertThat(budgetPeriodRepository.findByBudgetAndPeriod(budget, TestModelRepo.initBasicPeriod()), is(budgetPeriod));
    }

    @Test
    public void shouldReturnBudgetPeriodsByBudget(){
        budgetPeriodRepository.create(budgetPeriod);
        budgetPeriodRepository.create(previousPeriod);

        assertThat(budgetPeriodRepository.findByBudget(budget), hasItems(budgetPeriod, previousPeriod));
    }

    @Test
    public void shouldReturnBudgetPeriodsByUser(){
        budgetPeriodRepository.create(budgetPeriod);
        budgetPeriodRepository.create(previousPeriod);

        assertThat(budgetPeriodRepository.findByUser(user), hasItems(budgetPeriod, previousPeriod));
    }

    @Test
    public void shouldReturnBudgetPeriodsInOrderAttempt1(){
        budgetPeriodRepository.create(budgetPeriod);
        budgetPeriodRepository.create(previousPeriod);

        assertThat(budgetPeriodRepository.findByBudgetOrderedByDate(budget).get(0), is(previousPeriod));
        assertThat(budgetPeriodRepository.findByBudgetOrderedByDate(budget).get(1), is(budgetPeriod));
    }

    @Test
    public void shouldReturnBudgetPeriodsInOrderAttempt2(){
        budgetPeriodRepository.create(previousPeriod);
        budgetPeriodRepository.create(budgetPeriod);

        assertThat(budgetPeriodRepository.findByBudgetOrderedByDate(budget).get(0), is(previousPeriod));
        assertThat(budgetPeriodRepository.findByBudgetOrderedByDate(budget).get(1), is(budgetPeriod));
    }
}

