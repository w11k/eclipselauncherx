/*
 * Copyright (c) 2012 WeigleWilczek and others.
 * 
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package com.weiglewilczek.xwt.launcher.listener;

import com.weiglewilczek.xwt.launcher.model.ModelElement;

public interface IListener {

	void handle(ListenerType type, ModelElement<?> object);
}
