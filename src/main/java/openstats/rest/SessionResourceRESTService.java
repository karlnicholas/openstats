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

import java.util.List;
import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.Validator;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

import openstats.data.*;
import openstats.model.*;

/**
 * JAX-RS Example
 * <p/>
 * This class produces a RESTful service to read/write the contents of the sessions table.
 */
@Path("/sessions")
@RequestScoped
public class SessionResourceRESTService {

    @Inject
    private SessionRepository repository;

    @Inject
    private Logger log;

    @Inject
    private Validator validator;


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Session> listAllSessions() {
        return repository.listAllSessions();
    }

    @GET
    @Path("/{id:[0-9][0-9]*}")
    @Produces(MediaType.APPLICATION_JSON)
    public String lookupSessionById(@PathParam("id") long id) {
        Session session = repository.loadById(id);
        if (session == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return "XXXX";
    }

    /**
     * Creates a new session from the values provided. Performs validation, and will return a JAX-RS response with either 200 ok,
     * or with a map of fields, and related errors.
     */
/*    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createSession(Session session) {

        Response.ResponseBuilder builder = null;

        try {
            // Validates session using bean validation
            validateSession(session);

            registration.register(session);

            // Create an "ok" response
            builder = Response.ok();
        } catch (ConstraintViolationException ce) {
            // Handle bean validation issues
            builder = createViolationResponse(ce.getConstraintViolations());
        } catch (ValidationException e) {
            // Handle the unique constrain violation
            Map<String, String> responseObj = new HashMap<String, String>();
            responseObj.put("email", "Email taken");
            builder = Response.status(Response.Status.CONFLICT).entity(responseObj);
        } catch (Exception e) {
            // Handle generic exceptions
            Map<String, String> responseObj = new HashMap<String, String>();
            responseObj.put("error", e.getMessage());
            builder = Response.status(Response.Status.BAD_REQUEST).entity(responseObj);
        }

        return builder.build();
    }
*/
    /**
     * <p>
     * Validates the given Session variable and throws validation exceptions based on the type of error. If the error is standard
     * bean validation errors then it will throw a ConstraintValidationException with the set of the constraints violated.
     * </p>
     * <p>
     * If the error is caused because an existing session with the same email is registered it throws a regular validation
     * exception so that it can be interpreted separately.
     * </p>
     *
     * @param session Session to be validated
     * @throws ConstraintViolationException If Bean Validation errors exist
     * @throws ValidationException If session with the same email already exists
     */
/*    
    private void validateSession(Session session) throws ConstraintViolationException, ValidationException {
        // Create a bean validator and check for issues.
        Set<ConstraintViolation<Session>> violations = validator.validate(session);

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violations));
        }

        // Check the uniqueness of the email address
        if (emailAlreadyExists(session.getEmail())) {
            throw new ValidationException("Unique Email Violation");
        }
    }
*/
    /**
     * Creates a JAX-RS "Bad Request" response including a map of all violation fields, and their message. This can then be used
     * by clients to show violations.
     *
     * @param violations A set of violations that needs to be reported
     * @return JAX-RS response containing all violations
     */
/*    
    private Response.ResponseBuilder createViolationResponse(Set<ConstraintViolation<?>> violations) {
        log.fine("Validation completed. violations found: " + violations.size());

        Map<String, String> responseObj = new HashMap<String, String>();

        for (ConstraintViolation<?> violation : violations) {
            responseObj.put(violation.getPropertyPath().toString(), violation.getMessage());
        }

        return Response.status(Response.Status.BAD_REQUEST).entity(responseObj);
    }
*/
    /**
     * Checks if a session with the same email address is already registered. This is the only way to easily capture the
     * "@UniqueConstraint(columnNames = "email")" constraint from the Session class.
     *
     * @param email The email to check
     * @return True if the email already exists, and false otherwise
     */
/*
    public boolean stateSessionAlreadyExists(String state, String session) {
        Session session = null;
        try {
            session = repository.findByEmail(email);
        } catch (NoResultException e) {
            // ignore
        }
        return session != null;
    }
*/    
}
