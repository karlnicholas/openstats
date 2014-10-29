package openstats.managedbeans;

import java.io.*;
import java.util.*;

import javax.faces.bean.*;
import javax.faces.context.*;
import javax.inject.Inject;

import openstats.facades.AssemblyFacade;
import openstats.osmodel.OSAssembly;
import openstats.util.AssemblyCsvHandler;

@ManagedBean
@RequestScoped
public class ExportCsv {

    @Inject
    private FacesContext facesContext;
    @Inject
    private AssemblyFacade assemblyFacade;
    
    private List<List<String>> csvBody = null;
    private List<String> csvHeader = null;
    
    public void goToIndex() throws IOException {
    	Map<String,Object> sessionMap = facesContext.getExternalContext().getSessionMap();
    	sessionMap.remove("selectAssembly");
    	sessionMap.remove("csvBody");
    	sessionMap.remove("csvHeader");
    	facesContext.getExternalContext().redirect("");
    }
    
    public void exportCsv() throws Exception {
		SelectAssembly assemblies = (SelectAssembly) facesContext.getExternalContext().getSessionMap().get("selectAssembly");

//		System.out.println("Exporting for: " + assemblies.getCurrentAssembly() + ":" + Arrays.toString(assemblies.getAssemblyGroupItems()) );
    	AssemblyCsvHandler createCsv = new AssemblyCsvHandler();
    	String[] keys = assemblies.getCurrentAssembly().split("-");
    	OSAssembly osAssembly = assemblyFacade.buildOSAssembly(assemblies.getAssemblyGroupItem(), keys[0], keys[1]);

    	ExternalContext ec = facesContext.getExternalContext();

	    ec.responseReset(); // Some JSF component library or some Filter might have set some headers in the buffer beforehand. We want to get rid of them, else it may collide.
	    ec.setResponseContentType("text/csv;charset=WINDOWS-1252"); // Check http://www.iana.org/assignments/media-types for all types. Use if necessary ExternalContext#getMimeType() for auto-detection based on filename.
	    ec.setResponseHeader("Content-Disposition","attachment; filename=\""+assemblies.getCurrentAssembly()+".csv\"");

	    Writer writer = new OutputStreamWriter(ec.getResponseOutputStream());
	    // Now you can write the InputStream of the file to the above OutputStream the usual way.
	    // ...
    	createCsv.writeCsv(writer, osAssembly );

	    facesContext.responseComplete(); // Important! Otherwise JSF will attempt to render the response which obviously will fail since it's already written with a file and closed.
    	
	}

	@SuppressWarnings("unchecked")
	public List<List<String>> getCsvBody() throws Exception {
		if ( csvBody != null ) return csvBody;
		ExternalContext ec = facesContext.getExternalContext();
		Map<String, Object> sessionMap = ec.getSessionMap();
		csvBody = (List<List<String>>) sessionMap.get("csvBody");
		if ( csvBody == null ) {
			createCsvSessionEntries(ec, sessionMap);
		}
		return csvBody;
	}

	@SuppressWarnings("unchecked")
	public List<String> getCsvHeader() throws Exception {
		if ( csvHeader != null ) return csvHeader;
		ExternalContext ec = facesContext.getExternalContext();
		Map<String, Object> sessionMap = ec.getSessionMap();
		csvHeader = (List<String>) sessionMap.get("csvHeader");
		if ( csvHeader == null ) {
			createCsvSessionEntries(ec, sessionMap);
		}
		return csvHeader;
	}
	
	private void createCsvSessionEntries(ExternalContext ec, Map<String, Object> sessionMap) throws Exception {
		SelectAssembly assemblies = (SelectAssembly) ec.getSessionMap().get("selectAssembly");
    	String[] keys = assemblies.getCurrentAssembly().split("-");
    	OSAssembly osAssembly = assemblyFacade.buildOSAssembly(assemblies.getAssemblyGroupItem(), keys[0], keys[1]);
		csvBody = new AssemblyCsvHandler().createBody(osAssembly);
		sessionMap.put("csvBody", csvBody);
		csvHeader = new AssemblyCsvHandler().createHeader(osAssembly);
		sessionMap.put("csvHeader", csvHeader);
	}
}
