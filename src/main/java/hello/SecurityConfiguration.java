package hello;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.AccessTokenRequest;
import org.springframework.security.oauth2.client.token.DefaultAccessTokenRequest;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@EnableOAuth2Client
@Configuration
@Component("security")
class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Value("${oauth.resource:https://accounts.spotify.com/api/}")
    private String baseUrl;

    @Value("${oauth.authorize:https://accounts.spotify.com/authorize}")
    private String authorizeUrl;

    @Value("${oauth.token:https://accounts.spotify.com/api/token}")
    private String tokenUrl;

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    @Bean
    protected OAuth2ProtectedResourceDetails resource() {

        ResourceOwnerPasswordResourceDetails resource = new ResourceOwnerPasswordResourceDetails();

        List scopes = new ArrayList<String>(2);
        scopes.add("playlist-read-private");
//        scopes.add("read");

        resource.setAccessTokenUri(tokenUrl);
        resource.setClientId("62ab5fc16b6a4a97aa08d2d143a070a1");
        resource.setClientSecret("c4c9078805624d4ba127fb658832e350");
        resource.setGrantType("authorization_code");
        resource.setScope(scopes);

        resource.setUsername("remcoperlee");
        resource.setPassword("Nikita01");
log.info("1");
        return resource;
    }

    @Bean
    public OAuth2RestOperations restTemplateOAuth2() {
        AccessTokenRequest atr = new DefaultAccessTokenRequest();
log.info("2");

        return new OAuth2RestTemplate(resource(), new DefaultOAuth2ClientContext(atr));
    }



    // disable REST besic auth, for now
    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                .antMatchers("/**");
    }
}
