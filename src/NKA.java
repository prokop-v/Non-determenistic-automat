import org.apache.batik.swing.JSVGCanvas;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class NKA {

    public static ArrayList<Character> list0_9 = new ArrayList<>();
    public static ArrayList<Character> list1_9 = new ArrayList<>();
    public static ArrayList<Character> list0_7 = new ArrayList<>();
    public static ArrayList<Character> list0_F = new ArrayList<>();
    public static ArrayList<Character> list0 = new ArrayList<>();
    public static ArrayList<Character> listX = new ArrayList<>();
    public static final char END = 'K';
    public static final char RESET = 'R';
    public static char input;


    public static void main(String[] args) throws InterruptedException {
        Collections.addAll(list0_9, '0', '1', '2', '3', '4', '5', '6', '7', '8', '9');
        Collections.addAll(list1_9, '1', '2', '3', '4', '5', '6', '7', '8', '9');
        Collections.addAll(list0_7, '0', '1', '2', '3', '4', '5', '6', '7');
        Collections.addAll(list0_F, '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F');
        list0.add('0');
        listX.add('X');
        ArrayList<Character>[][] array = new ArrayList[5][2];
        fillarray(array);

        JFrame frame = new JFrame("Simple SVG Viewer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        // Create a new JSVGCanvas
        JSVGCanvas svgCanvas1 = new JSVGCanvas();
        svgCanvas1.addKeyListener(new KeyListener() {
            char input;
            @Override
            public void keyTyped(KeyEvent e) {

                if(e.getKeyChar() == END) {

                    System.out.println("Program končí.");
                    //System.exit(0);
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });

        // Load the first SVG file
        File svgFile1 = new File("input_svgs/NKA_START.svg");

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


    public static void fillarray(ArrayList<Character>[][] array){
        //array[0] = stav A
        array[0][0] = list0;
        //array[1] = stav B
        array[1][0] = list1_9;
        array[1][1] = list0_9;
        //array[2] = stav C
        array[2][0] = list0_7;
        //array[3] = stav D
        array[3][0] = listX;
        //array[4] = stav E
        array[4][0] = list0_F;
    }
}