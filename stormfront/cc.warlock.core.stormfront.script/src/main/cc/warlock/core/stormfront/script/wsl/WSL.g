grammar WSL;

options { backtrack=true; memoize=true; }

@parser::header {
package cc.warlock.core.stormfront.script.wsl.internal;
import java.util.ArrayList;
import cc.warlock.core.stormfront.script.wsl.WSLScript;
import cc.warlock.core.stormfront.script.wsl.internal.WSLString;
import cc.warlock.core.stormfront.script.wsl.internal.WSLVariable;
import cc.warlock.core.stormfront.script.wsl.internal.WSLEqualityCondition.EqualityOperator;
import cc.warlock.core.stormfront.script.wsl.internal.WSLRelationalCondition.RelationalOperator;
}

@lexer::header {
package cc.warlock.core.stormfront.script.wsl.internal;
}

@parser::members {
	private WSLScript script;
	private int lineNum = 0;
	private int actionDepth = 0;
	public void setScript(WSLScript s) { script = s; }
	private boolean isNumber(String str) {
		try {
			Double.parseDouble(str);
			return true;
		} catch(NumberFormatException e) {
			return false;
		}
	}
	@Override
	public void reportError(RecognitionException ex) {
		script.getCommands().echo("Line: " + ex.line + ":" + ex.charPositionInLine + ": " + ex.toString());
		script.stop();
	}
}

@lexer::members {
	private boolean atStart = true;	
}

script
	: line (EOL line)* EOF
	;

line
	: (label=LABEL)? (c=expr)?
		{
			script.addLine(c);
			if(label != null) {
				int existingLine = script.labelLineNumber($label.text);
				if(existingLine != -1)
					script.getCommands().echo("Redefinition of label \"" + $label.text + "\" on line " + lineNum + ", originally defined on line " + existingLine);
				script.addLabel($label.text, lineNum);
			}
			lineNum++;
		}
	;

expr returns [WSLAbstractCommand command]
	: (IF)=> IF BLANK* cond=conditionalExpression BLANK* THEN BLANK* c=expr
		{
			command = new WSLCondition(lineNum, script, cond, c);
		}
	| (ACTION)=> ACTION BLANK* { actionDepth++; }
		( (REMOVE)=> REMOVE BLANK* { actionDepth--; } args=string_list
			{
				command = new WSLActionRemove(lineNum, script, args);
			}
		| (CLEAR)=> CLEAR BLANK*
			{
				command = new WSLActionClear(lineNum, script);
				actionDepth--;
			}
		| c=expr WHEN BLANK* { actionDepth--; } args=string_list
			{
				// FIXME: Variables in actions can't be "when"
				command = new WSLAction(lineNum, script, c, args);
			}
		)
	| (INSTANT)=> INSTANT BLANK* c=expr
		{
			command = c;
			command.setInstant(true);
		}
	| args=string_list
		{
			command = new WSLCommand(lineNum, script, args);
		}
	;

conditionalExpression returns [IWSLValue cond] @init { ArrayList<IWSLValue> args = null; }
	: arg=conditionalAndExpression { args = null; }
		(BLANK* OR BLANK* argNext=conditionalAndExpression
			{
				if(args == null) {
					args = new ArrayList<IWSLValue>();
					args.add(arg);
				}
				args.add(argNext);
			}
		)*
			{
				if(args == null)
					cond = arg;
				else
					cond = new WSLOrCondition(args);
			}
	;

conditionalAndExpression returns [IWSLValue cond] @init { ArrayList<IWSLValue> args = null; }
	: arg=equalityExpression
		(BLANK* AND BLANK* argNext=equalityExpression
			{
				if(args == null) {
					args = new ArrayList<IWSLValue>();
					args.add(arg);
				}
				args.add(argNext);
			}
		)*
		{
			if(args == null)
				cond = arg;
			else
				cond = new WSLAndCondition(args);
		}
	;

equalityExpression returns [IWSLValue cond]
	@init {
			ArrayList<IWSLValue> args = null;
			ArrayList<EqualityOperator> ops = null;
		}
	: arg=relationalExpression { args = null; ops = null; }
		(BLANK* op=equalityOp BLANK* argNext=relationalExpression
			{
				if(args == null) {
					args = new ArrayList<IWSLValue>();
					args.add(arg);
				}
				args.add(argNext);
				if(ops == null) {
					ops = new ArrayList<EqualityOperator>();
				}
				ops.add(op);
			}
		)*
			{
				if(args == null)
					cond = arg;
				else
					cond = new WSLEqualityCondition(args, ops);
			}
	;

equalityOp returns [EqualityOperator op]
	: EQUAL			{ op = EqualityOperator.equals; }
	| NOTEQUAL		{ op = EqualityOperator.notequals; }
	;
	
