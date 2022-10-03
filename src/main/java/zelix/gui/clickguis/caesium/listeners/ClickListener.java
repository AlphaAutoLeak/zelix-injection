package zelix.gui.clickguis.caesium.listeners;

import zelix.gui.clickguis.caesium.components.*;
import java.awt.event.*;
import zelix.*;
import zelix.managers.*;
import zelix.hack.*;

public class ClickListener implements ActionListener
{
    private GuiButton button;
    
    public ClickListener(final GuiButton button) {
        this.button = button;
    }
    
    @Override
    public void actionPerformed(final ActionEvent event) {
        final HackManager hackManager = Core.hackManager;
        final Hack m = HackManager.getHack(this.button.getText());
        m.toggle();
    }
}
