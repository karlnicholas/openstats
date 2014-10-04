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
	private GroupName[] assemblyGroups;
	private GroupName[] assemblyGroupItems;
	
    @Inject
    private AssemblyRepository assemblyRepository;
    
    @PostConstruct
	public void postConstruct() {
    	assemblyTitles = new TreeMap<String, Object>();
		List<Assembly> assemblies = assemblyRepository.listAllAssemblies(); 
		for ( Assembly assembly: assemblies ) {
			assemblyTitles.put(assembly.getState() + " " + assembly.getSession(), assembly.getState() + "-" + assembly.getSession());
		}
	}
        
    private void loadGroups(String currentAssembly) {
		this.currentAssembly = currentAssembly;		
		Set<GroupName> groups = new TreeSet<GroupName>();
		if ( currentAssembly != null && !currentAssembly.isEmpty()) {
			String[] keys = currentAssembly.split("-"); 
			Assembly assembly = assemblyRepository.findByStateSession(keys[0], keys[1]);
			for ( GroupName key: assembly.getAggregateGroupMap().keySet() ) {
				groups.add( key );
			}
			for ( GroupName key: assembly.getComputationGroupMap().keySet() ) {
				groups.add( key );
			}
			for ( GroupName key: assembly.getDistricts().getAggregateGroupMap().keySet() ) {
				groups.add(key);
			}
			for ( GroupName key: assembly.getDistricts().getComputationGroupMap().keySet() ) {
				groups.add(key);
			}
		}
		this.assemblyGroups = new GroupName[groups.size()];
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

	public GroupName[] getAssemblyGroups() {
		return assemblyGroups;
	}

	public void setAssemblyGroups(GroupName[] assemblyGroups) {
		this.assemblyGroups = assemblyGroups;
	}

	public GroupName[] getAssemblyGroupItems() {
		return assemblyGroupItems;
	}

	public void setAssemblyGroupItems(GroupName[] assemblyGroupItems) {
		this.assemblyGroupItems = assemblyGroupItems;
	}

}
