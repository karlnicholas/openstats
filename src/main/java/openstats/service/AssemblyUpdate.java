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
package openstats.service;

import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import javax.validation.*;

import openstats.data.AssemblyRepository;
import openstats.dbmodel.*;
import openstats.osmodel.OSAssembly;

// The @Stateless annotation eliminates the need for manual transaction demarcation
@Stateless
public class AssemblyUpdate {
    @Inject
    private Logger log;

    @Inject
    private EntityManager em;

    @Inject
    private AssemblyRepository assemblyRepository;

    @Inject
    private Event<DBAssembly> assemblyEventSrc;

    /*
     * find the assembly in the database
     * error if not found.
     * find the group mentioned for any state-session in the database
     * if not found then create
     * if found then check consistency
     * if consistency fails this error
     * update aggregations and computations.
     * 
     */
    public void updateDBAssembly(DBAssembly assembly) throws Exception {
        log.info("Updating group for " + assembly.getState()+"-"+assembly.getSession());
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<DBAssembly> criteria = cb.createQuery(DBAssembly.class);
        Root<DBAssembly> assemblyRoot = criteria.from(DBAssembly.class);
        criteria.select(assemblyRoot).where(
        		cb.equal(assemblyRoot.get("state"), assembly.getState()), 
        		cb.equal(assemblyRoot.get("session"), assembly.getSession())
        	);
        DBAssembly assemblyDb = em.createQuery(criteria).getSingleResult();
        if ( assemblyDb == null ) {
        	throw new ValidationException("Assembly not found for state and sesssion:" +assembly.getState()+"-"+assembly.getSession());
        }

        

        assemblyEventSrc.fire(assembly);
    }

    /**
     * 
     * @param osAssembly
     * @throws OpenStatsException
     */
	public void writeOSAssembly(OSAssembly osAssembly) throws OpenStatsException {
		DBGroup dbGroup = DBGroupHandler.getDBGroup(osAssembly.getOSGroup().getGroupName(), em);
		
		if ( dbGroup == null ) {
			// create new DBGroup
			dbGroup = new DBGroup(osAssembly.getOSGroup());
			DBGroupHandler.createDBGroup(dbGroup, em);
		}
		DBAssembly dbAssembly;
		Long count = assemblyRepository.checkByStateSession(osAssembly.getState(), osAssembly.getSession());
		if ( count > 0 ) {
			// update existing one
			dbAssembly = assemblyRepository.findByStateSession(osAssembly.getState(), osAssembly.getSession());
			dbAssembly.update(dbGroup, osAssembly);
			em.merge(dbAssembly);
		} else {
			// create a new one
			dbAssembly = new DBAssembly();
			dbAssembly.update(dbGroup, osAssembly);
			em.persist(dbAssembly);
		}
	}
}
