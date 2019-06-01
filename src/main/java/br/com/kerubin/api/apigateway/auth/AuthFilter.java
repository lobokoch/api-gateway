package br.com.kerubin.api.apigateway.auth;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

@Component
public class AuthFilter extends ZuulFilter {
	
	public static final String USER_HEADER = "X-User-Header";
	public static final String USER_TENANT = "X-Tenant-Header";
	
	public static final List<String> API_URLS = Arrays.asList("/api/oauth/", "/api/account/");
	
	@Autowired
    private JwtTokenStore tokenStore;
	

	@Override
	public boolean shouldFilter() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.getPrincipal() != null;
	}

	@Override
	public Object run() throws ZuulException {
		
		RequestContext ctx = RequestContext.getCurrentContext();
		
		String uri = ctx.getRequest().getRequestURI();
		System.out.println("uri: " + uri);
		
		
		boolean isApiUrl = API_URLS.stream().anyMatch(url -> uri.toLowerCase().contains(url));
		if (! isApiUrl) {
	        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	        if (authentication instanceof OAuth2Authentication) {
	        	OAuth2Authentication auth = (OAuth2Authentication) authentication;
	        	Object principal = auth.getPrincipal();
	        	if (principal != null) {
	        		ctx.addZuulRequestHeader(USER_HEADER, principal.toString());
	        	}
	        	
	        	if (auth.getDetails() instanceof OAuth2AuthenticationDetails) {
	        		OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) auth.getDetails();
	        		String tokenValue = details.getTokenValue();
	        		if (tokenValue != null) {
	        			OAuth2Authentication auth2 = tokenStore.readAuthentication(tokenValue);
	        			
	        			@SuppressWarnings("unchecked")
						Map<String, Object> details2 = (Map<String, Object>) auth2.getDetails();
	        			if (details2.containsKey("tenant")) {
	        				Object tenant = details2.get("tenant");
	        				if (tenant != null) {
	        					ctx.addZuulRequestHeader(USER_TENANT, tenant.toString());
	        				}
	        			}
	        			
	        		}
	        	}
	        	
	        }
		}
		
        return null;
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
