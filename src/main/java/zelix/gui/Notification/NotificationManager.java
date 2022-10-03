package zelix.gui.Notification;

import java.util.*;

public class NotificationManager
{
    public ArrayList<Notification> notifications;
    
    public NotificationManager() {
        this.notifications = new ArrayList<Notification>();
    }
    
    public void add(final Notification noti) {
        noti.y = this.notifications.size() * 25;
        this.notifications.add(noti);
    }
    
    public void draw() {
        int i = 0;
        Notification remove = null;
        for (final Notification notification : this.notifications) {
            if (notification.x == 0.0f) {
                notification.in = !notification.in;
            }
            if (Math.abs(notification.x - notification.width) < 0.1 && !notification.in) {
                remove = notification;
            }
            if (notification.in) {
                notification.x = notification.animationUtils.animate(0.0f, notification.x, 0.1f);
            }
            else {
                notification.x = (float)notification.animationUtils.animate(notification.width, notification.x, 0.10000000149011612);
            }
            notification.onRender();
            ++i;
        }
        if (remove != null) {
            this.notifications.remove(remove);
        }
    }
}
