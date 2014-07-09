// $ANTLR 3.2 Sep 23, 2009 12:02:23 /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g 2013-02-19 19:00:41

	package cc.warlock.core.stormfront.script.wsl;
	import java.util.ArrayList;
	import cc.warlock.core.stormfront.script.wsl.WSLEqualityCondition.EqualityOperator;
	import cc.warlock.core.stormfront.script.wsl.WSLRelationalCondition.RelationalOperator;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
public class WSLParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "EOL", "LABEL", "IF", "BLANK", "THEN", "ACTION", "REMOVE", "CLEAR", "WHEN", "INSTANT", "OR", "AND", "EQUAL", "NOTEQUAL", "GT", "LT", "GTE", "LTE", "CONTAINS", "CONTAINSRE", "NOT", "EXISTS", "LPAREN", "RPAREN", "PERCENT", "DOLLAR", "TRUE", "FALSE", "STRING", "QUOTE", "BACKSLASH", "AMP", "VERT", "WS", "LABEL_STRING", "WORD_CHAR", "COMMENT"
    };
    public static final int DOLLAR=29;
    public static final int LT=19;
    public static final int VERT=36;
    public static final int PERCENT=28;
    public static final int INSTANT=13;
    public static final int WORD_CHAR=39;
    public static final int AMP=35;
    public static final int CONTAINS=22;
    public static final int GTE=20;
    public static final int BLANK=7;
    public static final int REMOVE=10;
    public static final int LABEL_STRING=38;
    public static final int NOT=24;
    public static final int AND=15;
    public static final int EOF=-1;
    public static final int TRUE=30;
    public static final int LTE=21;
    public static final int ACTION=9;
    public static final int LPAREN=26;
    public static final int IF=6;
    public static final int CLEAR=11;
    public static final int NOTEQUAL=17;
    public static final int QUOTE=33;
    public static final int RPAREN=27;
    public static final int WS=37;
    public static final int EOL=4;
    public static final int THEN=8;
    public static final int LABEL=5;
    public static final int WHEN=12;
    public static final int CONTAINSRE=23;
    public static final int EQUAL=16;
    public static final int OR=14;
    public static final int GT=18;
    public static final int EXISTS=25;
    public static final int COMMENT=40;
    public static final int FALSE=31;
    public static final int STRING=32;
    public static final int BACKSLASH=34;

    // delegates
    // delegators


        public WSLParser(TokenStream input) {
            this(input, new RecognizerSharedState());
        }
        public WSLParser(TokenStream input, RecognizerSharedState state) {
            super(input, state);
            this.state.ruleMemo = new HashMap[145+1];
             
             
        }
        

    public String[] getTokenNames() { return WSLParser.tokenNames; }
    public String getGrammarFileName() { return "/home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g"; }


    	private WSLScript script;
    	private int lineNum = 1;
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



    // $ANTLR start "script"
    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:40:1: script : line ( EOL line )* EOF ;
    public final void script() throws RecognitionException {
        int script_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 1) ) { return ; }
            // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:41:2: ( line ( EOL line )* EOF )
            // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:41:4: line ( EOL line )* EOF
            {
            pushFollow(FOLLOW_line_in_script62);
            line();

            state._fsp--;
            if (state.failed) return ;
            // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:41:9: ( EOL line )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( (LA1_0==EOL) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:41:10: EOL line
            	    {
            	    match(input,EOL,FOLLOW_EOL_in_script65); if (state.failed) return ;
            	    pushFollow(FOLLOW_line_in_script67);
            	    line();

            	    state._fsp--;
            	    if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    break loop1;
                }
            } while (true);

            match(input,EOF,FOLLOW_EOF_in_script71); if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 1, script_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "script"


    // $ANTLR start "line"
    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:44:1: line : (label= LABEL )? (c= expr )? ;
    public final void line() throws RecognitionException {
        int line_StartIndex = input.index();
        Token label=null;
        WSLAbstractCommand c = null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 2) ) { return ; }
            // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:45:2: ( (label= LABEL )? (c= expr )? )
            // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:45:4: (label= LABEL )? (c= expr )?
            {
            // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:45:4: (label= LABEL )?
            int alt2=2;
            int LA2_0 = input.LA(1);

            if ( (LA2_0==LABEL) ) {
                alt2=1;
            }
            switch (alt2) {
                case 1 :
                    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:45:5: label= LABEL
                    {
                    label=(Token)match(input,LABEL,FOLLOW_LABEL_in_line85); if (state.failed) return ;

                    }
                    break;

            }

            // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:45:19: (c= expr )?
            int alt3=2;
            int LA3_0 = input.LA(1);

            if ( ((LA3_0>=IF && LA3_0<=VERT)) ) {
                alt3=1;
            }
            switch (alt3) {
                case 1 :
                    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:45:20: c= expr
                    {
                    pushFollow(FOLLOW_expr_in_line92);
                    c=expr();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;

            }

            if ( state.backtracking==0 ) {

              			script.addCommand(c);
              			if(label != null) {
              				int existingLine = script.labelLineNumber((label!=null?label.getText():null));
              				if(existingLine != -1)
              					script.scriptDebug(1, "Redefinition of label \"" + (label!=null?label.getText():null) + "\" on line " + lineNum + ", originally defined on line " + existingLine);
              				script.addLabel((label!=null?label.getText():null), lineNum);
              			}
              			lineNum++;
              		
            }

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 2, line_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "line"


    // $ANTLR start "expr"
    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:58:1: expr returns [WSLAbstractCommand command] : ( ( IF )=> IF ( BLANK )* cond= conditionalExpression ( BLANK )* THEN ( BLANK )* c= expr | ( ACTION )=> ACTION ( BLANK )* ( ( REMOVE )=> REMOVE ( BLANK )* args= string_list | ( CLEAR )=> CLEAR ( BLANK )* | c= expr WHEN ( BLANK )* args= string_list ) | ( INSTANT )=> INSTANT ( BLANK )* c= expr | args= string_list );
    public final WSLAbstractCommand expr() throws RecognitionException {
        WSLAbstractCommand command = null;
        int expr_StartIndex = input.index();
        IWSLValue cond = null;

        WSLAbstractCommand c = null;

        IWSLValue args = null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 3) ) { return command; }
            // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:59:2: ( ( IF )=> IF ( BLANK )* cond= conditionalExpression ( BLANK )* THEN ( BLANK )* c= expr | ( ACTION )=> ACTION ( BLANK )* ( ( REMOVE )=> REMOVE ( BLANK )* args= string_list | ( CLEAR )=> CLEAR ( BLANK )* | c= expr WHEN ( BLANK )* args= string_list ) | ( INSTANT )=> INSTANT ( BLANK )* c= expr | args= string_list )
            int alt13=4;
            alt13 = dfa13.predict(input);
            switch (alt13) {
                case 1 :
                    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:59:4: ( IF )=> IF ( BLANK )* cond= conditionalExpression ( BLANK )* THEN ( BLANK )* c= expr
                    {
                    match(input,IF,FOLLOW_IF_in_expr118); if (state.failed) return command;
                    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:59:14: ( BLANK )*
                    loop4:
                    do {
                        int alt4=2;
                        int LA4_0 = input.LA(1);

                        if ( (LA4_0==BLANK) ) {
                            alt4=1;
                        }


                        switch (alt4) {
                    	case 1 :
                    	    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:0:0: BLANK
                    	    {
                    	    match(input,BLANK,FOLLOW_BLANK_in_expr120); if (state.failed) return command;

                    	    }
                    	    break;

                    	default :
                    	    break loop4;
                        }
                    } while (true);

                    pushFollow(FOLLOW_conditionalExpression_in_expr125);
                    cond=conditionalExpression();

                    state._fsp--;
                    if (state.failed) return command;
                    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:59:48: ( BLANK )*
                    loop5:
                    do {
                        int alt5=2;
                        int LA5_0 = input.LA(1);

                        if ( (LA5_0==BLANK) ) {
                            alt5=1;
                        }


                        switch (alt5) {
                    	case 1 :
                    	    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:0:0: BLANK
                    	    {
                    	    match(input,BLANK,FOLLOW_BLANK_in_expr127); if (state.failed) return command;

                    	    }
                    	    break;

                    	default :
                    	    break loop5;
                        }
                    } while (true);

                    match(input,THEN,FOLLOW_THEN_in_expr130); if (state.failed) return command;
                    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:59:60: ( BLANK )*
                    loop6:
                    do {
                        int alt6=2;
                        alt6 = dfa6.predict(input);
                        switch (alt6) {
                    	case 1 :
                    	    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:0:0: BLANK
                    	    {
                    	    match(input,BLANK,FOLLOW_BLANK_in_expr132); if (state.failed) return command;

                    	    }
                    	    break;

                    	default :
                    	    break loop6;
                        }
                    } while (true);

                    pushFollow(FOLLOW_expr_in_expr137);
                    c=expr();

                    state._fsp--;
                    if (state.failed) return command;
                    if ( state.backtracking==0 ) {

                      			command = new WSLCondition(lineNum, script, cond, c);
                      		
                    }

                    }
                    break;
                case 2 :
                    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:63:4: ( ACTION )=> ACTION ( BLANK )* ( ( REMOVE )=> REMOVE ( BLANK )* args= string_list | ( CLEAR )=> CLEAR ( BLANK )* | c= expr WHEN ( BLANK )* args= string_list )
                    {
                    match(input,ACTION,FOLLOW_ACTION_in_expr151); if (state.failed) return command;
                    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:63:22: ( BLANK )*
                    loop7:
                    do {
                        int alt7=2;
                        alt7 = dfa7.predict(input);
                        switch (alt7) {
                    	case 1 :
                    	    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:0:0: BLANK
                    	    {
                    	    match(input,BLANK,FOLLOW_BLANK_in_expr153); if (state.failed) return command;

                    	    }
                    	    break;

                    	default :
                    	    break loop7;
                        }
                    } while (true);

                    if ( state.backtracking==0 ) {
                       actionDepth++; 
                    }
                    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:64:3: ( ( REMOVE )=> REMOVE ( BLANK )* args= string_list | ( CLEAR )=> CLEAR ( BLANK )* | c= expr WHEN ( BLANK )* args= string_list )
                    int alt11=3;
                    alt11 = dfa11.predict(input);
                    switch (alt11) {
                        case 1 :
                            // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:64:5: ( REMOVE )=> REMOVE ( BLANK )* args= string_list
                            {
                            match(input,REMOVE,FOLLOW_REMOVE_in_expr167); if (state.failed) return command;
                            // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:64:23: ( BLANK )*
                            loop8:
                            do {
                                int alt8=2;
                                alt8 = dfa8.predict(input);
                                switch (alt8) {
                            	case 1 :
                            	    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:0:0: BLANK
                            	    {
                            	    match(input,BLANK,FOLLOW_BLANK_in_expr169); if (state.failed) return command;

                            	    }
                            	    break;

                            	default :
                            	    break loop8;
                                }
                            } while (true);

                            if ( state.backtracking==0 ) {
                               actionDepth--; 
                            }
                            pushFollow(FOLLOW_string_list_in_expr176);
                            args=string_list();

                            state._fsp--;
                            if (state.failed) return command;
                            if ( state.backtracking==0 ) {

                              				command = new WSLActionRemove(lineNum, script, args);
                              			
                            }

                            }
                            break;
                        case 2 :
                            // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:68:5: ( CLEAR )=> CLEAR ( BLANK )*
                            {
                            match(input,CLEAR,FOLLOW_CLEAR_in_expr192); if (state.failed) return command;
                            // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:68:21: ( BLANK )*
                            loop9:
                            do {
                                int alt9=2;
                                int LA9_0 = input.LA(1);

                                if ( (LA9_0==BLANK) ) {
                                    alt9=1;
                                }


                                switch (alt9) {
                            	case 1 :
                            	    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:0:0: BLANK
                            	    {
                            	    match(input,BLANK,FOLLOW_BLANK_in_expr194); if (state.failed) return command;

                            	    }
                            	    break;

                            	default :
                            	    break loop9;
                                }
                            } while (true);

                            if ( state.backtracking==0 ) {

                              				command = new WSLActionClear(lineNum, script);
                              				actionDepth--;
                              			
                            }

                            }
                            break;
                        case 3 :
                            // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:73:5: c= expr WHEN ( BLANK )* args= string_list
                            {
                            pushFollow(FOLLOW_expr_in_expr208);
                            c=expr();

                            state._fsp--;
                            if (state.failed) return command;
                            match(input,WHEN,FOLLOW_WHEN_in_expr210); if (state.failed) return command;
                            // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:73:17: ( BLANK )*
                            loop10:
                            do {
                                int alt10=2;
                                alt10 = dfa10.predict(input);
                                switch (alt10) {
                            	case 1 :
                            	    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:0:0: BLANK
                            	    {
                            	    match(input,BLANK,FOLLOW_BLANK_in_expr212); if (state.failed) return command;

                            	    }
                            	    break;

                            	default :
                            	    break loop10;
                                }
                            } while (true);

                            if ( state.backtracking==0 ) {
                               actionDepth--; 
                            }
                            pushFollow(FOLLOW_string_list_in_expr219);
                            args=string_list();

                            state._fsp--;
                            if (state.failed) return command;
                            if ( state.backtracking==0 ) {

                              				// FIXME: Variables in actions can't be "when"
                              				command = new WSLAction(lineNum, script, c, args);
                              			
                            }

                            }
                            break;

                    }


                    }
                    break;
                case 3 :
                    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:79:4: ( INSTANT )=> INSTANT ( BLANK )* c= expr
                    {
                    match(input,INSTANT,FOLLOW_INSTANT_in_expr238); if (state.failed) return command;
                    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:79:24: ( BLANK )*
                    loop12:
                    do {
                        int alt12=2;
                        alt12 = dfa12.predict(input);
                        switch (alt12) {
                    	case 1 :
                    	    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:0:0: BLANK
                    	    {
                    	    match(input,BLANK,FOLLOW_BLANK_in_expr240); if (state.failed) return command;

                    	    }
                    	    break;

                    	default :
                    	    break loop12;
                        }
                    } while (true);

                    pushFollow(FOLLOW_expr_in_expr245);
                    c=expr();

                    state._fsp--;
                    if (state.failed) return command;
                    if ( state.backtracking==0 ) {

                      			command = c;
                      			command.setInstant(true);
                      		
                    }

                    }
                    break;
                case 4 :
                    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:84:4: args= string_list
                    {
                    pushFollow(FOLLOW_string_list_in_expr256);
                    args=string_list();

                    state._fsp--;
                    if (state.failed) return command;
                    if ( state.backtracking==0 ) {

                      			command = new WSLCommand(lineNum, script, args);
                      		
                    }

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 3, expr_StartIndex); }
        }
        return command;
    }
    // $ANTLR end "expr"


    // $ANTLR start "conditionalExpression"
    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:90:1: conditionalExpression returns [IWSLValue cond] : arg= conditionalAndExpression ( ( BLANK )* OR ( BLANK )* argNext= conditionalAndExpression )* ;
    public final IWSLValue conditionalExpression() throws RecognitionException {
        IWSLValue cond = null;
        int conditionalExpression_StartIndex = input.index();
        IWSLValue arg = null;

        IWSLValue argNext = null;


         ArrayList<IWSLValue> args = null; 
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 4) ) { return cond; }
            // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:91:2: (arg= conditionalAndExpression ( ( BLANK )* OR ( BLANK )* argNext= conditionalAndExpression )* )
            // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:91:4: arg= conditionalAndExpression ( ( BLANK )* OR ( BLANK )* argNext= conditionalAndExpression )*
            {
            pushFollow(FOLLOW_conditionalAndExpression_in_conditionalExpression282);
            arg=conditionalAndExpression();

            state._fsp--;
            if (state.failed) return cond;
            if ( state.backtracking==0 ) {
               args = null; 
            }
            // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:92:3: ( ( BLANK )* OR ( BLANK )* argNext= conditionalAndExpression )*
            loop16:
            do {
                int alt16=2;
                alt16 = dfa16.predict(input);
                switch (alt16) {
            	case 1 :
            	    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:92:4: ( BLANK )* OR ( BLANK )* argNext= conditionalAndExpression
            	    {
            	    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:92:4: ( BLANK )*
            	    loop14:
            	    do {
            	        int alt14=2;
            	        int LA14_0 = input.LA(1);

            	        if ( (LA14_0==BLANK) ) {
            	            alt14=1;
            	        }


            	        switch (alt14) {
            	    	case 1 :
            	    	    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:0:0: BLANK
            	    	    {
            	    	    match(input,BLANK,FOLLOW_BLANK_in_conditionalExpression289); if (state.failed) return cond;

            	    	    }
            	    	    break;

            	    	default :
            	    	    break loop14;
            	        }
            	    } while (true);

            	    match(input,OR,FOLLOW_OR_in_conditionalExpression292); if (state.failed) return cond;
            	    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:92:14: ( BLANK )*
            	    loop15:
            	    do {
            	        int alt15=2;
            	        int LA15_0 = input.LA(1);

            	        if ( (LA15_0==BLANK) ) {
            	            alt15=1;
            	        }


            	        switch (alt15) {
            	    	case 1 :
            	    	    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:0:0: BLANK
            	    	    {
            	    	    match(input,BLANK,FOLLOW_BLANK_in_conditionalExpression294); if (state.failed) return cond;

            	    	    }
            	    	    break;

            	    	default :
            	    	    break loop15;
            	        }
            	    } while (true);

            	    pushFollow(FOLLOW_conditionalAndExpression_in_conditionalExpression299);
            	    argNext=conditionalAndExpression();

            	    state._fsp--;
            	    if (state.failed) return cond;
            	    if ( state.backtracking==0 ) {

            	      				if(args == null) {
            	      					args = new ArrayList<IWSLValue>();
            	      					args.add(arg);
            	      				}
            	      				args.add(argNext);
            	      			
            	    }

            	    }
            	    break;

            	default :
            	    break loop16;
                }
            } while (true);

            if ( state.backtracking==0 ) {

              				if(args == null)
              					cond = arg;
              				else
              					cond = new WSLOrCondition(args);
              			
            }

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 4, conditionalExpression_StartIndex); }
        }
        return cond;
    }
    // $ANTLR end "conditionalExpression"


    // $ANTLR start "conditionalAndExpression"
    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:109:1: conditionalAndExpression returns [IWSLValue cond] : arg= equalityExpression ( ( BLANK )* AND ( BLANK )* argNext= equalityExpression )* ;
    public final IWSLValue conditionalAndExpression() throws RecognitionException {
        IWSLValue cond = null;
        int conditionalAndExpression_StartIndex = input.index();
        IWSLValue arg = null;

        IWSLValue argNext = null;


         ArrayList<IWSLValue> args = null; 
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 5) ) { return cond; }
            // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:110:2: (arg= equalityExpression ( ( BLANK )* AND ( BLANK )* argNext= equalityExpression )* )
            // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:110:4: arg= equalityExpression ( ( BLANK )* AND ( BLANK )* argNext= equalityExpression )*
            {
            pushFollow(FOLLOW_equalityExpression_in_conditionalAndExpression336);
            arg=equalityExpression();

            state._fsp--;
            if (state.failed) return cond;
            // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:111:3: ( ( BLANK )* AND ( BLANK )* argNext= equalityExpression )*
            loop19:
            do {
                int alt19=2;
                alt19 = dfa19.predict(input);
                switch (alt19) {
            	case 1 :
            	    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:111:4: ( BLANK )* AND ( BLANK )* argNext= equalityExpression
            	    {
            	    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:111:4: ( BLANK )*
            	    loop17:
            	    do {
            	        int alt17=2;
            	        int LA17_0 = input.LA(1);

            	        if ( (LA17_0==BLANK) ) {
            	            alt17=1;
            	        }


            	        switch (alt17) {
            	    	case 1 :
            	    	    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:0:0: BLANK
            	    	    {
            	    	    match(input,BLANK,FOLLOW_BLANK_in_conditionalAndExpression341); if (state.failed) return cond;

            	    	    }
            	    	    break;

            	    	default :
            	    	    break loop17;
            	        }
            	    } while (true);

            	    match(input,AND,FOLLOW_AND_in_conditionalAndExpression344); if (state.failed) return cond;
            	    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:111:15: ( BLANK )*
            	    loop18:
            	    do {
            	        int alt18=2;
            	        int LA18_0 = input.LA(1);

            	        if ( (LA18_0==BLANK) ) {
            	            alt18=1;
            	        }


            	        switch (alt18) {
            	    	case 1 :
            	    	    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:0:0: BLANK
            	    	    {
            	    	    match(input,BLANK,FOLLOW_BLANK_in_conditionalAndExpression346); if (state.failed) return cond;

            	    	    }
            	    	    break;

            	    	default :
            	    	    break loop18;
            	        }
            	    } while (true);

            	    pushFollow(FOLLOW_equalityExpression_in_conditionalAndExpression351);
            	    argNext=equalityExpression();

            	    state._fsp--;
            	    if (state.failed) return cond;
            	    if ( state.backtracking==0 ) {

            	      				if(args == null) {
            	      					args = new ArrayList<IWSLValue>();
            	      					args.add(arg);
            	      				}
            	      				args.add(argNext);
            	      			
            	    }

            	    }
            	    break;

            	default :
            	    break loop19;
                }
            } while (true);

            if ( state.backtracking==0 ) {

              			if(args == null)
              				cond = arg;
              			else
              				cond = new WSLAndCondition(args);
              		
            }

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 5, conditionalAndExpression_StartIndex); }
        }
        return cond;
    }
    // $ANTLR end "conditionalAndExpression"


    // $ANTLR start "equalityExpression"
    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:128:1: equalityExpression returns [IWSLValue cond] : arg= relationalExpression ( ( BLANK )* op= equalityOp ( BLANK )* argNext= relationalExpression )* ;
    public final IWSLValue equalityExpression() throws RecognitionException {
        IWSLValue cond = null;
        int equalityExpression_StartIndex = input.index();
        IWSLValue arg = null;

        EqualityOperator op = null;

        IWSLValue argNext = null;



        			ArrayList<IWSLValue> args = null;
        			ArrayList<EqualityOperator> ops = null;
        		
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 6) ) { return cond; }
            // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:133:2: (arg= relationalExpression ( ( BLANK )* op= equalityOp ( BLANK )* argNext= relationalExpression )* )
            // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:133:4: arg= relationalExpression ( ( BLANK )* op= equalityOp ( BLANK )* argNext= relationalExpression )*
            {
            pushFollow(FOLLOW_relationalExpression_in_equalityExpression388);
            arg=relationalExpression();

            state._fsp--;
            if (state.failed) return cond;
            if ( state.backtracking==0 ) {
               args = null; ops = null; 
            }
            // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:134:3: ( ( BLANK )* op= equalityOp ( BLANK )* argNext= relationalExpression )*
            loop22:
            do {
                int alt22=2;
                alt22 = dfa22.predict(input);
                switch (alt22) {
            	case 1 :
            	    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:134:4: ( BLANK )* op= equalityOp ( BLANK )* argNext= relationalExpression
            	    {
            	    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:134:4: ( BLANK )*
            	    loop20:
            	    do {
            	        int alt20=2;
            	        int LA20_0 = input.LA(1);

            	        if ( (LA20_0==BLANK) ) {
            	            alt20=1;
            	        }


            	        switch (alt20) {
            	    	case 1 :
            	    	    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:0:0: BLANK
            	    	    {
            	    	    match(input,BLANK,FOLLOW_BLANK_in_equalityExpression395); if (state.failed) return cond;

            	    	    }
            	    	    break;

            	    	default :
            	    	    break loop20;
            	        }
            	    } while (true);

            	    pushFollow(FOLLOW_equalityOp_in_equalityExpression400);
            	    op=equalityOp();

            	    state._fsp--;
            	    if (state.failed) return cond;
            	    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:134:25: ( BLANK )*
            	    loop21:
            	    do {
            	        int alt21=2;
            	        int LA21_0 = input.LA(1);

            	        if ( (LA21_0==BLANK) ) {
            	            alt21=1;
            	        }


            	        switch (alt21) {
            	    	case 1 :
            	    	    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:0:0: BLANK
            	    	    {
            	    	    match(input,BLANK,FOLLOW_BLANK_in_equalityExpression402); if (state.failed) return cond;

            	    	    }
            	    	    break;

            	    	default :
            	    	    break loop21;
            	        }
            	    } while (true);

            	    pushFollow(FOLLOW_relationalExpression_in_equalityExpression407);
            	    argNext=relationalExpression();

            	    state._fsp--;
            	    if (state.failed) return cond;
            	    if ( state.backtracking==0 ) {

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

            	    }
            	    break;

            	default :
            	    break loop22;
                }
            } while (true);

            if ( state.backtracking==0 ) {

              				if(args == null)
              					cond = arg;
              				else
              					cond = new WSLEqualityCondition(args, ops);
              			
            }

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 6, equalityExpression_StartIndex); }
        }
        return cond;
    }
    // $ANTLR end "equalityExpression"


    // $ANTLR start "equalityOp"
    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:155:1: equalityOp returns [EqualityOperator op] : ( EQUAL | NOTEQUAL );
    public final EqualityOperator equalityOp() throws RecognitionException {
        EqualityOperator op = null;
        int equalityOp_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 7) ) { return op; }
            // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:156:2: ( EQUAL | NOTEQUAL )
            int alt23=2;
            int LA23_0 = input.LA(1);

            if ( (LA23_0==EQUAL) ) {
                alt23=1;
            }
            else if ( (LA23_0==NOTEQUAL) ) {
                alt23=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return op;}
                NoViableAltException nvae =
                    new NoViableAltException("", 23, 0, input);

                throw nvae;
            }
            switch (alt23) {
                case 1 :
                    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:156:4: EQUAL
                    {
                    match(input,EQUAL,FOLLOW_EQUAL_in_equalityOp437); if (state.failed) return op;
                    if ( state.backtracking==0 ) {
                       op = EqualityOperator.equals; 
                    }

                    }
                    break;
                case 2 :
                    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:157:4: NOTEQUAL
                    {
                    match(input,NOTEQUAL,FOLLOW_NOTEQUAL_in_equalityOp446); if (state.failed) return op;
                    if ( state.backtracking==0 ) {
                       op = EqualityOperator.notequals; 
                    }

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 7, equalityOp_StartIndex); }
        }
        return op;
    }
    // $ANTLR end "equalityOp"


    // $ANTLR start "relationalExpression"
    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:160:1: relationalExpression returns [IWSLValue cond] : arg= unaryExpression ( ( BLANK )* op= relationalOp ( BLANK )* argNext= unaryExpression )* ;
    public final IWSLValue relationalExpression() throws RecognitionException {
        IWSLValue cond = null;
        int relationalExpression_StartIndex = input.index();
        IWSLValue arg = null;

        RelationalOperator op = null;

        IWSLValue argNext = null;



        			ArrayList<IWSLValue> args = null;
        			ArrayList<RelationalOperator> ops = null;
        		
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 8) ) { return cond; }
            // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:165:2: (arg= unaryExpression ( ( BLANK )* op= relationalOp ( BLANK )* argNext= unaryExpression )* )
            // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:165:4: arg= unaryExpression ( ( BLANK )* op= relationalOp ( BLANK )* argNext= unaryExpression )*
            {
            pushFollow(FOLLOW_unaryExpression_in_relationalExpression473);
            arg=unaryExpression();

            state._fsp--;
            if (state.failed) return cond;
            if ( state.backtracking==0 ) {
               args = null; ops = null; 
            }
            // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:166:3: ( ( BLANK )* op= relationalOp ( BLANK )* argNext= unaryExpression )*
            loop26:
            do {
                int alt26=2;
                alt26 = dfa26.predict(input);
                switch (alt26) {
            	case 1 :
            	    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:166:4: ( BLANK )* op= relationalOp ( BLANK )* argNext= unaryExpression
            	    {
            	    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:166:4: ( BLANK )*
            	    loop24:
            	    do {
            	        int alt24=2;
            	        int LA24_0 = input.LA(1);

            	        if ( (LA24_0==BLANK) ) {
            	            alt24=1;
            	        }


            	        switch (alt24) {
            	    	case 1 :
            	    	    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:0:0: BLANK
            	    	    {
            	    	    match(input,BLANK,FOLLOW_BLANK_in_relationalExpression480); if (state.failed) return cond;

            	    	    }
            	    	    break;

            	    	default :
            	    	    break loop24;
            	        }
            	    } while (true);

            	    pushFollow(FOLLOW_relationalOp_in_relationalExpression485);
            	    op=relationalOp();

            	    state._fsp--;
            	    if (state.failed) return cond;
            	    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:166:27: ( BLANK )*
            	    loop25:
            	    do {
            	        int alt25=2;
            	        int LA25_0 = input.LA(1);

            	        if ( (LA25_0==BLANK) ) {
            	            alt25=1;
            	        }


            	        switch (alt25) {
            	    	case 1 :
            	    	    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:0:0: BLANK
            	    	    {
            	    	    match(input,BLANK,FOLLOW_BLANK_in_relationalExpression487); if (state.failed) return cond;

            	    	    }
            	    	    break;

            	    	default :
            	    	    break loop25;
            	        }
            	    } while (true);

            	    pushFollow(FOLLOW_unaryExpression_in_relationalExpression492);
            	    argNext=unaryExpression();

            	    state._fsp--;
            	    if (state.failed) return cond;
            	    if ( state.backtracking==0 ) {

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

            	    }
            	    break;

            	default :
            	    break loop26;
                }
            } while (true);

            if ( state.backtracking==0 ) {

              				if(args == null)
              					cond = arg;
              				else
              					cond = new WSLRelationalCondition(script, args, ops);
              			
            }

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 8, relationalExpression_StartIndex); }
        }
        return cond;
    }
    // $ANTLR end "relationalExpression"


    // $ANTLR start "relationalOp"
    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:187:1: relationalOp returns [RelationalOperator op] : ( GT | LT | GTE | LTE | CONTAINS | CONTAINSRE );
    public final RelationalOperator relationalOp() throws RecognitionException {
        RelationalOperator op = null;
        int relationalOp_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 9) ) { return op; }
            // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:188:2: ( GT | LT | GTE | LTE | CONTAINS | CONTAINSRE )
            int alt27=6;
            switch ( input.LA(1) ) {
            case GT:
                {
                alt27=1;
                }
                break;
            case LT:
                {
                alt27=2;
                }
                break;
            case GTE:
                {
                alt27=3;
                }
                break;
            case LTE:
                {
                alt27=4;
                }
                break;
            case CONTAINS:
                {
                alt27=5;
                }
                break;
            case CONTAINSRE:
                {
                alt27=6;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return op;}
                NoViableAltException nvae =
                    new NoViableAltException("", 27, 0, input);

                throw nvae;
            }

            switch (alt27) {
                case 1 :
                    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:188:4: GT
                    {
                    match(input,GT,FOLLOW_GT_in_relationalOp522); if (state.failed) return op;
                    if ( state.backtracking==0 ) {
                       op = RelationalOperator.GreaterThan; 
                    }

                    }
                    break;
                case 2 :
                    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:189:4: LT
                    {
                    match(input,LT,FOLLOW_LT_in_relationalOp530); if (state.failed) return op;
                    if ( state.backtracking==0 ) {
                       op = RelationalOperator.LessThan; 
                    }

                    }
                    break;
                case 3 :
                    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:190:4: GTE
                    {
                    match(input,GTE,FOLLOW_GTE_in_relationalOp538); if (state.failed) return op;
                    if ( state.backtracking==0 ) {
                       op = RelationalOperator.GreaterThanEqualTo; 
                    }

                    }
                    break;
                case 4 :
                    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:191:4: LTE
                    {
                    match(input,LTE,FOLLOW_LTE_in_relationalOp546); if (state.failed) return op;
                    if ( state.backtracking==0 ) {
                       op = RelationalOperator.LessThanEqualTo; 
                    }

                    }
                    break;
                case 5 :
                    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:192:4: CONTAINS
                    {
                    match(input,CONTAINS,FOLLOW_CONTAINS_in_relationalOp554); if (state.failed) return op;
                    if ( state.backtracking==0 ) {
                       op = RelationalOperator.Contains; 
                    }

                    }
                    break;
                case 6 :
                    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:193:4: CONTAINSRE
                    {
                    match(input,CONTAINSRE,FOLLOW_CONTAINSRE_in_relationalOp561); if (state.failed) return op;
                    if ( state.backtracking==0 ) {
                       op = RelationalOperator.ContainsRe; 
                    }

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 9, relationalOp_StartIndex); }
        }
        return op;
    }
    // $ANTLR end "relationalOp"


    // $ANTLR start "unaryExpression"
    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:196:1: unaryExpression returns [IWSLValue cond] : ( NOT ( BLANK )* arg= unaryExpression | EXISTS ( BLANK )* arg= unaryExpression | arg= primaryExpression );
    public final IWSLValue unaryExpression() throws RecognitionException {
        IWSLValue cond = null;
        int unaryExpression_StartIndex = input.index();
        IWSLValue arg = null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 10) ) { return cond; }
            // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:197:2: ( NOT ( BLANK )* arg= unaryExpression | EXISTS ( BLANK )* arg= unaryExpression | arg= primaryExpression )
            int alt30=3;
            switch ( input.LA(1) ) {
            case NOT:
                {
                alt30=1;
                }
                break;
            case EXISTS:
                {
                alt30=2;
                }
                break;
            case LPAREN:
            case PERCENT:
            case DOLLAR:
            case TRUE:
            case FALSE:
            case STRING:
            case QUOTE:
                {
                alt30=3;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return cond;}
                NoViableAltException nvae =
                    new NoViableAltException("", 30, 0, input);

                throw nvae;
            }

            switch (alt30) {
                case 1 :
                    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:197:4: NOT ( BLANK )* arg= unaryExpression
                    {
                    match(input,NOT,FOLLOW_NOT_in_unaryExpression578); if (state.failed) return cond;
                    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:197:8: ( BLANK )*
                    loop28:
                    do {
                        int alt28=2;
                        int LA28_0 = input.LA(1);

                        if ( (LA28_0==BLANK) ) {
                            alt28=1;
                        }


                        switch (alt28) {
                    	case 1 :
                    	    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:0:0: BLANK
                    	    {
                    	    match(input,BLANK,FOLLOW_BLANK_in_unaryExpression580); if (state.failed) return cond;

                    	    }
                    	    break;

                    	default :
                    	    break loop28;
                        }
                    } while (true);

                    pushFollow(FOLLOW_unaryExpression_in_unaryExpression585);
                    arg=unaryExpression();

                    state._fsp--;
                    if (state.failed) return cond;
                    if ( state.backtracking==0 ) {
                       cond = new WSLNotCondition(arg); 
                    }

                    }
                    break;
                case 2 :
                    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:198:4: EXISTS ( BLANK )* arg= unaryExpression
                    {
                    match(input,EXISTS,FOLLOW_EXISTS_in_unaryExpression593); if (state.failed) return cond;
                    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:198:11: ( BLANK )*
                    loop29:
                    do {
                        int alt29=2;
                        int LA29_0 = input.LA(1);

                        if ( (LA29_0==BLANK) ) {
                            alt29=1;
                        }


                        switch (alt29) {
                    	case 1 :
                    	    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:0:0: BLANK
                    	    {
                    	    match(input,BLANK,FOLLOW_BLANK_in_unaryExpression595); if (state.failed) return cond;

                    	    }
                    	    break;

                    	default :
                    	    break loop29;
                        }
                    } while (true);

                    pushFollow(FOLLOW_unaryExpression_in_unaryExpression600);
                    arg=unaryExpression();

                    state._fsp--;
                    if (state.failed) return cond;
                    if ( state.backtracking==0 ) {
                       cond = new WSLExistsCondition(arg); 
                    }

                    }
                    break;
                case 3 :
                    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:199:4: arg= primaryExpression
                    {
                    pushFollow(FOLLOW_primaryExpression_in_unaryExpression610);
                    arg=primaryExpression();

                    state._fsp--;
                    if (state.failed) return cond;
                    if ( state.backtracking==0 ) {
                       cond = arg; 
                    }

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 10, unaryExpression_StartIndex); }
        }
        return cond;
    }
    // $ANTLR end "unaryExpression"


    // $ANTLR start "parenExpression"
    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:202:1: parenExpression returns [IWSLValue cond] : LPAREN ( BLANK )* arg= conditionalExpression ( BLANK )* RPAREN ;
    public final IWSLValue parenExpression() throws RecognitionException {
        IWSLValue cond = null;
        int parenExpression_StartIndex = input.index();
        IWSLValue arg = null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 11) ) { return cond; }
            // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:203:2: ( LPAREN ( BLANK )* arg= conditionalExpression ( BLANK )* RPAREN )
            // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:203:4: LPAREN ( BLANK )* arg= conditionalExpression ( BLANK )* RPAREN
            {
            match(input,LPAREN,FOLLOW_LPAREN_in_parenExpression631); if (state.failed) return cond;
            // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:203:11: ( BLANK )*
            loop31:
            do {
                int alt31=2;
                int LA31_0 = input.LA(1);

                if ( (LA31_0==BLANK) ) {
                    alt31=1;
                }


                switch (alt31) {
            	case 1 :
            	    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:0:0: BLANK
            	    {
            	    match(input,BLANK,FOLLOW_BLANK_in_parenExpression633); if (state.failed) return cond;

            	    }
            	    break;

            	default :
            	    break loop31;
                }
            } while (true);

            pushFollow(FOLLOW_conditionalExpression_in_parenExpression638);
            arg=conditionalExpression();

            state._fsp--;
            if (state.failed) return cond;
            // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:203:44: ( BLANK )*
            loop32:
            do {
                int alt32=2;
                int LA32_0 = input.LA(1);

                if ( (LA32_0==BLANK) ) {
                    alt32=1;
                }


                switch (alt32) {
            	case 1 :
            	    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:0:0: BLANK
            	    {
            	    match(input,BLANK,FOLLOW_BLANK_in_parenExpression640); if (state.failed) return cond;

            	    }
            	    break;

            	default :
            	    break loop32;
                }
            } while (true);

            match(input,RPAREN,FOLLOW_RPAREN_in_parenExpression643); if (state.failed) return cond;
            if ( state.backtracking==0 ) {
               cond = arg; 
            }

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 11, parenExpression_StartIndex); }
        }
        return cond;
    }
    // $ANTLR end "parenExpression"


    // $ANTLR start "primaryExpression"
    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:206:1: primaryExpression returns [IWSLValue cond] : (arg= parenExpression | (v= cond_value ) );
    public final IWSLValue primaryExpression() throws RecognitionException {
        IWSLValue cond = null;
        int primaryExpression_StartIndex = input.index();
        IWSLValue arg = null;

        IWSLValue v = null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 12) ) { return cond; }
            // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:207:2: (arg= parenExpression | (v= cond_value ) )
            int alt33=2;
            int LA33_0 = input.LA(1);

            if ( (LA33_0==LPAREN) ) {
                alt33=1;
            }
            else if ( ((LA33_0>=PERCENT && LA33_0<=QUOTE)) ) {
                alt33=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return cond;}
                NoViableAltException nvae =
                    new NoViableAltException("", 33, 0, input);

                throw nvae;
            }
            switch (alt33) {
                case 1 :
                    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:207:4: arg= parenExpression
                    {
                    pushFollow(FOLLOW_parenExpression_in_primaryExpression663);
                    arg=parenExpression();

                    state._fsp--;
                    if (state.failed) return cond;
                    if ( state.backtracking==0 ) {
                       cond = arg; 
                    }

                    }
                    break;
                case 2 :
                    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:208:4: (v= cond_value )
                    {
                    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:208:4: (v= cond_value )
                    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:208:5: v= cond_value
                    {
                    pushFollow(FOLLOW_cond_value_in_primaryExpression673);
                    v=cond_value();

                    state._fsp--;
                    if (state.failed) return cond;

                    }

                    if ( state.backtracking==0 ) {
                       cond = v; 
                    }

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 12, primaryExpression_StartIndex); }
        }
        return cond;
    }
    // $ANTLR end "primaryExpression"


    // $ANTLR start "cond_value"
    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:211:1: cond_value returns [IWSLValue value] : ( PERCENT (str= common_string ( PERCENT )? | v= escaped_var ) | DOLLAR (str= common_string ( DOLLAR )? | v= escaped_var ) | val= number | TRUE | FALSE | val= quoted_string );
    public final IWSLValue cond_value() throws RecognitionException {
        IWSLValue value = null;
        int cond_value_StartIndex = input.index();
        String str = null;

        IWSLValue v = null;

        IWSLValue val = null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 13) ) { return value; }
            // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:212:2: ( PERCENT (str= common_string ( PERCENT )? | v= escaped_var ) | DOLLAR (str= common_string ( DOLLAR )? | v= escaped_var ) | val= number | TRUE | FALSE | val= quoted_string )
            int alt38=6;
            switch ( input.LA(1) ) {
            case PERCENT:
                {
                alt38=1;
                }
                break;
            case DOLLAR:
                {
                alt38=2;
                }
                break;
            case STRING:
                {
                alt38=3;
                }
                break;
            case TRUE:
                {
                alt38=4;
                }
                break;
            case FALSE:
                {
                alt38=5;
                }
                break;
            case QUOTE:
                {
                alt38=6;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return value;}
                NoViableAltException nvae =
                    new NoViableAltException("", 38, 0, input);

                throw nvae;
            }

            switch (alt38) {
                case 1 :
                    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:212:4: PERCENT (str= common_string ( PERCENT )? | v= escaped_var )
                    {
                    match(input,PERCENT,FOLLOW_PERCENT_in_cond_value694); if (state.failed) return value;
                    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:212:12: (str= common_string ( PERCENT )? | v= escaped_var )
                    int alt35=2;
                    int LA35_0 = input.LA(1);

                    if ( (LA35_0==IF||(LA35_0>=THEN && LA35_0<=EXISTS)||(LA35_0>=TRUE && LA35_0<=STRING)||(LA35_0>=BACKSLASH && LA35_0<=VERT)) ) {
                        alt35=1;
                    }
                    else if ( (LA35_0==LPAREN) ) {
                        alt35=2;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return value;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 35, 0, input);

                        throw nvae;
                    }
                    switch (alt35) {
                        case 1 :
                            // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:212:13: str= common_string ( PERCENT )?
                            {
                            pushFollow(FOLLOW_common_string_in_cond_value699);
                            str=common_string();

                            state._fsp--;
                            if (state.failed) return value;
                            if ( state.backtracking==0 ) {
                               value = new WSLVariable(new WSLString(str), script); 
                            }
                            // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:213:61: ( PERCENT )?
                            int alt34=2;
                            int LA34_0 = input.LA(1);

                            if ( (LA34_0==PERCENT) ) {
                                alt34=1;
                            }
                            switch (alt34) {
                                case 1 :
                                    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:0:0: PERCENT
                                    {
                                    match(input,PERCENT,FOLLOW_PERCENT_in_cond_value706); if (state.failed) return value;

                                    }
                                    break;

                            }


                            }
                            break;
                        case 2 :
                            // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:214:5: v= escaped_var
                            {
                            pushFollow(FOLLOW_escaped_var_in_cond_value715);
                            v=escaped_var();

                            state._fsp--;
                            if (state.failed) return value;
                            if ( state.backtracking==0 ) {
                               value = new WSLVariable(v, script); 
                            }

                            }
                            break;

                    }


                    }
                    break;
                case 2 :
                    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:215:4: DOLLAR (str= common_string ( DOLLAR )? | v= escaped_var )
                    {
                    match(input,DOLLAR,FOLLOW_DOLLAR_in_cond_value723); if (state.failed) return value;
                    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:215:11: (str= common_string ( DOLLAR )? | v= escaped_var )
                    int alt37=2;
                    int LA37_0 = input.LA(1);

                    if ( (LA37_0==IF||(LA37_0>=THEN && LA37_0<=EXISTS)||(LA37_0>=TRUE && LA37_0<=STRING)||(LA37_0>=BACKSLASH && LA37_0<=VERT)) ) {
                        alt37=1;
                    }
                    else if ( (LA37_0==LPAREN) ) {
                        alt37=2;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return value;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 37, 0, input);

                        throw nvae;
                    }
                    switch (alt37) {
                        case 1 :
                            // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:215:12: str= common_string ( DOLLAR )?
                            {
                            pushFollow(FOLLOW_common_string_in_cond_value728);
                            str=common_string();

                            state._fsp--;
                            if (state.failed) return value;
                            if ( state.backtracking==0 ) {
                               value = new WSLLocalVariable(new WSLString(str), script); 
                            }
                            // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:216:66: ( DOLLAR )?
                            int alt36=2;
                            int LA36_0 = input.LA(1);

                            if ( (LA36_0==DOLLAR) ) {
                                alt36=1;
                            }
                            switch (alt36) {
                                case 1 :
                                    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:0:0: DOLLAR
                                    {
                                    match(input,DOLLAR,FOLLOW_DOLLAR_in_cond_value735); if (state.failed) return value;

                                    }
                                    break;

                            }


                            }
                            break;
                        case 2 :
                            // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:217:5: v= escaped_var
                            {
                            pushFollow(FOLLOW_escaped_var_in_cond_value744);
                            v=escaped_var();

                            state._fsp--;
                            if (state.failed) return value;
                            if ( state.backtracking==0 ) {
                               value = new WSLLocalVariable(v, script); 
                            }

                            }
                            break;

                    }


                    }
                    break;
                case 3 :
                    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:218:4: val= number
                    {
                    pushFollow(FOLLOW_number_in_cond_value754);
                    val=number();

                    state._fsp--;
                    if (state.failed) return value;
                    if ( state.backtracking==0 ) {
                       value = val; 
                    }

                    }
                    break;
                case 4 :
                    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:219:4: TRUE
                    {
                    match(input,TRUE,FOLLOW_TRUE_in_cond_value762); if (state.failed) return value;
                    if ( state.backtracking==0 ) {
                       value = new WSLBoolean(true); 
                    }

                    }
                    break;
                case 5 :
                    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:220:4: FALSE
                    {
                    match(input,FALSE,FOLLOW_FALSE_in_cond_value772); if (state.failed) return value;
                    if ( state.backtracking==0 ) {
                       value = new WSLBoolean(false); 
                    }

                    }
                    break;
                case 6 :
                    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:221:4: val= quoted_string
                    {
                    pushFollow(FOLLOW_quoted_string_in_cond_value784);
                    val=quoted_string();

                    state._fsp--;
                    if (state.failed) return value;
                    if ( state.backtracking==0 ) {
                       value = val; 
                    }

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 13, cond_value_StartIndex); }
        }
        return value;
    }
    // $ANTLR end "cond_value"


    // $ANTLR start "number"
    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:224:1: number returns [IWSLValue value] : {...}?v= STRING ;
    public final IWSLValue number() throws RecognitionException {
        IWSLValue value = null;
        int number_StartIndex = input.index();
        Token v=null;

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 14) ) { return value; }
            // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:225:2: ({...}?v= STRING )
            // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:225:4: {...}?v= STRING
            {
            if ( !(( isNumber(input.LT(1).getText()) )) ) {
                if (state.backtracking>0) {state.failed=true; return value;}
                throw new FailedPredicateException(input, "number", " isNumber(input.LT(1).getText()) ");
            }
            v=(Token)match(input,STRING,FOLLOW_STRING_in_number805); if (state.failed) return value;
            if ( state.backtracking==0 ) {
               value = new WSLNumber((v!=null?v.getText():null)); 
            }

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 14, number_StartIndex); }
        }
        return value;
    }
    // $ANTLR end "number"


    // $ANTLR start "string_list"
    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:228:1: string_list returns [IWSLValue value] : l= string_list_helper ;
    public final IWSLValue string_list() throws RecognitionException {
        IWSLValue value = null;
        int string_list_StartIndex = input.index();
        ArrayList<IWSLValue> l = null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 15) ) { return value; }
            // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:229:2: (l= string_list_helper )
            // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:229:4: l= string_list_helper
            {
            pushFollow(FOLLOW_string_list_helper_in_string_list824);
            l=string_list_helper();

            state._fsp--;
            if (state.failed) return value;
            if ( state.backtracking==0 ) {

              				if(l.size() > 1)
              					value = new WSLList(l);
              				else
              					value = l.get(0);
              			
            }

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 15, string_list_StartIndex); }
        }
        return value;
    }
    // $ANTLR end "string_list"


    // $ANTLR start "string_list_helper"
    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:238:1: string_list_helper returns [ArrayList<IWSLValue> list] : data= string_value (l= string_list_helper )? ;
    public final ArrayList<IWSLValue> string_list_helper() throws RecognitionException {
        ArrayList<IWSLValue> list = null;
        int string_list_helper_StartIndex = input.index();
        IWSLValue data = null;

        ArrayList<IWSLValue> l = null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 16) ) { return list; }
            // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:239:2: (data= string_value (l= string_list_helper )? )
            // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:239:4: data= string_value (l= string_list_helper )?
            {
            pushFollow(FOLLOW_string_value_in_string_list_helper846);
            data=string_value();

            state._fsp--;
            if (state.failed) return list;
            // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:239:22: (l= string_list_helper )?
            int alt39=2;
            alt39 = dfa39.predict(input);
            switch (alt39) {
                case 1 :
                    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:239:23: l= string_list_helper
                    {
                    pushFollow(FOLLOW_string_list_helper_in_string_list_helper851);
                    l=string_list_helper();

                    state._fsp--;
                    if (state.failed) return list;

                    }
                    break;

            }

            if ( state.backtracking==0 ) {

              			if(l == null) {
              				list = new ArrayList<IWSLValue>();
              				list.add(data);
              			} else {
              				list = l;
              				list.add(0, data);
              			}
              		
            }

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 16, string_list_helper_StartIndex); }
        }
        return list;
    }
    // $ANTLR end "string_list_helper"


    // $ANTLR start "string_value"
    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:251:1: string_value returns [IWSLValue value] : ( ( ( PERCENT | DOLLAR ) ~ ( EOL | PERCENT | DOLLAR | BLANK | EOF ) )=>v= variable | str= text_string | t= QUOTE );
    public final IWSLValue string_value() throws RecognitionException {
        IWSLValue value = null;
        int string_value_StartIndex = input.index();
        Token t=null;
        IWSLValue v = null;

        String str = null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 17) ) { return value; }
            // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:252:2: ( ( ( PERCENT | DOLLAR ) ~ ( EOL | PERCENT | DOLLAR | BLANK | EOF ) )=>v= variable | str= text_string | t= QUOTE )
            int alt40=3;
            alt40 = dfa40.predict(input);
            switch (alt40) {
                case 1 :
                    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:252:4: ( ( PERCENT | DOLLAR ) ~ ( EOL | PERCENT | DOLLAR | BLANK | EOF ) )=>v= variable
                    {
                    pushFollow(FOLLOW_variable_in_string_value898);
                    v=variable();

                    state._fsp--;
                    if (state.failed) return value;
                    if ( state.backtracking==0 ) {
                       value = v; 
                    }

                    }
                    break;
                case 2 :
                    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:253:4: str= text_string
                    {
                    pushFollow(FOLLOW_text_string_in_string_value907);
                    str=text_string();

                    state._fsp--;
                    if (state.failed) return value;
                    if ( state.backtracking==0 ) {
                       value = new WSLString(str); 
                    }

                    }
                    break;
                case 3 :
                    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:254:4: t= QUOTE
                    {
                    t=(Token)match(input,QUOTE,FOLLOW_QUOTE_in_string_value916); if (state.failed) return value;
                    if ( state.backtracking==0 ) {
                       value = new WSLString((t!=null?t.getText():null)); 
                    }

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 17, string_value_StartIndex); }
        }
        return value;
    }
    // $ANTLR end "string_value"


    // $ANTLR start "quoted_string"
    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:257:1: quoted_string returns [IWSLValue value] : QUOTE l= quoted_string_helper QUOTE ;
    public final IWSLValue quoted_string() throws RecognitionException {
        IWSLValue value = null;
        int quoted_string_StartIndex = input.index();
        ArrayList<IWSLValue> l = null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 18) ) { return value; }
            // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:258:2: ( QUOTE l= quoted_string_helper QUOTE )
            // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:258:4: QUOTE l= quoted_string_helper QUOTE
            {
            match(input,QUOTE,FOLLOW_QUOTE_in_quoted_string935); if (state.failed) return value;
            pushFollow(FOLLOW_quoted_string_helper_in_quoted_string939);
            l=quoted_string_helper();

            state._fsp--;
            if (state.failed) return value;
            match(input,QUOTE,FOLLOW_QUOTE_in_quoted_string941); if (state.failed) return value;
            if ( state.backtracking==0 ) {

              			if(l.size() > 1)
              				value = new WSLList(l);
              			else
              				value = l.get(0);
              		
            }

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 18, quoted_string_StartIndex); }
        }
        return value;
    }
    // $ANTLR end "quoted_string"


    // $ANTLR start "quoted_string_helper"
    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:267:1: quoted_string_helper returns [ArrayList<IWSLValue> list] : data= quoted_string_value (l= quoted_string_helper )? ;
    public final ArrayList<IWSLValue> quoted_string_helper() throws RecognitionException {
        ArrayList<IWSLValue> list = null;
        int quoted_string_helper_StartIndex = input.index();
        IWSLValue data = null;

        ArrayList<IWSLValue> l = null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 19) ) { return list; }
            // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:268:2: (data= quoted_string_value (l= quoted_string_helper )? )
            // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:268:4: data= quoted_string_value (l= quoted_string_helper )?
            {
            pushFollow(FOLLOW_quoted_string_value_in_quoted_string_helper962);
            data=quoted_string_value();

            state._fsp--;
            if (state.failed) return list;
            // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:268:29: (l= quoted_string_helper )?
            int alt41=2;
            int LA41_0 = input.LA(1);

            if ( ((LA41_0>=IF && LA41_0<=STRING)||(LA41_0>=BACKSLASH && LA41_0<=VERT)) ) {
                alt41=1;
            }
            switch (alt41) {
                case 1 :
                    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:268:30: l= quoted_string_helper
                    {
                    pushFollow(FOLLOW_quoted_string_helper_in_quoted_string_helper967);
                    l=quoted_string_helper();

                    state._fsp--;
                    if (state.failed) return list;

                    }
                    break;

            }

            if ( state.backtracking==0 ) {

              			if(l == null) {
              				list = new ArrayList<IWSLValue>();
              				list.add(data);
              			} else {
              				list = l;
              				list.add(0, data);
              			}
              		
            }

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 19, quoted_string_helper_StartIndex); }
        }
        return list;
    }
    // $ANTLR end "quoted_string_helper"


    // $ANTLR start "quoted_string_value"
    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:280:1: quoted_string_value returns [IWSLValue value] : ( ( ( PERCENT | DOLLAR ) ~ ( EOL | PERCENT | DOLLAR | BLANK | EOF ) )=>v= variable | ( BACKSLASH QUOTE )=> BACKSLASH t= QUOTE | str= text_string );
    public final IWSLValue quoted_string_value() throws RecognitionException {
        IWSLValue value = null;
        int quoted_string_value_StartIndex = input.index();
        Token t=null;
        IWSLValue v = null;

        String str = null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 20) ) { return value; }
            // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:281:2: ( ( ( PERCENT | DOLLAR ) ~ ( EOL | PERCENT | DOLLAR | BLANK | EOF ) )=>v= variable | ( BACKSLASH QUOTE )=> BACKSLASH t= QUOTE | str= text_string )
            int alt42=3;
            alt42 = dfa42.predict(input);
            switch (alt42) {
                case 1 :
                    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:281:4: ( ( PERCENT | DOLLAR ) ~ ( EOL | PERCENT | DOLLAR | BLANK | EOF ) )=>v= variable
                    {
                    pushFollow(FOLLOW_variable_in_quoted_string_value1014);
                    v=variable();

                    state._fsp--;
                    if (state.failed) return value;
                    if ( state.backtracking==0 ) {
                       value = v; 
                    }

                    }
                    break;
                case 2 :
                    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:282:4: ( BACKSLASH QUOTE )=> BACKSLASH t= QUOTE
                    {
                    match(input,BACKSLASH,FOLLOW_BACKSLASH_in_quoted_string_value1028); if (state.failed) return value;
                    t=(Token)match(input,QUOTE,FOLLOW_QUOTE_in_quoted_string_value1032); if (state.failed) return value;
                    if ( state.backtracking==0 ) {
                       value = new WSLString((t!=null?t.getText():null)); 
                    }

                    }
                    break;
                case 3 :
                    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:283:4: str= text_string
                    {
                    pushFollow(FOLLOW_text_string_in_quoted_string_value1041);
                    str=text_string();

                    state._fsp--;
                    if (state.failed) return value;
                    if ( state.backtracking==0 ) {
                       value = new WSLString(str); 
                    }

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 20, quoted_string_value_StartIndex); }
        }
        return value;
    }
    // $ANTLR end "quoted_string_value"


    // $ANTLR start "common_string"
    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:286:1: common_string returns [String value] : (str= STRING | str= IF | str= THEN | str= OR | str= AND | str= NOTEQUAL | str= NOT | str= EQUAL | str= GTE | str= LTE | str= GT | str= LT | str= AMP | str= VERT | str= EXISTS | str= CONTAINS | str= CONTAINSRE | str= ACTION | str= TRUE | str= FALSE | {...}?str= WHEN | str= REMOVE | str= CLEAR | str= INSTANT | str= BACKSLASH ) ;
    public final String common_string() throws RecognitionException {
        String value = null;
        int common_string_StartIndex = input.index();
        Token str=null;

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 21) ) { return value; }
            // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:287:2: ( (str= STRING | str= IF | str= THEN | str= OR | str= AND | str= NOTEQUAL | str= NOT | str= EQUAL | str= GTE | str= LTE | str= GT | str= LT | str= AMP | str= VERT | str= EXISTS | str= CONTAINS | str= CONTAINSRE | str= ACTION | str= TRUE | str= FALSE | {...}?str= WHEN | str= REMOVE | str= CLEAR | str= INSTANT | str= BACKSLASH ) )
            // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:287:4: (str= STRING | str= IF | str= THEN | str= OR | str= AND | str= NOTEQUAL | str= NOT | str= EQUAL | str= GTE | str= LTE | str= GT | str= LT | str= AMP | str= VERT | str= EXISTS | str= CONTAINS | str= CONTAINSRE | str= ACTION | str= TRUE | str= FALSE | {...}?str= WHEN | str= REMOVE | str= CLEAR | str= INSTANT | str= BACKSLASH )
            {
            // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:287:4: (str= STRING | str= IF | str= THEN | str= OR | str= AND | str= NOTEQUAL | str= NOT | str= EQUAL | str= GTE | str= LTE | str= GT | str= LT | str= AMP | str= VERT | str= EXISTS | str= CONTAINS | str= CONTAINSRE | str= ACTION | str= TRUE | str= FALSE | {...}?str= WHEN | str= REMOVE | str= CLEAR | str= INSTANT | str= BACKSLASH )
            int alt43=25;
            alt43 = dfa43.predict(input);
            switch (alt43) {
                case 1 :
                    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:287:5: str= STRING
                    {
                    str=(Token)match(input,STRING,FOLLOW_STRING_in_common_string1061); if (state.failed) return value;

                    }
                    break;
                case 2 :
                    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:287:18: str= IF
                    {
                    str=(Token)match(input,IF,FOLLOW_IF_in_common_string1067); if (state.failed) return value;

                    }
                    break;
                case 3 :
                    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:287:27: str= THEN
                    {
                    str=(Token)match(input,THEN,FOLLOW_THEN_in_common_string1073); if (state.failed) return value;

                    }
                    break;
                case 4 :
                    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:287:38: str= OR
                    {
                    str=(Token)match(input,OR,FOLLOW_OR_in_common_string1079); if (state.failed) return value;

                    }
                    break;
                case 5 :
                    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:287:47: str= AND
                    {
                    str=(Token)match(input,AND,FOLLOW_AND_in_common_string1085); if (state.failed) return value;

                    }
                    break;
                case 6 :
                    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:287:57: str= NOTEQUAL
                    {
                    str=(Token)match(input,NOTEQUAL,FOLLOW_NOTEQUAL_in_common_string1091); if (state.failed) return value;

                    }
                    break;
                case 7 :
                    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:288:5: str= NOT
                    {
                    str=(Token)match(input,NOT,FOLLOW_NOT_in_common_string1099); if (state.failed) return value;

                    }
                    break;
                case 8 :
                    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:288:15: str= EQUAL
                    {
                    str=(Token)match(input,EQUAL,FOLLOW_EQUAL_in_common_string1105); if (state.failed) return value;

                    }
                    break;
                case 9 :
                    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:288:27: str= GTE
                    {
                    str=(Token)match(input,GTE,FOLLOW_GTE_in_common_string1111); if (state.failed) return value;

                    }
                    break;
                case 10 :
                    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:288:37: str= LTE
                    {
                    str=(Token)match(input,LTE,FOLLOW_LTE_in_common_string1117); if (state.failed) return value;

                    }
                    break;
                case 11 :
                    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:288:47: str= GT
                    {
                    str=(Token)match(input,GT,FOLLOW_GT_in_common_string1123); if (state.failed) return value;

                    }
                    break;
                case 12 :
                    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:288:56: str= LT
                    {
                    str=(Token)match(input,LT,FOLLOW_LT_in_common_string1129); if (state.failed) return value;

                    }
                    break;
                case 13 :
                    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:288:66: str= AMP
                    {
                    str=(Token)match(input,AMP,FOLLOW_AMP_in_common_string1136); if (state.failed) return value;

                    }
                    break;
                case 14 :
                    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:289:5: str= VERT
                    {
                    str=(Token)match(input,VERT,FOLLOW_VERT_in_common_string1144); if (state.failed) return value;

                    }
                    break;
                case 15 :
                    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:289:16: str= EXISTS
                    {
                    str=(Token)match(input,EXISTS,FOLLOW_EXISTS_in_common_string1150); if (state.failed) return value;

                    }
                    break;
                case 16 :
                    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:289:29: str= CONTAINS
                    {
                    str=(Token)match(input,CONTAINS,FOLLOW_CONTAINS_in_common_string1156); if (state.failed) return value;

                    }
                    break;
                case 17 :
                    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:289:44: str= CONTAINSRE
                    {
                    str=(Token)match(input,CONTAINSRE,FOLLOW_CONTAINSRE_in_common_string1162); if (state.failed) return value;

                    }
                    break;
                case 18 :
                    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:289:61: str= ACTION
                    {
                    str=(Token)match(input,ACTION,FOLLOW_ACTION_in_common_string1168); if (state.failed) return value;

                    }
                    break;
                case 19 :
                    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:289:74: str= TRUE
                    {
                    str=(Token)match(input,TRUE,FOLLOW_TRUE_in_common_string1174); if (state.failed) return value;

                    }
                    break;
                case 20 :
                    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:290:5: str= FALSE
                    {
                    str=(Token)match(input,FALSE,FOLLOW_FALSE_in_common_string1182); if (state.failed) return value;

                    }
                    break;
                case 21 :
                    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:290:17: {...}?str= WHEN
                    {
                    if ( !(( actionDepth == 0 )) ) {
                        if (state.backtracking>0) {state.failed=true; return value;}
                        throw new FailedPredicateException(input, "common_string", " actionDepth == 0 ");
                    }
                    str=(Token)match(input,WHEN,FOLLOW_WHEN_in_common_string1190); if (state.failed) return value;

                    }
                    break;
                case 22 :
                    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:290:50: str= REMOVE
                    {
                    str=(Token)match(input,REMOVE,FOLLOW_REMOVE_in_common_string1196); if (state.failed) return value;

                    }
                    break;
                case 23 :
                    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:290:63: str= CLEAR
                    {
                    str=(Token)match(input,CLEAR,FOLLOW_CLEAR_in_common_string1202); if (state.failed) return value;

                    }
                    break;
                case 24 :
                    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:291:5: str= INSTANT
                    {
                    str=(Token)match(input,INSTANT,FOLLOW_INSTANT_in_common_string1210); if (state.failed) return value;

                    }
                    break;
                case 25 :
                    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:291:19: str= BACKSLASH
                    {
                    str=(Token)match(input,BACKSLASH,FOLLOW_BACKSLASH_in_common_string1216); if (state.failed) return value;

                    }
                    break;

            }

            if ( state.backtracking==0 ) {
               value = (str!=null?str.getText():null); 
            }

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 21, common_string_StartIndex); }
        }
        return value;
    }
    // $ANTLR end "common_string"


    // $ANTLR start "text_string"
    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:295:1: text_string returns [String value] : ( ( ( PERCENT PERCENT )=> PERCENT t= PERCENT | ( DOLLAR DOLLAR )=> DOLLAR t= DOLLAR | t= PERCENT | t= DOLLAR | t= RPAREN | t= LPAREN | t= BLANK ) | str= common_string );
    public final String text_string() throws RecognitionException {
        String value = null;
        int text_string_StartIndex = input.index();
        Token t=null;
        String str = null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 22) ) { return value; }
            // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:296:2: ( ( ( PERCENT PERCENT )=> PERCENT t= PERCENT | ( DOLLAR DOLLAR )=> DOLLAR t= DOLLAR | t= PERCENT | t= DOLLAR | t= RPAREN | t= LPAREN | t= BLANK ) | str= common_string )
            int alt45=2;
            int LA45_0 = input.LA(1);

            if ( (LA45_0==BLANK||(LA45_0>=LPAREN && LA45_0<=DOLLAR)) ) {
                alt45=1;
            }
            else if ( (LA45_0==IF||(LA45_0>=THEN && LA45_0<=EXISTS)||(LA45_0>=TRUE && LA45_0<=STRING)||(LA45_0>=BACKSLASH && LA45_0<=VERT)) ) {
                alt45=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return value;}
                NoViableAltException nvae =
                    new NoViableAltException("", 45, 0, input);

                throw nvae;
            }
            switch (alt45) {
                case 1 :
                    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:296:4: ( ( PERCENT PERCENT )=> PERCENT t= PERCENT | ( DOLLAR DOLLAR )=> DOLLAR t= DOLLAR | t= PERCENT | t= DOLLAR | t= RPAREN | t= LPAREN | t= BLANK )
                    {
                    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:296:4: ( ( PERCENT PERCENT )=> PERCENT t= PERCENT | ( DOLLAR DOLLAR )=> DOLLAR t= DOLLAR | t= PERCENT | t= DOLLAR | t= RPAREN | t= LPAREN | t= BLANK )
                    int alt44=7;
                    alt44 = dfa44.predict(input);
                    switch (alt44) {
                        case 1 :
                            // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:296:5: ( PERCENT PERCENT )=> PERCENT t= PERCENT
                            {
                            match(input,PERCENT,FOLLOW_PERCENT_in_text_string1244); if (state.failed) return value;
                            t=(Token)match(input,PERCENT,FOLLOW_PERCENT_in_text_string1248); if (state.failed) return value;

                            }
                            break;
                        case 2 :
                            // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:297:5: ( DOLLAR DOLLAR )=> DOLLAR t= DOLLAR
                            {
                            match(input,DOLLAR,FOLLOW_DOLLAR_in_text_string1261); if (state.failed) return value;
                            t=(Token)match(input,DOLLAR,FOLLOW_DOLLAR_in_text_string1265); if (state.failed) return value;

                            }
                            break;
                        case 3 :
                            // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:298:5: t= PERCENT
                            {
                            t=(Token)match(input,PERCENT,FOLLOW_PERCENT_in_text_string1273); if (state.failed) return value;

                            }
                            break;
                        case 4 :
                            // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:298:17: t= DOLLAR
                            {
                            t=(Token)match(input,DOLLAR,FOLLOW_DOLLAR_in_text_string1279); if (state.failed) return value;

                            }
                            break;
                        case 5 :
                            // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:298:28: t= RPAREN
                            {
                            t=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_text_string1285); if (state.failed) return value;

                            }
                            break;
                        case 6 :
                            // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:298:39: t= LPAREN
                            {
                            t=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_text_string1291); if (state.failed) return value;

                            }
                            break;
                        case 7 :
                            // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:298:50: t= BLANK
                            {
                            t=(Token)match(input,BLANK,FOLLOW_BLANK_in_text_string1297); if (state.failed) return value;

                            }
                            break;

                    }

                    if ( state.backtracking==0 ) {
                       value = (t!=null?t.getText():null); 
                    }

                    }
                    break;
                case 2 :
                    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:300:4: str= common_string
                    {
                    pushFollow(FOLLOW_common_string_in_text_string1309);
                    str=common_string();

                    state._fsp--;
                    if (state.failed) return value;
                    if ( state.backtracking==0 ) {
                       value = str; 
                    }

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 22, text_string_StartIndex); }
        }
        return value;
    }
    // $ANTLR end "text_string"


    // $ANTLR start "variable"
    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:303:1: variable returns [IWSLValue value] : ( PERCENT (str= escaped_var | str= variable_string ( PERCENT )? ) | DOLLAR (str= escaped_var | str= variable_string ( DOLLAR )? ) );
    public final IWSLValue variable() throws RecognitionException {
        IWSLValue value = null;
        int variable_StartIndex = input.index();
        IWSLValue str = null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 23) ) { return value; }
            // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:304:2: ( PERCENT (str= escaped_var | str= variable_string ( PERCENT )? ) | DOLLAR (str= escaped_var | str= variable_string ( DOLLAR )? ) )
            int alt50=2;
            int LA50_0 = input.LA(1);

            if ( (LA50_0==PERCENT) ) {
                alt50=1;
            }
            else if ( (LA50_0==DOLLAR) ) {
                alt50=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return value;}
                NoViableAltException nvae =
                    new NoViableAltException("", 50, 0, input);

                throw nvae;
            }
            switch (alt50) {
                case 1 :
                    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:304:4: PERCENT (str= escaped_var | str= variable_string ( PERCENT )? )
                    {
                    match(input,PERCENT,FOLLOW_PERCENT_in_variable1326); if (state.failed) return value;
                    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:304:12: (str= escaped_var | str= variable_string ( PERCENT )? )
                    int alt47=2;
                    int LA47_0 = input.LA(1);

                    if ( (LA47_0==LPAREN) ) {
                        alt47=1;
                    }
                    else if ( (LA47_0==IF||(LA47_0>=THEN && LA47_0<=EXISTS)||(LA47_0>=TRUE && LA47_0<=STRING)||(LA47_0>=BACKSLASH && LA47_0<=VERT)) ) {
                        alt47=2;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return value;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 47, 0, input);

                        throw nvae;
                    }
                    switch (alt47) {
                        case 1 :
                            // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:304:13: str= escaped_var
                            {
                            pushFollow(FOLLOW_escaped_var_in_variable1331);
                            str=escaped_var();

                            state._fsp--;
                            if (state.failed) return value;

                            }
                            break;
                        case 2 :
                            // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:304:31: str= variable_string ( PERCENT )?
                            {
                            pushFollow(FOLLOW_variable_string_in_variable1337);
                            str=variable_string();

                            state._fsp--;
                            if (state.failed) return value;
                            // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:304:51: ( PERCENT )?
                            int alt46=2;
                            int LA46_0 = input.LA(1);

                            if ( (LA46_0==PERCENT) ) {
                                int LA46_1 = input.LA(2);

                                if ( (synpred99_WSL()) ) {
                                    alt46=1;
                                }
                            }
                            switch (alt46) {
                                case 1 :
                                    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:0:0: PERCENT
                                    {
                                    match(input,PERCENT,FOLLOW_PERCENT_in_variable1339); if (state.failed) return value;

                                    }
                                    break;

                            }


                            }
                            break;

                    }

                    if ( state.backtracking==0 ) {
                       value = new WSLVariable(str, script); 
                    }

                    }
                    break;
                case 2 :
                    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:306:4: DOLLAR (str= escaped_var | str= variable_string ( DOLLAR )? )
                    {
                    match(input,DOLLAR,FOLLOW_DOLLAR_in_variable1350); if (state.failed) return value;
                    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:306:11: (str= escaped_var | str= variable_string ( DOLLAR )? )
                    int alt49=2;
                    int LA49_0 = input.LA(1);

                    if ( (LA49_0==LPAREN) ) {
                        alt49=1;
                    }
                    else if ( (LA49_0==IF||(LA49_0>=THEN && LA49_0<=EXISTS)||(LA49_0>=TRUE && LA49_0<=STRING)||(LA49_0>=BACKSLASH && LA49_0<=VERT)) ) {
                        alt49=2;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return value;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 49, 0, input);

                        throw nvae;
                    }
                    switch (alt49) {
                        case 1 :
                            // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:306:12: str= escaped_var
                            {
                            pushFollow(FOLLOW_escaped_var_in_variable1355);
                            str=escaped_var();

                            state._fsp--;
                            if (state.failed) return value;

                            }
                            break;
                        case 2 :
                            // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:306:30: str= variable_string ( DOLLAR )?
                            {
                            pushFollow(FOLLOW_variable_string_in_variable1361);
                            str=variable_string();

                            state._fsp--;
                            if (state.failed) return value;
                            // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:306:50: ( DOLLAR )?
                            int alt48=2;
                            int LA48_0 = input.LA(1);

                            if ( (LA48_0==DOLLAR) ) {
                                int LA48_1 = input.LA(2);

                                if ( (synpred102_WSL()) ) {
                                    alt48=1;
                                }
                            }
                            switch (alt48) {
                                case 1 :
                                    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:0:0: DOLLAR
                                    {
                                    match(input,DOLLAR,FOLLOW_DOLLAR_in_variable1363); if (state.failed) return value;

                                    }
                                    break;

                            }


                            }
                            break;

                    }

                    if ( state.backtracking==0 ) {
                       value = new WSLLocalVariable(str, script); 
                    }

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 23, variable_StartIndex); }
        }
        return value;
    }
    // $ANTLR end "variable"


    // $ANTLR start "variable_string"
    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:310:1: variable_string returns [IWSLValue value] : str= variable_string_helper ;
    public final IWSLValue variable_string() throws RecognitionException {
        IWSLValue value = null;
        int variable_string_StartIndex = input.index();
        StringBuffer str = null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 24) ) { return value; }
            // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:311:2: (str= variable_string_helper )
            // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:311:4: str= variable_string_helper
            {
            pushFollow(FOLLOW_variable_string_helper_in_variable_string1386);
            str=variable_string_helper();

            state._fsp--;
            if (state.failed) return value;
            if ( state.backtracking==0 ) {

              			value = new WSLString(str.toString());
              		
            }

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 24, variable_string_StartIndex); }
        }
        return value;
    }
    // $ANTLR end "variable_string"


    // $ANTLR start "variable_string_helper"
    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:317:1: variable_string_helper returns [StringBuffer value] : str= common_string (rest= variable_string_helper )? ;
    public final StringBuffer variable_string_helper() throws RecognitionException {
        StringBuffer value = null;
        int variable_string_helper_StartIndex = input.index();
        String str = null;

        StringBuffer rest = null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 25) ) { return value; }
            // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:318:2: (str= common_string (rest= variable_string_helper )? )
            // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:318:4: str= common_string (rest= variable_string_helper )?
            {
            pushFollow(FOLLOW_common_string_in_variable_string_helper1407);
            str=common_string();

            state._fsp--;
            if (state.failed) return value;
            // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:318:26: (rest= variable_string_helper )?
            int alt51=2;
            alt51 = dfa51.predict(input);
            switch (alt51) {
                case 1 :
                    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:0:0: rest= variable_string_helper
                    {
                    pushFollow(FOLLOW_variable_string_helper_in_variable_string_helper1411);
                    rest=variable_string_helper();

                    state._fsp--;
                    if (state.failed) return value;

                    }
                    break;

            }

            if ( state.backtracking==0 ) {

              			if(rest == null) {
              				value = new StringBuffer(str);
              			} else {
              				value = rest;
              				value.insert(0, str);
              			}
              		
            }

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 25, variable_string_helper_StartIndex); }
        }
        return value;
    }
    // $ANTLR end "variable_string_helper"


    // $ANTLR start "escaped_var"
    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:329:1: escaped_var returns [IWSLValue value] : LPAREN str= vstring_list RPAREN ;
    public final IWSLValue escaped_var() throws RecognitionException {
        IWSLValue value = null;
        int escaped_var_StartIndex = input.index();
        IWSLValue str = null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 26) ) { return value; }
            // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:330:2: ( LPAREN str= vstring_list RPAREN )
            // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:330:4: LPAREN str= vstring_list RPAREN
            {
            match(input,LPAREN,FOLLOW_LPAREN_in_escaped_var1431); if (state.failed) return value;
            pushFollow(FOLLOW_vstring_list_in_escaped_var1435);
            str=vstring_list();

            state._fsp--;
            if (state.failed) return value;
            match(input,RPAREN,FOLLOW_RPAREN_in_escaped_var1437); if (state.failed) return value;
            if ( state.backtracking==0 ) {
               value = str; 
            }

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 26, escaped_var_StartIndex); }
        }
        return value;
    }
    // $ANTLR end "escaped_var"


    // $ANTLR start "vstring_list"
    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:334:1: vstring_list returns [IWSLValue value] : l= vstring_list_helper ;
    public final IWSLValue vstring_list() throws RecognitionException {
        IWSLValue value = null;
        int vstring_list_StartIndex = input.index();
        ArrayList<IWSLValue> l = null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 27) ) { return value; }
            // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:335:2: (l= vstring_list_helper )
            // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:335:4: l= vstring_list_helper
            {
            pushFollow(FOLLOW_vstring_list_helper_in_vstring_list1458);
            l=vstring_list_helper();

            state._fsp--;
            if (state.failed) return value;
            if ( state.backtracking==0 ) {

              			if(l.size() > 1)
              				value = new WSLList(l);
              			else
              				value = l.get(0);
              		
            }

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 27, vstring_list_StartIndex); }
        }
        return value;
    }
    // $ANTLR end "vstring_list"


    // $ANTLR start "vstring_list_helper"
    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:344:1: vstring_list_helper returns [ArrayList<IWSLValue> list] : data= vstring_value (l= vstring_list_helper )? ;
    public final ArrayList<IWSLValue> vstring_list_helper() throws RecognitionException {
        ArrayList<IWSLValue> list = null;
        int vstring_list_helper_StartIndex = input.index();
        IWSLValue data = null;

        ArrayList<IWSLValue> l = null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 28) ) { return list; }
            // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:345:2: (data= vstring_value (l= vstring_list_helper )? )
            // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:345:4: data= vstring_value (l= vstring_list_helper )?
            {
            pushFollow(FOLLOW_vstring_value_in_vstring_list_helper1479);
            data=vstring_value();

            state._fsp--;
            if (state.failed) return list;
            // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:345:23: (l= vstring_list_helper )?
            int alt52=2;
            int LA52_0 = input.LA(1);

            if ( ((LA52_0>=IF && LA52_0<=EXISTS)||(LA52_0>=PERCENT && LA52_0<=VERT)) ) {
                alt52=1;
            }
            switch (alt52) {
                case 1 :
                    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:345:24: l= vstring_list_helper
                    {
                    pushFollow(FOLLOW_vstring_list_helper_in_vstring_list_helper1484);
                    l=vstring_list_helper();

                    state._fsp--;
                    if (state.failed) return list;

                    }
                    break;

            }

            if ( state.backtracking==0 ) {

              			if(l == null) {
              				list = new ArrayList<IWSLValue>();
              				list.add(data);
              			} else {
              				list = l;
              				list.add(0, data);
              			}
              		
            }

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 28, vstring_list_helper_StartIndex); }
        }
        return list;
    }
    // $ANTLR end "vstring_list_helper"


    // $ANTLR start "vstring_value"
    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:357:1: vstring_value returns [IWSLValue value] : ( ( ( PERCENT | DOLLAR ) ~ ( EOL | PERCENT | DOLLAR | BLANK | EOF ) )=>v= variable | ( ( PERCENT PERCENT )=> PERCENT t= PERCENT | ( DOLLAR DOLLAR )=> DOLLAR t= DOLLAR | t= QUOTE | ( BACKSLASH LPAREN )=> BACKSLASH t= LPAREN | ( BACKSLASH RPAREN )=> BACKSLASH t= RPAREN | t= BLANK ) | str= common_string );
    public final IWSLValue vstring_value() throws RecognitionException {
        IWSLValue value = null;
        int vstring_value_StartIndex = input.index();
        Token t=null;
        IWSLValue v = null;

        String str = null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 29) ) { return value; }
            // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:358:2: ( ( ( PERCENT | DOLLAR ) ~ ( EOL | PERCENT | DOLLAR | BLANK | EOF ) )=>v= variable | ( ( PERCENT PERCENT )=> PERCENT t= PERCENT | ( DOLLAR DOLLAR )=> DOLLAR t= DOLLAR | t= QUOTE | ( BACKSLASH LPAREN )=> BACKSLASH t= LPAREN | ( BACKSLASH RPAREN )=> BACKSLASH t= RPAREN | t= BLANK ) | str= common_string )
            int alt54=3;
            alt54 = dfa54.predict(input);
            switch (alt54) {
                case 1 :
                    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:358:4: ( ( PERCENT | DOLLAR ) ~ ( EOL | PERCENT | DOLLAR | BLANK | EOF ) )=>v= variable
                    {
                    pushFollow(FOLLOW_variable_in_vstring_value1531);
                    v=variable();

                    state._fsp--;
                    if (state.failed) return value;
                    if ( state.backtracking==0 ) {
                       value = v; 
                    }

                    }
                    break;
                case 2 :
                    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:359:4: ( ( PERCENT PERCENT )=> PERCENT t= PERCENT | ( DOLLAR DOLLAR )=> DOLLAR t= DOLLAR | t= QUOTE | ( BACKSLASH LPAREN )=> BACKSLASH t= LPAREN | ( BACKSLASH RPAREN )=> BACKSLASH t= RPAREN | t= BLANK )
                    {
                    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:359:4: ( ( PERCENT PERCENT )=> PERCENT t= PERCENT | ( DOLLAR DOLLAR )=> DOLLAR t= DOLLAR | t= QUOTE | ( BACKSLASH LPAREN )=> BACKSLASH t= LPAREN | ( BACKSLASH RPAREN )=> BACKSLASH t= RPAREN | t= BLANK )
                    int alt53=6;
                    int LA53_0 = input.LA(1);

                    if ( (LA53_0==PERCENT) && (synpred111_WSL())) {
                        alt53=1;
                    }
                    else if ( (LA53_0==DOLLAR) && (synpred112_WSL())) {
                        alt53=2;
                    }
                    else if ( (LA53_0==QUOTE) ) {
                        alt53=3;
                    }
                    else if ( (LA53_0==BACKSLASH) ) {
                        int LA53_4 = input.LA(2);

                        if ( (LA53_4==LPAREN) && (synpred114_WSL())) {
                            alt53=4;
                        }
                        else if ( (LA53_4==RPAREN) && (synpred115_WSL())) {
                            alt53=5;
                        }
                        else {
                            if (state.backtracking>0) {state.failed=true; return value;}
                            NoViableAltException nvae =
                                new NoViableAltException("", 53, 4, input);

                            throw nvae;
                        }
                    }
                    else if ( (LA53_0==BLANK) ) {
                        alt53=6;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return value;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 53, 0, input);

                        throw nvae;
                    }
                    switch (alt53) {
                        case 1 :
                            // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:359:5: ( PERCENT PERCENT )=> PERCENT t= PERCENT
                            {
                            match(input,PERCENT,FOLLOW_PERCENT_in_vstring_value1546); if (state.failed) return value;
                            t=(Token)match(input,PERCENT,FOLLOW_PERCENT_in_vstring_value1550); if (state.failed) return value;

                            }
                            break;
                        case 2 :
                            // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:360:5: ( DOLLAR DOLLAR )=> DOLLAR t= DOLLAR
                            {
                            match(input,DOLLAR,FOLLOW_DOLLAR_in_vstring_value1563); if (state.failed) return value;
                            t=(Token)match(input,DOLLAR,FOLLOW_DOLLAR_in_vstring_value1567); if (state.failed) return value;

                            }
                            break;
                        case 3 :
                            // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:361:5: t= QUOTE
                            {
                            t=(Token)match(input,QUOTE,FOLLOW_QUOTE_in_vstring_value1575); if (state.failed) return value;

                            }
                            break;
                        case 4 :
                            // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:361:15: ( BACKSLASH LPAREN )=> BACKSLASH t= LPAREN
                            {
                            match(input,BACKSLASH,FOLLOW_BACKSLASH_in_vstring_value1586); if (state.failed) return value;
                            t=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_vstring_value1590); if (state.failed) return value;

                            }
                            break;
                        case 5 :
                            // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:362:5: ( BACKSLASH RPAREN )=> BACKSLASH t= RPAREN
                            {
                            match(input,BACKSLASH,FOLLOW_BACKSLASH_in_vstring_value1603); if (state.failed) return value;
                            t=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_vstring_value1607); if (state.failed) return value;

                            }
                            break;
                        case 6 :
                            // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:362:47: t= BLANK
                            {
                            t=(Token)match(input,BLANK,FOLLOW_BLANK_in_vstring_value1613); if (state.failed) return value;

                            }
                            break;

                    }

                    if ( state.backtracking==0 ) {
                       value = new WSLString((t!=null?t.getText():null)); 
                    }

                    }
                    break;
                case 3 :
                    // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:364:4: str= common_string
                    {
                    pushFollow(FOLLOW_common_string_in_vstring_value1625);
                    str=common_string();

                    state._fsp--;
                    if (state.failed) return value;
                    if ( state.backtracking==0 ) {
                       value = new WSLString(str); 
                    }

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 29, vstring_value_StartIndex); }
        }
        return value;
    }
    // $ANTLR end "vstring_value"

    // $ANTLR start synpred4_WSL
    public final void synpred4_WSL_fragment() throws RecognitionException {   
        // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:59:4: ( IF )
        // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:59:5: IF
        {
        match(input,IF,FOLLOW_IF_in_synpred4_WSL114); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred4_WSL

    // $ANTLR start synpred7_WSL
    public final void synpred7_WSL_fragment() throws RecognitionException {   
        // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:59:60: ( BLANK )
        // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:59:60: BLANK
        {
        match(input,BLANK,FOLLOW_BLANK_in_synpred7_WSL132); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred7_WSL

    // $ANTLR start synpred8_WSL
    public final void synpred8_WSL_fragment() throws RecognitionException {   
        // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:63:4: ( ACTION )
        // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:63:5: ACTION
        {
        match(input,ACTION,FOLLOW_ACTION_in_synpred8_WSL147); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred8_WSL

    // $ANTLR start synpred9_WSL
    public final void synpred9_WSL_fragment() throws RecognitionException {   
        // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:63:22: ( BLANK )
        // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:63:22: BLANK
        {
        match(input,BLANK,FOLLOW_BLANK_in_synpred9_WSL153); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred9_WSL

    // $ANTLR start synpred10_WSL
    public final void synpred10_WSL_fragment() throws RecognitionException {   
        // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:64:5: ( REMOVE )
        // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:64:6: REMOVE
        {
        match(input,REMOVE,FOLLOW_REMOVE_in_synpred10_WSL163); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred10_WSL

    // $ANTLR start synpred11_WSL
    public final void synpred11_WSL_fragment() throws RecognitionException {   
        // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:64:23: ( BLANK )
        // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:64:23: BLANK
        {
        match(input,BLANK,FOLLOW_BLANK_in_synpred11_WSL169); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred11_WSL

    // $ANTLR start synpred12_WSL
    public final void synpred12_WSL_fragment() throws RecognitionException {   
        // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:68:5: ( CLEAR )
        // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:68:6: CLEAR
        {
        match(input,CLEAR,FOLLOW_CLEAR_in_synpred12_WSL188); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred12_WSL

    // $ANTLR start synpred14_WSL
    public final void synpred14_WSL_fragment() throws RecognitionException {   
        // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:73:17: ( BLANK )
        // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:73:17: BLANK
        {
        match(input,BLANK,FOLLOW_BLANK_in_synpred14_WSL212); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred14_WSL

    // $ANTLR start synpred15_WSL
    public final void synpred15_WSL_fragment() throws RecognitionException {   
        // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:79:4: ( INSTANT )
        // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:79:5: INSTANT
        {
        match(input,INSTANT,FOLLOW_INSTANT_in_synpred15_WSL234); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred15_WSL

    // $ANTLR start synpred16_WSL
    public final void synpred16_WSL_fragment() throws RecognitionException {   
        // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:79:24: ( BLANK )
        // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:79:24: BLANK
        {
        match(input,BLANK,FOLLOW_BLANK_in_synpred16_WSL240); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred16_WSL

    // $ANTLR start synpred51_WSL
    public final void synpred51_WSL_fragment() throws RecognitionException {   
        ArrayList<IWSLValue> l = null;


        // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:239:23: (l= string_list_helper )
        // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:239:23: l= string_list_helper
        {
        pushFollow(FOLLOW_string_list_helper_in_synpred51_WSL851);
        l=string_list_helper();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred51_WSL

    // $ANTLR start synpred57_WSL
    public final void synpred57_WSL_fragment() throws RecognitionException {   
        // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:252:4: ( ( PERCENT | DOLLAR ) ~ ( EOL | PERCENT | DOLLAR | BLANK | EOF ) )
        // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:252:5: ( PERCENT | DOLLAR ) ~ ( EOL | PERCENT | DOLLAR | BLANK | EOF )
        {
        if ( (input.LA(1)>=PERCENT && input.LA(1)<=DOLLAR) ) {
            input.consume();
            state.errorRecovery=false;state.failed=false;
        }
        else {
            if (state.backtracking>0) {state.failed=true; return ;}
            MismatchedSetException mse = new MismatchedSetException(null,input);
            throw mse;
        }

        if ( (input.LA(1)>=LABEL && input.LA(1)<=IF)||(input.LA(1)>=THEN && input.LA(1)<=RPAREN)||(input.LA(1)>=TRUE && input.LA(1)<=COMMENT) ) {
            input.consume();
            state.errorRecovery=false;state.failed=false;
        }
        else {
            if (state.backtracking>0) {state.failed=true; return ;}
            MismatchedSetException mse = new MismatchedSetException(null,input);
            throw mse;
        }


        }
    }
    // $ANTLR end synpred57_WSL

    // $ANTLR start synpred58_WSL
    public final void synpred58_WSL_fragment() throws RecognitionException {   
        String str = null;


        // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:253:4: (str= text_string )
        // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:253:4: str= text_string
        {
        pushFollow(FOLLOW_text_string_in_synpred58_WSL907);
        str=text_string();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred58_WSL

    // $ANTLR start synpred65_WSL
    public final void synpred65_WSL_fragment() throws RecognitionException {   
        // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:281:4: ( ( PERCENT | DOLLAR ) ~ ( EOL | PERCENT | DOLLAR | BLANK | EOF ) )
        // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:281:5: ( PERCENT | DOLLAR ) ~ ( EOL | PERCENT | DOLLAR | BLANK | EOF )
        {
        if ( (input.LA(1)>=PERCENT && input.LA(1)<=DOLLAR) ) {
            input.consume();
            state.errorRecovery=false;state.failed=false;
        }
        else {
            if (state.backtracking>0) {state.failed=true; return ;}
            MismatchedSetException mse = new MismatchedSetException(null,input);
            throw mse;
        }

        if ( (input.LA(1)>=LABEL && input.LA(1)<=IF)||(input.LA(1)>=THEN && input.LA(1)<=RPAREN)||(input.LA(1)>=TRUE && input.LA(1)<=COMMENT) ) {
            input.consume();
            state.errorRecovery=false;state.failed=false;
        }
        else {
            if (state.backtracking>0) {state.failed=true; return ;}
            MismatchedSetException mse = new MismatchedSetException(null,input);
            throw mse;
        }


        }
    }
    // $ANTLR end synpred65_WSL

    // $ANTLR start synpred66_WSL
    public final void synpred66_WSL_fragment() throws RecognitionException {   
        // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:282:4: ( BACKSLASH QUOTE )
        // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:282:5: BACKSLASH QUOTE
        {
        match(input,BACKSLASH,FOLLOW_BACKSLASH_in_synpred66_WSL1022); if (state.failed) return ;
        match(input,QUOTE,FOLLOW_QUOTE_in_synpred66_WSL1024); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred66_WSL

    // $ANTLR start synpred91_WSL
    public final void synpred91_WSL_fragment() throws RecognitionException {   
        // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:296:5: ( PERCENT PERCENT )
        // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:296:6: PERCENT PERCENT
        {
        match(input,PERCENT,FOLLOW_PERCENT_in_synpred91_WSL1238); if (state.failed) return ;
        match(input,PERCENT,FOLLOW_PERCENT_in_synpred91_WSL1240); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred91_WSL

    // $ANTLR start synpred92_WSL
    public final void synpred92_WSL_fragment() throws RecognitionException {   
        // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:297:5: ( DOLLAR DOLLAR )
        // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:297:6: DOLLAR DOLLAR
        {
        match(input,DOLLAR,FOLLOW_DOLLAR_in_synpred92_WSL1255); if (state.failed) return ;
        match(input,DOLLAR,FOLLOW_DOLLAR_in_synpred92_WSL1257); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred92_WSL

    // $ANTLR start synpred93_WSL
    public final void synpred93_WSL_fragment() throws RecognitionException {   
        Token t=null;

        // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:298:5: (t= PERCENT )
        // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:298:5: t= PERCENT
        {
        t=(Token)match(input,PERCENT,FOLLOW_PERCENT_in_synpred93_WSL1273); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred93_WSL

    // $ANTLR start synpred94_WSL
    public final void synpred94_WSL_fragment() throws RecognitionException {   
        Token t=null;

        // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:298:17: (t= DOLLAR )
        // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:298:17: t= DOLLAR
        {
        t=(Token)match(input,DOLLAR,FOLLOW_DOLLAR_in_synpred94_WSL1279); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred94_WSL

    // $ANTLR start synpred99_WSL
    public final void synpred99_WSL_fragment() throws RecognitionException {   
        // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:304:51: ( PERCENT )
        // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:304:51: PERCENT
        {
        match(input,PERCENT,FOLLOW_PERCENT_in_synpred99_WSL1339); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred99_WSL

    // $ANTLR start synpred102_WSL
    public final void synpred102_WSL_fragment() throws RecognitionException {   
        // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:306:50: ( DOLLAR )
        // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:306:50: DOLLAR
        {
        match(input,DOLLAR,FOLLOW_DOLLAR_in_synpred102_WSL1363); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred102_WSL

    // $ANTLR start synpred103_WSL
    public final void synpred103_WSL_fragment() throws RecognitionException {   
        StringBuffer rest = null;


        // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:318:26: (rest= variable_string_helper )
        // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:318:26: rest= variable_string_helper
        {
        pushFollow(FOLLOW_variable_string_helper_in_synpred103_WSL1411);
        rest=variable_string_helper();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred103_WSL

    // $ANTLR start synpred110_WSL
    public final void synpred110_WSL_fragment() throws RecognitionException {   
        // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:358:4: ( ( PERCENT | DOLLAR ) ~ ( EOL | PERCENT | DOLLAR | BLANK | EOF ) )
        // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:358:5: ( PERCENT | DOLLAR ) ~ ( EOL | PERCENT | DOLLAR | BLANK | EOF )
        {
        if ( (input.LA(1)>=PERCENT && input.LA(1)<=DOLLAR) ) {
            input.consume();
            state.errorRecovery=false;state.failed=false;
        }
        else {
            if (state.backtracking>0) {state.failed=true; return ;}
            MismatchedSetException mse = new MismatchedSetException(null,input);
            throw mse;
        }

        if ( (input.LA(1)>=LABEL && input.LA(1)<=IF)||(input.LA(1)>=THEN && input.LA(1)<=RPAREN)||(input.LA(1)>=TRUE && input.LA(1)<=COMMENT) ) {
            input.consume();
            state.errorRecovery=false;state.failed=false;
        }
        else {
            if (state.backtracking>0) {state.failed=true; return ;}
            MismatchedSetException mse = new MismatchedSetException(null,input);
            throw mse;
        }


        }
    }
    // $ANTLR end synpred110_WSL

    // $ANTLR start synpred111_WSL
    public final void synpred111_WSL_fragment() throws RecognitionException {   
        // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:359:5: ( PERCENT PERCENT )
        // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:359:6: PERCENT PERCENT
        {
        match(input,PERCENT,FOLLOW_PERCENT_in_synpred111_WSL1540); if (state.failed) return ;
        match(input,PERCENT,FOLLOW_PERCENT_in_synpred111_WSL1542); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred111_WSL

    // $ANTLR start synpred112_WSL
    public final void synpred112_WSL_fragment() throws RecognitionException {   
        // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:360:5: ( DOLLAR DOLLAR )
        // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:360:6: DOLLAR DOLLAR
        {
        match(input,DOLLAR,FOLLOW_DOLLAR_in_synpred112_WSL1557); if (state.failed) return ;
        match(input,DOLLAR,FOLLOW_DOLLAR_in_synpred112_WSL1559); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred112_WSL

    // $ANTLR start synpred114_WSL
    public final void synpred114_WSL_fragment() throws RecognitionException {   
        // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:361:15: ( BACKSLASH LPAREN )
        // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:361:16: BACKSLASH LPAREN
        {
        match(input,BACKSLASH,FOLLOW_BACKSLASH_in_synpred114_WSL1580); if (state.failed) return ;
        match(input,LPAREN,FOLLOW_LPAREN_in_synpred114_WSL1582); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred114_WSL

    // $ANTLR start synpred115_WSL
    public final void synpred115_WSL_fragment() throws RecognitionException {   
        // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:362:5: ( BACKSLASH RPAREN )
        // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:362:6: BACKSLASH RPAREN
        {
        match(input,BACKSLASH,FOLLOW_BACKSLASH_in_synpred115_WSL1597); if (state.failed) return ;
        match(input,RPAREN,FOLLOW_RPAREN_in_synpred115_WSL1599); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred115_WSL

    // $ANTLR start synpred116_WSL
    public final void synpred116_WSL_fragment() throws RecognitionException {   
        Token t=null;

        // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:359:4: ( ( ( PERCENT PERCENT )=> PERCENT t= PERCENT | ( DOLLAR DOLLAR )=> DOLLAR t= DOLLAR | t= QUOTE | ( BACKSLASH LPAREN )=> BACKSLASH t= LPAREN | ( BACKSLASH RPAREN )=> BACKSLASH t= RPAREN | t= BLANK ) )
        // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:359:4: ( ( PERCENT PERCENT )=> PERCENT t= PERCENT | ( DOLLAR DOLLAR )=> DOLLAR t= DOLLAR | t= QUOTE | ( BACKSLASH LPAREN )=> BACKSLASH t= LPAREN | ( BACKSLASH RPAREN )=> BACKSLASH t= RPAREN | t= BLANK )
        {
        // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:359:4: ( ( PERCENT PERCENT )=> PERCENT t= PERCENT | ( DOLLAR DOLLAR )=> DOLLAR t= DOLLAR | t= QUOTE | ( BACKSLASH LPAREN )=> BACKSLASH t= LPAREN | ( BACKSLASH RPAREN )=> BACKSLASH t= RPAREN | t= BLANK )
        int alt74=6;
        int LA74_0 = input.LA(1);

        if ( (LA74_0==PERCENT) && (synpred111_WSL())) {
            alt74=1;
        }
        else if ( (LA74_0==DOLLAR) && (synpred112_WSL())) {
            alt74=2;
        }
        else if ( (LA74_0==QUOTE) ) {
            alt74=3;
        }
        else if ( (LA74_0==BACKSLASH) ) {
            int LA74_4 = input.LA(2);

            if ( (LA74_4==LPAREN) && (synpred114_WSL())) {
                alt74=4;
            }
            else if ( (LA74_4==RPAREN) && (synpred115_WSL())) {
                alt74=5;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("", 74, 4, input);

                throw nvae;
            }
        }
        else if ( (LA74_0==BLANK) ) {
            alt74=6;
        }
        else {
            if (state.backtracking>0) {state.failed=true; return ;}
            NoViableAltException nvae =
                new NoViableAltException("", 74, 0, input);

            throw nvae;
        }
        switch (alt74) {
            case 1 :
                // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:359:5: ( PERCENT PERCENT )=> PERCENT t= PERCENT
                {
                match(input,PERCENT,FOLLOW_PERCENT_in_synpred116_WSL1546); if (state.failed) return ;
                t=(Token)match(input,PERCENT,FOLLOW_PERCENT_in_synpred116_WSL1550); if (state.failed) return ;

                }
                break;
            case 2 :
                // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:360:5: ( DOLLAR DOLLAR )=> DOLLAR t= DOLLAR
                {
                match(input,DOLLAR,FOLLOW_DOLLAR_in_synpred116_WSL1563); if (state.failed) return ;
                t=(Token)match(input,DOLLAR,FOLLOW_DOLLAR_in_synpred116_WSL1567); if (state.failed) return ;

                }
                break;
            case 3 :
                // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:361:5: t= QUOTE
                {
                t=(Token)match(input,QUOTE,FOLLOW_QUOTE_in_synpred116_WSL1575); if (state.failed) return ;

                }
                break;
            case 4 :
                // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:361:15: ( BACKSLASH LPAREN )=> BACKSLASH t= LPAREN
                {
                match(input,BACKSLASH,FOLLOW_BACKSLASH_in_synpred116_WSL1586); if (state.failed) return ;
                t=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_synpred116_WSL1590); if (state.failed) return ;

                }
                break;
            case 5 :
                // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:362:5: ( BACKSLASH RPAREN )=> BACKSLASH t= RPAREN
                {
                match(input,BACKSLASH,FOLLOW_BACKSLASH_in_synpred116_WSL1603); if (state.failed) return ;
                t=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_synpred116_WSL1607); if (state.failed) return ;

                }
                break;
            case 6 :
                // /home/sproctor/git/warlock2/stormfront/cc.warlock.core.stormfront.script/src/main/cc/warlock/core/stormfront/script/wsl/WSL.g:362:47: t= BLANK
                {
                t=(Token)match(input,BLANK,FOLLOW_BLANK_in_synpred116_WSL1613); if (state.failed) return ;

                }
                break;

        }


        }
    }
    // $ANTLR end synpred116_WSL

    // Delegated rules

    public final boolean synpred4_WSL() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred4_WSL_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred65_WSL() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred65_WSL_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred9_WSL() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred9_WSL_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred66_WSL() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred66_WSL_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred16_WSL() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred16_WSL_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred111_WSL() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred111_WSL_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred10_WSL() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred10_WSL_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred8_WSL() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred8_WSL_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred91_WSL() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred91_WSL_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred102_WSL() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred102_WSL_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred15_WSL() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred15_WSL_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred103_WSL() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred103_WSL_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred12_WSL() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred12_WSL_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred99_WSL() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred99_WSL_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred7_WSL() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred7_WSL_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred58_WSL() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred58_WSL_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred115_WSL() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred115_WSL_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred51_WSL() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred51_WSL_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred92_WSL() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred92_WSL_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred94_WSL() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred94_WSL_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred57_WSL() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred57_WSL_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred110_WSL() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred110_WSL_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred116_WSL() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred116_WSL_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred112_WSL() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred112_WSL_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred14_WSL() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred14_WSL_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred114_WSL() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred114_WSL_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred93_WSL() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred93_WSL_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred11_WSL() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred11_WSL_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }


    protected DFA13 dfa13 = new DFA13(this);
    protected DFA6 dfa6 = new DFA6(this);
    protected DFA7 dfa7 = new DFA7(this);
    protected DFA11 dfa11 = new DFA11(this);
    protected DFA8 dfa8 = new DFA8(this);
    protected DFA10 dfa10 = new DFA10(this);
    protected DFA12 dfa12 = new DFA12(this);
    protected DFA16 dfa16 = new DFA16(this);
    protected DFA19 dfa19 = new DFA19(this);
    protected DFA22 dfa22 = new DFA22(this);
    protected DFA26 dfa26 = new DFA26(this);
    protected DFA39 dfa39 = new DFA39(this);
    protected DFA40 dfa40 = new DFA40(this);
    protected DFA42 dfa42 = new DFA42(this);
    protected DFA43 dfa43 = new DFA43(this);
    protected DFA44 dfa44 = new DFA44(this);
    protected DFA51 dfa51 = new DFA51(this);
    protected DFA54 dfa54 = new DFA54(this);
    static final String DFA13_eotS =
        "\43\uffff";
    static final String DFA13_eofS =
        "\43\uffff";
    static final String DFA13_minS =
        "\1\6\3\0\37\uffff";
    static final String DFA13_maxS =
        "\1\44\3\0\37\uffff";
    static final String DFA13_acceptS =
        "\4\uffff\1\4\33\uffff\1\1\1\2\1\3";
    static final String DFA13_specialS =
        "\1\uffff\1\0\1\1\1\2\37\uffff}>";
    static final String[] DFA13_transitionS = {
            "\1\1\2\4\1\2\3\4\1\3\27\4",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA13_eot = DFA.unpackEncodedString(DFA13_eotS);
    static final short[] DFA13_eof = DFA.unpackEncodedString(DFA13_eofS);
    static final char[] DFA13_min = DFA.unpackEncodedStringToUnsignedChars(DFA13_minS);
    static final char[] DFA13_max = DFA.unpackEncodedStringToUnsignedChars(DFA13_maxS);
    static final short[] DFA13_accept = DFA.unpackEncodedString(DFA13_acceptS);
    static final short[] DFA13_special = DFA.unpackEncodedString(DFA13_specialS);
    static final short[][] DFA13_transition;

    static {
        int numStates = DFA13_transitionS.length;
        DFA13_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA13_transition[i] = DFA.unpackEncodedString(DFA13_transitionS[i]);
        }
    }

    class DFA13 extends DFA {

        public DFA13(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 13;
            this.eot = DFA13_eot;
            this.eof = DFA13_eof;
            this.min = DFA13_min;
            this.max = DFA13_max;
            this.accept = DFA13_accept;
            this.special = DFA13_special;
            this.transition = DFA13_transition;
        }
        public String getDescription() {
            return "58:1: expr returns [WSLAbstractCommand command] : ( ( IF )=> IF ( BLANK )* cond= conditionalExpression ( BLANK )* THEN ( BLANK )* c= expr | ( ACTION )=> ACTION ( BLANK )* ( ( REMOVE )=> REMOVE ( BLANK )* args= string_list | ( CLEAR )=> CLEAR ( BLANK )* | c= expr WHEN ( BLANK )* args= string_list ) | ( INSTANT )=> INSTANT ( BLANK )* c= expr | args= string_list );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA13_1 = input.LA(1);

                         
                        int index13_1 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred4_WSL()) ) {s = 32;}

                        else if ( (true) ) {s = 4;}

                         
                        input.seek(index13_1);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA13_2 = input.LA(1);

                         
                        int index13_2 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred8_WSL()) ) {s = 33;}

                        else if ( (true) ) {s = 4;}

                         
                        input.seek(index13_2);
                        if ( s>=0 ) return s;
                        break;
                    case 2 : 
                        int LA13_3 = input.LA(1);

                         
                        int index13_3 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred15_WSL()) ) {s = 34;}

                        else if ( (true) ) {s = 4;}

                         
                        input.seek(index13_3);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 13, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA6_eotS =
        "\43\uffff";
    static final String DFA6_eofS =
        "\2\uffff\1\1\40\uffff";
    static final String DFA6_minS =
        "\1\6\1\uffff\1\4\37\0\1\uffff";
    static final String DFA6_maxS =
        "\1\44\1\uffff\1\44\37\0\1\uffff";
    static final String DFA6_acceptS =
        "\1\uffff\1\2\40\uffff\1\1";
    static final String DFA6_specialS =
        "\3\uffff\1\22\1\14\1\24\1\4\1\21\1\11\1\16\1\27\1\34\1\25\1\7\1"+
        "\12\1\1\1\5\1\36\1\33\1\30\1\23\1\15\1\10\1\6\1\3\1\35\1\31\1\20"+
        "\1\13\1\0\1\2\1\17\1\26\1\32\1\uffff}>";
    static final String[] DFA6_transitionS = {
            "\1\1\1\2\35\1",
            "",
            "\1\1\1\uffff\1\3\1\12\1\14\1\4\1\36\1\37\1\35\1\5\1\15\1\16"+
            "\1\21\1\17\1\24\1\25\1\22\1\23\1\31\1\32\1\20\1\30\1\11\1\10"+
            "\1\6\1\7\1\33\1\34\1\13\1\41\1\40\1\26\1\27",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            ""
    };

    static final short[] DFA6_eot = DFA.unpackEncodedString(DFA6_eotS);
    static final short[] DFA6_eof = DFA.unpackEncodedString(DFA6_eofS);
    static final char[] DFA6_min = DFA.unpackEncodedStringToUnsignedChars(DFA6_minS);
    static final char[] DFA6_max = DFA.unpackEncodedStringToUnsignedChars(DFA6_maxS);
    static final short[] DFA6_accept = DFA.unpackEncodedString(DFA6_acceptS);
    static final short[] DFA6_special = DFA.unpackEncodedString(DFA6_specialS);
    static final short[][] DFA6_transition;

    static {
        int numStates = DFA6_transitionS.length;
        DFA6_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA6_transition[i] = DFA.unpackEncodedString(DFA6_transitionS[i]);
        }
    }

    class DFA6 extends DFA {

        public DFA6(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 6;
            this.eot = DFA6_eot;
            this.eof = DFA6_eof;
            this.min = DFA6_min;
            this.max = DFA6_max;
            this.accept = DFA6_accept;
            this.special = DFA6_special;
            this.transition = DFA6_transition;
        }
        public String getDescription() {
            return "()* loopback of 59:60: ( BLANK )*";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA6_29 = input.LA(1);

                         
                        int index6_29 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred7_WSL()) ) {s = 34;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index6_29);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA6_15 = input.LA(1);

                         
                        int index6_15 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred7_WSL()) ) {s = 34;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index6_15);
                        if ( s>=0 ) return s;
                        break;
                    case 2 : 
                        int LA6_30 = input.LA(1);

                         
                        int index6_30 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred7_WSL()) ) {s = 34;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index6_30);
                        if ( s>=0 ) return s;
                        break;
                    case 3 : 
                        int LA6_24 = input.LA(1);

                         
                        int index6_24 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred7_WSL()) ) {s = 34;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index6_24);
                        if ( s>=0 ) return s;
                        break;
                    case 4 : 
                        int LA6_6 = input.LA(1);

                         
                        int index6_6 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred7_WSL()) ) {s = 34;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index6_6);
                        if ( s>=0 ) return s;
                        break;
                    case 5 : 
                        int LA6_16 = input.LA(1);

                         
                        int index6_16 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred7_WSL()) ) {s = 34;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index6_16);
                        if ( s>=0 ) return s;
                        break;
                    case 6 : 
                        int LA6_23 = input.LA(1);

                         
                        int index6_23 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred7_WSL()) ) {s = 34;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index6_23);
                        if ( s>=0 ) return s;
                        break;
                    case 7 : 
                        int LA6_13 = input.LA(1);

                         
                        int index6_13 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred7_WSL()) ) {s = 34;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index6_13);
                        if ( s>=0 ) return s;
                        break;
                    case 8 : 
                        int LA6_22 = input.LA(1);

                         
                        int index6_22 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred7_WSL()) ) {s = 34;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index6_22);
                        if ( s>=0 ) return s;
                        break;
                    case 9 : 
                        int LA6_8 = input.LA(1);

                         
                        int index6_8 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred7_WSL()) ) {s = 34;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index6_8);
                        if ( s>=0 ) return s;
                        break;
                    case 10 : 
                        int LA6_14 = input.LA(1);

                         
                        int index6_14 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred7_WSL()) ) {s = 34;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index6_14);
                        if ( s>=0 ) return s;
                        break;
                    case 11 : 
                        int LA6_28 = input.LA(1);

                         
                        int index6_28 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred7_WSL()) ) {s = 34;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index6_28);
                        if ( s>=0 ) return s;
                        break;
                    case 12 : 
                        int LA6_4 = input.LA(1);

                         
                        int index6_4 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred7_WSL()) ) {s = 34;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index6_4);
                        if ( s>=0 ) return s;
                        break;
                    case 13 : 
                        int LA6_21 = input.LA(1);

                         
                        int index6_21 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred7_WSL()) ) {s = 34;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index6_21);
                        if ( s>=0 ) return s;
                        break;
                    case 14 : 
                        int LA6_9 = input.LA(1);

                         
                        int index6_9 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred7_WSL()) ) {s = 34;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index6_9);
                        if ( s>=0 ) return s;
                        break;
                    case 15 : 
                        int LA6_31 = input.LA(1);

                         
                        int index6_31 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred7_WSL()) ) {s = 34;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index6_31);
                        if ( s>=0 ) return s;
                        break;
                    case 16 : 
                        int LA6_27 = input.LA(1);

                         
                        int index6_27 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred7_WSL()) ) {s = 34;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index6_27);
                        if ( s>=0 ) return s;
                        break;
                    case 17 : 
                        int LA6_7 = input.LA(1);

                         
                        int index6_7 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred7_WSL()) ) {s = 34;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index6_7);
                        if ( s>=0 ) return s;
                        break;
                    case 18 : 
                        int LA6_3 = input.LA(1);

                         
                        int index6_3 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred7_WSL()) ) {s = 34;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index6_3);
                        if ( s>=0 ) return s;
                        break;
                    case 19 : 
                        int LA6_20 = input.LA(1);

                         
                        int index6_20 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred7_WSL()) ) {s = 34;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index6_20);
                        if ( s>=0 ) return s;
                        break;
                    case 20 : 
                        int LA6_5 = input.LA(1);

                         
                        int index6_5 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred7_WSL()) ) {s = 34;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index6_5);
                        if ( s>=0 ) return s;
                        break;
                    case 21 : 
                        int LA6_12 = input.LA(1);

                         
                        int index6_12 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred7_WSL()) ) {s = 34;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index6_12);
                        if ( s>=0 ) return s;
                        break;
                    case 22 : 
                        int LA6_32 = input.LA(1);

                         
                        int index6_32 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred7_WSL()) ) {s = 34;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index6_32);
                        if ( s>=0 ) return s;
                        break;
                    case 23 : 
                        int LA6_10 = input.LA(1);

                         
                        int index6_10 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred7_WSL()) ) {s = 34;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index6_10);
                        if ( s>=0 ) return s;
                        break;
                    case 24 : 
                        int LA6_19 = input.LA(1);

                         
                        int index6_19 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred7_WSL()) ) {s = 34;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index6_19);
                        if ( s>=0 ) return s;
                        break;
                    case 25 : 
                        int LA6_26 = input.LA(1);

                         
                        int index6_26 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred7_WSL()) ) {s = 34;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index6_26);
                        if ( s>=0 ) return s;
                        break;
                    case 26 : 
                        int LA6_33 = input.LA(1);

                         
                        int index6_33 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred7_WSL()) ) {s = 34;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index6_33);
                        if ( s>=0 ) return s;
                        break;
                    case 27 : 
                        int LA6_18 = input.LA(1);

                         
                        int index6_18 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred7_WSL()) ) {s = 34;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index6_18);
                        if ( s>=0 ) return s;
                        break;
                    case 28 : 
                        int LA6_11 = input.LA(1);

                         
                        int index6_11 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred7_WSL()) ) {s = 34;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index6_11);
                        if ( s>=0 ) return s;
                        break;
                    case 29 : 
                        int LA6_25 = input.LA(1);

                         
                        int index6_25 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred7_WSL()) ) {s = 34;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index6_25);
                        if ( s>=0 ) return s;
                        break;
                    case 30 : 
                        int LA6_17 = input.LA(1);

                         
                        int index6_17 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred7_WSL()) ) {s = 34;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index6_17);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 6, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA7_eotS =
        "\41\uffff";
    static final String DFA7_eofS =
        "\41\uffff";
    static final String DFA7_minS =
        "\1\6\11\uffff\1\0\26\uffff";
    static final String DFA7_maxS =
        "\1\44\11\uffff\1\0\26\uffff";
    static final String DFA7_acceptS =
        "\1\uffff\1\2\36\uffff\1\1";
    static final String DFA7_specialS =
        "\12\uffff\1\0\26\uffff}>";
    static final String[] DFA7_transitionS = {
            "\1\1\1\12\35\1",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\uffff",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA7_eot = DFA.unpackEncodedString(DFA7_eotS);
    static final short[] DFA7_eof = DFA.unpackEncodedString(DFA7_eofS);
    static final char[] DFA7_min = DFA.unpackEncodedStringToUnsignedChars(DFA7_minS);
    static final char[] DFA7_max = DFA.unpackEncodedStringToUnsignedChars(DFA7_maxS);
    static final short[] DFA7_accept = DFA.unpackEncodedString(DFA7_acceptS);
    static final short[] DFA7_special = DFA.unpackEncodedString(DFA7_specialS);
    static final short[][] DFA7_transition;

    static {
        int numStates = DFA7_transitionS.length;
        DFA7_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA7_transition[i] = DFA.unpackEncodedString(DFA7_transitionS[i]);
        }
    }

    class DFA7 extends DFA {

        public DFA7(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 7;
            this.eot = DFA7_eot;
            this.eof = DFA7_eof;
            this.min = DFA7_min;
            this.max = DFA7_max;
            this.accept = DFA7_accept;
            this.special = DFA7_special;
            this.transition = DFA7_transition;
        }
        public String getDescription() {
            return "()* loopback of 63:22: ( BLANK )*";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA7_10 = input.LA(1);

                         
                        int index7_10 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred9_WSL()) ) {s = 32;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index7_10);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 7, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA11_eotS =
        "\45\uffff";
    static final String DFA11_eofS =
        "\45\uffff";
    static final String DFA11_minS =
        "\2\6\1\0\1\uffff\37\0\2\uffff";
    static final String DFA11_maxS =
        "\2\44\1\0\1\uffff\37\0\2\uffff";
    static final String DFA11_acceptS =
        "\3\uffff\1\3\37\uffff\1\2\1\1";
    static final String DFA11_specialS =
        "\2\uffff\1\33\1\uffff\1\17\1\32\1\11\1\15\1\22\1\35\1\1\1\4\1\7"+
        "\1\12\1\16\1\23\1\26\1\34\1\5\1\2\1\14\1\10\1\24\1\21\1\37\1\31"+
        "\1\3\1\0\1\13\1\25\1\20\1\36\1\30\1\27\1\6\2\uffff}>";
    static final String[] DFA11_transitionS = {
            "\4\3\1\1\1\2\31\3",
            "\1\12\1\10\1\13\1\32\1\36\1\37\1\35\1\40\1\14\1\15\1\20\1\16"+
            "\1\23\1\24\1\21\1\22\1\30\1\31\1\17\1\27\1\7\1\6\1\4\1\5\1\33"+
            "\1\34\1\11\1\42\1\41\1\25\1\26",
            "\1\uffff",
            "",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "",
            ""
    };

    static final short[] DFA11_eot = DFA.unpackEncodedString(DFA11_eotS);
    static final short[] DFA11_eof = DFA.unpackEncodedString(DFA11_eofS);
    static final char[] DFA11_min = DFA.unpackEncodedStringToUnsignedChars(DFA11_minS);
    static final char[] DFA11_max = DFA.unpackEncodedStringToUnsignedChars(DFA11_maxS);
    static final short[] DFA11_accept = DFA.unpackEncodedString(DFA11_acceptS);
    static final short[] DFA11_special = DFA.unpackEncodedString(DFA11_specialS);
    static final short[][] DFA11_transition;

    static {
        int numStates = DFA11_transitionS.length;
        DFA11_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA11_transition[i] = DFA.unpackEncodedString(DFA11_transitionS[i]);
        }
    }

    class DFA11 extends DFA {

        public DFA11(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 11;
            this.eot = DFA11_eot;
            this.eof = DFA11_eof;
            this.min = DFA11_min;
            this.max = DFA11_max;
            this.accept = DFA11_accept;
            this.special = DFA11_special;
            this.transition = DFA11_transition;
        }
        public String getDescription() {
            return "64:3: ( ( REMOVE )=> REMOVE ( BLANK )* args= string_list | ( CLEAR )=> CLEAR ( BLANK )* | c= expr WHEN ( BLANK )* args= string_list )";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA11_27 = input.LA(1);

                         
                        int index11_27 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred10_WSL()) ) {s = 36;}

                        else if ( (true) ) {s = 3;}

                         
                        input.seek(index11_27);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA11_10 = input.LA(1);

                         
                        int index11_10 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred10_WSL()) ) {s = 36;}

                        else if ( (true) ) {s = 3;}

                         
                        input.seek(index11_10);
                        if ( s>=0 ) return s;
                        break;
                    case 2 : 
                        int LA11_19 = input.LA(1);

                         
                        int index11_19 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred10_WSL()) ) {s = 36;}

                        else if ( (true) ) {s = 3;}

                         
                        input.seek(index11_19);
                        if ( s>=0 ) return s;
                        break;
                    case 3 : 
                        int LA11_26 = input.LA(1);

                         
                        int index11_26 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred10_WSL()) ) {s = 36;}

                        else if ( (true) ) {s = 3;}

                         
                        input.seek(index11_26);
                        if ( s>=0 ) return s;
                        break;
                    case 4 : 
                        int LA11_11 = input.LA(1);

                         
                        int index11_11 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred10_WSL()) ) {s = 36;}

                        else if ( (true) ) {s = 3;}

                         
                        input.seek(index11_11);
                        if ( s>=0 ) return s;
                        break;
                    case 5 : 
                        int LA11_18 = input.LA(1);

                         
                        int index11_18 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred10_WSL()) ) {s = 36;}

                        else if ( (true) ) {s = 3;}

                         
                        input.seek(index11_18);
                        if ( s>=0 ) return s;
                        break;
                    case 6 : 
                        int LA11_34 = input.LA(1);

                         
                        int index11_34 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred10_WSL()) ) {s = 36;}

                        else if ( (true) ) {s = 3;}

                         
                        input.seek(index11_34);
                        if ( s>=0 ) return s;
                        break;
                    case 7 : 
                        int LA11_12 = input.LA(1);

                         
                        int index11_12 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred10_WSL()) ) {s = 36;}

                        else if ( (true) ) {s = 3;}

                         
                        input.seek(index11_12);
                        if ( s>=0 ) return s;
                        break;
                    case 8 : 
                        int LA11_21 = input.LA(1);

                         
                        int index11_21 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred10_WSL()) ) {s = 36;}

                        else if ( (true) ) {s = 3;}

                         
                        input.seek(index11_21);
                        if ( s>=0 ) return s;
                        break;
                    case 9 : 
                        int LA11_6 = input.LA(1);

                         
                        int index11_6 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred10_WSL()) ) {s = 36;}

                        else if ( (true) ) {s = 3;}

                         
                        input.seek(index11_6);
                        if ( s>=0 ) return s;
                        break;
                    case 10 : 
                        int LA11_13 = input.LA(1);

                         
                        int index11_13 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred10_WSL()) ) {s = 36;}

                        else if ( (true) ) {s = 3;}

                         
                        input.seek(index11_13);
                        if ( s>=0 ) return s;
                        break;
                    case 11 : 
                        int LA11_28 = input.LA(1);

                         
                        int index11_28 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred10_WSL()) ) {s = 36;}

                        else if ( (true) ) {s = 3;}

                         
                        input.seek(index11_28);
                        if ( s>=0 ) return s;
                        break;
                    case 12 : 
                        int LA11_20 = input.LA(1);

                         
                        int index11_20 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred10_WSL()) ) {s = 36;}

                        else if ( (true) ) {s = 3;}

                         
                        input.seek(index11_20);
                        if ( s>=0 ) return s;
                        break;
                    case 13 : 
                        int LA11_7 = input.LA(1);

                         
                        int index11_7 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred10_WSL()) ) {s = 36;}

                        else if ( (true) ) {s = 3;}

                         
                        input.seek(index11_7);
                        if ( s>=0 ) return s;
                        break;
                    case 14 : 
                        int LA11_14 = input.LA(1);

                         
                        int index11_14 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred10_WSL()) ) {s = 36;}

                        else if ( (true) ) {s = 3;}

                         
                        input.seek(index11_14);
                        if ( s>=0 ) return s;
                        break;
                    case 15 : 
                        int LA11_4 = input.LA(1);

                         
                        int index11_4 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred10_WSL()) ) {s = 36;}

                        else if ( (true) ) {s = 3;}

                         
                        input.seek(index11_4);
                        if ( s>=0 ) return s;
                        break;
                    case 16 : 
                        int LA11_30 = input.LA(1);

                         
                        int index11_30 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred10_WSL()) ) {s = 36;}

                        else if ( (true) ) {s = 3;}

                         
                        input.seek(index11_30);
                        if ( s>=0 ) return s;
                        break;
                    case 17 : 
                        int LA11_23 = input.LA(1);

                         
                        int index11_23 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred10_WSL()) ) {s = 36;}

                        else if ( (true) ) {s = 3;}

                         
                        input.seek(index11_23);
                        if ( s>=0 ) return s;
                        break;
                    case 18 : 
                        int LA11_8 = input.LA(1);

                         
                        int index11_8 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred10_WSL()) ) {s = 36;}

                        else if ( (true) ) {s = 3;}

                         
                        input.seek(index11_8);
                        if ( s>=0 ) return s;
                        break;
                    case 19 : 
                        int LA11_15 = input.LA(1);

                         
                        int index11_15 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred10_WSL()) ) {s = 36;}

                        else if ( (true) ) {s = 3;}

                         
                        input.seek(index11_15);
                        if ( s>=0 ) return s;
                        break;
                    case 20 : 
                        int LA11_22 = input.LA(1);

                         
                        int index11_22 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred10_WSL()) ) {s = 36;}

                        else if ( (true) ) {s = 3;}

                         
                        input.seek(index11_22);
                        if ( s>=0 ) return s;
                        break;
                    case 21 : 
                        int LA11_29 = input.LA(1);

                         
                        int index11_29 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred10_WSL()) ) {s = 36;}

                        else if ( (true) ) {s = 3;}

                         
                        input.seek(index11_29);
                        if ( s>=0 ) return s;
                        break;
                    case 22 : 
                        int LA11_16 = input.LA(1);

                         
                        int index11_16 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred10_WSL()) ) {s = 36;}

                        else if ( (true) ) {s = 3;}

                         
                        input.seek(index11_16);
                        if ( s>=0 ) return s;
                        break;
                    case 23 : 
                        int LA11_33 = input.LA(1);

                         
                        int index11_33 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred10_WSL()) ) {s = 36;}

                        else if ( (true) ) {s = 3;}

                         
                        input.seek(index11_33);
                        if ( s>=0 ) return s;
                        break;
                    case 24 : 
                        int LA11_32 = input.LA(1);

                         
                        int index11_32 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred10_WSL()) ) {s = 36;}

                        else if ( (true) ) {s = 3;}

                         
                        input.seek(index11_32);
                        if ( s>=0 ) return s;
                        break;
                    case 25 : 
                        int LA11_25 = input.LA(1);

                         
                        int index11_25 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred10_WSL()) ) {s = 36;}

                        else if ( (true) ) {s = 3;}

                         
                        input.seek(index11_25);
                        if ( s>=0 ) return s;
                        break;
                    case 26 : 
                        int LA11_5 = input.LA(1);

                         
                        int index11_5 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred10_WSL()) ) {s = 36;}

                        else if ( (true) ) {s = 3;}

                         
                        input.seek(index11_5);
                        if ( s>=0 ) return s;
                        break;
                    case 27 : 
                        int LA11_2 = input.LA(1);

                         
                        int index11_2 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred12_WSL()) ) {s = 35;}

                        else if ( (true) ) {s = 3;}

                         
                        input.seek(index11_2);
                        if ( s>=0 ) return s;
                        break;
                    case 28 : 
                        int LA11_17 = input.LA(1);

                         
                        int index11_17 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred10_WSL()) ) {s = 36;}

                        else if ( (true) ) {s = 3;}

                         
                        input.seek(index11_17);
                        if ( s>=0 ) return s;
                        break;
                    case 29 : 
                        int LA11_9 = input.LA(1);

                         
                        int index11_9 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred10_WSL()) ) {s = 36;}

                        else if ( (true) ) {s = 3;}

                         
                        input.seek(index11_9);
                        if ( s>=0 ) return s;
                        break;
                    case 30 : 
                        int LA11_31 = input.LA(1);

                         
                        int index11_31 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred10_WSL()) ) {s = 36;}

                        else if ( (true) ) {s = 3;}

                         
                        input.seek(index11_31);
                        if ( s>=0 ) return s;
                        break;
                    case 31 : 
                        int LA11_24 = input.LA(1);

                         
                        int index11_24 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred10_WSL()) ) {s = 36;}

                        else if ( (true) ) {s = 3;}

                         
                        input.seek(index11_24);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 11, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA8_eotS =
        "\43\uffff";
    static final String DFA8_eofS =
        "\2\uffff\1\1\40\uffff";
    static final String DFA8_minS =
        "\1\6\1\uffff\1\4\37\0\1\uffff";
    static final String DFA8_maxS =
        "\1\44\1\uffff\1\44\37\0\1\uffff";
    static final String DFA8_acceptS =
        "\1\uffff\1\2\40\uffff\1\1";
    static final String DFA8_specialS =
        "\3\uffff\1\25\1\23\1\13\1\17\1\32\1\0\1\35\1\31\1\26\1\21\1\20\1"+
        "\14\1\10\1\3\1\36\1\33\1\27\1\24\1\12\1\16\1\2\1\6\1\30\1\34\1\22"+
        "\1\4\1\15\1\1\1\5\1\11\1\7\1\uffff}>";
    static final String[] DFA8_transitionS = {
            "\1\1\1\2\35\1",
            "",
            "\1\1\1\uffff\1\11\1\7\1\12\1\31\1\35\1\36\1\34\1\37\1\13\1"+
            "\14\1\17\1\15\1\22\1\23\1\20\1\21\1\27\1\30\1\16\1\26\1\6\1"+
            "\5\1\3\1\4\1\32\1\33\1\10\1\41\1\40\1\24\1\25",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            ""
    };

    static final short[] DFA8_eot = DFA.unpackEncodedString(DFA8_eotS);
    static final short[] DFA8_eof = DFA.unpackEncodedString(DFA8_eofS);
    static final char[] DFA8_min = DFA.unpackEncodedStringToUnsignedChars(DFA8_minS);
    static final char[] DFA8_max = DFA.unpackEncodedStringToUnsignedChars(DFA8_maxS);
    static final short[] DFA8_accept = DFA.unpackEncodedString(DFA8_acceptS);
    static final short[] DFA8_special = DFA.unpackEncodedString(DFA8_specialS);
    static final short[][] DFA8_transition;

    static {
        int numStates = DFA8_transitionS.length;
        DFA8_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA8_transition[i] = DFA.unpackEncodedString(DFA8_transitionS[i]);
        }
    }

    class DFA8 extends DFA {

        public DFA8(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 8;
            this.eot = DFA8_eot;
            this.eof = DFA8_eof;
            this.min = DFA8_min;
            this.max = DFA8_max;
            this.accept = DFA8_accept;
            this.special = DFA8_special;
            this.transition = DFA8_transition;
        }
        public String getDescription() {
            return "()* loopback of 64:23: ( BLANK )*";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA8_8 = input.LA(1);

                         
                        int index8_8 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred11_WSL()) ) {s = 34;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index8_8);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA8_30 = input.LA(1);

                         
                        int index8_30 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred11_WSL()) ) {s = 34;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index8_30);
                        if ( s>=0 ) return s;
                        break;
                    case 2 : 
                        int LA8_23 = input.LA(1);

                         
                        int index8_23 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred11_WSL()) ) {s = 34;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index8_23);
                        if ( s>=0 ) return s;
                        break;
                    case 3 : 
                        int LA8_16 = input.LA(1);

                         
                        int index8_16 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred11_WSL()) ) {s = 34;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index8_16);
                        if ( s>=0 ) return s;
                        break;
                    case 4 : 
                        int LA8_28 = input.LA(1);

                         
                        int index8_28 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred11_WSL()) ) {s = 34;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index8_28);
                        if ( s>=0 ) return s;
                        break;
                    case 5 : 
                        int LA8_31 = input.LA(1);

                         
                        int index8_31 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred11_WSL()) ) {s = 34;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index8_31);
                        if ( s>=0 ) return s;
                        break;
                    case 6 : 
                        int LA8_24 = input.LA(1);

                         
                        int index8_24 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred11_WSL()) ) {s = 34;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index8_24);
                        if ( s>=0 ) return s;
                        break;
                    case 7 : 
                        int LA8_33 = input.LA(1);

                         
                        int index8_33 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred11_WSL()) ) {s = 34;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index8_33);
                        if ( s>=0 ) return s;
                        break;
                    case 8 : 
                        int LA8_15 = input.LA(1);

                         
                        int index8_15 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred11_WSL()) ) {s = 34;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index8_15);
                        if ( s>=0 ) return s;
                        break;
                    case 9 : 
                        int LA8_32 = input.LA(1);

                         
                        int index8_32 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred11_WSL()) ) {s = 34;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index8_32);
                        if ( s>=0 ) return s;
                        break;
                    case 10 : 
                        int LA8_21 = input.LA(1);

                         
                        int index8_21 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred11_WSL()) ) {s = 34;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index8_21);
                        if ( s>=0 ) return s;
                        break;
                    case 11 : 
                        int LA8_5 = input.LA(1);

                         
                        int index8_5 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred11_WSL()) ) {s = 34;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index8_5);
                        if ( s>=0 ) return s;
                        break;
                    case 12 : 
                        int LA8_14 = input.LA(1);

                         
                        int index8_14 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred11_WSL()) ) {s = 34;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index8_14);
                        if ( s>=0 ) return s;
                        break;
                    case 13 : 
                        int LA8_29 = input.LA(1);

                         
                        int index8_29 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred11_WSL()) ) {s = 34;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index8_29);
                        if ( s>=0 ) return s;
                        break;
                    case 14 : 
                        int LA8_22 = input.LA(1);

                         
                        int index8_22 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred11_WSL()) ) {s = 34;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index8_22);
                        if ( s>=0 ) return s;
                        break;
                    case 15 : 
                        int LA8_6 = input.LA(1);

                         
                        int index8_6 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred11_WSL()) ) {s = 34;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index8_6);
                        if ( s>=0 ) return s;
                        break;
                    case 16 : 
                        int LA8_13 = input.LA(1);

                         
                        int index8_13 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred11_WSL()) ) {s = 34;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index8_13);
                        if ( s>=0 ) return s;
                        break;
                    case 17 : 
                        int LA8_12 = input.LA(1);

                         
                        int index8_12 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred11_WSL()) ) {s = 34;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index8_12);
                        if ( s>=0 ) return s;
                        break;
                    case 18 : 
                        int LA8_27 = input.LA(1);

                         
                        int index8_27 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred11_WSL()) ) {s = 34;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index8_27);
                        if ( s>=0 ) return s;
                        break;
                    case 19 : 
                        int LA8_4 = input.LA(1);

                         
                        int index8_4 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred11_WSL()) ) {s = 34;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index8_4);
                        if ( s>=0 ) return s;
                        break;
                    case 20 : 
                        int LA8_20 = input.LA(1);

                         
                        int index8_20 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred11_WSL()) ) {s = 34;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index8_20);
                        if ( s>=0 ) return s;
                        break;
                    case 21 : 
                        int LA8_3 = input.LA(1);

                         
                        int index8_3 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred11_WSL()) ) {s = 34;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index8_3);
                        if ( s>=0 ) return s;
                        break;
                    case 22 : 
                        int LA8_11 = input.LA(1);

                         
                        int index8_11 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred11_WSL()) ) {s = 34;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index8_11);
                        if ( s>=0 ) return s;
                        break;
                    case 23 : 
                        int LA8_19 = input.LA(1);

                         
                        int index8_19 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred11_WSL()) ) {s = 34;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index8_19);
                        if ( s>=0 ) return s;
                        break;
                    case 24 : 
                        int LA8_25 = input.LA(1);

                         
                        int index8_25 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred11_WSL()) ) {s = 34;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index8_25);
                        if ( s>=0 ) return s;
                        break;
                    case 25 : 
                        int LA8_10 = input.LA(1);

                         
                        int index8_10 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred11_WSL()) ) {s = 34;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index8_10);
                        if ( s>=0 ) return s;
                        break;
                    case 26 : 
                        int LA8_7 = input.LA(1);

                         
                        int index8_7 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred11_WSL()) ) {s = 34;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index8_7);
                        if ( s>=0 ) return s;
                        break;
                    case 27 : 
                        int LA8_18 = input.LA(1);

                         
                        int index8_18 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred11_WSL()) ) {s = 34;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index8_18);
                        if ( s>=0 ) return s;
                        break;
                    case 28 : 
                        int LA8_26 = input.LA(1);

                         
                        int index8_26 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred11_WSL()) ) {s = 34;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index8_26);
                        if ( s>=0 ) return s;
                        break;
                    case 29 : 
                        int LA8_9 = input.LA(1);

                         
                        int index8_9 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred11_WSL()) ) {s = 34;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index8_9);
                        if ( s>=0 ) return s;
                        break;
                    case 30 : 
                        int LA8_17 = input.LA(1);

                         
                        int index8_17 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred11_WSL()) ) {s = 34;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index8_17);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 8, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA10_eotS =
        "\43\uffff";
    static final String DFA10_eofS =
        "\2\uffff\1\1\40\uffff";
    static final String DFA10_minS =
        "\1\6\1\uffff\1\4\37\0\1\uffff";
    static final String DFA10_maxS =
        "\1\44\1\uffff\1\44\37\0\1\uffff";
    static final String DFA10_acceptS =
        "\1\uffff\1\2\40\uffff\1\1";
    static final String DFA10_specialS =
        "\3\uffff\1\30\1\23\1\33\1\15\1\14\1\27\1\34\1\16\1\22\1\6\1\12\1"+
        "\2\1\4\1\31\1\35\1\21\1\26\1\32\1\36\1\20\1\25\1\11\1\13\1\1\1\3"+
        "\1\10\1\17\1\24\1\7\1\5\1\0\1\uffff}>";
    static final String[] DFA10_transitionS = {
            "\1\1\1\2\35\1",
            "",
            "\1\1\1\uffff\1\11\1\7\1\12\1\31\1\35\1\36\1\34\1\37\1\13\1"+
            "\14\1\17\1\15\1\22\1\23\1\20\1\21\1\27\1\30\1\16\1\26\1\6\1"+
            "\5\1\3\1\4\1\32\1\33\1\10\1\41\1\40\1\24\1\25",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            ""
    };

    static final short[] DFA10_eot = DFA.unpackEncodedString(DFA10_eotS);
    static final short[] DFA10_eof = DFA.unpackEncodedString(DFA10_eofS);
    static final char[] DFA10_min = DFA.unpackEncodedStringToUnsignedChars(DFA10_minS);
    static final char[] DFA10_max = DFA.unpackEncodedStringToUnsignedChars(DFA10_maxS);
    static final short[] DFA10_accept = DFA.unpackEncodedString(DFA10_acceptS);
    static final short[] DFA10_special = DFA.unpackEncodedString(DFA10_specialS);
    static final short[][] DFA10_transition;

    static {
        int numStates = DFA10_transitionS.length;
        DFA10_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA10_transition[i] = DFA.unpackEncodedString(DFA10_transitionS[i]);
        }
    }

    class DFA10 extends DFA {

        public DFA10(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 10;
            this.eot = DFA10_eot;
            this.eof = DFA10_eof;
            this.min = DFA10_min;
            this.max = DFA10_max;
            this.accept = DFA10_accept;
            this.special = DFA10_special;
            this.transition = DFA10_transition;
        }
        public String getDescription() {
            return "()* loopback of 73:17: ( BLANK )*";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA10_33 = input.LA(1);

                         
                        int index10_33 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred14_WSL()) ) {s = 34;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index10_33);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA10_26 = input.LA(1);

                         
                        int index10_26 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred14_WSL()) ) {s = 34;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index10_26);
                        if ( s>=0 ) return s;
                        break;
                    case 2 : 
                        int LA10_14 = input.LA(1);

                         
                        int index10_14 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred14_WSL()) ) {s = 34;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index10_14);
                        if ( s>=0 ) return s;
                        break;
                    case 3 : 
                        int LA10_27 = input.LA(1);

                         
                        int index10_27 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred14_WSL()) ) {s = 34;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index10_27);
                        if ( s>=0 ) return s;
                        break;
                    case 4 : 
                        int LA10_15 = input.LA(1);

                         
                        int index10_15 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred14_WSL()) ) {s = 34;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index10_15);
                        if ( s>=0 ) return s;
                        break;
                    case 5 : 
                        int LA10_32 = input.LA(1);

                         
                        int index10_32 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred14_WSL()) ) {s = 34;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index10_32);
                        if ( s>=0 ) return s;
                        break;
                    case 6 : 
                        int LA10_12 = input.LA(1);

                         
                        int index10_12 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred14_WSL()) ) {s = 34;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index10_12);
                        if ( s>=0 ) return s;
                        break;
                    case 7 : 
                        int LA10_31 = input.LA(1);

                         
                        int index10_31 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred14_WSL()) ) {s = 34;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index10_31);
                        if ( s>=0 ) return s;
                        break;
                    case 8 : 
                        int LA10_28 = input.LA(1);

                         
                        int index10_28 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred14_WSL()) ) {s = 34;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index10_28);
                        if ( s>=0 ) return s;
                        break;
                    case 9 : 
                        int LA10_24 = input.LA(1);

                         
                        int index10_24 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred14_WSL()) ) {s = 34;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index10_24);
                        if ( s>=0 ) return s;
                        break;
                    case 10 : 
                        int LA10_13 = input.LA(1);

                         
                        int index10_13 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred14_WSL()) ) {s = 34;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index10_13);
                        if ( s>=0 ) return s;
                        break;
                    case 11 : 
                        int LA10_25 = input.LA(1);

                         
                        int index10_25 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred14_WSL()) ) {s = 34;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index10_25);
                        if ( s>=0 ) return s;
                        break;
                    case 12 : 
                        int LA10_7 = input.LA(1);

                         
                        int index10_7 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred14_WSL()) ) {s = 34;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index10_7);
                        if ( s>=0 ) return s;
                        break;
                    case 13 : 
                        int LA10_6 = input.LA(1);

                         
                        int index10_6 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred14_WSL()) ) {s = 34;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index10_6);
                        if ( s>=0 ) return s;
                        break;
                    case 14 : 
                        int LA10_10 = input.LA(1);

                         
                        int index10_10 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred14_WSL()) ) {s = 34;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index10_10);
                        if ( s>=0 ) return s;
                        break;
                    case 15 : 
                        int LA10_29 = input.LA(1);

                         
                        int index10_29 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred14_WSL()) ) {s = 34;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index10_29);
                        if ( s>=0 ) return s;
                        break;
                    case 16 : 
                        int LA10_22 = input.LA(1);

                         
                        int index10_22 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred14_WSL()) ) {s = 34;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index10_22);
                        if ( s>=0 ) return s;
                        break;
                    case 17 : 
                        int LA10_18 = input.LA(1);

                         
                        int index10_18 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred14_WSL()) ) {s = 34;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index10_18);
                        if ( s>=0 ) return s;
                        break;
                    case 18 : 
                        int LA10_11 = input.LA(1);

                         
                        int index10_11 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred14_WSL()) ) {s = 34;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index10_11);
                        if ( s>=0 ) return s;
                        break;
                    case 19 : 
                        int LA10_4 = input.LA(1);

                         
                        int index10_4 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred14_WSL()) ) {s = 34;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index10_4);
                        if ( s>=0 ) return s;
                        break;
                    case 20 : 
                        int LA10_30 = input.LA(1);

                         
                        int index10_30 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred14_WSL()) ) {s = 34;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index10_30);
                        if ( s>=0 ) return s;
                        break;
                    case 21 : 
                        int LA10_23 = input.LA(1);

                         
                        int index10_23 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred14_WSL()) ) {s = 34;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index10_23);
                        if ( s>=0 ) return s;
                        break;
                    case 22 : 
                        int LA10_19 = input.LA(1);

                         
                        int index10_19 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred14_WSL()) ) {s = 34;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index10_19);
                        if ( s>=0 ) return s;
                        break;
                    case 23 : 
                        int LA10_8 = input.LA(1);

                         
                        int index10_8 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred14_WSL()) ) {s = 34;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index10_8);
                        if ( s>=0 ) return s;
                        break;
                    case 24 : 
                        int LA10_3 = input.LA(1);

                         
                        int index10_3 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred14_WSL()) ) {s = 34;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index10_3);
                        if ( s>=0 ) return s;
                        break;
                    case 25 : 
                        int LA10_16 = input.LA(1);

                         
                        int index10_16 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred14_WSL()) ) {s = 34;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index10_16);
                        if ( s>=0 ) return s;
                        break;
                    case 26 : 
                        int LA10_20 = input.LA(1);

                         
                        int index10_20 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred14_WSL()) ) {s = 34;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index10_20);
                        if ( s>=0 ) return s;
                        break;
                    case 27 : 
                        int LA10_5 = input.LA(1);

                         
                        int index10_5 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred14_WSL()) ) {s = 34;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index10_5);
                        if ( s>=0 ) return s;
                        break;
                    case 28 : 
                        int LA10_9 = input.LA(1);

                         
                        int index10_9 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred14_WSL()) ) {s = 34;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index10_9);
                        if ( s>=0 ) return s;
                        break;
                    case 29 : 
                        int LA10_17 = input.LA(1);

                         
                        int index10_17 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred14_WSL()) ) {s = 34;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index10_17);
                        if ( s>=0 ) return s;
                        break;
                    case 30 : 
                        int LA10_21 = input.LA(1);

                         
                        int index10_21 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred14_WSL()) ) {s = 34;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index10_21);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 10, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA12_eotS =
        "\41\uffff";
    static final String DFA12_eofS =
        "\41\uffff";
    static final String DFA12_minS =
        "\1\6\7\uffff\1\0\30\uffff";
    static final String DFA12_maxS =
        "\1\44\7\uffff\1\0\30\uffff";
    static final String DFA12_acceptS =
        "\1\uffff\1\2\36\uffff\1\1";
    static final String DFA12_specialS =
        "\10\uffff\1\0\30\uffff}>";
    static final String[] DFA12_transitionS = {
            "\1\1\1\10\35\1",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\uffff",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA12_eot = DFA.unpackEncodedString(DFA12_eotS);
    static final short[] DFA12_eof = DFA.unpackEncodedString(DFA12_eofS);
    static final char[] DFA12_min = DFA.unpackEncodedStringToUnsignedChars(DFA12_minS);
    static final char[] DFA12_max = DFA.unpackEncodedStringToUnsignedChars(DFA12_maxS);
    static final short[] DFA12_accept = DFA.unpackEncodedString(DFA12_acceptS);
    static final short[] DFA12_special = DFA.unpackEncodedString(DFA12_specialS);
    static final short[][] DFA12_transition;

    static {
        int numStates = DFA12_transitionS.length;
        DFA12_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA12_transition[i] = DFA.unpackEncodedString(DFA12_transitionS[i]);
        }
    }

    class DFA12 extends DFA {

        public DFA12(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 12;
            this.eot = DFA12_eot;
            this.eof = DFA12_eof;
            this.min = DFA12_min;
            this.max = DFA12_max;
            this.accept = DFA12_accept;
            this.special = DFA12_special;
            this.transition = DFA12_transition;
        }
        public String getDescription() {
            return "()* loopback of 79:24: ( BLANK )*";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA12_8 = input.LA(1);

                         
                        int index12_8 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred16_WSL()) ) {s = 32;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index12_8);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 12, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA16_eotS =
        "\4\uffff";
    static final String DFA16_eofS =
        "\4\uffff";
    static final String DFA16_minS =
        "\2\7\2\uffff";
    static final String DFA16_maxS =
        "\2\33\2\uffff";
    static final String DFA16_acceptS =
        "\2\uffff\1\2\1\1";
    static final String DFA16_specialS =
        "\4\uffff}>";
    static final String[] DFA16_transitionS = {
            "\1\1\1\2\5\uffff\1\3\14\uffff\1\2",
            "\1\1\1\2\5\uffff\1\3\14\uffff\1\2",
            "",
            ""
    };

    static final short[] DFA16_eot = DFA.unpackEncodedString(DFA16_eotS);
    static final short[] DFA16_eof = DFA.unpackEncodedString(DFA16_eofS);
    static final char[] DFA16_min = DFA.unpackEncodedStringToUnsignedChars(DFA16_minS);
    static final char[] DFA16_max = DFA.unpackEncodedStringToUnsignedChars(DFA16_maxS);
    static final short[] DFA16_accept = DFA.unpackEncodedString(DFA16_acceptS);
    static final short[] DFA16_special = DFA.unpackEncodedString(DFA16_specialS);
    static final short[][] DFA16_transition;

    static {
        int numStates = DFA16_transitionS.length;
        DFA16_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA16_transition[i] = DFA.unpackEncodedString(DFA16_transitionS[i]);
        }
    }

    class DFA16 extends DFA {

        public DFA16(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 16;
            this.eot = DFA16_eot;
            this.eof = DFA16_eof;
            this.min = DFA16_min;
            this.max = DFA16_max;
            this.accept = DFA16_accept;
            this.special = DFA16_special;
            this.transition = DFA16_transition;
        }
        public String getDescription() {
            return "()* loopback of 92:3: ( ( BLANK )* OR ( BLANK )* argNext= conditionalAndExpression )*";
        }
    }
    static final String DFA19_eotS =
        "\4\uffff";
    static final String DFA19_eofS =
        "\1\2\3\uffff";
    static final String DFA19_minS =
        "\2\7\2\uffff";
    static final String DFA19_maxS =
        "\2\33\2\uffff";
    static final String DFA19_acceptS =
        "\2\uffff\1\2\1\1";
    static final String DFA19_specialS =
        "\4\uffff}>";
    static final String[] DFA19_transitionS = {
            "\1\1\1\2\5\uffff\1\2\1\3\13\uffff\1\2",
            "\1\1\1\2\5\uffff\1\2\1\3\13\uffff\1\2",
            "",
            ""
    };

    static final short[] DFA19_eot = DFA.unpackEncodedString(DFA19_eotS);
    static final short[] DFA19_eof = DFA.unpackEncodedString(DFA19_eofS);
    static final char[] DFA19_min = DFA.unpackEncodedStringToUnsignedChars(DFA19_minS);
    static final char[] DFA19_max = DFA.unpackEncodedStringToUnsignedChars(DFA19_maxS);
    static final short[] DFA19_accept = DFA.unpackEncodedString(DFA19_acceptS);
    static final short[] DFA19_special = DFA.unpackEncodedString(DFA19_specialS);
    static final short[][] DFA19_transition;

    static {
        int numStates = DFA19_transitionS.length;
        DFA19_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA19_transition[i] = DFA.unpackEncodedString(DFA19_transitionS[i]);
        }
    }

    class DFA19 extends DFA {

        public DFA19(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 19;
            this.eot = DFA19_eot;
            this.eof = DFA19_eof;
            this.min = DFA19_min;
            this.max = DFA19_max;
            this.accept = DFA19_accept;
            this.special = DFA19_special;
            this.transition = DFA19_transition;
        }
        public String getDescription() {
            return "()* loopback of 111:3: ( ( BLANK )* AND ( BLANK )* argNext= equalityExpression )*";
        }
    }
    static final String DFA22_eotS =
        "\4\uffff";
    static final String DFA22_eofS =
        "\1\2\3\uffff";
    static final String DFA22_minS =
        "\2\7\2\uffff";
    static final String DFA22_maxS =
        "\2\33\2\uffff";
    static final String DFA22_acceptS =
        "\2\uffff\1\2\1\1";
    static final String DFA22_specialS =
        "\4\uffff}>";
    static final String[] DFA22_transitionS = {
            "\1\1\1\2\5\uffff\2\2\2\3\11\uffff\1\2",
            "\1\1\1\2\5\uffff\2\2\2\3\11\uffff\1\2",
            "",
            ""
    };

    static final short[] DFA22_eot = DFA.unpackEncodedString(DFA22_eotS);
    static final short[] DFA22_eof = DFA.unpackEncodedString(DFA22_eofS);
    static final char[] DFA22_min = DFA.unpackEncodedStringToUnsignedChars(DFA22_minS);
    static final char[] DFA22_max = DFA.unpackEncodedStringToUnsignedChars(DFA22_maxS);
    static final short[] DFA22_accept = DFA.unpackEncodedString(DFA22_acceptS);
    static final short[] DFA22_special = DFA.unpackEncodedString(DFA22_specialS);
    static final short[][] DFA22_transition;

    static {
        int numStates = DFA22_transitionS.length;
        DFA22_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA22_transition[i] = DFA.unpackEncodedString(DFA22_transitionS[i]);
        }
    }

    class DFA22 extends DFA {

        public DFA22(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 22;
            this.eot = DFA22_eot;
            this.eof = DFA22_eof;
            this.min = DFA22_min;
            this.max = DFA22_max;
            this.accept = DFA22_accept;
            this.special = DFA22_special;
            this.transition = DFA22_transition;
        }
        public String getDescription() {
            return "()* loopback of 134:3: ( ( BLANK )* op= equalityOp ( BLANK )* argNext= relationalExpression )*";
        }
    }
    static final String DFA26_eotS =
        "\4\uffff";
    static final String DFA26_eofS =
        "\1\2\3\uffff";
    static final String DFA26_minS =
        "\2\7\2\uffff";
    static final String DFA26_maxS =
        "\2\33\2\uffff";
    static final String DFA26_acceptS =
        "\2\uffff\1\2\1\1";
    static final String DFA26_specialS =
        "\4\uffff}>";
    static final String[] DFA26_transitionS = {
            "\1\1\1\2\5\uffff\4\2\6\3\3\uffff\1\2",
            "\1\1\1\2\5\uffff\4\2\6\3\3\uffff\1\2",
            "",
            ""
    };

    static final short[] DFA26_eot = DFA.unpackEncodedString(DFA26_eotS);
    static final short[] DFA26_eof = DFA.unpackEncodedString(DFA26_eofS);
    static final char[] DFA26_min = DFA.unpackEncodedStringToUnsignedChars(DFA26_minS);
    static final char[] DFA26_max = DFA.unpackEncodedStringToUnsignedChars(DFA26_maxS);
    static final short[] DFA26_accept = DFA.unpackEncodedString(DFA26_acceptS);
    static final short[] DFA26_special = DFA.unpackEncodedString(DFA26_specialS);
    static final short[][] DFA26_transition;

    static {
        int numStates = DFA26_transitionS.length;
        DFA26_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA26_transition[i] = DFA.unpackEncodedString(DFA26_transitionS[i]);
        }
    }

    class DFA26 extends DFA {

        public DFA26(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 26;
            this.eot = DFA26_eot;
            this.eof = DFA26_eof;
            this.min = DFA26_min;
            this.max = DFA26_max;
            this.accept = DFA26_accept;
            this.special = DFA26_special;
            this.transition = DFA26_transition;
        }
        public String getDescription() {
            return "()* loopback of 166:3: ( ( BLANK )* op= relationalOp ( BLANK )* argNext= unaryExpression )*";
        }
    }
    static final String DFA39_eotS =
        "\43\uffff";
    static final String DFA39_eofS =
        "\1\3\1\uffff\1\1\40\uffff";
    static final String DFA39_minS =
        "\1\4\1\uffff\1\4\1\uffff\37\0";
    static final String DFA39_maxS =
        "\1\44\1\uffff\1\44\1\uffff\37\0";
    static final String DFA39_acceptS =
        "\1\uffff\1\1\1\uffff\1\2\37\uffff";
    static final String DFA39_specialS =
        "\4\uffff\1\33\1\17\1\4\1\12\1\20\1\32\1\1\1\3\1\11\1\16\1\22\1\25"+
        "\1\27\1\34\1\2\1\7\1\13\1\21\1\24\1\26\1\31\1\36\1\30\1\35\1\23"+
        "\1\15\1\10\1\14\1\0\1\6\1\5}>";
    static final String[] DFA39_transitionS = {
            "\1\3\1\uffff\6\1\1\2\30\1",
            "",
            "\1\1\1\uffff\1\12\1\4\1\13\1\32\1\36\1\37\1\35\1\40\1\14\1"+
            "\15\1\20\1\16\1\23\1\24\1\21\1\22\1\30\1\31\1\17\1\27\1\10\1"+
            "\7\1\5\1\6\1\33\1\34\1\11\1\42\1\41\1\25\1\26",
            "",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff"
    };

    static final short[] DFA39_eot = DFA.unpackEncodedString(DFA39_eotS);
    static final short[] DFA39_eof = DFA.unpackEncodedString(DFA39_eofS);
    static final char[] DFA39_min = DFA.unpackEncodedStringToUnsignedChars(DFA39_minS);
    static final char[] DFA39_max = DFA.unpackEncodedStringToUnsignedChars(DFA39_maxS);
    static final short[] DFA39_accept = DFA.unpackEncodedString(DFA39_acceptS);
    static final short[] DFA39_special = DFA.unpackEncodedString(DFA39_specialS);
    static final short[][] DFA39_transition;

    static {
        int numStates = DFA39_transitionS.length;
        DFA39_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA39_transition[i] = DFA.unpackEncodedString(DFA39_transitionS[i]);
        }
    }

    class DFA39 extends DFA {

        public DFA39(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 39;
            this.eot = DFA39_eot;
            this.eof = DFA39_eof;
            this.min = DFA39_min;
            this.max = DFA39_max;
            this.accept = DFA39_accept;
            this.special = DFA39_special;
            this.transition = DFA39_transition;
        }
        public String getDescription() {
            return "239:22: (l= string_list_helper )?";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA39_32 = input.LA(1);

                         
                        int index39_32 = input.index();
                        input.rewind();
                        s = -1;
                        if ( ((synpred51_WSL()&&( actionDepth == 0 ))) ) {s = 1;}

                        else if ( (true) ) {s = 3;}

                         
                        input.seek(index39_32);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA39_10 = input.LA(1);

                         
                        int index39_10 = input.index();
                        input.rewind();
                        s = -1;
                        if ( ((synpred51_WSL()&&( actionDepth == 0 ))) ) {s = 1;}

                        else if ( (true) ) {s = 3;}

                         
                        input.seek(index39_10);
                        if ( s>=0 ) return s;
                        break;
                    case 2 : 
                        int LA39_18 = input.LA(1);

                         
                        int index39_18 = input.index();
                        input.rewind();
                        s = -1;
                        if ( ((synpred51_WSL()&&( actionDepth == 0 ))) ) {s = 1;}

                        else if ( (true) ) {s = 3;}

                         
                        input.seek(index39_18);
                        if ( s>=0 ) return s;
                        break;
                    case 3 : 
                        int LA39_11 = input.LA(1);

                         
                        int index39_11 = input.index();
                        input.rewind();
                        s = -1;
                        if ( ((synpred51_WSL()&&( actionDepth == 0 ))) ) {s = 1;}

                        else if ( (true) ) {s = 3;}

                         
                        input.seek(index39_11);
                        if ( s>=0 ) return s;
                        break;
                    case 4 : 
                        int LA39_6 = input.LA(1);

                         
                        int index39_6 = input.index();
                        input.rewind();
                        s = -1;
                        if ( ((synpred51_WSL()&&( actionDepth == 0 ))) ) {s = 1;}

                        else if ( (true) ) {s = 3;}

                         
                        input.seek(index39_6);
                        if ( s>=0 ) return s;
                        break;
                    case 5 : 
                        int LA39_34 = input.LA(1);

                         
                        int index39_34 = input.index();
                        input.rewind();
                        s = -1;
                        if ( ((synpred51_WSL()&&( actionDepth == 0 ))) ) {s = 1;}

                        else if ( (true) ) {s = 3;}

                         
                        input.seek(index39_34);
                        if ( s>=0 ) return s;
                        break;
                    case 6 : 
                        int LA39_33 = input.LA(1);

                         
                        int index39_33 = input.index();
                        input.rewind();
                        s = -1;
                        if ( ((synpred51_WSL()&&( actionDepth == 0 ))) ) {s = 1;}

                        else if ( (true) ) {s = 3;}

                         
                        input.seek(index39_33);
                        if ( s>=0 ) return s;
                        break;
                    case 7 : 
                        int LA39_19 = input.LA(1);

                         
                        int index39_19 = input.index();
                        input.rewind();
                        s = -1;
                        if ( ((synpred51_WSL()&&( actionDepth == 0 ))) ) {s = 1;}

                        else if ( (true) ) {s = 3;}

                         
                        input.seek(index39_19);
                        if ( s>=0 ) return s;
                        break;
                    case 8 : 
                        int LA39_30 = input.LA(1);

                         
                        int index39_30 = input.index();
                        input.rewind();
                        s = -1;
                        if ( ((synpred51_WSL()&&( actionDepth == 0 ))) ) {s = 1;}

                        else if ( (true) ) {s = 3;}

                         
                        input.seek(index39_30);
                        if ( s>=0 ) return s;
                        break;
                    case 9 : 
                        int LA39_12 = input.LA(1);

                         
                        int index39_12 = input.index();
                        input.rewind();
                        s = -1;
                        if ( ((synpred51_WSL()&&( actionDepth == 0 ))) ) {s = 1;}

                        else if ( (true) ) {s = 3;}

                         
                        input.seek(index39_12);
                        if ( s>=0 ) return s;
                        break;
                    case 10 : 
                        int LA39_7 = input.LA(1);

                         
                        int index39_7 = input.index();
                        input.rewind();
                        s = -1;
                        if ( ((synpred51_WSL()&&( actionDepth == 0 ))) ) {s = 1;}

                        else if ( (true) ) {s = 3;}

                         
                        input.seek(index39_7);
                        if ( s>=0 ) return s;
                        break;
                    case 11 : 
                        int LA39_20 = input.LA(1);

                         
                        int index39_20 = input.index();
                        input.rewind();
                        s = -1;
                        if ( ((synpred51_WSL()&&( actionDepth == 0 ))) ) {s = 1;}

                        else if ( (true) ) {s = 3;}

                         
                        input.seek(index39_20);
                        if ( s>=0 ) return s;
                        break;
                    case 12 : 
                        int LA39_31 = input.LA(1);

                         
                        int index39_31 = input.index();
                        input.rewind();
                        s = -1;
                        if ( ((synpred51_WSL()&&( actionDepth == 0 ))) ) {s = 1;}

                        else if ( (true) ) {s = 3;}

                         
                        input.seek(index39_31);
                        if ( s>=0 ) return s;
                        break;
                    case 13 : 
                        int LA39_29 = input.LA(1);

                         
                        int index39_29 = input.index();
                        input.rewind();
                        s = -1;
                        if ( ((synpred51_WSL()&&( actionDepth == 0 ))) ) {s = 1;}

                        else if ( (true) ) {s = 3;}

                         
                        input.seek(index39_29);
                        if ( s>=0 ) return s;
                        break;
                    case 14 : 
                        int LA39_13 = input.LA(1);

                         
                        int index39_13 = input.index();
                        input.rewind();
                        s = -1;
                        if ( ((synpred51_WSL()&&( actionDepth == 0 ))) ) {s = 1;}

                        else if ( (true) ) {s = 3;}

                         
                        input.seek(index39_13);
                        if ( s>=0 ) return s;
                        break;
                    case 15 : 
                        int LA39_5 = input.LA(1);

                         
                        int index39_5 = input.index();
                        input.rewind();
                        s = -1;
                        if ( ((synpred51_WSL()&&( actionDepth == 0 ))) ) {s = 1;}

                        else if ( (true) ) {s = 3;}

                         
                        input.seek(index39_5);
                        if ( s>=0 ) return s;
                        break;
                    case 16 : 
                        int LA39_8 = input.LA(1);

                         
                        int index39_8 = input.index();
                        input.rewind();
                        s = -1;
                        if ( ((synpred51_WSL()&&( actionDepth == 0 ))) ) {s = 1;}

                        else if ( (true) ) {s = 3;}

                         
                        input.seek(index39_8);
                        if ( s>=0 ) return s;
                        break;
                    case 17 : 
                        int LA39_21 = input.LA(1);

                         
                        int index39_21 = input.index();
                        input.rewind();
                        s = -1;
                        if ( ((synpred51_WSL()&&( actionDepth == 0 ))) ) {s = 1;}

                        else if ( (true) ) {s = 3;}

                         
                        input.seek(index39_21);
                        if ( s>=0 ) return s;
                        break;
                    case 18 : 
                        int LA39_14 = input.LA(1);

                         
                        int index39_14 = input.index();
                        input.rewind();
                        s = -1;
                        if ( ((synpred51_WSL()&&( actionDepth == 0 ))) ) {s = 1;}

                        else if ( (true) ) {s = 3;}

                         
                        input.seek(index39_14);
                        if ( s>=0 ) return s;
                        break;
                    case 19 : 
                        int LA39_28 = input.LA(1);

                         
                        int index39_28 = input.index();
                        input.rewind();
                        s = -1;
                        if ( ((synpred51_WSL()&&( actionDepth == 0 ))) ) {s = 1;}

                        else if ( (true) ) {s = 3;}

                         
                        input.seek(index39_28);
                        if ( s>=0 ) return s;
                        break;
                    case 20 : 
                        int LA39_22 = input.LA(1);

                         
                        int index39_22 = input.index();
                        input.rewind();
                        s = -1;
                        if ( ((synpred51_WSL()&&( actionDepth == 0 ))) ) {s = 1;}

                        else if ( (true) ) {s = 3;}

                         
                        input.seek(index39_22);
                        if ( s>=0 ) return s;
                        break;
                    case 21 : 
                        int LA39_15 = input.LA(1);

                         
                        int index39_15 = input.index();
                        input.rewind();
                        s = -1;
                        if ( ((synpred51_WSL()&&( actionDepth == 0 ))) ) {s = 1;}

                        else if ( (true) ) {s = 3;}

                         
                        input.seek(index39_15);
                        if ( s>=0 ) return s;
                        break;
                    case 22 : 
                        int LA39_23 = input.LA(1);

                         
                        int index39_23 = input.index();
                        input.rewind();
                        s = -1;
                        if ( ((synpred51_WSL()&&( actionDepth == 0 ))) ) {s = 1;}

                        else if ( (true) ) {s = 3;}

                         
                        input.seek(index39_23);
                        if ( s>=0 ) return s;
                        break;
                    case 23 : 
                        int LA39_16 = input.LA(1);

                         
                        int index39_16 = input.index();
                        input.rewind();
                        s = -1;
                        if ( ((synpred51_WSL()&&( actionDepth == 0 ))) ) {s = 1;}

                        else if ( (true) ) {s = 3;}

                         
                        input.seek(index39_16);
                        if ( s>=0 ) return s;
                        break;
                    case 24 : 
                        int LA39_26 = input.LA(1);

                         
                        int index39_26 = input.index();
                        input.rewind();
                        s = -1;
                        if ( ((synpred51_WSL()&&( actionDepth == 0 ))) ) {s = 1;}

                        else if ( (true) ) {s = 3;}

                         
                        input.seek(index39_26);
                        if ( s>=0 ) return s;
                        break;
                    case 25 : 
                        int LA39_24 = input.LA(1);

                         
                        int index39_24 = input.index();
                        input.rewind();
                        s = -1;
                        if ( ((synpred51_WSL()&&( actionDepth == 0 ))) ) {s = 1;}

                        else if ( (true) ) {s = 3;}

                         
                        input.seek(index39_24);
                        if ( s>=0 ) return s;
                        break;
                    case 26 : 
                        int LA39_9 = input.LA(1);

                         
                        int index39_9 = input.index();
                        input.rewind();
                        s = -1;
                        if ( ((synpred51_WSL()&&( actionDepth == 0 ))) ) {s = 1;}

                        else if ( (true) ) {s = 3;}

                         
                        input.seek(index39_9);
                        if ( s>=0 ) return s;
                        break;
                    case 27 : 
                        int LA39_4 = input.LA(1);

                         
                        int index39_4 = input.index();
                        input.rewind();
                        s = -1;
                        if ( ((synpred51_WSL()&&( actionDepth == 0 ))) ) {s = 1;}

                        else if ( (true) ) {s = 3;}

                         
                        input.seek(index39_4);
                        if ( s>=0 ) return s;
                        break;
                    case 28 : 
                        int LA39_17 = input.LA(1);

                         
                        int index39_17 = input.index();
                        input.rewind();
                        s = -1;
                        if ( ((synpred51_WSL()&&( actionDepth == 0 ))) ) {s = 1;}

                        else if ( (true) ) {s = 3;}

                         
                        input.seek(index39_17);
                        if ( s>=0 ) return s;
                        break;
                    case 29 : 
                        int LA39_27 = input.LA(1);

                         
                        int index39_27 = input.index();
                        input.rewind();
                        s = -1;
                        if ( ((synpred51_WSL()&&( actionDepth == 0 ))) ) {s = 1;}

                        else if ( (true) ) {s = 3;}

                         
                        input.seek(index39_27);
                        if ( s>=0 ) return s;
                        break;
                    case 30 : 
                        int LA39_25 = input.LA(1);

                         
                        int index39_25 = input.index();
                        input.rewind();
                        s = -1;
                        if ( ((synpred51_WSL()&&( actionDepth == 0 ))) ) {s = 1;}

                        else if ( (true) ) {s = 3;}

                         
                        input.seek(index39_25);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 39, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA40_eotS =
        "\41\uffff";
    static final String DFA40_eofS =
        "\41\uffff";
    static final String DFA40_minS =
        "\1\6\2\0\36\uffff";
    static final String DFA40_maxS =
        "\1\44\2\0\36\uffff";
    static final String DFA40_acceptS =
        "\3\uffff\1\2\33\uffff\1\3\1\1";
    static final String DFA40_specialS =
        "\1\uffff\1\0\1\1\36\uffff}>";
    static final String[] DFA40_transitionS = {
            "\26\3\1\1\1\2\3\3\1\37\3\3",
            "\1\uffff",
            "\1\uffff",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA40_eot = DFA.unpackEncodedString(DFA40_eotS);
    static final short[] DFA40_eof = DFA.unpackEncodedString(DFA40_eofS);
    static final char[] DFA40_min = DFA.unpackEncodedStringToUnsignedChars(DFA40_minS);
    static final char[] DFA40_max = DFA.unpackEncodedStringToUnsignedChars(DFA40_maxS);
    static final short[] DFA40_accept = DFA.unpackEncodedString(DFA40_acceptS);
    static final short[] DFA40_special = DFA.unpackEncodedString(DFA40_specialS);
    static final short[][] DFA40_transition;

    static {
        int numStates = DFA40_transitionS.length;
        DFA40_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA40_transition[i] = DFA.unpackEncodedString(DFA40_transitionS[i]);
        }
    }

    class DFA40 extends DFA {

        public DFA40(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 40;
            this.eot = DFA40_eot;
            this.eof = DFA40_eof;
            this.min = DFA40_min;
            this.max = DFA40_max;
            this.accept = DFA40_accept;
            this.special = DFA40_special;
            this.transition = DFA40_transition;
        }
        public String getDescription() {
            return "251:1: string_value returns [IWSLValue value] : ( ( ( PERCENT | DOLLAR ) ~ ( EOL | PERCENT | DOLLAR | BLANK | EOF ) )=>v= variable | str= text_string | t= QUOTE );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA40_1 = input.LA(1);

                         
                        int index40_1 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred57_WSL()) ) {s = 32;}

                        else if ( (synpred58_WSL()) ) {s = 3;}

                         
                        input.seek(index40_1);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA40_2 = input.LA(1);

                         
                        int index40_2 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred57_WSL()) ) {s = 32;}

                        else if ( (synpred58_WSL()) ) {s = 3;}

                         
                        input.seek(index40_2);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 40, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA42_eotS =
        "\41\uffff";
    static final String DFA42_eofS =
        "\41\uffff";
    static final String DFA42_minS =
        "\1\6\3\0\35\uffff";
    static final String DFA42_maxS =
        "\1\44\3\0\35\uffff";
    static final String DFA42_acceptS =
        "\4\uffff\1\3\32\uffff\1\1\1\2";
    static final String DFA42_specialS =
        "\1\uffff\1\0\1\1\1\2\35\uffff}>";
    static final String[] DFA42_transitionS = {
            "\26\4\1\1\1\2\3\4\1\uffff\1\3\2\4",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA42_eot = DFA.unpackEncodedString(DFA42_eotS);
    static final short[] DFA42_eof = DFA.unpackEncodedString(DFA42_eofS);
    static final char[] DFA42_min = DFA.unpackEncodedStringToUnsignedChars(DFA42_minS);
    static final char[] DFA42_max = DFA.unpackEncodedStringToUnsignedChars(DFA42_maxS);
    static final short[] DFA42_accept = DFA.unpackEncodedString(DFA42_acceptS);
    static final short[] DFA42_special = DFA.unpackEncodedString(DFA42_specialS);
    static final short[][] DFA42_transition;

    static {
        int numStates = DFA42_transitionS.length;
        DFA42_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA42_transition[i] = DFA.unpackEncodedString(DFA42_transitionS[i]);
        }
    }

    class DFA42 extends DFA {

        public DFA42(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 42;
            this.eot = DFA42_eot;
            this.eof = DFA42_eof;
            this.min = DFA42_min;
            this.max = DFA42_max;
            this.accept = DFA42_accept;
            this.special = DFA42_special;
            this.transition = DFA42_transition;
        }
        public String getDescription() {
            return "280:1: quoted_string_value returns [IWSLValue value] : ( ( ( PERCENT | DOLLAR ) ~ ( EOL | PERCENT | DOLLAR | BLANK | EOF ) )=>v= variable | ( BACKSLASH QUOTE )=> BACKSLASH t= QUOTE | str= text_string );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA42_1 = input.LA(1);

                         
                        int index42_1 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred65_WSL()) ) {s = 31;}

                        else if ( (true) ) {s = 4;}

                         
                        input.seek(index42_1);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA42_2 = input.LA(1);

                         
                        int index42_2 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred65_WSL()) ) {s = 31;}

                        else if ( (true) ) {s = 4;}

                         
                        input.seek(index42_2);
                        if ( s>=0 ) return s;
                        break;
                    case 2 : 
                        int LA42_3 = input.LA(1);

                         
                        int index42_3 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred66_WSL()) ) {s = 32;}

                        else if ( (true) ) {s = 4;}

                         
                        input.seek(index42_3);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 42, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA43_eotS =
        "\32\uffff";
    static final String DFA43_eofS =
        "\32\uffff";
    static final String DFA43_minS =
        "\1\6\31\uffff";
    static final String DFA43_maxS =
        "\1\44\31\uffff";
    static final String DFA43_acceptS =
        "\1\uffff\1\1\1\2\1\3\1\4\1\5\1\6\1\7\1\10\1\11\1\12\1\13\1\14\1"+
        "\15\1\16\1\17\1\20\1\21\1\22\1\23\1\24\1\25\1\26\1\27\1\30\1\31";
    static final String DFA43_specialS =
        "\32\uffff}>";
    static final String[] DFA43_transitionS = {
            "\1\2\1\uffff\1\3\1\22\1\26\1\27\1\25\1\30\1\4\1\5\1\10\1\6\1"+
            "\13\1\14\1\11\1\12\1\20\1\21\1\7\1\17\4\uffff\1\23\1\24\1\1"+
            "\1\uffff\1\31\1\15\1\16",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA43_eot = DFA.unpackEncodedString(DFA43_eotS);
    static final short[] DFA43_eof = DFA.unpackEncodedString(DFA43_eofS);
    static final char[] DFA43_min = DFA.unpackEncodedStringToUnsignedChars(DFA43_minS);
    static final char[] DFA43_max = DFA.unpackEncodedStringToUnsignedChars(DFA43_maxS);
    static final short[] DFA43_accept = DFA.unpackEncodedString(DFA43_acceptS);
    static final short[] DFA43_special = DFA.unpackEncodedString(DFA43_specialS);
    static final short[][] DFA43_transition;

    static {
        int numStates = DFA43_transitionS.length;
        DFA43_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA43_transition[i] = DFA.unpackEncodedString(DFA43_transitionS[i]);
        }
    }

    class DFA43 extends DFA {

        public DFA43(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 43;
            this.eot = DFA43_eot;
            this.eof = DFA43_eof;
            this.min = DFA43_min;
            this.max = DFA43_max;
            this.accept = DFA43_accept;
            this.special = DFA43_special;
            this.transition = DFA43_transition;
        }
        public String getDescription() {
            return "287:4: (str= STRING | str= IF | str= THEN | str= OR | str= AND | str= NOTEQUAL | str= NOT | str= EQUAL | str= GTE | str= LTE | str= GT | str= LT | str= AMP | str= VERT | str= EXISTS | str= CONTAINS | str= CONTAINSRE | str= ACTION | str= TRUE | str= FALSE | {...}?str= WHEN | str= REMOVE | str= CLEAR | str= INSTANT | str= BACKSLASH )";
        }
    }
    static final String DFA44_eotS =
        "\12\uffff";
    static final String DFA44_eofS =
        "\12\uffff";
    static final String DFA44_minS =
        "\1\7\2\0\7\uffff";
    static final String DFA44_maxS =
        "\1\35\2\0\7\uffff";
    static final String DFA44_acceptS =
        "\3\uffff\1\5\1\6\1\7\1\1\1\3\1\2\1\4";
    static final String DFA44_specialS =
        "\1\uffff\1\0\1\1\7\uffff}>";
    static final String[] DFA44_transitionS = {
            "\1\5\22\uffff\1\4\1\3\1\1\1\2",
            "\1\uffff",
            "\1\uffff",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA44_eot = DFA.unpackEncodedString(DFA44_eotS);
    static final short[] DFA44_eof = DFA.unpackEncodedString(DFA44_eofS);
    static final char[] DFA44_min = DFA.unpackEncodedStringToUnsignedChars(DFA44_minS);
    static final char[] DFA44_max = DFA.unpackEncodedStringToUnsignedChars(DFA44_maxS);
    static final short[] DFA44_accept = DFA.unpackEncodedString(DFA44_acceptS);
    static final short[] DFA44_special = DFA.unpackEncodedString(DFA44_specialS);
    static final short[][] DFA44_transition;

    static {
        int numStates = DFA44_transitionS.length;
        DFA44_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA44_transition[i] = DFA.unpackEncodedString(DFA44_transitionS[i]);
        }
    }

    class DFA44 extends DFA {

        public DFA44(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 44;
            this.eot = DFA44_eot;
            this.eof = DFA44_eof;
            this.min = DFA44_min;
            this.max = DFA44_max;
            this.accept = DFA44_accept;
            this.special = DFA44_special;
            this.transition = DFA44_transition;
        }
        public String getDescription() {
            return "296:4: ( ( PERCENT PERCENT )=> PERCENT t= PERCENT | ( DOLLAR DOLLAR )=> DOLLAR t= DOLLAR | t= PERCENT | t= DOLLAR | t= RPAREN | t= LPAREN | t= BLANK )";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA44_1 = input.LA(1);

                         
                        int index44_1 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred91_WSL()) ) {s = 6;}

                        else if ( (synpred93_WSL()) ) {s = 7;}

                         
                        input.seek(index44_1);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA44_2 = input.LA(1);

                         
                        int index44_2 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred92_WSL()) ) {s = 8;}

                        else if ( (synpred94_WSL()) ) {s = 9;}

                         
                        input.seek(index44_2);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 44, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA51_eotS =
        "\43\uffff";
    static final String DFA51_eofS =
        "\1\32\42\uffff";
    static final String DFA51_minS =
        "\1\4\31\0\11\uffff";
    static final String DFA51_maxS =
        "\1\44\31\0\11\uffff";
    static final String DFA51_acceptS =
        "\32\uffff\1\2\7\uffff\1\1";
    static final String DFA51_specialS =
        "\1\uffff\1\0\1\1\1\2\1\3\1\4\1\5\1\6\1\7\1\10\1\11\1\12\1\13\1\14"+
        "\1\15\1\16\1\17\1\20\1\21\1\22\1\23\1\24\1\25\1\26\1\27\1\30\11"+
        "\uffff}>";
    static final String[] DFA51_transitionS = {
            "\1\32\1\uffff\1\2\1\32\1\3\1\22\1\26\1\27\1\25\1\30\1\4\1\5"+
            "\1\10\1\6\1\13\1\14\1\11\1\12\1\20\1\21\1\7\1\17\4\32\1\23\1"+
            "\24\1\1\1\32\1\31\1\15\1\16",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA51_eot = DFA.unpackEncodedString(DFA51_eotS);
    static final short[] DFA51_eof = DFA.unpackEncodedString(DFA51_eofS);
    static final char[] DFA51_min = DFA.unpackEncodedStringToUnsignedChars(DFA51_minS);
    static final char[] DFA51_max = DFA.unpackEncodedStringToUnsignedChars(DFA51_maxS);
    static final short[] DFA51_accept = DFA.unpackEncodedString(DFA51_acceptS);
    static final short[] DFA51_special = DFA.unpackEncodedString(DFA51_specialS);
    static final short[][] DFA51_transition;

    static {
        int numStates = DFA51_transitionS.length;
        DFA51_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA51_transition[i] = DFA.unpackEncodedString(DFA51_transitionS[i]);
        }
    }

    class DFA51 extends DFA {

        public DFA51(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 51;
            this.eot = DFA51_eot;
            this.eof = DFA51_eof;
            this.min = DFA51_min;
            this.max = DFA51_max;
            this.accept = DFA51_accept;
            this.special = DFA51_special;
            this.transition = DFA51_transition;
        }
        public String getDescription() {
            return "318:26: (rest= variable_string_helper )?";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA51_1 = input.LA(1);

                         
                        int index51_1 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred103_WSL()) ) {s = 34;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index51_1);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA51_2 = input.LA(1);

                         
                        int index51_2 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred103_WSL()) ) {s = 34;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index51_2);
                        if ( s>=0 ) return s;
                        break;
                    case 2 : 
                        int LA51_3 = input.LA(1);

                         
                        int index51_3 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred103_WSL()) ) {s = 34;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index51_3);
                        if ( s>=0 ) return s;
                        break;
                    case 3 : 
                        int LA51_4 = input.LA(1);

                         
                        int index51_4 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred103_WSL()) ) {s = 34;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index51_4);
                        if ( s>=0 ) return s;
                        break;
                    case 4 : 
                        int LA51_5 = input.LA(1);

                         
                        int index51_5 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred103_WSL()) ) {s = 34;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index51_5);
                        if ( s>=0 ) return s;
                        break;
                    case 5 : 
                        int LA51_6 = input.LA(1);

                         
                        int index51_6 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred103_WSL()) ) {s = 34;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index51_6);
                        if ( s>=0 ) return s;
                        break;
                    case 6 : 
                        int LA51_7 = input.LA(1);

                         
                        int index51_7 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred103_WSL()) ) {s = 34;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index51_7);
                        if ( s>=0 ) return s;
                        break;
                    case 7 : 
                        int LA51_8 = input.LA(1);

                         
                        int index51_8 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred103_WSL()) ) {s = 34;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index51_8);
                        if ( s>=0 ) return s;
                        break;
                    case 8 : 
                        int LA51_9 = input.LA(1);

                         
                        int index51_9 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred103_WSL()) ) {s = 34;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index51_9);
                        if ( s>=0 ) return s;
                        break;
                    case 9 : 
                        int LA51_10 = input.LA(1);

                         
                        int index51_10 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred103_WSL()) ) {s = 34;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index51_10);
                        if ( s>=0 ) return s;
                        break;
                    case 10 : 
                        int LA51_11 = input.LA(1);

                         
                        int index51_11 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred103_WSL()) ) {s = 34;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index51_11);
                        if ( s>=0 ) return s;
                        break;
                    case 11 : 
                        int LA51_12 = input.LA(1);

                         
                        int index51_12 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred103_WSL()) ) {s = 34;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index51_12);
                        if ( s>=0 ) return s;
                        break;
                    case 12 : 
                        int LA51_13 = input.LA(1);

                         
                        int index51_13 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred103_WSL()) ) {s = 34;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index51_13);
                        if ( s>=0 ) return s;
                        break;
                    case 13 : 
                        int LA51_14 = input.LA(1);

                         
                        int index51_14 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred103_WSL()) ) {s = 34;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index51_14);
                        if ( s>=0 ) return s;
                        break;
                    case 14 : 
                        int LA51_15 = input.LA(1);

                         
                        int index51_15 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred103_WSL()) ) {s = 34;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index51_15);
                        if ( s>=0 ) return s;
                        break;
                    case 15 : 
                        int LA51_16 = input.LA(1);

                         
                        int index51_16 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred103_WSL()) ) {s = 34;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index51_16);
                        if ( s>=0 ) return s;
                        break;
                    case 16 : 
                        int LA51_17 = input.LA(1);

                         
                        int index51_17 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred103_WSL()) ) {s = 34;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index51_17);
                        if ( s>=0 ) return s;
                        break;
                    case 17 : 
                        int LA51_18 = input.LA(1);

                         
                        int index51_18 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred103_WSL()) ) {s = 34;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index51_18);
                        if ( s>=0 ) return s;
                        break;
                    case 18 : 
                        int LA51_19 = input.LA(1);

                         
                        int index51_19 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred103_WSL()) ) {s = 34;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index51_19);
                        if ( s>=0 ) return s;
                        break;
                    case 19 : 
                        int LA51_20 = input.LA(1);

                         
                        int index51_20 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred103_WSL()) ) {s = 34;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index51_20);
                        if ( s>=0 ) return s;
                        break;
                    case 20 : 
                        int LA51_21 = input.LA(1);

                         
                        int index51_21 = input.index();
                        input.rewind();
                        s = -1;
                        if ( ((synpred103_WSL()&&( actionDepth == 0 ))) ) {s = 34;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index51_21);
                        if ( s>=0 ) return s;
                        break;
                    case 21 : 
                        int LA51_22 = input.LA(1);

                         
                        int index51_22 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred103_WSL()) ) {s = 34;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index51_22);
                        if ( s>=0 ) return s;
                        break;
                    case 22 : 
                        int LA51_23 = input.LA(1);

                         
                        int index51_23 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred103_WSL()) ) {s = 34;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index51_23);
                        if ( s>=0 ) return s;
                        break;
                    case 23 : 
                        int LA51_24 = input.LA(1);

                         
                        int index51_24 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred103_WSL()) ) {s = 34;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index51_24);
                        if ( s>=0 ) return s;
                        break;
                    case 24 : 
                        int LA51_25 = input.LA(1);

                         
                        int index51_25 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred103_WSL()) ) {s = 34;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index51_25);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 51, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA54_eotS =
        "\73\uffff";
    static final String DFA54_eofS =
        "\4\uffff\1\5\66\uffff";
    static final String DFA54_minS =
        "\3\6\1\uffff\1\6\65\uffff\1\0";
    static final String DFA54_maxS =
        "\3\44\1\uffff\1\44\65\uffff\1\0";
    static final String DFA54_acceptS =
        "\3\uffff\1\2\1\uffff\1\3\64\1\1\uffff";
    static final String DFA54_specialS =
        "\1\uffff\1\1\1\0\67\uffff\1\2}>";
    static final String[] DFA54_transitionS = {
            "\1\5\1\3\22\5\2\uffff\1\1\1\2\3\5\1\3\1\4\2\5",
            "\1\10\1\uffff\1\11\1\30\1\34\1\35\1\33\1\36\1\12\1\13\1\16"+
            "\1\14\1\21\1\22\1\17\1\20\1\26\1\27\1\15\1\25\1\6\1\uffff\1"+
            "\3\1\uffff\1\31\1\32\1\7\1\uffff\1\37\1\23\1\24",
            "\1\42\1\uffff\1\43\1\62\1\66\1\67\1\65\1\70\1\44\1\45\1\50"+
            "\1\46\1\53\1\54\1\51\1\52\1\60\1\61\1\47\1\57\1\40\2\uffff\1"+
            "\3\1\63\1\64\1\41\1\uffff\1\71\1\55\1\56",
            "",
            "\24\5\1\3\1\72\11\5",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\uffff"
    };

    static final short[] DFA54_eot = DFA.unpackEncodedString(DFA54_eotS);
    static final short[] DFA54_eof = DFA.unpackEncodedString(DFA54_eofS);
    static final char[] DFA54_min = DFA.unpackEncodedStringToUnsignedChars(DFA54_minS);
    static final char[] DFA54_max = DFA.unpackEncodedStringToUnsignedChars(DFA54_maxS);
    static final short[] DFA54_accept = DFA.unpackEncodedString(DFA54_acceptS);
    static final short[] DFA54_special = DFA.unpackEncodedString(DFA54_specialS);
    static final short[][] DFA54_transition;

    static {
        int numStates = DFA54_transitionS.length;
        DFA54_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA54_transition[i] = DFA.unpackEncodedString(DFA54_transitionS[i]);
        }
    }

    class DFA54 extends DFA {

        public DFA54(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 54;
            this.eot = DFA54_eot;
            this.eof = DFA54_eof;
            this.min = DFA54_min;
            this.max = DFA54_max;
            this.accept = DFA54_accept;
            this.special = DFA54_special;
            this.transition = DFA54_transition;
        }
        public String getDescription() {
            return "357:1: vstring_value returns [IWSLValue value] : ( ( ( PERCENT | DOLLAR ) ~ ( EOL | PERCENT | DOLLAR | BLANK | EOF ) )=>v= variable | ( ( PERCENT PERCENT )=> PERCENT t= PERCENT | ( DOLLAR DOLLAR )=> DOLLAR t= DOLLAR | t= QUOTE | ( BACKSLASH LPAREN )=> BACKSLASH t= LPAREN | ( BACKSLASH RPAREN )=> BACKSLASH t= RPAREN | t= BLANK ) | str= common_string );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA54_2 = input.LA(1);

                         
                        int index54_2 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (LA54_2==DOLLAR) ) {s = 3;}

                        else if ( (LA54_2==LPAREN) && (synpred110_WSL())) {s = 32;}

                        else if ( (LA54_2==STRING) && (synpred110_WSL())) {s = 33;}

                        else if ( (LA54_2==IF) && (synpred110_WSL())) {s = 34;}

                        else if ( (LA54_2==THEN) && (synpred110_WSL())) {s = 35;}

                        else if ( (LA54_2==OR) && (synpred110_WSL())) {s = 36;}

                        else if ( (LA54_2==AND) && (synpred110_WSL())) {s = 37;}

                        else if ( (LA54_2==NOTEQUAL) && (synpred110_WSL())) {s = 38;}

                        else if ( (LA54_2==NOT) && (synpred110_WSL())) {s = 39;}

                        else if ( (LA54_2==EQUAL) && (synpred110_WSL())) {s = 40;}

                        else if ( (LA54_2==GTE) && (synpred110_WSL())) {s = 41;}

                        else if ( (LA54_2==LTE) && (synpred110_WSL())) {s = 42;}

                        else if ( (LA54_2==GT) && (synpred110_WSL())) {s = 43;}

                        else if ( (LA54_2==LT) && (synpred110_WSL())) {s = 44;}

                        else if ( (LA54_2==AMP) && (synpred110_WSL())) {s = 45;}

                        else if ( (LA54_2==VERT) && (synpred110_WSL())) {s = 46;}

                        else if ( (LA54_2==EXISTS) && (synpred110_WSL())) {s = 47;}

                        else if ( (LA54_2==CONTAINS) && (synpred110_WSL())) {s = 48;}

                        else if ( (LA54_2==CONTAINSRE) && (synpred110_WSL())) {s = 49;}

                        else if ( (LA54_2==ACTION) && (synpred110_WSL())) {s = 50;}

                        else if ( (LA54_2==TRUE) && (synpred110_WSL())) {s = 51;}

                        else if ( (LA54_2==FALSE) && (synpred110_WSL())) {s = 52;}

                        else if ( (LA54_2==WHEN) && (synpred110_WSL())) {s = 53;}

                        else if ( (LA54_2==REMOVE) && (synpred110_WSL())) {s = 54;}

                        else if ( (LA54_2==CLEAR) && (synpred110_WSL())) {s = 55;}

                        else if ( (LA54_2==INSTANT) && (synpred110_WSL())) {s = 56;}

                        else if ( (LA54_2==BACKSLASH) && (synpred110_WSL())) {s = 57;}

                         
                        input.seek(index54_2);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA54_1 = input.LA(1);

                         
                        int index54_1 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (LA54_1==PERCENT) ) {s = 3;}

                        else if ( (LA54_1==LPAREN) && (synpred110_WSL())) {s = 6;}

                        else if ( (LA54_1==STRING) && (synpred110_WSL())) {s = 7;}

                        else if ( (LA54_1==IF) && (synpred110_WSL())) {s = 8;}

                        else if ( (LA54_1==THEN) && (synpred110_WSL())) {s = 9;}

                        else if ( (LA54_1==OR) && (synpred110_WSL())) {s = 10;}

                        else if ( (LA54_1==AND) && (synpred110_WSL())) {s = 11;}

                        else if ( (LA54_1==NOTEQUAL) && (synpred110_WSL())) {s = 12;}

                        else if ( (LA54_1==NOT) && (synpred110_WSL())) {s = 13;}

                        else if ( (LA54_1==EQUAL) && (synpred110_WSL())) {s = 14;}

                        else if ( (LA54_1==GTE) && (synpred110_WSL())) {s = 15;}

                        else if ( (LA54_1==LTE) && (synpred110_WSL())) {s = 16;}

                        else if ( (LA54_1==GT) && (synpred110_WSL())) {s = 17;}

                        else if ( (LA54_1==LT) && (synpred110_WSL())) {s = 18;}

                        else if ( (LA54_1==AMP) && (synpred110_WSL())) {s = 19;}

                        else if ( (LA54_1==VERT) && (synpred110_WSL())) {s = 20;}

                        else if ( (LA54_1==EXISTS) && (synpred110_WSL())) {s = 21;}

                        else if ( (LA54_1==CONTAINS) && (synpred110_WSL())) {s = 22;}

                        else if ( (LA54_1==CONTAINSRE) && (synpred110_WSL())) {s = 23;}

                        else if ( (LA54_1==ACTION) && (synpred110_WSL())) {s = 24;}

                        else if ( (LA54_1==TRUE) && (synpred110_WSL())) {s = 25;}

                        else if ( (LA54_1==FALSE) && (synpred110_WSL())) {s = 26;}

                        else if ( (LA54_1==WHEN) && (synpred110_WSL())) {s = 27;}

                        else if ( (LA54_1==REMOVE) && (synpred110_WSL())) {s = 28;}

                        else if ( (LA54_1==CLEAR) && (synpred110_WSL())) {s = 29;}

                        else if ( (LA54_1==INSTANT) && (synpred110_WSL())) {s = 30;}

                        else if ( (LA54_1==BACKSLASH) && (synpred110_WSL())) {s = 31;}

                         
                        input.seek(index54_1);
                        if ( s>=0 ) return s;
                        break;
                    case 2 : 
                        int LA54_58 = input.LA(1);

                         
                        int index54_58 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred116_WSL()) ) {s = 3;}

                        else if ( (true) ) {s = 5;}

                         
                        input.seek(index54_58);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 54, _s, input);
            error(nvae);
            throw nvae;
        }
    }
 

    public static final BitSet FOLLOW_line_in_script62 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_EOL_in_script65 = new BitSet(new long[]{0x0000001FFFFFFFF0L});
    public static final BitSet FOLLOW_line_in_script67 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_EOF_in_script71 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LABEL_in_line85 = new BitSet(new long[]{0x0000001FFFFFFFC2L});
    public static final BitSet FOLLOW_expr_in_line92 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IF_in_expr118 = new BitSet(new long[]{0x00000003F7000080L});
    public static final BitSet FOLLOW_BLANK_in_expr120 = new BitSet(new long[]{0x00000003F7000080L});
    public static final BitSet FOLLOW_conditionalExpression_in_expr125 = new BitSet(new long[]{0x0000000000000180L});
    public static final BitSet FOLLOW_BLANK_in_expr127 = new BitSet(new long[]{0x0000000000000180L});
    public static final BitSet FOLLOW_THEN_in_expr130 = new BitSet(new long[]{0x0000001FFFFFFFC0L});
    public static final BitSet FOLLOW_BLANK_in_expr132 = new BitSet(new long[]{0x0000001FFFFFFFC0L});
    public static final BitSet FOLLOW_expr_in_expr137 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ACTION_in_expr151 = new BitSet(new long[]{0x0000001FFFFFFFC0L});
    public static final BitSet FOLLOW_BLANK_in_expr153 = new BitSet(new long[]{0x0000001FFFFFFFC0L});
    public static final BitSet FOLLOW_REMOVE_in_expr167 = new BitSet(new long[]{0x0000001FFFFFFFC0L});
    public static final BitSet FOLLOW_BLANK_in_expr169 = new BitSet(new long[]{0x0000001FFFFFFFC0L});
    public static final BitSet FOLLOW_string_list_in_expr176 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_CLEAR_in_expr192 = new BitSet(new long[]{0x0000000000000082L});
    public static final BitSet FOLLOW_BLANK_in_expr194 = new BitSet(new long[]{0x0000000000000082L});
    public static final BitSet FOLLOW_expr_in_expr208 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_WHEN_in_expr210 = new BitSet(new long[]{0x0000001FFFFFFFC0L});
    public static final BitSet FOLLOW_BLANK_in_expr212 = new BitSet(new long[]{0x0000001FFFFFFFC0L});
    public static final BitSet FOLLOW_string_list_in_expr219 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_INSTANT_in_expr238 = new BitSet(new long[]{0x0000001FFFFFFFC0L});
    public static final BitSet FOLLOW_BLANK_in_expr240 = new BitSet(new long[]{0x0000001FFFFFFFC0L});
    public static final BitSet FOLLOW_expr_in_expr245 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_string_list_in_expr256 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_conditionalAndExpression_in_conditionalExpression282 = new BitSet(new long[]{0x0000000000004082L});
    public static final BitSet FOLLOW_BLANK_in_conditionalExpression289 = new BitSet(new long[]{0x0000000000004080L});
    public static final BitSet FOLLOW_OR_in_conditionalExpression292 = new BitSet(new long[]{0x00000003F7000080L});
    public static final BitSet FOLLOW_BLANK_in_conditionalExpression294 = new BitSet(new long[]{0x00000003F7000080L});
    public static final BitSet FOLLOW_conditionalAndExpression_in_conditionalExpression299 = new BitSet(new long[]{0x0000000000004082L});
    public static final BitSet FOLLOW_equalityExpression_in_conditionalAndExpression336 = new BitSet(new long[]{0x0000000000008082L});
    public static final BitSet FOLLOW_BLANK_in_conditionalAndExpression341 = new BitSet(new long[]{0x0000000000008080L});
    public static final BitSet FOLLOW_AND_in_conditionalAndExpression344 = new BitSet(new long[]{0x00000003F7000080L});
    public static final BitSet FOLLOW_BLANK_in_conditionalAndExpression346 = new BitSet(new long[]{0x00000003F7000080L});
    public static final BitSet FOLLOW_equalityExpression_in_conditionalAndExpression351 = new BitSet(new long[]{0x0000000000008082L});
    public static final BitSet FOLLOW_relationalExpression_in_equalityExpression388 = new BitSet(new long[]{0x0000000000030082L});
    public static final BitSet FOLLOW_BLANK_in_equalityExpression395 = new BitSet(new long[]{0x0000000000030080L});
    public static final BitSet FOLLOW_equalityOp_in_equalityExpression400 = new BitSet(new long[]{0x00000003F7000080L});
    public static final BitSet FOLLOW_BLANK_in_equalityExpression402 = new BitSet(new long[]{0x00000003F7000080L});
    public static final BitSet FOLLOW_relationalExpression_in_equalityExpression407 = new BitSet(new long[]{0x0000000000030082L});
    public static final BitSet FOLLOW_EQUAL_in_equalityOp437 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NOTEQUAL_in_equalityOp446 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_unaryExpression_in_relationalExpression473 = new BitSet(new long[]{0x0000000000FC0082L});
    public static final BitSet FOLLOW_BLANK_in_relationalExpression480 = new BitSet(new long[]{0x0000000000FC0080L});
    public static final BitSet FOLLOW_relationalOp_in_relationalExpression485 = new BitSet(new long[]{0x00000003F7000080L});
    public static final BitSet FOLLOW_BLANK_in_relationalExpression487 = new BitSet(new long[]{0x00000003F7000080L});
    public static final BitSet FOLLOW_unaryExpression_in_relationalExpression492 = new BitSet(new long[]{0x0000000000FC0082L});
    public static final BitSet FOLLOW_GT_in_relationalOp522 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LT_in_relationalOp530 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_GTE_in_relationalOp538 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LTE_in_relationalOp546 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_CONTAINS_in_relationalOp554 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_CONTAINSRE_in_relationalOp561 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NOT_in_unaryExpression578 = new BitSet(new long[]{0x00000003F7000080L});
    public static final BitSet FOLLOW_BLANK_in_unaryExpression580 = new BitSet(new long[]{0x00000003F7000080L});
    public static final BitSet FOLLOW_unaryExpression_in_unaryExpression585 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_EXISTS_in_unaryExpression593 = new BitSet(new long[]{0x00000003F7000080L});
    public static final BitSet FOLLOW_BLANK_in_unaryExpression595 = new BitSet(new long[]{0x00000003F7000080L});
    public static final BitSet FOLLOW_unaryExpression_in_unaryExpression600 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_primaryExpression_in_unaryExpression610 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_parenExpression631 = new BitSet(new long[]{0x00000003F7000080L});
    public static final BitSet FOLLOW_BLANK_in_parenExpression633 = new BitSet(new long[]{0x00000003F7000080L});
    public static final BitSet FOLLOW_conditionalExpression_in_parenExpression638 = new BitSet(new long[]{0x0000000008000080L});
    public static final BitSet FOLLOW_BLANK_in_parenExpression640 = new BitSet(new long[]{0x0000000008000080L});
    public static final BitSet FOLLOW_RPAREN_in_parenExpression643 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_parenExpression_in_primaryExpression663 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_cond_value_in_primaryExpression673 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PERCENT_in_cond_value694 = new BitSet(new long[]{0x0000001DFFFFFFC0L});
    public static final BitSet FOLLOW_common_string_in_cond_value699 = new BitSet(new long[]{0x0000000010000002L});
    public static final BitSet FOLLOW_PERCENT_in_cond_value706 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_escaped_var_in_cond_value715 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DOLLAR_in_cond_value723 = new BitSet(new long[]{0x0000001DFFFFFFC0L});
    public static final BitSet FOLLOW_common_string_in_cond_value728 = new BitSet(new long[]{0x0000000020000002L});
    public static final BitSet FOLLOW_DOLLAR_in_cond_value735 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_escaped_var_in_cond_value744 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_number_in_cond_value754 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TRUE_in_cond_value762 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FALSE_in_cond_value772 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_quoted_string_in_cond_value784 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STRING_in_number805 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_string_list_helper_in_string_list824 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_string_value_in_string_list_helper846 = new BitSet(new long[]{0x0000001FFFFFFFC2L});
    public static final BitSet FOLLOW_string_list_helper_in_string_list_helper851 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_variable_in_string_value898 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_text_string_in_string_value907 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_QUOTE_in_string_value916 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_QUOTE_in_quoted_string935 = new BitSet(new long[]{0x0000001DFFFFFFC0L});
    public static final BitSet FOLLOW_quoted_string_helper_in_quoted_string939 = new BitSet(new long[]{0x0000000200000000L});
    public static final BitSet FOLLOW_QUOTE_in_quoted_string941 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_quoted_string_value_in_quoted_string_helper962 = new BitSet(new long[]{0x0000001DFFFFFFC2L});
    public static final BitSet FOLLOW_quoted_string_helper_in_quoted_string_helper967 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_variable_in_quoted_string_value1014 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_BACKSLASH_in_quoted_string_value1028 = new BitSet(new long[]{0x0000000200000000L});
    public static final BitSet FOLLOW_QUOTE_in_quoted_string_value1032 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_text_string_in_quoted_string_value1041 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STRING_in_common_string1061 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IF_in_common_string1067 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_THEN_in_common_string1073 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_OR_in_common_string1079 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_AND_in_common_string1085 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NOTEQUAL_in_common_string1091 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NOT_in_common_string1099 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_EQUAL_in_common_string1105 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_GTE_in_common_string1111 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LTE_in_common_string1117 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_GT_in_common_string1123 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LT_in_common_string1129 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_AMP_in_common_string1136 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_VERT_in_common_string1144 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_EXISTS_in_common_string1150 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_CONTAINS_in_common_string1156 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_CONTAINSRE_in_common_string1162 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ACTION_in_common_string1168 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TRUE_in_common_string1174 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FALSE_in_common_string1182 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WHEN_in_common_string1190 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_REMOVE_in_common_string1196 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_CLEAR_in_common_string1202 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_INSTANT_in_common_string1210 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_BACKSLASH_in_common_string1216 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PERCENT_in_text_string1244 = new BitSet(new long[]{0x0000000010000000L});
    public static final BitSet FOLLOW_PERCENT_in_text_string1248 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DOLLAR_in_text_string1261 = new BitSet(new long[]{0x0000000020000000L});
    public static final BitSet FOLLOW_DOLLAR_in_text_string1265 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PERCENT_in_text_string1273 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DOLLAR_in_text_string1279 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RPAREN_in_text_string1285 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_text_string1291 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_BLANK_in_text_string1297 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_common_string_in_text_string1309 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PERCENT_in_variable1326 = new BitSet(new long[]{0x0000001DFFFFFFC0L});
    public static final BitSet FOLLOW_escaped_var_in_variable1331 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_variable_string_in_variable1337 = new BitSet(new long[]{0x0000000010000002L});
    public static final BitSet FOLLOW_PERCENT_in_variable1339 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DOLLAR_in_variable1350 = new BitSet(new long[]{0x0000001DFFFFFFC0L});
    public static final BitSet FOLLOW_escaped_var_in_variable1355 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_variable_string_in_variable1361 = new BitSet(new long[]{0x0000000020000002L});
    public static final BitSet FOLLOW_DOLLAR_in_variable1363 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_variable_string_helper_in_variable_string1386 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_common_string_in_variable_string_helper1407 = new BitSet(new long[]{0x0000001DFFFFFFC2L});
    public static final BitSet FOLLOW_variable_string_helper_in_variable_string_helper1411 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_escaped_var1431 = new BitSet(new long[]{0x0000001FFFFFFFC0L});
    public static final BitSet FOLLOW_vstring_list_in_escaped_var1435 = new BitSet(new long[]{0x0000000008000000L});
    public static final BitSet FOLLOW_RPAREN_in_escaped_var1437 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_vstring_list_helper_in_vstring_list1458 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_vstring_value_in_vstring_list_helper1479 = new BitSet(new long[]{0x0000001FFFFFFFC2L});
    public static final BitSet FOLLOW_vstring_list_helper_in_vstring_list_helper1484 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_variable_in_vstring_value1531 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PERCENT_in_vstring_value1546 = new BitSet(new long[]{0x0000000010000000L});
    public static final BitSet FOLLOW_PERCENT_in_vstring_value1550 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DOLLAR_in_vstring_value1563 = new BitSet(new long[]{0x0000000020000000L});
    public static final BitSet FOLLOW_DOLLAR_in_vstring_value1567 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_QUOTE_in_vstring_value1575 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_BACKSLASH_in_vstring_value1586 = new BitSet(new long[]{0x0000000004000000L});
    public static final BitSet FOLLOW_LPAREN_in_vstring_value1590 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_BACKSLASH_in_vstring_value1603 = new BitSet(new long[]{0x0000000008000000L});
    public static final BitSet FOLLOW_RPAREN_in_vstring_value1607 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_BLANK_in_vstring_value1613 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_common_string_in_vstring_value1625 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IF_in_synpred4_WSL114 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_BLANK_in_synpred7_WSL132 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ACTION_in_synpred8_WSL147 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_BLANK_in_synpred9_WSL153 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_REMOVE_in_synpred10_WSL163 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_BLANK_in_synpred11_WSL169 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_CLEAR_in_synpred12_WSL188 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_BLANK_in_synpred14_WSL212 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_INSTANT_in_synpred15_WSL234 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_BLANK_in_synpred16_WSL240 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_string_list_helper_in_synpred51_WSL851 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_synpred57_WSL873 = new BitSet(new long[]{0x000001FFCFFFFF60L});
    public static final BitSet FOLLOW_set_in_synpred57_WSL881 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_text_string_in_synpred58_WSL907 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_synpred65_WSL989 = new BitSet(new long[]{0x000001FFCFFFFF60L});
    public static final BitSet FOLLOW_set_in_synpred65_WSL997 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_BACKSLASH_in_synpred66_WSL1022 = new BitSet(new long[]{0x0000000200000000L});
    public static final BitSet FOLLOW_QUOTE_in_synpred66_WSL1024 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PERCENT_in_synpred91_WSL1238 = new BitSet(new long[]{0x0000000010000000L});
    public static final BitSet FOLLOW_PERCENT_in_synpred91_WSL1240 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DOLLAR_in_synpred92_WSL1255 = new BitSet(new long[]{0x0000000020000000L});
    public static final BitSet FOLLOW_DOLLAR_in_synpred92_WSL1257 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PERCENT_in_synpred93_WSL1273 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DOLLAR_in_synpred94_WSL1279 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PERCENT_in_synpred99_WSL1339 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DOLLAR_in_synpred102_WSL1363 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_variable_string_helper_in_synpred103_WSL1411 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_synpred110_WSL1506 = new BitSet(new long[]{0x000001FFCFFFFF60L});
    public static final BitSet FOLLOW_set_in_synpred110_WSL1514 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PERCENT_in_synpred111_WSL1540 = new BitSet(new long[]{0x0000000010000000L});
    public static final BitSet FOLLOW_PERCENT_in_synpred111_WSL1542 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DOLLAR_in_synpred112_WSL1557 = new BitSet(new long[]{0x0000000020000000L});
    public static final BitSet FOLLOW_DOLLAR_in_synpred112_WSL1559 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_BACKSLASH_in_synpred114_WSL1580 = new BitSet(new long[]{0x0000000004000000L});
    public static final BitSet FOLLOW_LPAREN_in_synpred114_WSL1582 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_BACKSLASH_in_synpred115_WSL1597 = new BitSet(new long[]{0x0000000008000000L});
    public static final BitSet FOLLOW_RPAREN_in_synpred115_WSL1599 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PERCENT_in_synpred116_WSL1546 = new BitSet(new long[]{0x0000000010000000L});
    public static final BitSet FOLLOW_PERCENT_in_synpred116_WSL1550 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DOLLAR_in_synpred116_WSL1563 = new BitSet(new long[]{0x0000000020000000L});
    public static final BitSet FOLLOW_DOLLAR_in_synpred116_WSL1567 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_QUOTE_in_synpred116_WSL1575 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_BACKSLASH_in_synpred116_WSL1586 = new BitSet(new long[]{0x0000000004000000L});
    public static final BitSet FOLLOW_LPAREN_in_synpred116_WSL1590 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_BACKSLASH_in_synpred116_WSL1603 = new BitSet(new long[]{0x0000000008000000L});
    public static final BitSet FOLLOW_RPAREN_in_synpred116_WSL1607 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_BLANK_in_synpred116_WSL1613 = new BitSet(new long[]{0x0000000000000002L});

}