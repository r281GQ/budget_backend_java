package budget.repository;

import budget.accessories.TestAccessories;
import budget.accessories.TestModelRepo;
import budget.config.TestingDataBaseConfig;
import budget.model.User;
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
 * Created by veghe on 07/12/2016.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestingDataBaseConfig.class)
@WebAppConfiguration
@Transactional
@ActiveProfiles("testing")
public class UserRepositoryAndDataBaseIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    private User user;

    @Before
    public void setUp(){
        user = TestModelRepo.initBasicUserForIntegrationTesting();
    }

    @Test
    public void shouldCreateUser(){
        userRepository.create(user);
    }

    @Test
    public void shouldGetUserBackByIdentifier(){
        userRepository.create(user);
        assertThat(userRepository.get(user.getIdentifier()).getEmail(), is("endre@mail.oom"));
    }

    @Test
    public void shouldGetUserBackByEmail(){
        userRepository.create(user);
        assertThat(userRepository.get(user.getIdentifier()).getEmail(), is("endre@mail.oom"));
    }

    @Test
    public void shouldNotBeAbleToDelete(){
        try {
            userRepository.delete(TestModelRepo.BASIC_ID);
            fail(TestAccessories.NO_EXCEPTION_THROWN);
        }catch (Exception e){}
    }

    @Test
    public void shouldDelete(){
        userRepository.create(user);
        userRepository.delete(user.getIdentifier());
        assertThat(userRepository.findAll(), not(hasItem(user)));
    }

    @Test
    public void shouldReturnNullByEmail(){
        User user = userRepository.getByEmail(TestModelRepo.BASIC_USER_EMAIL);
        assertThat(user, is(nullValue()));
    }

    @Test
    public void shouldUpdate(){
        userRepository.create(user);

        User toUpdate = TestModelRepo.initBasicUserForIntegrationTesting();
        toUpdate.setIdentifier(user.getIdentifier());

        toUpdate.setName("New Name");

        userRepository.update(toUpdate);

        assertThat(userRepository.get(user.getIdentifier()).getName(), is("New Name"));
    }
}