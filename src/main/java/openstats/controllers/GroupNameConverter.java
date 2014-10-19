package openstats.controllers;

import javax.faces.bean.ManagedBean;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.*;
import javax.persistence.*;

import openstats.model.*;
import openstats.osmodel.OSGroup;

@ManagedBean(name = "groupNameConverterBean") 
@FacesConverter(value = "groupNameConverter")
public class GroupNameConverter implements Converter {
	
	@PersistenceContext(unitName = "openstats")
    private transient EntityManager em;
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {		
		try {
			return GroupNameHandler.getDBGroup(value, em);
		} catch (OpenStatsException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		return ((OSGroup)value).getGroupName();
	}

}
