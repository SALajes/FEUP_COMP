/* Generated By:JJTree&JavaCC: Do not edit this line. Javamm.java */
public class Javamm/*@bgen(jjtree)*/implements JavammTreeConstants, JavammConstants {/*@bgen(jjtree)*/
  protected static JJTJavammState jjtree = new JJTJavammState();static int numErrors = 0;
    static final int MAX_NUM_ERRORS = 5;
        public static void main(String args[]) throws ParseException {
                Javamm javamm = new Javamm(System.in);
                SimpleNode root = javamm.Start();
                root.dump("");
        }

  static final public SimpleNode Start() throws ParseException {
                     /*@bgen(jjtree) Start */
  SimpleNode jjtn000 = new SimpleNode(JJTSTART);
  boolean jjtc000 = true;
  jjtree.openNodeScope(jjtn000);
    try {
      label_1:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case IMPORT:
          ;
          break;
        default:
          jj_la1[0] = jj_gen;
          break label_1;
        }
        ImportDeclaration();
      }
      ClassDeclaration();
      jj_consume_token(0);
          jjtree.closeNodeScope(jjtn000, true);
          jjtc000 = false;
          {if (true) return jjtn000;}
    } catch (Throwable jjte000) {
          if (jjtc000) {
            jjtree.clearNodeScope(jjtn000);
            jjtc000 = false;
          } else {
            jjtree.popNode();
          }
          if (jjte000 instanceof RuntimeException) {
            {if (true) throw (RuntimeException)jjte000;}
          }
          if (jjte000 instanceof ParseException) {
            {if (true) throw (ParseException)jjte000;}
          }
          {if (true) throw (Error)jjte000;}
    } finally {
          if (jjtc000) {
            jjtree.closeNodeScope(jjtn000, true);
          }
    }
    throw new Error("Missing return statement in function");
  }

  static final public void ImportDeclaration() throws ParseException {
    jj_consume_token(IMPORT);
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case STATIC:
      jj_consume_token(STATIC);
      break;
    default:
      jj_la1[1] = jj_gen;
      ;
    }
    jj_consume_token(ID);
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case DOT:
    case OPAREN:
      label_2:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case DOT:
          ;
          break;
        default:
          jj_la1[2] = jj_gen;
          break label_2;
        }
        jj_consume_token(DOT);
        jj_consume_token(ID);
      }
      jj_consume_token(OPAREN);
      Type();
      label_3:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case COL:
          ;
          break;
        default:
          jj_la1[3] = jj_gen;
          break label_3;
        }
        jj_consume_token(COL);
        Type();
      }
      jj_consume_token(CPAREN);
      break;
    default:
      jj_la1[4] = jj_gen;
      ;
    }
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case VOID:
    case INT:
    case BOOLEAN:
    case ID:
      Type();
      break;
    default:
      jj_la1[5] = jj_gen;
      ;
    }
    jj_consume_token(SEMICOL);
  }

  static final public void ClassDeclaration() throws ParseException {
    jj_consume_token(CLASS);
    jj_consume_token(ID);
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case EXTENDS:
      jj_consume_token(EXTENDS);
      jj_consume_token(ID);
      break;
    default:
      jj_la1[6] = jj_gen;
      ;
    }
    jj_consume_token(OCURLY);
    label_4:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case VOID:
      case INT:
      case BOOLEAN:
      case ID:
        ;
        break;
      default:
        jj_la1[7] = jj_gen;
        break label_4;
      }
      VarDeclaration();
    }
    label_5:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case PUBLIC:
        ;
        break;
      default:
        jj_la1[8] = jj_gen;
        break label_5;
      }
      MethodDeclaration();
    }
    jj_consume_token(CCURLY);
  }

  static final public void VarDeclaration() throws ParseException {
    Type();
    jj_consume_token(ID);
    jj_consume_token(SEMICOL);
  }

  static final public void MethodDeclaration() throws ParseException {
    jj_consume_token(PUBLIC);
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case STATIC:
      MainDeclaration();
      break;
    case VOID:
    case INT:
    case BOOLEAN:
    case ID:
      FunctionDeclaration();
      break;
    default:
      jj_la1[9] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
  }

  static final public void MainDeclaration() throws ParseException {
    jj_consume_token(STATIC);
    jj_consume_token(VOID);
    jj_consume_token(MAIN);
    jj_consume_token(OPAREN);
    jj_consume_token(STRING);
    jj_consume_token(OSBRACKET);
    jj_consume_token(CSBRACKET);
    jj_consume_token(ID);
    jj_consume_token(CPAREN);
    jj_consume_token(OCURLY);
    label_6:
    while (true) {
      if (jj_2_1(2)) {
        ;
      } else {
        break label_6;
      }
      VarDeclaration();
    }
    label_7:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case NEW:
      case IF:
      case WHILE:
      case TRUE:
      case FALSE:
      case THIS:
      case NOT:
      case OCURLY:
      case OPAREN:
      case NUM:
      case ID:
        ;
        break;
      default:
        jj_la1[10] = jj_gen;
        break label_7;
      }
      Statement();
    }
    jj_consume_token(CCURLY);
  }

  static final public void FunctionDeclaration() throws ParseException {
    Type();
    jj_consume_token(ID);
    jj_consume_token(OPAREN);
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case VOID:
    case INT:
    case BOOLEAN:
    case ID:
      Type();
      jj_consume_token(ID);
      label_8:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case COL:
          ;
          break;
        default:
          jj_la1[11] = jj_gen;
          break label_8;
        }
        jj_consume_token(COL);
        Type();
        jj_consume_token(ID);
      }
      break;
    default:
      jj_la1[12] = jj_gen;
      ;
    }
    jj_consume_token(CPAREN);
    jj_consume_token(OCURLY);
    label_9:
    while (true) {
      if (jj_2_2(2)) {
        ;
      } else {
        break label_9;
      }
      VarDeclaration();
    }
    label_10:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case NEW:
      case IF:
      case WHILE:
      case TRUE:
      case FALSE:
      case THIS:
      case NOT:
      case OCURLY:
      case OPAREN:
      case NUM:
      case ID:
        ;
        break;
      default:
        jj_la1[13] = jj_gen;
        break label_10;
      }
      Statement();
    }
    jj_consume_token(RETURN);
    Expression();
    jj_consume_token(SEMICOL);
          SimpleNode jjtn001 = new SimpleNode(JJTTERM);
          boolean jjtc001 = true;
          jjtree.openNodeScope(jjtn001);
    try {
      jj_consume_token(CCURLY);
    } finally {
          if (jjtc001) {
            jjtree.closeNodeScope(jjtn001, true);
          }
    }
  }

  static final public void Type() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case INT:
      jj_consume_token(INT);
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case OSBRACKET:
        jj_consume_token(OSBRACKET);
        jj_consume_token(CSBRACKET);
        break;
      default:
        jj_la1[14] = jj_gen;
        ;
      }
      break;
    case BOOLEAN:
      jj_consume_token(BOOLEAN);
      break;
    case ID:
      jj_consume_token(ID);
      break;
    case VOID:
      jj_consume_token(VOID);
      break;
    default:
      jj_la1[15] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
  }

  static final public void Statement() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case OCURLY:
      jj_consume_token(OCURLY);
      label_11:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case NEW:
        case IF:
        case WHILE:
        case TRUE:
        case FALSE:
        case THIS:
        case NOT:
        case OCURLY:
        case OPAREN:
        case NUM:
        case ID:
          ;
          break;
        default:
          jj_la1[16] = jj_gen;
          break label_11;
        }
        Statement();
      }
      jj_consume_token(CCURLY);
      break;
    case IF:
      jj_consume_token(IF);
      jj_consume_token(OPAREN);
      Expression();
      jj_consume_token(CPAREN);
      Statement();
      jj_consume_token(ELSE);
      Statement();
      break;
    case NEW:
    case TRUE:
    case FALSE:
    case THIS:
    case NOT:
    case OPAREN:
    case NUM:
    case ID:
      if (jj_2_3(2)) {
        jj_consume_token(ID);
        StatementId();
      } else {
        ;
      }
      Expression();
      jj_consume_token(SEMICOL);
      break;
    case WHILE:
      jj_consume_token(WHILE);
      jj_consume_token(OPAREN);
      try {
        Expression();
        jj_consume_token(CPAREN);
      } catch (ParseException e) {
        System.out.println(e.toString());  // print the error message
        Token t;
        Javamm.numErrors++; // Increase the number of errors since execution

        if (Javamm.numErrors > 5) { // Chose 5 but this value should be passed through command line maybe?
        System.out.println("Too many errors. Aborting.");
        System.exit(-1);
        }

        do {
          t = getNextToken(); // Skip the tokens until the close of parenthesis
        } while (t.kind != CPAREN);
          // The above loop consumes tokens all the way up to a token of
          // "kind".  We use a do-while loop rather than a while because the
          // current token is the one immediately before the erroneous token
          // (in our case the token immediately before what should have been
          // "if"/"while".

      }
      Statement();
      break;
    default:
      jj_la1[17] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
  }

  static final public void StatementId() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case EQUALS:
      jj_consume_token(EQUALS);
      break;
    case OSBRACKET:
      jj_consume_token(OSBRACKET);
      Expression();
      jj_consume_token(CSBRACKET);
      jj_consume_token(EQUALS);
      break;
    default:
      jj_la1[18] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
  }

  static final public void Expression() throws ParseException {
    ExpressionTerminal();
    if (jj_2_4(2)) {
      ExpressionL();
    } else {
      ;
    }
  }

  static final public void ExpressionL() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case LTHAN:
    case PLUS:
    case MINUS:
    case ASTERISK:
    case FSLASH:
    case AND:
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case AND:
        jj_consume_token(AND);
        break;
      case LTHAN:
        jj_consume_token(LTHAN);
        break;
      case PLUS:
        jj_consume_token(PLUS);
        break;
      case MINUS:
        jj_consume_token(MINUS);
        break;
      case ASTERISK:
        jj_consume_token(ASTERISK);
        break;
      case FSLASH:
        jj_consume_token(FSLASH);
        break;
      default:
        jj_la1[19] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
      Expression();
      break;
    case OSBRACKET:
      jj_consume_token(OSBRACKET);
      Expression();
      jj_consume_token(CSBRACKET);
      break;
    case DOT:
      ExpressionDot();
      break;
    default:
      jj_la1[20] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
  }

  static final public void ExpressionDot() throws ParseException {
    jj_consume_token(DOT);
    ExpressionDotL();
  }

  static final public void ExpressionDotL() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case LEN:
      jj_consume_token(LEN);
      break;
    case ID:
      jj_consume_token(ID);
      jj_consume_token(OPAREN);
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case NEW:
      case TRUE:
      case FALSE:
      case THIS:
      case NOT:
      case OPAREN:
      case NUM:
      case ID:
        Expression();
        label_12:
        while (true) {
          switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
          case COL:
            ;
            break;
          default:
            jj_la1[21] = jj_gen;
            break label_12;
          }
          jj_consume_token(COL);
          Expression();
        }
        break;
      default:
        jj_la1[22] = jj_gen;
        ;
      }
      jj_consume_token(CPAREN);
      break;
    default:
      jj_la1[23] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
  }

  static final public void ExpressionTerminal() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case NUM:
      jj_consume_token(NUM);
      break;
    case TRUE:
      jj_consume_token(TRUE);
      break;
    case FALSE:
      jj_consume_token(FALSE);
      break;
    case ID:
      jj_consume_token(ID);
      break;
    case THIS:
      jj_consume_token(THIS);
      break;
    case NOT:
      jj_consume_token(NOT);
      Expression();
      break;
    case NEW:
      ExpressionNew();
      break;
    case OPAREN:
      jj_consume_token(OPAREN);
      Expression();
      jj_consume_token(CPAREN);
      break;
    default:
      jj_la1[24] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
  }

  static final public void ExpressionNew() throws ParseException {
    jj_consume_token(NEW);
    ExpressionNewL();
  }

  static final public void ExpressionNewL() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case INT:
      jj_consume_token(INT);
      jj_consume_token(OSBRACKET);
      Expression();
      jj_consume_token(CSBRACKET);
      break;
    case ID:
      jj_consume_token(ID);
      jj_consume_token(OPAREN);
      jj_consume_token(CPAREN);
      break;
    default:
      jj_la1[25] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
  }

  static private boolean jj_2_1(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_1(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(0, xla); }
  }

  static private boolean jj_2_2(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_2(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(1, xla); }
  }

  static private boolean jj_2_3(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_3(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(2, xla); }
  }

  static private boolean jj_2_4(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_4(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(3, xla); }
  }

  static private boolean jj_3R_26() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_scan_token(14)) {
    jj_scanpos = xsp;
    if (jj_3R_30()) return true;
    }
    return false;
  }

  static private boolean jj_3_3() {
    if (jj_scan_token(ID)) return true;
    if (jj_3R_14()) return true;
    return false;
  }

  static private boolean jj_3R_23() {
    if (jj_scan_token(DOT)) return true;
    if (jj_3R_26()) return true;
    return false;
  }

  static private boolean jj_3R_20() {
    if (jj_3R_23()) return true;
    return false;
  }

  static private boolean jj_3R_24() {
    if (jj_scan_token(OSBRACKET)) return true;
    return false;
  }

  static private boolean jj_3R_19() {
    if (jj_scan_token(OSBRACKET)) return true;
    if (jj_3R_22()) return true;
    return false;
  }

  static private boolean jj_3R_15() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_18()) {
    jj_scanpos = xsp;
    if (jj_3R_19()) {
    jj_scanpos = xsp;
    if (jj_3R_20()) return true;
    }
    }
    return false;
  }

  static private boolean jj_3R_18() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_scan_token(33)) {
    jj_scanpos = xsp;
    if (jj_scan_token(26)) {
    jj_scanpos = xsp;
    if (jj_scan_token(28)) {
    jj_scanpos = xsp;
    if (jj_scan_token(29)) {
    jj_scanpos = xsp;
    if (jj_scan_token(30)) {
    jj_scanpos = xsp;
    if (jj_scan_token(31)) return true;
    }
    }
    }
    }
    }
    if (jj_3R_22()) return true;
    return false;
  }

  static private boolean jj_3_2() {
    if (jj_3R_13()) return true;
    return false;
  }

  static private boolean jj_3R_22() {
    if (jj_3R_25()) return true;
    return false;
  }

  static private boolean jj_3R_16() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_21()) {
    jj_scanpos = xsp;
    if (jj_scan_token(18)) {
    jj_scanpos = xsp;
    if (jj_scan_token(46)) {
    jj_scanpos = xsp;
    if (jj_scan_token(12)) return true;
    }
    }
    }
    return false;
  }

  static private boolean jj_3R_21() {
    if (jj_scan_token(INT)) return true;
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_24()) jj_scanpos = xsp;
    return false;
  }

  static private boolean jj_3R_17() {
    if (jj_scan_token(OSBRACKET)) return true;
    return false;
  }

  static private boolean jj_3_1() {
    if (jj_3R_13()) return true;
    return false;
  }

  static private boolean jj_3R_14() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_scan_token(32)) {
    jj_scanpos = xsp;
    if (jj_3R_17()) return true;
    }
    return false;
  }

  static private boolean jj_3R_31() {
    if (jj_scan_token(NEW)) return true;
    return false;
  }

  static private boolean jj_3R_29() {
    if (jj_scan_token(OPAREN)) return true;
    return false;
  }

  static private boolean jj_3R_28() {
    if (jj_3R_31()) return true;
    return false;
  }

  static private boolean jj_3R_27() {
    if (jj_scan_token(NOT)) return true;
    return false;
  }

  static private boolean jj_3_4() {
    if (jj_3R_15()) return true;
    return false;
  }

  static private boolean jj_3R_13() {
    if (jj_3R_16()) return true;
    if (jj_scan_token(ID)) return true;
    return false;
  }

  static private boolean jj_3R_25() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_scan_token(45)) {
    jj_scanpos = xsp;
    if (jj_scan_token(23)) {
    jj_scanpos = xsp;
    if (jj_scan_token(24)) {
    jj_scanpos = xsp;
    if (jj_scan_token(46)) {
    jj_scanpos = xsp;
    if (jj_scan_token(25)) {
    jj_scanpos = xsp;
    if (jj_3R_27()) {
    jj_scanpos = xsp;
    if (jj_3R_28()) {
    jj_scanpos = xsp;
    if (jj_3R_29()) return true;
    }
    }
    }
    }
    }
    }
    }
    return false;
  }

  static private boolean jj_3R_30() {
    if (jj_scan_token(ID)) return true;
    return false;
  }

  static private boolean jj_initialized_once = false;
  /** Generated Token Manager. */
  static public JavammTokenManager token_source;
  static SimpleCharStream jj_input_stream;
  /** Current token. */
  static public Token token;
  /** Next token. */
  static public Token jj_nt;
  static private int jj_ntk;
  static private Token jj_scanpos, jj_lastpos;
  static private int jj_la;
  static private int jj_gen;
  static final private int[] jj_la1 = new int[26];
  static private int[] jj_la1_0;
  static private int[] jj_la1_1;
  static {
      jj_la1_init_0();
      jj_la1_init_1();
   }
   private static void jj_la1_init_0() {
      jj_la1_0 = new int[] {0x80,0x800,0x0,0x0,0x0,0x61000,0x200,0x61000,0x400,0x61800,0x3c82000,0x0,0x61000,0x3c82000,0x0,0x61000,0x3c82000,0x3c82000,0x0,0xf4000000,0xf4000000,0x0,0x3802000,0x4000,0x3802000,0x20000,};
   }
   private static void jj_la1_init_1() {
      jj_la1_1 = new int[] {0x0,0x0,0x10,0x8,0x110,0x4000,0x0,0x4000,0x0,0x4000,0x6160,0x8,0x4000,0x6160,0x400,0x4000,0x6160,0x6160,0x401,0x2,0x412,0x8,0x6120,0x4000,0x6120,0x4000,};
   }
  static final private JJCalls[] jj_2_rtns = new JJCalls[4];
  static private boolean jj_rescan = false;
  static private int jj_gc = 0;

  /** Constructor with InputStream. */
  public Javamm(java.io.InputStream stream) {
     this(stream, null);
  }
  /** Constructor with InputStream and supplied encoding */
  public Javamm(java.io.InputStream stream, String encoding) {
    if (jj_initialized_once) {
      System.out.println("ERROR: Second call to constructor of static parser.  ");
      System.out.println("       You must either use ReInit() or set the JavaCC option STATIC to false");
      System.out.println("       during parser generation.");
      throw new Error();
    }
    jj_initialized_once = true;
    try { jj_input_stream = new SimpleCharStream(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source = new JavammTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 26; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Reinitialise. */
  static public void ReInit(java.io.InputStream stream) {
     ReInit(stream, null);
  }
  /** Reinitialise. */
  static public void ReInit(java.io.InputStream stream, String encoding) {
    try { jj_input_stream.ReInit(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jjtree.reset();
    jj_gen = 0;
    for (int i = 0; i < 26; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Constructor. */
  public Javamm(java.io.Reader stream) {
    if (jj_initialized_once) {
      System.out.println("ERROR: Second call to constructor of static parser. ");
      System.out.println("       You must either use ReInit() or set the JavaCC option STATIC to false");
      System.out.println("       during parser generation.");
      throw new Error();
    }
    jj_initialized_once = true;
    jj_input_stream = new SimpleCharStream(stream, 1, 1);
    token_source = new JavammTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 26; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Reinitialise. */
  static public void ReInit(java.io.Reader stream) {
    jj_input_stream.ReInit(stream, 1, 1);
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jjtree.reset();
    jj_gen = 0;
    for (int i = 0; i < 26; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Constructor with generated Token Manager. */
  public Javamm(JavammTokenManager tm) {
    if (jj_initialized_once) {
      System.out.println("ERROR: Second call to constructor of static parser. ");
      System.out.println("       You must either use ReInit() or set the JavaCC option STATIC to false");
      System.out.println("       during parser generation.");
      throw new Error();
    }
    jj_initialized_once = true;
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 26; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Reinitialise. */
  public void ReInit(JavammTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jjtree.reset();
    jj_gen = 0;
    for (int i = 0; i < 26; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  static private Token jj_consume_token(int kind) throws ParseException {
    Token oldToken;
    if ((oldToken = token).next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    if (token.kind == kind) {
      jj_gen++;
      if (++jj_gc > 100) {
        jj_gc = 0;
        for (int i = 0; i < jj_2_rtns.length; i++) {
          JJCalls c = jj_2_rtns[i];
          while (c != null) {
            if (c.gen < jj_gen) c.first = null;
            c = c.next;
          }
        }
      }
      return token;
    }
    token = oldToken;
    jj_kind = kind;
    throw generateParseException();
  }

  static private final class LookaheadSuccess extends java.lang.Error { }
  static final private LookaheadSuccess jj_ls = new LookaheadSuccess();
  static private boolean jj_scan_token(int kind) {
    if (jj_scanpos == jj_lastpos) {
      jj_la--;
      if (jj_scanpos.next == null) {
        jj_lastpos = jj_scanpos = jj_scanpos.next = token_source.getNextToken();
      } else {
        jj_lastpos = jj_scanpos = jj_scanpos.next;
      }
    } else {
      jj_scanpos = jj_scanpos.next;
    }
    if (jj_rescan) {
      int i = 0; Token tok = token;
      while (tok != null && tok != jj_scanpos) { i++; tok = tok.next; }
      if (tok != null) jj_add_error_token(kind, i);
    }
    if (jj_scanpos.kind != kind) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) throw jj_ls;
    return false;
  }


/** Get the next Token. */
  static final public Token getNextToken() {
    if (token.next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    jj_gen++;
    return token;
  }

/** Get the specific Token. */
  static final public Token getToken(int index) {
    Token t = token;
    for (int i = 0; i < index; i++) {
      if (t.next != null) t = t.next;
      else t = t.next = token_source.getNextToken();
    }
    return t;
  }

  static private int jj_ntk() {
    if ((jj_nt=token.next) == null)
      return (jj_ntk = (token.next=token_source.getNextToken()).kind);
    else
      return (jj_ntk = jj_nt.kind);
  }

  static private java.util.List<int[]> jj_expentries = new java.util.ArrayList<int[]>();
  static private int[] jj_expentry;
  static private int jj_kind = -1;
  static private int[] jj_lasttokens = new int[100];
  static private int jj_endpos;

  static private void jj_add_error_token(int kind, int pos) {
    if (pos >= 100) return;
    if (pos == jj_endpos + 1) {
      jj_lasttokens[jj_endpos++] = kind;
    } else if (jj_endpos != 0) {
      jj_expentry = new int[jj_endpos];
      for (int i = 0; i < jj_endpos; i++) {
        jj_expentry[i] = jj_lasttokens[i];
      }
      boolean exists = false;
      for (java.util.Iterator<?> it = jj_expentries.iterator(); it.hasNext();) {
        exists = true;
        int[] oldentry = (int[])(it.next());
        if (oldentry.length == jj_expentry.length) {
          for (int i = 0; i < jj_expentry.length; i++) {
            if (oldentry[i] != jj_expentry[i]) {
              exists = false;
              break;
            }
          }
          if (exists) break;
        }
      }
      if (!exists) jj_expentries.add(jj_expentry);
      if (pos != 0) jj_lasttokens[(jj_endpos = pos) - 1] = kind;
    }
  }

  /** Generate ParseException. */
  static public ParseException generateParseException() {
    jj_expentries.clear();
    boolean[] la1tokens = new boolean[47];
    if (jj_kind >= 0) {
      la1tokens[jj_kind] = true;
      jj_kind = -1;
    }
    for (int i = 0; i < 26; i++) {
      if (jj_la1[i] == jj_gen) {
        for (int j = 0; j < 32; j++) {
          if ((jj_la1_0[i] & (1<<j)) != 0) {
            la1tokens[j] = true;
          }
          if ((jj_la1_1[i] & (1<<j)) != 0) {
            la1tokens[32+j] = true;
          }
        }
      }
    }
    for (int i = 0; i < 47; i++) {
      if (la1tokens[i]) {
        jj_expentry = new int[1];
        jj_expentry[0] = i;
        jj_expentries.add(jj_expentry);
      }
    }
    jj_endpos = 0;
    jj_rescan_token();
    jj_add_error_token(0, 0);
    int[][] exptokseq = new int[jj_expentries.size()][];
    for (int i = 0; i < jj_expentries.size(); i++) {
      exptokseq[i] = jj_expentries.get(i);
    }
    return new ParseException(token, exptokseq, tokenImage);
  }

  /** Enable tracing. */
  static final public void enable_tracing() {
  }

  /** Disable tracing. */
  static final public void disable_tracing() {
  }

  static private void jj_rescan_token() {
    jj_rescan = true;
    for (int i = 0; i < 4; i++) {
    try {
      JJCalls p = jj_2_rtns[i];
      do {
        if (p.gen > jj_gen) {
          jj_la = p.arg; jj_lastpos = jj_scanpos = p.first;
          switch (i) {
            case 0: jj_3_1(); break;
            case 1: jj_3_2(); break;
            case 2: jj_3_3(); break;
            case 3: jj_3_4(); break;
          }
        }
        p = p.next;
      } while (p != null);
      } catch(LookaheadSuccess ls) { }
    }
    jj_rescan = false;
  }

  static private void jj_save(int index, int xla) {
    JJCalls p = jj_2_rtns[index];
    while (p.gen > jj_gen) {
      if (p.next == null) { p = p.next = new JJCalls(); break; }
      p = p.next;
    }
    p.gen = jj_gen + xla - jj_la; p.first = token; p.arg = xla;
  }

  static final class JJCalls {
    int gen;
    Token first;
    int arg;
    JJCalls next;
  }

}
