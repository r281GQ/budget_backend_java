package budget.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by veghe on 18/08/2016.
 */
@Embeddable
public class Period implements Comparable<Period>, Serializable {

    @JsonIgnore
    @Temporal(TemporalType.DATE)
    private Date period;

    private String representation;

    public Date getPeriod() {
        return period;
    }

    public void setPeriod(Date period) {
        this.period = period;
    }

    public String getRepresentation() {
        return representation;
    }

    public void setRepresentation(String representation) {
        this.representation = representation;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (getRepresentation() == null)
            return false;

        if (obj instanceof Period == false)
            return false;

        Period other = (Period) obj;

        if (other.getRepresentation() == null)
            return false;

        if (getRepresentation().equals(other.getRepresentation()))
            return true;
        else
            return false;
    }

    @Override
    public int compareTo(Period o) {

        int yearThis = getYear(this);
        int monthThis = getMonth(this);

        int yearOther = getYear(o);
        int monthOther = getMonth(o);

        if (yearThis > yearOther) {
            return 1;
        } else if (yearThis < yearOther) {
            return -1;
        } else {
            if (monthThis < monthOther) {
                return -1;
            } else if (monthThis > monthOther) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    private int getMonth(Period o) {
        return Integer.parseInt(o.getRepresentation().substring(0, 2));
    }

    private int getYear(Period period) {
        return Integer.parseInt(period.getRepresentation().substring(3));
    }
}
