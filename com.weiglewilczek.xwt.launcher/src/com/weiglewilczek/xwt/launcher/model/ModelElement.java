package com.weiglewilczek.xwt.launcher.model;

import com.weiglewilczek.xwt.launcher.managers.BaseFields;

public abstract class ModelElement<T extends BaseFields<T>> {

	private Long id;

	public abstract Object getProperty(T field);

	public abstract void setProperty(T field, Object value);

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		if (getId() != null)
			return getClass().hashCode() + getId().hashCode();
		else
			return getClass().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj instanceof ModelElement) {
			if (((ModelElement) obj).getId() != null
					&& ((ModelElement) obj).getId().equals(getId())) {
				return true;
			}
		}

		return false;
	}
}
