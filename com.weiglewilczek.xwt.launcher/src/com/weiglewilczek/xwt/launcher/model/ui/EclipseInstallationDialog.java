package com.weiglewilczek.xwt.launcher.model.ui;

import java.net.URL;

import org.eclipse.e4.xwt.XWT;
import org.eclipse.e4.xwt.databinding.IBindingContext;
import org.eclipse.e4.xwt.jface.AbstractDialog;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.weiglewilczek.xwt.launcher.model.EclipseInstallation;

public class EclipseInstallationDialog extends AbstractDialog {

	public EclipseInstallationDialog(Shell shell, EclipseInstallation eclipse) {
		super(shell, "New Eclipse Installation", eclipse);
	}

	@Override
	protected URL getContentURL() {
		return this.getClass().getResource("EclipseInstallationDialog.xwt");
	}

	public EclipseInstallation getDataContext() {
		return (EclipseInstallation) dataContext;
	}

	public void performSelectionEvent(Event event) {
		FileDialog dialog = new FileDialog(getShell());
		String path = dialog.open();
		if (path != null && path.length() > 0) {
			Text pathText = (Text) XWT.findElementByName(event.widget,
					"path");
			if (pathText != null)
				pathText.setText(path);
		}
		
		IBindingContext context = XWT.getBindingContext(event.widget);
		context.updateModels();
	}
}
