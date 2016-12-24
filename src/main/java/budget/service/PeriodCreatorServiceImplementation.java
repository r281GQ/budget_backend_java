package budget.service;

import budget.model.Period;
import budget.service.interfaces.PeriodCreatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by veghe on 18/08/2016.
 */
@Service
public class PeriodCreatorServiceImplementation implements PeriodCreatorService {

    @Autowired
    private SimpleDateFormat df;

    @Override
    public Period createPeriod (Date date){
        Period period = new Period();

        period.setPeriod(date);
        period.setRepresentation(df.format(date));

        return period;
    }
}
