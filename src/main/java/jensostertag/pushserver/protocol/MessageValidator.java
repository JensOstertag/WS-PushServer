package jensostertag.pushserver.protocol;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import jensostertag.pushserver.data.DockerVariables;
import jensostertag.pushserver.exceptions.InvalidMessageException;
import jensostertag.pushserver.util.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class MessageValidator {
    private static final String[][] SCHEMAS = {
        {"ERROR",                      DockerVariables.schemaBaseDirectory() + "error.schema.json"},

        {"CLIENT_SUBSCRIBE_CHANNEL",   DockerVariables.schemaBaseDirectory() + "client/subscribeChannel.schema.json"},
        {"CLIENT_UNSUBSCRIBE_CHANNEL", DockerVariables.schemaBaseDirectory() + "client/unsubscribeChannel.schema.json"},
        {"CLIENT_ACK",                 DockerVariables.schemaBaseDirectory() + "client/ack.schema.json"},
        {"CLIENT_PUSH",                DockerVariables.schemaBaseDirectory() + "client/push.schema.json"},

        {"SERVER_CHANNEL_CREATE",      DockerVariables.schemaBaseDirectory() + "server/createChannel.schema.json"},
        {"SERVER_CHANNEL_DELETE",      DockerVariables.schemaBaseDirectory() + "server/deleteChannel.schema.json"},
        {"SERVER_CHANNEL_PING",        DockerVariables.schemaBaseDirectory() + "server/pingChannel.schema.json"},
        {"SERVER_PUSH_MESSAGE",        DockerVariables.schemaBaseDirectory() + "server/pushMessage.schema.json"},
        {"SERVER_ACK",                 DockerVariables.schemaBaseDirectory() + "server/ack.schema.json"},
        {"SERVER_SYSADMIN",            DockerVariables.schemaBaseDirectory() + "server/sysadmin.schema.json"}
    };

    public static MessageType getMessageType(String jsonString) throws JsonProcessingException, InvalidMessageException {
        for(String[] schema : MessageValidator.SCHEMAS) {
            if(MessageValidator.isValid(jsonString, schema[1])) {
                return MessageType.valueOf(schema[0]);
            }
        }

        throw new InvalidMessageException("Invalid message");
    }

    public static boolean isValid(String jsonString, String schemaPath) throws JsonProcessingException {
        JsonSchemaFactory schemaFactory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7);

        try {
            File schemaFile = new File(schemaPath);
            InputStream schemaInputStream = new FileInputStream(schemaFile);

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(jsonString);

            return schemaFactory.getSchema(schemaInputStream).validate(jsonNode).isEmpty();
        } catch(FileNotFoundException e) {
            new Logger("MessageValidator").error("Could not find schema file \"" + schemaPath + "\"");
            return false;
        }
    }
}
