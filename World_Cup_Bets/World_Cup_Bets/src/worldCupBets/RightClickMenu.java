package worldCupBets;

import java.awt.Component;
import java.awt.Frame;
import java.awt.PopupMenu;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class RightClickMenu {
    private final Frame frame;
    private final PopupMenu popupMenu;
    private final MouseAdapter listener;
    
    public RightClickMenu(MySystem system, GUI gui) {
        this.frame = system.getFrame();

        popupMenu = new PopupMenu();
        popupMenu.add(gui.createIdanItem());

        frame.add(popupMenu);
        listener = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) { showIfPopup(e); }

            @Override
            public void mouseReleased(MouseEvent e) { showIfPopup(e); }

            private void showIfPopup(MouseEvent e) {
                if (e.isPopupTrigger() || e.getButton() == MouseEvent.BUTTON3) {
                    popupMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        };
    }

    public void enableOn(Component c) {
        if (c == null) return;
        c.addMouseListener(listener);
    }

    public void disableOn(Component c) {
        if (c == null) return;
        c.removeMouseListener(listener);
    }

    public void enable() {
        frame.add(popupMenu);
    }
}
