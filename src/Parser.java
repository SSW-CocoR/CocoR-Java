/*-------------------------------------------------------------------------
Compiler Generator Coco/R,
Copyright (c) 1990, 2004 Hanspeter Moessenboeck, University of Linz
extended by M. Loeberbauer & A. Woess, Univ. of Linz
ported from C# to Java by Wolfgang Ahorner
with improvements by Pat Terry, Rhodes University

This program is free software; you can redistribute it and/or modify it
under the terms of the GNU General Public License as published by the
Free Software Foundation; either version 2, or (at your option) any
later version.

This program is distributed in the hope that it will be useful, but
WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
for more details.

You should have received a copy of the GNU General Public License along
with this program; if not, write to the Free Software Foundation, Inc.,
59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.

As an exception, it is allowed to write an extension of Coco/R that is
used as a plugin in non-free software.

If not otherwise stated, any source code generated by Coco/R (other than
Coco/R itself) does not fall under the GNU General Public License.
------------------------------------------------------------------------*/
package Coco;

import java.util.ArrayList;
import java.util.Stack;

public class Parser {

	public class SynTree {
		public SynTree(Token t ) {
			tok = t;
			children = new ArrayList<SynTree>();
		}

		public Token tok;
		public ArrayList<SynTree> children;

		private void printIndent(int n) {
			for(int i=0; i < n; ++i) System.out.print(" ");
		}

		public void dump_all(int indent, boolean isLast) {
			int last_idx = children.size();
			if(tok.col > 0) {
				printIndent(indent);
				System.out.println(((isLast || (last_idx == 0)) ? "= " : " ") + "\t" + tok.line + "\t" + tok.col + "\t" + tok.kind + "\t" + tok.val);
			}
			else {
				printIndent(indent);
				System.out.println(children.size() + "\t" + tok.line + "\t" + tok.kind + "\t" + tok.val);
			}
			if(last_idx > 0) {
					for(int idx=0; idx < last_idx; ++idx) children.get(idx).dump_all(indent+4, idx == last_idx);
			}
		}
		public void dump_all() {
			dump_all(0, false);
		}

		public void dump_pruned(int indent, boolean isLast) {
			int last_idx = children.size();
			int indentPlus = 4;
			if(tok.col > 0) {
				printIndent(indent);
				System.out.println(((isLast || (last_idx == 0)) ? "= " : " ") + "\t" + tok.line + "\t" + tok.col + "\t" + tok.kind + "\t" + tok.val);
			}
			else {
				if(last_idx == 1) {
					if(children.get(0).children.size() == 0) {
						printIndent(indent);
						System.out.println(children.size() + "\t" + tok.line + "\t" + tok.kind + "\t" + tok.val);
					}
					else indentPlus = 0;
				}
				else {
					printIndent(indent);
					System.out.println(children.size() + "\t" + tok.line + "\t" + tok.kind + "\t" + tok.val);
				}
			}
			if(last_idx > 0) {
					for(int idx=0; idx < last_idx; ++idx) children.get(idx).dump_pruned(indent+indentPlus, idx == last_idx);
			}
		}
		public void dump_pruned() {
			dump_pruned(0, false);
		}
	}

	//non terminals
	public static final int _NT_Coco = 0;
	public static final int _NT_SetDecl = 1;
	public static final int _NT_TokenDecl = 2;
	public static final int _NT_TokenExpr = 3;
	public static final int _NT_Set = 4;
	public static final int _NT_AttrDecl = 5;
	public static final int _NT_SemText = 6;
	public static final int _NT_Expression = 7;
	public static final int _NT_SimSet = 8;
	public static final int _NT_Char = 9;
	public static final int _NT_Sym = 10;
	public static final int _NT_TypeName = 11;
	public static final int _NT_Term = 12;
	public static final int _NT_Resolver = 13;
	public static final int _NT_Factor = 14;
	public static final int _NT_Attribs = 15;
	public static final int _NT_Condition = 16;
	public static final int _NT_TokenTerm = 17;
	public static final int _NT_TokenFactor = 18;
	public static final int _NT_Bracketed = 19;
	public static final int maxNT = 19;
	//terminals
	public static final int _EOF = 0;
	public static final int _ident = 1;
	public static final int _number = 2;
	public static final int _string = 3;
	public static final int _badString = 4;
	public static final int _char = 5;
//	public static final int _("COMPILER") = 6;
//	public static final int _("IGNORECASE") = 7;
//	public static final int _("CHARACTERS") = 8;
//	public static final int _("TOKENS") = 9;
//	public static final int _("PRAGMAS") = 10;
//	public static final int _("COMMENTS") = 11;
//	public static final int _("FROM") = 12;
//	public static final int _("TO") = 13;
//	public static final int _("NESTED") = 14;
//	public static final int _("IGNORE") = 15;
//	public static final int _("PRODUCTIONS") = 16;
//	public static final int _("=") = 17;
//	public static final int _(".") = 18;
//	public static final int _("END") = 19;
//	public static final int _("+") = 20;
//	public static final int _("-") = 21;
//	public static final int _("..") = 22;
//	public static final int _("ANY") = 23;
//	public static final int _(":") = 24;
//	public static final int _("<") = 25;
//	public static final int _("^") = 26;
//	public static final int _("out") = 27;
//	public static final int _(">") = 28;
//	public static final int _(",") = 29;
//	public static final int _("<.") = 30;
//	public static final int _(".>") = 31;
//	public static final int _("[") = 32;
//	public static final int _("]") = 33;
//	public static final int _("|") = 34;
//	public static final int _("WEAK") = 35;
//	public static final int _("(") = 36;
//	public static final int _(")") = 37;
//	public static final int _("{") = 38;
//	public static final int _("}") = 39;
//	public static final int _("SYNC") = 40;
//	public static final int _("IF") = 41;
//	public static final int _("CONTEXT") = 42;
//	public static final int _("(.") = 43;
//	public static final int _(".)") = 44;
//	public static final int _(???) = 45;
	//non terminals
	public static final int maxT = 45;
	public static final int _ddtSym = 46;
	public static final int _optionSym = 47;

