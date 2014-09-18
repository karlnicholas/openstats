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
package openstats.data;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.*;

import openstats.model.*;

import java.util.*;

@ApplicationScoped
public class SessionRepository {

    @Inject
    private EntityManager em;

    public Session findById(Long id) {
        return em.find(Session.class, id);
    }

    public Session loadById(Long id) {
    	System.out.println("XXXXXXXXXXXXXXXX");
/*    	
    	CriteriaBuilder builder = em.getCriteriaBuilder();
    	CriteriaQuery<Session> sessionQuery = builder.createQuery(Session.class);
    	Root<Session> sessionRoot = sessionQuery.from(Session.class);
    	sessionQuery.select(sessionRoot);
    	sessionQuery.where( builder.equal( sessionRoot.get( "id" ), id ) );
    	Session session = em.createQuery(sessionQuery).getSingleResult();
*/
    	TypedQuery<Session> query = em.createQuery(""
    			+ "select s from Session as s "
    			+ "left join fetch s.districts as d "
    			+ "join fetch d.districtList as dl "
    			+ "join fetch dl.legislators as legs "
    			+ "join fetch d.userData as ud "
    			+ "join fetch ud.aggregates as aggs "
    			+ "join fetch aggs.groups as agroups "
    			+ "join fetch ud.computations as comps "
    			+ "join fetch comps.groups as cgroups "
    			+ "where s.id = :id", Session.class);
    	
    	query.setParameter("id", id);
    	Session session = query.getSingleResult();
    	Aggregates aggregates = session.getDistricts().getUserData().getAggregates();
    	aggregates.setDescriptions(new LinkedHashMap<String, ArrayList<String>>());
    	aggregates.setAggregateMap(new LinkedHashMap<String, Aggregate>());
    	
    	Computations computations = session.getDistricts().getUserData().getComputations();
    	computations.setDescriptions(new LinkedHashMap<String, ArrayList<String>>());
    	computations.setComputationMap(new LinkedHashMap<String, Computation>());

    	System.out.println("ZZZZZZZZZZZZZZZZZZZ");
    	return session;
    	
    }

    public List<Session> listAllSessions() {
		return em.createNamedQuery("Session.listSessions", Session.class).getResultList();
    }

}
