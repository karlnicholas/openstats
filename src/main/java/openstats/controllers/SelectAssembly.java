package openstats.controllers;

import java.io.*;
import java.util.*;

import javax.annotation.PostConstruct;
import javax.faces.bean.*;
import javax.inject.*;

import openstats.data.AssemblyRepository;
import openstats.model.*;

@ManagedBean
@SessionScoped
public class SelectAssembly implements Serializable {
	private static final long serialVersionUID = 482581809901780483L;
	//
	private Map<String,Object> assemblyTitles;
	private String currentAssembly;
	//
	private DBGroup[] assemblyGroups;
	private DBGroup[] assemblyGroupItems;
	
    @Inject
    private AssemblyRepository assemblyRepository;
    
    @PostConstruct
	public void postConstruct() {
    	assemblyTitles = new TreeMap<String, Object>();
		List<DBAssembly> assemblies = assemblyRepository.listAllAssemblies(); 
		for ( DBAssembly assembly: assemblies ) {
			assemblyTitles.put(assembly.getState() + " " + assembly.getSession(), assembly.getState() + "-" + assembly.getSession());
		}
	}
        
    private void loadGroups(String currentAssembly) {
		this.currentAssembly = currentAssembly;		
		Set<DBGroup> groups = new TreeSet<DBGroup>();
		if ( currentAssembly != null && !currentAssembly.isEmpty()) {
			String[] keys = currentAssembly.split("-"); 
			DBAssembly assembly = assemblyRepository.findByStateSession(keys[0], keys[1]);
			for ( DBGroup key: assembly.getAggregateGroupMap().keySet() ) {
				groups.add( key );
			}
			for ( DBGroup key: assembly.getComputationGroupMap().keySet() ) {
				groups.add( key );
			}
			for ( DBGroup key: assembly.getDistricts().getAggregateGroupMap().keySet() ) {
				groups.add(key);
			}
			for ( DBGroup key: assembly.getDistricts().getComputationGroupMap().keySet() ) {
				groups.add(key);
			}
		}
		this.assemblyGroups = new DBGroup[groups.size()];
		this.assemblyGroups = groups.toArray(this.assemblyGroups);
	}

	public Map<String, Object> getAssemblyTitles() {
		return assemblyTitles;
	}

	public void setAssemblyTitles(Map<String, Object> assemblyTitles) {
		this.assemblyTitles = assemblyTitles;
	}

	public String getCurrentAssembly() {
		return currentAssembly;
	}

	public void setCurrentAssembly(String currentAssembly) {
		this.currentAssembly = currentAssembly;
		loadGroups(currentAssembly);
	}

	public DBGroup[] getAssemblyGroups() {
		return assemblyGroups;
	}

	public void setAssemblyGroups(DBGroup[] assemblyGroups) {
		this.assemblyGroups = assemblyGroups;
	}

	public DBGroup[] getAssemblyGroupItems() {
		return assemblyGroupItems;
	}

	public void setAssemblyGroupItems(DBGroup[] assemblyGroupItems) {
		this.assemblyGroupItems = assemblyGroupItems;
	}

}
