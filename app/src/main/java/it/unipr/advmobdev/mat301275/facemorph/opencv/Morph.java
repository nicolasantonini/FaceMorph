package it.unipr.advmobdev.mat301275.facemorph.opencv;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.util.Log;

import com.google.android.gms.common.util.IOUtils;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import it.unipr.advmobdev.mat301275.facemorph.R;

import static org.opencv.core.Core.BORDER_REFLECT_101;
import static org.opencv.imgcodecs.Imgcodecs.CV_LOAD_IMAGE_UNCHANGED;
import static org.opencv.imgcodecs.Imgcodecs.imdecode;

public class Morph {

    public static ArrayList<Point> readPoints1(Context context) {
        InputStream istri = context.getResources().openRawResource(R.raw.hillary_clinton_jpg);
        BufferedReader br = new BufferedReader(new InputStreamReader(istri));
        String readLine = null;

        ArrayList<Point> points = new ArrayList<>();

        try {
            while ((readLine = br.readLine()) != null) {
                String[] splitted = readLine.split("\\s+");
                Point point = new Point();
                point.x = Integer.parseInt(splitted[0]);
                point.y = Integer.parseInt(splitted[1]);
                points.add(point);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return points;
    }

    public static ArrayList<Point> readPoints2(Context context) {
        InputStream istri = context.getResources().openRawResource(R.raw.ted_cruz_jpg);
        BufferedReader br = new BufferedReader(new InputStreamReader(istri));
        String readLine = null;

        ArrayList<Point> points = new ArrayList<>();

        try {
            while ((readLine = br.readLine()) != null) {
                String[] splitted = readLine.split("\\s+");
                Point point = new Point();
                point.x = Integer.parseInt(splitted[0]);
                point.y = Integer.parseInt(splitted[1]);
                points.add(point);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return points;
    }

    public static ArrayList<Triangle> readTri(Context context) {

        InputStream istri = context.getResources().openRawResource(R.raw.tri);
        BufferedReader br = new BufferedReader(new InputStreamReader(istri));
        String readLine = null;

        ArrayList<Triangle> triangles = new ArrayList<>();

        try {
            while ((readLine = br.readLine()) != null) {
                String[] splitted = readLine.split("\\s+");
                Triangle tri = new Triangle();
                tri.x = Integer.parseInt(splitted[0]);
                tri.y = Integer.parseInt(splitted[1]);
                tri.z = Integer.parseInt(splitted[2]);
                triangles.add(tri);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return triangles;
    }

    //OK!
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

        ArrayList<Point> tRectInt;

        for (int i = 0; i < 3; i++) {
            tRect.add(new Point(t.get(i).x - r.x, t.get(i).y - r.y));
            t1Rect.add(new Point(t1.get(i).x - r1.x, t1.get(i).y - r1.y));
            t2Rect.add(new Point(t2.get(i).x - r2.x, t2.get(i).y - r2.y));
        }

        Mat mask = new Mat(r.height, r.width, CvType.CV_32FC3, Scalar.all(0.0));

        MatOfPoint tRectMat = new MatOfPoint();
        tRectMat.fromList(tRect);

        Log.i("CIAONE_MASK_BERFORE", mask.get(0, 0)[0] + " " + mask.get(0, 0)[1] + " " + mask.get(0, 0)[2]);

        Imgproc.fillConvexPoly(mask, tRectMat, new Scalar(1.0, 1.0, 1.0), 16, 0);

        Log.i("CIAONE_MASK_AFTER", mask.get(0, 0)[0] + " " + mask.get(0, 0)[1] + " " + mask.get(0, 0)[2]);

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

        //Log.i("Pixello", String.valueOf(warpImage1.get(0,0)[0]) + " " + String.valueOf(warpImage1.get(0,0)[1]) + " " + String.valueOf(warpImage1.get(0,0)[2]));
        //Log.i("Pixello", String.valueOf(warpImage2.get(0,0)[0]) + " " + String.valueOf(warpImage2.get(0,0)[1]) + " " + String.valueOf(warpImage2.get(0,0)[2]));

        Mat imgRect = new Mat(warpImage1.rows(), warpImage1.cols(), warpImage1.type(), Scalar.all(0));
        Mat imgTemp = new Mat(warpImage1.rows(), warpImage1.cols(), warpImage1.type(), Scalar.all(0));

        //Log.i("Pixello", String.valueOf(warpImage1.get(0,0)[0]) + " " + String.valueOf(warpImage1.get(0,0)[1]) + " " + String.valueOf(warpImage1.get(0,0)[2]));

        Core.multiply(warpImage1, new Scalar(1.0 - alpha, 1.0 - alpha, 1.0 - alpha), imgRect);

        Log.i("CIAONE_001", warpImage1.get(0, 0)[0] + " " + warpImage1.get(0, 0)[1] + " " + warpImage1.get(0, 0)[2]);

        Core.multiply(warpImage2, new Scalar(alpha, alpha, alpha), imgTemp);

        Log.i("CIAONE_002", warpImage2.get(0, 0)[0] + " " + warpImage2.get(0, 0)[1] + " " + warpImage2.get(0, 0)[2]);

        Core.add(imgRect, imgTemp, imgRect);

        Log.i("CIAONE_003", imgRect.get(0, 0)[0] + " " + imgRect.get(0, 0)[1] + " " + imgRect.get(0, 0)[2]);


        //Log.i("Pixello", String.valueOf(imgRect.get(0,0)[0]) + " " + String.valueOf(imgRect.get(0,0)[1]) + " " + String.valueOf(imgRect.get(0,0)[2]));

        Core.multiply(imgRect, mask, imgRect);

        Log.i("CIAONE_004", imgRect.get(0, 0)[0] + " " + imgRect.get(0, 0)[1] + " " + imgRect.get(0, 0)[2]);

        if (imgRect.get(0, 0)[0] == 0.0) {
            Log.i("CIAONE_KO", "ERRORE");
        }

        Mat imgTemp2 = new Mat(mask.rows(), mask.cols(), mask.type(), Scalar.all(0));

        Log.i("CIAONE_005", imgTemp2.get(0, 0)[0] + " " + imgTemp2.get(0, 0)[1] + " " + imgTemp2.get(0, 0)[2]);

        Mat allones = new Mat(mask.rows(), mask.cols(), mask.type(), Scalar.all(1.0));

        Core.subtract(allones, mask, imgTemp2);
        //Core.subtract(mask, new Scalar(255.0, 255.0, 255.0), imgTemp2); // !!!!!

        Log.i("CIAONE_006", imgTemp2.get(0, 0)[0] + " " + imgTemp2.get(0, 0)[1] + " " + imgTemp2.get(0, 0)[2]);

        //Core.multiply(img.submat(r), mask, img.submat(r));

        Log.i("CIAONE_007", img.submat(r).get(0, 0)[0] + " " + img.submat(r).get(0, 0)[1] + " " + img.submat(r).get(0, 0)[2]);


        Core.bitwise_or(img.submat(r), imgRect, img.submat(r));
        //Core.add(img.submat(r), imgRect, img.submat(r));
        Log.i("CIAONE_008", img.submat(r).get(0, 0)[0] + " " + img.submat(r).get(0, 0)[1] + " " + img.submat(r).get(0, 0)[2]);


    }

    public static void getMorph(double alpha, Context context, ProgressCallback callback) throws IOException {

        /* OPENS THE TWO INPUT MATS */
        InputStream is = context.getResources().openRawResource(R.raw.hillary_clinton);
        int nRead;
        byte[] data = new byte[16 * 1024];
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        while ((nRead = is.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }
        byte[] bytes = buffer.toByteArray();
        Mat img1 = imdecode(new MatOfByte(bytes), CV_LOAD_IMAGE_UNCHANGED);
        Imgproc.cvtColor(img1, img1, Imgproc.COLOR_BGR2RGB);

        InputStream is2 = context.getResources().openRawResource(R.raw.ted_cruz);
        int nRead2;
        byte[] data2 = new byte[16 * 1024];
        ByteArrayOutputStream buffer2 = new ByteArrayOutputStream();
        while ((nRead2 = is2.read(data2, 0, data2.length)) != -1) {
            buffer2.write(data2, 0, nRead2);
        }
        byte[] bytes2 = buffer2.toByteArray();
        Mat img2 = imdecode(new MatOfByte(bytes2), CV_LOAD_IMAGE_UNCHANGED);
        Imgproc.cvtColor(img2, img2, Imgproc.COLOR_BGR2RGB);
        /* ******************** */

        img1.convertTo(img1, CvType.CV_32F);
        img2.convertTo(img2, CvType.CV_32F);

        Mat imgMorph = new Mat(img1.size(), CvType.CV_32FC3, Scalar.all(0.0));

        ArrayList<Point> points1 = readPoints1(context);
        ArrayList<Point> points2 = readPoints2(context);

        ArrayList<Point> points = new ArrayList<>();

        for (int i = 0; i < points1.size(); i++) {
            double x;
            double y;
            x = (1.0 - alpha) * points1.get(i).x + alpha * points2.get(i).x;
            y = (1.0 - alpha) * points1.get(i).y + alpha * points2.get(i).y;

            points.add(new Point(x, y));
        }

        ArrayList<Triangle> triangles = readTri(context);

        int iteration = 0;
        for (Triangle triangle : triangles) {
            Log.i("Triangles", String.valueOf(triangle.x));

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

            if (iteration == callback.getIterations()) {
                break;
            } else {
                iteration++;
            }

        }

        /* RETURNS AN IMAGE TO THE APP */

        imgMorph.convertTo(imgMorph, CvType.CV_8U);


        //img2.convertTo(img2, CvType.CV_8U);

        final Bitmap bitmap = Bitmap.createBitmap(img2.cols(), img2.rows(), Bitmap.Config.RGB_565);
        Utils.matToBitmap(imgMorph, bitmap);
        callback.imageCalcolated(bitmap);
    }

}