	static final boolean _T = true;
	static final boolean _x = false;
	static final int minErrDist = 2;

	public Token t;    // last recognized token
	public Token la;   // lookahead token
	int errDist = minErrDist;

	public Scanner scanner;
	public Errors errors;

	static final int id = 0;
	static final int str = 1;

	public Trace trace;         // other Coco objects referenced by this ATG
	public Tab tab;
	public DFA dfa;
	public ParserGen pgen;

	boolean genScanner;
	String tokenString;         // used in declarations of literal tokens
	String noString = "-none-"; // used in declarations of literal tokens

/*-------------------------------------------------------------------------*/



	public SynTree ast_root;
	Stack<SynTree> ast_stack;

	void AstAddTerminal() {
        SynTree st = new SynTree( t );
        ast_stack.peek().children.add(st);
	}

	boolean AstAddNonTerminal(int kind, String nt_name, int line) {
        Token ntTok = new Token();
        ntTok.kind = kind;
        ntTok.line = line;
        ntTok.val = nt_name;
        SynTree st = new SynTree( ntTok );
        ast_stack.peek().children.add(st);
        ast_stack.push(st);
        return true;
	}

	void AstPopNonTerminal() {
        ast_stack.pop();
	}

	public Parser(Scanner scanner) {
		this.scanner = scanner;
		errors = new Errors();
	}

	void SynErr (int n) {
		if (errDist >= minErrDist) errors.SynErr(la.line, la.col, n);
		errDist = 0;
	}

	public void SemErr (String msg) {
		if (errDist >= minErrDist) errors.SemErr(t.line, t.col, msg);
		errDist = 0;
	}

	void Get () {
		for (;;) {
			t = la;
			la = scanner.Scan();
			if (la.kind <= maxT) {
				++errDist;
				break;
			}

			if (la.kind == _ddtSym) {
				tab.SetDDT(la.val); 
			}
			if (la.kind == _optionSym) {
				tab.SetOption(la.val); 
			}
			la = t;
		}
	}

	boolean isKind(Token t, int n) {
		int k = t.kind;
		while(k >= 0) {
			if (k == n) return true;
			k = tBase[k];
		}
		return false;
	}

	void Expect (int n) {
		if (isKind(la, n)) Get(); else { SynErr(n); }
	}

	boolean StartOf (int s) {
		return set[s][la.kind];
	}

	void ExpectWeak (int n, int follow) {
		if (isKind(la, n)) Get();
		else {
			SynErr(n);
			while (!StartOf(follow)) Get();
		}
	}

	boolean WeakSeparator (int n, int syFol, int repFol) {
		int kind = la.kind;
		if (isKind(la, n)) { Get(); return true; }
		else if (StartOf(repFol)) return false;
		else {
			SynErr(n);
			while (!(set[syFol][kind] || set[repFol][kind] || set[0][kind])) {
				Get();
				kind = la.kind;
			}
			return StartOf(syFol);
		}
	}

	void Coco_NT() {
		Symbol sym; Graph g, g1, g2; String gramName; CharSet s; int beg, line; 
		if (StartOf(1)) {
			Get();
			beg = t.pos; line = t.line; 
			while (StartOf(1)) {
				Get();
			}
			pgen.usingPos = new Position(beg, la.pos, 0, line); 
		}
		Expect(6 /* "COMPILER" */);
		genScanner = true;
		tab.ignored = new CharSet(); 
		Expect(_ident);
		gramName = t.val;
		beg = la.pos; line = la.line;
		
		while (StartOf(2)) {
			Get();
		}
		tab.semDeclPos = new Position(beg, la.pos, 0, line); 
		if (isKind(la, 7 /* "IGNORECASE" */)) {
			Get();
			dfa.ignoreCase = true; 
		}
		if (isKind(la, 8 /* "CHARACTERS" */)) {
			Get();
			while (isKind(la, _ident)) {
				SetDecl_NT();
			}
		}
		if (isKind(la, 9 /* "TOKENS" */)) {
			Get();
			while (isKind(la, _ident) || isKind(la, _string) || isKind(la, _char)) {
				TokenDecl_NT(Node.t);
			}
		}
		if (isKind(la, 10 /* "PRAGMAS" */)) {
			Get();
			while (isKind(la, _ident) || isKind(la, _string) || isKind(la, _char)) {
				TokenDecl_NT(Node.pr);
			}
		}
		while (isKind(la, 11 /* "COMMENTS" */)) {
			Get();
			boolean nested = false; 
			Expect(12 /* "FROM" */);
			g1 = TokenExpr_NT();
			Expect(13 /* "TO" */);
			g2 = TokenExpr_NT();
			if (isKind(la, 14 /* "NESTED" */)) {
				Get();
				nested = true; 
			}
			dfa.NewComment(g1.l, g2.l, nested); 
		}
		while (isKind(la, 15 /* "IGNORE" */)) {
			Get();
			s = Set_NT();
			tab.ignored.Or(s); 
		}
		while (!(isKind(la, _EOF) || isKind(la, 16 /* "PRODUCTIONS" */))) {SynErr(46); Get();}
		Expect(16 /* "PRODUCTIONS" */);
		if (genScanner) dfa.MakeDeterministic();
		tab.DeleteNodes();
		
		while (isKind(la, _ident)) {
			Get();
			sym = tab.FindSym(t.val);
			boolean undef = sym == null;
			if (undef) sym = tab.NewSym(Node.nt, t.val, t.line, t.col);
			else {
			 if (sym.typ == Node.nt) {
			   if (sym.graph != null) SemErr("name declared twice");
			 } else SemErr("this symbol kind not allowed on left side of production");
			 sym.line = t.line;
			}
			boolean noAttrs = sym.attrPos == null;
			sym.attrPos = null;
			boolean noRet = sym.retVar==null;
			sym.retVar = null;
			
			if (isKind(la, 25 /* "<" */) || isKind(la, 30 /* "<." */)) {
				AttrDecl_NT(sym);
			}
			if (!undef)
			 if (noAttrs != (sym.attrPos == null)
			   || noRet != (sym.retVar == null))
			   SemErr("attribute mismatch between declaration and use of this symbol");
			
			if (isKind(la, 43 /* "(." */)) {
				sym.semPos = SemText_NT();
			}
			ExpectWeak(17 /* "=" */, 3);
			g = Expression_NT();
			sym.graph = g.l;
			tab.Finish(g);
			
			ExpectWeak(18 /* "." */, 4);
		}
		Expect(19 /* "END" */);
		Expect(_ident);
		if (gramName.compareTo(t.val) != 0)
		 SemErr("name does not match grammar name");
		tab.gramSy = tab.FindSym(gramName);
		if (tab.gramSy == null)
		 SemErr("missing production for grammar name");
		else {
		 sym = tab.gramSy;
		 if (sym.attrPos != null)
		   SemErr("grammar symbol must not have attributes");
		}
		tab.noSym = tab.NewSym(Node.t, "???", 0, 0); // noSym gets highest number
		tab.SetupAnys();
		tab.RenumberPragmas();
		if (tab.ddt[2]) tab.PrintNodes();
		if (errors.count == 0) {
		 System.out.println("checking");
		 tab.CompSymbolSets();
		 if (tab.ddt[7]) tab.XRef();
		 boolean doGenCode = false;
		 if(tab.ignoreErrors) {
		   doGenCode = true;
		   tab.GrammarCheckAll();
		 }
		 else doGenCode = tab.GrammarOk();
		 if(tab.genRREBNF && doGenCode) {
		   pgen.WriteRREBNF();
		 }
		 if (doGenCode) {
		   System.out.print("parser");
		   pgen.WriteParser();
		   if (genScanner) {
		     System.out.print(" + scanner");
		     dfa.WriteScanner();
		     if (tab.ddt[0]) dfa.PrintStates();
		   }
		   System.out.println(" generated");
		   if (tab.ddt[8]) pgen.WriteStatistics();
		 }
		}
		if (tab.ddt[6]) tab.PrintSymbolTable();
		
		Expect(18 /* "." */);
	}

