package mil.ln.ncos.xss;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.core.Ordered;
import javax.servlet.http.HttpServletRequestWrapper;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class XssFilter implements Filter {
	@SuppressWarnings("unused")
	private FilterConfig filterConfig;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		// log.debug("request.getServletPath()="+((HttpServletRequest)request).getServletPath());
		HttpServletRequestWrapper requestWrapper = new XssFilterWrapper((HttpServletRequest) request);
		// if(path != null && path.indexOf("/sso/") == -1) {
		chain.doFilter(requestWrapper, response);
		// }
	}

	@Override
	public void destroy() {
		this.filterConfig = null;
	}
}