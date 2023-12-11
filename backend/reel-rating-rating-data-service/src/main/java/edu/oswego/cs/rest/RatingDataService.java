package edu.oswego.cs.rest;

import com.ibm.websphere.security.jwt.JwtConsumer;

import edu.oswego.cs.rest.JsonClasses.JSession;
import edu.oswego.cs.rest.JsonClasses.Rating;
import edu.oswego.cs.rest.JsonClasses.Tag;
import jakarta.enterprise.context.RequestScoped;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.*;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/")
@RequestScoped
public class RatingDataService {
  
  String AuthServiceUrl = System.getenv("AUTH_SERVICE_URL");

  /**
   * gets the username of the client request. Also authenticates the client using a JWT.
   *
   * @param sessionId
   * @return String representation of the username within the request
   * @throws Exception
   */
  public String getUsername(String sessionId) throws Exception {
    Client authClient = ClientBuilder.newClient();
    WebTarget target = authClient.target(AuthServiceUrl + "/reel-rating-auth-service/jwt/generate/" + sessionId);
    Response response = target.request().get();
    String value = response.readEntity(String.class);
    if (value == null || value == "") {
       return null;
    }
    JwtConsumer jwtConsumer = JwtConsumer.create("reel_rating_token");
    String username = jwtConsumer.createJwt(value)
               .getClaims()
               .getAllClaims().get("upn").toString();
    authClient.close();
    return username;
  }

  /*
   * Rating Create Endpoints
   *
   * createRating
   */
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Path("/rating/create")
  public Response createRating(@Context HttpServletRequest request, Rating rating) throws Exception {
    String sessionId = rating.getJSESSIONID();
    String requesterUsername = getUsername(sessionId);
    if (requesterUsername == null) { return Response.status(Response.Status.UNAUTHORIZED).build(); }
    DatabaseController dbc = new DatabaseController();
    dbc.createRating(rating.getRatingName(), rating.getUserRating(), rating.getUpperbound(), rating.getSubtype(), requesterUsername, rating.getMovieId(), rating.getPrivacy());
    return Response.ok().build();
  }

