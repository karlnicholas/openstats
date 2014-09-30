package openstats.listeners;

import java.util.Arrays;

import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import openstats.pagebeans.Assemblies;

@ManagedBean(name="exportCsvListener")
public class ExportCsvListener {

	public void exportCsv() throws Exception {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		Assemblies assemblies = (Assemblies) facesContext.getExternalContext().getSessionMap().get("assemblies");

		System.out.println("Exporting for: " + assemblies.getCurrentAssembly() + ":" + Arrays.toString(assemblies.getAssemblyGroupItems()) );
		facesContext.getExternalContext().redirect("");
	}
}