	void SetDecl_NT() {
		CharSet s; 
		Expect(_ident);
		String name = t.val;
		CharClass c = tab.FindCharClass(name);
		if (c != null) SemErr("name declared twice");
		
		Expect(17 /* "=" */);
		s = Set_NT();
		if (s.Elements() == 0) SemErr("character set must not be empty");
		c = tab.NewCharClass(name, s);
		
		Expect(18 /* "." */);
	}

	void TokenDecl_NT(int typ) {
		SymInfo s, si; Symbol sym, inheritsSym; Graph g; 
		s = Sym_NT();
		sym = tab.FindSym(s.name);
		if (sym != null) SemErr("name declared twice");
		else {
		 sym = tab.NewSym(typ, s.name, t.line, t.col);
		 sym.tokenKind = Symbol.fixedToken;
		}
		tokenString = null;
		
		if (isKind(la, 24 /* ":" */)) {
			Get();
			si = Sym_NT();
			inheritsSym = tab.FindSym(si.name);
			if (inheritsSym == null) SemErr("token can't inherit from undeclared name");
			else if (inheritsSym == sym) SemErr("token must not inherit from itself");
			else if (inheritsSym.typ != typ) SemErr("token can't inherit from this token type");
			else sym.inherits = inheritsSym;
			
		}
		while (!(StartOf(5))) {SynErr(47); Get();}
		if (isKind(la, 17 /* "=" */)) {
			Get();
			g = TokenExpr_NT();
			Expect(18 /* "." */);
			if (s.kind == str) SemErr("a literal must not be declared with a structure");
			tab.Finish(g);
			if (tokenString == null || tokenString.equals(noString))
			 dfa.ConvertToStates(g.l, sym);
			else { // TokenExpr is a single string
			 if (tab.literals.get(tokenString) != null)
			   SemErr("token string declared twice");
			 tab.literals.put(tokenString, sym);
			 dfa.MatchLiteral(tokenString, sym);
			}
			
		} else if (StartOf(6)) {
			if (s.kind == id) genScanner = false;
			else dfa.MatchLiteral(sym.name, sym);
			
		} else SynErr(48);
		if (isKind(la, 43 /* "(." */)) {
			sym.semPos = SemText_NT();
			if (typ == Node.t) errors.Warning("Warning semantic action on token declarations require a custom Scanner"); 
		}
	}

	Graph  TokenExpr_NT() {
		Graph  g;
		Graph g2; 
		g = TokenTerm_NT();
		boolean first = true; 
		while (WeakSeparator(34,7,8) ) {
			g2 = TokenTerm_NT();
			if (first) { tab.MakeFirstAlt(g); first = false; }
			tab.MakeAlternative(g, g2);
			
		}
		return g;
	}

	CharSet  Set_NT() {
		CharSet  s;
		CharSet s2; 
		s = SimSet_NT();
		while (isKind(la, 20 /* "+" */) || isKind(la, 21 /* "-" */)) {
			if (isKind(la, 20 /* "+" */)) {
				Get();
				s2 = SimSet_NT();
				s.Or(s2); 
			} else {
				Get();
				s2 = SimSet_NT();
				s.Subtract(s2); 
			}
		}
		return s;
	}

