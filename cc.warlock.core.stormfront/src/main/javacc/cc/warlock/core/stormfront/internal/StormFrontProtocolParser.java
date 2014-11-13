/* Generated By:JavaCC: Do not edit this line. StormFrontProtocolParser.java */
package cc.warlock.core.stormfront.internal;

import java.io.IOException;

import cc.warlock.core.stormfront.xml.IStormFrontXMLHandler;
import cc.warlock.core.stormfront.xml.StormFrontAttribute;
import cc.warlock.core.stormfront.xml.StormFrontAttributeList;

public class StormFrontProtocolParser implements StormFrontProtocolParserConstants {
        private IStormFrontXMLHandler handler;
        private StormFrontAttributeList currentAttributes = new StormFrontAttributeList();

        public void setHandler (IStormFrontXMLHandler handler)
        {
                this.handler = handler;
        }

        protected void characters (String characters)
        {
                handler.characters(characters);
        }

        protected void startElement (String name)
        {
                handler.startElement(name, currentAttributes, token_source.tagBuffer.toString());
                currentAttributes.clear();
        }

        protected void addAttribute (String name, String value)
        {
                StormFrontAttribute attribute = new StormFrontAttribute();
                attribute.setName(name);
                attribute.setValue(value);

                currentAttributes.add(attribute);
        }

        protected void endElement (String name)
        {
                handler.endElement(name, token_source.tagBuffer.toString());
        }

        protected void emptyElement (String name)
        {
                handler.startElement(name, currentAttributes, token_source.tagBuffer.toString());
                currentAttributes.clear();
                handler.endElement(name, null);
        }

        public void passThrough() {
                SetState(PASS_THRU);
        }

        private void SetState(int state) {
                if (state != token_source.curLexState) {
                Token root = new Token(), last=root;
                root.next = null;

                // First, we build a list of tokens to push back, in backwards order
                while (token.next != null) {
                        Token t = token;
                        // Find the token whose token.next is the last in the chain
                        while (t.next != null && t.next.next != null)
                                t = t.next;

                        // put it at the end of the new chain
                        last.next = t.next;
                        last = t.next;

                        // If there are special tokens, these go before the regular tokens,
                        // so we want to push them back onto the input stream in the order
                        // we find them along the specialToken chain.

                        if (t.next.specialToken != null) {
                                Token tt=t.next.specialToken;
                                while (tt != null) {
                                        last.next = tt;
                                        last = tt;
                                        tt.next = null;
                                        tt = tt.specialToken;
                                }
                        }
                        t.next = null;
                }

                while (root.next != null) {
                        token_source.backup(root.next.image.length());
                        root.next = root.next.next;
                }
                jj_ntk = -1;
                token_source.SwitchTo(state);
                }
        }

