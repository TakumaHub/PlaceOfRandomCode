import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

public class Solution {
    
    static String [][] canvas;
    static int width;
    static int height;
    static int minX;
    static int minY;
    
    // Get rid of extra # for the rastered squares.
        // Idea:    1) do something for the points between x1 and x2, etc.
        //          2) may need to keep info on what layout the corners have. (2 or 4 choices)
        // Expand 1)
        //          NOTE: Remember the canvas has y-coordinate pointing downwards for positive.
        //          a) Find gradient between x1 and x2
        //          b) for positive gradient, and both rise and run are positive (i.e. x2>=x1, y2>=y1),
        //              need to reset values to the left of gradient line to "." (Gradient looks like \).
        //              Also, values to change are greater y-values from x1 towards x2.
        //          c) test each individual pixel within the box made from the corners x1 and x2.
        //          d) if gradient of pixel from x1 is greater than gradient from x1 to x2, change to ".".
        // for line joining x1 to x2:
        //          e) In the negative gradient case (x2>=x1, y2 < y1), test box will go with decreasing y, 
        //              and if gradient is less than the gradient from x1 to x2 (i.e. greater negative) then
        //              change to ".".
    static void deleteExtra(int x1, int y1, int x2, int y2){
        double gradx1x2 = 0;
        boolean run = false;
        boolean rise = false;
        if(x2-x1 > 0){
            run = true;
        }
        if(y2-y1 > 0){
            rise = true;
        }
        if(x2-x1 != 0){
            gradx1x2 = (double)(y2-y1)/(x2-x1);     // need double cast, else it is int/int = int
        }else{      // do nothing in this case.
            return;
        }
        
        if(gradx1x2 != 0){
            removeSide(x1,y1, x2,y2, run, rise);
        }   // do nothing for x1 == x2 (grad == 0) as no changes to canvas are necessary.
        return;
    }

    static boolean checkPerfectSquare(int x1, int y1, int x2, int y2){
        if(y2-y1 != 0){
            return true;
        }
        if(x2-x1 != 0){
            return true;
        }
        return false;
    }
    
    static void removeSide(int x1, int y1, int x2, int y2, boolean posRun, boolean posRise){
        double gradx1x2 = (double)(y2-y1)/(x2-x1);
        if(!(posRun || posRise) || (!posRun &&posRise)){  //switch for negative on negative = positive gradient
            int temp = x1;
            x1 = x2;
            x2 = temp;
        }
        if(!(posRun || posRise)|| (posRun && !posRise)){
            int temp = y1;
            y1 = y2;
            y2 = temp;
        }

        // We know x1 & y1 are the minimum values of the rectangle, x2 & y2 are the maximum.
        for(int i = y1; i<=y2; i++){
                for(int j = x1; j<=x2; j++){
                    if( i<height && i>=0 && j<width && j>=0){
                        double pointGradient = 0;
                        if(j-x1 != 0){
                            if((posRun && posRise)||!(posRun ||posRise)){
                                pointGradient = (double)(i-y1)/(j-x1);
                            }else{
                                pointGradient = (double)(i-y2)/(j-x1);
                            }
                        }else{
                            if((posRun && posRise)||!(posRun ||posRise)){
                                pointGradient = Double.POSITIVE_INFINITY;
                            }else{
                                pointGradient = Double.NEGATIVE_INFINITY;
                            }
                        }
                        if((posRun && posRise)){
                            if(gradx1x2 > pointGradient && !(i==y1 && j==x1)){
                                canvas[i][j] = ".";
                            }
                        }else if (!(posRun ||posRise)){
                            if(gradx1x2 < pointGradient && !(i==y1 && j==x1) ){
                                canvas[i][j] = ".";
                            }
                        } else if (!posRun && posRise){
                            if(gradx1x2 < pointGradient && !(i==y2 && j==x1) ){
                                canvas[i][j] = ".";
                            }
                        }else if (posRun && !posRise){
                            if(gradx1x2 > pointGradient && !(i==y2 && j==x1) ){
                                canvas[i][j] = ".";
                            }
                        }
                    }
                }
            }
        return;
    }
    
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int w = in.nextInt();
        int h = in.nextInt();
        int circleX = in.nextInt();
        int circleY = in.nextInt();
        int r = in.nextInt();
        int x1 = in.nextInt();
        int y1 = in.nextInt();
        int x3 = in.nextInt();
        int y3 = in.nextInt();
        // your code goes here
        width = w;
        height = h;
        // make a canvas that is w width (columns) and h height (rows).
        canvas = new String[h][w];
        for(int i = 0; i < h; i++){
            for(int j = 0; j < w; j++){
                canvas[i][j] = ".";
            }
        }
        
