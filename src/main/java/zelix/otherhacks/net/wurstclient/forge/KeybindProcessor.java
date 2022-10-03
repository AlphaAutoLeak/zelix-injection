package zelix.otherhacks.net.wurstclient.forge;

import net.minecraftforge.fml.common.gameevent.*;
import org.lwjgl.input.*;
import net.minecraftforge.fml.common.eventhandler.*;

public final class KeybindProcessor
{
    private final HackList hax;
    private final KeybindList keybinds;
    private final CommandProcessor cmdProcessor;
    
    public KeybindProcessor(final HackList hax, final KeybindList keybinds, final CommandProcessor cmdProcessor) {
        this.hax = hax;
        this.keybinds = keybinds;
        this.cmdProcessor = cmdProcessor;
    }
    
    @SubscribeEvent
    public void onKeyInput(final InputEvent.KeyInputEvent event) {
        final int keyCode = Keyboard.getEventKey();
        if (keyCode == 0 || !Keyboard.getEventKeyState()) {
            return;
        }
        String commands = this.keybinds.getCommands(Keyboard.getKeyName(keyCode));
        if (commands == null) {
            return;
        }
        commands = commands.replace(";", "¡ì").replace("¡ì¡ì", ";");
        for (String command : commands.split("¡ì")) {
            command = command.trim();
            if (command.startsWith(".")) {
                this.cmdProcessor.runCommand(command.substring(1));
            }
            else if (command.contains(" ")) {
                this.cmdProcessor.runCommand(command);
            }
            else {
                final Hack hack = this.hax.get(command);
                if (hack != null) {
                    hack.setEnabled(!hack.isEnabled());
                }
                else {
                    this.cmdProcessor.runCommand(command);
                }
            }
        }
    }
}
