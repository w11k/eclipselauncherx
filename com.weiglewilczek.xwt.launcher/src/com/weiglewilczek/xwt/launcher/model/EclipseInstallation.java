package com.weiglewilczek.xwt.launcher.model;

import java.io.Serializable;

import com.weiglewilczek.xwt.launcher.managers.EclipseInstallationFields;

public class EclipseInstallation extends Installation<EclipseInstallationFields> implements Serializable, Cloneable {
	
	private static final long serialVersionUID = 1L;

	public EclipseInstallation(Long id, String name, String pathToExecutable) {
		super(id, name, pathToExecutable);
	}
	
	public EclipseInstallation(String name, String pathToExecutable) {
		super(name, pathToExecutable);
	}
	
	public EclipseInstallation() {
		super();
	}
	
	public boolean equals(Object obj) {
		if(obj != null && obj instanceof EclipseInstallation)
		{
			return super.equals(obj);
		}
		
		return false;
	}

	@Override
	public String toString() {
		return "EclipseInstallation[id="+getId()+", name="+getName()+", path="+getPathToExecutable()+"]";
	}
	
	@Override
	protected Object clone() throws CloneNotSupportedException {
		return new EclipseInstallation(getName(), getPathToExecutable());
	}

	@Override
	public Object getProperty(EclipseInstallationFields field) {
		switch (field) {
		case NAME:
			return getName();
		case PATH_TO_EXECUTABLE:
			return getPathToExecutable();
		default:
			return null;
		}
	}

	@Override
	public void setProperty(EclipseInstallationFields field, Object value) {
		switch (field) {
		case NAME:
			setName((String) value);
			break;
		case PATH_TO_EXECUTABLE:
			setPathToExecutable((String) value);
			break;
		}
	}
	
}
