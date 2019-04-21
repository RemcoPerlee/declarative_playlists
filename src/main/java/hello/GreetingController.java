package hello;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import static java.util.Arrays.asList;

@RestController
public class GreetingController {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    @Value("${oauth.token:https://accounts.spotify.com/api/token}")
    private String tokenUrl;


//    @Autowired
//    @Qualifier("security")
//    private OAuth2RestOperations restTemplateOAuth2;

//    @Bean
//    public RestTemplate restTemplate(RestTemplateBuilder builder) {
//        return builder.build();
//    }

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();


    @RequestMapping("/greeting")
    public Greeting greeting(@RequestParam(value="name", defaultValue="World") String name) throws Exception {

        RestTemplate restTemplate = new RestTemplateBuilder().build();

        Quote quote = restTemplate.getForObject(
                "http://gturnquist-quoters.cfapps.io/api/random", Quote.class);

//        restTemplateOAuth2.getAccessToken();

//        List scopes = new ArrayList<String>(2);
//        scopes.add("playlist-read-private");
//
//        ResourceOwnerPasswordResourceDetails resource = new ResourceOwnerPasswordResourceDetails();
//        resource.setUsername("remcoperlee");
//        resource.setPassword("Nikita01");
//        resource.setAccessTokenUri(tokenUrl);
//        resource.setClientId("62ab5fc16b6a4a97aa08d2d143a070a1");
//        resource.setClientSecret("c4c9078805624d4ba127fb658832e350");
//        resource.setGrantType("client_credentials");
//        resource.setScope(scopes);
//
//        DefaultOAuth2ClientContext clientContext = new DefaultOAuth2ClientContext();
//
//        OAuth2RestTemplate restTemplateOAuth2 = new OAuth2RestTemplate(resource, clientContext);
//        restTemplateOAuth2.setMessageConverters(asList(new MappingJackson2HttpMessageConverter()));
//
//        log.info("1");
//        restTemplateOAuth2.getAccessToken();
//        log.info("2");
//
//        Playlists playlists = restTemplateOAuth2.getForObject("https://api.spotify.com//v1/users/remcoperlee/playlists", Playlists.class);
//        log.info("3");

        RestTemplate spotify = oAuthRestTemplate();


        Playlists playlists = spotify.getForObject("https://api.spotify.com/v1/users/remcoperlee/playlists", Playlists.class);

        log.info(playlists.toString());
        List<Playlist> list = playlists.getPlaylists();

        log.info(list.toString());
        log.info(String.valueOf(list.size()));

        for (Playlist playlist : list) {
            log.info(playlist.getId());
            log.info(playlist.getName());
        }

        return new Greeting(counter.incrementAndGet(),
                String.format(template, name),
                quote);
    }

    @Bean
    public RestTemplate oAuthRestTemplate() {
        ClientCredentialsResourceDetails resourceDetails = new ClientCredentialsResourceDetails();
        resourceDetails.setId("1");
        resourceDetails.setClientId("62ab5fc16b6a4a97aa08d2d143a070a1");
        resourceDetails.setClientSecret("c4c9078805624d4ba127fb658832e350");
        resourceDetails.setAccessTokenUri(tokenUrl);

        DefaultOAuth2ClientContext oauth2ClientContext = new DefaultOAuth2ClientContext();
        OAuth2RestTemplate restTemplate = new OAuth2RestTemplate(resourceDetails, oauth2ClientContext);

        return restTemplate;
    }
}

