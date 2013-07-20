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

package org.ijsberg.iglu.util.logic;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Logical expression, composed of statements and subexpressions that may be evaluated.
 * <p/>
 * An expression that has been analyzed fully, contains 1 operator and a number of elements that
 * may be evaluated accordingly.
 */
public class Expression extends Statement {
	private static HashMap cache = new HashMap();

	private ArrayList elements = new ArrayList();
	private Object[] elementsArray;
	private Operator operator;

	/**
	 * Copies contents of given expression into this one.
	 *
	 * @param expression
	 */
	private void copy(Expression expression) {
		this.operator = expression.operator;
		this.elementsArray = expression.elementsArray;
		this.elements = expression.elements;
	}

	/**
	 * @param operator
	 * @param elementsArray
	 */
	private Expression(Operator operator, Object[] elementsArray) {
		this.operator = operator;
		this.elementsArray = elementsArray;
		for (int i = 0; i < elementsArray.length; i++) {
			elements.add(elementsArray[i]);
		}
	}


	/**
	 * Constructs an expression based on a logical expression defined in a string.
	 * The string may contain
	 * <ul>
	 * <li>statements (which are not interpreted, just stored in Statement objects)</li>
	 * <li>logical operators '!'=NOT followed by an element,
	 * <li>'|': OR inbetween 2 elements,
	 * <li>'&amp;': AND inbetween 2 elements,
	 * <li>'(' and ')': group a subexpression</li>
	 * </ul>
	 *
	 * @param expression
	 * @see Statement
	 */
	public Expression(String expression) {
		this(expression, false);
	}


	/**
	 * Constructs an expression based on a logical expression defined in a string.
	 * The string may contain
	 * <ul>
	 * <li>statements (which are not interpreted, just stored in Statement objects)</li>
	 * <li>logical operators '!'=NOT followed by statement,
	 * <li>'|': OR inbetween 2 statements,
	 * <li>'&amp;': AND inbetween 2 statements,
	 * <li>'(' and ')': group a subexpression</li>
	 * </ul>
	 * <p/>
	 * Using the cache is only recommended for expressions that are used as constants
	 *
	 * @param expression
	 * @param useCache   expressions will be cached in a static cache if true
	 * @see Statement
	 */
	public Expression(String expression, boolean useCache) {
		if (useCache && cache.containsKey(expression)) {
			copy((Expression) cache.get(expression));
			return;
		}
		StringBuffer currentStatement = new StringBuffer();
		Object currentExpression = null;
		for (int i = 0; i < expression.length(); i++) {
			char c = expression.charAt(i);
			switch (c) {
				case ')': {
					throw new IllegalArgumentException("can't parse expression [" + expression + "] error at character " + i);
				}
				case '(': {
					int endOfSubExpression = findEndOfSubExpression(expression, i);
					if (endOfSubExpression == -1 || endOfSubExpression < i || currentStatement.length() > 0) {
						throw new IllegalArgumentException("can't parse expression [" + expression + "] error at character " + i);
					}
					String subExpressionStr = expression.substring(i + 1, endOfSubExpression);
					Expression subExpression = new Expression(subExpressionStr);
					elements.add(subExpression);
					i = endOfSubExpression;
					break;
				}
				case '&': {
					addStatement(currentStatement);
					elements.add(new Operator(AND));
					break;
				}
				case '|': {
					addStatement(currentStatement);
					elements.add(new Operator(OR));
					break;
				}
				case '!': {
					addStatement(currentStatement);
					if ((i + 1) < expression.length()) {
/*						if(expression.charAt(i + 1) == '=')
						{
							i++;
							elements.add(new Operator(NEQ));
						}
						else
*/
						{
							elements.add(new Operator(NOT));
						}
					}
					break;
				}
/*				case'=':
				{
					if((i + 1) < expression.length())
					{
						if(expression.charAt(++i) == '=')
						{
							elements.add(new Operator(EQ));
						}
						else
						{
							throw new IllegalArgumentException("can't parse expression [" + expression + "] error at character " + i);
						}
					}
				}
				case'<':
				{
					addStatement(currentStatement);
					elements.add(new Operator(LT));
					break;
				}
				case'>':
				{
					addStatement(currentStatement);
					elements.add(new Operator(GT));
					break;
				}*/
				default: {
					currentStatement.append(c);
				}
			}
		}
		addStatement(currentStatement);

		elementsArray = elements.toArray();

		//process expression further
		//push NOTs into expressions
		for (int i = 0; i < elementsArray.length; i++) {
			if (elementsArray[i] instanceof Operator) {
				Operator operator = (Operator) elementsArray[i];
				if (operator.getType() == NOT) {
					if (i != elementsArray.length - 1 && !(elementsArray[i + 1] instanceof Operator)) {
						elements.add(i + 2, new Expression(operator, new Object[]{elementsArray[i + 1]}));
						elements.remove(i);
						elements.remove(i);
						elementsArray = elements.toArray();
					}
					else {
						throw new IllegalArgumentException("can't parse expression [" + expression + ']');
					}
				}
			}
		}

/*		compact(expression, LT);
		compact(expression, GT);
		compact(expression, EQ);
		compact(expression, NEQ);
*/
		compact(expression, AND);
		//no real need to push ORs, checks are needed though, so do it anyway
		compact(expression, OR);

		if (useCache) {
			cache.put(expression, this);
		}
	}

