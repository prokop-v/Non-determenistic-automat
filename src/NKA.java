import org.apache.batik.swing.JSVGCanvas;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Scanner;

public class NKA {

    public static ArrayList<Character> list0_9 = new ArrayList<>();
    public static ArrayList<Character> list1_9 = new ArrayList<>();
    public static ArrayList<Character> list0_7 = new ArrayList<>();
    public static ArrayList<Character> list0_F = new ArrayList<>();
    public static ArrayList<Character> list0 = new ArrayList<>();
    public static ArrayList<Character> listX = new ArrayList<>();
    public static JSVGCanvas svgCanvas = new JSVGCanvas();
    public static final char END = 'K';
    public static final char RESET = 'R';
    public static char input;
    public static String picture = "NKA_START";
    public static int newState = 5;
    public static HashMap<Integer, int[]> table = new HashMap<>();
    public static ArrayList<Character>[][] array = new ArrayList[5][2];


    public static void main(String[] args) throws InterruptedException {
        Collections.addAll(list0_9, '0', '1', '2', '3', '4', '5', '6', '7', '8', '9');
        Collections.addAll(list1_9, '1', '2', '3', '4', '5', '6', '7', '8', '9');
        Collections.addAll(list0_7, '0', '1', '2', '3', '4', '5', '6', '7');
        Collections.addAll(list0_F, '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F');
        list0.add('0');
        listX.add('X');
        fillarray(array);
        fillTable(table);
        JFrame frame = new JFrame("Simple SVG Viewer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        svgCanvas.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                changeState(newState, e.getKeyChar());
                System.out.println(e.getKeyChar());
            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });

        // Load the first SVG file
        File svgFile1 = new File("input_svgs/" + picture + ".svg");

        if (!svgFile1.exists()) {
            System.err.println("One or both SVG files not found.");
            return;
        }

        // Set the first SVG
        updateSVG();

        // Add the canvas to the frame
        frame.add(svgCanvas, BorderLayout.CENTER);
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
        //array[5] = stav S
    }

    public static void pictureSetter(String pictureName){
        picture = pictureName;
        updateSVG();
    }

    public static void updateSVG() {
        File svgFile1 = new File("input_svgs/" + picture + ".svg");

        if (!svgFile1.exists()) {
            System.err.println("SVG file not found: " + svgFile1.getPath());
            return;
        }

        // Set the new SVG
        svgCanvas.setURI(svgFile1.toURI().toString());
    }

    public static void changeState(int oldState, char e){
        //e = input
        //oldstate = kde zrovna jsme, chceme vrátit index stavu, do kterého se pomocí inputu dostaneme
        char keyChar = Character.toUpperCase(e);
        if (keyChar == END) {
            System.out.println("Program končí.");
            System.exit(0);
        }
        if (keyChar == RESET) {
            pictureSetter("NKA_START");
        }

        newState = searchInput(table.get(oldState), e);

        if (newState == 0) {
            pictureSetter("NKA_FIRST_INPUT_0");
        }
        if (keyChar == '0' && oldState == '6') {
            pictureSetter("NKA_FIRST_INPUT_0");
        }
        if(newState == -1){
            System.err.println("Vstupní znak nevyhovuje žádnému povolenému znaku");
            System.exit(1);
        }
    }

    public static int searchInput(int[] states, char key){
        for(int i = 0; i < states.length; i++){
            for (int y = 0; y < array[states[i]].length; y++){
                if (array[states[i]][y].contains(key) == true) {
                    return states[i];
                }
            }
        }
        System.out.println("xd");
        return -1;
    }

    public static void fillTable(HashMap<Integer, int[]> table){
        table.put(5, new int[]{0, 1}); //S
        table.put(0, new int[]{2, 3}); //A
        table.put(1, new int[]{1}); //B
        table.put(2, new int[]{2}); //C
        table.put(3, new int[]{4}); //D
        table.put(4, new int[]{4}); //E
    }
}