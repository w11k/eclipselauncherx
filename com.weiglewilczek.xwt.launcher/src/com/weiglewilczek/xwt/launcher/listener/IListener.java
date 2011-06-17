package com.weiglewilczek.xwt.launcher.listener;

import com.weiglewilczek.xwt.launcher.model.ModelElement;

public interface IListener {
	
	void handle(ListenerType type, ModelElement object);
}
