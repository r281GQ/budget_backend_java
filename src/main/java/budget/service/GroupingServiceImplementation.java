package budget.service;

import budget.accessories.validators.ValidationService;
import budget.controller.exceptions.InvalidDataProvidedException;
import budget.controller.exceptions.ResourceNotFoundException;
import budget.model.Grouping;
import budget.model.User;
import budget.repository.interfaces.GroupingRepository;
import budget.service.interfaces.CrossEffectManager;
import budget.service.interfaces.DefaultValueProviderService;
import budget.service.interfaces.DeleteHelperService;
import budget.service.interfaces.GroupingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by veghe on 08/12/2016.
 */
@Service
@Transactional
public class GroupingServiceImplementation implements GroupingService {

    @Autowired
    private GroupingRepository groupingRepository;

    @Autowired
    private ValidationService validationService;

    @Autowired
    private DeleteHelperService deleteHelperService;

    @Autowired
    private CrossEffectManager crossEffectManager;

    @Autowired
    private DefaultValueProviderService defaultValueProviderService;

    @Override
    public Grouping getById(long id) throws ResourceNotFoundException {
        Grouping grouping = groupingRepository.get(id);
        if (grouping == null) {
            grouping = new Grouping();
            grouping.setIdentifier(id);
            throw new ResourceNotFoundException(grouping);
        }
        return grouping;
    }

    @Override
    public void create(Grouping grouping) throws InvalidDataProvidedException {
        if (grouping == null) {
            grouping = new Grouping();
            grouping.setIdentifier(0L);
            throw new InvalidDataProvidedException(defaultValueProviderService.getExceptionMessages(InvalidType.NO_CONTENT_PROVIDED), grouping);
        }
        if (grouping.getIdentifier() != null)
            throw new InvalidDataProvidedException(defaultValueProviderService.getExceptionMessages(InvalidType.ID_PRESENT_ON_CREATION), grouping);

        setUser(grouping);

        groupingRepository.create(grouping);
    }

    private void setUser(Grouping grouping) {
        grouping.setUser(crossEffectManager.getUserById(grouping.getUser().getIdentifier()));
    }

    @Override
    public void delete(Grouping grouping) throws ResourceNotFoundException {
        if (grouping == null) {
            grouping = new Grouping();
            grouping.setIdentifier(0L);
            throw new ResourceNotFoundException(grouping);
        }
        if (grouping.getIdentifier() == null || groupingRepository.get(grouping.getIdentifier()) == null)
            throw new ResourceNotFoundException(grouping);
        deleteHelperService.deleteGrouping(grouping);
    }

    @Override
    public void update(Grouping grouping) throws InvalidDataProvidedException {
        if (grouping == null) {
            grouping = new Grouping();
            grouping.setIdentifier(0L);
            throw new ResourceNotFoundException(grouping);
        }
        if (grouping.getIdentifier() == null || groupingRepository.get(grouping.getIdentifier()) == null)
            throw new ResourceNotFoundException(grouping);

        setUser(grouping);

        if (!validationService.isUpdateAble(grouping))
            throw new InvalidDataProvidedException(defaultValueProviderService.getExceptionMessages(InvalidType.INVALIDATION_GROUPING), grouping);

        groupingRepository.update(grouping);
    }

    @Override
    public List<Grouping> getByUser(User user) {
        return groupingRepository.findByUser(user);
    }
}
