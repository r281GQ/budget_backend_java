package budget.model;

import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * Created by veghe on 21/08/2016.
 */
public class PeriodTest {

    private SimpleDateFormat df;

    private Period previous;
    private Period current;
    private Period next;

    private String previousString;
    private String currentString;
    private String nextString;

    @Before
    public void setUp() throws ParseException {

        df = new SimpleDateFormat("MM-yyyy");

        previous = new Period();
        current = new Period();
        next = new Period();

        previousString = "07-2016";
        currentString = "08-2016";
        nextString = "09-2016";

        previous.setRepresentation(previousString);
        previous.setPeriod(df.parse(previousString));

        current.setRepresentation(currentString);
        current.setPeriod(df.parse(currentString));

        next.setPeriod(df.parse(nextString));
        next.setRepresentation(nextString);
    }

    @Test
    public void compare(){
//        assertThat(new Long(-1).compareTo(new Long(3)),is(-1));


        String comp = new String(previousString);

        String year = comp.substring(3);
        int yearint = Integer.parseInt(year);
        String month = comp.substring(0,2);
        int monthint = Integer.parseInt(month);

        assertThat(monthint,is(7));
        assertThat(yearint,is(2016));
        assertThat(previous.compareTo(current), is(-1));
        assertThat(current.compareTo(current), is(0));
        assertThat(current.compareTo(next), is(-1));
    }

    @Test
    public void should_return_as_equals() throws ParseException {
        next.setRepresentation(currentString);
        next.setPeriod(df.parse(currentString));

        assertThat(next.compareTo(current), is(0));
    }

}