	void AttrDecl_NT(Symbol sym) {
		int beg, col, line; 
		if (isKind(la, 25 /* "<" */)) {
			Get();
			if (isKind(la, 26 /* "^" */) || isKind(la, 27 /* "out" */)) {
				if (isKind(la, 26 /* "^" */)) {
					Get();
				} else {
					Get();
				}
				beg = la.pos; 
				TypeName_NT();
				sym.retType = scanner.buffer.GetString(beg, la.pos); 
				Expect(_ident);
				sym.retVar = t.val; 
				if (isKind(la, 28 /* ">" */)) {
					Get();
				} else if (isKind(la, 29 /* "," */)) {
					Get();
					beg = la.pos; col = la.col; line = la.line; 
					while (StartOf(9)) {
						Get();
					}
					Expect(28 /* ">" */);
					if (t.pos > beg)
					 sym.attrPos = new Position(beg, t.pos, col, line); 
				} else SynErr(49);
			} else if (StartOf(10)) {
				beg = la.pos; col = la.col; line = la.line; 
				if (StartOf(11)) {
					Get();
					while (StartOf(9)) {
						Get();
					}
				}
				Expect(28 /* ">" */);
				if (t.pos > beg)
				 sym.attrPos = new Position(beg, t.pos, col, line); 
			} else SynErr(50);
		} else if (isKind(la, 30 /* "<." */)) {
			Get();
			if (isKind(la, 26 /* "^" */) || isKind(la, 27 /* "out" */)) {
				if (isKind(la, 26 /* "^" */)) {
					Get();
				} else {
					Get();
				}
				beg = la.pos; 
				TypeName_NT();
				sym.retType = scanner.buffer.GetString(beg, la.pos); 
				Expect(_ident);
				sym.retVar = t.val; 
				if (isKind(la, 31 /* ".>" */)) {
					Get();
				} else if (isKind(la, 29 /* "," */)) {
					Get();
					beg = la.pos; col = la.col; line = la.line; 
					while (StartOf(12)) {
						Get();
					}
					Expect(31 /* ".>" */);
					if (t.pos > beg)
					 sym.attrPos = new Position(beg, t.pos, col, line); 
				} else SynErr(51);
			} else if (StartOf(10)) {
				beg = la.pos; col = la.col; line = la.line; 
				if (StartOf(13)) {
					Get();
					while (StartOf(12)) {
						Get();
					}
				}
				Expect(31 /* ".>" */);
				if (t.pos > beg)
				 sym.attrPos = new Position(beg, t.pos, col, line); 
			} else SynErr(52);
		} else SynErr(53);
	}

	Position  SemText_NT() {
		Position  pos;
		Expect(43 /* "(." */);
		int beg = la.pos; int col = la.col; int line = la.line; 
		while (StartOf(14)) {
			if (StartOf(15)) {
				Get();
			} else if (isKind(la, _badString)) {
				Get();
				SemErr("bad string in semantic action"); 
			} else {
				Get();
				SemErr("missing end of previous semantic action"); 
			}
		}
		Expect(44 /* ".)" */);
		pos = new Position(beg, t.pos, col, line); 
		return pos;
	}

	Graph  Expression_NT() {
		Graph  g;
		Graph g2; 
		g = Term_NT();
		boolean first = true; 
		while (WeakSeparator(34,16,17) ) {
			g2 = Term_NT();
			if (first) { tab.MakeFirstAlt(g); first = false; }
			tab.MakeAlternative(g, g2);
			
		}
		return g;
	}

	CharSet  SimSet_NT() {
		CharSet  s;
		int n1, n2; 
		s = new CharSet(); 
		if (isKind(la, _ident)) {
			Get();
			CharClass c = tab.FindCharClass(t.val);
			if (c == null) SemErr("undefined name"); else s.Or(c.set);
			
		} else if (isKind(la, _string)) {
			Get();
			String name = t.val;
			name = tab.Unescape(name.substring(1, name.length()-1));
			for (int i = 0; i < name.length(); i++)
			 if (dfa.ignoreCase) s.Set(Character.toLowerCase(name.charAt(i)));
			 else s.Set(name.charAt(i)); 
		} else if (isKind(la, _char)) {
			n1 = Char_NT();
			s.Set(n1); 
			if (isKind(la, 22 /* ".." */)) {
				Get();
				n2 = Char_NT();
				for (int i = n1; i <= n2; i++) s.Set(i); 
			}
		} else if (isKind(la, 23 /* "ANY" */)) {
			Get();
			s = new CharSet(); s.Fill(); 
		} else SynErr(54);
		return s;
	}

	int  Char_NT() {
		int  n;
		Expect(_char);
		String name = t.val; n = 0;
		name = tab.Unescape(name.substring(1, name.length()-1));
		if (name.length() == 1) n = name.charAt(0);
		else SemErr("unacceptable character value");
		if (dfa.ignoreCase && (char)n >= 'A' && (char)n <= 'Z') n += 32;
		
		return n;
	}

	SymInfo  Sym_NT() {
		SymInfo  s;
		s = new SymInfo(); s.name = "???"; s.kind = id; 
		if (isKind(la, _ident)) {
			Get();
			s.kind = id; s.name = t.val; 
		} else if (isKind(la, _string) || isKind(la, _char)) {
			if (isKind(la, _string)) {
				Get();
				s.name = t.val; 
			} else {
				Get();
				s.name = "\"" + t.val.substring(1, t.val.length()-1) + "\""; 
			}
			s.kind = str;
			if (dfa.ignoreCase) s.name = s.name.toLowerCase();
			if (s.name.indexOf(' ') >= 0)
			 SemErr("literal tokens must not contain blanks"); 
		} else SynErr(55);
		return s;
	}

	void TypeName_NT() {
		Expect(_ident);
		while (isKind(la, 18 /* "." */) || isKind(la, 25 /* "<" */) || isKind(la, 32 /* "[" */)) {
			if (isKind(la, 18 /* "." */)) {
				Get();
				Expect(_ident);
			} else if (isKind(la, 32 /* "[" */)) {
				Get();
				Expect(33 /* "]" */);
			} else {
				Get();
				TypeName_NT();
				while (isKind(la, 29 /* "," */)) {
					Get();
					TypeName_NT();
				}
				Expect(28 /* ">" */);
			}
		}
	}

	Graph  Term_NT() {
		Graph  g;
		Graph g2; Node rslv = null; g = null; 
		if (StartOf(18)) {
			if (isKind(la, 41 /* "IF" */)) {
				rslv = tab.NewNode(Node.rslv, null, la.line, la.col); 
				rslv.pos = Resolver_NT();
				g = new Graph(rslv);                       
			}
			g2 = Factor_NT();
			if (rslv != null) tab.MakeSequence(g, g2);
			else g = g2;
			
			while (StartOf(19)) {
				g2 = Factor_NT();
				tab.MakeSequence(g, g2); 
			}
		} else if (StartOf(20)) {
			g = new Graph(tab.NewNode(Node.eps, null, t.line, t.col)); 
		} else SynErr(56);
		if (g == null) // invalid start of Term
		 g = new Graph(tab.NewNode(Node.eps, null, t.line, t.col));
		
		return g;
	}

