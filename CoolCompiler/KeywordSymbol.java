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

import java.io.PrintStream;

/** String table entry for identifiers. */
class KeywordSymbol extends AbstractSymbol {
	public int token;
    /* Creates a new symbol.
     * 
     * @see AbstractSymbol
     * */
    public KeywordSymbol(String str, int len, int index) {
	super(str, len, index);
        if (str.equals("if")) token = TokenConstants.IF;
        else if (str.toLowerCase().equals("class")) token = TokenConstants.CLASS;
        else if (str.toLowerCase().equals("inherits")) token = TokenConstants.INHERITS; 
        else if (str.toLowerCase().equals("not")) token = TokenConstants.NOT;
        else if (str.toLowerCase().equals("isvoid")) token = TokenConstants.ISVOID;
        else if (str.toLowerCase().equals("let")) token = TokenConstants.LET;
        else if (str.toLowerCase().equals("in")) token = TokenConstants.IN;
        else if (str.toLowerCase().equals("then")) token = TokenConstants.THEN;
        else if (str.toLowerCase().equals("else")) token = TokenConstants.ELSE;
        else if (str.toLowerCase().equals("fi")) token = TokenConstants.FI;
        else if (str.toLowerCase().equals("while")) token = TokenConstants.WHILE;
        else if (str.toLowerCase().equals("loop")) token = TokenConstants.LOOP;
        else if (str.toLowerCase().equals("pool")) token = TokenConstants.POOL;
        else if (str.toLowerCase().equals("case")) token = TokenConstants.CASE;
        else if (str.toLowerCase().equals("of")) token = TokenConstants.OF;
        else if (str.toLowerCase().equals("esac")) token = TokenConstants.ESAC;
        else if (str.toLowerCase().equals("new")) token = TokenConstants.NEW;
        else if (str.toLowerCase().equals("true")) token = TokenConstants.BOOL_CONST;
        else if (str.toLowerCase().equals("false")) token = TokenConstants.BOOL_CONST;
    }

    /** Returns a copy of this symbol */
    public Object clone() {
	return new KeywordSymbol(str, str.length(), index);
    }
}

