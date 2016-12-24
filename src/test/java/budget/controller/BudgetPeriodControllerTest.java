package budget.controller;

import budget.accessories.TestModelRepo;
import budget.assembler.BudgetPeriodAssembler;
import budget.model.BudgetPeriod;
import budget.model.Period;
import budget.model.User;
import budget.service.interfaces.BudgetPeriodService;
import budget.service.interfaces.DefaultValueProviderService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;

/**
 * Created by veghe on 21/11/2016.
 */
public class BudgetPeriodControllerTest {

    private User user;

    private BudgetPeriod budgetPeriod;

    private MockMvc mockMvc;

    @Mock
    private BudgetPeriodService budgetPeriodService;

    @Spy
    private BudgetPeriodAssembler budgetPeriodAssembler = new BudgetPeriodAssembler();

    @InjectMocks
    private BudgetPeriodController budgetPeriodController = new BudgetPeriodController();

    @Mock
    private DefaultValueProviderService defaultValueProviderService;

    @InjectMocks
    private CustomExceptionTranslator customExceptionTranslator = new CustomExceptionTranslator();
    private Period period;

    @Before
    public void setUp(){
        budgetPeriod = TestModelRepo.initBasicBudgetPeriodWithDefaultUserAndPeriodAndBudget();

        initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(budgetPeriodController).setControllerAdvice(customExceptionTranslator).build();
    }

    @Test
    public void should_return_http_200_with_get() throws Exception {
        when(budgetPeriodService.getById(1l)).thenReturn(budgetPeriod);
        mockMvc.perform(get("/budgetPeriods/1")).andDo(print()).andExpect(status().isOk());
        verify(budgetPeriodService).getById(1l);
        verify(budgetPeriodAssembler).toResource(budgetPeriod);
    }

    @Test
    public void should_return_http_405_with_post_on_endpoint_get_id() throws Exception {
        when(budgetPeriodService.getById(1l)).thenReturn(budgetPeriod);
        mockMvc.perform(post("/budgetPeriods/1")).
                andDo(print()).
                andExpect(
                        jsonPath("$.httpCode",is(405))
                ).
                andExpect(status().isMethodNotAllowed());

        verifyZeroInteractions(budgetPeriodService, budgetPeriodAssembler);
    }

    @Test
    public void testLinks() throws Exception {
        when(budgetPeriodService.getById(1l)).thenReturn(budgetPeriod);
        mockMvc.perform(get("/budgetPeriods/1")).
                andDo(print()).
                andExpect(
                        jsonPath("$.identifier",is(TestModelRepo.BASIC_ID.intValue()))
                ).
                andExpect(
                        jsonPath("$.name", is(TestModelRepo.BASIC_BUDGET_PERIOD_NAME))
                ).
                andExpect(
                        jsonPath("$.allowance", is(TestModelRepo.BASIC_BUDGET_PERIOD_ALLOWANCE.doubleValue()))
                ).
                andExpect(
                        jsonPath("$.balance", is(TestModelRepo.BASIC_BUDGET_PERIOD_BALANCE.doubleValue()))
                ).
                andExpect(
                        jsonPath("$.period.representation", is(TestModelRepo.CURRENT_DATE_REPRESENTATION))
                ).
                andExpect(
                        jsonPath("$.period.period", is(nullValue()))
                ).
                andExpect(
                        jsonPath("$.budget.identifier", is(TestModelRepo.BASIC_ID.intValue()))
                ).
                andExpect(
                        jsonPath("$.budget.user", is(nullValue()))
                ).
                andExpect(
                        jsonPath("$.user.identifier", is(TestModelRepo.BASIC_ID.intValue()))
                ).
                andExpect(
                        jsonPath("$.user.role", is(TestModelRepo.ROLE_USER))
                ).
                andExpect(
                        jsonPath("$.user.email", is(TestModelRepo.BASIC_USER_EMAIL))
                ).
                andExpect(
                        jsonPath("$.user.name", is(TestModelRepo.BASIC_USER_NAME))
                ).
                andExpect(
                        jsonPath("$.user.password", is(nullValue()))
                ).
                andExpect(
                        jsonPath("$.budget.name", is(TestModelRepo.BASIC_BUDGET_NAME))
                ).
                andExpect(
                        jsonPath("$.budget.defaultAllowance", is(TestModelRepo.BASIC_BUDGET_DEFAULT_ALLOWANCE.doubleValue()))
                ).
                andExpect(
                        jsonPath("$.links[?(@.rel == 'self')].href", hasItem(containsString("budgetPeriods/"+TestModelRepo.BASIC_ID)))
                ).
                andExpect(
                        jsonPath("$.links[?(@.rel == 'self')].href", hasSize(1))
                ).
                andExpect(
                        jsonPath("$.links[?(@.rel == 'transactions')].href", hasItem(containsString("budgetPeriods/"+TestModelRepo.BASIC_ID+"/transactions")))
                ).
                andExpect(
                        jsonPath("$.links[?(@.rel == 'transactions')].href", hasSize(1))
                ).
                andExpect(
                        jsonPath("$.links[?(@.rel == 'user')].href", hasItem(containsString("users/"+TestModelRepo.BASIC_ID)))
                ).
                andExpect(
                        jsonPath("$.links[?(@.rel == 'user')].href", hasSize(1))
                ).
                andExpect(
                        jsonPath("$.links[?(@.rel == 'budget')].href", hasItem(containsString("budgets/"+TestModelRepo.BASIC_ID)))
                ).
                andExpect(
                        jsonPath("$.links[?(@.rel == 'budget')].href", hasSize(1))
                ).
                andExpect(status().isOk());
    }
}