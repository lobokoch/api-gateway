/**********************************************************************************************
Code generated with MKL Plug-in version: 47.8.0
Code generated at time stamp: 2020-01-13T08:05:57.276
Copyright: Kerubin - logokoch@gmail.com

WARNING: DO NOT CHANGE THIS CODE BECAUSE THE CHANGES WILL BE LOST IN THE NEXT CODE GENERATION.
***********************************************************************************************/

package br.com.kerubin.api.apigateway;

import java.io.IOException;
import java.util.Collections;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class HttpFilter implements Filter {
	
	
	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		
		HttpServletRequest request = (HttpServletRequest) req;
		//HttpServletResponse response = (HttpServletResponse) resp;
		
        log.info("************ START doFilter ************");
        
        String method = request.getMethod();
        String url = request.getRequestURL().toString();        
        log.info("method/url: {} {}", method, url);
        
        
        String uri = request.getRequestURI();
        log.info("request.getRequestURI():{}", uri);
        
        
        log.info("request.getHeader(Origin):{}", request.getHeader("Origin"));
        
        String queryString = request.getQueryString();
		log.info("request.getQueryString: {}", queryString);
		
		HttpHeaders httpHeaders = Collections
			    .list(request.getHeaderNames())
			    .stream()
			    .collect(Collectors.toMap(
			        Function.identity(),
			        h -> Collections.list(request.getHeaders(h)),
			        (oldValue, newValue) -> newValue,
			        HttpHeaders::new
			    ));
		
		log.info("httpHeaders: {}", httpHeaders.toString());
		log.info("************ END doFilter ************");
		
		chain.doFilter(req, resp);
		
	}
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// 
	}


	@Override
	public void destroy() {
		// 
	}

}

