/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.expense.rest;

import cz.muni.expense.auth.AuthAccessElement;
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
import cz.muni.expense.data.UserRepository;
import cz.muni.expense.enums.UserRole;
import cz.muni.expense.model.BaseEntity;
import cz.muni.expense.model.User;
import cz.muni.expense.model.User_;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Peter Petrinec
 * @param <T>
 */
public abstract class GenericRESTService<T extends BaseEntity> {

    @Context
    UriInfo uriInfo;

    @Context
    protected HttpServletRequest req;

    @Inject
    protected UserRepository userRepository;

    protected GenericRepository<T> repository;

    protected void setRepository(GenericRepository<T> repository) {
        this.repository = repository;
    }

    @RolesAllowed({UserRole.BASIC_USER, UserRole.ADMIN, UserRole.PRIVILEGED_USER})
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<T> listAll() {
        return repository.findAll();
    }

    @RolesAllowed({UserRole.BASIC_USER, UserRole.ADMIN, UserRole.PRIVILEGED_USER})
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

    @RolesAllowed({UserRole.BASIC_USER, UserRole.ADMIN, UserRole.PRIVILEGED_USER})
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(T entity) {
        setUser(entity);
        T e = repository.create(entity);
        URI createdUri = URI.create(uriInfo.getAbsolutePath() + e.getId().toString());
        return Response.created(createdUri).entity(e).build();
    }

    @RolesAllowed({UserRole.BASIC_USER, UserRole.ADMIN, UserRole.PRIVILEGED_USER})
    @POST
    @Path("/{id:[0-9][0-9]*}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(T entity) {
        System.out.println(entity);
        setUser(entity);
        System.out.println(entity);
        if (!canEdit(entity)) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        T e = repository.update(entity);
        URI createdUri = URI.create(uriInfo.getAbsolutePath() + e.getId().toString());
        return Response.created(createdUri).entity(e).build();
    }

    @RolesAllowed({UserRole.BASIC_USER, UserRole.ADMIN, UserRole.PRIVILEGED_USER})
    @DELETE
    @Path("/{id:[0-9][0-9]*}")
    public Response deleteById(@PathParam("id") long id) {
        Response.ResponseBuilder builder = null;

        T t = repository.findById(id);
        if (t == null) {
            builder = Response.status(Response.Status.BAD_REQUEST);
        } else {
            if (canEdit(t)) {
                repository.delete(t);
                builder = Response.status(Response.Status.OK);
            } else {
                builder = Response.status(Response.Status.FORBIDDEN);
            }
        }

        return builder.build();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

    protected User getUser() {
        String userName = req.getHeader(AuthAccessElement.PARAM_AUTH_ID);
        System.out.println(userName);
        return userRepository.findByUsername(userName);
    }

    protected void setUser(T entity) {
    }

    protected boolean canEdit(T entity) {
        return true;
    }
}
