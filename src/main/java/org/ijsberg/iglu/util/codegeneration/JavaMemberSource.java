/*
 * Copyright 2011 Jeroen Meetsma
 *
 *
 * This file is part of Iglu.
 *
 * Iglu is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Iglu is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Iglu.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.ijsberg.iglu.util.codegeneration;

/**
 * Contains the definition of a class member and its contents needed to create Java source code.
 */
public class JavaMemberSource extends JavaSource {
	protected String modifiers;
	protected String type;
	protected String name;
	private String defaultVal;

	/**
	 * @param modifiers
	 * @param type
	 * @param name
	 */
	public JavaMemberSource(String modifiers, String type, String name) {
		this.modifiers = modifiers;
		this.type = type;
		this.name = name;
	}

	/**
	 * @param modifiers
	 * @param type
	 * @param name
	 * @param defaultVal
	 */
	public JavaMemberSource(String modifiers, String type, String name, String defaultVal) {
		this.modifiers = modifiers;
		this.type = type;
		this.name = name;
		this.defaultVal = defaultVal;
	}

	/**
	 * Generates source code.
	 *
	 * @return
	 */
	public String toString() {
		return '\t' + modifiers + ' ' + type + ' ' + name + (defaultVal != null ? " = " + defaultVal : "") + ';';
	}
}
