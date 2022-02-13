package it.unipr.advmobdev.mat301275.facemorph.opencv;

import org.opencv.core.Point;

import java.util.ArrayList;

public class MorphConfiguration {
    public ArrayList<Point> points1;
    public ArrayList<Point> points2;

    public MorphConfiguration(ArrayList<Point> points1, ArrayList<Point> points2) {
        this.points1 = points1;
        this.points2 = points2;
    }

}
