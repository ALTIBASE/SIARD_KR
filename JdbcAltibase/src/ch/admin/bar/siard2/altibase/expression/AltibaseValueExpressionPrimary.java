/*======================================================================
AltibaseValueExpressionPrimary implements the value
translation from ISO SQL to Altibase
Version     : $Id: $
Application : SIARD2
Description : AltibaseValueExpressionPrimary implements the value
			  translation from ISO SQL to Altibase
Platform    : Java 7
------------------------------------------------------------------------
Copyright  : 2016, Enter AG, Rüti ZH, Switzerland
Created    : 08.12.2016, Simon Jutz
======================================================================*/
package ch.admin.bar.siard2.altibase.expression;

import ch.enterag.sqlparser.SqlFactory;
import ch.enterag.sqlparser.expression.ValueExpressionPrimary;

/* =============================================================================== */

/**
 * Implements the value translation from ISO SQL to Altibase
 *
 * @author YounJung Park
 */
public class AltibaseValueExpressionPrimary extends ValueExpressionPrimary
{
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String format()
	{
		String sExpression = "";

		if (super.isArrayValueConstructor())
		{
			throw new IllegalArgumentException("ARRAY value constructor is not available in Altibase!");
		}
		else if (super.isMultisetValueConstructor())
		{
			throw new IllegalArgumentException("MULTISET value constructor is not available in Altibase!");
		}
		else if (super.isTableMultisetValueConstructor())
		{
			throw new IllegalArgumentException("TABLE MULTISET value constructor is not available in Altibase!");
		}
		else
		{
			sExpression = super.format();
		}

		return sExpression;
	} /* format */

	/* ------------------------------------------------------------------------ */

	/**
	 * constructor (only to be called by the factory)
	 *
	 * @param sf the factory
	 */
	public AltibaseValueExpressionPrimary(SqlFactory sf)
	{
		super(sf);
	} /* constructor */

}
