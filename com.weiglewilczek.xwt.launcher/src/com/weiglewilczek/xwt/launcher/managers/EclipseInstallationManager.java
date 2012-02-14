package com.weiglewilczek.xwt.launcher.managers;

import java.util.List;

import com.weiglewilczek.xwt.launcher.model.EclipseInstallation;
import com.weiglewilczek.xwt.launcher.model.LaunchConfiguration;

public class EclipseInstallationManager extends
		BaseManager<EclipseInstallation, EclipseInstallationFields> {

	private static EclipseInstallationManager instance;

	private EclipseInstallationManager() {
		super();
	}

	public static final EclipseInstallationManager getInstance() {
		if (instance == null)
			instance = new EclipseInstallationManager();

		return instance;
	}

	@Override
	public List<EclipseInstallation> enumerateAll() {
		return super.enumerateAll(EclipseInstallation.class);
	}

	@Override
	protected EclipseInstallationFields[] getFields() {
		return EclipseInstallationFields.values();
	}

	@Override
	protected PreferenceFields getNextIdField() {
		return PreferenceFields.ECLIPSE_INSTALLATION_NEXT_ID;
	}

	@Override
	protected PreferenceFields getIdsListField() {
		return PreferenceFields.ECLIPSE_INSTALLATIONS;
	}

	@Override
	protected EclipseInstallation createModel() {
		return new EclipseInstallation();
	}

	@Override
	public void delete(EclipseInstallation object) throws Exception {
		List<LaunchConfiguration> launchConfigurations = LaunchConfigurationManager
				.getInstance().enumerateAll();
		for (LaunchConfiguration launchConfiguration : launchConfigurations) {
			if (object.equals(launchConfiguration.getEclipse())) {
				throw new ConstraintViolationException(
						"The Eclipse Installation cannot be delete. It is referenced by min. a Launch Configuration.");
			}
		}

		super.delete(object);
	}
}
