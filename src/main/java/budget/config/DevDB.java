package budget.config;

import budget.service.interfaces.ExchangeService;
import com.mysql.cj.jdbc.Driver;
import org.hibernate.dialect.MySQLDialect;
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
 * Created by veghe on 12/12/2016.
 */
@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = "budget")
@Profile("development")
public class DevDB {

    @Bean
    @Primary
    public ExchangeService exchangeService() {
        return new ExchangeService() {
            @Override
            public BigDecimal getRate(String currencyPair) {
                return new BigDecimal(1);
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
        properties.setProperty("hibernate.hbm2ddl.auto", "update");
        properties.setProperty("javax.persistence.validation.mode", "NONE");
        return properties;
    }

    @Bean
    public DataSource getDataSource() {
        SimpleDriverDataSource ds = new SimpleDriverDataSource();
        ds.setDriverClass(Driver.class);
        ds.setUrl("jdbc:mysql://localhost:3306/budget_development?autoReconnect=true&useSSL=false&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC");
        ds.setUsername("root");
        ds.setPassword("hkYrt61R3N");
        return ds;
    }

    @Bean
    public JpaVendorAdapter getJPAVendor() {
        HibernateJpaVendorAdapter jpa = new HibernateJpaVendorAdapter();
        jpa.setShowSql(true);
        jpa.setDatabase(Database.MYSQL);
        jpa.setGenerateDdl(true);
        jpa.setDatabasePlatform(MySQLDialect.class.getName());
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
