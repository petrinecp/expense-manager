/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.expense.rest;

import java.util.List;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import cz.muni.expense.data.GenericRepository;
import cz.muni.expense.model.BaseEntity;

/**
 *
 * @author Peter Petrinec
 * @param <T>
 */
public abstract class GenericRESTService<T extends BaseEntity> {

    private GenericRepository<T> repository;

    protected void setRepository(GenericRepository<T> repository) {
        this.repository = repository;
    }

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
}