        // Add the Square
        // opposite corners defined, the position of adjacent corners are:
        // width-wise - height-wise = vertical distance to the other corners.
        // 
        // pick x1, y1 as the corner on the left side. i.e. x1 < x3
        int horiLength = 0;
        int vertLength = 0;
        int x4 = 0;
        int x2 = 0;
        int y2 = 0;
        int y4 = 0;
        
        if ((x1 >= x3 && y1 < y3) ){
            
            x4 = (x1+x3)/2 -(y3-y1)/2;
            x2 = (x1+x3)/2 + (y3-y1)/2;
        } else if ((x1 >= x3 && y1 >= y3) ){
            x4 = (x1+x3)/2 + (y3-y1)/2; // middle sign different to above
            x2 = (x1+x3)/2 - (y3-y1)/2; // middle sign different to above
        } else if (x1 < x3 && y1 >= y3){
            x4 = (x1+x3)/2 - (y3-y1)/2;
            x2 = (x1+x3)/2 + (y3-y1)/2;
        } else if(x1 < x3 && y1 < y3){
            x4 = (x1+x3)/2 - (y3-y1)/2; 
            x2 = (x1+x3)/2 + (y3-y1)/2;
        }
        y2 = (y3+y1)/2 + (x1-x3)/2;
        y4 = (y3+y1)/2 - (x1-x3)/2;
        // draw the square: Larger box version
        // Get mins and maxes to create biggest square
        minX = Math.min(Math.min(x1,x2), Math.min(x3, x4));
        int maxX = Math.max(Math.max(x1,x2), Math.max(x3,x4));
        minY = Math.min(Math.min(y1,y2), Math.min(y3, y4));
        int maxY = Math.max(Math.max(y1,y2), Math.max(y3,y4));
        //System.out.println(x1+ " " +x2+ " " +x3+ " "+x4+ " "+minX+ " "+maxX);
        for(int i = minY; i<=maxY; i++){
            for(int j = minX; j<=maxX; j++){
                if(i < h && i>=0  && j < w && j>=0)
                    canvas[i][j] = "#";
            }
        }
             
        // Line x1 to x2, etc:
        if(checkPerfectSquare(x1,y1, x2, y2)){
                deleteExtra(x1,y1, x2,y2);
                deleteExtra(x4,y4, x1,y1);
                deleteExtra(x3,y3, x4,y4);
                deleteExtra(x2,y2, x3,y3);
        }
        
        // Add the circle last
        double radius = r;
        for(int i = -r; i<=r ; i++){
            for(int j = -r; j <= r; j++){
                     //Additional condition: 
                     // if pixel's distance to centre is beyond radius, exclude pixel.
                 if(Math.sqrt((i)*(i) + (j)*(j)) <= radius){    // conpare to a doube, not an int
                     if (circleX + j >= 0 && circleX +j< w && circleY + i >=0 && circleY+i < h){
                         canvas[circleY + i][circleX + j] = "#";
                     }
                 }
            }
        }
        //print
        for(int i = 0; i < h; i++){
            for(int j = 0; j < w; j++){
                System.out.print(canvas[i][j]);
            }
            System.out.println();
        }
    }
}
