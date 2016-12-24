package budget.service;

import budget.accessories.validators.ValidationService;
import budget.controller.exceptions.InvalidDataProvidedException;
import budget.controller.exceptions.ResourceNotFoundException;
import budget.model.Equity;
import budget.model.User;
import budget.repository.interfaces.EquityRepository;
import budget.service.interfaces.CrossEffectManager;
import budget.service.interfaces.DefaultValueProviderService;
import budget.service.interfaces.DeleteHelperService;
import budget.service.interfaces.EquityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by veghe on 27/11/2016.
 */
@Service
public class EquityServiceImplementation implements EquityService {

    @Autowired
    private DefaultValueProviderService defaultValueProviderService;

    @Autowired
    private EquityRepository equityRepository;

    @Autowired
    private DeleteHelperService deleteHelperService;

    @Autowired
    private ValidationService validationService;

    @Autowired
    private CrossEffectManager crossEffectManager;

    @Override
    public Equity getById(long id) {
        Equity equity = equityRepository.get(id);
        if (equity == null) {
            equity.setIdentifier(id);
            throw new ResourceNotFoundException(equity);
        }
        return equity;
    }

    @Override
    public void create(Equity equity) {
        if (equity == null) {
            equity = new Equity();
            equity.setIdentifier(0L);
            throw new InvalidDataProvidedException(defaultValueProviderService.getExceptionMessages(InvalidType.NO_CONTENT_PROVIDED), equity);
        }
        if (equity.getIdentifier() != null)
            throw new InvalidDataProvidedException(defaultValueProviderService.getExceptionMessages(InvalidType.ID_PRESENT_ON_CREATION), equity);

        setUser(equity);
        equityRepository.create(equity);
    }

    @Override
    public void delete(Equity equity) {
        if (equity == null) {
            equity = new Equity();
            equity.setIdentifier(0L);
            throw new ResourceNotFoundException(equity);
        }
        if (equity.getIdentifier() == null || equityRepository.get(equity.getIdentifier()) == null) {
            throw new ResourceNotFoundException(equity);
        }
        deleteHelperService.deleteEquity(equity);
    }

    @Override
    public void update(Equity equity) {
        if (equity == null) {
            equity = new Equity();
            equity.setIdentifier(0L);
            throw new ResourceNotFoundException(equity);
        }
        if (equityRepository.get(equity.getIdentifier()) == null) {
            throw new ResourceNotFoundException(equity);
        }
        setUser(equity);
        if (equity.getIdentifier() == null || !validationService.isUpdateAble(equity))
            throw new InvalidDataProvidedException(defaultValueProviderService.getExceptionMessages(InvalidType.INVALIDATION_EQUITY), equity);
        equityRepository.update(equity);
    }

    @Override
    public List<Equity> getByUser(User user) {
        return equityRepository.findByUser(user);
    }

    private void setUser(Equity equity) {
        equity.setUser(crossEffectManager.getUserById(equity.getUser().getIdentifier()));
    }
}
