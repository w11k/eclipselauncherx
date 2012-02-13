package com.weiglewilczek.xwt.launcher.handlers;

import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.osgi.service.prefs.BackingStoreException;

import com.weiglewilczek.xwt.launcher.managers.EclipseInstallationManager;
import com.weiglewilczek.xwt.launcher.managers.GroupManager;
import com.weiglewilczek.xwt.launcher.managers.JavaInstallationManager;
import com.weiglewilczek.xwt.launcher.managers.LaunchConfigurationManager;
import com.weiglewilczek.xwt.launcher.model.EclipseInstallation;
import com.weiglewilczek.xwt.launcher.model.Group;
import com.weiglewilczek.xwt.launcher.model.JavaInstallation;
import com.weiglewilczek.xwt.launcher.model.LaunchConfiguration;
import com.weiglewilczek.xwt.launcher.model.LaunchConfigurationDataContext;
import com.weiglewilczek.xwt.launcher.model.ObservableGroup;
import com.weiglewilczek.xwt.launcher.model.ui.GroupsView;
import com.weiglewilczek.xwt.launcher.model.ui.LaunchConfigurationDialog;

public class NewLaunchConfiguration extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		Group parentGroup = null;
		ISelection selection = HandlerUtil.getCurrentSelection(event);
		if (selection != null && selection instanceof IStructuredSelection) {
			IStructuredSelection viewerSelection = (IStructuredSelection) selection;
			if (viewerSelection.getFirstElement() instanceof ObservableGroup) {
				parentGroup = ((ObservableGroup) viewerSelection
						.getFirstElement()).getGroup();
			} else if (viewerSelection.getFirstElement() instanceof LaunchConfiguration) {
				LaunchConfiguration sisterNode = (LaunchConfiguration) viewerSelection
						.getFirstElement();
				parentGroup = GroupManager.getInstance()
						.getGroupForConfiguration(sisterNode);
			}
		}

		if (parentGroup == null) {
			parentGroup = GroupManager.getInstance().get(-1l);
		}

		LaunchConfiguration configuration = new LaunchConfiguration();

		List<EclipseInstallation> eclipses = EclipseInstallationManager
				.getInstance().enumerateAll();
		WritableList writableEclipses = new WritableList();
		writableEclipses.addAll(eclipses);

		List<JavaInstallation> javas = JavaInstallationManager.getInstance()
				.enumerateAll();
		WritableList writableJavas = new WritableList();
		writableJavas.addAll(javas);

		LaunchConfigurationDataContext dataContext = new LaunchConfigurationDataContext(
				configuration, writableEclipses, writableJavas);

		LaunchConfigurationDialog launchConfiguration = new LaunchConfigurationDialog(
				HandlerUtil.getActiveShell(event), dataContext);
		if (launchConfiguration.open() == LaunchConfigurationDialog.OK) {
			try {
				LaunchConfigurationManager.getInstance().create(configuration);

				parentGroup.addConfiguration(configuration);
				GroupManager.getInstance().update(parentGroup);

				if (!(PlatformUI.getWorkbench().getActiveWorkbenchWindow()
						.getActivePage().getActivePart() instanceof GroupsView)) {
					try {
						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView("com.weiglewilczek.xwt.launcher.views.Groups");
					} catch (PartInitException e) {
						throw new ExecutionException("Error opening groups view", e);
					}
				}
			} catch (BackingStoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return null;
	}
}
