package com.weiglewilczek.xwt.launcher.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

import com.weiglewilczek.xwt.launcher.managers.LaunchConfigurationManager;
import com.weiglewilczek.xwt.launcher.model.LaunchConfiguration;

public class DeleteLaunchConfiguration extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IStructuredSelection selection = (IStructuredSelection)HandlerUtil.getCurrentSelection(event);
		LaunchConfiguration lc = (LaunchConfiguration) selection.getFirstElement();
		try {
			LaunchConfigurationManager.getInstance().delete(lc);
		} catch (Exception e) {
			throw new ExecutionException("Error deleting launch configuration: " + lc.getName(), e);
		}
		
		return null;
	}

}
