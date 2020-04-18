package ub.cse.algo;

import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

public class Point implements Serializable{
    public Integer _x;  // x coordinate
    public Integer _y;  // y coordinate
    
    Point(Integer x, Integer y) {
        _x = x;
        _y = y;
    }
    
    /**
     * Used to compare if two Point match.
     * @param compare The other Point that this is being compared to.
     * @return true if they share the same x and y coordinate
     */
    public boolean equals(Point compare){
        return (_x.equals(compare._x)) && (_y.equals(compare._y));
    }
}

