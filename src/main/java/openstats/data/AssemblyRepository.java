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

import javax.inject.Inject;
import javax.persistence.*;
import javax.persistence.criteria.*;

import openstats.dbmodel.*;

import java.util.*;

public class AssemblyRepository {

    @Inject
    private EntityManager em;
    public AssemblyRepository() {}
    // for testing
    public AssemblyRepository(EntityManager em) {
    	this.em = em;
    }

    public DBAssembly findById(Long id) {
        return em.find(DBAssembly.class, id);
    }

    public List<DBAssembly> listAllAssemblies() {
		return em.createNamedQuery(DBAssembly.LISTASSEMBLIES, DBAssembly.class).getResultList();
    }
    
    public DBAssembly findByStateSession(String state, String session) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<DBAssembly> criteria = cb.createQuery(DBAssembly.class);
        Root<DBAssembly> assembly = criteria.from(DBAssembly.class);
        // Swap criteria statements if you would like to try out type-safe criteria queries, a new
        // feature in JPA 2.0
        // criteria.select(member).where(cb.equal(member.get(Member_.email), email));
        criteria.select(assembly).where(
        		cb.equal(assembly.get("state"), state), 
        		cb.equal(assembly.get("session"), session)
        	);
        return em.createQuery(criteria).getSingleResult();
        
    }

    public Long checkByStateSession(String state, String session) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = cb.createQuery(Long.class);
        Root<DBAssembly> assembly = criteria.from(DBAssembly.class);
        // Swap criteria statements if you would like to try out type-safe criteria queries, a new
        // feature in JPA 2.0
        // criteria.select(member).where(cb.equal(member.get(Member_.email), email));
        criteria.select(cb.count(assembly)).where(
        		cb.equal(assembly.get("state"), state), 
        		cb.equal(assembly.get("session"), session)
        	);
        return em.createQuery(criteria).getSingleResult();
        
    }
}
