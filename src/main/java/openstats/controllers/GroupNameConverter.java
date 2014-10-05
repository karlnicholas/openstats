package openstats.controllers;

import javax.faces.bean.ManagedBean;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.*;
import javax.persistence.*;

import openstats.model.*;

@ManagedBean(name = "groupNameConverterBean") 
@FacesConverter(value = "groupNameConverter")
public class GroupNameConverter implements Converter {

	@PersistenceContext(unitName = "openstats")
    private transient EntityManager em;
    
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {		
		return GroupNameHandler.getGroupName(value, em);
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		return ((GroupName)value).getGroupName();
	}

}
