package com.weiglewilczek.xwt.launcher.model;

import java.io.Serializable;

import com.weiglewilczek.xwt.launcher.managers.JavaInstallationFields;

public class JavaInstallation extends Installation<JavaInstallationFields> implements Serializable, Cloneable {

	private static final long serialVersionUID = 1L;

	public JavaInstallation(Long id, String name, String pathToExecutable) {
		super(id, name, pathToExecutable);
	}
	
	public JavaInstallation(String name, String pathToExecutable) {
		super(name, pathToExecutable);
	}
	
	public JavaInstallation() {
		super();
	}
	
	public boolean equals(Object obj) {
		if(obj != null && obj instanceof JavaInstallation)
		{
			return super.equals(obj);
		}
		
		return false;
	}

	@Override
	public String toString() {
		return "JavaInstallation[id="+getId()+", name="+getName()+", path="+getPathToExecutable()+"]";
	}
	
	@Override
	protected Object clone() throws CloneNotSupportedException {
		return new JavaInstallation(getName(), getPathToExecutable());
	}
	
	@Override
	public Object getProperty(JavaInstallationFields field) {
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
	public void setProperty(JavaInstallationFields field, Object value) {
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
