package budget.service;

import budget.accessories.TestAccessories;
import budget.accessories.TestModelRepo;
import budget.accessories.validators.ValidationService;
import budget.controller.exceptions.InvalidDataProvidedException;
import budget.controller.exceptions.ResourceNotFoundException;
import budget.model.Account;
import budget.model.User;
import budget.repository.interfaces.AccountRepository;
import budget.service.interfaces.AccountService;
import budget.service.interfaces.CrossEffectManager;
import budget.service.interfaces.DefaultValueProviderService;
import budget.service.interfaces.DeleteHelperService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by veghe on 14/08/2016.
 */
public class AccountServiceImplementationTest {

    @Mock
    private ValidationService validationService;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private DeleteHelperService deleteHelperService;

    @Mock
    private CrossEffectManager crossEffectManager;

    @Mock
    private DefaultValueProviderService defaultValueProviderService;

    @InjectMocks
    private AccountService accountService = new AccountServiceImplementation();

    private Account account;

    private User user;

    @Before
    public void setUp() throws Exception {

        initMocks(this);

        user = TestModelRepo.initBasicUser();

        account = TestModelRepo.initBasicAccount();
        account.setUser(user);
    }

    @Test
    public void shouldReturnAccountOnGet(){
        when(accountRepository.get(TestModelRepo.BASIC_ID)).thenReturn(account);
        accountService.getById(TestModelRepo.BASIC_ID);

        verify(accountRepository, times(1)).get(TestModelRepo.BASIC_ID);
    }

    @Test
    public void shouldThrowResourceNotFoundExceptionOnGet(){
        when(accountRepository.get(TestModelRepo.BASIC_ID)).thenReturn(null);

        try {
            accountService.getById(1L);
            fail(TestAccessories.NO_EXCEPTION_THROWN);
        }catch (ResourceNotFoundException e){

        }catch (Exception e){
            fail(TestAccessories.WRONG_EXCEPTION + e.getClass().getTypeName());
        }

        verify(accountRepository, times(1)).get(TestModelRepo.BASIC_ID);
    }

    @Test
    public void shouldCreateAccount(){
        when(crossEffectManager.getUserById(account.getIdentifier())).thenReturn(user);

//        doAnswer(nothing -> {account.setIdentifier(1L); return null;}).when(accountRepository.create(account));

//        doAnswer(invocation -> )

        account.setIdentifier(null);
        accountService.create(account);

        verify(accountRepository, times(1)).create(any(Account.class));
    }

    @Test
    public void shouldThrowResourceNotFoundOnUpdate(){
        when(accountRepository.get(TestModelRepo.BASIC_ID)).thenReturn(null);

        try {
            accountService.update(account);
            fail(TestAccessories.NO_EXCEPTION_THROWN);
        }catch (ResourceNotFoundException e){

        }catch (Exception e){
            fail(TestAccessories.WRONG_EXCEPTION + e.getClass().getTypeName());
        }

        verify(accountRepository, times(0)).update(account);
        verify(accountRepository, times(1)).get(TestModelRepo.BASIC_ID);
    }

    @Test
    public void shouldThrowInvalidDataOnUpdate(){
        when(accountRepository.get(TestModelRepo.BASIC_ID)).thenReturn(account);
        when(validationService.isUpdateAble(account)).thenReturn(false);

        when(defaultValueProviderService.getExceptionMessages(InvalidType.INVALIDATION_ACCOUNT)).thenReturn("");

        try {
            accountService.update(account);
            fail(TestAccessories.NO_EXCEPTION_THROWN);
        }catch (InvalidDataProvidedException e){

        }catch (Exception e){
            fail(TestAccessories.WRONG_EXCEPTION + e.getClass().getTypeName());
        }

        verify(accountRepository, times(0)).update(account);
        verify(accountRepository, times(1)).get(TestModelRepo.BASIC_ID);
    }


    @Test
    public void shouldDeleteAccount(){
        when(accountRepository.get(account.getIdentifier())).thenReturn(account);

        accountService.delete(account);
        verify(deleteHelperService, times(1)).deleteAccount(account);
    }

    @Test
    public void shouldThrowResourceNotFoundExceptionWhenAccountForDeletionIsNotInDatabase(){
        when(accountRepository.get(TestModelRepo.BASIC_ID)).thenReturn(null);

        try {
            accountService.delete(account);
            fail(TestAccessories.NO_EXCEPTION_THROWN);
        }catch (ResourceNotFoundException e){

        }catch (Exception e){
            fail(TestAccessories.WRONG_EXCEPTION + e.getClass().getTypeName());
        }

        verify(deleteHelperService, times(0)).deleteAccount(account);
        verify(accountRepository, times(1)).get(TestModelRepo.BASIC_ID);
    }
}