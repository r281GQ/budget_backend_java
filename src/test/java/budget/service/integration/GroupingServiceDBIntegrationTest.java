package budget.service.integration;

import budget.accessories.TestModelRepo;
import budget.config.TestingDataBaseConfig;
import budget.model.Grouping;
import budget.model.User;
import budget.service.interfaces.GroupingService;
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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by veghe on 14/12/2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestingDataBaseConfig.class)
@ActiveProfiles("testing")
@WebAppConfiguration
@Transactional
public class GroupingServiceDBIntegrationTest {

    @Autowired
    private GroupingService groupingService;

    @Autowired
    private UserService userService;

    private User user;

    private Grouping grouping;

    private User toInject;

    @Before
    public void setUp() {
        user = TestModelRepo.initBasicUser();
        user.setIdentifier(null);

        grouping = TestModelRepo.initBasicGrouping();
        grouping.setIdentifier(null);

        userService.create(user);
        grouping.setUser(user);

        toInject = new User();
        toInject.setIdentifier(user.getIdentifier());
    }

    @Test
    public void shouldCreateGrouping(){
        groupingService.create(grouping);

        assertThat(groupingService.getById(grouping.getIdentifier()).getName(), is(grouping.getName()));
    }


    @Test
    public void shouldReturnGroupingOnGet(){
        groupingService.create(grouping);

        assertThat(groupingService.getById(grouping.getIdentifier()).getName(), is(grouping.getName()));
    }


    @Test
    public void shouldReturnUserFromDBOnCreation(){
        toInject = new User();

        toInject.setIdentifier(user.getIdentifier());
        grouping.setUser(toInject);

        groupingService.create(grouping);

        assertThat(groupingService.getById(grouping.getIdentifier()).getUser().getName(), is(user.getName()));
    }

    @Test
    public void shouldReturnUserFromDBOnUpdate(){
        grouping.setUser(toInject);

        groupingService.create(grouping);

        assertThat(groupingService.getById(grouping.getIdentifier()).getUser().getName(), is(user.getName()));

        Grouping toUpdate = TestModelRepo.initUpdateGrouping(toInject, grouping);

        groupingService.update(toUpdate);

        assertThat(groupingService.getById(grouping.getIdentifier()).getUser().getName(), is(user.getName()));
    }

}