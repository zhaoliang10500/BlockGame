package ca.mcgill.ecse223.block.application;

import java.awt.geom.Line2D;
import java.util.Scanner;

public class Main() {
    
    public static void main (String[] args) {
        Scanner scan = new Scanner(System.in);
        int numberCars = scan.nextInt();
        int lightHeight = scan.nextInt();
        
        int index = 0;
        
        for (int i = 0; i < numberCars; i++) {
            int maxHeight = 0;
            int carHeight = 0;
            if (carHeight > maxHeight) {
                maxHeight = carHeight;
                index = i;
            }
            carHeight = scan.nextInt();
            
            Line2D currentCarToLight = new Line2D.Double(
              i,
              carHeight,
              0,
              lightHeight
    );
            Line2D carJToLight = new Line2D.Double(
              index,
              maxHeight,
              0,
              lightHeight
    );
            if (carJToLight.intersects(currentCarToLight)) {
                System.out.println(index);
            } else {
                System.out.println("0");
            }
           
        }
        
    }
}