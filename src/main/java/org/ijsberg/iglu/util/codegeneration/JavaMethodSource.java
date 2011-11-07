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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;

/**
 * Contains the definition of a class method and its contents needed to create Java source code.
 */
public class JavaMethodSource extends JavaMemberSource {
	private ArrayList parameters = new ArrayList();
	private ArrayList statements = new ArrayList();

	private TreeMap exceptionTypes = new TreeMap();

	public static final String RET = "\n";


	/**
	 * @param modifiers
	 * @param returnType
	 * @param name
	 */
	public JavaMethodSource(String modifiers, String returnType, String name) {
		super(modifiers, returnType, name);
	}

	/**
	 * Generates source code.
	 *
	 * @return
	 */
	public String toString() {
		StringBuffer retval = new StringBuffer();
		retval.append(this.javaDocToString("\t"));

		retval.append('\t' + modifiers + ' ' + type + ' ' + name);
		addParameters(retval);
		addThrowsClause(retval);

		retval.append(RET);

		addBody(retval);

		return retval.toString();
	}

	private void addBody(StringBuffer retval) {
		Iterator i;
		retval.append("\t{" + RET);
		i = statements.iterator();
		while (i.hasNext()) {
			retval.append("\t\t" + i.next() + RET);
		}
		retval.append("\t}" + RET);
	}

	private void addThrowsClause(StringBuffer retval) {
		Iterator i;
		if (!exceptionTypes.isEmpty()) {
			retval.append(" throws ");
			i = exceptionTypes.values().iterator();
			while (i.hasNext()) {
				retval.append(((Class) i.next()).getName() + (i.hasNext() ? ", " : ""));
			}
		}
	}

	private void addParameters(StringBuffer retval) {
		Iterator i;
		retval.append('(');
		i = parameters.iterator();
		while (i.hasNext()) {
			retval.append(i.next() + (i.hasNext() ? ", " : ""));
		}
		retval.append(')');
	}

	/**
	 * @param statement
	 */
	public void addStatement(String statement) {
		statements.add(statement);
	}

	/**
	 * @param exceptionType
	 */
	public void addExceptionType(Class exceptionType) {
		exceptionTypes.put(exceptionType, exceptionType);
	}

	/**
	 * @return
	 */
	public String getMethodName() {
		return name;
	}
}
