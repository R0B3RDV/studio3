/**
 * Aptana Studio
 * Copyright (c) 2005-2011 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the GNU Public License (GPL) v3 (with exceptions).
 * Please see the license.html included with this distribution for details.
 * Any modifications to this file must keep this entire header intact.
 */
package com.aptana.parsing.ast;

/**
 * @author ayeung
 */
public interface IParseError
{
	/**
	 * The starting offset where the error is located
	 * 
	 * @return the starting offset of the error
	 */
	public int getOffset();

	/**
	 * The message for the parse error
	 * 
	 * @return the error message
	 */
	public String getMessage();

}
