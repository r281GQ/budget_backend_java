package budget.service;

import budget.model.Currency;
import budget.service.interfaces.ExchangeService;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by veghe on 11/09/2016.
 */
@Service
public class ExchangeServiceImplementation implements ExchangeService {

    private Set<Currency> currencies;

    private Map<String, BigDecimal> rates;

    private List<String> currencyPairs;

    public ExchangeServiceImplementation() throws IOException {
        setRates(new HashMap<>());
        setCurrencies(new HashSet<>());
        setCurrencyPairs(new ArrayList<>());
        addCurrency(Currency.GBP);
        addCurrency(Currency.EUR);
        addCurrency(Currency.USD);
        addCurrency(Currency.HUF);
        for (String currency : currencyPairs) {
            rates.put(currency, new BigDecimal(convert(currency)));
        }
        refreshRates();
    }

    @Override
    public BigDecimal getRate(String currencyPair) {
        return getRates().get(currencyPair);
    }


    public void refreshRates() {
        rates.clear();
        for (String pairs : currencyPairs) {
            try {
                rates.put(pairs, new BigDecimal(convert(pairs)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void addCurrency(Currency currency) {
        currencies.add(currency);
        currencyPairs.clear();
        String currentPair = "";
        for (Currency first : currencies) {
            for (Currency second : currencies) {
                currentPair = first.toString() + second.toString();
                currencyPairs.add(currentPair);
            }
        }
    }

    public Map<String, BigDecimal> getRates() {
        return Collections.unmodifiableMap(rates);
    }

    public void setRates(Map<String, BigDecimal> rates) {
        this.rates = rates;
    }

    public float convert(String currency) throws IOException {


        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpGet httpGet = new HttpGet(
                "http://quote.yahoo.com/d/quotes.csv?s=" + currency + "=X&f=l1&e=.csv");
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        String responseBody = client.execute(httpGet, responseHandler);
        client.close();
        return Float.parseFloat(responseBody);
    }

    public Set<Currency> getCurrencies() {
        return currencies;
    }

    public void setCurrencies(Set<Currency> currencies) {
        this.currencies = currencies;
    }

    public List<String> getCurrencyPairs() {
        return currencyPairs;
    }

    public void setCurrencyPairs(List<String> currencyPairs) {
        this.currencyPairs = currencyPairs;
    }

    public BigDecimal getExchangeRate(String pair) {
        return getRates().get(pair);
    }
}
