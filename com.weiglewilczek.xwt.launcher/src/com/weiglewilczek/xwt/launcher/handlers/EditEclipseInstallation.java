package com.weiglewilczek.xwt.launcher.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;
import org.osgi.service.prefs.BackingStoreException;

import com.weiglewilczek.xwt.launcher.managers.EclipseInstallationManager;
import com.weiglewilczek.xwt.launcher.model.EclipseInstallation;
import com.weiglewilczek.xwt.launcher.model.ui.EclipseInstallationDialog;

public class EditEclipseInstallation extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IStructuredSelection selection = (IStructuredSelection)HandlerUtil.getCurrentSelection(event);
		EclipseInstallation eclipse = (EclipseInstallation)selection.getFirstElement();
		
		EclipseInstallationDialog dialog = new EclipseInstallationDialog(HandlerUtil.getActiveShell(event), eclipse);
		if(dialog.open() == EclipseInstallationDialog.OK)
		{
			try {
				EclipseInstallationManager.getInstance().update(eclipse);
			} catch (BackingStoreException e) {
				throw new ExecutionException("Error updating eclipse installation: " + eclipse.getName(), e);
			}
		}
		
		return null;
	}
}
