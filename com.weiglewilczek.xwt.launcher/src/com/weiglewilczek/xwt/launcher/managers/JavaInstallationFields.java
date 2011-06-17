package com.weiglewilczek.xwt.launcher.managers;


public enum JavaInstallationFields implements BaseFields<JavaInstallationFields> {
	ID("JavaInstallationId", new Long(0)),
	NAME("JavaInstallationName", new String()),
	PATH_TO_EXECUTABLE("JavaInstallationPathToExecutable", new String());

	private final String name;
	private final Object typeInstance;

	private JavaInstallationFields(String name, Object typeInstance) {
		this.name = name;
		this.typeInstance = typeInstance;
	}
	
	public String getName()
	{
		return name;
	}
	
	public Object getTypeInstance() {
		return typeInstance;
	}
	
	public JavaInstallationFields findForName(String name)
	{
		for (JavaInstallationFields field : JavaInstallationFields.values()) {
			if(field.getName().equals(name))
				return field;
		}
		
		return null;
	}
}
