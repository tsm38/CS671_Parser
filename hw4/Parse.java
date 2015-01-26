import java.util.*;

public class Parse
{
    private String line;    // input buffer
    private int[] vars;     // variable store
    private Scanner scan;

    // constructor
    public Parse()
    {
        line = "";
        scan = new Scanner(System.in);
        vars = new int[26];
        for (int i=0; i<26; i++)
            vars[i]=0;
    }

    // entry point into Parse
    public void run()
    {
        String token;

        token = getToken();
        parseCode(token);                 // SQRL ::= <code>
    }

    // parses <code>
    private void parseCode(String token)
    {
        while (!token.equals("."))
        {
            parseStmt(token);
            token = getToken();
        }
    }

    // parse <stmt>
    private void parseStmt(String token)
    {
        int val;

        if (token.equals("print"))        // ::= print <expr>
        {
            token = getToken();
            val = parseExpr(token);

            // this is my code to execute
            System.out.println(val);
        }
        else if (token.equals("input"))   // ::= input <var>
        {
            token = getToken();
            val = parseVar(token);

            // this is my code to execute
            System.out.print("? ");
            val = scan.nextInt();
            storeVar(token, val);
        }
        else if (token.equals("if"))      // ::= if <cond> <stmt>
        {
        }
        else if (isVar(token))            // ::= <var> = <expr>
        {
        }
        else
        {
            reportError(token);
        }
    }


    // parse <expr>
    private int parseExpr(String token)
    {
        int val;
        String op;

        val = parseVal(token);
        op = getToken();

        switch(op.charAt(0))
        {
            case '+':                         // ::= <val> + <val>
                val += parseVal(getToken());
                break;
            case '-':                         // ::= <val> - <val>
                val -= parseVal(getToken());
                break;
            case '*':                         // ::= <val> * <val>
                val *= parseVal(getToken());
                break;
            case '/':                         // ::= <val> / <val>
                val /= parseVal(getToken());
                break;
            default:                          // ::= <val>
                line = op + line;
                break;
        }

        return val;
    }


    // parse <val>
    private int parseVal(String token)
    {
        if (isNumber(token))                  // ::= <num>
            return Integer.parseInt(token);
        if (isVar(token))                     // ::= <var>
            return parseVar(token);

        reportError(token);

        return -1; // this will never happen
    }


    // is it a <num>
    private boolean isNumber(String token)
    {
        for (int i=0; i<token.length(); i++)
            if (!Character.isDigit(token.charAt(i)))
                return false;

        return true;
    }


    // check to see if <var>
    private boolean isVar(String token)
    {
        return (token.length() == 1 && isAlpha(token.charAt(0)));
    }


    // is it a to z?
    private boolean isAlpha(char ch)
    {
        return ((int) ch) >= 97 && ((int) ch) <= 122;
    }


    // parseVar <var>
    private int parseVar(String token)
    {
        if (!isVar(token))
            reportError(token);

        return vars[((int)token.charAt(0)) - 97];
    }


    // stores value in variable
    private void storeVar(String token, int val)
    {
        vars[((int)token.charAt(0)) - 97] = val;
    }


    // We have a SYNTAX error
    private void reportError(String token)
    {
        System.out.println("ERROR: " + token +" " + line);
        for (int i=0; i<token.length()+7; i++)
            System.out.print(" ");
        System.out.println("^");
        System.exit(-1);
    }

    
    // checks for blank space characters
    private boolean isBlank(char ch)
    {
        switch (ch)
        {
            case ' ':
            case '\t':
            case '\r':
                return true;
            default:
                return false;
        }
    }

    // checs for delimeters
    private boolean isDelim(char ch)
    {
        if(isBlank(ch))
            return true;

        switch (ch)
        {
            case '+': case '-': case '*': case '/':
            case '=': case '>': case '<':
            case '.':
                return true;
            default:
                return false;
        }
    }

    // skip over leading blank space
    private String skipLeadBlanks(String buffer)
    {
        int i;
        for (i=0; i<buffer.length(); i++)
            if (!isBlank(buffer.charAt(i)))
                break;
        return buffer.substring(i);
    }

    private String getToken()
    {
        int i; // increment variable
        String token = "";

        line = skipLeadBlanks(line);

        // grab a non-blank line
        while (line.length() == 0)
        {
            line = scan.nextLine();
            line = skipLeadBlanks(line);
        }

        // grab our actual token
        for (i=0; i<line.length(); i++)
            if (isDelim(line.charAt(i)))
            {
                if (i == 0)
                    i++;
                token = line.substring(0,i);
                line = line.substring(i);
                return token;
            }

        // entire line is token
        token = line;
        line = "";
        return token;
    }
}
