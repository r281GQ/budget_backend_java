package budget.config;

import budget.config.security.SecurityConfiguration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.Filter;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

/**
 * Created by veghe on 24/07/2016.
 */
public class Dispatcher extends AbstractAnnotationConfigDispatcherServletInitializer {

//    @Override
//    protected Filter[] getServletFilters() {
//        Filter [] basic = super.getServletFilters();
//        List<Filter> f = new ArrayList<>(Arrays.asList(basic));
//        f.add(new CORSFilter());
//        return (Filter[]) f.toArray();
//    }

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class<?>[]{TestingDataBaseConfig.class, DevDB.class, SecurityConfiguration.class};
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class<?>[]{WebConfiguration.class};
    }

    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }

    @Override
    protected WebApplicationContext createRootApplicationContext() {
        WebApplicationContext context = super.createRootApplicationContext();
        ((ConfigurableEnvironment) context.getEnvironment()).setActiveProfiles("development");
        return context;
    }

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        servletContext.setInitParameter("spring.profiles.active", "development");
        super.onStartup(servletContext);
    }

    @Override
    protected void registerDispatcherServlet(ServletContext servletContext) {
        String servletName = super.getServletName();
        Assert.hasLength(servletName, "getServletName() may not return empty or null");

        WebApplicationContext servletAppContext = super.createServletApplicationContext();
        Assert.notNull(servletAppContext,
                "createServletApplicationContext() did not return an application " +
                        "context for servlet [" + servletName + "]");

        DispatcherServlet dispatcherServlet = new DispatcherServlet(servletAppContext);
        System.out.println(servletName);
        //>>> START: My custom code, rest is exqact copy of the super class
        dispatcherServlet.setThrowExceptionIfNoHandlerFound(true);
        //>>> END

        ServletRegistration.Dynamic registration = servletContext.addServlet(servletName, dispatcherServlet);
        Assert.notNull(registration,
                "Failed to register servlet with name '" + servletName + "'." +
                        "Check if there is another servlet registered under the same name.");

        registration.setLoadOnStartup(1);
        registration.addMapping(getServletMappings());
        registration.setAsyncSupported(isAsyncSupported());

        Filter[] filters = getServletFilters();
        if (!ObjectUtils.isEmpty(filters)) {
            for (Filter filter : filters) {
                super.registerServletFilter(servletContext, filter);
            }
        }
        super.customizeRegistration(registration);
    }
}
