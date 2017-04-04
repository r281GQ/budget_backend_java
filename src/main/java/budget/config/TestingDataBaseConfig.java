package budget.config;

import budget.service.interfaces.ExchangeService;
import org.h2.Driver;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Properties;

/**
 * Created by veghe on 24/07/2016.
 */

@Configuration
@ComponentScan(basePackages = "budget")
@EnableTransactionManagement
@Profile("testing")
//@PropertySource(value = {"classpath:/application.properties"})
public class TestingDataBaseConfig {

//    @Autowired
//    private Environment environment;

    @Bean
    @Primary
    public ExchangeService getExchangeService() {
        return new ExchangeService() {
            @Override
            public BigDecimal getRate(String currencyPair) {
                return currencyPair.equals("GBPGBP") || currencyPair.equals("EUREUR") ? new BigDecimal(1) : new BigDecimal(2);
            }
        };
    }

    @Bean
    public SimpleDateFormat getSimpleDateFormat() {
        return new SimpleDateFormat("MM-yyyy");
    }

    @Bean
    public Properties getEntityProperties() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.hbm2ddl.auto", "create-drop" );
        properties.setProperty("javax.persistence.validation.mode", "NONE");
        return properties;
    }

    @Bean
    public DataSource getDataSource() {
        SimpleDriverDataSource ds = new SimpleDriverDataSource();
        ds.setDriverClass(Driver.class);
        ds.setUrl("jdbc:h2:~/test)");
        ds.setPassword("");
        ds.setUsername("sa");
        return ds;
    }

    @Bean
    public JpaVendorAdapter getJPAVendor() {
        HibernateJpaVendorAdapter jpa = new HibernateJpaVendorAdapter();
        jpa.setShowSql(true);
        jpa.setDatabase(Database.H2);
        jpa.setGenerateDdl(true);

        return jpa;
    }

    @Bean
    public PlatformTransactionManager getManager() {
        JpaTransactionManager txMng = new JpaTransactionManager();
        txMng.setEntityManagerFactory(getContainer().getObject());
        return txMng;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean getContainer() {
        LocalContainerEntityManagerFactoryBean container = new LocalContainerEntityManagerFactoryBean();
        container.setDataSource(getDataSource());
        container.setPackagesToScan("budget");
        container.setJpaVendorAdapter(getJPAVendor());
        container.setJpaProperties(getEntityProperties());
        return container;
    }
}
