package zelix.otherhacks.net.wurstclient.forge.clickgui;

import zelix.otherhacks.net.wurstclient.forge.compatibility.*;
import java.util.*;

public final class Window
{
    private String title;
    private int x;
    private int y;
    private int width;
    private int height;
    private boolean valid;
    private final ArrayList<Component> children;
    private boolean dragging;
    private int dragOffsetX;
    private int dragOffsetY;
    private boolean minimized;
    private boolean minimizable;
    private boolean pinned;
    private boolean pinnable;
    private boolean closable;
    private boolean closing;
    private boolean invisible;
    private int innerHeight;
    private int maxHeight;
    private int scrollOffset;
    private boolean scrollingEnabled;
    private boolean draggingScrollbar;
    private int scrollbarDragOffsetY;
    
    public Window(final String title) {
        this.children = new ArrayList<Component>();
        this.minimizable = true;
        this.pinnable = true;
        this.title = title;
    }
    
    public String getTitle() {
        return this.title;
    }
    
    public void setTitle(final String title) {
        this.title = title;
    }
    
    public int getX() {
        return this.x;
    }
    
    public void setX(final int x) {
        this.x = x;
    }
    
    public int getY() {
        return this.y;
    }
    
    public void setY(final int y) {
        this.y = y;
    }
    
    public int getWidth() {
        return this.width;
    }
    
    public void setWidth(final int width) {
        if (this.width != width) {
            this.invalidate();
        }
        this.width = width;
    }
    
    public int getHeight() {
        return this.height;
    }
    
    public void setHeight(final int height) {
        if (this.height != height) {
            this.invalidate();
        }
        this.height = height;
    }
    
    public void pack() {
        int maxChildWidth = 0;
        for (final Component c : this.children) {
            if (c.getWidth() > maxChildWidth) {
                maxChildWidth = c.getDefaultWidth();
            }
        }
        maxChildWidth += 4;
        int titleBarWidth = WMinecraft.getFontRenderer().getStringWidth(this.title) + 4;
        if (this.minimizable) {
            titleBarWidth += 11;
        }
        if (this.pinnable) {
            titleBarWidth += 11;
        }
        if (this.closable) {
            titleBarWidth += 11;
        }
        int childrenHeight = 13;
        for (final Component c2 : this.children) {
            childrenHeight += c2.getHeight() + 2;
        }
        childrenHeight += 2;
        if (childrenHeight > this.maxHeight + 13 && this.maxHeight > 0) {
            this.setWidth(Math.max(maxChildWidth + 3, titleBarWidth));
            this.setHeight(this.maxHeight + 13);
        }
        else {
            this.setWidth(Math.max(maxChildWidth, titleBarWidth));
            this.setHeight(childrenHeight);
        }
        this.validate();
    }
    
    public void validate() {
        if (this.valid) {
            return;
        }
        int offsetY = 2;
        int cWidth = this.width - 4;
        for (final Component c : this.children) {
            c.setX(2);
            c.setY(offsetY);
            c.setWidth(cWidth);
            offsetY += c.getHeight() + 2;
        }
        this.innerHeight = offsetY;
        if (this.maxHeight == 0) {
            this.setHeight(this.innerHeight + 13);
        }
        else if (this.height > this.maxHeight + 13) {
            this.setHeight(this.maxHeight + 13);
        }
        else if (this.height < this.maxHeight + 13) {
            this.setHeight(Math.min(this.maxHeight + 13, this.innerHeight + 13));
        }
        this.scrollingEnabled = (this.innerHeight > this.height - 13);
        if (this.scrollingEnabled) {
            cWidth -= 3;
        }
        this.scrollOffset = Math.min(this.scrollOffset, 0);
        this.scrollOffset = Math.max(this.scrollOffset, -this.innerHeight + this.height - 13);
        for (final Component c : this.children) {
            c.setWidth(cWidth);
        }
        this.valid = true;
    }
    
    public void invalidate() {
        this.valid = false;
    }
    
    public int countChildren() {
        return this.children.size();
    }
    
    public Component getChild(final int index) {
        return this.children.get(index);
    }
    
    public void add(final Component component) {
        this.children.add(component);
        component.setParent(this);
        this.invalidate();
    }
    
    public void remove(final int index) {
        this.children.get(index).setParent(null);
        this.children.remove(index);
        this.invalidate();
    }
    
    public void remove(final Component component) {
        this.children.remove(component);
        component.setParent(null);
        this.invalidate();
    }
    
    public boolean isDragging() {
        return this.dragging;
    }
    
    public void startDragging(final int mouseX, final int mouseY) {
        this.dragging = true;
        this.dragOffsetX = this.x - mouseX;
        this.dragOffsetY = this.y - mouseY;
    }
    
    public void dragTo(final int mouseX, final int mouseY) {
        this.x = mouseX + this.dragOffsetX;
        this.y = mouseY + this.dragOffsetY;
    }
    
    public void stopDragging() {
        this.dragging = false;
        this.dragOffsetX = 0;
        this.dragOffsetY = 0;
    }
    
    public boolean isMinimized() {
        return this.minimized;
    }
    
    public void setMinimized(final boolean minimized) {
        this.minimized = minimized;
    }
    
    public boolean isMinimizable() {
        return this.minimizable;
    }
    
    public void setMinimizable(final boolean minimizable) {
        this.minimizable = minimizable;
    }
    
    public boolean isPinned() {
        return this.pinned;
    }
    
    public void setPinned(final boolean pinned) {
        this.pinned = pinned;
    }
    
    public boolean isPinnable() {
        return this.pinnable;
    }
    
    public void setPinnable(final boolean pinnable) {
        this.pinnable = pinnable;
    }
    
    public boolean isClosable() {
        return this.closable;
    }
    
    public void setClosable(final boolean closable) {
        this.closable = closable;
    }
    
    public boolean isClosing() {
        return this.closing;
    }
    
    public void close() {
        this.closing = true;
    }
    
    public boolean isInvisible() {
        return this.invisible;
    }
    
    public void setInvisible(final boolean invisible) {
        this.invisible = invisible;
    }
    
    public int getInnerHeight() {
        return this.innerHeight;
    }
    
    public void setMaxHeight(final int maxHeight) {
        if (this.maxHeight != maxHeight) {
            this.invalidate();
        }
        this.maxHeight = maxHeight;
    }
    
    public int getScrollOffset() {
        return this.scrollOffset;
    }
    
    public void setScrollOffset(final int scrollOffset) {
        this.scrollOffset = scrollOffset;
    }
    
    public boolean isScrollingEnabled() {
        return this.scrollingEnabled;
    }
    
    public boolean isDraggingScrollbar() {
        return this.draggingScrollbar;
    }
    
    public void startDraggingScrollbar(final int mouseY) {
        this.draggingScrollbar = true;
        final double outerHeight = this.height - 13;
        final double scrollbarY = outerHeight * (-this.scrollOffset / this.innerHeight) + 1.0;
        this.scrollbarDragOffsetY = (int)(scrollbarY - mouseY);
    }
    
    public void dragScrollbarTo(final int mouseY) {
        final int scrollbarY = mouseY + this.scrollbarDragOffsetY;
        final double outerHeight = this.height - 13;
        this.scrollOffset = (int)((scrollbarY - 1) / outerHeight * this.innerHeight * -1.0);
        this.scrollOffset = Math.min(this.scrollOffset, 0);
        this.scrollOffset = Math.max(this.scrollOffset, -this.innerHeight + this.height - 13);
    }
    
    public void stopDraggingScrollbar() {
        this.draggingScrollbar = false;
        this.scrollbarDragOffsetY = 0;
    }
}
