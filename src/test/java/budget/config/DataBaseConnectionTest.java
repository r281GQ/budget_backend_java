package budget.config;

import budget.model.User;
import budget.repository.interfaces.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by veghe on 13/08/2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestingDataBaseConfig.class)
@WebAppConfiguration
@Transactional
@ActiveProfiles("testing")
public class DataBaseConnectionTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void shouldConnectToDatabase(){
        userRepository.create(new User());
    }
}
