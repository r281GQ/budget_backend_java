package budget.service;


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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by veghe on 20/09/2016.
 */

@Service
@Transactional
public class AccountServiceImplementation implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private DeleteHelperService deleteHelperService;

    @Autowired
    private ValidationService validationService;

    @Autowired
    private CrossEffectManager crossEffectManager;

    @Autowired
    private DefaultValueProviderService defaultValueProviderService;

    @Override
    public Account getById(long id) {
        Account account = accountRepository.get(id);

        if (account == null) {
            account = new Account();
            account.setIdentifier(id);
            throw new ResourceNotFoundException(account);
        }

        return account;
    }

    @Override
    public void create(Account account) {
        if (account == null) {
            account = new Account();
            account.setIdentifier(0L);
            throw new InvalidDataProvidedException(defaultValueProviderService.getExceptionMessages(InvalidType.NO_CONTENT_PROVIDED), account);
        }
        if (account.getIdentifier() != null)
            throw new InvalidDataProvidedException(defaultValueProviderService.getExceptionMessages(InvalidType.ID_PRESENT_ON_CREATION), account);

        setUserFromDataBase(account);

        accountRepository.create(account);
    }

    private void setUserFromDataBase(Account account) {
        account.setUser(crossEffectManager.getUserById(account.getUser().getIdentifier()));
    }

    @Override
    public void update(Account account) {
        if (account == null) {
            account = new Account();
            account.setIdentifier(0L);
            throw new ResourceNotFoundException(account);
        }
        if (account.getIdentifier() == null || accountRepository.get(account.getIdentifier()) == null)
            throw new ResourceNotFoundException(account);
        setUserFromDataBase(account);

        if (!validationService.isUpdateAble(account))
            throw new InvalidDataProvidedException(defaultValueProviderService.getExceptionMessages(InvalidType.INVALIDATION_ACCOUNT), account);


        accountRepository.update(account);
    }

    @Override
    public void delete(Account account) {
        if (account == null) {
            account = new Account();
            account.setIdentifier(0L);
            throw new ResourceNotFoundException(account);
        }


        if (account.getIdentifier() == null || accountRepository.get(account.getIdentifier()) == null)
            throw new ResourceNotFoundException(account);

        deleteHelperService.deleteAccount(account);
    }

    @Override
    public List<Account> getByUser(User user) {
        return accountRepository.findByUser(user);
    }
}
