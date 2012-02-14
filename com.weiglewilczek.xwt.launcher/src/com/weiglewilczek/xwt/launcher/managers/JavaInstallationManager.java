package com.weiglewilczek.xwt.launcher.managers;

import java.util.List;

import com.weiglewilczek.xwt.launcher.model.JavaInstallation;
import com.weiglewilczek.xwt.launcher.model.LaunchConfiguration;

public class JavaInstallationManager extends
		BaseManager<JavaInstallation, JavaInstallationFields> {
	private static JavaInstallationManager instance;

	private JavaInstallationManager() {
		super();
	}

	public static final JavaInstallationManager getInstance() {
		if (instance == null)
			instance = new JavaInstallationManager();

		return instance;
	}

	@Override
	public List<JavaInstallation> enumerateAll() {
		return super.enumerateAll(JavaInstallation.class);
	}

	@Override
	protected JavaInstallationFields[] getFields() {
		return JavaInstallationFields.values();
	}

	@Override
	protected PreferenceFields getNextIdField() {
		return PreferenceFields.JAVA_INSTALLATION_NEXT_ID;
	}

	@Override
	protected PreferenceFields getIdsListField() {
		return PreferenceFields.JAVA_INSTALLATIONS;
	}

	@Override
	protected JavaInstallation createModel() {
		return new JavaInstallation();
	}

	@Override
	public void delete(JavaInstallation object) throws Exception {
		List<LaunchConfiguration> launchConfigurations = LaunchConfigurationManager
				.getInstance().enumerateAll();
		for (LaunchConfiguration launchConfiguration : launchConfigurations) {
			if (object.equals(launchConfiguration.getJava())) {
				throw new ConstraintViolationException(
						"The Java Installation cannot be delete. It is referenced by min. a Launch Configuration.");
			}
		}

		super.delete(object);
	}
}
