package budget.service;

import budget.accessories.TestAccessories;
import budget.accessories.TestModelRepo;
import budget.accessories.validators.ValidationService;
import budget.controller.exceptions.InvalidDataProvidedException;
import budget.controller.exceptions.ResourceAlreadyExists;
import budget.controller.exceptions.ResourceNotFoundException;
import budget.model.User;
import budget.repository.interfaces.UserRepository;
import budget.service.interfaces.DefaultValueProviderService;
import budget.service.interfaces.DeleteHelperService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by veghe on 17/11/2016.
 */
public class UserServiceImplementationTest {

    @Mock
    private DefaultValueProviderService defaultValueProviderService;

    @Mock
    private ValidationService validationService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private DeleteHelperService deleteHelper;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @InjectMocks
    private UserServiceImplementation userServiceImplementation = new UserServiceImplementation();

    private User user;

    @Before
    public void setUp(){
        user = TestModelRepo.initBasicUser();

        initMocks(this);
        when(defaultValueProviderService.getDefaultRole()).thenReturn("ROLE_USER");
    }

    @Test
    public void shouldSetRoleToDefaultOnCreation(){
        user.setIdentifier(null);
        when(userRepository.getByEmail(user.getEmail())).thenReturn(null);

        userServiceImplementation.create(user);
        assertThat(user.getRole(), is(TestModelRepo.ROLE_USER));
    }

    @Test
    public void shouldThrowInvalidDataIfUserAlreadyExistsOnCreation(){
        user.setIdentifier(null);
        when(userRepository.getByEmail(user.getEmail())).thenReturn(new User());

        try {
            userServiceImplementation.create(user);
            fail(TestAccessories.NO_EXCEPTION_THROWN);
        }catch (ResourceAlreadyExists e){

        }catch (Exception e){
            fail(TestAccessories.WRONG_EXCEPTION + e.getClass().getTypeName());
        }
        assertThat(user.getRole(), is(TestModelRepo.ROLE_USER));
        verify(userRepository, times(0)).create(user);
    }

    @Test
    public void shouldThrowResourceNotFoundOnUpdate(){
        when(userRepository.get(TestModelRepo.BASIC_ID)).thenReturn(null);

        try {
            userServiceImplementation.update(user);
            fail(TestAccessories.NO_EXCEPTION_THROWN);
        }catch (ResourceNotFoundException e){

        }catch (Exception e){
            fail(TestAccessories.WRONG_EXCEPTION + e.getClass().getTypeName());
        }
        verify(userRepository, times(0)).update(user);
    }

    @Test
    public void shouldThrowInvalidDateOnUpdate(){
        when(userRepository.get(TestModelRepo.BASIC_ID)).thenReturn(user);

        when(validationService.isUpdateAble(user)).thenReturn(false);

        try {
            userServiceImplementation.update(user);
            fail(TestAccessories.NO_EXCEPTION_THROWN);
        }catch (InvalidDataProvidedException e){

        }catch (Exception e){
            fail(TestAccessories.WRONG_EXCEPTION + e.getClass().getTypeName());
        }
        verify(userRepository, times(0)).update(user);
    }

    @Test
    public void shouldOnUpdate(){
        when(userRepository.get(TestModelRepo.BASIC_ID)).thenReturn(user);

        when(validationService.isUpdateAble(user)).thenReturn(true);

        userServiceImplementation.update(user);
        verify(userRepository, times(1)).update(user);
    }

    @Test
    public void shouldThrowResourceNotFoundOnGet(){
        when(userRepository.get(TestModelRepo.BASIC_ID)).thenReturn(null);

        try {
            userServiceImplementation.getById(TestModelRepo.BASIC_ID);
            fail(TestAccessories.NO_EXCEPTION_THROWN);
        }catch (ResourceNotFoundException e){

        }catch (Exception e){
            fail(TestAccessories.WRONG_EXCEPTION + e.getClass().getTypeName());
        }

        verify(userRepository, times(1)).get(TestModelRepo.BASIC_ID);
    }

    @Test
    public void shouldGetUser(){
        when(userRepository.get(TestModelRepo.BASIC_ID)).thenReturn(user);

        userServiceImplementation.getById(TestModelRepo.BASIC_ID);
        verify(userRepository, times(1)).get(TestModelRepo.BASIC_ID);
    }

    @Test
    public void shouldThrowResourceNotFoundOnDelete(){
        when(userRepository.get(TestModelRepo.BASIC_ID)).thenReturn(null);

        try {
            userServiceImplementation.delete(user);
            fail(TestAccessories.NO_EXCEPTION_THROWN);
        }catch (ResourceNotFoundException e){

        }catch (Exception e){
            fail(TestAccessories.WRONG_EXCEPTION + e.getClass().getTypeName());
        }

        verify(userRepository, times(1)).get(TestModelRepo.BASIC_ID);
        verify(deleteHelper, times(0)).deleteUser(user);
    }

    @Test
    public void shouldDeleteUser(){
        when(userRepository.get(TestModelRepo.BASIC_ID)).thenReturn(user);

        userServiceImplementation.delete(user);

        verify(userRepository, times(1)).get(TestModelRepo.BASIC_ID);
        verify(deleteHelper, times(1)).deleteUser(user);
    }
}