package br.com.kerubin.api.apigateway.auth;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AuthFilter extends ZuulFilter {
	
	private static final String TENANT_PARAM = "tenant";
	private static final String TENANT_ACCOUNT_TYPE_PARAM = "tenantAccountType";
	public static final String HTTP = "http://";
	public static final String SECURITY_AUTHORIZATION_SERVICE = "security-authorization";
	//public static final String SECURITY_AUTHORIZATION_SERVICE = "localhost:9002";
	
	public static final String USER_HEADER = "X-User-Header";
	public static final String USER_TENANT = "X-Tenant-Header";
	public static final String HEADER_TENANT_ACCOUNT_TYPE = "X-Tenant-AccountType-Header";
	
	public static final List<String> API_URLS = Arrays.asList("/api/oauth/", "/api/account/");
	// TODO: ver isso melhor public static final List<String> API_URLS = Arrays.asList("/api/oauth/", "/api/security/authorization/account/");
	
	@Autowired
    private JwtTokenStore tokenStore;
	
	@Autowired
	private RestTemplate restTemplate;
	

	@Override
	public boolean shouldFilter() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.getPrincipal() != null;
	}

	@Override
	public Object run() throws ZuulException {
		
		RequestContext ctx = RequestContext.getCurrentContext();
		HttpServletRequest request = ctx.getRequest();
		String uri = request.getRequestURI();
		String requestMethod = request.getMethod();
		
		log.info("Request method: " + requestMethod + ", uri: " + uri);	
		
		boolean isApiUrl = API_URLS.stream().anyMatch(url -> uri.toLowerCase().contains(url));
		if (! isApiUrl) {
	        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	        if (authentication instanceof OAuth2Authentication) {
	        	OAuth2Authentication auth = (OAuth2Authentication) authentication;
	        	Object principal = auth.getPrincipal();
	        	String username = null;
	        	String tenant = null;
	        	if (principal != null) {
	        		username = principal.toString();
	        		ctx.addZuulRequestHeader(USER_HEADER, principal.toString());
	        	}
	        	
	        	if (auth.getDetails() instanceof OAuth2AuthenticationDetails) {
	        		OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) auth.getDetails();
	        		String tokenValue = details.getTokenValue();
	        		if (tokenValue != null) {
	        			OAuth2Authentication auth2 = tokenStore.readAuthentication(tokenValue);
	        			
	        			@SuppressWarnings("unchecked")
						Map<String, Object> details2 = (Map<String, Object>) auth2.getDetails();
	        			
	        			// Tenant parameter
	        			if (details2.containsKey(TENANT_PARAM)) {
	        				Object tenantKey = details2.get(TENANT_PARAM);
	        				if (tenantKey != null) {
	        					tenant = tenantKey.toString();
	        					ctx.addZuulRequestHeader(USER_TENANT, tenant);
	        				}
	        			}
	        			
	        			// AccountType Tenant parameter
	        			if (details2.containsKey(TENANT_ACCOUNT_TYPE_PARAM)) {
	        				Object tenantAccountType = details2.get(TENANT_ACCOUNT_TYPE_PARAM);
	        				if (tenantAccountType != null) {
	        					ctx.addZuulRequestHeader(HEADER_TENANT_ACCOUNT_TYPE, tenantAccountType.toString());
	        				}
	        			}
	        			
	        		}
	        	}
	        	
	        	if (username != null && tenant != null) {
	        		computeTenantOperation(tenant, username, requestMethod, uri);
	        	}
	        	
	        }
		}
		
        return null;
	}

	private void computeTenantOperation(String tenant, String username, String requestMethod, String uri) {
		String url = HTTP + SECURITY_AUTHORIZATION_SERVICE + "/" + "billing/tenant/computeTenantOperation";
		
		HttpEntity<TenantUser> request = new HttpEntity<>(new TenantUser(tenant, username, requestMethod, uri));
		
		try {
			ResponseEntity<TenantUser> response = restTemplate.exchange(url, HttpMethod.POST, request, TenantUser.class);
			
			HttpStatus responseStatus = response.getStatusCode(); 
			
			if (!HttpStatus.OK.equals(responseStatus)) {
				throw new RuntimeException("Cannot execute this operation. responseStatus: " + responseStatus);
			} 
		
		} catch (Exception e) {
			log.error("Error at computeTenantOperation for URL: " + uri + ", tenant: " + tenant + ", username: " + username + ", requestMethod: " + requestMethod);
			throw e;
		}
	}

	@Override
	public String filterType() {
		return "pre";
	}

	@Override
	public int filterOrder() {
		return 1;
	}

}
