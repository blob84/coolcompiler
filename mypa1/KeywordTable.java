/*
Copyright (c) 2000 The Regents of the University of California.
All rights reserved.

Permission to use, copy, modify, and distribute this software for any
purpose, without fee, and without written agreement is hereby granted,
provided that the above copyright notice and the following two
paragraphs appear in all copies of this software.

IN NO EVENT SHALL THE UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY FOR
DIRECT, INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES ARISING OUT
OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF THE UNIVERSITY OF
CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY
AND FITNESS FOR A PARTICULAR PURPOSE.  THE SOFTWARE PROVIDED HEREUNDER IS
ON AN "AS IS" BASIS, AND THE UNIVERSITY OF CALIFORNIA HAS NO OBLIGATION TO
PROVIDE MAINTENANCE, SUPPORT, UPDATES, ENHANCEMENTS, OR MODIFICATIONS.
*/

class KeywordTable extends AbstractTable {
    /** Creates a new IdSymbol object. 
     * 
     * @see IdSymbol
     * */
    protected AbstractSymbol getNewSymbol(String s, int len, int index) {
	return new KeywordSymbol(s, len, index);
    }

    /** Looks up a symbol in this string table by its string representation
     * 
     * A fatal error is signalled if the string is not in the table
     *
     * @param s the string representation of the symbol
     * @return a symbol corresponding to the string
     * */
    public AbstractSymbol lookup(String s) {
	int len = s.length();
	AbstractSymbol sym = null;
	for (int i = 0; i < tbl.size(); i++) {
	    try {
		sym = (AbstractSymbol)tbl.elementAt(i);
	    } catch (ArrayIndexOutOfBoundsException ex) {
		Utilities.fatalError("Unexpected exception: " + ex);
	    }
	    if (sym.equalString(s, len)) {
		return sym;
	    }
	}
	//Utilities.fatalError("String table lookup failed on string: " + s);
	return null;
    }

}
