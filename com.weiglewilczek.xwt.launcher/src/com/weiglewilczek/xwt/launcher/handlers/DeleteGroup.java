package com.weiglewilczek.xwt.launcher.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

import com.weiglewilczek.xwt.launcher.managers.GroupManager;
import com.weiglewilczek.xwt.launcher.model.Group;
import com.weiglewilczek.xwt.launcher.model.ObservableGroup;

public class DeleteGroup extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IStructuredSelection selection = (IStructuredSelection)HandlerUtil.getCurrentSelection(event);
		ObservableGroup og = (ObservableGroup) selection.getFirstElement();
		Group group = og.getGroup();
		try {
			GroupManager.getInstance().delete(group);
		} catch (Exception e) {
			throw new ExecutionException("Error deleting group: " + group.getName(), e);
		}
		
		return null;
	}

}
