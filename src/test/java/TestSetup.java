import jensostertag.pushserver.event.EventInitiator;
import jensostertag.pushserver.eventhandler.*;

public class TestSetup {
    public static void testSetup() {
        // Event handlers
        EventInitiator.registerListener(new ConnectHandler());
        EventInitiator.registerListener(new DisconnectHandler());
        EventInitiator.registerListener(new MessageHandler());
        EventInitiator.registerListener(new ClientSubscribeHandler());
        EventInitiator.registerListener(new ClientUnsubscribeHandler());
        EventInitiator.registerListener(new PushMessageHandler());
    }
}
