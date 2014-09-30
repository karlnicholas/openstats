package openstats.pagebeans;

import java.io.*;
import java.util.*;

import javax.annotation.PostConstruct;
import javax.faces.bean.*;
import javax.faces.context.FacesContext;
import javax.inject.*;

import openstats.data.AssemblyRepository;
import openstats.model.*;

@ManagedBean(name="assemblies")
@SessionScoped
public class Assemblies implements Serializable {
	private static final long serialVersionUID = 482581809901780483L;
	//
	private Map<String,Object> assemblyTitles;
	private String currentAssembly;
	//
	private String[] assemblyGroups;
	private String[] assemblyGroupItems;
	
    @Inject
    private AssemblyRepository assemblyRepository;
    
    @PostConstruct
	public void postConstruct() {
    	assemblyTitles = new TreeMap<String, Object>();
		List<Assembly> assemblies = assemblyRepository.listAllAssemblies(); 
		for ( Assembly assembly: assemblies ) {
			assemblyTitles.put(assembly.getState() + " " + assembly.getAssembly(), assembly.getState() + "-" + assembly.getAssembly());
		}
	}
    
    private void loadGroups(String currentAssembly) {
		this.currentAssembly = currentAssembly;		
		Set<String> groups = new TreeSet<String>();
		if ( currentAssembly != null && !currentAssembly.isEmpty()) {
			String[] keys = currentAssembly.split("-"); 
			Assembly assembly = assemblyRepository.findByStateAssembly(keys[0], keys[1]);
			for ( String key: assembly.getAggregateGroupMap().keySet() ) {
				groups.add( key );
			}
			for ( String key: assembly.getComputationGroupMap().keySet() ) {
				groups.add( key );
			}
			for ( String key: assembly.getDistricts().getAggregateGroupMap().keySet() ) {
				groups.add(key);
			}
			for ( String key: assembly.getDistricts().getComputationGroupMap().keySet() ) {
				groups.add(key);
			}
		}
		this.assemblyGroups = new String[groups.size()];
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

	public String[] getAssemblyGroups() {
		return assemblyGroups;
	}

	public void setAssemblyGroups(String[] assemblyGroups) {
		this.assemblyGroups = assemblyGroups;
	}

	public String[] getAssemblyGroupItems() {
		return assemblyGroupItems;
	}

	public void setAssemblyGroupItems(String[] assemblyGroupItems) {
		this.assemblyGroupItems = assemblyGroupItems;
	}

}
