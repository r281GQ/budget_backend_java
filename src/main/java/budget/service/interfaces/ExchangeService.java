package budget.service.interfaces;

import java.math.BigDecimal;

/**
 * Created by veghe on 16/08/2016.
 */
public interface ExchangeService {

    BigDecimal getRate(String currencyPair);
}