	Position  Resolver_NT() {
		Position  pos;
		Expect(41 /* "IF" */);
		Expect(36 /* "(" */);
		int beg = la.pos; int col = la.col; int line = la.line; 
		Condition_NT();
		pos = new Position(beg, t.pos, col, line); 
		return pos;
	}

	Graph  Factor_NT() {
		Graph  g;
		SymInfo s; Position pos; boolean weak = false;
		g = null; 
		switch (la.kind) {
		case _ident: case _string: case _char: case 35 /* "WEAK" */: {
			if (isKind(la, 35 /* "WEAK" */)) {
				Get();
				weak = true; 
			}
			s = Sym_NT();
			Symbol sym = tab.FindSym(s.name);
			if (sym == null && s.kind == str)
			 sym = (Symbol)tab.literals.get(s.name);
			boolean undef = sym == null;
			if (undef) {
			 if (s.kind == id)
			   sym = tab.NewSym(Node.nt, s.name, 0, 0);  // forward nt
			 else if (genScanner) {
			   sym = tab.NewSym(Node.t, s.name, t.line, t.col);
			   dfa.MatchLiteral(sym.name, sym);
			 } else {  // undefined string in production
			   SemErr("undefined string in production");
			   sym = tab.eofSy;  // dummy
			 }
			}
			int typ = sym.typ;
			if (typ != Node.t && typ != Node.nt)
			 SemErr("this symbol kind is not allowed in a production");
			if (weak)
			 if (typ == Node.t) typ = Node.wt;
			 else SemErr("only terminals may be weak");
			Node p = tab.NewNode(typ, sym, t.line, t.col);
			g = new Graph(p);
			
			if (isKind(la, 25 /* "<" */) || isKind(la, 30 /* "<." */)) {
				Attribs_NT(p);
				if (s.kind != id) SemErr("a literal must not have attributes"); 
			}
			if (undef) {
			 sym.attrPos = p.pos;  // dummy
			 sym.retVar = p.retVar;  // AH - dummy
			} else if ((p.pos == null) != (sym.attrPos == null)
			      || (p.retVar == null) != (sym.retVar == null))
			 SemErr("attribute mismatch between declaration and use of this symbol");
			
			break;
		}
		case 36 /* "(" */: {
			Get();
			g = Expression_NT();
			Expect(37 /* ")" */);
			break;
		}
		case 32 /* "[" */: {
			Get();
			g = Expression_NT();
			Expect(33 /* "]" */);
			tab.MakeOption(g); 
			break;
		}
		case 38 /* "{" */: {
			Get();
			g = Expression_NT();
			Expect(39 /* "}" */);
			tab.MakeIteration(g); 
			break;
		}
		case 43 /* "(." */: {
			pos = SemText_NT();
			Node p = tab.NewNode(Node.sem, null, t.line, t.col);
			p.pos = pos;
			g = new Graph(p);
			
			break;
		}
		case 23 /* "ANY" */: {
			Get();
			Node p = tab.NewNode(Node.any, null, t.line, t.col);  // p.set is set in tab.SetupAnys
			g = new Graph(p);
			
			break;
		}
		case 40 /* "SYNC" */: {
			Get();
			Node p = tab.NewNode(Node.sync, null, t.line, t.col);
			g = new Graph(p);
			
			break;
		}
		default: SynErr(57); break;
		}
		if (g == null) // invalid start of Factor
		 g = new Graph(tab.NewNode(Node.eps, null, t.line, t.col));
		
		return g;
	}

	void Attribs_NT(Node n) {
		int beg, col, line; 
		if (isKind(la, 25 /* "<" */)) {
			Get();
			if (isKind(la, 26 /* "^" */) || isKind(la, 27 /* "out" */)) {
				if (isKind(la, 26 /* "^" */)) {
					Get();
				} else {
					Get();
				}
				beg = la.pos; 
				while (StartOf(21)) {
					if (StartOf(22)) {
						Get();
					} else if (isKind(la, 32 /* "[" */) || isKind(la, 36 /* "(" */)) {
						Bracketed_NT();
					} else {
						Get();
						SemErr("bad string in attributes"); 
					}
				}
				n.retVar = scanner.buffer.GetString(beg, la.pos); 
				if (isKind(la, 28 /* ">" */)) {
					Get();
				} else if (isKind(la, 29 /* "," */)) {
					Get();
					beg = la.pos; col = la.col; line = la.line; 
					while (StartOf(9)) {
						if (StartOf(23)) {
							Get();
						} else {
							Get();
							SemErr("bad string in attributes"); 
						}
					}
					Expect(28 /* ">" */);
					if (t.pos > beg) n.pos = new Position(beg, t.pos, col, line); 
				} else SynErr(58);
			} else if (StartOf(10)) {
				beg = la.pos; col = la.col; line = la.line; 
				if (StartOf(11)) {
					if (StartOf(24)) {
						Get();
					} else {
						Get();
						SemErr("bad string in attributes"); 
					}
					while (StartOf(9)) {
						if (StartOf(23)) {
							Get();
						} else {
							Get();
							SemErr("bad string in attributes"); 
						}
					}
				}
				Expect(28 /* ">" */);
				if (t.pos > beg) n.pos = new Position(beg, t.pos, col, line); 
			} else SynErr(59);
		} else if (isKind(la, 30 /* "<." */)) {
			Get();
			if (isKind(la, 26 /* "^" */) || isKind(la, 27 /* "out" */)) {
				if (isKind(la, 26 /* "^" */)) {
					Get();
				} else {
					Get();
				}
				beg = la.pos; 
				while (StartOf(25)) {
					if (StartOf(26)) {
						Get();
					} else if (isKind(la, 32 /* "[" */) || isKind(la, 36 /* "(" */)) {
						Bracketed_NT();
					} else {
						Get();
						SemErr("bad string in attributes"); 
					}
				}
				n.retVar = scanner.buffer.GetString(beg, la.pos); 
				if (isKind(la, 31 /* ".>" */)) {
					Get();
				} else if (isKind(la, 29 /* "," */)) {
					Get();
					beg = la.pos; col = la.col; line = la.line; 
					while (StartOf(12)) {
						if (StartOf(27)) {
							Get();
						} else {
							Get();
							SemErr("bad string in attributes"); 
						}
					}
					Expect(31 /* ".>" */);
					if (t.pos > beg) n.pos = new Position(beg, t.pos, col, line); 
				} else SynErr(60);
			} else if (StartOf(10)) {
				beg = la.pos; col = la.col; line = la.line; 
				if (StartOf(13)) {
					if (StartOf(28)) {
						Get();
					} else {
						Get();
						SemErr("bad string in attributes"); 
					}
					while (StartOf(12)) {
						if (StartOf(27)) {
							Get();
						} else {
							Get();
							SemErr("bad string in attributes"); 
						}
					}
				}
				Expect(31 /* ".>" */);
				if (t.pos > beg) n.pos = new Position(beg, t.pos, col, line); 
			} else SynErr(61);
		} else SynErr(62);
	}

