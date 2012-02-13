package com.weiglewilczek.xwt.launcher.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

public class OpenGroupsView extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		try {
			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView("com.weiglewilczek.xwt.launcher.views.Groups");
		} catch (PartInitException e) {
			throw new ExecutionException("Error opening groups view", e);
		}
		return null;
	}

}
