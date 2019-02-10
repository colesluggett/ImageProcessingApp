/*
 *Hunter Lloyd created App window
 *Cole Sluggett created all functions
 * Copyrite.......I wrote, ask permission if you want to use it outside of class.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.awt.image.PixelGrabber;
import java.awt.image.MemoryImageSource;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;
import java.util.prefs.Preferences;

class IMP implements MouseListener {
    JFrame frame;
    JPanel mp;
    JButton start;
    JScrollPane scroll;
    JMenuItem openItem, exitItem, resetItem;
    Toolkit toolkit;
    File pic;
    ImageIcon img;
    int colorX, colorY;
    int[] pixels;
    int[] results;
    //Instance Fields you will be using below

    //This will be your height and width of your 2d array
    int height = 0, width = 0;

    //your 2D array of pixels
    int picture[][];

    /*
     * In the Constructor I set up the GUI, the frame the menus. The open pulldown
     * menu is how you will open an image to manipulate.
     */
    IMP() {
        toolkit = Toolkit.getDefaultToolkit();
        frame = new JFrame("Image Processing Software by Hunter");
        JMenuBar bar = new JMenuBar();
        JMenu file = new JMenu("File");
        JMenu functions = getFunctions();
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent ev) {
                quit();
            }
        });
        openItem = new JMenuItem("Open");
        openItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                handleOpen();
            }
        });
        resetItem = new JMenuItem("Reset");
        resetItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                reset();
            }
        });
        exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                quit();
            }
        });
        file.add(openItem);
        file.add(resetItem);
        file.add(exitItem);
        bar.add(file);
        bar.add(functions);
        frame.setSize(600, 600);
        mp = new JPanel();
        mp.setBackground(new Color(0, 0, 0));
        scroll = new JScrollPane(mp);
        frame.getContentPane().add(scroll, BorderLayout.CENTER);
        JPanel butPanel = new JPanel();
        butPanel.setBackground(Color.black);
        start = new JButton("start");
        start.setEnabled(false);
        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                fun1();
            }
        });
        butPanel.add(start);
        frame.getContentPane().add(butPanel, BorderLayout.SOUTH);
        frame.setJMenuBar(bar);
        frame.setVisible(true);
    }

    /*
     * This method creates the pulldown menu and sets up listeners to selection of the menu choices. If the listeners are activated they call the methods
     * for handling the choice, fun1, fun2, fun3, fun4, etc. etc.
     */

    private JMenu getFunctions() {
        JMenu fun = new JMenu("Functions");

        JMenuItem firstItem = new JMenuItem("Remove Red(Example)");
        JMenuItem secondItem = new JMenuItem("Rotate 90 Degrees Clockwise");
        JMenuItem thirdItem = new JMenuItem("Rotate 90 Degrees Counter-Clockwise");
        JMenuItem fourthItem = new JMenuItem("Convert to Grayscale");
        JMenuItem fifthItem = new JMenuItem("Blur in Grayscale");
        JMenuItem sixthItem = new JMenuItem("Edge Detection 3x3 Mask");
        JMenuItem seventhItem = new JMenuItem("Edge Detection 5x5 Mask");
        JMenuItem eighthItem = new JMenuItem("Draw RGB Histogram");
        JMenuItem ninthItem = new JMenuItem("Histogram Equalization");
        JMenuItem tenthItem = new JMenuItem("Track Orange");

        firstItem.addActionListener(evt -> fun1());
        secondItem.addActionListener(evt -> fun2());
        thirdItem.addActionListener(evt -> fun3());
        fourthItem.addActionListener(evt -> fun4());
        fifthItem.addActionListener(evt -> fun5());
        sixthItem.addActionListener(evt -> fun6());
        seventhItem.addActionListener(evt -> fun7());
        eighthItem.addActionListener(evt -> fun8());
        ninthItem.addActionListener(evt -> fun9());
        tenthItem.addActionListener(evt -> fun10());


        fun.add(firstItem);
        fun.add(secondItem);
        fun.add(thirdItem);
        fun.add(fourthItem);
        fun.add(fifthItem);
        fun.add(sixthItem);
        fun.add(seventhItem);
        fun.add(eighthItem);
        fun.add(ninthItem);
        fun.add(tenthItem);

        return fun;

    }

    /*
     * This method handles opening an image file, breaking down the picture to a one-dimensional array and then drawing the image on the frame.
     * You don't need to worry about this method.
     */
    private void handleOpen() {
        img = new ImageIcon();
        JFileChooser chooser = new JFileChooser();
        Preferences pref = Preferences.userNodeForPackage(IMP.class);
        String path = pref.get("DEFAULT_PATH", "");

        chooser.setCurrentDirectory(new File(path));
        int option = chooser.showOpenDialog(frame);

        if (option == JFileChooser.APPROVE_OPTION) {
            pic = chooser.getSelectedFile();
            pref.put("DEFAULT_PATH", pic.getAbsolutePath());
            img = new ImageIcon(pic.getPath());
        }
        width = img.getIconWidth();
        height = img.getIconHeight();

        JLabel label = new JLabel(img);
        label.addMouseListener(this);
        pixels = new int[width * height];

        results = new int[width * height];


        Image image = img.getImage();

        PixelGrabber pg = new PixelGrabber(image, 0, 0, width, height, pixels, 0, width);
        try {
            pg.grabPixels();
        } catch (InterruptedException e) {
            System.err.println("Interrupted waiting for pixels");
            return;
        }
        for (int i = 0; i < width * height; i++)
            results[i] = pixels[i];
        turnTwoDimensional();
        mp.removeAll();
        mp.add(label);

        mp.revalidate();
    }

    /*
     * The libraries in Java give a one dimensional array of RGB values for an image, I thought a 2-Dimensional array would be more usefull to you
     * So this method changes the one dimensional array to a two-dimensional.
     */
    private void turnTwoDimensional() {
        picture = new int[height][width];
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++)
                picture[i][j] = pixels[i * width + j];


    }

    /*
     *  This method takes the picture back to the original picture
     */
    private void reset() {
        for (int i = 0; i < width * height; i++)
            pixels[i] = results[i];
        Image img2 = toolkit.createImage(new MemoryImageSource(width, height, pixels, 0, width));

        JLabel label2 = new JLabel(new ImageIcon(img2));
        turnTwoDimensional();
        mp.removeAll();
        mp.add(label2);

        mp.revalidate();
        mp.repaint();
    }

    /*
     * This method is called to redraw the screen with the new image.
     */
    private void resetPicture() {
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++)
                pixels[i * width + j] = picture[i][j];
        Image img2 = toolkit.createImage(new MemoryImageSource(width, height, pixels, 0, width));

        JLabel label2 = new JLabel(new ImageIcon(img2));
        mp.removeAll();
        mp.add(label2);

        mp.revalidate();
        mp.repaint();
    }


    /*
     * This method takes a single integer value and breaks it down doing bit manipulation to 4 individual int values for A, R, G, and B values
     */
    private int[] getPixelArray(int pixel) {
        int temp[] = new int[4];
        temp[0] = (pixel >> 24) & 0xff;
        temp[1] = (pixel >> 16) & 0xff;
        temp[2] = (pixel >> 8) & 0xff;
        temp[3] = (pixel) & 0xff;
        return temp;

    }

    /*
     * This method takes an array of size 4 and combines the first 8 bits of each to create one integer.
     */
    private int getPixels(int rgb[]) {
        int alpha = 0;
        int rgba = (rgb[0] << 24) | (rgb[1] << 16) | (rgb[2] << 8) | rgb[3];
        return rgba;
    }

    public void getValue() {
        int pix = picture[colorY][colorX];
        int temp[] = getPixelArray(pix);
        System.out.println("Color value " + temp[0] + " " + temp[1] + " " + temp[2] + " " + temp[3]);
    }

    /**************************************************************************************************
     * This is where you will put your methods. Every method below is called when the corresponding pulldown menu is
     * used. As long as you have a picture open first the when your fun1, fun2, fun....etc method is called you will
     * have a 2D array called picture that is holding each pixel from your picture.
     *************************************************************************************************/
    /*
     * Example function that just removes all red values from the picture.
     * Each pixel value in picture[i][j] holds an integer value. You need to send that pixel to getPixelArray the method which will return a 4 element array
     * that holds A,R,G,B values. Ignore [0], that's the Alpha channel which is transparency, we won't be using that, but you can on your own.
     * getPixelArray will breaks down your single int to 4 ints so you can manipulate the values for each level of R, G, B.
     * After you make changes and do your calculations to your pixel values the getPixels method will put the 4 values in your ARGB array back into a single
     * integer value so you can give it back to the program and display the new picture.
     */
    private void fun1() {

        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++) {
                int rgbArray[] = new int[4];

                //get three ints for R, G and B
                rgbArray = getPixelArray(picture[i][j]);


                rgbArray[1] = 0;
                //take three ints for R, G, B and put them back into a single int
                picture[i][j] = getPixels(rgbArray);
            }
        resetPicture();
    }

    /*
    This function rotates the image 90 degrees clockwise without stretching the pixels.

     */
    private void fun2() {
        //temp image to hold new values while rotating
        int rotateImg[][] = new int[width][height];

        int temp = height;
        height = width;
        width = temp;
        //Selects each pixel and sets it to the new value
        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++) {

                rotateImg[j][i] = picture[i][j];
            }
        //Sets original image to new rotated image
        picture = rotateImg;

        resetPicture();
    }


    /*
    This function rotates the image 90 degrees counter-clockwise without stretching the pixels.

     */
    private void fun3() {
        int rotateImg[][] = new int[width][height];
        int temp = width;
        width = height;
        height = temp;

        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++) {

                rotateImg[j][i] = picture[width - i - 1][height - j - 1];
            }
        picture = rotateImg;

        resetPicture();
    }

    /*
    This function turns the image into a grayscale image using the luminosity technique.

     */
    private void fun4() {
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++) {
                int rgbArray[];
                rgbArray = (getPixelArray(picture[i][j]));

                //Sets every pixels RGB value to the same thing while weighting each value.
                rgbArray[1] = rgbArray[2] = rgbArray[3] = (int) ((rgbArray[0] * .21) + (rgbArray[1] * .72) + (rgbArray[2] * .07) / 3);

                picture[i][j] = getPixels(rgbArray);
            }
        resetPicture();
    }

    /*
    This function uses the grayscale function and then blurs the image.
    
     */
    private void fun5() {
        fun4();
        int blurImg[][] = new int[height][width];
        for (int i = 1; i < height - 1; i++)
            for (int j = 1; j < width - 1; j++) {

                if (i != 0 || i != height - 1 || j != 0 || j != width - 1) {


                    //Gets surrounding pixels
                    int rgbArray1[] = (getPixelArray(picture[i - 1][j - 1]));
                    int rgbArray2[] = (getPixelArray(picture[i][j - 1]));
                    int rgbArray3[] = (getPixelArray(picture[i + 1][j - 1]));
                    int rgbArray4[] = (getPixelArray(picture[i + 1][j]));
                    int rgbArray5[] = (getPixelArray(picture[i + 1][j + 1]));
                    int rgbArray6[] = (getPixelArray(picture[i][j + 1]));
                    int rgbArray7[] = (getPixelArray(picture[i - 1][j + 1]));
                    int rgbArray8[] = (getPixelArray(picture[i - 1][j]));

                    //averages the r value
                    int blurAverage = (rgbArray1[1] + rgbArray2[1] + rgbArray3[1] + rgbArray4[1]
                            + rgbArray5[1] + rgbArray6[1] + rgbArray7[1] + rgbArray8[1]) / 8;

                    //sets new values to the average
                    int[] rgbArray = new int[4];
                    rgbArray[1] = rgbArray[2] = rgbArray[3] = blurAverage;
                    rgbArray[0] = 255;

                    blurImg[i][j] = getPixels(rgbArray);
                }
            }
        picture = blurImg;

        resetPicture();
    }

    /*
    This function uses the grayscale function and then uses a 3 by 3 mask to find the edges of the
    image and turns the images white and everything else black.
    
     */
    private void fun6() {
        fun4();

        int masked[][] = new int[height][width];
        for (int i = 1; i < height - 1; i++)
            for (int j = 1; j < width - 1; j++) {
                if (i != 0 || i != height - 1 || j != 0 || j != width - 1) {
                    int average;

                    int rgbArray0[] = getPixelArray(picture[i][j]);
                    int rgbArray1[] = (getPixelArray(picture[i - 1][j - 1]));
                    int rgbArray2[] = (getPixelArray(picture[i][j - 1]));
                    int rgbArray3[] = (getPixelArray(picture[i + 1][j - 1]));
                    int rgbArray4[] = (getPixelArray(picture[i + 1][j]));
                    int rgbArray5[] = (getPixelArray(picture[i + 1][j + 1]));
                    int rgbArray6[] = (getPixelArray(picture[i][j + 1]));
                    int rgbArray7[] = (getPixelArray(picture[i - 1][j + 1]));
                    int rgbArray8[] = (getPixelArray(picture[i - 1][j]));


                    average = (rgbArray0[1] * -8) + rgbArray1[1] + rgbArray2[1] + rgbArray3[1] + rgbArray4[1]
                            + rgbArray5[1] + rgbArray6[1] + rgbArray7[1] + rgbArray8[1];

                    masked[i][j] = fillArray(average);

                }
            }

        picture = masked;
        resetPicture();
    }

    /*
    This function uses the grayscale function and then uses a 5 by 5 mask to find the edges of the
    image and turns the images white and everything else black.
    
    Looks a lot better than the 3 by 3 mask.
     */
    private void fun7() {
        fun4();
        int masked[][] = new int[height][width];
        for (int i = 2; i < height - 2; i++)
            for (int j = 2; j < width - 2; j++) {
                if (i > 1 || i != height - 2 || j > 1 || j != width - 2) {
                    int average = 0;

                    int rgbArray0[] = getPixelArray(picture[i][j]);
                    int rgbArray1[] = getPixelArray(picture[i - 2][j - 2]);
                    int rgbArray2[] = getPixelArray(picture[i - 1][j - 2]);
                    int rgbArray3[] = getPixelArray(picture[i][j - 2]);
                    int rgbArray4[] = getPixelArray(picture[i + 1][j - 2]);
                    int rgbArray5[] = getPixelArray(picture[i + 2][j - 2]);
                    int rgbArray6[] = getPixelArray(picture[i + 2][j - 1]);
                    int rgbArray7[] = getPixelArray(picture[i + 2][j]);
                    int rgbArray8[] = getPixelArray(picture[i + 2][j + 1]);
                    int rgbArray9[] = getPixelArray(picture[i + 2][j + 2]);
                    int rgbArray10[] = getPixelArray(picture[i + 1][j + 2]);
                    int rgbArray11[] = getPixelArray(picture[i][j + 2]);
                    int rgbArray12[] = getPixelArray(picture[i - 1][j + 2]);
                    int rgbArray13[] = getPixelArray(picture[i - 2][j + 2]);
                    int rgbArray14[] = getPixelArray(picture[i - 2][j + 1]);
                    int rgbArray15[] = getPixelArray(picture[i - 2][j]);
                    int rgbArray16[] = getPixelArray(picture[i - 2][j - 1]);


                    average = (rgbArray0[1] * -16) + rgbArray1[1] + rgbArray2[1] + rgbArray3[1]
                            + rgbArray4[1] + rgbArray5[1] + rgbArray6[1] + rgbArray7[1] + rgbArray8[1]
                            + rgbArray9[1] + rgbArray10[1] + rgbArray11[1] + rgbArray12[1]
                            + rgbArray13[1] + rgbArray14[1] + rgbArray15[1] + rgbArray16[1];


                    masked[i][j] = fillArray(average);

                }
            }

        picture = masked;
        resetPicture();
    }

    /*
    This function creates 3 histograms of the 3 rgb values and displays how many values of each one it has.
    
     */
    private void fun8() {

        //creates each color array
        int[][] red = iColorArray(1);
        int[][] green = iColorArray(2);
        int[][] blue = iColorArray(3);

        //counts each value for how many times it appears in the image
        Map<Integer, Integer> redMap = createMap(red);
        Map<Integer, Integer> greenMap = createMap(green);
        Map<Integer, Integer> blueMap = createMap(blue);

        //creates window for red, green, and then blue
        JFrame redFrame = new JFrame("Red");
        redFrame.setLayout(new BorderLayout());
        //creates panel
        redFrame.add(new JScrollPane(new MyPanel(redMap, 1)));
        //sets location
        redFrame.setLocation(600, 0);
        redFrame.pack();
        redFrame.setVisible(true);

        JFrame greenFrame = new JFrame("Green");
        greenFrame.setLayout(new BorderLayout());
        greenFrame.add(new JScrollPane(new MyPanel(greenMap, 2)));
        greenFrame.pack();
        greenFrame.setLocation(905, 0);
        greenFrame.setVisible(true);

        JFrame blueFrame = new JFrame("Blue");
        blueFrame.setLayout(new BorderLayout());
        blueFrame.add(new JScrollPane(new MyPanel(blueMap, 3)));
        blueFrame.pack();
        blueFrame.setLocation(1210, 0);
        blueFrame.setVisible(true);

    }

    /*
    This function equalizes the histograms so the rgb values are evenly spaced throughout
    the histogram.
    
     */
    private void fun9() {

        int equal[][];
        int red[][];
        int green[][];
        int blue[][];

        red = iColorArray(1);
        green = iColorArray(2);
        blue = iColorArray(3);

        Map<Integer, Integer> redMap = createMap(red);
        equal = equalize(redMap, 1);
        picture = equal;
        Map<Integer, Integer> greenMap = createMap(green);
        equal = equalize(greenMap, 2);
        picture = equal;
        Map<Integer, Integer> blueMap = createMap(blue);
        equal = equalize(blueMap, 3);
        picture = equal;


        picture = equal;
        resetPicture();
    }

    /*
    This function tracks the color orange in any image and turns it into a binary image
    where the pixels that are orange turn into white and everything else turn into black.
    
     */
    private void fun10(){
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++) {
                int rgbArray[];

                //get three ints for R, G and B
                rgbArray = getPixelArray(picture[i][j]);

                if((rgbArray[1] > 230 && rgbArray[1] < 256) && (rgbArray[2] > 75 && rgbArray[2] < 200) && (rgbArray[3] > -1 && rgbArray[3] < 80)){
                    rgbArray[1] = rgbArray[2] =rgbArray[3] = 255;
                }else{
                    rgbArray[1] = rgbArray[2] =rgbArray[3] = 0;
                }
                picture[i][j] = getPixels(rgbArray);
            }
        resetPicture();
    }


    /*
    This function finds the individual color arrays for the color of choice for both
    histogram functions.
    
     */
    private int[][] iColorArray(int color) {
        int temp[][] = new int[height][width];
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++) {
                int rgbArray[];

                //get three ints for R, G and B
                rgbArray = getPixelArray(picture[i][j]);
                temp[i][j] = rgbArray[color];

            }
        return temp;
    }

    /*
    This function will equalize each color using the color map and the rgb array number as an int.
    
     */
    private int[][] equalize(Map<Integer, Integer> color, int c) {
        int tempPic[][] = new int[height][width];
        double pmf[] = new double[256];
        double cdf[] = new double[256];
        int totalPix = height * width;
        for (Integer key : color.keySet()) {
            double value = color.get(key);
            pmf[key] = value / totalPix;
            if (key == 0) {
                cdf[key] = pmf[key];
            } else {
                cdf[key] = cdf[key - 1] + pmf[key];
            }
        }
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++) {
                int rgbArray[];

                //get three ints for R, G and B
                rgbArray = getPixelArray(picture[i][j]);
                rgbArray[c] = (int) Math.round((rgbArray[c] - 1) * cdf[rgbArray[c]]);
                tempPic[i][j] = getPixels(rgbArray);
            }
        return tempPic;
    }

    /*
    This function fills an array with the same number and does not allow the number to be
    smaller than 0 or bigger than 255 for the mask functions.
    
     */
    private int fillArray(int a) {
        int finalRGB[] = new int[4];
        int b;
        if (a > 255) {
            a = 255;
            Arrays.fill(finalRGB, a);
            b = getPixels(finalRGB);
        } else if (a < 0) {
            a = 0;
            Arrays.fill(finalRGB, a);
            b = getPixels(finalRGB);
        } else {
            Arrays.fill(finalRGB, a);
            b = getPixels(finalRGB);
        }
        return b;
    }

    /*
    This function creates a map for the color array of choice to map
    how many values of each rgb value is in a image.
    
     */
    private Map createMap(int[][] color) {
        Map<Integer, Integer> map = new TreeMap<>();
        for (int c = 0; c < color.length; c++) {
            for (int r = 0; r < color[c].length; r++) {
                int value = color[c][r];
                int count;
                if (map.containsKey(value)) {
                    count = map.get(value);
                    count++;
                } else {
                    count = 1;
                }
                map.put(value, count);
            }
        }
        for (int i = 0; i < 256; i++) {
            if (!map.containsKey(i)) {
                map.put(i, 0);
            }
        }
        return map;
    }

    private void quit() {
        System.exit(0);
    }

    @Override
    public void mouseEntered(MouseEvent m) {
    }

    @Override
    public void mouseExited(MouseEvent m) {
    }

    @Override
    public void mouseClicked(MouseEvent m) {
        colorX = m.getX();
        colorY = m.getY();
        System.out.println(colorX + "  " + colorY);
        getValue();
        start.setEnabled(true);
    }

    @Override
    public void mousePressed(MouseEvent m) {
    }

    @Override
    public void mouseReleased(MouseEvent m) {
    }

    public static void main(String[] args) {
        IMP imp = new IMP();
    }

}