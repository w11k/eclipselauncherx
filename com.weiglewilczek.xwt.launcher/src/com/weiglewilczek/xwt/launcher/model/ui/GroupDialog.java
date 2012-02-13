package com.weiglewilczek.xwt.launcher.model.ui;

import java.net.URL;

import org.eclipse.e4.xwt.jface.AbstractDialog;
import org.eclipse.swt.widgets.Shell;

import com.weiglewilczek.xwt.launcher.model.Group;

public class GroupDialog extends AbstractDialog {

	public GroupDialog(Shell shell, Group group) {
		super(shell, "New Group", group);
	}

	@Override
	protected URL getContentURL() {
		return this.getClass().getResource("GroupDialog.xwt");
	}

	public Group getDataContext() {
		return (Group) dataContext;
	}
}
