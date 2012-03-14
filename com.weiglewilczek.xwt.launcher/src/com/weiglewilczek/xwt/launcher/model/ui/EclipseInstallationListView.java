/*
 * Copyright (c) 2012 WeigleWilczek and others.
 * 
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package com.weiglewilczek.xwt.launcher.model.ui;

import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.e4.xwt.XWT;
import org.eclipse.e4.xwt.pde.ui.views.XWTViewPart;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.handlers.IHandlerService;

import com.weiglewilczek.xwt.launcher.Activator;
import com.weiglewilczek.xwt.launcher.Messages;
import com.weiglewilczek.xwt.launcher.listener.IListener;
import com.weiglewilczek.xwt.launcher.listener.ListenerType;
import com.weiglewilczek.xwt.launcher.managers.EclipseInstallationManager;
import com.weiglewilczek.xwt.launcher.model.EclipseInstallation;
import com.weiglewilczek.xwt.launcher.model.EclipseInstallationsListDataContext;
import com.weiglewilczek.xwt.launcher.model.ModelElement;

public class EclipseInstallationListView extends XWTViewPart implements
		IListener {

	private EclipseInstallationsListDataContext context;

	private TableViewer eclipseInstallations;

	public EclipseInstallationListView() {
		EclipseInstallationManager.getInstance().addListener(this);

		WritableList eclipseInstallations = new WritableList();
		eclipseInstallations.addAll(EclipseInstallationManager.getInstance()
				.enumerateAll());
		context = new EclipseInstallationsListDataContext(eclipseInstallations);
		setDataContext(context);
	}

	@Override
	protected void updateContent() {
		if (container != null) {
			setContent(this.getClass().getResource(
					"EclipseInstallationListView.xwt"));

			if (container.getChildren().length > 0) {

				Control xwtContext = container.getChildren()[0];
				eclipseInstallations = (TableViewer) XWT.findElementByName(
						xwtContext, "eclipseInstallationsList");
				getSite().setSelectionProvider(eclipseInstallations);

				eclipseInstallations
						.addDoubleClickListener(new IDoubleClickListener() {

							@Override
							public void doubleClick(DoubleClickEvent event) {
								IHandlerService hs = (IHandlerService) getSite()
										.getService(IHandlerService.class);
								try {
									hs.executeCommand(
											"com.weiglewilczek.xwt.launcher.commands.Edit",
											null);
								} catch (Exception e) {
									Activator
											.logError(
													Messages.EclipseInstallationListView_Error,
													e);
								}
							}
						});

			}
		}
	}

	@Override
	public void setFocus() {
		if (eclipseInstallations != null) {
			eclipseInstallations.getTable().setFocus();
		}
	}

	@Override
	public void handle(ListenerType type, ModelElement<?> object) {
		if (object instanceof EclipseInstallation) {
			switch (type) {
			case CREATE:
				context.getEclipseInstallations().add(object);
				eclipseInstallations.refresh();
				break;
			case UPDATE:
				eclipseInstallations.refresh(true);
				break;
			case DELETE:
				context.getEclipseInstallations().remove(object);
				eclipseInstallations.refresh();
				break;

			default:
				break;
			}
		}
	}
}
