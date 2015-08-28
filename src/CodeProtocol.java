/**
 * SENG3400 Assignment 1
 * Simon Hartcher
 * C3185790
 */

import java.net.*;
import java.net.*;
import java.io.*;
import java.util.regex.*;

/**
 * Contains the CodeConverter server protocol implementation
 */
public class CodeProtocol {
    private CodeProtocolState state = CodeProtocolState.Waiting;

    /**
     * Valid input and output pattern
     */
    private Pattern acPattern = Pattern.compile("^[a-zA-Z0-9]$");

    /**
     * Process the input string for the protocol
     * @param  input The string to process
     * @return       The protocol response
     */
    public String processInput(String input) {
        //for changing of state
        switch (input) {
            case "ASCII":
                //transition to the ascii code state
                state = CodeProtocolState.AC;
                return "ASCII: OK";
            case "AC":
                //transition to the ascii code state
                state = CodeProtocolState.AC;
                return "CHANGE: OK\nASCII: OK";
            case "CA":
                //transition to the char code state
                state = CodeProtocolState.CA;
                return "CHANGE: OK\nASCII: OK";
            case "BYE":
                //transition to the waiting state
                state = CodeProtocolState.Waiting;
                return "BYE: OK";
            case "END":
                //transition to the end state
                state = CodeProtocolState.End;
                return "END: OK";
        }

        //processing current state
        Matcher m;
        switch (state) {
            case AC:
                //classify input string
                m = acPattern.matcher(input);

                //if the input is valid
                if (m.matches()) {
                    //return the character code
                    return "" + (int)input.charAt(0) + "\nASCII: OK";
                }

                //unsupported character
                return "ERR\nASCII: OK";
            case CA:
                //get char code from input
                try {
                    //get byte value of string
                    byte code = Byte.parseByte(input);

                    //create new string containing character from byte
                    String str = new String(new byte[] { code });

                    //check if the str is valid
                    m = acPattern.matcher(str);
                    if (m.matches()) {
                        //return valid string
                        return str + "\nASCII: OK";
                    }
                }
                catch (NumberFormatException e) { }

                //unsupported character code
                return "ERR\nASCII: OK";
        }

        //shouldn't get here
        return null;
    }

    /**
     * Get the current state of the protocol instance
     * @return The state enum value
     */
    public CodeProtocolState getState() {
        return state;
    }
}
