package budget.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Created by veghe on 05/11/2016.
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private BudgetUserService userDetailsService;

    @Autowired
    private EntryPoint entryPoint;

    @Bean
    public JWTTokenUtil getJwtTokenUtil() {
        return new JWTTokenUtil();
    }

    @Bean
    public AuthenticationManager getAuthManager() throws Exception {
        return super.authenticationManager();
    }

    @Bean
    public JWTAuthenticationFilter getJWTAuthenticationFilter() throws Exception {
        JWTAuthenticationFilter jwtAuthenticationFilter = new JWTAuthenticationFilter();
        jwtAuthenticationFilter.setAuthenticationManager(getAuthManager());
        return jwtAuthenticationFilter;
    }

    @Bean
    public BCryptPasswordEncoder getBCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAuthProvider getJwtAuthProvider() {
        return new JwtAuthProvider();
    }

    @Override
    protected void configure (AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.authenticationProvider(getJwtAuthProvider());
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(getBCryptPasswordEncoder());
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().
                //allowing pre-flight requests
                antMatchers(HttpMethod.OPTIONS, "/**").
                //so users can sign up
                antMatchers(HttpMethod.POST,"/signUp").
                //so users can log in
                antMatchers(HttpMethod.POST,"/auth");
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.
            //since it is not a multi-page app we do not need cross-site-srcipting-forgery protection
            csrf().disable().
            exceptionHandling().authenticationEntryPoint(entryPoint)
                .and()
            //there is no need to populate the httpSession with principal since it is a stateless security implementation
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).
                and()
            //any requests should be authenticated
            .authorizeRequests().anyRequest().authenticated()
                .and()
            //all these methods should be disabled since we have our own implementation
            .httpBasic().disable()
            .formLogin().disable()
            .logout().disable();

        //register our own filter
        httpSecurity.addFilterBefore(getJWTAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}
