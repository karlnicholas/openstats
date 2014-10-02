/*
 * JBoss, Home of Professional Open Source
 * Copyright 2014, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package openstats.rest;

import java.util.*;
import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.persistence.criteria.*;
import javax.validation.*;
import javax.ws.rs.*;
import javax.ws.rs.Path;
import javax.ws.rs.core.*;

import openstats.data.*;
import openstats.model.*;
import openstats.model.DtoInterface.DTOTYPE;
import openstats.service.AssemblyUpdate;

/**
 * JAX-RS Example
 * <p/>
 * This class produces a RESTful service to read/write the contents of the assemblies table.
 */
@Path("/assemblies")
@RequestScoped
public class AssemblyResourceRESTService {

    @Inject
    private Logger log;

    @Inject
    private AssemblyRepository repository;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Assembly> listAllAssemblies() {
        return repository.listAllAssemblies();
    }

    @GET
    @Path("/summary/{id:[0-9][0-9]*}")
    @Produces(MediaType.APPLICATION_JSON)
    public Assembly getSummary(@PathParam("id") long id) {
        Assembly assembly = repository.findById(id);
    	assembly = assembly.createDto(DTOTYPE.SUMMARY);
        if (assembly == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return assembly;
    }

    @GET
    @Path("/full/{id:[0-9][0-9]*}")
    @Produces(MediaType.APPLICATION_JSON)
    public Assembly getFull(@PathParam("id") long id) {
        Assembly assembly = repository.findById(id);
    	assembly = assembly.createDto(DTOTYPE.FULL);
        if (assembly == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return assembly;
    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createAssembly(Assembly assembly) {

        Response.ResponseBuilder builder = null;

        try {


            // Create an "ok" response
            builder = Response.ok();
        } catch (Exception e) {
            // Handle generic exceptions
            Map<String, String> responseObj = new HashMap<String, String>();
            responseObj.put("error", e.getMessage());
            builder = Response.status(Response.Status.BAD_REQUEST).entity(responseObj);
        }

        return builder.build();
    }

    /**
     * Creates a JAX-RS "Bad Request" response including a map of all violation fields, and their message. This can then be used
     * by clients to show violations.
     *
     * @param violations A set of violations that needs to be reported
     * @return JAX-RS response containing all violations
     */
    private Response.ResponseBuilder createViolationResponse(Set<ConstraintViolation<?>> violations) {
        log.fine("Validation completed. violations found: " + violations.size());

        Map<String, String> responseObj = new HashMap<String, String>();

        for (ConstraintViolation<?> violation : violations) {
            responseObj.put(violation.getPropertyPath().toString(), violation.getMessage());
        }

        return Response.status(Response.Status.BAD_REQUEST).entity(responseObj);
    }

    /**
     * Checks if a assembly with the same email address is already registered. This is the only way to easily capture the
     * "@UniqueConstraint(columnNames = "email")" constraint from the Assembly class.
     *
     * @param stateKey the state to check
     * @param assembleyKey the Assembly.assembly to check
     * @return True if the email already exists, and false otherwise
     */
    public boolean stateSessionAlreadyExists(String state, String session) {
        Assembly assembly = null;
        try {
            assembly = repository.findByStateSession(state, session);
        } catch (NoResultException e) {
            // ignore
        }
        return assembly != null;
    }
}
