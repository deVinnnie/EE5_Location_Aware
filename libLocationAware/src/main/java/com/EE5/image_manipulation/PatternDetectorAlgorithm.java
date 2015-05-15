package com.EE5.image_manipulation;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.RotatedRect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * The official implementation of the algorithm.
 */
public class PatternDetectorAlgorithm implements PatternDetectorAlgorithmInterface{
    public double ra;
    public int ii;
    int distance;
    public int distance2;
    boolean setupflag;

    private Scalar orange = new Scalar(255, 120, 0);
    private Scalar light_blue = new Scalar(0, 255, 255);
    private Scalar dark_blue = new Scalar(0, 120, 255);
    private Scalar light_green = new Scalar(0, 255, 0);

    private Mat mIntermediateMat = new Mat();

    public PatternDetectorAlgorithm(){
        distance2 = 0;
        setupflag = false;
        ii = 0;
    }


    /**
     * Do magic!
     *
     * <img src="./doc-files/PatternDetection_01.png" alt="Pattern Detection 01"/>
     *
     * The figure below shows the order in which the corner points of the pattern are defined.
     * Point 1 is near the inner square. Then moving in the clockwise direction you find point 2, 3 and 4.
     *
     * <img src="./doc-files/Pattern_Point_Naming_Convention.png" alt="Naming Convention for Points of Pattern"/>
     *
     * (Uses bitwise operators instead of logical operators. Bitwise is marginally faster.)
     *
     * @param rgba Color image
     * @param gray2 Grayscale image
     * @return Corner points and angle of the pattern.
     *         Careful x and y axis are corrected to be compatible with Calc.java convention!!!!!!
     */
    public PatternCoordinator find(Mat rgba, Mat gray2) {
        double extra_angle = 0;

        long startTime = System.currentTimeMillis();
        //Imgproc.resize(gray2,gray2,process_size);

        List<MatOfPoint> contour = new ArrayList<MatOfPoint>();
        Mat hierarchys = new Mat();

        Imgproc.threshold(gray2, mIntermediateMat, 80, 255, Imgproc.THRESH_BINARY); //Apply tresholding, result is stored in 'mIntermediateMat'
        //Imgproc.threshold(gray2, mIntermediateMat, 80, 255, Imgproc.THRESH_OTSU);
        //Imgproc.Canny(mIntermediateMat, mIntermediateMat, 80, 90);                           //mIntermediateMat is a Mat format variable in field


        //Let OpenCV find contours, the result of this operation is stored in 'contour'.
        Imgproc.findContours(mIntermediateMat, contour, hierarchys, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
        List<MatOfPoint> con_in_range;
        List<MatOfPoint> squareContours;
        List<MatOfPoint> pContour;

        MatOfPoint squ_in  = new MatOfPoint();
        MatOfPoint squ_out = new MatOfPoint();

        if(setupflag == false)
        {
            //Filter contours with the wrong size.
            con_in_range = getContoursBySize(distance2, contour);
            //Filter out non square contours.
            squareContours = getContoursSquare2(con_in_range);
            //Find the right contour for the pattern.
            pContour = findPattern(squareContours);
            Imgproc.drawContours(rgba, squareContours, -1, orange, 4);
            /** tot hier werkt het goed **/
            if (pContour.size() == 2) {
                squ_out = pContour.get(1);
                squ_in = pContour.get(0);
                RotatedRect rot_re_out = new RotatedRect();
                RotatedRect rot_re_in = new RotatedRect();
                MatOfPoint2f mp2f_out = new MatOfPoint2f();
                MatOfPoint2f mp2f_in = new MatOfPoint2f();
                mp2f_in = new MatOfPoint2f(squ_in.toArray());
                mp2f_out = new MatOfPoint2f(squ_out.toArray());
                rot_re_out = Imgproc.minAreaRect(mp2f_out);
                rot_re_in = Imgproc.minAreaRect(mp2f_in);

                double size_out = rot_re_out.size.area();
                double size_in = rot_re_in.size.area();
                ra = size_out/size_in;
                if((ra>4)&&(ra<16)){
                    ii++;
                    if(ii == 3) {
                        setupflag = true;
                        distance2 = distance2 + 4;
                        ii = 0;
                    }
                }
                else{
                    setupflag = false;
                }
            }

            if(distance2 > 50){
                distance2 = 0;
            }
            distance2 = distance2 + 2;
            PatternCoordinator pc = new PatternCoordinator(
                    //Pattern in center.
                    new Point(320-50,240-50),
                    new Point(320+50,240-50),
                    new Point(320-50,240+50),
                    new Point(320+50,240+50),
                    0.00
            );
            return pc;
        }
        else {
            con_in_range = getContoursBySize(distance2, contour);
            squareContours = getContoursSquare2(con_in_range);
            pContour = findPattern(squareContours);

            Point innerCenter = new Point();
            Point outterCenter = new Point();

            RotatedRect NewMtx1 = new RotatedRect();
            RotatedRect NewMtx2 = new RotatedRect();

            MatOfPoint2f appo = new MatOfPoint2f();
            MatOfPoint2f appo2 = new MatOfPoint2f();
            if (pContour.size() == 2) {
                appo = new MatOfPoint2f(pContour.get(0).toArray());
                NewMtx1 = Imgproc.minAreaRect(appo);
                innerCenter = NewMtx1.center;
                appo2 = new MatOfPoint2f(pContour.get(1).toArray());
                NewMtx2 = Imgproc.minAreaRect(appo2);
                outterCenter = NewMtx2.center;
            }
            else{
                //setupflag = false;
            }

            List<MatOfPoint> appro_con = new ArrayList<MatOfPoint>();
            appro_con.add(new MatOfPoint(appo.toArray()));

            //Imgproc.cvtColor(mIntermediateMat, rgba, Imgproc.COLOR_GRAY2BGRA, 4); //Convert to rgba;
            //When this line is commented the following commands will draw on the original rgba image.

            //Overlay the image with some useful lines.
            //Imgproc.drawContours(image, contour, -1, orange, 4);
            //Imgproc.drawContours(image,outterContours,-1,new Scalar(120,120,255),4);
            Imgproc.drawContours(rgba, squareContours, -1, orange, 4);
            //Original:Imgproc.drawContours(image, pContour, -1, light_green, 4);
            //Imgproc.drawContours(image, pContour, -1, light_green, 2);
            //Imgproc.drawContours(image, pContour, 0, light_green, 2);
            //Core.line(image, innerCenter, outterCenter, orange, 2);

            //Outer
            Point a = new Point(NewMtx2.boundingRect().x, NewMtx2.boundingRect().y);
            Point b = new Point(NewMtx2.boundingRect().x + NewMtx2.boundingRect().width, NewMtx2.boundingRect().y + NewMtx2.boundingRect().height);
            Core.rectangle(rgba, a, b, dark_blue, 3);

            Point out[] = new Point[4];
            //Point out_send[];// = new Point[4];

            NewMtx2.points(out);

            PatternCoordinator out_send = Cal_Pointnum(out, innerCenter);

            if(out_send.getNum2() !=  null) {
                String out_point1 = "1";//"point 1 is ("+ String.valueOf(out[0].x)+","+ String.valueOf(out[0].y)+")";
                String out_point2 = "2";//"point 2 is ("+ String.valueOf(out[1].x)+","+ String.valueOf(out[1].y)+")";
                String out_point3 = "3";//"point 3 is ("+ String.valueOf(out[2].x)+","+ String.valueOf(out[2].y)+")";
                String out_point4 = "4";//"point 4 is ("+ String.valueOf(out[3].x)+","+ String.valueOf(out[3].y)+")";
                Core.putText(rgba, out_point1, out_send.getNum1(), Core.FONT_HERSHEY_SIMPLEX, 1, light_blue);
                Core.putText(rgba, out_point2, out_send.getNum2(), Core.FONT_HERSHEY_SIMPLEX, 1, light_blue);
                Core.putText(rgba, out_point3, out_send.getNum3(), Core.FONT_HERSHEY_SIMPLEX, 1, light_blue);
                Core.putText(rgba, out_point4, out_send.getNum4(), Core.FONT_HERSHEY_SIMPLEX, 1, light_blue);
                //Core.putText(image, String.valueOf(out_send.getAngle()),new Point(50, 300) , Core.FONT_HERSHEY_SIMPLEX, 1, light_blue);
            }

            //Inner
            Point a2 = new Point(NewMtx1.boundingRect().x, NewMtx1.boundingRect().y);
            Point b2 = new Point(NewMtx1.boundingRect().x + NewMtx1.boundingRect().width, NewMtx1.boundingRect().y + NewMtx1.boundingRect().height);
            Core.rectangle(rgba, a2, b2, light_green,3);

            double x1, x2, y1, y2;
            x1 = innerCenter.x;
            x2 = outterCenter.x;
            y1 = innerCenter.y;
            y2 = outterCenter.y;

            double k = (y2 - y1) / (x1 - x2);

            if ((k < -1) | (k > 1)) {
                if (y2 >= y1) {
                    extra_angle = 180;
                } else {
                    extra_angle = 0;
                }
            } else if ((-1 < k) | (k < 1)) {
                if (x1 >= x2) {
                    extra_angle = 270;
                } else {
                    extra_angle = 90;
                }
            }

            double finalangle =NewMtx2.angle+90+extra_angle;

            String kk = "k is (" + String.valueOf(k) + ")";
            Core.putText(rgba, kk, new Point(50, 400), Core.FONT_HERSHEY_SIMPLEX, 1, light_blue);
            String dis = "the distance2 is "+ String.valueOf(distance2) +" )";
            Core.putText(rgba, dis, new Point(50, 350), Core.FONT_HERSHEY_SIMPLEX, 1, light_blue);
            String angle = "rotate angle is (" + String.valueOf(finalangle) + ")";
            Core.putText(rgba, angle, new Point(50, 450), Core.FONT_HERSHEY_SIMPLEX, 1, light_blue);

            //Core.putText(image, String.valueOf(pContour.size()), new Point(50, 550), Core.FONT_HERSHEY_SIMPLEX, 1, light_blue);
            //Core.putText(image, String.valueOf(innerCenter.x)+" "+innerCenter.y, new Point(50, 600), Core.FONT_HERSHEY_SIMPLEX, 1, light_blue);
            //Core.putText(image, String.valueOf(outterCenter.x)+" "+outterCenter.y, new Point(50, 650), Core.FONT_HERSHEY_SIMPLEX, 1, light_blue);

            long stopTime = System.currentTimeMillis();
            String sss = String.valueOf(stopTime - startTime);
            //Log.i(TAG2, "spend_time for one frame = " + sss + " ms");
            //Log.i(TAG,"The coordinator is = " + sss);

            //PatternCoordinator pc = new PatternCoordinator(out_send[0],out_send[1],out_send[2],out_send[3],finalangle);
            //PatternCoordinator pc = new PatternCoordinator(out[0],out[1],out[2],out[3],finalangle);
            PatternCoordinator pc = new PatternCoordinator(new Point(),new Point(),new Point(),new Point(),0.0);
            if(out_send.getNum2() !=  null) {
                pc = new PatternCoordinator(out_send.getNum1(), out_send.getNum2(), out_send.getNum3(), out_send.getNum4(), finalangle);
            }

            PatternCoordinator flipped = new PatternCoordinator(
                    new Point(pc.getNum1().y, pc.getNum1().x),
                    new Point(pc.getNum2().y, pc.getNum2().x),
                    new Point(pc.getNum3().y, pc.getNum3().x),
                    new Point(pc.getNum4().y, pc.getNum4().x),
                    0.0
            );
            return flipped;
        }

    }

    private PatternCoordinator Cal_Pointnum(Point[] point, Point in_center){
        Point[] point_send = new Point[4];
        double dis=0;
        double min_dis=10000000;

        for (int i = 0;i<4;i++){
            dis = Math.sqrt(Math.pow(in_center.x - point[i].x,2)+Math.pow(in_center.y-point[i].y,2));
            if(dis<min_dis){
                min_dis = dis;
                if(i == 0){
                    point_send[0] = point[0];
                    point_send[1] = point[1];
                    point_send[2] = point[2];
                    point_send[3] = point[3];
                };
                if(i == 1) {
                    point_send[0] = point[1];
                    point_send[1] = point[2];
                    point_send[2] = point[3];
                    point_send[3] = point[0];
                }
                if(i == 2) {
                    point_send[0] = point[2];
                    point_send[1] = point[3];
                    point_send[2] = point[0];
                    point_send[3] = point[1];
                }
                if(i == 3) {
                    point_send[0] = point[3];
                    point_send[1] = point[0];
                    point_send[2] = point[1];
                    point_send[3] = point[2];
                }
            }
        }
        PatternCoordinator pc = new PatternCoordinator(point_send[0],point_send[1],point_send[2],point_send[3],dis);
        return pc;
    }

    private List<MatOfPoint> getContoursBySize(int dis, List<MatOfPoint> contour) {
        List<MatOfPoint> con_in_range = new ArrayList<MatOfPoint>();
        //filter out contours out of certain range
        Iterator<MatOfPoint> each = contour.iterator();
        while (each.hasNext()) {
            MatOfPoint contours = each.next();
            if ((Imgproc.contourArea(contours) < dis * 600) & (Imgproc.contourArea(contours) > dis * 10)) {
                con_in_range.add(contours);
            }
        }
        return con_in_range;
    }

    private List<MatOfPoint> getContoursSquare2(List<MatOfPoint> con_in_range) {
        List<MatOfPoint> squareContours = new ArrayList<MatOfPoint>();
        List<Point> cl = new ArrayList<Point>();
        Iterator<Point> con;
        Iterator<MatOfPoint> each_con = con_in_range.iterator();
        Rect out_rect = new Rect();

        while(each_con.hasNext()){
            MatOfPoint contours = each_con.next();
            out_rect = Imgproc.boundingRect(contours);

            if((Math.abs(out_rect.height - out_rect.width) < 10)){
                    //&(3>(out_rect.area()/Imgproc.contourArea(contours)))
                    //&(0.75<(out_rect.area()/Imgproc.contourArea(contours)))){
                squareContours.add(contours);
            }
        }
        return squareContours;
    }

    private List<MatOfPoint> getContoursSquare(List<MatOfPoint> con_in_range){
        Point point_right = new Point();
        Point point_left = new Point();
        Point point_top = new Point();
        Point point_bottom = new Point();

        double p_x_max = 0;
        double p_x_min = 0;
        double p_y_max = 0;
        double p_y_min = 0;

        List<MatOfPoint> squareContours = new ArrayList<MatOfPoint>();
        List<Point> cl = new ArrayList<Point>();
        Iterator<Point> con;
        Iterator<MatOfPoint> each_con = con_in_range.iterator();

        while(each_con.hasNext()){
            MatOfPoint contours = each_con.next();
            cl = contours.toList();             //transform contour to points of list

            con = cl.iterator();
            int i = 0,j = 0;
            while(con.hasNext()){
                Point p = con.next();
                if(i == 0){
                    point_bottom = p;
                    point_top = p;
                    point_right = p;
                    point_left = p;
                }
                else {
                    if (p.x >= point_right.x) {
                        point_right = p;
                        //p_x_max = p.x;
                        if (p.y <= point_right.y) {
                            point_right = p;
                        }
                    }
                    if (p.x <= point_left.x) {
                        point_left = p;
                        p_x_min = p.x;
                        if (p.y >= point_left.y) {
                            point_left = p;
                        }
                    }
                    if (p.y <= point_top.y) {
                        point_top = p;
                        p_y_max = p.y;
                        if (p.x >= point_top.x) {
                            point_top = p;
                        }
                    }
                    if (p.y >= point_bottom.y) {
                        point_bottom = p;
                        //p_y_min = p.y;
                        if (p.x <= point_bottom.x) {
                            point_bottom = p;
                        }
                    }
                }
                i++;
            }
            //point_top.x = p_y_min;

            //determine the shape of square
            double length1 = Math.sqrt(Math.pow(point_top.x-point_left.x,2)+Math.pow(point_top.y-point_left.y,2));
            double length2 = Math.sqrt(Math.pow(point_left.x-point_bottom.x,2)+Math.pow(point_left.y-point_bottom.y,2));
            double length3 = Math.sqrt(Math.pow(point_bottom.x-point_right.x,2)+Math.pow(point_bottom.y-point_right.y,2));
            double length4 = Math.sqrt(Math.pow(point_top.x-point_right.x,2)+Math.pow(point_top.y-point_right.y,2));
            double areadiff = (length2-length1)+(length4-length3)+(length3-length2)+(length4-length2);

            double slope1 = (point_top.y-point_left.y)/(point_top.x-point_left.x);
            double slope2 = (point_right.y-point_top.y)/(point_right.x-point_top.x);
            double slope3 = (point_bottom.y-point_right.y)/(point_bottom.x-point_right.x);
            double slope4 = (point_left.y-point_bottom.y)/(point_left.x-point_bottom.x);

            double an1 = slope1*slope2*slope3*slope4;

            //The product of slope is around 1, and the sum of length differences of each edge is low.
            if((0.6 <an1)&(an1<1.4)&(Math.abs(areadiff)<50)){
                squareContours.add(contours);
            }
        }
        return squareContours;
    }

    private MatOfPoint getContoursOutter(List<MatOfPoint> squareContour ){
        MatOfPoint outterCon = new MatOfPoint();
        MatOfPoint sqr = new MatOfPoint() ;

        Iterator<MatOfPoint> squareCon = squareContour.iterator();
        double maxSquareArea = 0;
        while(squareCon.hasNext()){
            sqr = squareCon.next();
            double SquareArea = Imgproc.contourArea(sqr);
            if(SquareArea > maxSquareArea) {
                outterCon = sqr;
            }
        }
        return outterCon;
    }

    private MatOfPoint getContoursInner(List<MatOfPoint> squareContour,MatOfPoint outterContours ){
        MatOfPoint innerCon = new MatOfPoint();
        double areaOut = Imgproc.contourArea(outterContours);
        Iterator<MatOfPoint> squareCon = squareContour.iterator();
        while (squareCon.hasNext()) {
            MatOfPoint sqr = squareCon.next();
            if ((Imgproc.contourArea(sqr) < 0.5*areaOut) & (Imgproc.contourArea(sqr) > 0.1*areaOut)) {
                innerCon = sqr;
            }
        }
        return innerCon;
    }

    private Point getShapeCenter(MatOfPoint shapeContour){
        Iterator<Point> con_point = shapeContour.toList().iterator();
        Point shapeCenter = new Point();
        double x_sum = 0;
        double y_sum = 0;
        int count = 0;
        while(con_point.hasNext()){
            Point pt = con_point.next();
            x_sum += pt.x;
            y_sum += pt.y;
            count++;
        }
        shapeCenter.x = x_sum/count;
        shapeCenter.y = y_sum/count;
        return shapeCenter;
    }

    private List<MatOfPoint> findPattern(List<MatOfPoint> contours){
        double distance = 1000000;
        List<MatOfPoint> patternContours = new ArrayList<MatOfPoint>();
        Iterator<MatOfPoint> iter1 = contours.iterator();
        while(iter1.hasNext()){
            MatOfPoint con1 = iter1.next();
            Point center1 = getShapeCenter(con1);
            Iterator<MatOfPoint> iter2 = contours.iterator();
            while(iter2.hasNext()){
                MatOfPoint con2 = iter2.next();
                Point center2 = getShapeCenter(con2);
                double dis_this = Math.abs(center1.x-center2.x)+Math.abs(center1.y-center2.y);
                if((dis_this < distance)&(dis_this>0)){//&(dis_this<70)){
                    distance = dis_this;
                    patternContours.clear();
                    patternContours.add(con1);
                    patternContours.add(con2);
                }
            }
        }
        return patternContours;
    }

    public int getDistance2(){
        return distance2;
    }
    public void setDistance(int dis){
        distance = dis;
    }
    public void setDistance2(int dis){
        distance2 = dis;
    }
    public void setSetupflag(boolean flag){
        setupflag = flag;
    }
}
