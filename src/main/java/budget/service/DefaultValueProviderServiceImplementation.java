package budget.service;

import budget.service.interfaces.DefaultValueProviderService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by veghe on 18/11/2016.
 */
@Service
public class DefaultValueProviderServiceImplementation implements DefaultValueProviderService {

    private static final String DEFAULT_ROLE = "ROLE_USER";

    private final Map<InvalidType, String> exceptionMessages;

    public DefaultValueProviderServiceImplementation() {

        this.exceptionMessages = new HashMap<>();
        exceptionMessages.put(InvalidType.ID_PRESENT_ON_CREATION, "Identifier must not be present on creation.");

        exceptionMessages.put(InvalidType.ID_NOT_PRESENT_ON_UPDATE, "Identifier must be present in payload when updating resource.");

        exceptionMessages.put(InvalidType.NO_CONTENT_PROVIDED, "No content was attached in payload.");

        exceptionMessages.put(InvalidType.MISSING_USER, "Resource needs to belong to a user. User based on provided id was either not found in database or you do not have permission.");

        exceptionMessages.put(InvalidType.MISSING_ACCOUNT, "Resource needs to belong to an account. Account based on provided id was either not found in database or you do not have permission.");

        exceptionMessages.put(InvalidType.MISSING_BUDGET, "Resource needs to belong to a budget. Budget based on provided id was either not found in database or you do not have permission.");

        exceptionMessages.put(InvalidType.MISSING_GROUPING, "Resource needs to belong to a grouping. Grouping based on provided id was either not found in database or you do not have permission.");

        exceptionMessages.put(InvalidType.INSUFFICIENT_BALANCE, "There is not enough balance to proceed.");

        exceptionMessages.put(InvalidType.INVALIDATION_EQUITY, "");

        exceptionMessages.put(InvalidType.INVALIDATION_ACCOUNT, "");

        exceptionMessages.put(InvalidType.INVALIDATION_BUDGET, "");

        exceptionMessages.put(InvalidType.INVALIDATION_BUDGETPERIOD, "");

        exceptionMessages.put(InvalidType.INVALIDATION_GROUPING, "");

        exceptionMessages.put(InvalidType.INVALIDATION_TRANSACTION, "");

        exceptionMessages.put(InvalidType.INVALIDATION_USER, "");

        exceptionMessages.put(InvalidType.INVALIDATION_EQUITY, "ID, TYPE");
    }

    @Override
    public String getDefaultRole() {
        return DEFAULT_ROLE;
    }

    @Override
    public String getExceptionMessages(InvalidType invalidType) {
        return exceptionMessages.get(invalidType);
    }
}
