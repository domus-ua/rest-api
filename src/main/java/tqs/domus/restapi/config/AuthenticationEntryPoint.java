package tqs.domus.restapi.config;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Vasco Ramos
 * @date 05/05/20
 * @time 22
 */
@Component
public class AuthenticationEntryPoint extends BasicAuthenticationEntryPoint {

	/**
	 * @param request  Received request.
	 * @param response Generated request response.
	 * @param authEx   Authentication Exception.
	 * @throws IOException
	 */
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authEx)
			throws IOException {
		response.addHeader("WWW-Authenticate", "Basic realm=" + getRealmName());
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		PrintWriter writer = response.getWriter();
		writer.println("HTTP Status 401 - " + authEx.getMessage());
	}

	@Override
	public void afterPropertiesSet() {
		setRealmName("DeveloperStack");
		super.afterPropertiesSet();
	}

}
