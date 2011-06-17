package com.weiglewilczek.xwt.launcher.handlers;

import org.eclipse.e4.xwt.XWT;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.widgets.Control;

import com.weiglewilczek.xwt.launcher.model.EclipseInstallation;
import com.weiglewilczek.xwt.launcher.model.JavaInstallation;
import com.weiglewilczek.xwt.launcher.model.LaunchConfiguration;

public class LaunchConfigurationSelectionHandler {
	public static void addEclipseSelectionListener(Control referenceWidget,
			final LaunchConfiguration launchConfiguration) {
		final ComboViewer eclipseViewer = (ComboViewer) XWT.findElementByName(
				referenceWidget, "eclipse");
		eclipseViewer
				.addSelectionChangedListener(new ISelectionChangedListener() {

					@Override
					public void selectionChanged(SelectionChangedEvent event) {
						IStructuredSelection selection = (IStructuredSelection) eclipseViewer
								.getSelection();
						Object selected = selection.getFirstElement();

						launchConfiguration
								.setEclipse((EclipseInstallation) selected);
					}
				});
	}

	public static void addJavaSelectionListener(Control referenceWidget,
			final LaunchConfiguration launchConfiguration) {
		final ComboViewer javaViewer = (ComboViewer) XWT.findElementByName(
				referenceWidget, "java");
		javaViewer
		.addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection selection = (IStructuredSelection) javaViewer
						.getSelection();
				Object selected = selection.getFirstElement();

				launchConfiguration
						.setJava((JavaInstallation) selected);
			}
		});
	}
}
