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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Test;

public class FileFilterRuleSetTest extends DirStructureDependentTest {
	
	
	@Test
	public void testFileMatchesRulesForNameIncludes() throws Exception {
		
		File file = new File(dirStructRoot + "WWW/cornerstone/architecture.gif");
		
		FileFilterRuleSet ruleSet = new FileFilterRuleSet("*.gif");
		assertTrue(ruleSet.fileMatchesRules(file));

		ruleSet = new FileFilterRuleSet("*/cornerstone/*");
		assertTrue(ruleSet.fileMatchesRules(file));
		
		ruleSet = new FileFilterRuleSet("*/cornerstone/*.jpg");
		assertFalse(ruleSet.fileMatchesRules(file));
	}

	@Test
	public void testFileMatchesRulesForNameExcludes() throws Exception {
		
		File file = new File(dirStructRoot + "WWW/cornerstone/architecture.gif");

		assertTrue(file.exists());
		
		FileFilterRuleSet ruleSet = new FileFilterRuleSet("*.gif", "*/_d0/*");
		assertTrue(ruleSet.fileMatchesRules(file));

		ruleSet = new FileFilterRuleSet("*/cornerstone/*", "*.jpg");
		assertTrue(ruleSet.fileMatchesRules(file));

		ruleSet = new FileFilterRuleSet("*/cornerstone/*", "*.gif");
		assertFalse(ruleSet.fileMatchesRules(file));
	}

	@Test
	public void testFileMatchesRulesForContentsIncludes() throws Exception {
		
		File file = new File(dirStructRoot + "WWW/cornerstone/index.html");
		assertTrue(file.exists());
		
		FileFilterRuleSet ruleSet = new FileFilterRuleSet("*.html", "*/_d0/*", new String[]{""}, new String[]{""});
		assertTrue(ruleSet.fileMatchesRules(file));

		ruleSet = new FileFilterRuleSet("*.html", "*/_d0/*", new String[]{"This file is part of Iglu"}, new String[]{""});
		assertTrue(ruleSet.fileMatchesRules(file));
	
/*		ruleSet = new FileFilterRuleSet("*.html", "* /_d0/ *", "  ~ This file is part of Iglu.", "");
		assertTrue(ruleSet.fileMatchesRules(file));
		
		ruleSet = new FileFilterRuleSet("*.html", "* /_d0/ *", "  ~ This f?le is p?rt of Iglu.", "");
		assertTrue(ruleSet.fileMatchesRules(file));

		ruleSet = new FileFilterRuleSet("*.html", "* /_d0/ *", "  + This f?le is p?rt of Iglu.", "");
		assertFalse(ruleSet.fileMatchesRules(file));       */
	}

	@Test
	public void testFileMatchesRulesForContentsExcludes() throws Exception {
		
		File file = new File(dirStructRoot + "WWW/cornerstone/index.html");
		assertTrue(file.exists());
		
		/*FileFilterRuleSet ruleSet = new FileFilterRuleSet("*.html", "* /_d0/*", "*", "");
		assertTrue(ruleSet.fileMatchesRules(file));

		ruleSet = new FileFilterRuleSet("*.html", "* /_d0/ *", "*", "*This file is part of Iglu*");
		assertFalse(ruleSet.fileMatchesRules(file));

		ruleSet = new FileFilterRuleSet("*.html", "* /_d0/ *", "*FITNESS FOR A PARTICULAR PURPOSE*", "  ~ This file is part of Iglu.");
		assertFalse(ruleSet.fileMatchesRules(file));

		ruleSet = new FileFilterRuleSet("*.html", "* /_d0/ *", "*FITNESS FOR A PARTICULAR PURPOSE*", "  + This file is part of Iglu.");
		assertTrue(ruleSet.fileMatchesRules(file));     */
	}
	
	
}
