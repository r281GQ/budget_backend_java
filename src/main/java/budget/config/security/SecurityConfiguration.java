package budget.config.security;

import budget.config.CORSFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import javax.servlet.Filter;

//import org.springframework.http.HttpMethod;

/**
 * Created by veghe on 05/11/2016.
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private Filter f = new CORSFilter();

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

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.
                csrf().disable().
//                            sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).
//                            and().
//                            securityContext().securityContextRepository(null).and().
                    authorizeRequests()
                        //css or certain frontend content to be available
                        //going from top to down
                                .antMatchers(HttpMethod.OPTIONS,"/**").permitAll()

                        .antMatchers("/resources/**","/users/create").permitAll().
                        anyRequest().authenticated().
                            and().
//                csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()).ignoringAntMatchers("/users/create").
//                        and().
                        httpBasic();

//                    formLogin().permitAll();

    }

//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.
//                addFilterAfter(f, BasicAuthenticationFilter.class).
//                csrf().disable().
//                authorizeRequests().
//                //css or certain frontend content to be available
//                //going from top to down
//                        antMatchers("/resources/**").permitAll()
//                .antMatchers(HttpMethod.OPTIONS,"/**").permitAll().
//                anyRequest().authenticated().and().httpBasic().
//                and().anonymous().disable().exceptionHandling().authenticationEntryPoint(new BasicAuthenticationEntryPoint() {
//            @Override
//            public void commence(final HttpServletRequest request, final HttpServletResponse response, final AuthenticationException authException) throws IOException, ServletException {
//                if (HttpMethod.OPTIONS.equals(request.getMethod())) {
//                    response.setStatus(HttpServletResponse.SC_OK);
//                    response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, request.getHeader(HttpHeaders.ORIGIN));
//                    response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, request.getHeader(HttpHeaders.ACCESS_CONTROL_REQUEST_HEADERS));
//                    response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, request.getHeader(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD));
//                    response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");
//                } else {
//                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
//                }
//            } });
////                formLogin().
////                permitAll();
//
//        }

//    @Bean
//    public FilterRegistrationBean corsFilter() {
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        CorsConfiguration config = new CorsConfiguration();
//        config.setAllowCredentials(true);
//        config.addAllowedOrigin("http://localhost:4200");
//        config.addAllowedHeader("*");
//        config.addAllowedMethod("*");
//        source.registerCorsConfiguration("/**", config);
//        FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
//        bean.setOrder(0);
//        return bean;
//    }
    }
