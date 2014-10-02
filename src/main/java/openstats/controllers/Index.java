package openstats.controllers;

import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

@ManagedBean
public class Index {
	
	public void clearSessionScope() {
		System.out.println("clear");
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().clear();
	}

}
