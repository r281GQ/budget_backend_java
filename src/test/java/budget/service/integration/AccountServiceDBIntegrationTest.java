package budget.service.integration;

import budget.accessories.TestModelRepo;
import budget.config.TestingDataBaseConfig;
import budget.model.Account;
import budget.model.User;
import budget.service.interfaces.AccountService;
import budget.service.interfaces.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import static budget.accessories.ExceptionCatchers.catchInvalidDataProvided;
import static budget.accessories.ExceptionCatchers.catchResourceNotFound;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by veghe on 14/12/2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestingDataBaseConfig.class)
@ActiveProfiles("testing")
@WebAppConfiguration
@Transactional
public class AccountServiceDBIntegrationTest {

    @Autowired
    private AccountService accountService;

    @Autowired
    private UserService userService;

    private User user;

    private Account account;

    @Before
    public void setUp() {
        user = TestModelRepo.initBasicUser();
        user.setIdentifier(null);

        account = TestModelRepo.initBasicAccount();
        account.setIdentifier(null);

        userService.create(user);
        account.setUser(user);
    }

    @Test
    public void shouldCreateAccount() {
        accountService.create(account);
        assertThat(account.getIdentifier(), is(not(nullValue())));
    }

    @Test
    public void shouldThrowInvalidDataOnCreation() {
        account.setIdentifier(5L);

        catchInvalidDataProvided(() -> accountService.create(account));
    }

    @Test
    public void shouldReturnAccountOnGet() {
        accountService.create(account);

        Account fromDataBase = accountService.getById(account.getIdentifier());

        assertThat(fromDataBase.getName(), is(account.getName()));
        assertThat(fromDataBase.getUser(), is(user));
    }


    @Test
    public void shouldThrowResourceNotFoundExceptionOnGet() {
        catchResourceNotFound(() -> accountService.getById(0));
    }

    @Test
    public void shouldDeleteAccount() {
        accountService.create(account);
        accountService.delete(account);

        assertThat(accountService.getByUser(user), not(hasItems(account)));
    }

    @Test
    public void shouldAddItselfToUser() {
        accountService.create(account);
        assertThat(accountService.getByUser(user), (hasItems(account)));
    }

    @Test
    public void shouldThrowResourceNotFoundOnDeletionWhenAccountIsNull() {
        account = null;

        catchResourceNotFound(() -> accountService.delete(account));
    }

    @Test
    public void shouldThrowResourceNotFoundOnUpdateWhenAccountIsNull() {

        accountService.create(account);

        Account toUpdate = null;

        catchResourceNotFound(() -> accountService.update(toUpdate));
    }

    @Test
    public void shouldThrowResourceNotFoundOnDeletionWhenIdIsNotFound() {
        account.setIdentifier(10L);

        catchResourceNotFound(() -> accountService.delete(account));
    }

    @Test
    public void shouldThrowResourceNotFoundOnCreateWhenAccountIsNull() {
        account = null;
        catchInvalidDataProvided(() -> accountService.create(account));
    }

    @Test
    public void shouldThrowInvalidDataOnUpdate() {
        accountService.create(account);
        Account toUpdate = TestModelRepo.initBasicAccount();
        toUpdate.setIdentifier(account.getIdentifier());

        User anotherUser = new User();
        anotherUser.setIdentifier(10L);
        toUpdate.setUser(anotherUser);
        catchInvalidDataProvided(() -> accountService.update(toUpdate));
    }

    @Test
    public void shouldUpdate(){
        accountService.create(account);

        Account toUpdate = TestModelRepo.initBasicAccount();
        toUpdate.setIdentifier(account.getIdentifier());
        toUpdate.setUser(user);

        toUpdate.setName("changed name");

        accountService.update(toUpdate);

        assertThat(accountService.getById(account.getIdentifier()).getName(), is("changed name"));
    }
}
