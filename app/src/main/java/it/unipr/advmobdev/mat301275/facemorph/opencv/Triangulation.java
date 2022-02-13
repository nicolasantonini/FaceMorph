package it.unipr.advmobdev.mat301275.facemorph.opencv;

import android.util.Log;

import org.opencv.core.MatOfFloat6;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.imgproc.Subdiv2D;

import java.util.ArrayList;

public class Triangulation {

    public static ArrayList<Triangle> calcDelaunayTriangles(Rect rect, ArrayList<Point> points) {

        //Gets the list of triangles from the Delaunay triangolation of the provided points
        ArrayList<Triangle> triangles = new ArrayList<>();
        Subdiv2D subdiv2D = new Subdiv2D(rect);
        for (int i = 0; i < points.size(); i++) {
            subdiv2D.insert(points.get(i));
        }
        MatOfFloat6 triangleList = new MatOfFloat6();
        subdiv2D.getTriangleList(triangleList);
        Point[] pt = new Point[3];
        int[] ind = new int[3];
        for (int i = 0; i < triangleList.width()*triangleList.rows(); i++) {
            double[] t = triangleList.get(i,0);
            pt[0] = new Point(t[0], t[1]);
            pt[1] = new Point(t[2], t[3]);
            pt[2] = new Point(t[4], t[5]);
            if (rect.contains(pt[0]) && rect.contains(pt[1]) && rect.contains(pt[2])) {
                for (int j = 0; j < 3; j++) {
                    for (int k = 0; k < points.size(); k++) {
                        if ((Math.abs(pt[j].x - points.get(k).x) < 1) && (Math.abs(pt[j].y - points.get(k).y) < 1)) {
                            ind[j] = k;
                        }
                    }
                }
                Triangle tr = new Triangle(ind[0], ind[1], ind[2]);
                triangles.add(tr);
            }
        }
        return triangles;
    }
}
