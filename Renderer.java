import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class Renderer {
    // Create a JFrame (window)
    private JFrame frame = new JFrame("Idle Fractal");
    // Create a JPanel to hold components
    private JPanel panel = new JPanel();

    public void init(){
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1600, 900);

        // Add the panel to the frame
        frame.add(panel);

        // Set the frame to be visible
        frame.setVisible(true);
    }

    public void render() {
        // This method will be used to render the game
        // For now, we will just set the background color
        panel.setBackground(java.awt.Color.BLACK);
        JLabel label = new JLabel("Idle Fractal");
        panel.add(label);
    }
}
