package com.github.hatimiti.flutist.common.message;

import static com.github.hatimiti.flutist.common.util._Obj.*;
import static com.github.hatimiti.flutist.common.util._Str.*;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.github.hatimiti.flutist.common.util._Obj;

public class Owner {

	private String property;
	private String name;
	private Integer index;

	private Owner(String property, String name, Integer index) {
		this.property = property;
		this.name = name;
		this.index = index;
	}

	public static Owner empty() {
		return of("");
	}

	public static Owner of(String property) {
		return of(property, "");
	}

	public static Owner of(String property, String name) {
		return of(property, name, (Integer) null);
	}

	public static Owner of(String property, String name, Integer index) {
		return new Owner(toEmpty(property), toEmpty(name), index);
	}

	@Override
	public String toString() {
		StringBuilder pn = new StringBuilder();
		if (_Obj.isNotEmpty(name)) {
			pn.append(name);
		}
		if (isNotEmpty(index)) {
			pn.append("[" + index + "]");
		}
		if ((isNotEmpty(name) || isNotEmpty(index))
				&& isNotEmpty(property)) {
			pn.append(".");
		}
		if (isNotEmpty(property)) {
			pn.append(this.property);
		}
		return pn.toString();
	}

	public String getProperty() {
		return property;
	}

	public String getName() {
		return name;
	}

	public Integer getIndex() {
		return index;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
			.append(property)
			.append(name)
			.append(index)
			.toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof Owner)) {
			return false;
		}
		Owner target = (Owner) obj;
		return new EqualsBuilder()
			.append(this.property, target.property)
			.append(this.name, target.name)
			.append(this.index, target.index)
			.isEquals();
	}

}
