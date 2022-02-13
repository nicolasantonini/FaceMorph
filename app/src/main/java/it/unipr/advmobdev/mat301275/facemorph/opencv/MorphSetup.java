package it.unipr.advmobdev.mat301275.facemorph.opencv;

import android.graphics.Bitmap;

import org.opencv.android.Utils;
import org.opencv.core.DMatch;
import org.opencv.core.KeyPoint;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.Point;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.FeatureDetector;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MorphSetup {

    private static int FEATURE_DETECTION_METHOD = FeatureDetector.ORB;
    private static int DESCRIPTOR_EXTRACTOR_METHOD = DescriptorExtractor.ORB;
    private static int MAX_FEATURES = 10;

    public static MorphConfiguration getConfiguration(Bitmap bitmap1, Bitmap bitmap2) {
        //Gets a Mat from the Bitmap of the image 1
        Mat img1 = new Mat();
        Bitmap bit1 = bitmap1.copy(Bitmap.Config.RGB_565, true);
        Utils.bitmapToMat(bit1, img1);
        Imgproc.cvtColor(img1, img1, Imgproc.COLOR_RGBA2RGB);

        //Gets a Mat from the Bitmap of the image 2
        Mat img2 = new Mat();
        Bitmap bit2 = bitmap2.copy(Bitmap.Config.RGB_565, true);
        Utils.bitmapToMat(bit2, img2);
        Imgproc.cvtColor(img2, img2, Imgproc.COLOR_RGBA2RGB);

        //Detects the keypoints of the two images
        Mat greyImage=new Mat();
        Mat greyImageToMatch=new Mat();
        MatOfKeyPoint keyPoints=new MatOfKeyPoint();
        MatOfKeyPoint keyPointsToMatch=new MatOfKeyPoint();
        Imgproc.cvtColor(img1, greyImage, Imgproc.COLOR_RGB2GRAY);
        Imgproc.cvtColor(img2, greyImageToMatch, Imgproc.COLOR_RGB2GRAY);
        FeatureDetector detector=FeatureDetector.create(FEATURE_DETECTION_METHOD);
        detector.detect(greyImage, keyPoints);
        detector.detect(greyImageToMatch, keyPointsToMatch);

        //Computes the descriptors of the keypoints
        DescriptorExtractor dExtractor = DescriptorExtractor.create(DESCRIPTOR_EXTRACTOR_METHOD);
        Mat descriptors=new Mat();
        Mat descriptorsToMatch=new Mat();
        dExtractor.compute(greyImage, keyPoints, descriptors);
        dExtractor.compute(greyImageToMatch, keyPointsToMatch, descriptorsToMatch);
        List<KeyPoint> keyPointsList = keyPoints.toList();
        List<KeyPoint> keyPointsList2 = keyPointsToMatch.toList();

        //Performs features matching
        DescriptorMatcher matcher = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE);
        MatOfDMatch matches=new MatOfDMatch();
        matcher.match(descriptorsToMatch,descriptors,matches);
        ArrayList<DMatch> goodMatches=new ArrayList<DMatch>();
        List<DMatch> allMatches=matches.toList();
        Collections.sort(allMatches, (match2, match1) -> {
            float distance = match2.distance - match1.distance;
            return (int) (distance);
        });
        for (int i = 0; i < MAX_FEATURES; i++ ) {
            if (i == allMatches.size()) {
                break;
            }
            goodMatches.add(allMatches.get(i));
        }
        MatOfDMatch goodEnough=new MatOfDMatch();
        goodEnough.fromList(goodMatches);

        //Loops the matches, and adds the correct points to the lists of points
        ArrayList<Point> points1 = new ArrayList<>();
        ArrayList<Point> points2 = new ArrayList<>();
        for (DMatch goodMatch: goodMatches) {
            KeyPoint kp1 = keyPointsList.get(goodMatch.trainIdx);
            KeyPoint kp2 = keyPointsList2.get(goodMatch.queryIdx);
            Point p1 = kp1.pt;
            Point p2 = kp2.pt;
            double distance = Math.sqrt((p2.y - p1.y) * (p2.y - p1.y) + (p2.x - p1.x) * (p2.x - p1.x));
            if (distance < ( (double) bitmap1.getWidth() / 3.0 )) {
                points1.add(p1);
                points2.add(p2);
            }
        }

        //Adds some points to points1 (on the edges) to increase precision
        points1.add(new Point(0,0));
        points1.add(new Point(img1.cols() - 1.0,0));
        points1.add(new Point(img1.cols() - 1.0, img1.rows() - 1.0));
        points1.add(new Point(0, img1.rows() - 1.0));

        //Adds some points to points2 (on the edges) to increase precision
        points2.add(new Point(0,0));
        points2.add(new Point(img2.cols() - 1.0,0));
        points2.add(new Point(img2.cols() - 1.0, img2.rows() - 1.0));
        points2.add(new Point(0, img2.rows() - 1.0));

        return new MorphConfiguration(points1, points2);
    }
}
