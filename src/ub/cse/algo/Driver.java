package ub.cse.algo;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Arrays;

public class Driver {

    private static ArrayList<Point> _points;
    private static Integer _n_points; 
    public static void main(String[] args){
        if(args.length != 1){
            System.err.println("Please provide the input filepath as the first argument");
            return;
        }
        //Get the input filename from command line arguments and parse it
        String inputFile = args[0];
        readFile(inputFile);

        // Get the distance
        Solution student_class = new Solution(_n_points, _points);
        System.out.println(student_class.outputClosestDistance());
    }

    /**
     * This function will read input file 
     * @param inFile file name of file to be read
     */
    private static void readFile(String inFile){
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(inFile));
            _n_points = Integer.valueOf(bufferedReader.readLine());
            _points = new ArrayList<Point>();
            String line = "";
            for (int i = 0; i < _n_points; i++) {
                line = bufferedReader.readLine();
                String[] parts = line.trim().split("\\s+"); 
                Point point = new Point(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
                _points.add(point);
            }
            bufferedReader.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
