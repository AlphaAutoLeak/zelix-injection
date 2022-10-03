package zelix.command;

import zelix.utils.hooks.visual.*;
import zelix.utils.*;
import java.lang.reflect.*;
import java.util.*;

public class DumpClasses extends Command
{
    public DumpClasses() {
        super("dumpclasses");
    }
    
    @Override
    public void runCommand(final String s, final String[] args) {
        try {
            final ArrayList<String> list = new ArrayList<String>();
            final Field f = ClassLoader.class.getDeclaredField("classes");
            f.setAccessible(true);
            final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            final Vector<Class> classes = (Vector<Class>)f.get(classLoader);
            for (final Class<?> clazz : classes) {
                final String className = clazz.getName();
                if (args.length > 0) {
                    if (!className.contains(args[0])) {
                        continue;
                    }
                    list.add("\n" + className);
                }
                else {
                    list.add("\n" + className);
                }
            }
            if (list.isEmpty()) {
                ChatUtils.error("List is empty.");
            }
            else {
                Utils.copy(list.toString());
                ChatUtils.message("List copied to clipboard.");
            }
        }
        catch (Exception e) {
            ChatUtils.error("Usage: " + this.getSyntax());
        }
    }
    
    @Override
    public String getDescription() {
        return "Get classes from ClassLoader by regex.";
    }
    
    @Override
    public String getSyntax() {
        return "dumpclasses <regex>";
    }
}
