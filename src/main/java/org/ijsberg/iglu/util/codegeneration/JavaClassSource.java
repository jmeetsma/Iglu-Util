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
 * Contains the definition of a class and its contents needed to create Java source code.
 */
public class JavaClassSource extends JavaSource {
	private String modifiers;
	private String packageName;
	private String className;
	private String implementsClause;
	private String extendsClause;
	private TreeMap imports = new TreeMap();
	private ArrayList members = new ArrayList();
	private ArrayList methods = new ArrayList();

	public static final String RET = "\n";


	/**
	 * @param modifiers
	 * @param packageName
	 * @param className
	 * @param implementsClause
	 * @param extendsClause
	 */
	public JavaClassSource(String modifiers, String packageName, String className, String implementsClause, String extendsClause) {
		this.modifiers = modifiers;
		this.packageName = packageName;
		this.className = className;
		this.implementsClause = implementsClause;
		this.extendsClause = extendsClause;
	}

	/**
	 * @param className
	 */
	public void addImport(String className) {
		imports.put(className, className);
	}

	/**
	 * @param member
	 */
	public void addMember(JavaMemberSource member) {
		members.add(member);
	}

	/**
	 * @param method
	 * @return
	 */
	public JavaMethodSource addMethod(JavaMethodSource method) {
		methods.add(method);
		return method;
	}

	/**
	 * @param methodName
	 * @return
	 */
	public JavaMethodSource getMethod(String methodName) {
		Iterator i = methods.iterator();
		while (i.hasNext()) {
			JavaMethodSource method = (JavaMethodSource) i.next();
			if (methodName.equals(method.getMethodName())) {
				return method;
			}
		}
		return null;
	}

	/**
	 * @return
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * @return
	 */
	public String getPackageName() {
		return packageName;
	}

	/**
	 * @return
	 */
	public String getName() {
		return (packageName != null && !"".equals(packageName) ? packageName + '.' : "") + className;
	}

	/**
	 * @return
	 */
	public int getNrofMethods() {
		return methods.size();
	}

	/**
	 * Generates source code.
	 *
	 * @return
	 */
	public String toString() {

		StringBuffer retval = new StringBuffer();
		addPackage(retval);
		addImports(retval);
		addClassDeclaration(retval);
		addBody(retval);

		return retval.toString();
	}

	private void addBody(StringBuffer retval) {
		Iterator i;
		retval.append('{' + RET);

		i = members.iterator();
		while (i.hasNext()) {
			retval.append(i.next().toString() + RET);
		}
		i = methods.iterator();
		while (i.hasNext()) {
			retval.append(RET);
			retval.append(i.next().toString());
		}

		retval.append('}' + RET);
	}

	private void addClassDeclaration(StringBuffer retval) {
		retval.append(modifiers + " class " + className);
		if (implementsClause != null) {
			retval.append(" implements " + implementsClause);
		}
		if (extendsClause != null) {
			retval.append(" extends " + extendsClause);
		}
		retval.append(RET);
	}

	private void addImports(StringBuffer retval) {
		Iterator i;
		i = imports.values().iterator();
		while (i.hasNext()) {
			retval.append("import " + i.next() + ';' + RET);
		}
		retval.append(RET);
	}

	private void addPackage(StringBuffer retval) {
		if (packageName != null && !"".equals(packageName)) {
			retval.append("package " + packageName + ';' + RET);
			retval.append(RET);
		}
	}

}
