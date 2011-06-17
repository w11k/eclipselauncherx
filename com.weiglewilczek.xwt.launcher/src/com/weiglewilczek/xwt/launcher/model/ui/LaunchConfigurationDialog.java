package com.weiglewilczek.xwt.launcher.model.ui;

import java.awt.datatransfer.StringSelection;
import java.net.URL;

import org.eclipse.e4.xwt.XWT;
import org.eclipse.e4.xwt.databinding.IBindingContext;
import org.eclipse.e4.xwt.jface.AbstractDialog;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.weiglewilczek.xwt.launcher.handlers.LaunchConfigurationSelectionHandler;
import com.weiglewilczek.xwt.launcher.model.EclipseInstallation;
import com.weiglewilczek.xwt.launcher.model.JavaInstallation;
import com.weiglewilczek.xwt.launcher.model.LaunchConfigurationDataContext;

public class LaunchConfigurationDialog extends AbstractDialog {

	private ComboViewer eclipse;
	private ComboViewer java;

	public LaunchConfigurationDialog(Shell shell,
			LaunchConfigurationDataContext dataContext) {
		super(shell, "New Launch Configuration", dataContext);
	}

	@Override
	protected URL getContentURL() {
		return this.getClass().getResource("LaunchConfigurationDialog.xwt");
	}

	public LaunchConfigurationDataContext getDataContext() {
		return (LaunchConfigurationDataContext) dataContext;
	}

	public void performSelectionEvent(Event event) {
		DirectoryDialog dialog = new DirectoryDialog(getShell());
		String path = dialog.open();
		if (path != null && path.length() > 0) {
			Text pathText = (Text) XWT.findElementByName(event.widget, "path");
			if (pathText != null)
				pathText.setText(path);

		}

		IBindingContext context = XWT.getBindingContext(event.widget);
		context.updateModels();
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Control area = super.createDialogArea(parent);
		LaunchConfigurationSelectionHandler.addEclipseSelectionListener(area,
				getDataContext().getLaunchConfiguration());
		LaunchConfigurationSelectionHandler.addJavaSelectionListener(area,
				getDataContext().getLaunchConfiguration());

		if (area instanceof Composite
				&& ((Composite) area).getChildren().length > 0) {
			Control contextControl = ((Composite) area).getChildren()[0];

			eclipse = (ComboViewer) XWT.findElementByName(contextControl,
					"eclipse");
			java = (ComboViewer) XWT.findElementByName(contextControl, "java");
			
			if (eclipse != null && getDataContext().getEclipse() != null) {
				eclipse.setSelection(new StructuredSelection(getDataContext().getEclipse()));
			}
			
			if (java != null && getDataContext().getJava() != null) {
				java.setSelection(new StructuredSelection(getDataContext().getJava()));
			}
		}

		return area;
	}

	public void performEclipseSelectionEvent(Event event) {
		ComboViewer eclipseViewer = (ComboViewer) XWT.findElementByName(
				event.widget, "eclipse");
		IStructuredSelection selection = (IStructuredSelection) eclipseViewer
				.getSelection();
		Object selected = selection.getFirstElement();

		getDataContext().setEclipse((EclipseInstallation) selected);
	}

	public void performJavaSelectionEvent(Event event) {
		ComboViewer eclipseViewer = (ComboViewer) XWT.findElementByName(
				event.widget, "java");
		IStructuredSelection selection = (IStructuredSelection) eclipseViewer
				.getSelection();
		Object selected = selection.getFirstElement();

		getDataContext().setJava((JavaInstallation) selected);
	}

}
