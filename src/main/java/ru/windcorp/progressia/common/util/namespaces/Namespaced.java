/*******************************************************************************
 * Progressia
 * Copyright (C) 2020  Wind Corporation
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *******************************************************************************/
package ru.windcorp.progressia.common.util.namespaces;

public abstract class Namespaced {
	
	private final String id;

	public Namespaced(String id) {
		NamespacedUtil.checkId(id);
		this.id = id;
	}
	
	public final String getId() {
		return id;
	}
	
	public String getNamespace() {
		return NamespacedUtil.getNamespace(getId());
	}
	
	public String getName() {
		return NamespacedUtil.getName(getId());
	}
	
	@Override
	public String toString() {
		return getId();
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (getClass() != obj.getClass())
			return false;
		Namespaced other = (Namespaced) obj;
		if (!id.equals(other.id))
			return false;
		return true;
	}

}