relationalExpression returns [IWSLValue cond]
	@init {
			ArrayList<IWSLValue> args = null;
			ArrayList<RelationalOperator> ops = null;
		}
	: arg=unaryExpression { args = null; ops = null; }
		(BLANK* op=relationalOp BLANK* argNext=unaryExpression
			{
				if(args == null) {
					args = new ArrayList<IWSLValue>();
					args.add(arg);
				}
				args.add(argNext);
				if(ops == null) {
					ops = new ArrayList<RelationalOperator>();
				}
				ops.add(op);
			}
		)*
			{
				if(args == null)
					cond = arg;
				else
					cond = new WSLRelationalCondition(args, ops);
			}
	;

relationalOp returns [RelationalOperator op]
	: GT		{ op = RelationalOperator.GreaterThan; }
	| LT		{ op = RelationalOperator.LessThan; }
	| GTE		{ op = RelationalOperator.GreaterThanEqualTo; }
	| LTE		{ op = RelationalOperator.LessThanEqualTo; }
	| CONTAINS	{ op = RelationalOperator.Contains; }
	| CONTAINSRE { op = RelationalOperator.ContainsRe; }
	;

unaryExpression returns [IWSLValue cond]
	: NOT BLANK* arg=unaryExpression		{ cond = new WSLNotCondition(arg); }
	| EXISTS BLANK* arg=unaryExpression		{ cond = new WSLExistsCondition(arg); }
	| arg=primaryExpression					{ cond = arg; }
	;

parenExpression returns [IWSLValue cond]
	: LPAREN BLANK* arg=conditionalExpression BLANK* RPAREN		{ cond = arg; }
	;

primaryExpression returns [IWSLValue cond]
	: arg=parenExpression	{ cond = arg; }
	| (v=cond_value)			{ cond = v; }
	;
	
cond_value returns [IWSLValue value]
	: PERCENT (str=common_string
			{ value = new WSLVariable(new WSLString(str)); } PERCENT?
		| v=escaped_var { value = new WSLVariable(v); })
	| DOLLAR (str=common_string
			{ value = new WSLLocalVariable(new WSLString(str)); } DOLLAR?
		| v=escaped_var { value = new WSLLocalVariable(v); })
	| val=number		{ value = val; }
	| TRUE				{ value = new WSLBoolean(true); }
	| FALSE				{ value = new WSLBoolean(false); }
	| val=quoted_string	{ value = val; }
	;

number returns [IWSLValue value]
	: { isNumber(input.LT(1).getText()) }? v=STRING	{ value = new WSLNumber($v.text); }
	;

string_list returns [IWSLValue value]
	: l=string_list_helper
			{
				if(l.size() > 1)
					value = new WSLList(l);
				else
					value = l.get(0);
			}
	;

string_list_helper returns [ArrayList<IWSLValue> list]
	: data=string_value (l=string_list_helper)?
		{
			if(l == null) {
				list = new ArrayList<IWSLValue>();
				list.add(data);
			} else {
				list = l;
				list.add(0, data);
			}
		}
	;

string_value returns [IWSLValue value]
	: ((PERCENT | DOLLAR) ~(EOL|PERCENT|DOLLAR|BLANK|EOF))=> v=variable { value = v; }
	| str=text_string	{ value = new WSLString(str); }
	| t=QUOTE			{ value = new WSLString($t.text); }
	;

quoted_string returns [IWSLValue value]
	: QUOTE l=quoted_string_helper QUOTE
		{
			if(l.size() > 1)
				value = new WSLList(l);
			else
				value = l.get(0);
		}
	;

quoted_string_helper returns [ArrayList<IWSLValue> list]
	: data=quoted_string_value (l=quoted_string_helper)?
		{
			if(l == null) {
				list = new ArrayList<IWSLValue>();
				list.add(data);
			} else {
				list = l;
				list.add(0, data);
			}
		}
	;

quoted_string_value returns [IWSLValue value]
	: ((PERCENT | DOLLAR) ~(EOL|PERCENT|DOLLAR|BLANK|EOF))=> v=variable { value = v; }
	| (BACKSLASH QUOTE)=> BACKSLASH t=QUOTE { value = new WSLString($t.text); }
	| str=text_string { value = new WSLString(str); }
	;

common_string returns [String value]
	: (str=STRING | str=IF | str=THEN | str=OR | str=AND | str=NOTEQUAL
		| str=NOT | str=EQUAL | str=GTE | str=LTE | str=GT | str=LT  | str=AMP
		| str=VERT | str=EXISTS | str=CONTAINS | str=CONTAINSRE | str=ACTION | str=TRUE
		| str=FALSE | { actionDepth == 0 }? str=WHEN | str=REMOVE | str=CLEAR
		| str=INSTANT | str=BACKSLASH)
		{ value = $str.text; }
	;

text_string returns [String value]
	: ((PERCENT PERCENT)=> PERCENT t=PERCENT
		| (DOLLAR DOLLAR)=> DOLLAR t=DOLLAR
		| t=PERCENT | t=DOLLAR | t=RPAREN | t=LPAREN | t=BLANK)
		{ value = $t.text; }
	| str=common_string { value = str; }
	;

