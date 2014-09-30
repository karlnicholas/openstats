package openstats.listeners;

import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

@ManagedBean(name="indexListener")
public class IndexListener {
	
	public void clearSessionScope() {
		System.out.println("clear");
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().clear();
	}

}
