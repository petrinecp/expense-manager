/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.expense.rest;

import java.net.URI;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import cz.muni.expense.data.GenericRepository;
import cz.muni.expense.enums.UserRole;
import cz.muni.expense.model.BaseEntity;
import javax.annotation.security.RolesAllowed;

/**
 *
 * @author Peter Petrinec
 * @param <T>
 */
public abstract class GenericRESTService<T extends BaseEntity>{

    @Context
    UriInfo uriInfo;
    
    protected GenericRepository<T> repository;

    protected void setRepository(GenericRepository<T> repository) {
        this.repository = repository;
    }

    @RolesAllowed({UserRole.BASIC_USER})
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<T> listAll() {
        return repository.findAll();
    }

    @GET
    @Path("/{id:[0-9][0-9]*}")
    @Produces(MediaType.APPLICATION_JSON)
    public T lookupById(@PathParam("id") long id) {
        T t = repository.findById(id);
        if (t == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return t;
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)  
    public Response create(T entity) {
        T e = repository.create(entity);
        URI createdUri = URI.create(uriInfo.getAbsolutePath() + e.getId().toString());
        return Response.created(createdUri).entity(e).build();
    }
    
    @POST
    @Path("/{id:[0-9][0-9]*}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)  
    public Response update(T entity) {
        T e = repository.update(entity);
        URI createdUri = URI.create(uriInfo.getAbsolutePath() + e.getId().toString());
        return Response.created(createdUri).entity(e).build();
    }
    
    @DELETE
    @Path("/{id:[0-9][0-9]*}")
    public Response deleteById(@PathParam("id") long id) {
        Response.ResponseBuilder builder = null;
        
        T t = repository.findById(id);
        if (t == null) {
            builder = Response.status(Response.Status.BAD_REQUEST);
        } else {
            repository.delete(t);
            builder = Response.status(Response.Status.OK);
        }
        
        return builder.build();
    }
    
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
