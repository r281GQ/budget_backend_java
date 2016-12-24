package budget.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Created by veghe on 05/11/2016.
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthenticationFailure authenticationFailure;

    @Autowired
    private BudgetUserService userDetailsService;

    @Autowired
    private AuthenticationSuccess authenticationSucces;

    @Autowired
    private EntryPoint entryPoint;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(userDetailsService);
    }

//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.
//                csrf().disable().
//                exceptionHandling().authenticationEntryPoint(entryPoint).and().
//                authorizeRequests().
//                        anyRequest().authenticated().
//                        antMatchers("/**").
//                        hasAnyRole("USER").
//                        and().
//                    formLogin().
//                        failureHandler(authenticationFailure).
//                        successHandler(authenticationSucces).
//                    permitAll();
//    }

//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.
////                csrf().disable().
////                            sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).
////                            and().
////                            securityContext().securityContextRepository(null).and().
//                    authorizeRequests().
//                        //css or certain frontend content to be available
//                        //going from top to down
//                        antMatchers("/resources/**","/users/create").permitAll().
//                        anyRequest().authenticated().
//                            and().
//                csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()).ignoringAntMatchers("/users/create").
//                        and().
//                        httpBasic();
//
////                    formLogin().permitAll();
//
//    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.
                csrf().disable().
                authorizeRequests().
                //css or certain frontend content to be available
                //going from top to down
                        antMatchers("/resources/**").permitAll().
                anyRequest().authenticated().
                and().httpBasic();
//                formLogin().
//                permitAll();

    }
}
