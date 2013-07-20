/*
 * Copyright 2011-2013 Jeroen Meetsma - IJsberg
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

package org.ijsberg.iglu.util.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.ijsberg.iglu.util.formatting.PatternMatchingSupport;

/**
 * Contains up to 4 rules that a file may match.
 *
 * Masks are wildcard expressions as defined in Iglu's pattern matching support.
 *
 * Note: Unix-style file separators are assumed in expressions.
 *
 * @see PatternMatchingSupport#valueMatchesWildcardExpression(String, String)
 */
public class FileFilterRuleSet implements Cloneable {
	
	private String includeFilesWithNameMask;
	private String excludeFilesWithNameMask = "";
	private String includeFilesContainingLineMask = "*";
	private String excludeFilesContainingLineMask = "";

	/**
	 * Note: Unix-style file separators are assumed in expressions.
	 *
	 * @param includeFileWithNameMask
	 */
	public FileFilterRuleSet(String includeFilesWithNameMask) {
		super();
		this.includeFilesWithNameMask = includeFilesWithNameMask;
	}

	
	/**
	 * Note: Unix-style file separators are assumed in expressions.
	 *
	 * @param includeFileWithNameMask
	 * @param excludeFileWithNameMask
	 */
	public FileFilterRuleSet(String includeFilesWithNameMask, String excludeFilesWithNameMask) {
		super();
		this.includeFilesWithNameMask = includeFilesWithNameMask;
		this.excludeFilesWithNameMask = excludeFilesWithNameMask;
	}

	
	/**
	 * Note: Unix-style file separators are assumed in expressions.
	 *
	 * @param includeFileWithNameMask
	 * @param excludeFileWithNameMask
	 * @param includeFileContainingLineMask
	 * @param excludeFileContainingLineMask
	 */
	public FileFilterRuleSet(String includeFilesWithNameMask, String excludeFilesWithNameMask,
			String includeFilesContainingLineMask, String excludeFilesContainingLineMask) {
		super();
		this.includeFilesWithNameMask = includeFilesWithNameMask;
		this.excludeFilesWithNameMask = excludeFilesWithNameMask;
		this.includeFilesContainingLineMask = includeFilesContainingLineMask;
		this.excludeFilesContainingLineMask = excludeFilesContainingLineMask;
	}
/*
	//TODO shortcut
	public boolean matchesAll() {
		return excludesNone() && "*".equals(this.includeFilesWithNameMask);
	}
*/
/*	private boolean matches(String mask, String defaultMask) {
		return (this.excludeFilesWithNameMask == null || "".equals(this.excludeFilesWithNameMask)) &&
				(this.excludeFilesContainingLineMask == null || "".equals(this.excludeFilesContainingLineMask));
	}
*/
	/**
	 * Checks if file matches rules.
	 * @param file
	 * @return true if a file exists, matches include and exclude rules and is successfully parsed in case of inspection of contents
	 * @throws IOException
	 */
	public boolean fileMatchesRules(File file) {
		
		if(file.exists()) {
			String fileName = FileSupport.convertToUnixStylePath(file.getPath());
			try {
				return
						includeBecauseOfName(fileName) &&
						!excludeBecauseOfName(fileName) &&
						includeBecauseOfContainedTextLine(file) &&
						!excludeBecauseOfContainedTextLine(file);

			} catch (IOException ioe) {
				//at the moment file does not match rules
			}
		}
		return false;
	}
	

	private boolean includeBecauseOfName(String fileName) {
		boolean retval = includeFilesWithNameMask == null || "*".equals(includeFilesWithNameMask) || 
				PatternMatchingSupport.valueMatchesWildcardExpression(fileName, includeFilesWithNameMask);
		return retval;
	}

	private boolean excludeBecauseOfName(String fileName) {
		boolean retval = excludeFilesWithNameMask != null && !"".equals(excludeFilesWithNameMask) &&
				PatternMatchingSupport.valueMatchesWildcardExpression(fileName, excludeFilesWithNameMask);

		return retval;
	}

	private boolean includeBecauseOfContainedTextLine(File file) throws IOException {
		boolean retval = includeFilesContainingLineMask == null || "*".equals(includeFilesContainingLineMask) || occurenceFound(file, includeFilesContainingLineMask);
		return retval;
	}
	
	private boolean excludeBecauseOfContainedTextLine(File file) throws IOException {
		boolean retval = excludeFilesContainingLineMask != null && !"".equals(excludeFilesContainingLineMask) && occurenceFound(file, excludeFilesContainingLineMask);
		return retval;
	}


	private static boolean occurenceFound(File file, String expression) throws IOException {
		InputStream input = new FileInputStream(file);

		BufferedReader reader = new BufferedReader(new InputStreamReader(input));

		try {
			String line;
			while ((line = reader.readLine()) != null) {
				if(PatternMatchingSupport.valueMatchesWildcardExpression(line, expression)) {
					return true;
				}
			}
		} finally {
			reader.close();
			input.close();
		}
		return false;
	}


	public void setIncludeFilesWithNameMask(String includeFilesWithNameMask) {

		this.includeFilesWithNameMask = includeFilesWithNameMask;
	}


	public void setExcludeFilesWithNameMask(String excludeFilesWithNameMask) {
		this.excludeFilesWithNameMask = excludeFilesWithNameMask;
	}


	public void setIncludeFilesContainingLineMask(
			String includeFilesContainingLineMask) {
		this.includeFilesContainingLineMask = includeFilesContainingLineMask;
	}


	public void setExcludeFilesContainingLineMask(
			String excludeFilesContainingLineMask) {
		this.excludeFilesContainingLineMask = excludeFilesContainingLineMask;
	}

	@Override
	public FileFilterRuleSet clone() {
		return new FileFilterRuleSet(includeFilesWithNameMask, excludeFilesWithNameMask, includeFilesContainingLineMask, excludeFilesContainingLineMask);
	}
	
	
	

}