variable returns [IWSLValue value]
	: PERCENT (str=escaped_var | str=variable_string PERCENT?)
		{ value = new WSLVariable(str); }
	| DOLLAR (str=escaped_var | str=variable_string DOLLAR?)
		{ value = new WSLLocalVariable(str); }
	;

variable_string returns [IWSLValue value]
	: str=variable_string_helper
		{
			value = new WSLString(str.toString());
		}
	;

variable_string_helper returns [StringBuffer value]
	: str=common_string rest=variable_string_helper?
		{
			if(rest == null) {
				value = new StringBuffer(str);
			} else {
				value = rest;
				value.insert(0, str);
			}
		}
	;

escaped_var returns [IWSLValue value]
	: LPAREN str=vstring_list RPAREN
		{ value = str; }
	;

vstring_list returns [IWSLValue value]
	: l=vstring_list_helper
		{
			if(l.size() > 1)
				value = new WSLList(l);
			else
				value = l.get(0);
		}
	;

vstring_list_helper returns [ArrayList<IWSLValue> list]
	: data=vstring_value (l=vstring_list_helper)?
		{
			if(l == null) {
				list = new ArrayList<IWSLValue>();
				list.add(data);
			} else {
				list = l;
				list.add(0, data);
			}
		}
	;

vstring_value returns [IWSLValue value]
	: ((PERCENT | DOLLAR) ~(EOL|PERCENT|DOLLAR|BLANK|EOF))=> v=variable { value = v; }
	| ((PERCENT PERCENT)=> PERCENT t=PERCENT
		| (DOLLAR DOLLAR)=> DOLLAR t=DOLLAR
		| t=QUOTE | (BACKSLASH LPAREN)=> BACKSLASH t=LPAREN
		| (BACKSLASH RPAREN)=> BACKSLASH t=RPAREN | t=BLANK)
		{ value = new WSLString($t.text); }
	| str=common_string { value = new WSLString(str); }
	;


IF
	: 'if' { atStart = false; }
	;
THEN
	: 'then' { atStart = false; }
	;
OR
	: ('or' | '||') { atStart = false; }
	;
AND
	: ('and' | '&&') { atStart = false; }
	;
NOTEQUAL
	: ('!=' | '<>') { atStart = false; }
	;
NOT
	: ('not' | '!') { atStart = false; }
	;
EQUAL
	: ('==' | '=') { atStart = false; }
	;
GTE
	: '>=' { atStart = false; }
	;
LTE
	: '<=' { atStart = false; }
	;
GT
	: '>' { atStart = false; }
	;
LT
	: '<' { atStart = false; }
	;
LPAREN
	: '(' { atStart = false; }
	;
RPAREN
	: ')' { atStart = false; }
	;
EXISTS
	: 'exists' { atStart = false; }
	;
CONTAINS
	: ('contains' | 'indexof') { atStart = false; }
	;
CONTAINSRE
	: 'containsre' { atStart = false; }
	;
ACTION
	: 'action' { atStart = false; }
	;
WHEN
	: 'when' { atStart = false; }
	;
REMOVE
	: 'remove' { atStart = false; }
	;
CLEAR
	: 'clear' { atStart = false; }
	;
TRUE
	: 'true' { atStart = false; }
	;
FALSE
	: 'false' { atStart = false; }
	;
INSTANT
	: 'instant' {atStart = false; }
	;
QUOTE
	: '"' { atStart = false; }
	;
BLANK
	: (' ' | '\t')+			{ if(atStart) $channel = HIDDEN; }
	;
EOL
	: ('\r'? '\n' | '\r')	{ atStart = true; }
	;
PERCENT
	: '%'					{ atStart = false; }
	;
DOLLAR
	: '$'					{ atStart = false; }
	;
AMP
	: '&'					{ atStart = false; }
	;
VERT
	: '|'					{ atStart = false; }
	;

STRING
	: (~('%'|'$'|'\\'|'"'|'!'|'='|'>'|'<'|'('|')'|'&'|'|'|WS))+  { atStart = false; }
	;
BACKSLASH
    : '\\' { atStart = false; }
	;
LABEL
	: { atStart }?=> (LABEL_STRING ':')=> label=LABEL_STRING ':' { setText($label.text); atStart = false; }
	;
COMMENT
	: { atStart }?=> (~(WORD_CHAR|WS|'$'|'%'|'\\'))=> ~(WORD_CHAR|WS|'$'|'%'|'\\') (~('\n'|'\r'))* { $channel = HIDDEN; }
	;

fragment WS
	: ' ' | '\t' | '\n' | '\r'
	;
fragment WORD_CHAR
	: ('a'..'z'|'0'..'9'|'_')
	;
fragment LABEL_STRING
	: (~(WS|':'))+
	;
