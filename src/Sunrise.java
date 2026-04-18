import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Sunrise extends JPanel implements ActionListener {

    private Timer timer;
    private double sunY;

    private boolean expandSun = false;
    private boolean oceanScene = false;

    private int sunSize = 80;
    private int mountainOffset = 0;

    private double waveTime = 0;

    private float transitionAlpha = 0f;

    public Sunrise() {
        sunY = 500;
        timer = new Timer(40, this);
        timer.start();
    }


    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        // Ocean scene
        if (oceanScene) {
            drawOcean(g2d);
            return;
        }

        //Sunrise
        drawSunrise(g2d);

        // transition
        if (transitionAlpha > 0) {
            Graphics2D gFade = (Graphics2D) g2d.create();

            gFade.setComposite(AlphaComposite.getInstance(
                    AlphaComposite.SRC_OVER, transitionAlpha));

            drawOcean(gFade);

            gFade.dispose();
        }
    }

    private void drawSunrise(Graphics2D g2d) {
        // Sky
        GradientPaint sky = new GradientPaint(
                0, 0, new Color(10, 10, 50),
                0, getHeight(), new Color(255, 120, 0)
        );
        g2d.setPaint(sky);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        // Sun
        g2d.setColor(new Color(255, 200, 0));
        g2d.fillOval(getWidth()/2 - sunSize/2, (int) sunY, sunSize, sunSize);

        // Mountains
        g2d.setColor(new Color(30, 30, 30));

        int[] x1 = {0, 200, 400};
        int[] y1 = {
                getHeight() + mountainOffset,
                250 + mountainOffset,
                getHeight() + mountainOffset
        };
        g2d.fillPolygon(x1, y1, 3);

        int[] x2 = {200, 450, 700};
        int[] y2 = {
                getHeight() + mountainOffset,
                200 + mountainOffset,
                getHeight() + mountainOffset
        };
        g2d.fillPolygon(x2, y2, 3);
    }

    private void drawOcean(Graphics2D g2d) {

        // Sky
        GradientPaint sky = new GradientPaint(
                0, 0, new Color(135, 206, 250),
                0, getHeight(), new Color(25, 100, 200)
        );
        g2d.setPaint(sky);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        //  Sun glow
        g2d.setColor(new Color(255, 255, 200, 80));
        g2d.fillOval(getWidth() - 140, 40, 120, 120);

        //  Sun
        g2d.setColor(new Color(253, 252, 210));
        g2d.fillOval(getWidth() - 120, 60, 80, 80);

        //  Clouds
        g2d.setColor(new Color(255, 255, 255, 180));
        g2d.fillOval(100, 80, 80, 40);
        g2d.fillOval(130, 60, 80, 50);
        g2d.fillOval(160, 80, 80, 40);

        int baseY = getHeight() - 120;

        //  Back wave
        g2d.setColor(new Color(0, 120, 170));
        for (int x = 0; x < getWidth(); x++) {
            int y = (int)(baseY + Math.sin((x + waveTime * 0.5) * 0.03) * 20);
            g2d.drawLine(x, y, x, getHeight());
        }

        // Front wave
        g2d.setColor(new Color(0, 80, 130));
        for (int x = 0; x < getWidth(); x++) {
            int y = (int)(baseY + Math.sin((x + waveTime) * 0.05) * 35);
            g2d.drawLine(x, y, x, getHeight());
        }
    }


    public void actionPerformed(ActionEvent e) {

        //  Sunrise
        if (!expandSun) {
            sunY -= 1.2;
            if (sunY < 200) {
                expandSun = true;
            }
        }

        //  Expand sun + mountains move
        else if (!oceanScene) {
            sunSize += 15;
            sunY -= 5;
            mountainOffset += 5;

            if (sunSize > getWidth() * 2) {
                transitionAlpha += 0.02f;

                if (transitionAlpha >= 1f) {
                    oceanScene = true;
                }
            }
        }

        //  Ocean animation
        else {
            waveTime += 5;
        }

        repaint();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Sunrise to Ocean");
        Sunrise panel = new Sunrise();

        frame.add(panel);
        frame.setSize(700, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}