	void Condition_NT() {
		while (StartOf(29)) {
			if (isKind(la, 36 /* "(" */)) {
				Get();
				Condition_NT();
			} else {
				Get();
			}
		}
		Expect(37 /* ")" */);
	}

	Graph  TokenTerm_NT() {
		Graph  g;
		Graph g2; 
		g = TokenFactor_NT();
		while (StartOf(7)) {
			g2 = TokenFactor_NT();
			tab.MakeSequence(g, g2); 
		}
		if (isKind(la, 42 /* "CONTEXT" */)) {
			Get();
			Expect(36 /* "(" */);
			g2 = TokenExpr_NT();
			tab.SetContextTrans(g2.l); dfa.hasCtxMoves = true;
			tab.MakeSequence(g, g2); 
			Expect(37 /* ")" */);
		}
		return g;
	}

	Graph  TokenFactor_NT() {
		Graph  g;
		SymInfo s; 
		g = null; 
		if (isKind(la, _ident) || isKind(la, _string) || isKind(la, _char)) {
			s = Sym_NT();
			if (s.kind == id) {
			 CharClass c = tab.FindCharClass(s.name);
			 if (c == null) {
			   SemErr("undefined name: " + s.name);
			   c = tab.NewCharClass(s.name, new CharSet());
			 }
			 Node p = tab.NewNode(Node.clas, null, 0, 0); p.val = c.n;
			 g = new Graph(p);
			 tokenString = noString;
			} else { // str
			 g = tab.StrToGraph(s.name);
			 if (tokenString == null) tokenString = s.name;
			 else tokenString = noString;
			}
			
		} else if (isKind(la, 36 /* "(" */)) {
			Get();
			g = TokenExpr_NT();
			Expect(37 /* ")" */);
		} else if (isKind(la, 32 /* "[" */)) {
			Get();
			g = TokenExpr_NT();
			Expect(33 /* "]" */);
			tab.MakeOption(g); tokenString = noString; 
		} else if (isKind(la, 38 /* "{" */)) {
			Get();
			g = TokenExpr_NT();
			Expect(39 /* "}" */);
			tab.MakeIteration(g); tokenString = noString; 
		} else SynErr(63);
		if (g == null) // invalid start of TokenFactor
		 g = new Graph(tab.NewNode(Node.eps, null, t.line, t.col)); 
		return g;
	}

	void Bracketed_NT() {
		if (isKind(la, 36 /* "(" */)) {
			Get();
			while (StartOf(29)) {
				if (isKind(la, 32 /* "[" */) || isKind(la, 36 /* "(" */)) {
					Bracketed_NT();
				} else {
					Get();
				}
			}
			Expect(37 /* ")" */);
		} else if (isKind(la, 32 /* "[" */)) {
			Get();
			while (StartOf(30)) {
				if (isKind(la, 32 /* "[" */) || isKind(la, 36 /* "(" */)) {
					Bracketed_NT();
				} else {
					Get();
				}
			}
			Expect(33 /* "]" */);
		} else SynErr(64);
	}



	public void Parse() {
		la = new Token();
		la.val = "";
		Get();
		Coco_NT();
		Expect(0);

	}

	// a token's base type
	public static final int[] tBase = {

		-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,
		-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,
		-1,-1,-1,-1,-1,-1,
	};

