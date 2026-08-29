package com.example.jtcp.protocol;

import java.util.List;

public class ProtocolValidator {
    public void checkVersion(String version) throws ProtocolException {
        if(!version.equals(Protocol.getVersion())) {
            throw new ProtocolException(ProtocolMessage.INVALID_VERSION);
        }
    }
    public Command checkCommand(String command) throws ProtocolException{
        try{
            return Command.valueOf(command);
        }
        catch (IllegalArgumentException e){
            throw new ProtocolException(ProtocolMessage.INVALID_COMMAND);
        }
    }
    public void checkArguments(Command command,List<String> arguments) throws ProtocolException{
        if(arguments.size()!=command.getArgumentCount()){
            throw new ProtocolException(String.format(ProtocolMessage.ARGUMENT_COUNT_MISMATCH,command,command.getArgumentCount(),arguments.size()));
        }
    }

}
