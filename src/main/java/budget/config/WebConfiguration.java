package budget.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by veghe on 24/07/2016.
 */
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "budget")
public class WebConfiguration extends WebMvcConfigurerAdapter {}
