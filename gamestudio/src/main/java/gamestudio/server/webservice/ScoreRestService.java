package gamestudio.server.webservice;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;

import gamestudio.entity.Score;
import gamestudio.service.ScoreService;

@Path("/score")
public class ScoreRestService {

	@Autowired
	private ScoreService scoreService;

	// http://localhost:8080/rest/score/
	@POST
	@Consumes("application/json")
	public Response addScore(Score score) {
		scoreService.addScore(score);
		return Response.ok().build();
	}

	// http://localhost:8080/rest/score/mines
	@GET
	@Path("/{game}")
	@Produces("application/json")
	public List<Score> getBestScoresForGame(@PathParam("game") String game) {
		System.out.println("getBestScoresForGame - " + game);
		List<Score> res = scoreService.getTopScores(game);
		System.out.println("Result: " + res);
		return res;
	}
}
