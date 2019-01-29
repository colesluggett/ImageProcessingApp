
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.Map;

public class MyPanel extends JPanel {

    private int color;
    private Map<Integer, Integer> map;


    //gives myPanel all of its preferences
    MyPanel(Map<Integer, Integer> map, int color) {
        this.color = color;
        this.map = map;
        Dimension size = new Dimension(305, 600);
        setPreferredSize(size);

    }

    //sets configurations for histogram graph
    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        if (map != null) {
            int  xBorder = 5;
            int  yBorder = 5;
            int width = 295;
            int height = 590;
            //draws border
            Graphics2D  gc = (Graphics2D) g;
             gc.setColor(Color.DARK_GRAY);
             gc.drawRect(  xBorder,  yBorder, width, height);
            int lineWidth = 1;
            int maxValue = 0;
            //finds most used value for each rgb value
            for (Integer key : map.keySet()) {
                int value = map.get(key);
                if(value > maxValue)
                    maxValue = value;
            }
            int xPosition = 25;
            //creates each histogram bar
            for (Integer key : map.keySet()) {
                int value = map.get(key);
                int lineHeight = Math.round(((float) value / (float) maxValue) * height);
                int yPosition = height +  yBorder - lineHeight;
                Rectangle2D line = drawing(xPosition, yPosition, lineWidth, lineHeight);
                gc.fill(line);
                //sets color of bar to the color value
                switch (color){
                    case(1):
                         gc.setColor(new Color(key,0,0));
                        break;
                    case(2):
                         gc.setColor(new Color(0,key,0));
                        break;
                    case(3):
                         gc.setColor(new Color(0,0,key));
                        break;
                }
                 gc.draw(line);
                xPosition += 1;
            }
             gc.dispose();
        }
    }

    //function for drawing a bar.
    private Rectangle2D drawing(int x, int y, int w, int h) {
        return new Rectangle2D.Float(x, y, w, h);
    }

}
