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
		
		FileFilterRuleSet ruleSet = new FileFilterRuleSet("*.html", "*/_d0/*", "*", "");
		assertTrue(ruleSet.fileMatchesRules(file));

		ruleSet = new FileFilterRuleSet("*.html", "*/_d0/*", "*This file is part of Iglu*", "");
		assertTrue(ruleSet.fileMatchesRules(file));
	
		ruleSet = new FileFilterRuleSet("*.html", "*/_d0/*", "  ~ This file is part of Iglu.", "");
		assertTrue(ruleSet.fileMatchesRules(file));
		
		ruleSet = new FileFilterRuleSet("*.html", "*/_d0/*", "  ~ This f?le is p?rt of Iglu.", "");
		assertTrue(ruleSet.fileMatchesRules(file));

		ruleSet = new FileFilterRuleSet("*.html", "*/_d0/*", "  + This f?le is p?rt of Iglu.", "");
		assertFalse(ruleSet.fileMatchesRules(file));
	}

	@Test
	public void testFileMatchesRulesForContentsExcludes() throws Exception {
		
		File file = new File(dirStructRoot + "WWW/cornerstone/index.html");
		assertTrue(file.exists());
		
		FileFilterRuleSet ruleSet = new FileFilterRuleSet("*.html", "*/_d0/*", "*", "");
		assertTrue(ruleSet.fileMatchesRules(file));

		ruleSet = new FileFilterRuleSet("*.html", "*/_d0/*", "*", "*This file is part of Iglu*");
		assertFalse(ruleSet.fileMatchesRules(file));

		ruleSet = new FileFilterRuleSet("*.html", "*/_d0/*", "*FITNESS FOR A PARTICULAR PURPOSE*", "  ~ This file is part of Iglu.");
		assertFalse(ruleSet.fileMatchesRules(file));

		ruleSet = new FileFilterRuleSet("*.html", "*/_d0/*", "*FITNESS FOR A PARTICULAR PURPOSE*", "  + This file is part of Iglu.");
		assertTrue(ruleSet.fileMatchesRules(file));
	}
	
	
}
