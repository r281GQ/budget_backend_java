package budget.config;

//import org.springframework.web.filter.CorsFilter;

import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by veghe on 08/01/2017.
 */
public class CORSFilter extends OncePerRequestFilter {
    static final String ORIGIN = "Origin";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        System.out.println(request.getHeader(ORIGIN));
        System.out.println(request.getMethod());
        if(request.getHeader(ORIGIN)!= null) {
            if (request.getHeader(ORIGIN).equals("null")) {
                String origin = request.getHeader(ORIGIN);
                response.setHeader("Access-Control-Allow-Origin", "*");//* or origin as u prefer
                response.setHeader("Access-Control-Allow-Credentials", "true");
                response.setHeader("Access-Control-Allow-Headers",
                        request.getHeader("Access-Control-Request-Headers"));
            }
        }
        if (request.getMethod().equals("OPTIONS")) {
            try {
                response.getWriter().print("OK");
                response.getWriter().flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            filterChain.doFilter(request, response);
        }
    }
//public class CORSFilter extends OncePerRequestFilter {
}
//public class CORSFilter  {
//    private static final Log LOG = LogFactory.getLog(CORSFilter.class);

//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        response.addHeader("Access-Control-Allow-Origin", "*");
//
////        if (request.getHeader("Access-Control-Request-Method") != null) {
////            LOG.trace("Sending Header....");
//            // CORS "pre-flight" request
//            response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTION");
////			response.addHeader("Access-Control-Allow-Headers", "Authorization");
//            response.addHeader("Access-Control-Allow-Headers", "Content-Type");
//            response.addHeader("Access-Control-Max-Age", "1");
////        }
//
//        filterChain.doFilter(request, response);
//    }

//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        response.addHeader("Access-Control-Allow-Origin", "*");
//
//        if (request.getHeader("Access-Control-Request-Method") != null && "OPTIONS".equals(request.getMethod())) {
////            LOG.trace("Sending Header....");
//            // CORS "pre-flight" request
//            response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
////			response.addHeader("Access-Control-Allow-Headers", "Authorization");
//            response.addHeader("Access-Control-Allow-Headers", "Content-Type");
//            response.addHeader("Access-Control-Max-Age", "1");
//        }
//
//        filterChain.doFilter(request, response);
//    }

//}