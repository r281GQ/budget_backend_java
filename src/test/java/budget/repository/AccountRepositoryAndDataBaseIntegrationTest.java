package budget.repository;

import budget.accessories.TestAccessories;
import budget.accessories.TestModelRepo;
import budget.config.TestingDataBaseConfig;
import budget.model.Account;
import budget.model.User;
import budget.repository.interfaces.AccountRepository;
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
 * Created by veghe on 07/12/2016.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestingDataBaseConfig.class)
@WebAppConfiguration
@Transactional
@ActiveProfiles("testing")
public class AccountRepositoryAndDataBaseIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    private User user;

    private Account account;

    private Account toUpdate;

    @Before
    public void setUp(){
        user = TestModelRepo.initBasicUserForIntegrationTesting();
        userRepository.create(user);

        account = TestModelRepo.initBasicAccountForIntegrationTesting();
        toUpdate = TestModelRepo.initBasicAccountForIntegrationTesting();

        account.setUser(user);
        toUpdate.setUser(user);

    }

    @Test
    public void shouldCreateAccount(){
        accountRepository.create(account);
    }

    @Test
    public void shouldGetBackAccountByIdentifier(){
        accountRepository.create(account);

        assertThat(accountRepository.get(account.getIdentifier()).getUser(), is(user));
    }

    @Test
    public void shouldDeleteAccount(){
        accountRepository.create(account);

        accountRepository.delete(account.getIdentifier());

        assertThat(accountRepository.get(account.getIdentifier()), is(nullValue()));
    }

    @Test
    public void shouldNotBeAbleToDelete(){
        try {
            accountRepository.delete(TestModelRepo.BASIC_ID);
            fail(TestAccessories.NO_EXCEPTION_THROWN);
        }catch (Exception e){}
    }

    @Test
    public void shouldUpdate(){
        accountRepository.create(account);

        toUpdate.setIdentifier(account.getIdentifier());
        toUpdate.setName("New Name");

        accountRepository.update(toUpdate);

        assertThat(accountRepository.get(account.getIdentifier()).getName(), is("New Name"));
    }

    @Test
    public void shouldFindAllAccountOfCorrespondingUser(){
        accountRepository.create(account);
        accountRepository.create(toUpdate);

        assertThat(accountRepository.findByUser(user), hasItems(account, toUpdate));
    }
}