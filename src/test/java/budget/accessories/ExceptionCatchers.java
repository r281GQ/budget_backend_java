package budget.accessories;

import budget.accessories.TestAccessories;
import budget.controller.exceptions.InvalidDataProvidedException;
import budget.controller.exceptions.ResourceAlreadyExists;
import budget.controller.exceptions.ResourceNotFoundException;

import static org.junit.Assert.fail;

/**
 * Created by veghe on 15/12/2016.
 */
public class ExceptionCatchers {

    public static void catchResourceNotFound(ActualMethod actualMethod){

        try {
            actualMethod.invoke();
            fail(TestAccessories.NO_EXCEPTION_THROWN);
        }catch (ResourceNotFoundException e){

        }catch (Exception e){
            fail(TestAccessories.WRONG_EXCEPTION + e.getClass().getTypeName());
        }
    }

    public static void catchInvalidDataProvided (ActualMethod actualMethod){

        try {
            actualMethod.invoke();
            fail(TestAccessories.NO_EXCEPTION_THROWN);
        }catch (InvalidDataProvidedException e){

        }catch (Exception e){
            fail(TestAccessories.WRONG_EXCEPTION + e.getClass().getTypeName());
        }
    }

    public static void catchResourceAlreadyExistsProvided (ActualMethod actualMethod){

        try {
            actualMethod.invoke();
            fail(TestAccessories.NO_EXCEPTION_THROWN);
        }catch (ResourceAlreadyExists e){

        }catch (Exception e){
            fail(TestAccessories.WRONG_EXCEPTION + e.getClass().getTypeName());
        }
    }

    public interface ActualMethod{
        void invoke();
    }
}
