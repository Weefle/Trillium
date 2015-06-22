package net.gettrillium.trillium.events;

import net.gettrillium.trillium.api.SignType;
import net.gettrillium.trillium.api.events.SignInteractEvent;
import net.gettrillium.trillium.api.messageutils.Message;
import net.gettrillium.trillium.api.messageutils.Mood;
import org.bukkit.event.EventHandler;

public class SignInteract {

    @EventHandler
    public void signInteract(SignInteractEvent event) {
        if (event.getSignType() == SignType.FEED) {
            if (event.getSign().getLine(1) != null) {
                event.getPlayer().setFoodLevel(20);
                new Message(Mood.GOOD, "Feed", "Your hunger has been saturated.").to(event.getPlayer());
            } else {
                if (event.getPlayer().hasPermission(event.getSign().getLine(1))) {
                    event.getPlayer().setFoodLevel(20);
                    new Message(Mood.GOOD, "Feed", "Your hunger has been saturated.").to(event.getPlayer());
                }
            }
        }

        if (event.getSignType() == SignType.HEAL) {
            if (event.getSign().getLine(1) != null) {
                event.getPlayer().setHealth(20.0);
                new Message(Mood.GOOD, "Heal", "Your health has been restored.").to(event.getPlayer());
            } else {
                if (event.getPlayer().hasPermission(event.getSign().getLine(1))) {
                    event.getPlayer().setHealth(20.0);
                    new Message(Mood.GOOD, "Heal", "Your health has been restored.").to(event.getPlayer());
                }
            }
        }
    }
}
