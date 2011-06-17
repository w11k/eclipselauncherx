package com.weiglewilczek.xwt.launcher.managers;


public enum EclipseInstallationFields implements BaseFields<EclipseInstallationFields> {
	ID("EclipseInstallationId", new Long(0)),
	NAME("EclipseInstallationName", new String()),
	PATH_TO_EXECUTABLE("EclipseInstallationPathToExecutable", new String());

	private final String name;
	private final Object typeInstance;

	private EclipseInstallationFields(String name, Object typeInstance) {
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
	
	public EclipseInstallationFields findForName(String name)
	{
		for (EclipseInstallationFields field : EclipseInstallationFields.values()) {
			if(field.getName().equals(name))
				return field;
		}
		
		return null;
	}
}
