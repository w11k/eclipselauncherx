package com.weiglewilczek.xwt.launcher.handlers;

import java.io.File;
import java.io.IOException;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.PlatformUI;

import com.weiglewilczek.xwt.launcher.managers.ImportExportManager;

public class Export extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent arg0) throws ExecutionException {
		File file = null;
		FileDialog dialog = new FileDialog(PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getShell(), SWT.SAVE);
		dialog.setFileName("EclipseLauncherX.properties");
		dialog.setFilterExtensions(new String[] { "prefs", "properties", "*" });
		String fileName = dialog.open();

		if (fileName != null) {
			file = new File(fileName);
			if (!file.exists()) {

				try {

					file.createNewFile();

					if (file.exists()) {
						ImportExportManager.getInstance()
								.exportProperties(file);
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		return null;
	}

}
