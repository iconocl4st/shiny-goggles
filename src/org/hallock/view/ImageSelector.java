package org.hallock.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class ImageSelector {
    private int selectorSize;

    private int x1;
    private int y1;
    private int y2;
    private int x2;

    private int startX = -1;
    private int startY = -1;

    private JFrame upperLeft;
    private JFrame upperRight;
    private JFrame lowerLeft;
    private JFrame lowerRight;

    private ImageSelector(int selectorSize, JFrame ul, JFrame ur, JFrame ll, JFrame lr) {
        this.selectorSize = selectorSize;

        this.upperLeft = ul;
        this.upperRight = ur;
        this.lowerLeft = ll;
        this.lowerRight = lr;
    }

    public void show() {
        upperLeft.setVisible(true);
        upperRight.setVisible(true);
        lowerLeft.setVisible(true);
        lowerRight.setVisible(true);
    }

    public Rectangle select() {
        Rectangle ret = getRectangle();
        upperLeft.setVisible(false);
        upperRight.setVisible(false);
        lowerLeft.setVisible(false);
        lowerRight.setVisible(false);
        return ret;
    }

    public void setRectangle(Rectangle rec) {
        x1 = rec.x - selectorSize;
        y1 = rec.y - selectorSize;
        x2 = rec.x + rec.width;
        y2 = rec.y + rec.height;

        reposition();
    }

    public Rectangle getRectangle() {
        return new Rectangle(
                x1 + selectorSize,
                y1 + selectorSize,
                x2 - x1 - selectorSize,
                y2 - y1 - selectorSize
        );
    }

    private void requestUpperLeft(int i, int i1) {
        x1 = Math.min(i, x2 - selectorSize);
        y1 = Math.min(i1, y2 - selectorSize);
        reposition();
    }

    private void requestUpperRight(int i, int i1) {
        x2 = Math.max(i, x1 + selectorSize);
        y1 = Math.min(i1, y2 - selectorSize);
        reposition();
    }
    private void requestLowerLeft(int i, int i1) {
        x1 = Math.min(i, x2 - selectorSize);
        y2 = Math.max(i1, y1 + selectorSize);
        reposition();
    }
    private void requestLowerRight(int i, int i1) {
        x2 = Math.max(i, x1 + selectorSize);
        y2 = Math.max(i1, y1 + selectorSize);
        reposition();
    }
    private void reposition() {
        upperLeft.setLocation(x1, y1);
        upperRight.setLocation(x2, y1);
        lowerLeft.setLocation(x1, y2);
        lowerRight.setLocation(x2, y2);
    }










    private static JFrame createJFrame(int selectorSize) {
        final JFrame frame = new JFrame();
        frame.setSize(selectorSize, selectorSize);
        frame.setUndecorated(true);
        frame.getContentPane().setBackground(Color.red);
        frame.setAlwaysOnTop(true);
        return frame;
    }

    public static ImageSelector createImageSelector(
            int selectorSize,
            Rectangle initialRectangle
    ) {
        final ImageSelector selector = new ImageSelector(
                selectorSize,
                createJFrame(selectorSize),
                createJFrame(selectorSize),
                createJFrame(selectorSize),
                createJFrame(selectorSize)
        );
        selector.setRectangle(initialRectangle);

        final MouseListener initiateListener = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                selector.startX = mouseEvent.getX();
                selector.startY = mouseEvent.getY();
            }
        };
        selector.upperLeft.addMouseListener(initiateListener);
        selector.upperRight.addMouseListener(initiateListener);
        selector.lowerLeft.addMouseListener(initiateListener);
        selector.lowerRight.addMouseListener(initiateListener);

        selector.upperLeft.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent mouseEvent) {
                selector.requestUpperLeft(
                        mouseEvent.getXOnScreen() - selector.startX,
                        mouseEvent.getYOnScreen() - selector.startY
                );
            }
        });
        selector.upperRight.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent mouseEvent) {
                selector.requestUpperRight(
                        mouseEvent.getXOnScreen() - selector.startX,
                        mouseEvent.getYOnScreen() - selector.startY
                );
            }
        });
        selector.lowerLeft.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent mouseEvent) {
                selector.requestLowerLeft(
                        mouseEvent.getXOnScreen() - selector.startX,
                        mouseEvent.getYOnScreen() - selector.startY
                );
            }
        });
        selector.lowerRight.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent mouseEvent) {
                selector.requestLowerRight(
                        mouseEvent.getXOnScreen() - selector.startX,
                        mouseEvent.getYOnScreen() - selector.startY
                );
            }
        });
        return selector;
    }
    public static ImageSelector createImageSelector() {
        // 172,y=188,width=62,height=89
        return createImageSelector(15, new Rectangle(
                172, 188, 62, 89
        ));
    }
}
