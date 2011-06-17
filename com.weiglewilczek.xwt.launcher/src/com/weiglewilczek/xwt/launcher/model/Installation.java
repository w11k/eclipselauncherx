package com.weiglewilczek.xwt.launcher.model;

import java.io.Serializable;

import com.weiglewilczek.xwt.launcher.managers.BaseFields;

public abstract class Installation<T extends BaseFields<T>> extends ModelElement<T> implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private String name;
	
	private String pathToExecutable;

	public Installation(Long id, String name, String pathToExecutable) {
		setId(id);
		this.name = name;
		this.pathToExecutable = pathToExecutable;
	}
	
	public Installation(String name, String pathToExecutable) {
		this.name = name;
		this.pathToExecutable = pathToExecutable;
	}
	
	public Installation() {
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPathToExecutable() {
		return pathToExecutable;
	}

	public void setPathToExecutable(String pathToExecutable) {
		this.pathToExecutable = pathToExecutable;
	}

	@Override
	public String toString() {
		return "Installation[id="+getId()+", name="+name+", path="+pathToExecutable+"]";
	}
	
}
