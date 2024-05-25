import org.apache.batik.swing.JSVGCanvas;
import javax.swing.*;
import java.awt.*;
import java.io.File;

public class SimpleSVGViewer {

    public static void main(String[] args) throws InterruptedException {
        // Create and set up the window
        JFrame frame = new JFrame("Simple SVG Viewer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        // Create a new JSVGCanvas
        JSVGCanvas svgCanvas1 = new JSVGCanvas();
        JSVGCanvas svgCanvas2 = new JSVGCanvas();


        // Load the first SVG file
        File svgFile1 = new File("input_svgs/NKA_INPUT_X.svg");

        if (!svgFile1.exists()) {
            System.err.println("One or both SVG files not found.");
            return;
        }

        // Set the first SVG
        svgCanvas1.setURI(svgFile1.toURI().toString());

        // Add the canvas to the frame
        frame.add(svgCanvas1, BorderLayout.CENTER);
        frame.setVisible(true);
    }
}