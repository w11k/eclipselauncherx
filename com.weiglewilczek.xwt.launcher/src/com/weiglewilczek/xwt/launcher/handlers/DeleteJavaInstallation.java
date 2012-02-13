package com.weiglewilczek.xwt.launcher.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

import com.weiglewilczek.xwt.launcher.managers.JavaInstallationManager;
import com.weiglewilczek.xwt.launcher.model.JavaInstallation;

public class DeleteJavaInstallation extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IStructuredSelection selection = (IStructuredSelection)HandlerUtil.getCurrentSelection(event);
		JavaInstallation ji = (JavaInstallation) selection.getFirstElement();
		try {
			JavaInstallationManager.getInstance().delete(ji);
		} catch (Exception e) {
			throw new ExecutionException("Error deleting java installation: " + ji.getName(), e);
		}
		
		return null;
	}

}
