package br.com.kerubin.api.gateway.auth;

/*import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.kerubin.api.gateway.service.KerubinGatewayServiceApplication;
import io.restassured.RestAssured;
import io.restassured.response.Response;*/

/*@RunWith(SpringRunner.class)
@SpringBootTest(
		classes = KerubinGatewayServiceApplication.class,
		webEnvironment = WebEnvironment.RANDOM_PORT
)
@ComponentScan(basePackages = "br.com.kerubin.api.gateway.config")*/
public class AuthenticationClaimsIntegrationTest {

	/*@Autowired
    private JwtTokenStore tokenStore;

    @Test
    public void whenTokenDontContainIssuer_thenSuccess() {
        final String tokenValue = obtainAccessToken("kerubin-fe", "admin", "123");
        OAuth2AccessToken token = tokenStore.readAccessToken(tokenValue);
        System.out.println(token.getAdditionalInformation());
        final OAuth2Authentication auth = tokenStore.readAuthentication(tokenValue);
        System.out.println(tokenValue);
        System.out.println(auth);
        assertTrue(auth.isAuthenticated());
        System.out.println(auth.getDetails());

        @SuppressWarnings("unchecked")
		Map<String, Object> details = (Map<String, Object>) auth.getDetails();
        System.out.println(details.get("tenant"));
        assertTrue(details.containsKey("tenant"));
    }

    private String obtainAccessToken(String clientId, String username, String password) {
        final Map<String, String> params = new HashMap<String, String>();
        params.put("grant_type", "password");
        params.put("client_id", clientId);
        params.put("username", username);
        params.put("password", password);
        final Response response = RestAssured.given().auth().preemptive()
        		.basic(clientId, "123")
        		.and()
        		.with()
        		.params(params)
        		.when()
        		.post("http://localhost:9002/oauth/token");
        return response.jsonPath().getString("access_token");
    }*/

}
