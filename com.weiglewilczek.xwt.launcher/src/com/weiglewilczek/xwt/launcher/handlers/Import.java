package com.weiglewilczek.xwt.launcher.handlers;

import java.io.File;
import java.io.IOException;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.PlatformUI;

import com.weiglewilczek.xwt.launcher.managers.GroupManager;
import com.weiglewilczek.xwt.launcher.managers.ImportExportManager;
import com.weiglewilczek.xwt.launcher.model.Group;

public class Import extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent arg0) throws ExecutionException {
		File file = null;
		FileDialog dialog = new FileDialog(PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getShell(), SWT.OPEN);
		dialog.setFilterExtensions(new String[] { "prefs", "properties", "*" });
		String fileName = dialog.open();

		if (fileName != null) {
			file = new File(fileName);

			if (file.exists()) {
				try {
					ImportExportManager.getInstance().importProperties(file);

					// trigger refresh of other group
					Group other = GroupManager.getInstance().get(-1l);
					GroupManager.getInstance().update(other);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		return null;
	}
}
