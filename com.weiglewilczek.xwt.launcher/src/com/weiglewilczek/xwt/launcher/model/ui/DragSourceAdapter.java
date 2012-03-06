/*
 * Copyright (c) 2012 WeigleWilczek and others.
 * 
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package com.weiglewilczek.xwt.launcher.model.ui;

import java.util.Iterator;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.TextTransfer;

import com.weiglewilczek.xwt.launcher.model.LaunchConfiguration;

public class DragSourceAdapter extends org.eclipse.swt.dnd.DragSourceAdapter {
	private TreeViewer viewer;

	public DragSourceAdapter(TreeViewer viewer) {
		this.viewer = viewer;
	}

	@Override
	public void dragFinished(DragSourceEvent event) {
		if (!event.doit)
			return;
	}

	@Override
	public void dragSetData(DragSourceEvent event) {
		if (TextTransfer.getInstance().isSupportedType(event.dataType)) {
			IStructuredSelection selection = (IStructuredSelection) viewer
					.getSelection();
			StringBuffer buffer = new StringBuffer();

			for (Iterator<?> i = selection.iterator(); i.hasNext();) {
				Object selected = i.next();
				if (selected instanceof LaunchConfiguration) {
					buffer.append(((LaunchConfiguration) selected).getId()
							.toString());
					buffer.append(",");
				}
			}

			event.data = buffer.toString();
		}

	}

	@Override
	public void dragStart(DragSourceEvent event) {
		event.doit = !viewer.getSelection().isEmpty();

		if (event.doit) {
			IStructuredSelection selection = (IStructuredSelection) viewer
					.getSelection();
			for (Iterator<?> i = selection.iterator(); i.hasNext();) {
				Object selected = i.next();
				if (!(selected instanceof LaunchConfiguration)) {
					event.doit = false;
				}
			}
		}
	}
}
