package org.eintr.springframework.beans;

import java.util.ArrayList;
import java.util.List;

public class PropertyValues {
	private final List<PropertyValue> propertyValueList = new ArrayList<>();

	public void addPropertyValue(PropertyValue pv) {
		this.propertyValueList.add(pv);
	}


	public void add(String name, Object value) {
		this.propertyValueList.add(new PropertyValue(name, value));
	}

	public PropertyValue[] getPropertyValues() {
		return this.propertyValueList.toArray(new PropertyValue[0]);
	}

	public PropertyValue getPropertyValue(String propertyName) {
		for (PropertyValue pv : this.propertyValueList) {
			if (pv.getName().equals(propertyName)) {
				return pv;
			}
		}
		return null;
	}

	public boolean isEmpty() {
		return propertyValueList.isEmpty();
	}
}
