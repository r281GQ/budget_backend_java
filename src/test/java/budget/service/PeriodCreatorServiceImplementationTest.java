package budget.service;

import budget.model.Period;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by veghe on 11/09/2016.
 */

public class PeriodCreatorServiceImplementationTest {

    private static Date TEST_DATE;
    private static String TEST_DATE_STRING_REPRESENTATION = "01-2000";
    private static String DATE_FORMAT = "MM-yyyy";

    @Spy
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-yyyy");

    @InjectMocks
    private PeriodCreatorServiceImplementation periodCreatorServiceImplementation = new PeriodCreatorServiceImplementation();

    @Before
    public void setUp(){
        initMocks(this);

        try {
            TEST_DATE = new SimpleDateFormat(DATE_FORMAT).parse(TEST_DATE_STRING_REPRESENTATION);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void createPeriod(){
        Period period = periodCreatorServiceImplementation.createPeriod(TEST_DATE);

        assertThat(period.getRepresentation(), is(TEST_DATE_STRING_REPRESENTATION));
    }
}