	/**
	 * @param expression
	 * @param operatorType
	 */
	private void compact(String expression, int operatorType) {
		elementsArray = elements.toArray();

		//process expression further
		//push operators into expressions
		for (int i = 0; i < elementsArray.length; i++) {
			if (elementsArray[i] instanceof Operator) {
				Operator operator = (Operator) elementsArray[i];
				if (operator.getType() == operatorType) {
					if (i != elementsArray.length - 1 && i > 0 && !(elementsArray[i - 1] instanceof Operator) && !(elementsArray[i + 1] instanceof Operator)) {
						if (elementsArray[i - 1] instanceof Expression && ((Expression) elementsArray[i - 1]).getOperatorType() == operatorType) {
							((Expression) elementsArray[i - 1]).addElement((LogicalElement) elementsArray[i + 1]);
							elements.remove(i);
							elements.remove(i);
							elementsArray = elements.toArray();
							i--;
						}
						else {
							elements.add(i + 2, new Expression(operator, new Object[]{elementsArray[i - 1], elementsArray[i + 1]}));
							elements.remove(i - 1);
							elements.remove(i - 1);
							elements.remove(i - 1);
							elementsArray = elements.toArray();
							i--;
						}
					}
					else {
						throw new IllegalArgumentException("can't parse expression [" + expression + ']');
					}
				}
			}
		}
	}


	/**
	 * @return operator type used for evaluation of elements
	 */
	private int getOperatorType() {
		if (operator != null) {
			return operator.getType();
		}
		return NONE;
	}


	/**
	 * Adds a new statement to this Expression's elements.
	 * Clears the statementBuffer.
	 *
	 * @param statementBuffer
	 * @return
	 */
	private boolean addStatement(StringBuffer statementBuffer) {
		if (statementBuffer.length() > 0) {
			elements.add(new Statement(statementBuffer.toString()));
			elementsArray = elements.toArray();
			statementBuffer.delete(0, statementBuffer.length());
			return true;
		}
		return false;
	}


	/**
	 * Finds the position of the end bracket ')' matching a start bracket '('.
	 *
	 * @param expression
	 * @param start	  position of the start bracket
	 * @return position of the matching end bracket or 0 if it can not be located
	 */
	public static int findEndOfSubExpression(String expression, int start) {
		int count = 0;
		for (int i = start; i < expression.length(); i++) {
			switch (expression.charAt(i)) {
				case '(': {
					count++;
					break;
				}
				case ')': {
					count--;
					if (count == 0) {
						return i;
					}
					break;
				}
			}
		}
		throw new IllegalArgumentException("can't parse expression [" + expression + "]: end of subexpression not found");
	}

	/**
	 * @param e
	 */
	private void addElement(LogicalElement e) {
		elements.add(e);
		elementsArray = elements.toArray();
	}

	/**
	 * Answers questions like:
	 * <ul>
	 * <li>does "r|w|x" match "x" (true).</li>
	 * <li>does "r|w&x" match "x" (false).</li>
	 * </ul>
	 *
	 * @param statement
	 * @return true if the expression logically matches the statement
	 */
	public boolean match(String statement) {
		return match(new Object[]{statement});
	}

	/**
	 * Answers questions like:
	 * <ul>
	 * <li>does "r|w|x" match {"r","x"} (true).</li>
	 * <li>does "r|w&x" contain {"r","x"} (true).</li>
	 * <li>does "r|w&x" contain {"r"} (true).</li>
	 * <li>does "r|w&x" contain {"x"} (false).</li>
	 * </ul>
	 *
	 * @param statements
	 * @return true if the expression logically matches the statement
	 */
	public boolean match(Object[] statements) {
		boolean result = true;

		if (operator == null) {
			return ((Statement) elementsArray[0]).match(statements);
		}

		switch (operator.getType()) {
			case NOT: {
				return !((Statement) elementsArray[0]).match(statements);
			}
			case AND: {
				for (int i = 0; i < elementsArray.length; i++) {
					result = result && ((Statement) elementsArray[i]).match(statements);
				}
				break;
			}
			case OR: {
				result = false;
				for (int i = 0; i < elementsArray.length; i++) {
					result = result || ((Statement) elementsArray[i]).match(statements);
				}
				break;
			}
		}
		return result;
	}


	/**
	 * @return a representation of the parsed expression tree
	 */
	public String toString() {
		if (operator != null) {
			return operator.toString() + '(' + elements + ')';
		}
		return elements.toString();
	}

	/**
	 * Clears static expression cache.
	 */
	public static void clearCache() {
		cache.clear();
	}


	/**
	 * @return the number of logical elements this expression contains (not the grand total)
	 */
	public int getNrofElements() {
		return elements.size();
	}

	/**
	 * @return true if this expression contains one single statement (is therefor not composed)
	 */
	public boolean isSingleStatement() {
		if (elements.size() == 1) {
			if ((elementsArray[0] instanceof Expression)) {
				return ((Expression) elementsArray[0]).isSingleStatement();
			}
			else {
				return true;
			}
		}
		return false;
	}

	/**
	 * Performance optimizer:
	 * getStatement() in combination with isSingleStatement()
	 * isolates a single statement that can be evaluated immediately.
	 *
	 * @return the first statement string found
	 */
	public String getStatement() {
		if (elements.size() == 1) {
			if ((elementsArray[0] instanceof Expression)) {
				return ((Expression) elementsArray[0]).getStatement();
			}
			else if ((elementsArray[0] instanceof Statement)) {
				return ((Statement) elementsArray[0]).getStatement();
			}
		}
		return statement;
	}
}
