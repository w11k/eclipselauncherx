package com.weiglewilczek.xwt.launcher.model;

import org.eclipse.core.databinding.observable.list.WritableList;

import com.weiglewilczek.xwt.launcher.listener.IListener;
import com.weiglewilczek.xwt.launcher.listener.ListenerType;
import com.weiglewilczek.xwt.launcher.managers.EclipseInstallationManager;
import com.weiglewilczek.xwt.launcher.managers.JavaInstallationManager;

public class LaunchConfigurationDataContext implements IListener {

	private LaunchConfiguration launchConfiguration;

	private WritableList javaInstallations;

	private WritableList eclipseInstallations;

	public LaunchConfigurationDataContext() {
		JavaInstallationManager.getInstance().addListener(this);
		EclipseInstallationManager.getInstance().addListener(this);
	}

	public LaunchConfiguration getLaunchConfiguration() {
		return launchConfiguration;
	}

	public void setLaunchConfiguration(LaunchConfiguration launchConfiguration) {
		this.launchConfiguration = launchConfiguration;
	}

	public WritableList getJavaInstallations() {
		return javaInstallations;
	}

	public void setJavaInstallations(WritableList javaInstallations) {
		this.javaInstallations = javaInstallations;
	}

	public WritableList getEclipseInstallations() {
		return eclipseInstallations;
	}

	public void setEclipseInstallations(WritableList eclipseInstallations) {
		this.eclipseInstallations = eclipseInstallations;
	}

	// a multi property path in xwt-File leads to stack overflow while
	// initialize binding if multiple properties of the same parent property is
	// bound!
	// so all getters and setter from launchConfiguration used in
	// LaunchConfigurationDialog are duplicated here for now.
	public String getName() {
		return launchConfiguration.getName();
	}

	public void setName(String name) {
		launchConfiguration.setName(name);
	}

	public String getWorkspacePath() {
		return launchConfiguration.getWorkspacePath();
	}

	public void setWorkspacePath(String workspacePath) {
		launchConfiguration.setWorkspacePath(workspacePath);
	}

	public EclipseInstallation getEclipse() {
		return launchConfiguration.getEclipse();
	}

	public void setEclipse(EclipseInstallation eclipse) {
		launchConfiguration.setEclipse(eclipse);
	}

	public JavaInstallation getJava() {
		return launchConfiguration.getJava();
	}

	public void setJava(JavaInstallation java) {
		launchConfiguration.setJava(java);
	}

	public String getVmArgs() {
		return launchConfiguration.getVmArgs();
	}

	public void setVmArgs(String vmArgs) {
		launchConfiguration.setVmArgs(vmArgs);
	}

	public String getEclipseArgs() {
		return launchConfiguration.getEclipseArgs();
	}

	public void setEclipseArgs(String eclipseArgs) {
		launchConfiguration.setEclipseArgs(eclipseArgs);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void handle(ListenerType type, ModelElement object) {
		switch (type) {
		case CREATE:
			if (object instanceof JavaInstallation) {
				javaInstallations.remove(object);
			}
			if (object instanceof EclipseInstallation) {
				eclipseInstallations.remove(object);
			}
			break;
		case UPDATE:
			if (object instanceof JavaInstallation) {
				javaInstallations.remove(((JavaInstallation) object).getId());
				javaInstallations.add(object);
			}
			if (object instanceof EclipseInstallation) {
				eclipseInstallations.remove(((EclipseInstallation) object)
						.getId());
				eclipseInstallations.add(object);
			}
			break;
		case DELETE:
			if (object instanceof JavaInstallation) {
				javaInstallations.remove(((JavaInstallation) object).getId());
			}
			if (object instanceof EclipseInstallation) {
				eclipseInstallations.remove(((EclipseInstallation) object)
						.getId());
			}
			break;

		default:
			break;
		}

	}
}
