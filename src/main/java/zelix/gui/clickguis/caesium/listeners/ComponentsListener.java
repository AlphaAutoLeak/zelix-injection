package zelix.gui.clickguis.caesium.listeners;

import zelix.*;
import zelix.gui.clickguis.caesium.components.listeners.ComponentListener;
import zelix.gui.clickguis.caesium.components.listeners.KeyListener;
import zelix.managers.*;
import java.awt.event.*;
import zelix.value.*;
import zelix.gui.clickguis.caesium.components.*;
import zelix.gui.clickguis.caesium.components.listeners.*;
import zelix.hack.*;
import java.util.*;

public class ComponentsListener extends ComponentListener
{
    private GuiButton button;
    
    public ComponentsListener(final GuiButton button) {
        this.button = button;
    }
    
    @Override
    public void addComponents() {
        this.add(new GuiLabel("Settings >"));
        final HackManager hackManager = Core.hackManager;
        final Hack m = HackManager.getHack(this.button.getText());
        for (final Value set : m.getValues()) {
            if (set instanceof BooleanValue) {
                final GuiToggleButton toggleButton = new GuiToggleButton((BooleanValue)set);
                toggleButton.setToggled(((BooleanValue)set).getValue());
                toggleButton.addClickListener(new ActionListener() {
                    @Override
                    public void actionPerformed(final ActionEvent e) {
                        set.setValue(toggleButton.isToggled());
                        new Thread(() -> {}).start();
                    }
                });
                this.add(toggleButton);
            }
            if (set instanceof NumberValue) {
                final GuiSlider slider = new GuiSlider((NumberValue)set, ((NumberValue)set).getMin(), ((NumberValue)set).getMax(), ((NumberValue)set).getValue(), 2);
                slider.addValueListener(new ValueListener() {
                    @Override
                    public void valueUpdated(final double value) {
                        set.setValue(value);
                    }
                    
                    @Override
                    public void valueChanged(final double value) {
                        set.setValue(value);
                        new Thread(() -> {}).start();
                    }
                });
                this.add(slider);
            }
            if (set instanceof ModeValue) {
                final GuiComboBox comboBox = new GuiComboBox((ModeValue)set);
                comboBox.addComboListener(new ComboListener() {
                    @Override
                    public void comboChanged(final String combo) {
                        new Thread(() -> {}).start();
                    }
                });
                this.add(comboBox);
            }
        }
        final GuiGetKey ggk = new GuiGetKey("KeyBind", m.getKey());
        ggk.addKeyListener(new KeyListener() {
            @Override
            public void keyChanged(final int key) {
                m.setKey(key);
                new Thread(() -> {}).start();
            }
        });
        this.add(ggk);
    }
}
