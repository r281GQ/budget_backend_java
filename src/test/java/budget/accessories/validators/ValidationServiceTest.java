package budget.accessories.validators;

import budget.accessories.TestModelRepo;
import budget.model.Account;
import budget.model.Currency;
import budget.model.User;
import budget.repository.interfaces.AccountRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.math.BigDecimal;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by veghe on 02/12/2016.
 */
public class ValidationServiceTest {

    @Mock
    private AccountRepository accountRepository;

    private Account database;
    private Account provided;

    private User user;
    private User otherUser;

    @InjectMocks
    private ValidationServiceImplementation validationServiceImplementation = new ValidationServiceImplementation();

    @Before
    public void setUp(){
        initMocks(this);

        user = TestModelRepo.initBasicUser();
        otherUser = new User();
        otherUser.setIdentifier(5l);

        database = TestModelRepo.initBasicAccount();
        database.setUser(user);

        provided = TestModelRepo.initBasicAccount();
        provided.setUser(user);

        when(accountRepository.get(TestModelRepo.BASIC_ID)).thenReturn(database);
    }

    @Test
    public void shouldReturnTrueWhenAccountsElementsAreEqual(){
        assertThat(validationServiceImplementation.isUpdateAble(provided), is(true));
    }

    @Test
    public void shouldReturnFalseWhenBalancesAreDifferent(){
        database.setBalance(new BigDecimal(500));
        assertThat(validationServiceImplementation.isUpdateAble(provided), is(false));
    }
    @Test
    public void shouldReturnFalseWhenCurrenciesAreDifferent(){
        database.setCurrency(Currency.EUR);
        assertThat(validationServiceImplementation.isUpdateAble(provided), is(false));
    }

    @Test
    public void shouldReturnFalseWhenUsersAreDifferent(){
        database.setUser(otherUser);
        assertThat(validationServiceImplementation.isUpdateAble(provided), is(false));
    }


    @Test
    public void shouldReturnTrueWhenOnlyNamesAreDifferent(){
        database.setName("Side");
        assertThat(validationServiceImplementation.isUpdateAble(provided), is(true));
    }
}