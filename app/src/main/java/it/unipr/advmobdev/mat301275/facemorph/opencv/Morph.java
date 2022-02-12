package it.unipr.advmobdev.mat301275.facemorph.opencv;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.util.Log;

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

    public static void applyAffineTransform(Mat warpImage, Mat src, MatOfPoint2f srcTri, MatOfPoint2f dstTri) {
        Mat warpMat = Imgproc.getAffineTransform(srcTri, dstTri);
        Imgproc.warpPerspective(src, warpImage, warpMat, warpImage.size(), Imgproc.INTER_LINEAR, BORDER_REFLECT_101);
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

        Mat mask = new Mat(r.height, r.width, CvType.CV_32FC3);

        MatOfPoint tRectMat = new MatOfPoint();
        tRectMat.fromList(tRect);

        Imgproc.fillConvexPoly(mask, tRectMat, new Scalar(1.0, 1.0, 1.0), 16, 0);

        Mat img1Rect = new Mat(img1,r1);
        Mat img2Rect = new Mat(img2,r2);

        Mat warpImage1 = new Mat(r.height, r.width, img1Rect.type());
        Mat warpImage2 = new Mat(r.height, r.width, img2Rect.type());

        MatOfPoint2f t1RectMat = new MatOfPoint2f();
        t1RectMat.fromList(t1Rect);

        MatOfPoint2f t2RectMat = new MatOfPoint2f();
        t2RectMat.fromList(t2Rect);

        MatOfPoint2f tRectMatF = new MatOfPoint2f();
        tRectMatF.fromList(tRect);

        applyAffineTransform(warpImage1, img1Rect, t1RectMat, tRectMatF);
        applyAffineTransform(warpImage2, img2Rect, t2RectMat, tRectMatF);

        Mat imgRect = new Mat(warpImage1.rows(), warpImage1.cols(), warpImage1.type());

        Mat imgTemp = new Mat(warpImage1.rows(), warpImage1.cols(), warpImage1.type());

        Core.multiply(warpImage1, new Scalar(1.0 - alpha), imgRect);
        Core.multiply(warpImage2, new Scalar(alpha), imgTemp);
        Core.add(imgRect, imgTemp, imgRect);

        Core.multiply(imgRect, mask, imgRect);

        Mat imgTemp2 = new Mat(mask.rows(), mask.cols(), mask.type());
        Core.subtract(mask, new Scalar(1.0), imgTemp2); // !!!!!

        Core.multiply(img.submat(r), imgTemp2, img.submat(r));

        Core.add(img.submat(r), imgRect, img.submat(r));

    }

    public static Bitmap getMorph(double alpha, Context context) throws IOException {

        InputStream is = context.getResources().openRawResource(R.raw.donald_trump);
        int nRead;
        byte[] data = new byte[16 * 1024];
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        while ((nRead = is.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }
        byte[] bytes = buffer.toByteArray();

        Mat mat = imdecode(new MatOfByte(bytes), CV_LOAD_IMAGE_UNCHANGED);
        Imgproc.cvtColor(mat, mat, Imgproc.COLOR_BGR2RGB);
        //Imgcodecs imgcodecs = new Imgcodecs();
        //Mat m = imgcodecs.imread("ted_cruz.jpg");
        //Log.i("Ok", "ok");
        final Bitmap bitmap = Bitmap.createBitmap(mat.cols(), mat.rows(), Bitmap.Config.RGB_565);
        Utils.matToBitmap(mat, bitmap);
        return bitmap;
    }

}
