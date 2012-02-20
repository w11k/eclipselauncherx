package com.weiglewilczek.xwt.launcher.handlers;

import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;
import java.util.prefs.BackingStoreException;

import com.weiglewilczek.xwt.launcher.managers.EclipseInstallationManager;
import com.weiglewilczek.xwt.launcher.managers.JavaInstallationManager;
import com.weiglewilczek.xwt.launcher.managers.LaunchConfigurationManager;
import com.weiglewilczek.xwt.launcher.model.EclipseInstallation;
import com.weiglewilczek.xwt.launcher.model.JavaInstallation;
import com.weiglewilczek.xwt.launcher.model.LaunchConfiguration;
import com.weiglewilczek.xwt.launcher.model.LaunchConfigurationDataContext;
import com.weiglewilczek.xwt.launcher.model.ui.LaunchConfigurationDialog;

public class EditLaunchConfiguration extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		Object currentSelection = HandlerUtil.getCurrentSelection(event);
		if (currentSelection != null
				&& currentSelection instanceof IStructuredSelection) {
			IStructuredSelection selection = (IStructuredSelection) currentSelection;

			if (selection.getFirstElement() != null && selection.getFirstElement() instanceof LaunchConfiguration) {
				LaunchConfiguration configuration = (LaunchConfiguration)selection.getFirstElement();
				
				List<EclipseInstallation> eclipses = EclipseInstallationManager
						.getInstance().enumerateAll();
				WritableList writableEclipses = new WritableList();
				writableEclipses.addAll(eclipses);

				List<JavaInstallation> javas = JavaInstallationManager
						.getInstance().enumerateAll();
				WritableList writableJavas = new WritableList();
				writableJavas.addAll(javas);
				
				LaunchConfigurationDataContext dataContext = new LaunchConfigurationDataContext(configuration, writableEclipses, writableJavas);

				LaunchConfigurationDialog launchConfiguration = new LaunchConfigurationDialog(
						HandlerUtil.getActiveShell(event), dataContext);
				if (launchConfiguration.open() == LaunchConfigurationDialog.OK) {
					try {
						LaunchConfigurationManager.getInstance().update(
								configuration);
					} catch (BackingStoreException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		
		return null;
	}

}
