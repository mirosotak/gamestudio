package gamestudio.service.impl;

import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import gamestudio.entity.Rating;
import gamestudio.service.RatingService;


public class RatingServiceRestClient implements RatingService {
	private static final String URL = "http://localhost:8080/rest/score";

    @Override
    public void setRating(Rating rating)  {
        Client client = ClientBuilder.newClient();
        Response response = client.target(URL).request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(rating, MediaType.APPLICATION_JSON), Response.class);
    }

    @Override
    public double getAverageRating(String game) {
        Client client = ClientBuilder.newClient();
        Double o = client.target(URL).path("/" + game).request(MediaType.APPLICATION_JSON)
				.get(Double.class);
        return o != null ? o : 0;
        		
    }
}



