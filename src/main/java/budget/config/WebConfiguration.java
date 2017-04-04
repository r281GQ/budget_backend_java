package budget.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by veghe on 24/07/2016.
 */
@Configuration

@EnableWebMvc
@ComponentScan(basePackages = "budget")
public class WebConfiguration extends WebMvcConfigurerAdapter {

    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**").addResourceLocations("/dist/");
    }



            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedMethods("DELETE", "PUT", "OPTIONS", "GET", "POST");
            }


}
