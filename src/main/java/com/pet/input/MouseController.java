//package com.pet.input;
//
//import java.awt.event.MouseAdapter;
//
//import org.w3c.dom.events.MouseEvent;
//
//public class MouseController extends MouseAdapter {
//    // This class will handle mouse events for the pet,
//    // such as
//    // For now, it's a placeholder,
//    // but it can be expanded in the future to include more functionality.
//
//    // to prevent physics update during interaction
//
//    @Override
//    public void mousePressed(MouseEvent e) {
//        isPhysicsEnabled = false;
//        dragOffset = e.getPoint();
//
//    }
//
//    @Override
//    public void mouseReleased(MouseEvent e) {
//        // TASK :== add timmer to enable physics after some time
//        isPhysicsEnabled = true;
//    }
//
//    // handle mouse click
//    @Override
//    public void mouseClicked(MouseEvent e) {
//        // if left button clicked extit application
//        if (e.getButton() == MouseEvent.BUTTON3) {
//            System.exit(0);
//        } else if (e.getButton() == MouseEvent.BUTTON1) {
//            handleClickInteraction(animationManager);
//            isPhysicsEnabled = true;
//        }
//    }
//
//}
