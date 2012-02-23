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
 * @see PatternMatchingSupport#valueMatchesWildcardExpression(String, String)
 */
public class FileFilterRuleSet {
	
	private String includeFilesWithNameMask;
	private String excludeFilesWithNameMask = "";
	private String includeFilesContainingLineMask = "*";
	private String excludeFilesContainingLineMask = "";

	/**
	 * 
	 * @param includeFileWithNameMask
	 */
	public FileFilterRuleSet(String includeFileWithNameMask) {
		super();
		this.includeFilesWithNameMask = includeFileWithNameMask;
	}

	
	/**
	 * 
	 * @param includeFileWithNameMask
	 * @param excludeFileWithNameMask
	 */
	public FileFilterRuleSet(String includeFileWithNameMask, String excludeFileWithNameMask) {
		super();
		this.includeFilesWithNameMask = includeFileWithNameMask;
		this.excludeFilesWithNameMask = excludeFileWithNameMask;
	}

	
	/**
	 * 
	 * @param includeFileWithNameMask
	 * @param excludeFileWithNameMask
	 * @param includeFileContainingLineMask
	 * @param excludeFileContainingLineMask
	 */
	public FileFilterRuleSet(String includeFileWithNameMask, String excludeFileWithNameMask,
			String includeFileContainingLineMask, String excludeFileContainingLineMask) {
		super();
		this.includeFilesWithNameMask = includeFileWithNameMask;
		this.excludeFilesWithNameMask = excludeFileWithNameMask;
		this.includeFilesContainingLineMask = includeFileContainingLineMask;
		this.excludeFilesContainingLineMask = excludeFileContainingLineMask;
	}
	

	/**
	 * 
	 * @param file
	 * @return true if a file exists, matches include and exclude rules and is successfully parsed in case of inspection of contents
	 * @throws IOException
	 */
	public boolean fileMatchesRules(File file) {
		String fileName = file.getPath();
		try {
			return file.exists() &&
					includeBecauseOfName(fileName) &&
					!excludeBecauseOfName(fileName) &&
					includeBecauseOfContainedTextLine(file) &&
					!excludeBecauseOfContainedTextLine(file);
		} catch (IOException ioe) {
			return false;
		}
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


	
	
	
	

}
