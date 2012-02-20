package com.weiglewilczek.xwt.launcher.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;
import java.util.prefs.BackingStoreException;

import com.weiglewilczek.xwt.launcher.managers.JavaInstallationManager;
import com.weiglewilczek.xwt.launcher.model.JavaInstallation;
import com.weiglewilczek.xwt.launcher.model.ui.JavaInstallationDialog;

public class EditJavaInstallation extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IStructuredSelection selection = (IStructuredSelection) HandlerUtil.getCurrentSelection(event);
		JavaInstallation java = (JavaInstallation)selection.getFirstElement();
		
		JavaInstallationDialog dialog = new JavaInstallationDialog(HandlerUtil.getActiveShell(event), java);
		if(dialog.open() == JavaInstallationDialog.OK)
		{
			try {
				JavaInstallationManager.getInstance().update(java);
			} catch (BackingStoreException e) {
				throw new ExecutionException("Error updating java installation: " + java.getName(), e);
			}
		}
		
		return null;
	}
}
