package br.com.brlsistemas.libraryapi.api.security;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CorsFilter implements Filter {

	/**
	 * Essa classe é responsável por controlar o CORS.
	 *
	 * O Spring irá interceptar as requisições com o método OPTIONS para
	 * verificar se a origem tem permissão para acessar a API.
	 */

//	private  String[] originPermitida = {"http://localhost:9090"};
//	private final String[] originPermitida = {"http://localhost:9090", "https://library-api-mycloud.herokuapp.com/"};
	private final String[] originPermitida = {"https://library-api-mycloud.herokuapp.com/"};
//	private final String[] originPermitida = {"https://library-api-mycloud.herokuapp.com/actuator1"};

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;

        Set<String> allowedOrigins = new HashSet<String>(Arrays.asList (originPermitida));

        String originHeader = req.getHeader("Origin");

        if (allowedOrigins.contains(originHeader)) {
        	resp.setHeader("Access-Control-Allow-Origin", originHeader);
        } else {
        	allowedOrigins.forEach(p -> resp.setHeader("Access-Control-Allow-Origin", p));
        }

		resp.setHeader("Access-Control-Allow-Credentials", "true");

		if ("OPTIONS".equals(req.getMethod()) && allowedOrigins.contains(originHeader)) {
			resp.setHeader("Access-Control-Allow-Methods", "POST, DELETE, GET, PUT, PATCH, OPTIONS");
			resp.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type, Accept");
			resp.setHeader("Access-Control-Max-Age", "10"); // tempo do pre-flight para fazer a requisição OPTIONS novamente = 10 segundos

			resp.setStatus(HttpServletResponse.SC_OK);
		} else {
			chain.doFilter(req, resp);
		}
	}

}
