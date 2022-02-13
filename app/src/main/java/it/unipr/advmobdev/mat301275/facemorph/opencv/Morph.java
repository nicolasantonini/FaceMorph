package it.unipr.advmobdev.mat301275.facemorph.opencv;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.util.Log;

import com.google.android.gms.common.util.IOUtils;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.DMatch;
import org.opencv.core.KeyPoint;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfFloat6;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.FeatureDetector;
import org.opencv.features2d.Features2d;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Subdiv2D;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import it.unipr.advmobdev.mat301275.facemorph.R;

import static org.opencv.core.Core.BORDER_REFLECT_101;
import static org.opencv.imgcodecs.Imgcodecs.CV_LOAD_IMAGE_UNCHANGED;
import static org.opencv.imgcodecs.Imgcodecs.imdecode;

public class Morph {

    public static void applyAffineTransform(Mat warpImage, Mat src, MatOfPoint2f srcTri, MatOfPoint2f dstTri) {
        Mat warpMat = Imgproc.getAffineTransform(srcTri, dstTri);
        Mat tmpWarpMat = new Mat(3, 3, CvType.CV_64FC1);
        tmpWarpMat.put(0,0,warpMat.get(0,0));
        tmpWarpMat.put(0,1,warpMat.get(0,1));
        tmpWarpMat.put(0,2,warpMat.get(0,2));
        tmpWarpMat.put(1,0,warpMat.get(1,0));
        tmpWarpMat.put(1,1,warpMat.get(1,1));
        tmpWarpMat.put(1,2,warpMat.get(1,2));
        tmpWarpMat.put(2,0, 0.0);
        tmpWarpMat.put(2,1, 0.0);
        tmpWarpMat.put(2,2, 1.0);
        Imgproc.warpPerspective(src, warpImage, tmpWarpMat, warpImage.size(), Imgproc.INTER_LINEAR, BORDER_REFLECT_101);
    }

    public static void morphTriangle(Mat img1, Mat img2, Mat img, List<Point> t1, List<Point> t2, List<Point> t, double alpha) {
        MatOfPoint tMat = new MatOfPoint();
        tMat.fromList(t);

        MatOfPoint t1Mat = new MatOfPoint();
        t1Mat.fromList(t1);

        MatOfPoint t2Mat = new MatOfPoint();
        t2Mat.fromList(t2);

        Rect r = Imgproc.boundingRect(tMat);
        Rect r1 = Imgproc.boundingRect(t1Mat);
        Rect r2 = Imgproc.boundingRect(t2Mat);

        ArrayList<Point> t1Rect = new ArrayList<>();
        ArrayList<Point> t2Rect = new ArrayList<>();
        ArrayList<Point> tRect = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            tRect.add(new Point(t.get(i).x - r.x, t.get(i).y - r.y));
            t1Rect.add(new Point(t1.get(i).x - r1.x, t1.get(i).y - r1.y));
            t2Rect.add(new Point(t2.get(i).x - r2.x, t2.get(i).y - r2.y));
        }

        Mat mask = new Mat(r.height, r.width, CvType.CV_32FC3, Scalar.all(0.0));
        MatOfPoint tRectMat = new MatOfPoint();
        tRectMat.fromList(tRect);
        Imgproc.fillConvexPoly(mask, tRectMat, new Scalar(1.0, 1.0, 1.0), 16, 0);
        Mat img1Rect = new Mat(img1,r1);
        Mat img2Rect = new Mat(img2,r2);
        Mat warpImage1 = new Mat(r.height, r.width, img1Rect.type(), Scalar.all(0.0));
        Mat warpImage2 = new Mat(r.height, r.width, img2Rect.type(), Scalar.all(0.0));
        MatOfPoint2f t1RectMat = new MatOfPoint2f();
        t1RectMat.fromList(t1Rect);
        MatOfPoint2f t2RectMat = new MatOfPoint2f();
        t2RectMat.fromList(t2Rect);
        MatOfPoint2f tRectMatF = new MatOfPoint2f();
        tRectMatF.fromList(tRect);
        applyAffineTransform(warpImage1, img1Rect, t1RectMat, tRectMatF);
        applyAffineTransform(warpImage2, img2Rect, t2RectMat, tRectMatF);
        Mat imgRect = new Mat(warpImage1.rows(), warpImage1.cols(), warpImage1.type(), Scalar.all(0));
        Mat imgTemp = new Mat(warpImage1.rows(), warpImage1.cols(), warpImage1.type(), Scalar.all(0));
        Core.multiply(warpImage1, new Scalar(1.0 - alpha, 1.0 - alpha, 1.0 - alpha), imgRect);
        Core.multiply(warpImage2, new Scalar(alpha, alpha, alpha), imgTemp);
        Core.add(imgRect, imgTemp, imgRect);
        Core.multiply(imgRect, mask, imgRect);
        Mat imgTemp2 = new Mat(mask.rows(), mask.cols(), mask.type(), Scalar.all(0));
        Mat allones = new Mat(mask.rows(), mask.cols(), mask.type(), Scalar.all(1.0));
        Core.subtract(allones, mask, imgTemp2);
        Core.bitwise_or(img.submat(r), imgRect, img.submat(r));
    }

    public static Bitmap morph(MorphConfiguration configuration, Bitmap bitmap1, Bitmap bitmap2, double alpha, int triangles) {
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

        //Creates the output image
        img1.convertTo(img1, CvType.CV_32F);
        img2.convertTo(img2, CvType.CV_32F);
        Mat imgMorph = new Mat(img1.size(), CvType.CV_32FC3, Scalar.all(0.0));

        //Retrieves the points of the two images from the configuration
        ArrayList<Point> points1 = configuration.points1;
        ArrayList<Point> points2 = configuration.points2;

        //Calcolates the points on the final image
        ArrayList<Point> points = new ArrayList<>();
        for (int i = 0; i < points1.size(); i++) {
            double x;
            double y;
            x = (1.0 - alpha) * points1.get(i).x + alpha * points2.get(i).x;
            y = (1.0 - alpha) * points1.get(i).y + alpha * points2.get(i).y;
            points.add(new Point(x, y));
        }

        //Gets the Delaunay triangulation of the points
        ArrayList<Triangle> trianglesAuto = Triangulation.calcDelaunayTriangles(new Rect(0,0,img1.width(), img1.height()), points);

        int iteration = 0;

        //Loops the triangles and adds every patch to the final image
        for (Triangle triangle : trianglesAuto) {

            ArrayList<Point> t1 = new ArrayList<>();
            ArrayList<Point> t2 = new ArrayList<>();
            ArrayList<Point> t = new ArrayList<>();

            t1.add(points1.get(triangle.x));
            t1.add(points1.get(triangle.y));
            t1.add(points1.get(triangle.z));

            t2.add(points2.get(triangle.x));
            t2.add(points2.get(triangle.y));
            t2.add(points2.get(triangle.z));

            t.add(points.get(triangle.x));
            t.add(points.get(triangle.y));
            t.add(points.get(triangle.z));

            morphTriangle(img1, img2, imgMorph, t1, t2, t, alpha);

            if (iteration >= ( ( triangles / 100.0) * (double) trianglesAuto.size() - 1.0)) {
                break;
            } else {
                iteration++;
            }

        }

        /* RETURNS AN IMAGE TO THE APP */

        imgMorph.convertTo(imgMorph, CvType.CV_8U);
        final Bitmap bitmap = Bitmap.createBitmap(img2.cols(), img2.rows(), Bitmap.Config.RGB_565);
        Utils.matToBitmap(imgMorph, bitmap);
        return bitmap;

    }

}
