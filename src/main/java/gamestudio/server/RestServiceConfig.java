package gamestudio.server;


import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;

import gamestudio.server.webservice.CommentRestService;
import gamestudio.server.webservice.RatingRestService;
import gamestudio.server.webservice.ScoreRestService;

import javax.ws.rs.ApplicationPath;

@Configuration
@ApplicationPath("/rest")
public class RestServiceConfig extends ResourceConfig {
    //Jersey
    public RestServiceConfig() {
       register(CommentRestService.class);
       register(RatingRestService.class);
       register(ScoreRestService.class);
        //packages("gamestudio.server");
    }
}
