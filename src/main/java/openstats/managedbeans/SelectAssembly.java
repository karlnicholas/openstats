package openstats.managedbeans;

import java.io.*;
import java.util.*;

import javax.annotation.PostConstruct;
import javax.faces.bean.*;
import javax.inject.*;

import openstats.dbmodel.*;
import openstats.facades.AssemblyFacade;

@ManagedBean
@SessionScoped
public class SelectAssembly implements Serializable {
	private static final long serialVersionUID = 482581809901780483L;
	//
	private Map<String,Object> assemblyTitles;
	private String currentAssembly;
	//
	private DBGroup[] assemblyGroups;
	private DBGroup assemblyGroupItem;
	
    @Inject
    private AssemblyFacade assemblyFacade;
    
    @PostConstruct
	public void postConstruct() {
    	assemblyTitles = new TreeMap<String, Object>();
		List<DBAssembly> assemblies = assemblyFacade.listAllAssemblies(); 
		for ( DBAssembly assembly: assemblies ) {
			assemblyTitles.put(assembly.getState() + " " + assembly.getSession(), assembly.getState() + "-" + assembly.getSession());
		}
	}
        
    private void loadGroups(String currentAssembly) {
		this.currentAssembly = currentAssembly;		
		String[] keys = currentAssembly.split("-");
		Set<DBGroup> groups = assemblyFacade.loadGroupsForAssembly(keys[0], keys[1]);
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

	public DBGroup getAssemblyGroupItem() {
		return assemblyGroupItem;
	}

	public void setAssemblyGroupItem(DBGroup assemblyGroupItem) {
		this.assemblyGroupItem = assemblyGroupItem;
	}

}
