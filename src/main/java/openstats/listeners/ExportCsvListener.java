package openstats.listeners;

import java.io.*;
import java.util.*;

import javax.faces.bean.ManagedBean;
import javax.faces.context.*;
import javax.inject.Inject;

import openstats.data.AssemblyRepository;
import openstats.model.Assembly;
import openstats.pagebeans.Assemblies;
import openstats.util.WriteCsv;

@ManagedBean(name="exportCsvListener")
public class ExportCsvListener {

    @Inject
    private FacesContext facesContext;
    @Inject
    private AssemblyRepository assemblyRepository;
    
    public void goToIndex() throws IOException {
    	facesContext.getExternalContext().redirect("");
    }
    
    public void exportCsv() throws Exception {
		Assemblies assemblies = (Assemblies) facesContext.getExternalContext().getSessionMap().get("assemblies");

//		System.out.println("Exporting for: " + assemblies.getCurrentAssembly() + ":" + Arrays.toString(assemblies.getAssemblyGroupItems()) );
    	WriteCsv createCsv = new WriteCsv();
    	String[] keys = assemblies.getCurrentAssembly().split("-");
    	Assembly assembly = assemblyRepository.findByStateAssembly(keys[0], keys[1]);

	    ExternalContext ec = facesContext.getExternalContext();

	    ec.responseReset(); // Some JSF component library or some Filter might have set some headers in the buffer beforehand. We want to get rid of them, else it may collide.
	    ec.setResponseContentType("text/csv;charset=WINDOWS-1252"); // Check http://www.iana.org/assignments/media-types for all types. Use if necessary ExternalContext#getMimeType() for auto-detection based on filename.
	    ec.setResponseHeader("Content-Disposition","attachment; filename=\""+assemblies.getCurrentAssembly()+".csv\"");

	    OutputStream output = ec.getResponseOutputStream();
	    // Now you can write the InputStream of the file to the above OutputStream the usual way.
	    // ...
    	createCsv.writeCsv(output, assembly, Arrays.asList(assemblies.getAssemblyGroupItems()));

	    facesContext.responseComplete(); // Important! Otherwise JSF will attempt to render the response which obviously will fail since it's already written with a file and closed.
    	
	}
    
}
