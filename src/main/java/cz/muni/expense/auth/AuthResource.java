package cz.muni.expense.auth;


import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author majo
 */
@Path("/auth")
public class AuthResource {

    @EJB
    AuthService authService;

    @POST
    @Path("login")
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response login(@Context HttpServletRequest request, AuthLoginElement loginElement) {
        AuthAccessElement accessElement = authService.login(loginElement);
        if (accessElement != null) {
            request.getSession().setAttribute(AuthAccessElement.PARAM_AUTH_ID, accessElement.getAuthId());
            request.getSession().setAttribute(AuthAccessElement.PARAM_AUTH_TOKEN, accessElement.getAuthToken());
            return Response.ok(accessElement, MediaType.APPLICATION_JSON).build();
        }
        return Response.status(Response.Status.NOT_FOUND).entity("User not found").build();
    }
    
    @POST
    @Path("logout")
    @PermitAll
    public Response logout(@Context HttpServletRequest request) {
    	Response.ResponseBuilder builder = null;
    	
    	String authId = (String) request.getSession().getAttribute(AuthAccessElement.PARAM_AUTH_ID);
    	String authToken = (String) request.getSession().getAttribute(AuthAccessElement.PARAM_AUTH_TOKEN);
    	
    	if(authService.logout(authId, authToken)){
    		builder = Response.status(Response.Status.OK).entity("User successfully logged out.");
    	} else {
    		builder = Response.status(Response.Status.BAD_REQUEST).entity("User not found or not logged in.");
    	};
    	
    	return builder.build();
    }
}