package com.weiglewilczek.xwt.launcher.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

import com.weiglewilczek.xwt.launcher.managers.ConstraintViolationException;
import com.weiglewilczek.xwt.launcher.managers.EclipseInstallationManager;
import com.weiglewilczek.xwt.launcher.model.EclipseInstallation;

public class DeleteEclipseInstallation extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IStructuredSelection selection = (IStructuredSelection) HandlerUtil
				.getCurrentSelection(event);
		EclipseInstallation ei = (EclipseInstallation) selection
				.getFirstElement();
		try {
			EclipseInstallationManager.getInstance().delete(ei);
		} catch (ConstraintViolationException e) {
			MessageDialog.openError(PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow().getShell(),
					"Error deleting Eclipse installation", e.getMessage());
		} catch (Exception e) {
			throw new ExecutionException(
					"Error deleting eclipse installation: " + ei.getName(), e);
		}

		return null;
	}

}