  /*
   * Rating Get Endpoints
   *
   * getMostPopularAggregatedRatingForMovie
   * getRatingsWithSameNameAndUpperbound
   * getRatingsWithSameName
   * getRatingsWithMovieId
   * getRatingsWithUpperbound
   */
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/rating/getMostPopularAggregatedRatingForMovie/{movieId}")
  public Response getMostPopularAggregatedRatingForMovie(@Context HttpServletRequest request, @PathParam("movieId") String movieId, JSession jsession) throws Exception {
    String sessionId = jsession.getJSESSIONID();
    String requesterUsername = getUsername(sessionId);
    if (requesterUsername == null) { return Response.status(Response.Status.UNAUTHORIZED).build(); }
    DatabaseController dbc = new DatabaseController();
    Rating aggregatedRating = dbc.getMostPopularAggregatedRatingForMovie(movieId);
    return Response.ok(aggregatedRating).build();
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/rating/getRatingsWithSameNameAndUpperbound/")
  public Response getRatingsWithSameNameAndUpperbound(@Context HttpServletRequest request, Rating rating) throws Exception {
    String sessionId = rating.getJSESSIONID();
    String requesterUsername = getUsername(sessionId);
    if (requesterUsername == null) { return Response.status(Response.Status.UNAUTHORIZED).build(); }
    DatabaseController dbc = new DatabaseController();
    List<Rating> ratings = dbc.getRatingsWithSameNameAndUpperbound(rating.getRatingName(), rating.getUpperbound());
    return Response.ok(ratings).build();
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/rating/getRatingsWithSameName/{ratingName}")
  public Response getRatingsWithSameName(@Context HttpServletRequest request, @PathParam("ratingName") String ratingName, JSession jsession) throws Exception {
    String sessionId = jsession.getJSESSIONID();
    String requesterUsername = getUsername(sessionId);
    if (requesterUsername == null) { return Response.status(Response.Status.UNAUTHORIZED).build(); }
    DatabaseController dbc = new DatabaseController();
    List<Rating> ratings = dbc.getRatingsWithSameName(ratingName);
    return Response.ok(ratings).build();
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/rating/getRatingsWithMovieId/{movieId}")
  public Response getRatingsWithMovieId(@Context HttpServletRequest request, @PathParam("movieId") String movieId, JSession jsession) throws Exception {
    String sessionId = jsession.getJSESSIONID();
    String requesterUsername = getUsername(sessionId);
    if (requesterUsername == null) { return Response.status(Response.Status.UNAUTHORIZED).build(); }
    DatabaseController dbc = new DatabaseController();
    List<Rating> ratings = dbc.getRatingsWithMovieId(movieId);
    return Response.ok(ratings).build();
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/rating/getRatingsWithUpperbound/{upperbound}")
  public Response getRatingsWithUpperbound(@Context HttpServletRequest request, @PathParam("upperbound") String upperbound , JSession jsession) throws Exception {
    String sessionId = jsession.getJSESSIONID();
    String requesterUsername = getUsername(sessionId);
    if (requesterUsername == null) { return Response.status(Response.Status.UNAUTHORIZED).build(); }
    DatabaseController dbc = new DatabaseController();
    List<Rating> ratings = dbc.getRatingsWithUpperbound(upperbound);
    return Response.ok(ratings).build();
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/rating/getUniqueRatingCategoriesAndUserRatingWithMovieId/{movieId}")
  public Response getUniqueRatingCategoriesAndUserRatingWithMovieId(@Context HttpServletRequest request, @PathParam("movieId") String movieId , JSession jsession) throws Exception {
    String sessionId = jsession.getJSESSIONID();
    String requesterUsername = getUsername(sessionId);
    if (requesterUsername == null) { return Response.status(Response.Status.UNAUTHORIZED).build(); }
    DatabaseController dbc = new DatabaseController();
    List<Rating> ratings = dbc.getUniqueRatingCategoriesAndUserRatingWithMovieId(movieId, requesterUsername);
    return Response.ok(ratings).build();
  }

  /*
   * Tag Create Endpoints
   *
   * createTag
   */

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Path("/tag/create/{movieId}")
  public Response createTag(@Context HttpServletRequest request, Tag tag, @PathParam("movieId") String movieId) throws Exception {
    String sessionId = tag.getJSESSIONID();
    String requesterUsername = getUsername(sessionId);
    if (requesterUsername == null) { return Response.status(Response.Status.UNAUTHORIZED).build(); }
    DatabaseController db = new DatabaseController();
    db.createTag(tag.getTagName(), movieId, requesterUsername, tag.getPrivacy());
    return Response.ok().build();
  }

  /*
   * Tag Get Endpoints
   *
   * getTagsWithMovieId
   * getTagsWithTagName
   * getTagsWithUsername
   * getTagState
   * getTagScoresForMovieModal
   */
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/tag/getTagsWithMovieId/{movieId}")
  public Response getTagsWithMovieId(@Context HttpServletRequest request, @PathParam("movieId") String movieId, JSession jsession) throws Exception {
    String sessionId = jsession.getJSESSIONID();
    String requesterUsername = getUsername(sessionId);
    if (requesterUsername == null) { return Response.status(Response.Status.UNAUTHORIZED).build(); }
    DatabaseController dbc = new DatabaseController();
    List<Tag> tags = dbc.getTagsWithMovieId(movieId);
    return Response.ok(tags).build();
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/tag/getTagsWithTagName/{tagName}")
  public Response getTagsWithTagName(@Context HttpServletRequest request, @PathParam("tagName") String tagName, JSession jsession) throws Exception {
    String sessionId = jsession.getJSESSIONID();
    String requesterUsername = getUsername(sessionId);
    if (requesterUsername == null) { return Response.status(Response.Status.UNAUTHORIZED).build(); }

    DatabaseController dbc = new DatabaseController();
    List<Tag> tags = dbc.getTagsWithTagName(tagName);
    return Response.ok(tags).build();
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/tag/getTagsWithUsername/{username}")
  public Response getTagsWithUsername(@Context HttpServletRequest request, @PathParam("username") String username, JSession jsession) throws Exception {
    String sessionId = jsession.getJSESSIONID();
    String requesterUsername = getUsername(sessionId);
    if (requesterUsername == null) { return Response.status(Response.Status.UNAUTHORIZED).build(); }

    DatabaseController dbc = new DatabaseController();
    List<Tag> tags = dbc.getTagsWithUsername(username);
    return Response.ok(tags).build();
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/tag/getTagState/")
  public Response getTagState(@Context HttpServletRequest request, Tag tag) throws Exception {
    String sessionId = tag.getJSESSIONID();
    String requesterUsername = getUsername(sessionId);
    if (requesterUsername == null) { return Response.status(Response.Status.UNAUTHORIZED).build(); }

    DatabaseController dbc = new DatabaseController();
    Tag returnTag = new Tag();
    returnTag.setTagName(tag.getTagName()); returnTag.setUsername(requesterUsername); returnTag.setMovieId(tag.getMovieId());
    returnTag.setState(dbc.getTagState(requesterUsername, tag.getMovieId(), tag.getTagName()));
    return Response.ok(returnTag).build();
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/tag/getTagScoresForMovieModal/{movieId}")
  public Response getTagScoresForMovieModal(@Context HttpServletRequest request, @PathParam("movieId") String movieId, JSession jsession) throws Exception {
    String sessionId = jsession.getJSESSIONID();
    String requesterUsername = getUsername(sessionId);
    if (requesterUsername == null) { return Response.status(Response.Status.UNAUTHORIZED).build(); }

    DatabaseController dbc = new DatabaseController();
    List<Tag> tags = dbc.getTagScoresForMovieModal(requesterUsername, movieId);
    return Response.ok(tags).build();
  }

  /*
   * Tag Update Endpoints
   *
   * upvoteTag
   * downvoteTag
   */
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/tag/upvoteTag/")
  public Response upvoteTag(@Context HttpServletRequest request, Tag tag) throws Exception {
    String sessionId = tag.getJSESSIONID();
    String requesterUsername = getUsername(sessionId);
    if (requesterUsername == null) {
      return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    DatabaseController dbc = new DatabaseController();
    dbc.upvoteTag(getUsername(sessionId), tag.getTagName(), tag.getMovieId());
    return Response.ok().build();
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/tag/downvoteTag/")
  public Response downvoteTag(@Context HttpServletRequest request, Tag tag) throws Exception {
    String sessionId = tag.getJSESSIONID();
    String requesterUsername = getUsername(sessionId);
    if (requesterUsername == null) {
      return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    DatabaseController dbc = new DatabaseController();
    dbc.downvoteTag(getUsername(sessionId), tag.getTagName(), tag.getMovieId());
    return Response.ok().build();
  }
}
