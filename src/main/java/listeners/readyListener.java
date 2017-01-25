package listeners;

import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class readyListener extends ListenerAdapter{

    @Override
    public void onReady(ReadyEvent event) {
        System.out.println("Ready to go.");
    }
}
