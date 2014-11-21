package openstats.managedbeans;

import java.io.*;
import java.util.*;

import javax.annotation.PostConstruct;
import javax.faces.bean.*;
import javax.faces.context.*;
import javax.inject.Inject;

import openstats.dbmodel.DBAssembly;
import openstats.dbmodel.DBGroup;
import openstats.facades.AssemblyFacade;
import openstats.model.Assembly;
import openstats.util.AssemblyCsvHandler;

@ManagedBean
@ViewScoped
public class ExportCsv implements Serializable {
	private static final long serialVersionUID = 1L;

	@Inject
    private FacesContext facesContext;
    @Inject
    private AssemblyFacade assemblyFacade;
    
    private Assembly osAssembly = null;
    
	//
	private Map<String,Object> assemblyTitles;
	private String currentAssembly;
	//
	private DBGroup[] assemblyGroups;
	private DBGroup assemblyGroupItem;
	
    @PostConstruct
	public void postConstruct() {
    	assemblyTitles = new TreeMap<String, Object>();
		List<DBAssembly> assemblies = assemblyFacade.listAllAssemblies(); 
		for ( DBAssembly assembly: assemblies ) {
			assemblyTitles.put(assembly.getState() + " " + assembly.getSession(), assembly.getState() + "-" + assembly.getSession());
		}
	}
        
    private void loadGroups() {
		if ( currentAssembly == null || currentAssembly.isEmpty() ) return;
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
		loadGroups();
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

    public void exportCsv() throws Exception {
    	AssemblyCsvHandler createCsv = new AssemblyCsvHandler();
    	String[] keys = currentAssembly.split("-");
    	Assembly osAssembly = assemblyFacade.buildOSAssembly(assemblyGroupItem, keys[0], keys[1]);

    	ExternalContext ec = facesContext.getExternalContext();

	    ec.responseReset(); // Some JSF component library or some Filter might have set some headers in the buffer beforehand. We want to get rid of them, else it may collide.
	    ec.setResponseContentType("text/csv;charset=WINDOWS-1252"); // Check http://www.iana.org/assignments/media-types for all types. Use if necessary ExternalContext#getMimeType() for auto-detection based on filename.
	    ec.setResponseHeader("Content-Disposition","attachment; filename=\""+currentAssembly+".csv\"");

	    Writer writer = new OutputStreamWriter(ec.getResponseOutputStream());
	    // Now you can write the InputStream of the file to the above OutputStream the usual way.
	    // ...
    	createCsv.writeCsv(writer, osAssembly );
    	writer.flush();

	    facesContext.responseComplete(); // Important! Otherwise JSF will attempt to render the response which obviously will fail since it's already written with a file and closed.
    	
	}

    public void goToIndex() throws IOException {
    	facesContext.getExternalContext().redirect("");
    }
	
	public Assembly getOSAssembly() throws Exception {
		if ( osAssembly == null ) {
	    	String[] keys = currentAssembly.split("-");
	    	osAssembly = assemblyFacade.buildOSAssembly(assemblyGroupItem, keys[0], keys[1]);
		}
		return osAssembly;
	}

}
