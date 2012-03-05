package com.weiglewilczek.xwt.launcher.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

import com.weiglewilczek.xwt.launcher.managers.GroupManager;
import com.weiglewilczek.xwt.launcher.model.Group;
import com.weiglewilczek.xwt.launcher.model.ObservableGroup;
import com.weiglewilczek.xwt.launcher.model.ui.GroupDialog;

public class EditGroup extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		Object currentSelection = HandlerUtil.getCurrentSelection(event);
		if (currentSelection != null
				&& currentSelection instanceof IStructuredSelection) {
			IStructuredSelection selection = (IStructuredSelection) currentSelection;

			if (selection.getFirstElement() != null
					&& selection.getFirstElement() instanceof ObservableGroup) {
				Group group = ((ObservableGroup) selection.getFirstElement())
						.getGroup();

				GroupDialog groupDialog = new GroupDialog(
						HandlerUtil.getActiveShell(event), group);
				if (groupDialog.open() == GroupDialog.OK) {
					try {
						GroupManager.getInstance().update(group);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}

		return null;
	}

}
