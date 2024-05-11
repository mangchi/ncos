package mil.ln.ncos.cmmn.util;

import java.util.Objects;

import javax.servlet.http.HttpServletRequest;


public class HttpIpUtil {
	private static final String[] IP_HEADER_CANDIDATES = { "X-Forwarded-For", "Proxy-Client-IP", "WL-Proxy-Client-IP",
			"HTTP_X_FORWARDED_FOR", "HTTP_X_FORWARDED", "HTTP_X_CLUSTER_CLIENT_IP", "HTTP_CLIENT_IP",
			"HTTP_FORWARDED_FOR", "HTTP_FORWARDED", "HTTP_VIA", "REMOTE_ADDR" };

	public static String getClientIpAddress(HttpServletRequest request) {

		for (String header : IP_HEADER_CANDIDATES) {
			String ipFromHeader = request.getHeader(header);
			if (Objects.nonNull(ipFromHeader) && ipFromHeader.length() != 0
					&& !"unknown".equalsIgnoreCase(ipFromHeader)) {
				String ip = ipFromHeader.split(",")[0];
				return ip;
			}
		}
		return request.getRemoteAddr();
	}

}
