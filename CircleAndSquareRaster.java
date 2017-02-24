import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

public class Solution {
    
    static String [][] canvas;
    static int width;
    static int height;
    
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
        System.out.println("In for ("+x1 +","+y1 +") and (" + x2+","+y2+")");
        double gradx1x2 = 0;
        if(x2-x1 != 0){
            gradx1x2 = (double)(y2-y1)/(x2-x1);     // need double cast, else it is int/int = int
        }else{      // do nothing in this case.
            System.out.println("Did nothing for ("+x1 +","+y1 +") and (" + x2+","+y2+")");
            return;
        }
        if(gradx1x2 > 0){
            if(x2-x1>0){    // checks whether true positive gradient
                // We know x1 & y1 are the minimum values of the rectangle, x2 & y2 are the maximum.
                for(int i = y1; i<=y2 && i<height; i++){
                    for(int j = x1; j<=x2 && j<width; j++){
                        double pointGradient = 0;
                        if(j-x1 != 0){
                            pointGradient = (double)(i-y1)/(j-x1);
                        }else{
                            System.out.println("Did infinite for ("+x1 +","+y1 +") and (" + x2+","+y2+")");
                            pointGradient = Double.POSITIVE_INFINITY;
                        }
                        if(gradx1x2 > pointGradient){
                            
                            canvas[i][j] = ".";
                        }
                    }
                }
            }else{
                System.out.println("In the else for ("+x1 +","+y1 +") and (" + x2+","+y2+")");
            }
        }else if (gradx1x2 < 0){
            System.out.println(" Bumming inside else if for ("+x1 +","+y1 +") and (" + x2+","+y2+")");
        } else {
            // do nothing for x1 == x2 (grad == 0) as no changes to canvas are necessary.
            System.out.println("What am I doing at ("+x1 +","+y1 +") and (" + x2+","+y2+")");
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
                //System.out.print(canvas[i][j]);
            }
            //System.out.println();
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
        /*boolean vertPos = true;
        
        if(y1>y3){
            vertLength = y1-y3;
        } else{
            vertLength = y3-y1;
            vertPos = false;
        }
        
        if(x1>x3){ 
            horiLength = x1-x3;
            x4 = x3 + vertLength;
            if(vertPos){    // y4 > y3
                y4 = y3 + horiLength - vertLength;
            }else{
                y4 = y3 + vertLength;
            }
        } else {
            x4 = x3 - vertLength;
            
        }
        */
        
        if ((x1 >= x3 && y1 < y3) || (x1 < x3 && y1 >= y3)){
            x4 = (x1+x3)/2 - (y3-y1)/2;
            x2 = (x1+x3)/2 + (y3-y1)/2;
        } else if ((x1 >= x3 && y1 >= y3) || (x1 < x3 && y1 < y3)){
            x4 = (x1+x3)/2 + (y3-y1)/2; // middle sign different to above
            x2 = (x1+x3)/2 - (y3-y1)/2; // middle sign different to above
        } 
        y2 = (y3+y1)/2 + (x1-x3)/2;
        y4 = (y3+y1)/2 - (x1-x3)/2;
        
        /*else if ((x1 < x3 && y1 >= y3)){
            x4 = (x1+x3)/2 - (y3-y1)/2;
            y4 = (y3+y1)/2 - (x1-x3)/2;
            x2 = (x1+x3)/2 + (y3-y1)/2;
            y2 = (y3+y1)/2 + (x1-x3)/2;
        } else if (x1 < x3 && y1 < y3){
            x4 = (x1+x3)/2 + (y3-y1)/2; 
            y4 = (y3+y1)/2 - (x1-x3)/2; 
            x2 = (x1+x3)/2 - (y3-y1)/2; 
            y2 = (y3+y1)/2 + (x1-x3)/2;
        }
        */
        
        // draw the square: Larger box version
        // Get mins and maxes to create biggest square
        int left = Math.min(Math.min(x1,x2), Math.min(x3, x4));
        int right = Math.max(Math.max(x1,x2), Math.max(x3,x4));
        int top = Math.min(Math.min(y1,y2), Math.min(y3, y4));
        int bottom = Math.max(Math.max(y1,y2), Math.max(y3,y4));
        //System.out.println(x1+ " " +x2+ " " +x3+ " "+x4+ " "+left+ " "+right);
        for(int i = top; i<=bottom && i < h; i++){
            for(int j = left; j<=right && j < w; j++){
                canvas[i][j] = "#";
            }
        }
        
        
        
        // Line x1 to x2:
        deleteExtra(x1,y1, x2,y2);
        deleteExtra(x4,y4, x1,y1);
        deleteExtra(x3,y3, x4,y4);
        deleteExtra(x2,y2, x3,y3);
        
        
        if(y1>= 0 && y1 < h && x1 >= 0 && x1 < w)
            canvas[y1][x1] = "1";
        if(y2>= 0 && y2 < h && x2 >= 0 && x2 < w)
            canvas[y2][x2] = "2";
        if(y3>= 0 && y3 < h && x3 >= 0 && x3 < w)
            canvas[y3][x3] = "3";
        if(y4>= 0 && y4 < h && x4 >= 0 && x4 < w)
            canvas[y4][x4] = "4";
        
        
        
        // Add the circle last
        double radius = r;
        for(int i = -r; i<=r; i++){
            for(int j = -r; j <= r; j++){
                 if (circleX + i >= 0 && circleX < w && circleY + j >=0 && circleY < h){
                     //Additional condition: 
                     // if pixel's distance to centre is beyond radius, exclude pixel.
                         
                     if(Math.sqrt(i*i + j*j) <= radius){    // conpare to a doube, not an int
                        canvas[circleY + j][circleX + i] = "#";
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
