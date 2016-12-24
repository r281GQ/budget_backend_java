package budget.service;

import budget.accessories.TestAccessories;
import budget.accessories.TestModelRepo;
import budget.accessories.builder.TransactionBuilder;
import budget.accessories.validators.ValidationService;
import budget.controller.exceptions.ResourceNotFoundException;
import budget.model.*;
import budget.repository.interfaces.TransactionRepository;
import budget.service.interfaces.CrossEffectManager;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

/**
 * Created by veghe on 14/08/2016.
 */
public class TransactionServiceImplementationTest {

    @Captor
    private ArgumentCaptor <Transaction> transactionArgumentCaptor;

    @Mock
    private CrossEffectManager crossEffectManager;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionServiceImplementation transactionServiceImplementation = new TransactionServiceImplementation();

    private Account account;

    private User user;

    private Grouping grouping;

    private Transaction transaction;

    private Equity equity;

    private Period period;

    private Budget budget;
    @Mock
    private ValidationService validationService;

    @Before
    public void setUp() {
        transaction = TransactionBuilder.initialize(user, account)
                .setAmount(50)
                .setCurrency(Currency.GBP)
                .setGrouping(grouping)
                .setIdentifier(1L)
                .setName("Loan")
                .setPeriod(period)
                .setBudget(budget)
                .setAmountAtTheMomentOfTransactionForAccount(50)
                    .build();
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldThrowResourceNotFoundOnGet(){
        when(transactionRepository.get(TestModelRepo.BASIC_ID)).thenReturn(null);

        try {
            transactionServiceImplementation.get(TestModelRepo.BASIC_ID);
            fail(TestAccessories.NO_EXCEPTION_THROWN);
        }catch (ResourceNotFoundException e){

        }catch (Exception e){
            fail(TestAccessories.WRONG_EXCEPTION + e.getClass().getTypeName());
        }
        verify(transactionRepository).get(TestModelRepo.BASIC_ID);
    }

    @Test
    public void shouldReturnTransactionOnGet(){
        when(transactionRepository.get(TestModelRepo.BASIC_ID)).thenReturn(transaction);

        transactionServiceImplementation.get(TestModelRepo.BASIC_ID);
        verify(transactionRepository).get(TestModelRepo.BASIC_ID);
    }

    @Test
    public void shouldCreate(){
        transaction.setIdentifier(null);
        doAnswer(new Answer() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                transaction.setIdentifier(1L);
                return null;
            }
        }).when(crossEffectManager).createTransaction(any(Transaction.class));
        transactionServiceImplementation.create(transaction);
        verify(crossEffectManager).createTransaction(transaction);
    }

    @Test
    public void shouldThrowResourceNotFoundOnDeletion(){
        when(transactionRepository.get(TestModelRepo.BASIC_ID)).thenReturn(null);

        try {
            transactionServiceImplementation.delete(transaction);
            fail(TestAccessories.NO_EXCEPTION_THROWN);
        }catch (ResourceNotFoundException e){

        }catch (Exception e){
            fail(TestAccessories.WRONG_EXCEPTION + e.getClass().getTypeName());
        }
        verify(transactionRepository).get(TestModelRepo.BASIC_ID);
        verify(crossEffectManager, times(0)).deleteTransaction(transaction);
    }

    @Test
    public void shouldDelete(){
        when(transactionRepository.get(TestModelRepo.BASIC_ID)).thenReturn(transaction);

        transactionServiceImplementation.delete(transaction);
        verify(transactionRepository).get(TestModelRepo.BASIC_ID);
        verify(crossEffectManager, times(1)).deleteTransaction(transaction);
    }

    @Test
    public void shouldThrowResourceNotFoundOnUpdate(){
        when(transactionRepository.get(TestModelRepo.BASIC_ID)).thenReturn(null);

        try {
            transactionServiceImplementation.update(transaction);
            fail(TestAccessories.NO_EXCEPTION_THROWN);
        }catch (ResourceNotFoundException e){

        }catch (Exception e){
            fail(TestAccessories.WRONG_EXCEPTION + e.getClass().getTypeName());
        }
        verify(transactionRepository).get(TestModelRepo.BASIC_ID);
        verify(crossEffectManager, times(0)).deleteTransaction(transaction);
        verify(crossEffectManager, times(0)).createTransaction(transaction);
    }

    @Test
    public void shouldUpdate(){

        when(validationService.isUpdateAble(transaction)).thenReturn(true);

        assertThat(transaction.getCreationDate(), is(nullValue()));

        Transaction original = new Transaction();
        original.setIdentifier(TestModelRepo.BASIC_ID);
        original.setCreationDate(TestModelRepo.CURRENT_DATE);

        when(transactionRepository.get(TestModelRepo.BASIC_ID)).thenReturn(original);

        transactionServiceImplementation.update(transaction);

        verify(transactionRepository).get(TestModelRepo.BASIC_ID);
        verify(crossEffectManager, times(1)).deleteTransaction(transactionArgumentCaptor.capture());
        assertThat(transactionArgumentCaptor.getValue().getCreationDate(), is(nullValue()));
        verify(crossEffectManager, times(1)).createTransaction(transactionArgumentCaptor.capture());
        assertThat(transactionArgumentCaptor.getValue().getCreationDate(), is(TestModelRepo.CURRENT_DATE));
    }
}