	private static final boolean[][] set = {
		{_T,_T,_x,_T, _x,_T,_x,_x, _x,_x,_T,_T, _x,_x,_x,_T, _T,_T,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_T, _x,_x,_x},
		{_x,_T,_T,_T, _T,_T,_x,_T, _T,_T,_T,_T, _T,_T,_T,_T, _T,_T,_T,_T, _T,_T,_T,_T, _T,_T,_T,_T, _T,_T,_T,_T, _T,_T,_T,_T, _T,_T,_T,_T, _T,_T,_T,_T, _T,_T,_x},
		{_x,_T,_T,_T, _T,_T,_T,_x, _x,_x,_x,_x, _T,_T,_T,_x, _x,_T,_T,_T, _T,_T,_T,_T, _T,_T,_T,_T, _T,_T,_T,_T, _T,_T,_T,_T, _T,_T,_T,_T, _T,_T,_T,_T, _T,_T,_x},
		{_T,_T,_x,_T, _x,_T,_x,_x, _x,_x,_T,_T, _x,_x,_x,_T, _T,_T,_T,_x, _x,_x,_x,_T, _x,_x,_x,_x, _x,_x,_x,_x, _T,_x,_T,_T, _T,_x,_T,_x, _T,_T,_x,_T, _x,_x,_x},
		{_T,_T,_x,_T, _x,_T,_x,_x, _x,_x,_T,_T, _x,_x,_x,_T, _T,_T,_x,_T, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_T, _x,_x,_x},
		{_T,_T,_x,_T, _x,_T,_x,_x, _x,_x,_T,_T, _x,_x,_x,_T, _T,_T,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_T, _x,_x,_x},
		{_x,_T,_x,_T, _x,_T,_x,_x, _x,_x,_T,_T, _x,_x,_x,_T, _T,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_T, _x,_x,_x},
		{_x,_T,_x,_T, _x,_T,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _T,_x,_x,_x, _T,_x,_T,_x, _x,_x,_x,_x, _x,_x,_x},
		{_x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_T, _x,_T,_T,_T, _T,_x,_T,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_T,_x,_x, _x,_T,_x,_T, _x,_x,_x,_x, _x,_x,_x},
		{_x,_T,_T,_T, _T,_T,_T,_T, _T,_T,_T,_T, _T,_T,_T,_T, _T,_T,_T,_T, _T,_T,_T,_T, _T,_T,_T,_T, _x,_T,_T,_T, _T,_T,_T,_T, _T,_T,_T,_T, _T,_T,_T,_T, _T,_T,_x},
		{_x,_T,_T,_T, _T,_T,_T,_T, _T,_T,_T,_T, _T,_T,_T,_T, _T,_T,_T,_T, _T,_T,_T,_T, _T,_T,_x,_x, _T,_T,_T,_T, _T,_T,_T,_T, _T,_T,_T,_T, _T,_T,_T,_T, _T,_T,_x},
		{_x,_T,_T,_T, _T,_T,_T,_T, _T,_T,_T,_T, _T,_T,_T,_T, _T,_T,_T,_T, _T,_T,_T,_T, _T,_T,_x,_x, _x,_T,_T,_T, _T,_T,_T,_T, _T,_T,_T,_T, _T,_T,_T,_T, _T,_T,_x},
		{_x,_T,_T,_T, _T,_T,_T,_T, _T,_T,_T,_T, _T,_T,_T,_T, _T,_T,_T,_T, _T,_T,_T,_T, _T,_T,_T,_T, _T,_T,_T,_x, _T,_T,_T,_T, _T,_T,_T,_T, _T,_T,_T,_T, _T,_T,_x},
		{_x,_T,_T,_T, _T,_T,_T,_T, _T,_T,_T,_T, _T,_T,_T,_T, _T,_T,_T,_T, _T,_T,_T,_T, _T,_T,_x,_x, _T,_T,_T,_x, _T,_T,_T,_T, _T,_T,_T,_T, _T,_T,_T,_T, _T,_T,_x},
		{_x,_T,_T,_T, _T,_T,_T,_T, _T,_T,_T,_T, _T,_T,_T,_T, _T,_T,_T,_T, _T,_T,_T,_T, _T,_T,_T,_T, _T,_T,_T,_T, _T,_T,_T,_T, _T,_T,_T,_T, _T,_T,_T,_T, _x,_T,_x},
		{_x,_T,_T,_T, _x,_T,_T,_T, _T,_T,_T,_T, _T,_T,_T,_T, _T,_T,_T,_T, _T,_T,_T,_T, _T,_T,_T,_T, _T,_T,_T,_T, _T,_T,_T,_T, _T,_T,_T,_T, _T,_T,_T,_x, _x,_T,_x},
		{_x,_T,_x,_T, _x,_T,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_T,_x, _x,_x,_x,_T, _x,_x,_x,_x, _x,_x,_x,_x, _T,_T,_T,_T, _T,_T,_T,_T, _T,_T,_x,_T, _x,_x,_x},
		{_x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_T,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_T,_x,_x, _x,_T,_x,_T, _x,_x,_x,_x, _x,_x,_x},
		{_x,_T,_x,_T, _x,_T,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_T, _x,_x,_x,_x, _x,_x,_x,_x, _T,_x,_x,_T, _T,_x,_T,_x, _T,_T,_x,_T, _x,_x,_x},
		{_x,_T,_x,_T, _x,_T,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_T, _x,_x,_x,_x, _x,_x,_x,_x, _T,_x,_x,_T, _T,_x,_T,_x, _T,_x,_x,_T, _x,_x,_x},
		{_x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_T,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_T,_T,_x, _x,_T,_x,_T, _x,_x,_x,_x, _x,_x,_x},
		{_x,_T,_T,_T, _T,_T,_T,_T, _T,_T,_T,_T, _T,_T,_T,_T, _T,_T,_T,_T, _T,_T,_T,_T, _T,_T,_T,_T, _x,_x,_T,_T, _T,_T,_T,_T, _T,_T,_T,_T, _T,_T,_T,_T, _T,_T,_x},
		{_x,_T,_T,_T, _x,_T,_T,_T, _T,_T,_T,_T, _T,_T,_T,_T, _T,_T,_T,_T, _T,_T,_T,_T, _T,_T,_T,_T, _x,_x,_T,_T, _x,_T,_T,_T, _x,_T,_T,_T, _T,_T,_T,_T, _T,_T,_x},
		{_x,_T,_T,_T, _x,_T,_T,_T, _T,_T,_T,_T, _T,_T,_T,_T, _T,_T,_T,_T, _T,_T,_T,_T, _T,_T,_T,_T, _x,_T,_T,_T, _T,_T,_T,_T, _T,_T,_T,_T, _T,_T,_T,_T, _T,_T,_x},
		{_x,_T,_T,_T, _x,_T,_T,_T, _T,_T,_T,_T, _T,_T,_T,_T, _T,_T,_T,_T, _T,_T,_T,_T, _T,_T,_x,_x, _x,_T,_T,_T, _T,_T,_T,_T, _T,_T,_T,_T, _T,_T,_T,_T, _T,_T,_x},
		{_x,_T,_T,_T, _T,_T,_T,_T, _T,_T,_T,_T, _T,_T,_T,_T, _T,_T,_T,_T, _T,_T,_T,_T, _T,_T,_T,_T, _T,_x,_T,_x, _T,_T,_T,_T, _T,_T,_T,_T, _T,_T,_T,_T, _T,_T,_x},
		{_x,_T,_T,_T, _x,_T,_T,_T, _T,_T,_T,_T, _T,_T,_T,_T, _T,_T,_T,_T, _T,_T,_T,_T, _T,_T,_T,_T, _T,_x,_T,_x, _x,_T,_T,_T, _x,_T,_T,_T, _T,_T,_T,_T, _T,_T,_x},
		{_x,_T,_T,_T, _x,_T,_T,_T, _T,_T,_T,_T, _T,_T,_T,_T, _T,_T,_T,_T, _T,_T,_T,_T, _T,_T,_T,_T, _T,_T,_T,_x, _T,_T,_T,_T, _T,_T,_T,_T, _T,_T,_T,_T, _T,_T,_x},
		{_x,_T,_T,_T, _x,_T,_T,_T, _T,_T,_T,_T, _T,_T,_T,_T, _T,_T,_T,_T, _T,_T,_T,_T, _T,_T,_x,_x, _T,_T,_T,_x, _T,_T,_T,_T, _T,_T,_T,_T, _T,_T,_T,_T, _T,_T,_x},
		{_x,_T,_T,_T, _T,_T,_T,_T, _T,_T,_T,_T, _T,_T,_T,_T, _T,_T,_T,_T, _T,_T,_T,_T, _T,_T,_T,_T, _T,_T,_T,_T, _T,_T,_T,_T, _T,_x,_T,_T, _T,_T,_T,_T, _T,_T,_x},
		{_x,_T,_T,_T, _T,_T,_T,_T, _T,_T,_T,_T, _T,_T,_T,_T, _T,_T,_T,_T, _T,_T,_T,_T, _T,_T,_T,_T, _T,_T,_T,_T, _T,_x,_T,_T, _T,_T,_T,_T, _T,_T,_T,_T, _T,_T,_x}

	};
} // end Parser


