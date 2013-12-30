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

import java.io.File;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

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
	private String[] includeFilesContainingText = new String[0];
	private String[] excludeFilesContainingText = new String[0];

	/**
	 * Note: Unix-style file separators are assumed in expressions.
	 *
	 * @param includeFilesWithNameMask
	 */
	public FileFilterRuleSet(String includeFilesWithNameMask) {
		super();
		this.includeFilesWithNameMask = includeFilesWithNameMask;
	}

	
	/**
	 * Note: Unix-style file separators are assumed in expressions.
	 *
	 * @param includeFilesWithNameMask
	 * @param excludeFilesWithNameMask
	 */
	public FileFilterRuleSet(String includeFilesWithNameMask, String excludeFilesWithNameMask) {
		super();
		this.includeFilesWithNameMask = includeFilesWithNameMask;
		this.excludeFilesWithNameMask = excludeFilesWithNameMask;
	}

	
	public FileFilterRuleSet(String includeFilesWithNameMask, String excludeFilesWithNameMask,
			String[] includeFilesContainingLineMask, String[] excludeFilesContainingLineMask) {
		super();
		this.includeFilesWithNameMask = includeFilesWithNameMask;
		this.excludeFilesWithNameMask = excludeFilesWithNameMask;
		this.includeFilesContainingText = includeFilesContainingLineMask;
		this.excludeFilesContainingText = excludeFilesContainingLineMask;
	}
/*
	//TODO shortcut
	public boolean matchesAll() {
		return excludesNone() && "*".equals(this.includeFilesWithNameMask);
	}
*/
/*	private boolean matches(String mask, String defaultMask) {
		return (this.excludeFilesWithNameMask == null || "".equals(this.excludeFilesWithNameMask)) &&
				(this.excludeFilesContainingText == null || "".equals(this.excludeFilesContainingText));
	}
*/
	/**
	 * Checks if file matches rules.
	 * @param file
	 * @return true if a file exists, matches include and exclude rules and is successfully parsed in case of inspection of contents
	 * @throws IOException
	 */
	public boolean fileMatchesRules(File file) {

        try {
            if(file.exists()) {
                if(includeFilesContainingText.length == 0 && excludeFilesContainingText.length == 0) {
                    return fileMatchesRules(
                            FileSupport.convertToUnixStylePath(file.getPath()));
                } else {
                    return fileMatchesRules(
                            FileSupport.convertToUnixStylePath(file.getPath()),
                            FileSupport.getTextFileFromFS(file));
                }
            }
        } catch (IOException ioe) {
            //at the moment file does not match rules
        }
		return false;
	}

    public boolean fileMatchesRules(ZipEntry entry, ZipFile zipFile) {

        try {
                if(includeFilesContainingText.length == 0 && excludeFilesContainingText.length == 0) {
                    return fileMatchesRules(
                            FileSupport.convertToUnixStylePath(entry.getName()));
                } else {
                    return fileMatchesRules(
                            FileSupport.convertToUnixStylePath(entry.getName()),
                            FileSupport.getTextFileFromJar(entry.getName(), zipFile));
                }
        } catch (IOException ioe) {
            //at the moment file does not match rules
        }
        return false;
    }

    public boolean fileMatchesRules(String fileName) {

            return
                    includeBecauseOfName(fileName) &&
                            !excludeBecauseOfName(fileName);
    }

    public boolean fileMatchesRules(String fileName, String fileContents) {

        try {
            return
                    fileMatchesRules(fileName) &&
                        includeBecauseOfContainedTextLine(fileContents) &&
                        !excludeBecauseOfContainedTextLine(fileContents);

        } catch (IOException ioe) {
            //at the moment file does not match rules
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

	private boolean includeBecauseOfContainedTextLine(String fileContents) throws IOException {
		boolean retval = includeFilesContainingText == null ||
                includeFilesContainingText.length == 0 ||
                occurenceFound(fileContents, includeFilesContainingText);
		return retval;
	}
	
	private boolean excludeBecauseOfContainedTextLine(String fileContents) throws IOException {
		boolean retval = excludeFilesContainingText != null &&
                excludeFilesContainingText.length > 0 &&
                occurenceFound(fileContents, excludeFilesContainingText);
		return retval;
	}


    private static boolean occurenceFound(String fileContents, String[] expressions) throws IOException {

        for(String expression : expressions) {
            if(fileContents.contains(expression)) {
                return true;
            }
        }
        return false;

/*
        if(new String(fileContents).contains(expression)) {
            System.out.println(new String(fileContents));

        }
        System.out.println(PatternMatchingSupport.valueMatchesWildcardExpression(
                new String(fileContents), expression));

        return PatternMatchingSupport.valueMatchesWildcardExpression(
                new String(fileContents), expression);   */
    }

/*	private static boolean occurenceFound(File file, String expression) throws IOException {
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
*/

	public void setIncludeFilesWithNameMask(String includeFilesWithNameMask) {

		this.includeFilesWithNameMask = includeFilesWithNameMask;
	}


	public void setExcludeFilesWithNameMask(String excludeFilesWithNameMask) {
		this.excludeFilesWithNameMask = excludeFilesWithNameMask;
	}


	public void setIncludeFilesContainingText(
            String ... includeFilesContainingText) {
		this.includeFilesContainingText = includeFilesContainingText;
	}


	public void setExcludeFilesContainingText(
            String ... excludeFilesContainingText) {
		this.excludeFilesContainingText = excludeFilesContainingText;
	}

	@Override
	public FileFilterRuleSet clone() {
		return new FileFilterRuleSet(includeFilesWithNameMask, excludeFilesWithNameMask, includeFilesContainingText, excludeFilesContainingText);
	}
	

    public String toString() {
        return "file filter:\n" +
                "include names: " + includeFilesWithNameMask + "\n" +
                "include lines containing: " + includeFilesContainingText + "\n" +
                "exclude names: " + excludeFilesContainingText + "\n" +
                "exclude lines containing: " + excludeFilesContainingText + "\n";
    }
	

}
