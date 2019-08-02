package br.com.kerubin.api.apigateway.auth;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TenantUser {
	
	private String tenant;
	private String user;
	private String requestMethod;
	private String uri;
	
	public TenantUser() {
		
	}
	
	public TenantUser(String tenant, String user, String requestMethod, String uri) {
		this.tenant = tenant;
		this.user = user;
		this.requestMethod = requestMethod;
		this.uri = uri;
	}
	
}
