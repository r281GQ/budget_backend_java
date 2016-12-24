package budget.service.interfaces;

import budget.model.Period;

import java.util.Date;

/**
 * Created by veghe on 11/09/2016.
 */
public interface PeriodCreatorService {

    Period createPeriod (Date date);
}
