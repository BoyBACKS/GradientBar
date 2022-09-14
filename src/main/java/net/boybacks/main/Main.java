package net.boybacks.main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

public class Main {

    public static final int WIDE = 1920;
    public static final int HIGH = 50;
    static class GradientPanel extends JPanel {
        private static final float HUE_MIN = 1f/6;
        private static final float HUE_MAX = 6f/6;
        private final Timer timer;
        private float hue = HUE_MIN;
        private Color color1 = Color.WHITE;
        private Color color2 = Color.BLACK;
        private float delta = 0.01f;

        GradientPanel() {
            ActionListener action = new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent evt) {
                    hue += delta;
                    if (hue > HUE_MAX) {
                        hue = HUE_MAX;
                        delta = -delta;
                    }
                    if (hue < HUE_MIN) {
                        hue = HUE_MIN;
                        delta = -delta;
                    }
                    color1 = Color.getHSBColor(hue, 1, 1);
                    color2 = Color.getHSBColor(hue, 3f/4 + delta, 3f/4 + delta);
                    repaint();
                }
            };
            timer = new Timer(100, action);
            timer.start();
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            GradientPaint p = new GradientPaint(
                    0, 0, color1, getWidth(), 0, color2);
            g2d.setPaint(p);
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(WIDE, HIGH);
        }
    }

    public static JFrame frame = new JFrame("Gradient Animation");
    private static void createAndShowUI() {

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setType(Window.Type.UTILITY);
        GradientPanel imagePanel = new GradientPanel();
        MoveListener listener = new MoveListener();
        frame.addMouseListener(listener);
        frame.addMouseMotionListener(listener);
        frame.setUndecorated(true);
        frame.setShape(new RoundRectangle2D.Double(0,0, WIDE, HIGH, 20, 20));
        frame.setAlwaysOnTop(true);
        frame.add(imagePanel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowUI();
                new Main();
            }
        });

        if(!SystemTray.isSupported()){
            System.out.println("System tray is not supported !!! ");
            return ;
        }
        SystemTray systemTray = SystemTray.getSystemTray();

        Image image = Toolkit.getDefaultToolkit().getImage("pepe-nervous.gif");

        PopupMenu trayPopupMenu = new PopupMenu();

        MenuItem action = new MenuItem("Reset");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        action.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setLocation(0, (int) (screenSize.getHeight()/2));
                frame.setAlwaysOnTop(true);
            }
        });
        trayPopupMenu.add(action);

        MenuItem close = new MenuItem("Close");
        close.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        trayPopupMenu.add(close);

        TrayIcon trayIcon = new TrayIcon(image, "GradientBar", trayPopupMenu);
        trayIcon.setImageAutoSize(true);

        try{
            systemTray.add(trayIcon);
        }catch(AWTException awtException){
            awtException.printStackTrace();
        }
    }

    public static class MoveListener implements MouseListener, MouseMotionListener {

        private Point pressedPoint;
        private Rectangle frameBounds;

        @Override
        public void mouseClicked(MouseEvent event) {
        }

        @Override
        public void mousePressed(MouseEvent event) {
            this.frameBounds = frame.getBounds();
            this.pressedPoint = event.getPoint();
        }

        @Override
        public void mouseReleased(MouseEvent event) {
            moveJFrame(event);
        }

        @Override
        public void mouseEntered(MouseEvent event) {
        }

        @Override
        public void mouseExited(MouseEvent event) {
        }

        @Override
        public void mouseDragged(MouseEvent event) {
            moveJFrame(event);
        }

        @Override
        public void mouseMoved(MouseEvent event) {
        }

        private void moveJFrame(MouseEvent event) {
            Point endPoint = event.getPoint();

            //int xDiff = endPoint.x - pressedPoint.x;
            int yDiff = endPoint.y - pressedPoint.y;
            //frameBounds.x += xDiff;
            frameBounds.y += yDiff;
            frame.setBounds(frameBounds);
            if (yDiff == 0) {
                frame.setAlwaysOnTop(false);
            }
            else
                frame.setAlwaysOnTop(true);
        }

    }
}
