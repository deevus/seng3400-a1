import java.net.*;
import java.io.*;
import java.util.regex.*;

public class CodeProtocol {
    private CodeProtocolState state = CodeProtocolState.Waiting;
    private Pattern acPattern = Pattern.compile("^[a-zA-Z0-9]$");

    public String processInput(String input) {
        //for changing of state
        switch (input) {
            case "ASCII":
                state = CodeProtocolState.AC;
                return "ASCII: OK";
            case "AC":
                state = CodeProtocolState.AC;
                return "CHANGE: OK\nASCII: OK";
            case "CA":
                state = CodeProtocolState.CA;
                return "CHANGE: OK\nASCII: OK";
            case "BYE":
                state = CodeProtocolState.Waiting;
                return "BYE: OK";
            case "END":
                state = CodeProtocolState.End;
                return "END: OK";
        }

        //processing current state
        Matcher m;
        switch (state) {
            case AC:
                m = acPattern.matcher(input);
                if (m.matches()) {
                    return "" + (int)input.charAt(0) + "\nASCII: OK";
                }
                return "ERR\nASCII: OK";
            case CA:
                //get char code from input
                try {
                    byte code = Byte.parseByte(input);
                    String str = new String(new byte[] { code });
                    m = acPattern.matcher(str);
                    if (m.matches()) {
                        return str + "\nASCII: OK";
                    }
                }
                catch (NumberFormatException e) { }
                return "ERR\nASCII: OK";
        }

        //shouldn't get here
        return null;
    }

    public CodeProtocolState getState() {
        return state;
    }
}