  final public void Document() throws ParseException {
    label_1:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case TAG_START_OPEN:
      case TAG_END_OPEN:
      case ENTITY:
      case PCDATA:
      case PASS_STRING:
        ;
        break;
      default:
        jj_la1[0] = jj_gen;
        break label_1;
      }
      try {
        Element();
      } catch (TokenMgrError e) {
                System.err.println("Lexer error");
                try {
                        while(jj_input_stream.readChar() != '\u005cn') { }
                } catch (IOException io_e) {
                        {if (true) throw e;}
                }
      }
    }
    jj_consume_token(0);
  }

  final public void Element() throws ParseException {
                  Token data; String str;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case TAG_START_OPEN:
      Tag();
      break;
    case TAG_END_OPEN:
      EndTag();
      break;
    case PCDATA:
      data = jj_consume_token(PCDATA);
                            characters(data.image);
      break;
    case ENTITY:
      str = Entity();
                           characters(str);
      break;
    case PASS_STRING:
      data = jj_consume_token(PASS_STRING);
                                 characters(data.image);
      break;
    default:
      jj_la1[1] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
  }

  final public void Tag() throws ParseException {
              Token name;
    jj_consume_token(TAG_START_OPEN);
    name = jj_consume_token(GENERIC_ID);
    label_2:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case GENERIC_ID:
        ;
        break;
      default:
        jj_la1[2] = jj_gen;
        break label_2;
      }
      Attribute();
    }
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case TAG_CLOSE:
      jj_consume_token(TAG_CLOSE);
                                                                         startElement(name.image);
      break;
    case TAG_EMPTY_CLOSE:
      jj_consume_token(TAG_EMPTY_CLOSE);
                                                                                                                           emptyElement(name.image);
      break;
    default:
      jj_la1[3] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
  }

  final public void Attribute() throws ParseException {
                   Token name, value;
    name = jj_consume_token(GENERIC_ID);
                            value = name;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case ATTR_EQ:
      jj_consume_token(ATTR_EQ);
      value = jj_consume_token(ATTR_VALUE);
      break;
    default:
      jj_la1[4] = jj_gen;
      ;
    }
                                                                                  addAttribute(name.image,value.image);
  }

  final public void EndTag() throws ParseException {
                Token name;
    jj_consume_token(TAG_END_OPEN);
    name = jj_consume_token(GENERIC_ID);
    jj_consume_token(TAG_CLOSE);
                                                       endElement(name.image);
  }

  final public String Entity() throws ParseException {
                   Token entity;
    entity = jj_consume_token(ENTITY);
                if(entity.image.equals("gt")) {if (true) return ">";}
                if(entity.image.equals("lt")) {if (true) return "<";}
                if(entity.image.equals("apos")) {if (true) return "'";}
                if(entity.image.equals("quot")) {if (true) return "\u005c"";}
                if(entity.image.equals("amp")) {if (true) return "&";}
                if(entity.image.startsWith("#")) {if (true) return "?";}
                {if (true) return "?";}
    throw new Error("Missing return statement in function");
  }

  /** Generated Token Manager. */
  public StormFrontProtocolParserTokenManager token_source;
  SimpleCharStream jj_input_stream;
  /** Current token. */
  public Token token;
  /** Next token. */
  public Token jj_nt;
  private int jj_ntk;
  private int jj_gen;
  final private int[] jj_la1 = new int[5];
  static private int[] jj_la1_0;
  static {
      jj_la1_init_0();
   }
   private static void jj_la1_init_0() {
      jj_la1_0 = new int[] {0x1001e,0x1001e,0x2000,0x600,0x800,};
   }

  /** Constructor with InputStream. */
  public StormFrontProtocolParser(java.io.InputStream stream) {
     this(stream, null);
  }
  /** Constructor with InputStream and supplied encoding */
  public StormFrontProtocolParser(java.io.InputStream stream, String encoding) {
    try { jj_input_stream = new SimpleCharStream(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source = new StormFrontProtocolParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 5; i++) jj_la1[i] = -1;
  }

  /** Reinitialise. */
  public void ReInit(java.io.InputStream stream) {
     ReInit(stream, null);
  }
  /** Reinitialise. */
  public void ReInit(java.io.InputStream stream, String encoding) {
    try { jj_input_stream.ReInit(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 5; i++) jj_la1[i] = -1;
  }

  /** Constructor. */
  public StormFrontProtocolParser(java.io.Reader stream) {
    jj_input_stream = new SimpleCharStream(stream, 1, 1);
    token_source = new StormFrontProtocolParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 5; i++) jj_la1[i] = -1;
  }

  /** Reinitialise. */
  public void ReInit(java.io.Reader stream) {
    jj_input_stream.ReInit(stream, 1, 1);
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 5; i++) jj_la1[i] = -1;
  }

  /** Constructor with generated Token Manager. */
  public StormFrontProtocolParser(StormFrontProtocolParserTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 5; i++) jj_la1[i] = -1;
  }

  /** Reinitialise. */
  public void ReInit(StormFrontProtocolParserTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 5; i++) jj_la1[i] = -1;
  }

  private Token jj_consume_token(int kind) throws ParseException {
    Token oldToken;
    if ((oldToken = token).next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    if (token.kind == kind) {
      jj_gen++;
      return token;
    }
    token = oldToken;
    jj_kind = kind;
    throw generateParseException();
  }


/** Get the next Token. */
  final public Token getNextToken() {
    if (token.next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    jj_gen++;
    return token;
  }

/** Get the specific Token. */
  final public Token getToken(int index) {
    Token t = token;
    for (int i = 0; i < index; i++) {
      if (t.next != null) t = t.next;
      else t = t.next = token_source.getNextToken();
    }
    return t;
  }

  private int jj_ntk() {
    if ((jj_nt=token.next) == null)
      return (jj_ntk = (token.next=token_source.getNextToken()).kind);
    else
      return (jj_ntk = jj_nt.kind);
  }

  private java.util.List<int[]> jj_expentries = new java.util.ArrayList<int[]>();
  private int[] jj_expentry;
  private int jj_kind = -1;

  /** Generate ParseException. */
  public ParseException generateParseException() {
    jj_expentries.clear();
    boolean[] la1tokens = new boolean[17];
    if (jj_kind >= 0) {
      la1tokens[jj_kind] = true;
      jj_kind = -1;
    }
    for (int i = 0; i < 5; i++) {
      if (jj_la1[i] == jj_gen) {
        for (int j = 0; j < 32; j++) {
          if ((jj_la1_0[i] & (1<<j)) != 0) {
            la1tokens[j] = true;
          }
        }
      }
    }
    for (int i = 0; i < 17; i++) {
      if (la1tokens[i]) {
        jj_expentry = new int[1];
        jj_expentry[0] = i;
        jj_expentries.add(jj_expentry);
      }
    }
    int[][] exptokseq = new int[jj_expentries.size()][];
    for (int i = 0; i < jj_expentries.size(); i++) {
      exptokseq[i] = jj_expentries.get(i);
    }
    return new ParseException(token, exptokseq, tokenImage);
  }

  /** Enable tracing. */
  final public void enable_tracing() {
  }

  /** Disable tracing. */
  final public void disable_tracing() {
  }

}
