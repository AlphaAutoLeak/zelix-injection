package zelix.gui.Notification;

import zelix.utils.hooks.visual.font.render.*;
import zelix.gui.Notification.Utils.*;
import zelix.*;
import zelix.utils.*;
import net.minecraft.client.gui.*;
import java.awt.*;
import zelix.utils.hooks.visual.*;
import zelix.gui.clickguis.huangbai.*;
import java.util.*;

public class Notification
{
    public String text;
    TTFFontRenderer sigmaFont;
    public double width;
    public double height;
    public float x;
    Type type;
    public float y;
    public float position;
    public boolean in;
    public AnimationUtils animationUtils;
    AnimationUtils yAnimationUtils;
    
    public Notification(final String text, final Type type) {
        this.sigmaFont = Core.fontManager.getFont("SFB 8");
        this.width = 30.0;
        this.height = 20.0;
        this.in = true;
        this.animationUtils = new AnimationUtils();
        this.yAnimationUtils = new AnimationUtils();
        this.text = text;
        this.type = type;
        this.width = this.sigmaFont.getWidth(text) + 25.0f;
        this.x = (float)this.width;
    }
    
    public void onRender() {
        int i = 0;
        for (final Notification notification : Core.notificationManager.notifications) {
            if (notification == this) {
                break;
            }
            ++i;
        }
        this.y = this.yAnimationUtils.animate((float)(i * (this.height + 5.0)), this.y, 0.1f);
        final ScaledResolution sr = new ScaledResolution(Wrapper.INSTANCE.mc());
        RenderUtils.drawRect(sr.getScaledWidth() + this.x - this.width, sr.getScaledHeight() - 55 - this.y - this.height, sr.getScaledWidth() + this.x, sr.getScaledHeight() - 55 - this.y, Colors.getColor(Color.black));
        ClickGuiRender.drawFilledCircle(sr.getScaledWidth() + this.x - this.width, sr.getScaledHeight() - 45 - this.y - this.height, 10.0, Colors.getColor(Color.black), 5);
        this.sigmaFont.drawStringWithShadow(this.text, (float)(sr.getScaledWidth() + this.x - this.width + 10.0), sr.getScaledHeight() - 50.0f - this.y - 18.0f, new Color(204, 204, 204, 232).getRGB());
    }
    
    public enum Type
    {
        Success, 
        Error, 
        Info;
    }
}
