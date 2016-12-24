package budget.service.interfaces;

import budget.service.InvalidType;

/**
 * Created by veghe on 18/11/2016.
 */
public interface DefaultValueProviderService {

    String getDefaultRole();

    String getExceptionMessages(InvalidType invalidType);
}
