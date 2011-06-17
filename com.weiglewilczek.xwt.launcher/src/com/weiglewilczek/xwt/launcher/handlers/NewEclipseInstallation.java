package com.weiglewilczek.xwt.launcher.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.handlers.HandlerUtil;
import org.osgi.service.prefs.BackingStoreException;

import com.weiglewilczek.xwt.launcher.managers.EclipseInstallationManager;
import com.weiglewilczek.xwt.launcher.model.EclipseInstallation;
import com.weiglewilczek.xwt.launcher.model.ui.EclipseInstallationDialog;

public class NewEclipseInstallation extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		EclipseInstallation eclipse = new EclipseInstallation();
		EclipseInstallationDialog dialog = new EclipseInstallationDialog(HandlerUtil.getActiveShell(event), eclipse);
		if(dialog.open() == EclipseInstallationDialog.OK)
		{
			try {
				EclipseInstallationManager.getInstance().create(eclipse);
			} catch (BackingStoreException e) {
				throw new ExecutionException("Error creating eclipse installation: " + eclipse.getName(), e);
			}
		}
		
		return null;
	}
}
