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

package org.ijsberg.iglu.util.misc;

import org.ijsberg.iglu.util.io.FileSupport;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.Set;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;


public class StringSupportTest {

	@Test
	public void testSplit() throws Exception {
		List result = StringSupport.split("org.ijsberg.iglu.util",".","");
		assertEquals("org", result.get(0));
		assertEquals("ijsberg", result.get(1));
		assertEquals("iglu", result.get(2));
		assertEquals("util", result.get(3));

		String line = "Harry went to the \"market square\" and bought some fish";
		result = StringSupport.split(line, " ", "\"");
		assertEquals("Harry", result.get(0));
		assertEquals("the", result.get(3));
		assertEquals("market square", result.get(4));
		assertEquals("and", result.get(5));

		line = "Harry went to the 'market square' and bought some fish";
		result = StringSupport.split(line, " ", "'");
		assertEquals("the", result.get(3));
		assertEquals("market square", result.get(4));
		assertEquals("and", result.get(5));

		line = "Harry went to the'market square'and bought some fish";
		result = StringSupport.split(line, " ", "'");
		assertEquals("the", result.get(3));
		assertEquals("market square", result.get(4));
		assertEquals("and", result.get(5));

		line = "the \"market square \" and a space";
		result = StringSupport.split(line, " ", "\"");
		assertEquals("market square ", result.get(1));
/*
//TODO this should work
		line = "the \"market \\\"square \" and a space";
		result = StringSupport.split(line, " ", "\"");
		assertEquals("market \"square ", result.get(1));
*/

		line = "Harry went to the 'market square' (in Apeldoorn) and bought some fish";
		result = StringSupport.split(line, " ", "'()");
		assertEquals("the", result.get(3));
		assertEquals("market square", result.get(4));
		assertEquals("in Apeldoorn", result.get(5));
		assertEquals("and", result.get(6));

		line = "Harry went to the market (in Apeldoorn) and bought some fish";
		result = StringSupport.split(line, " ", "'()", false, false, false, true);
		assertEquals("the", result.get(3));
		assertEquals("market", result.get(4));
		assertEquals("(in Apeldoorn)", result.get(5));
		assertEquals("and", result.get(6));

		line = "Harry went to the market(in Apeldoorn)and bought some fish";
		result = StringSupport.split(line, " ", "'()", false, false, false, true);
		assertEquals("the", result.get(3));
		assertEquals("market", result.get(4));
		assertEquals("(in Apeldoorn)", result.get(5));
		assertEquals("and", result.get(6));

	}

	@Test
	public void extractStringsInBetweenTagsFromText() throws Exception {

		String text = "this is a text with a [part in between] brackets";
		Set result = StringSupport.extractStringsInbetweenTagsFromText(text, '[', ']', false);
		assertEquals(1, result.size());
		assertEquals("part in between", result.toArray()[0]);

		text = "this is a text";
		result = StringSupport.extractStringsInbetweenTagsFromText(text, '[', ']', false);
		assertEquals(0, result.size());

		text = "this is a text with a {part in between} brackets {and another part}";
		result = StringSupport.extractStringsInbetweenTagsFromText(text, '{', '}', false);
		assertEquals(2, result.size());
		assertEquals("part in between", result.toArray()[0]);
		assertEquals("and another part", result.toArray()[1]);

		//sort results
		result = StringSupport.extractStringsInbetweenTagsFromText(text, '{', '}', true);
		assertEquals(2, result.size());
		assertEquals("and another part", result.toArray()[0]);
		assertEquals("part in between", result.toArray()[1]);

	}

	@Test
	public void extractStringsInBetweenQuotesFromText() throws Exception {

		String text = "this is a text with a 'part in between' brackets";
		Set result = StringSupport.extractStringsInbetweenTagsFromText(text, '\'', '\'', false);
		assertEquals(1, result.size());
		assertEquals("part in between", result.toArray()[0]);

		text = "this is a text";
		result = StringSupport.extractStringsInbetweenTagsFromText(text, '\'', '\'', false);
		assertEquals(0, result.size());

		text = "this is a text with a \"part in between\" brackets \"and another part\"";
		result = StringSupport.extractStringsInbetweenTagsFromText(text, '\"', '\"', false);
		assertEquals(2, result.size());
		assertEquals("part in between", result.toArray()[0]);
		assertEquals("and another part", result.toArray()[1]);
	}

	@Test
	public void testReplaceFirst() throws Exception {
		assertEquals("Harry met Dick", StringSupport.replaceFirst("Harry met Sally", "Sally", "Dick"));
		assertEquals("Dick met Sally", StringSupport.replaceFirst("Harry met Sally", "Harry", "Dick"));
		assertEquals("Harry dates Sally", StringSupport.replaceFirst("Harry met Sally", "met", "dates"));
	}

	
	@Test
	public void testReplaceAll() throws Exception {
		assertEquals("Harry met Dick", StringSupport.replaceAll("Harry met Sally", "Sally", "Dick"));
		assertEquals("Dick met Sally", StringSupport.replaceAll("Harry met Sally", "Harry", "Dick"));
		assertEquals("Harry dates Sally", StringSupport.replaceAll("Harry met Sally", "met", "dates"));

		assertEquals("Harry metDick", StringSupport.replaceAll("Harry metSally", "Sally", "Dick"));
	}

