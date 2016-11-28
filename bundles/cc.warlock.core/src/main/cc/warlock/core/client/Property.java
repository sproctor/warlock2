/**
 * Warlock, the open-source cross-platform game client
 *  
 * Copyright 2008, Warlock LLC, and individual contributors as indicated
 * by the @authors tag. 
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package cc.warlock.core.client;

import java.util.ArrayList;
import java.util.Collection;

public class Property<T> implements IProperty<T> {

	private T data;
	private ArrayList<IPropertyListener<T>> listeners = new ArrayList<IPropertyListener<T>>();
	
	public Property() {}
	
	public Property(T value) {
		this.data = value;
	}
	
	public void set(T data) {
		this.data = data;
		for(IPropertyListener<T> listener : listeners) {
			listener.propertyChanged(data);
		}
	}
	
	public T get() {
		return data;
	}
	
	public void addListener(IPropertyListener<T> listener) {
		if(listener == null)
			return;
		listeners.add(listener);
	}
	
	public boolean removeListener(IPropertyListener<T> listener) {
		return listeners.remove(listener);
	}
	
	protected Collection<IPropertyListener<T>> getListeners() {
		return listeners;
	}
}
