import jensostertag.pushserver.event.Event;
import jensostertag.pushserver.event.EventHandler;
import jensostertag.pushserver.event.EventInitiator;
import jensostertag.pushserver.event.Listener;
import org.testng.Assert;
import org.testng.annotations.Test;

public class EventInitiatorTest {
    @Test
    public void testRegisterListener() {
        Assert.assertEquals(0, EventInitiator.registerListener(new FailingRegister()));
    }
}

class FailingRegister implements Listener {
    public void onEvent(Event event) {
        // This method is not registered as an event handler because the @EventHandler annotation is missing
    }

    @EventHandler
    public void onEvent(Event event, Object secondParameter) {
        // This method is not registered as an event handler because it has more than one parameter
    }
}
