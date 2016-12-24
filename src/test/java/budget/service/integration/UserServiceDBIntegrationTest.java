package budget.service.integration;

import budget.accessories.TestModelRepo;
import budget.config.TestingDataBaseConfig;
import budget.model.User;
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

import static budget.accessories.ExceptionCatchers.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by veghe on 10/12/2016.
 */
@ActiveProfiles("testing")
@ContextConfiguration(classes = TestingDataBaseConfig.class)
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@WebAppConfiguration
public class UserServiceDBIntegrationTest {

    @Autowired
    private UserService userService;

    private User user;
    private User toUpdate;

    @Before
    public void setUp() {
        user = TestModelRepo.initBasicUser();
        user.setIdentifier(null);

        toUpdate = TestModelRepo.initBasicUser();
    }

    @Test
    public void shouldCreateUser() {
        userService.create(user);
        assertThat(userService.getById(user.getIdentifier()), is(user));
    }

    @Test
    public void shouldThrowInvalidDataExceptionOnCreationWhenIdentifierAlreadyExists() {
        user.setIdentifier(TestModelRepo.BASIC_ID);
        catchInvalidDataProvided(() -> userService.create(user));
    }

    @Test
    public void shouldThrowResourceNotFoundExceptionOnGetByIdWhenResourceIsNotPresentInDB() {
        catchResourceNotFound(() -> userService.getById(TestModelRepo.BASIC_ID));
    }

    @Test
    public void shouldThrowResourceNotFoundExceptionOnGetByEmailWhenResourceIsNotPresentInDB() {
        catchResourceNotFound(() -> userService.getByEmail(TestModelRepo.BASIC_USER_EMAIL));
    }

    @Test
    public void shouldReturnUserOnGetByEmail() {
        userService.create(user);
        userService.getByEmail(TestModelRepo.BASIC_USER_EMAIL);
        assertThat(userService.getById(user.getIdentifier()), is(user));
    }

    @Test
    public void shouldThrowResourceNotFoundExceptionOnDeleteWhenResourceIsNotPresentInDB() {
        catchResourceNotFound(() -> userService.delete(user));
    }

    @Test
    public void shouldReturnUserOnGet() {
        userService.create(user);

        assertThat(userService.getById(user.getIdentifier()).getEmail(), is(TestModelRepo.BASIC_USER_EMAIL));
    }

    @Test
    public void shouldDelete() {
        userService.create(user);
        userService.delete(user);
        assertThat(userService.getAll(), not(hasItems(user)));
    }

    @Test
    public void shouldThrowResourceNotFoundExceptionOnUpdateWhenResourceIsNotPresentInDB() {
        catchResourceNotFound(()->userService.update(user));
    }

    @Test
    public void shouldThrowInvalidDataExceptionOnUpdateWhenWrongRoleIsProvided() {
        userService.create(user);

        toUpdate.setIdentifier(user.getIdentifier());
        toUpdate.setRole("WRONG ROLE");

        catchInvalidDataProvided(()-> userService.update(toUpdate));
    }

    @Test
    public void shouldThrowInvalidDataOnCreateWhenEmailAlreadyExist() {
        userService.create(user);

        User otherUser = TestModelRepo.initBasicUser();
        otherUser.setIdentifier(null);
        otherUser.setEmail(TestModelRepo.BASIC_USER_EMAIL);

        catchResourceAlreadyExistsProvided(()->userService.create(otherUser));
    }

    @Test
    public void shouldUpdate() {
        userService.create(user);

        toUpdate.setIdentifier(user.getIdentifier());
        toUpdate.setName("name changed");

        userService.update(toUpdate);
        assertThat(userService.getById(user.getIdentifier()).getName(), is("name changed"));
    }
}