	@Test
	public void testReplaceEntireWords() throws Exception {
		assertEquals("Harry met Dick", StringSupport.replaceEntireWords("Harry met Sally", " ", "", "Sally", "Dick"));
		assertEquals("Dick met Sally", StringSupport.replaceEntireWords("Harry met Sally", " ", "", "Harry", "Dick"));
		assertEquals("Harry dates Sally", StringSupport.replaceEntireWords("Harry met Sally", " ", "", "met", "dates"));

		assertEquals("Harry metSally", StringSupport.replaceEntireWords("Harry metSally", " ", "", "Sally", "Dick"));

		assertEquals("Harry metSally", StringSupport.replaceEntireWords("Harry metSally", " ", "", "Sally", "Dick"));
	}

	@Test
	public void testReplaceEntireWordsWithPunctChars() throws Exception {

		//WORD:NAMESPACE_BEGIN VAL: namespace x {
		String start = "NAMESPACE_BEGIN(CryptoPP)";
		String result = StringSupport.replaceEntireWords(start, " \t+-{}*%&/()\"", "", "NAMESPACE_BEGIN",  "namespace x {");

		System.out.println(result);

/*

WORD:NAMESPACE_BEGIN VAL: namespace x {
0.NAMESPACE_BEGIN(CryptoPP)
0.[[MACRO_START:NAMESPACE_BEGIN:DigiModel/sw/src/utils/Crypto/config.h:75:(x)]] namespace x {[[MACRO_END:NAMESPACE_BEGIN]]()CryptoPP
WORD:NAMESPACE_BEGIN VAL: namespace x {
1.[[MACRO_START:NAMESPACE_BEGIN:DigiModel/sw/src/utils/Crypto/config.h:75:(x)]] namespace x {[[MACRO_END:NAMESPACE_BEGIN]]()CryptoPP
2.[[MACRO_START:NAMESPACE_BEGIN:DigiModel/sw/src/utils/Crypto/config.h:75:()x]] namespace x [[MACRO_END:NAMESPACE_BEGIN]]()CryptoPP


 */

			}

	@Test
	public void testReplaceEntireWordsWithPunctChars2() throws Exception {

		//WORD:NAMESPACE_BEGIN VAL: namespace x {
		String start = "NAMESPACE_BEGIN(CryptoPP)";
		String result = StringSupport.replaceEntireWords(start, " \t+-{}*%&/", "()\"", "NAMESPACE_BEGIN",  "namespace x {");

		System.out.println(result);

		assertEquals("namespace x {(CryptoPP)", result);

		start = "NAMESPACE_BEGIN(CryptoPP X)";
		result = StringSupport.replaceEntireWords(start, " \t+-{}*%&/", "()\"", "CryptoPP",  "BLA");

		System.out.println(result);

		assertEquals("NAMESPACE_BEGIN(CryptoPP X)", result);

	}

	/*
	WORD:vtkSetClampMacro VAL: \
3. vtkSetClampMacro( NeedToRender, int, 0, 1 );
4.[[MACRO_START:vtkSetClampMacro:External/VTK/src/Common/vtkSetGet.h:131:(name,type,min,max)]] \[[MACRO_END:vtkSetClampMacro]](NeedToRender, int, 0, 1 ;

	 */

	@Test
	public void testReplaceEntireWordsWithPunctChars3() throws Exception {

		String result = StringSupport.replaceEntireWords("vtkSetClampMacro( NeedToRender, int, 0, 1 );",
				" \t+-{}*%&/()\"", "", "vtkSetClampMacro", "\\");
		//WORD:NAMESPACE_BEGIN VAL: namespace x {

		System.out.println(result);

		assertEquals("\\( NeedToRender, int, 0, 1 );", result);

	}


	private static final String TEXT_FILE_PATH = "org/ijsberg/iglu/util/io/directory structure/root/WWW/contact.html";

	@Test
	public void testAbsorbInputStream() throws Exception {
		File tmpDir = FileSupport.createTmpDir("Iglu-Util-test");
		String tmpFilePath = tmpDir + "contact.html";
		FileSupport.copyClassLoadableResourceToFileSystem(TEXT_FILE_PATH, tmpFilePath);


		String loadedFile = StringSupport.absorbInputStream(new FileInputStream(tmpFilePath));
		assertTrue(loadedFile.startsWith("<!--"));
		assertTrue(loadedFile.endsWith("</html>"));

		//TODO original values do not work any more (copyright sign is gone)
		loadedFile = StringSupport.absorbInputStream(new FileInputStream(tmpFilePath), "utf-8");
		//assertEquals(65533, loadedFile.charAt(10));
		loadedFile = StringSupport.absorbInputStream(new FileInputStream(tmpFilePath), "windows-1251");
		//assertEquals(1100, loadedFile.charAt(10));

//		System.out.println(0 + loadedFile.charAt(10));
//		System.out.println(bytes[10] + ":" + bytes[11] + ":" + bytes[12] + ":" + bytes[13]);
		//-17 -65

		assertTrue(FileSupport.deleteFile(tmpDir));
	}


	@Test
	public void testCondenseWhitespace() {
		assertEquals("\tthis line contains too much whitespace ", StringSupport.condenseWhitespace("\t\tthis  line \t  contains too much  whitespace \t  "));
	}
	
}
