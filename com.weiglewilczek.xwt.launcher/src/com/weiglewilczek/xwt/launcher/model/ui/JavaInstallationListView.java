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
import com.weiglewilczek.xwt.launcher.listener.IListener;
import com.weiglewilczek.xwt.launcher.listener.ListenerType;
import com.weiglewilczek.xwt.launcher.managers.JavaInstallationManager;
import com.weiglewilczek.xwt.launcher.model.JavaInstallation;
import com.weiglewilczek.xwt.launcher.model.JavaInstallationsListDataContext;
import com.weiglewilczek.xwt.launcher.model.ModelElement;

public class JavaInstallationListView extends XWTViewPart implements IListener {

	private JavaInstallationsListDataContext context;

	private TableViewer javaInstallations;

	public JavaInstallationListView() {
		JavaInstallationManager.getInstance().addListener(this);

		WritableList javaInstallations = new WritableList();
		javaInstallations.addAll(JavaInstallationManager.getInstance()
				.enumerateAll());
		context = new JavaInstallationsListDataContext(javaInstallations);
		setDataContext(context);
	}

	@Override
	protected void updateContent() {
		if (container != null) {
			setContent(this.getClass().getResource(
					"JavaInstallationListView.xwt"));

			if (container.getChildren().length > 0) {

				Control xwtContext = container.getChildren()[0];
				javaInstallations = (TableViewer) XWT.findElementByName(
						xwtContext, "javaInstallationsList");
				getSite().setSelectionProvider(javaInstallations);

				javaInstallations
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
													"Error executing command: Edit Java Installation",
													e);
								}
							}
						});

			}
		}
	}

	@Override
	public void setFocus() {
		if (javaInstallations != null) {
			javaInstallations.getTable().setFocus();
		}
	}

	@Override
	public void handle(ListenerType type, ModelElement<?> object) {
		if (object instanceof JavaInstallation) {
			switch (type) {
			case CREATE:
				context.getJavaInstallations().add(object);
				javaInstallations.refresh();
				break;
			case UPDATE:
				javaInstallations.refresh(true);
				break;
			case DELETE:
				context.getJavaInstallations().remove(object);
				javaInstallations.refresh();
				break;

			default:
				break;
			}
		}
	}
}
