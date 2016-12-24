package budget.repository;

import budget.accessories.TestAccessories;
import budget.accessories.TestModelRepo;
import budget.config.TestingDataBaseConfig;
import budget.model.Budget;
import budget.model.User;
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

import static org.hamcrest.CoreMatchers.*;
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
public class BudgetRepositoryAndDataBaseIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BudgetRepository budgetRepository;

    private User user;

    private Budget budget;

    private Budget toUpdate;

    @Before
    public void setUp() {
        user = TestModelRepo.initBasicUserForIntegrationTesting();
        userRepository.create(user);

        budget = TestModelRepo.initBasicBudgetForIntegrationTesting();
        budget.setUser(user);

        toUpdate = TestModelRepo.initBasicBudgetForIntegrationTesting();
        toUpdate.setUser(user);
    }

    @Test
    public void shouldCreateBudget() {
        budgetRepository.create(budget);
        assertThat(budgetRepository.findByUser(user), hasItems(budget));
    }

    @Test
    public void shouldGetBudgetByIdentifier() {
        budgetRepository.create(budget);

        assertThat(budgetRepository.get(budget.getIdentifier()).getName(), is(budget.getName()));
    }

    @Test
    public void shouldNotBeAbleToDelete() {
        try {
            userRepository.delete(TestModelRepo.BASIC_ID);
            fail(TestAccessories.NO_EXCEPTION_THROWN);
        } catch (Exception e) {
        }
    }

    @Test
    public void shouldDelete() {
        budgetRepository.create(budget);

        budgetRepository.delete(budget.getIdentifier());
        assertThat(budgetRepository.get(budget.getIdentifier()), is(nullValue()));
    }

    @Test
    public void shouldUpdate() {
        budgetRepository.create(budget);

        toUpdate.setIdentifier(budget.getIdentifier());
        toUpdate.setName("New Name");

        budgetRepository.update(toUpdate);

        assertThat(budgetRepository.get(budget.getIdentifier()).getName(), is("New Name"));
    }

    @Test
    public void shouldGetAllBudgetsOfUser() {
        budgetRepository.create(budget);
        budgetRepository.create(toUpdate);

        assertThat(budgetRepository.findByUser(user), hasItems(budget, toUpdate));
    }
}