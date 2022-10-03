package zelix.managers;

import zelix.gui.clickguis.gishcode.*;
import zelix.utils.hooks.visual.*;
import zelix.hack.*;
import zelix.gui.clickguis.gishcode.base.*;
import java.util.*;
import zelix.gui.clickguis.gishcode.listener.*;
import zelix.value.*;
import zelix.gui.clickguis.gishcode.elements.*;

public class GuiManager extends ClickGui
{
    public void Init() {
        final int right = GLUtils.getScreenWidth();
        int framePosX = 20;
        int framePosY = 20;
        for (final HackCategory category : HackCategory.values()) {
            final int frameHeight = 180;
            final int frameWidth = 100;
            int hacksCount = 0;
            final String name = Character.toString(category.toString().toLowerCase().charAt(0)).toUpperCase() + category.toString().toLowerCase().substring(1);
            final Frame frame = new Frame(framePosX, framePosY, frameWidth, frameHeight, name);
            for (final Hack mod : HackManager.getHacks()) {
                if (mod.getCategory() == category) {
                    final ExpandingButton expandingButton = new ExpandingButton(0, 0, frameWidth, 14, frame, mod.getName(), mod) {
                        @Override
                        public void onUpdate() {
                            this.setEnabled(this.hack.isToggled());
                        }
                    };
                    expandingButton.addListner(new ComponentClickListener() {
                        @Override
                        public void onComponenetClick(final Component component, final int button) {
                            mod.toggle();
                        }
                    });
                    expandingButton.setEnabled(mod.isToggled());
                    if (!mod.getValues().isEmpty()) {
                        for (final Value value : mod.getValues()) {
                            if (value instanceof BooleanValue) {
                                final BooleanValue booleanValue = (BooleanValue)value;
                                final CheckButton button = new CheckButton(0, 0, expandingButton.getDimension().width, 14, expandingButton, booleanValue.getRenderName(), booleanValue.getValue(), null);
                                button.addListeners(new CheckButtonClickListener() {
                                    @Override
                                    public void onCheckButtonClick(final CheckButton checkButton) {
                                        for (final Value value1 : mod.getValues()) {
                                            if (value1.getRenderName().equals(booleanValue.getRenderName())) {
                                                value1.setValue(checkButton.isEnabled());
                                            }
                                        }
                                    }
                                });
                                expandingButton.addComponent(button);
                            }
                            else if (value instanceof NumberValue) {
                                final NumberValue doubleValue = (NumberValue)value;
                                final Slider slider = new Slider(doubleValue.getMin(), doubleValue.getMax(), doubleValue.getValue(), expandingButton, doubleValue.getRenderName());
                                slider.addListener(new SliderChangeListener() {
                                    @Override
                                    public void onSliderChange(final Slider slider) {
                                        for (final Value value1 : mod.getValues()) {
                                            if (value1.getRenderName().equals(value.getRenderName())) {
                                                value1.setValue(slider.getValue());
                                            }
                                        }
                                    }
                                });
                                expandingButton.addComponent(slider);
                            }
                            else {
                                if (!(value instanceof ModeValue)) {
                                    continue;
                                }
                                final Dropdown dropdown = new Dropdown(0, 0, frameWidth, 14, frame, value.getRenderName());
                                final ModeValue modeValue = (ModeValue)value;
                                for (final Mode mode : modeValue.getModes()) {
                                    final CheckButton button2 = new CheckButton(0, 0, expandingButton.getDimension().width, 14, expandingButton, mode.getName(), mode.isToggled(), modeValue);
                                    button2.addListeners(new CheckButtonClickListener() {
                                        @Override
                                        public void onCheckButtonClick(final CheckButton checkButton) {
                                            for (final Mode mode1 : modeValue.getModes()) {
                                                if (mode1.getName().equals(mode.getName())) {
                                                    mode1.setToggled(checkButton.isEnabled());
                                                }
                                            }
                                        }
                                    });
                                    dropdown.addComponent(button2);
                                }
                                expandingButton.addComponent(dropdown);
                            }
                        }
                    }
                    final KeybindMods keybind = new KeybindMods(0, 0, 8, 14, expandingButton, mod);
                    expandingButton.addComponent(keybind);
                    frame.addComponent(expandingButton);
                    ++hacksCount;
                }
            }
            if (framePosX + frameWidth + 10 < right) {
                framePosX += frameWidth + 10;
            }
            else {
                framePosX = 20;
                framePosY += 60;
            }
            frame.setMaximizible(true);
            frame.setPinnable(true);
            this.addFrame(frame);
        }
        if (!FileManager.CLICKGUI.exists()) {
            FileManager.saveClickGui();
        }
        else {
            FileManager.loadClickGui();
        }
    }
}
