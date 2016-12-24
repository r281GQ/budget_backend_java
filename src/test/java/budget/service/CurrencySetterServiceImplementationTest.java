package budget.service;

import budget.accessories.TestAccessories;
import budget.accessories.TestModelRepo;
import budget.accessories.builder.TransactionBuilder;
import budget.model.*;
import budget.service.interfaces.ExchangeService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by veghe on 06/12/2016.
 */
public class CurrencySetterServiceImplementationTest {

    private User user;

    private Account account;

    private Period period;

    private Grouping grouping;

    private Transaction transaction;

    private Equity equity;

    @Mock
    private ExchangeService exchangeService;

    @InjectMocks
    private CurrencySetterServiceImplementation currencySetterServiceImplementation = new CurrencySetterServiceImplementation();

    @Before
    public void setUp(){

        grouping = TestModelRepo.initBasicGroupingWithDefaultUser();

        equity = TestModelRepo.initBasicEquityWithDefaultUser();
        equity.setCurrency(Currency.HUF);

        user = TestModelRepo.initBasicUser();

        account = TestModelRepo.initBasicAccount();
        account.setCurrency(Currency.EUR);

        period = TestModelRepo.initBasicPeriod();

        transaction = TransactionBuilder.initialize(user, account)
                .setAmount(50)
                .setCurrency(Currency.GBP)
                .setGrouping(grouping)
                .setIdentifier(1L)
                .setName("Loan")
                .setPeriod(period)
                .setEquity(equity)
//                .setAmountAtTheMomentOfTransactionForAccount(50)
//                .setAmountAtTheMomentOfTransactionForEQ(50)
                .build();

        initMocks(this);
    }

    @Test
    public void shouldSetAccountValueTo75AndEQValueTo8000(){
        when(exchangeService.getRate("GBPEUR")).thenReturn(TestAccessories.GBPEUR);
        when(exchangeService.getRate("GBPHUF")).thenReturn(TestAccessories.GBPHUF);

        currencySetterServiceImplementation.setValues(transaction);

        assertThat(transaction.getAmountAtTheMomentOfTransactionForAccount().doubleValue(), is(75.0));
        assertThat(transaction.getAmountAtTheMomentOfTransactionForEQ().doubleValue(), is(20000.0));
    }
}