class Errors {
	public int count = 0;                                    // number of errors detected
	public java.io.PrintStream errorStream = System.out;     // error messages go to this stream
	public String errMsgFormat = "-- line {0} col {1}: {2}"; // 0=line, 1=column, 2=text

	protected void printMsg(int line, int column, String msg) {
		StringBuffer b = new StringBuffer(errMsgFormat);
		int pos = b.indexOf("{0}");
		if (pos >= 0) { b.delete(pos, pos+3); b.insert(pos, line); }
		pos = b.indexOf("{1}");
		if (pos >= 0) { b.delete(pos, pos+3); b.insert(pos, column); }
		pos = b.indexOf("{2}");
		if (pos >= 0) b.replace(pos, pos+3, msg);
		errorStream.println(b.toString());
	}

	public void SynErr (int line, int col, int n) {
		String s;
		switch (n) {
			case 0: s = "EOF expected"; break;
			case 1: s = "ident expected"; break;
			case 2: s = "number expected"; break;
			case 3: s = "string expected"; break;
			case 4: s = "badString expected"; break;
			case 5: s = "char expected"; break;
			case 6: s = "\"COMPILER\" expected"; break;
			case 7: s = "\"IGNORECASE\" expected"; break;
			case 8: s = "\"CHARACTERS\" expected"; break;
			case 9: s = "\"TOKENS\" expected"; break;
			case 10: s = "\"PRAGMAS\" expected"; break;
			case 11: s = "\"COMMENTS\" expected"; break;
			case 12: s = "\"FROM\" expected"; break;
			case 13: s = "\"TO\" expected"; break;
			case 14: s = "\"NESTED\" expected"; break;
			case 15: s = "\"IGNORE\" expected"; break;
			case 16: s = "\"PRODUCTIONS\" expected"; break;
			case 17: s = "\"=\" expected"; break;
			case 18: s = "\".\" expected"; break;
			case 19: s = "\"END\" expected"; break;
			case 20: s = "\"+\" expected"; break;
			case 21: s = "\"-\" expected"; break;
			case 22: s = "\"..\" expected"; break;
			case 23: s = "\"ANY\" expected"; break;
			case 24: s = "\":\" expected"; break;
			case 25: s = "\"<\" expected"; break;
			case 26: s = "\"^\" expected"; break;
			case 27: s = "\"out\" expected"; break;
			case 28: s = "\">\" expected"; break;
			case 29: s = "\",\" expected"; break;
			case 30: s = "\"<.\" expected"; break;
			case 31: s = "\".>\" expected"; break;
			case 32: s = "\"[\" expected"; break;
			case 33: s = "\"]\" expected"; break;
			case 34: s = "\"|\" expected"; break;
			case 35: s = "\"WEAK\" expected"; break;
			case 36: s = "\"(\" expected"; break;
			case 37: s = "\")\" expected"; break;
			case 38: s = "\"{\" expected"; break;
			case 39: s = "\"}\" expected"; break;
			case 40: s = "\"SYNC\" expected"; break;
			case 41: s = "\"IF\" expected"; break;
			case 42: s = "\"CONTEXT\" expected"; break;
			case 43: s = "\"(.\" expected"; break;
			case 44: s = "\".)\" expected"; break;
			case 45: s = "??? expected"; break;
			case 46: s = "this symbol not expected in Coco"; break;
			case 47: s = "this symbol not expected in TokenDecl"; break;
			case 48: s = "invalid TokenDecl"; break;
			case 49: s = "invalid AttrDecl"; break;
			case 50: s = "invalid AttrDecl"; break;
			case 51: s = "invalid AttrDecl"; break;
			case 52: s = "invalid AttrDecl"; break;
			case 53: s = "invalid AttrDecl"; break;
			case 54: s = "invalid SimSet"; break;
			case 55: s = "invalid Sym"; break;
			case 56: s = "invalid Term"; break;
			case 57: s = "invalid Factor"; break;
			case 58: s = "invalid Attribs"; break;
			case 59: s = "invalid Attribs"; break;
			case 60: s = "invalid Attribs"; break;
			case 61: s = "invalid Attribs"; break;
			case 62: s = "invalid Attribs"; break;
			case 63: s = "invalid TokenFactor"; break;
			case 64: s = "invalid Bracketed"; break;
			default: s = "error " + n; break;
		}
		printMsg(line, col, s);
		count++;
	}

	public void SemErr (int line, int col, String s) {
		printMsg(line, col, s);
		count++;
	}

	public void SemErr (String s) {
		errorStream.println(s);
		count++;
	}

	public void Warning (int line, int col, String s) {
		printMsg(line, col, s);
	}

	public void Warning (String s) {
		errorStream.println(s);
	}
} // Errors


class FatalError extends RuntimeException {
	public static final long serialVersionUID = 1L;
	public FatalError(String s) { super(s); }
}
