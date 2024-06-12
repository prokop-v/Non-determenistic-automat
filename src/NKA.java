import org.apache.batik.swing.JSVGCanvas;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;


/**
 * @author Vaclav Prokop, Filip Valtr
 */
public class NKA {

    /**0
     * List of characters which allows input from 0 to 9
     */
    public static ArrayList<Character> list0_9 = new ArrayList<>();

    /**
     * List of characters which allows input from 1 to 9
     */
    public static ArrayList<Character> list1_9 = new ArrayList<>();

    /**
     * List of characters which allows input from 0 to 7
     */
    public static ArrayList<Character> list0_7 = new ArrayList<>();

    /**
     * List of characters which allows input from 0 to F
     */
    public static ArrayList<Character> list0_F = new ArrayList<>();

    /**
     * List of characters which allows input of 0
     */
    public static ArrayList<Character> list0 = new ArrayList<>();

    /**
     * List of characters which allows input of X
     */
    public static ArrayList<Character> listX = new ArrayList<>();

    /**
     * Component which allows us to manipulate and draw on SVG files
     */
    public static JSVGCanvas svgCanvas = new JSVGCanvas();

    /**
     * Constant which represents the end of the program
     */
    public static final char END = 'K';

    /**
     * Constant used for restarting the program
     */
    public static final char RESET = 'R';

    /**
     * Attribute used for specifying the image to be loaded
     */
    public static String picture = "NKA_START";

    /**
     * Following state of the program
     */
    public static int newState = 5;

    /**
     * HashMap where the key is the current state and the value is the neighbors
     */
    public static HashMap<Integer, int[]> table = new HashMap<>();

    /**
     * Array of the lists
     */
    public static ArrayList<Character>[][] array = new ArrayList[5][2];

    /**
     * Attribute used for output
     */
    public static String outputTextField = "Zadaný řetezec: ";

    /**
     * Text area used for displaying the output
     */
    public static JTextField textField;

    public static void main(String[] args) throws InterruptedException {
        // Loading data into lists
        Collections.addAll(list0_9, '0', '1', '2', '3', '4', '5', '6', '7', '8', '9');
        Collections.addAll(list1_9, '1', '2', '3', '4', '5', '6', '7', '8', '9');
        Collections.addAll(list0_7, '0', '1', '2', '3', '4', '5', '6', '7');
        Collections.addAll(list0_F, '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F');
        list0.add('0');
        listX.add('X');

        // Filling array with lists
        fillArray(array);

        // Filling table (hash map) with states
        fillTable(table);

        // Creating the frame for the program output
        JFrame frame = new JFrame("Simple SVG Viewer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);

        // Create a text field
        textField = new JTextField(20);

        // Adjust the width here
        Dimension textFieldPreferredSize = new Dimension(150, 60);
        textField.setPreferredSize(textFieldPreferredSize);

        // Set font size
        Font textFieldFont = textField.getFont();
        textFieldFont = textFieldFont.deriveFont(Font.PLAIN, 20); // Adjust the font size here
        textField.setFont(textFieldFont);

        // Add the text field to the bottom of the frame
        frame.add(textField, BorderLayout.SOUTH);
        textField.setText(outputTextField);

        //Key listener
        svgCanvas.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                //
                changeState(newState, e.getKeyChar(), textField);
                char keyChar = Character.toUpperCase(e.getKeyChar());
                if (keyChar != 'R') {
                    textField.setText(textField.getText() + e.getKeyChar());
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN ||
                        e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    e.consume();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // No action needed on key release
            }
        });

        // Load the first SVG file
        String svgFileName = "input_svgs/" + picture + ".svg";
        URL svgFileUrl = NKA.class.getResource("/" + svgFileName);

        if (svgFileUrl == null) {
            System.err.println("One or both SVG files not found.");
            return;
        }

        // Set the first SVG
        svgCanvas.setURI(svgFileUrl.toString());

        // Add the canvas to the frame
        frame.add(svgCanvas, BorderLayout.CENTER);
        frame.setVisible(true);

        // Disable focusability
        textField.setFocusable(false);

