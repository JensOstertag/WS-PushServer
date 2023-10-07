import jensostertag.pushserver.data.DockerVariables;
import jensostertag.pushserver.event.EventInitiator;
import jensostertag.pushserver.eventhandler.*;
import jensostertag.pushserver.util.Logger;

public class TestSetup {
    public static void testSetup() {
        // Enable debugging
        Logger.setLogLevel(2);
        new Logger("TestSetup").debug("Using docker paths: " + DockerVariables.isDocker());
        new Logger("TestSetup").debug("Using schema base directory: " + DockerVariables.schemaBaseDirectory());

        // Event handlers
        EventInitiator.registerListener(new ConnectHandler());
        EventInitiator.registerListener(new DisconnectHandler());
        EventInitiator.registerListener(new MessageHandler());
        EventInitiator.registerListener(new ClientSubscribeHandler());
        EventInitiator.registerListener(new ClientUnsubscribeHandler());
        EventInitiator.registerListener(new PushMessageHandler());
    }
}