        // Update the SVG
        updateSVG();
    }

    /**
     * Fills the array with lists of characters for each state
     */
    public static void fillArray(ArrayList<Character>[][] array) {
        array[0][0] = list0;      // State A
        array[1][0] = list1_9;    // State B
        array[1][1] = list0_9;    // State B
        array[2][0] = list0_7;    // State C
        array[3][0] = listX;      // State D
        array[4][0] = list0_F;    // State E
        // State S is not explicitly filled
    }

    /**
     * Sets the current picture and updates the SVG
     */
    public static void pictureSetter(String pictureName) {
        picture = pictureName;
        updateSVG();
    }

    /**
     * Updates the SVG displayed in the canvas
     */
    public static void updateSVG() {
        String svgFileName = "input_svgs/" + picture + ".svg";
        URL svgFileUrl = NKA.class.getClassLoader().getResource(svgFileName);

        if (svgFileUrl != null) {
            // Load from JAR or resources folder
            svgCanvas.setURI(svgFileUrl.toString());
        } else {
            // Fallback to file system for IDE support
            File svgFile = new File(svgFileName);
            if (svgFile.exists()) {
                svgCanvas.setURI(svgFile.toURI().toString());
            } else {
                System.err.println("SVG file not found: " + svgFileName);
            }
        }
        disableTextFieldIfTextFits();
    }

    /**
     * Disables the text field if the text fits within its width
     */
    private static void disableTextFieldIfTextFits() {
        String text = textField.getText();
        FontMetrics fontMetrics = textField.getFontMetrics(textField.getFont());
        int textWidth = fontMetrics.stringWidth(text);
        int textFieldWidth = textField.getWidth();
        textField.setFocusable(textWidth >= textFieldWidth);
    }

    /**
     * Changes the state based on the input character and updates the text field
     */
    public static void changeState(int oldState, char e, JTextField textField) {
        char keyChar = Character.toUpperCase(e);
        if (keyChar == END) {
            System.out.println("Program ends.");
            System.exit(0);
        }
        if (keyChar == RESET) {
            pictureSetter("NKA_START");
            String outputTextField = "Zadaný řetezec: ";
            textField.setText(outputTextField);
            newState = 5;
            return;
        }

        //We find the new state and marked hims as a newone
        newState = searchInput(table.get(oldState), e);

        if (newState == 1 && oldState == 5) { // State transition B
            pictureSetter("NKA_FIRST_INPUT_1-9");
        } else if (newState == 1 && oldState == 1) {
            pictureSetter("NKA_INPUT_0-9_1");
        } else if (newState == 0 && oldState == 5) { // State transition A
            pictureSetter("NKA_FIRST_INPUT_0");
        } else if ((newState == 2 && oldState == 0) || (newState == 2 && oldState == 2)) { // State transition C
            pictureSetter("NKA_INPUT_0-7");
        } else if (newState == 3 && oldState == 0) { // State transition D
            pictureSetter("NKA_INPUT_X");
        } else if ((newState == 4 && oldState == 3) || (newState == 4 && oldState == 4)) { // State transition E
            pictureSetter("NKA_INPUT-0-9,A-F");
        }
        //if the character doesn't match allowed alphabet than the program ends and error log is printed
        if (newState == -1) {
            System.err.println("Input character: " + e + " does not match any allowed character.");
            System.exit(1);
        }
    }

    /**
     * Searches for the input character in the allowed states and returns the new state
     */
    public static int searchInput(int[] states, char key) {
        for (int i = 0; i < states.length; i++) {
            for (int y = 0; y < array[states[i]].length; y++) {
                if (array[states[i]][y] != null && array[states[i]][y].contains(key)) {
                    return states[i];
                }
            }
        }
        return -1;
    }

    /**
     * Fills the HashMap with the states and their neighbors
     */
    public static void fillTable(HashMap<Integer, int[]> table) {
        table.put(5, new int[]{0, 1}); // State S
        table.put(0, new int[]{2, 3}); // State A
        table.put(1, new int[]{1});    // State B
        table.put(2, new int[]{2});    // State C
        table.put(3, new int[]{4});    // State D
        table.put(4, new int[]{4});    // State E
    }
}
