package com.example.sensordatadisplay;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;
import java.util.Arrays;
import java.util.Stack;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import static java.lang.Math.sqrt;

public class GLRender implements GLSurfaceView.Renderer{
    private Cube mCube1;
    private Cube mCube2;

    private Lines mLine1;
    private Lines mLine2;
    private Lines mLine3;

    private Lines mBoundary;

    private Lines mBody;

    private Sphere mSphere;

    private Stack<float[]> mStack;

    private float[] vPMatrix = new float[16];
    private final float[] projectionMatrix = new float[16];
    private final float[] viewMatrix = new float[16];
    private float [] rotationMatrix = new float[16];

    private static float[] invertedViewProjectMatrix = new float[16];

    private float[] Line1Matrix1 = new float[16];
    private float[] Line2Matrix1 = new float[16];
    private float[] Line3Matrix1 = new float[16];
    private float[] Line1Matrix2 = new float[16];
    private float[] Line2Matrix2 = new float[16];
    private float[] Line3Matrix2 = new float[16];

    private float[] TempMatrix = new float[16];

    private float[] Cube1Matrix1 = new float[16];
    private float[] Cube2Matrix1 = new float[16];
    private float[] Cube3Matrix1 = new float[16];
    private float[] Cube4Matrix1 = new float[16];

    private float[] Cube1Matrix2 = new float[16];
    private float[] Cube2Matrix2 = new float[16];
    private float[] Cube3Matrix2 = new float[16];
    private float[] Cube4Matrix2 = new float[16];

    private float[] SphereMatrix1 = new float[16];
    private float[] SphereMatrix2 = new float[16];
    private float[] SphereMatrix3 = new float[16];
    private float[] SphereMatrix4 = new float[16];
    private float[] SphereMatrix5 = new float[16];
    private float[] SphereMatrix6 = new float[16];
    private float[] SphereMatrix7 = new float[16];
    private float[] SphereMatrix8 = new float[16];

    final float cubePositions1[] = {
            1f,-0.5f,0.2f,
            1f,-0.5f,-0.2f,
            1f,0.5f,-0.2f,
            1f,0.5f,0.2f,
            0f,-0.5f,0.2f,
            0f,-0.5f,-0.2f,
            0f,0.5f,-0.2f,
            0f,0.5f,0.2f
    };

    final float cubePositions2[] = {
            0f,-0.5f,0.2f,
            0f,-0.5f,-0.2f,
            0f,0.5f,-0.2f,
            0f,0.5f,0.2f,
            -1f,-0.5f,0.2f,
            -1f,-0.5f,-0.2f,
            -1f,0.5f,-0.2f,
            -1f,0.5f,0.2f
    };

    private float bodyPosition[] = {1.2f, 9, 0,
            1.2f, 6, 0,
            -3.8f, 6, 0,
            -1.8f, 6, 0,
            -1.8f, 6, 0,
            1.2f, 6, 0,
            1.2f, 6, 0,
            4.2f, 6, 0,
            4.2f, 6, 0,
            6.2f, 6, 0,
            1.2f, 6, 0,
            1.2f, 3, 0,
            2.4f, 3, 0,
            2.4f, 0, 0,
            2.4f, 0, 0,
            2.4f, -3, 0

    };

    private float boundaryPosition[] = {
            1f,-0.5f,0.2f,
            1f,-0.5f,-0.2f,
            1f,-0.5f,-0.2f,
            1f,0.5f,-0.2f,
            1f,0.5f,-0.2f,
            1f,0.5f,0.2f,
            1f,0.5f,0.2f,
            1f,-0.5f,0.2f,
            -1f,0.5f,0.2f,
            -1f,0.5f,-0.2f,
            -1f,0.5f,-0.2f,
            -1f,-0.5f,-0.2f,
            -1f,-0.5f,-0.2f,
            -1f,-0.5f,0.2f,
            -1f,-0.5f,0.2f,
            -1f,0.5f,0.2f,
            1f,0.5f,0.2f,
            -1f,0.5f,0.2f,
            1f,0.5f,-0.2f,
            -1f,0.5f,-0.2f,
            1f,-0.5f,0.2f,
            -1f,-0.5f,0.2f,
            1f,-0.5f,-0.2f,
            -1f,-0.5f,-0.2f
    };

    float color1[] = {0f,0f,0f,1f,
            0f,0f,0f,1f,
            0f,0f,0f,1f,
            0f,0f,0f,1f,
            0f,0f,0f,1f,
            0f,0f,0f,1f,
            0f,0f,0f,1f,
            0f,0f,0f,1f
    };

    float color2[] = {1f,0f,0f,1f,
            1f,0f,0f,1f,
            1f,0f,0f,1f,
            1f,0f,0f,1f,
            1f,0f,0f,1f,
            1f,0f,0f,1f,
            1f,0f,0f,1f,
            1f,0f,0f,1f
    };

    private float[] linePosition1 = {
            2.4f,  3.0f, 0.0f,
            0.0f, 3.0f, 0.0f


    };

    private float[] linePosition2 = {
            0.0f,  0.0f, 0.0f,
            0.0f, -3.0f, 0.0f
    };

    private float[] linePosition3 = {
            0.0f, 0.0f, 0.0f,
            0.0f, -3.0f, 0.0f
    };

    float LineColor[] = new float[]
            {
                    0f,0f,1f,1f,
                    0f,0f,1f,1f,
            };

    float BoundaryColor[] = new float[] {
            0f, 1f, 1f, 1f
    };

    private static boolean Cube1Pressed = false;
    private static boolean Cube2Pressed = false;
    private static boolean Cube3Pressed = false;
    private static boolean Cube4Pressed = false;
    private static Geometry.Point Cube1Position, Cube2Position, Cube3Position, Cube4Position;

    private static int CurrentClick = 0;

    private static float w1 = 0;
    private static float w2 = 0;
    private static float w3 = 0;
    private static float w4 = 0;
    private static float x1 = 0;
    private static float x2 = 0;
    private static float x3 = 0;
    private static float x4 = 0;
    private static float y1 = 0;
    private static float y2 = 0;
    private static float y3 = 0;
    private static float y4 = 0;
    private static float z1 = 0;
    private static float z2 = 0;
    private static float z3 = 0;
    private static float z4 = 0;

    private float[] UpdatePosition1 = {0, 3, 0, 1};
    private float[] UpdatePosition2 = {0, 6, 0, 1};
    private float[] TempPosition = new float[4];
    private float[] UpdateCube3Position = new float[4];
    private float[] UpdateCube4Position = new float[4];

    public static volatile float angleZ;

    public void setAngleZ(float z) {
        angleZ = z;
    }

    public static volatile float angleX;

    public void setAngleX(float x) {angleX = x;}

    public static volatile float angleY;

    public void setAngleY(float y) {angleY = y;}

    public static volatile float W;
    public void setW(float w) {W = w;}

    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        // Set the background frame color
        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);

        GLES20.glEnable(GLES20.GL_DEPTH_TEST);

        mStack = new Stack<>();

        mCube1 = new Cube(cubePositions1, color2);
        mCube2 = new Cube(cubePositions2, color1);

        mLine1 = new Lines(linePosition1);
        mLine2 = new Lines(linePosition2);
        mLine3 = new Lines(linePosition3);

        mBoundary = new Lines(boundaryPosition);

        mSphere = new Sphere();

        mBody = new Lines(bodyPosition);

        Cube1Position = new Geometry.Point(2.4f, -3f, 0f);
        Cube2Position = new Geometry.Point(0f, -3f, 0f);
        Cube3Position = new Geometry.Point(0f, 0f, 0f);
        Cube4Position = new Geometry.Point(0f, 3f, 0f);
    }

    //draw 3D model and create animation
    public void onDrawFrame(GL10 unused) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        float [] strach = new float[16];

        // Set the camera position (View matrix)
        Matrix.setLookAtM(viewMatrix, 0, 0f, 0f, 30f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

        // Calculate the projection and view transformation
        Matrix.multiplyMM(vPMatrix, 0, projectionMatrix, 0, viewMatrix, 0);
        Matrix.setIdentityM(invertedViewProjectMatrix,0);
        Matrix.invertM(invertedViewProjectMatrix, 0, vPMatrix, 0);

        mStack.push(Arrays.copyOf(vPMatrix, 16));
        Line1Matrix1 = vPMatrix;
        Line1Matrix2 = vPMatrix;
        vPMatrix = mStack.pop();

        mStack.push(Arrays.copyOf(vPMatrix, 16));
        Matrix.translateM(vPMatrix, 0, 0 ,3, 0);
        Line2Matrix1 = vPMatrix;
        Line2Matrix2 = vPMatrix;
        vPMatrix = mStack.pop();

        mStack.push(Arrays.copyOf(vPMatrix, 16));
        Line3Matrix1 = vPMatrix;
        Line3Matrix2 = vPMatrix;
        vPMatrix = mStack.pop();

        mStack.push(Arrays.copyOf(vPMatrix, 16));
        Matrix.translateM(vPMatrix, 0, 2.4f, 3, 0);
        Cube1Matrix1 = vPMatrix;
        Cube1Matrix2 = vPMatrix;
        vPMatrix = mStack.pop();

        mStack.push(Arrays.copyOf(vPMatrix, 16));
        Matrix.translateM(vPMatrix, 0, 0, 3, 0);
        Cube2Matrix1 = vPMatrix;
        Cube2Matrix2 = vPMatrix;
        vPMatrix = mStack.pop();

        mStack.push(Arrays.copyOf(vPMatrix, 16));
        Cube3Matrix1 = vPMatrix;
        Cube3Matrix2 = vPMatrix;
        vPMatrix = mStack.pop();

        mStack.push(Arrays.copyOf(vPMatrix, 16));
        Matrix.translateM(vPMatrix, 0, 0, -3, 0);
        Cube4Matrix1 = vPMatrix;
        Cube4Matrix2 = vPMatrix;
        vPMatrix = mStack.pop();

        mStack.push(Arrays.copyOf(vPMatrix, 16));
        Matrix.translateM(vPMatrix, 0, 1.2f, 9, 0);
        SphereMatrix1 = vPMatrix;
        mSphere.drawSphere(SphereMatrix1);
        vPMatrix = mStack.pop();

        mStack.push(Arrays.copyOf(vPMatrix, 16));
        Matrix.translateM(vPMatrix, 0, 1.2f, 6, 0);
        SphereMatrix2 = vPMatrix;
        mSphere.drawSphere(SphereMatrix2);
        vPMatrix = mStack.pop();

        mStack.push(Arrays.copyOf(vPMatrix, 16));
        Matrix.translateM(vPMatrix, 0, 4.2f, 6, 0);
        SphereMatrix3 = vPMatrix;
        mSphere.drawSphere(SphereMatrix3);
        vPMatrix = mStack.pop();

        mStack.push(Arrays.copyOf(vPMatrix, 16));
        Matrix.translateM(vPMatrix, 0, 6.2f, 6, 0);
        SphereMatrix4 = vPMatrix;
        mSphere.drawSphere(SphereMatrix4);
        vPMatrix = mStack.pop();

        mStack.push(Arrays.copyOf(vPMatrix, 16));
        Matrix.translateM(vPMatrix, 0, -1.8f, 6, 0);
        SphereMatrix5 = vPMatrix;
        mSphere.drawSphere(SphereMatrix5);
        vPMatrix = mStack.pop();

        mStack.push(Arrays.copyOf(vPMatrix, 16));
        Matrix.translateM(vPMatrix, 0, -3.8f, 6, 0);
        SphereMatrix6 = vPMatrix;
        mSphere.drawSphere(SphereMatrix6);
        vPMatrix = mStack.pop();

        mStack.push(Arrays.copyOf(vPMatrix, 16));
        Matrix.translateM(vPMatrix, 0, 2.4f, 0, 0);
        SphereMatrix7 = vPMatrix;
        mSphere.drawSphere(SphereMatrix7);
        vPMatrix = mStack.pop();

        mStack.push(Arrays.copyOf(vPMatrix, 16));
        Matrix.translateM(vPMatrix, 0, 2.4f, -3, 0);
        SphereMatrix8 = vPMatrix;
        mSphere.drawSphere(SphereMatrix8);
        vPMatrix = mStack.pop();

        mBody.drawBody(vPMatrix, LineColor);

        if (Cube1Pressed == false && Cube2Pressed == false && Cube3Pressed == false && Cube4Pressed == false && CurrentClick == 0){
            mLine1.drawLines(Line1Matrix2, LineColor);

            mLine2.drawLines(Line2Matrix2, LineColor);

            mLine3.drawLines(Line3Matrix2, LineColor);

            mCube1.draw(Cube1Matrix2);
            mCube2.draw(Cube1Matrix2);

            mCube1.draw(Cube2Matrix2);
            mCube2.draw(Cube2Matrix2);

            mCube1.draw(Cube3Matrix2);
            mCube2.draw(Cube3Matrix2);

            mCube1.draw(Cube4Matrix2);
            mCube2.draw(Cube4Matrix2);
        }

        else if (Cube1Pressed == false && Cube2Pressed == false && Cube3Pressed == false && Cube4Pressed == false && CurrentClick != 0){
            mLine1.drawLines(Line1Matrix2, LineColor);

            rotation(rotationMatrix, w2, -y2, x2, -z2);
            Matrix.multiplyMM(TempMatrix, 0, Line2Matrix1, 0, rotationMatrix, 0);
            Line2Matrix2 = TempMatrix;
            mLine2.drawLines(Line2Matrix2, LineColor);

            Matrix.translateM(TempMatrix, 0, 0 ,-3, 0);
            rotation(rotationMatrix, w3, -y3, x3, -z3);
            Matrix.multiplyMM(Line3Matrix2, 0, TempMatrix, 0, rotationMatrix, 0);
            mLine3.drawLines(Line3Matrix2, LineColor);

            rotation(rotationMatrix, w1, -y1, x1, -z1);
            Matrix.multiplyMM(TempMatrix, 0, Cube1Matrix1, 0, rotationMatrix, 0);
            Cube1Matrix2 = TempMatrix;
            mCube1.draw(Cube1Matrix2);
            mCube2.draw(Cube1Matrix2);

            rotation(rotationMatrix, w2, -y2, x2, -z2);
            Matrix.multiplyMM(TempMatrix, 0, Cube2Matrix1, 0, rotationMatrix, 0);
            Cube2Matrix2 = TempMatrix;
            mCube1.draw(Cube2Matrix2);
            mCube2.draw(Cube2Matrix2);

            Matrix.translateM(Cube2Matrix2, 0, 0 , -3, 0);
            rotation(rotationMatrix, w3, -y3, x3, -z3);
            Matrix.multiplyMM(Cube3Matrix2, 0, Cube2Matrix2, 0, rotationMatrix, 0);
            Matrix.translateM(Cube2Matrix2, 0,0, 3, 0);
            mCube1.draw(Cube3Matrix2);
            mCube2.draw(Cube3Matrix2);

            Matrix.translateM(Cube3Matrix2, 0, 0, -3, 0);
            rotation(rotationMatrix, w4, -y4, x4, -z4);
            Matrix.multiplyMM(Cube4Matrix2, 0, Cube3Matrix2, 0, rotationMatrix, 0);
            Matrix.translateM(Cube3Matrix2, 0, 0, 3, 0);
            mCube1.draw(Cube4Matrix2);
            mCube2.draw(Cube4Matrix2);
        }

        if (Cube1Pressed){
            mLine1.drawLines(Line1Matrix2, LineColor);

            rotation(rotationMatrix, w2, -y2, x2, -z2);
            Matrix.multiplyMM(TempMatrix, 0, Line2Matrix1, 0, rotationMatrix, 0);
            Line2Matrix2 = TempMatrix;
            mLine2.drawLines(Line2Matrix2, LineColor);

            Matrix.translateM(TempMatrix, 0, 0 ,-3, 0);
            rotation(rotationMatrix, w3, -y3, x3, -z3);
            Matrix.multiplyMM(Line3Matrix2, 0, TempMatrix, 0, rotationMatrix, 0);
            mLine3.drawLines(Line3Matrix2, LineColor);

            rotation(rotationMatrix, W, -angleY, angleX, -angleZ);
            Matrix.multiplyMM(TempMatrix, 0, Cube1Matrix1, 0, rotationMatrix, 0);
            Cube1Matrix2 = TempMatrix;
            mCube1.draw(Cube1Matrix2);
            mCube2.draw(Cube1Matrix2);
            mBoundary.drawBoundary(Cube1Matrix2, BoundaryColor);

            rotation(rotationMatrix, w2, -y2, x2, -z2);
            Matrix.multiplyMM(TempMatrix, 0, Cube2Matrix1, 0, rotationMatrix, 0);
            Cube2Matrix2 = TempMatrix;
            mCube1.draw(Cube2Matrix2);
            mCube2.draw(Cube2Matrix2);

            Matrix.translateM(Cube2Matrix2, 0, 0 , -3, 0);
            rotation(rotationMatrix, w3, -y3, x3, -z3);
            Matrix.multiplyMM(Cube3Matrix2, 0, Cube2Matrix2, 0, rotationMatrix, 0);
            Matrix.translateM(Cube2Matrix2, 0,0, 3, 0);
            mCube1.draw(Cube3Matrix2);
            mCube2.draw(Cube3Matrix2);

            Matrix.translateM(Cube3Matrix2, 0, 0, -3, 0);
            rotation(rotationMatrix, w4, -y4, x4, -z4);
            Matrix.multiplyMM(Cube4Matrix2, 0, Cube3Matrix2, 0, rotationMatrix, 0);
            Matrix.translateM(Cube3Matrix2, 0, 0, 3, 0);
            mCube1.draw(Cube4Matrix2);
            mCube2.draw(Cube4Matrix2);

            w1 = W;
            x1 = angleX;
            y1 = angleY;
            z1 = angleZ;
        }
        else if (Cube2Pressed){
            mLine1.drawLines(Line1Matrix2, LineColor);

            rotation(rotationMatrix, W, -angleY, angleX, -angleZ);
            Matrix.multiplyMM(TempMatrix, 0, Line2Matrix1, 0, rotationMatrix, 0);
            Line2Matrix2 = TempMatrix;
            mLine2.drawLines(Line2Matrix2, LineColor);

            Matrix.translateM(TempMatrix, 0, 0 ,-3, 0);
            rotation(rotationMatrix, w3, -y3, x3, -z3);
            Matrix.multiplyMM(Line3Matrix2, 0, TempMatrix, 0, rotationMatrix, 0);
            mLine3.drawLines(Line3Matrix2, LineColor);

            rotation(rotationMatrix, w1, -y1, x1, -z1);
            Matrix.multiplyMM(TempMatrix, 0, Cube1Matrix1, 0, rotationMatrix, 0);
            Cube1Matrix2 = TempMatrix;
            mCube1.draw(Cube1Matrix2);
            mCube2.draw(Cube1Matrix2);

            rotation(rotationMatrix, W, -angleY, angleX, -angleZ);
            Matrix.multiplyMM(TempMatrix, 0, Cube2Matrix1, 0, rotationMatrix, 0);
            Cube2Matrix2 = TempMatrix;
            mCube1.draw(Cube2Matrix2);
            mCube2.draw(Cube2Matrix2);
            mBoundary.drawBoundary(Cube2Matrix2, BoundaryColor);

            rotation(rotationMatrix, W, -angleY, angleX, -angleZ);
            Matrix.multiplyMV(UpdateCube3Position, 0, rotationMatrix, 0, UpdatePosition1, 0);
            Matrix.setIdentityM(strach, 0);
            Matrix.translateM(strach, 0, 0, -3, 0);
            Matrix.multiplyMV(UpdateCube3Position, 0, strach, 0, UpdateCube3Position, 0);
            Cube3Position = new Geometry.Point(-UpdateCube3Position[0], UpdateCube3Position[1], -UpdateCube3Position[2]);

            Matrix.translateM(Cube2Matrix2, 0, 0 , -3, 0);
            rotation(rotationMatrix, w3, -y3, x3, -z3);
            Matrix.multiplyMM(Cube3Matrix2, 0, Cube2Matrix2, 0, rotationMatrix, 0);
            Matrix.translateM(Cube2Matrix2, 0,0, 3, 0);
            mCube1.draw(Cube3Matrix2);
            mCube2.draw(Cube3Matrix2);

            Matrix.translateM(Cube3Matrix2, 0, 0, -3, 0);
            rotation(rotationMatrix, w4, -y4, x4, -z4);
            Matrix.multiplyMM(Cube4Matrix2, 0, Cube3Matrix2, 0, rotationMatrix, 0);
            Matrix.translateM(Cube3Matrix2, 0, 0, 3, 0);
            mCube1.draw(Cube4Matrix2);
            mCube2.draw(Cube4Matrix2);


            if (w3 == 0 && y3 == 0 && x3 == 0 && z3 == 0){
                Matrix.setIdentityM(strach, 0);
                rotation(rotationMatrix, W, -angleY, angleX, -angleZ);
                Matrix.multiplyMV(UpdateCube4Position, 0, rotationMatrix, 0, UpdatePosition2, 0);
                Matrix.setIdentityM(strach, 0);
                Matrix.translateM(strach, 0, 0, -3, 0);
                Matrix.multiplyMV(UpdateCube4Position, 0, strach, 0, UpdateCube4Position, 0);
                Cube4Position = new Geometry.Point(-UpdateCube4Position[0], UpdateCube4Position[1], -UpdateCube4Position[2]);
            }
            else {
                //rotation(rotationMatrix, W, -angleY, angleX, -angleZ);
                //Matrix.multiplyMV(TempPosition, 0, rotationMatrix, 0, UpdatePosition1, 0);
                Matrix.setIdentityM(strach, 0);
                Matrix.translateM(strach, 0, 0, 3, 0);
                Matrix.multiplyMV(TempPosition, 0, strach, 0, UpdateCube3Position, 0);
                rotation(rotationMatrix, w3, -y3, x3, -z3);
                Matrix.multiplyMV(UpdateCube4Position, 0, rotationMatrix, 0, TempPosition, 0);
                Matrix.setIdentityM(strach, 0);
                Matrix.translateM(strach, 0, UpdateCube3Position[0], UpdateCube3Position[1], UpdateCube3Position[2]);
                Matrix.multiplyMV(UpdateCube4Position, 0, strach, 0, UpdateCube4Position, 0);
                Cube4Position = new Geometry.Point(-UpdateCube4Position[0], UpdateCube4Position[1], -UpdateCube4Position[2]);
            }

            w2 = W;
            x2 = angleX;
            y2 = angleY;
            z2 = angleZ;
        }

        else if (Cube3Pressed){
            mLine1.drawLines(Line1Matrix2, LineColor);

            rotation(rotationMatrix, w2, -y2, x2, -z2);
            Matrix.multiplyMM(TempMatrix, 0, Line2Matrix1, 0, rotationMatrix, 0);
            Line2Matrix2 = TempMatrix;
            mLine2.drawLines(Line2Matrix2, LineColor);

            Matrix.translateM(TempMatrix, 0, 0 ,-3, 0);
            rotation(rotationMatrix, W, -angleY, angleX, -angleZ);
            Matrix.multiplyMM(Line3Matrix2, 0, TempMatrix, 0, rotationMatrix, 0);
            mLine3.drawLines(Line3Matrix2, LineColor);

            rotation(rotationMatrix, w1, -y1, x1, -z1);
            Matrix.multiplyMM(TempMatrix, 0, Cube1Matrix1, 0, rotationMatrix, 0);
            Cube1Matrix2 = TempMatrix;
            mCube1.draw(Cube1Matrix2);
            mCube2.draw(Cube1Matrix2);

            rotation(rotationMatrix, w2, -y2, x2, -z2);
            Matrix.multiplyMM(TempMatrix, 0, Cube2Matrix1, 0, rotationMatrix, 0);
            Cube2Matrix2 = TempMatrix;
            mCube1.draw(Cube2Matrix2);
            mCube2.draw(Cube2Matrix2);


            Matrix.translateM(Cube2Matrix2, 0, 0, -3, 0);
            rotation(rotationMatrix, W, -angleY, angleX, -angleZ);
            Matrix.multiplyMM(Cube3Matrix2, 0, Cube2Matrix2, 0, rotationMatrix, 0);
            Matrix.translateM(Cube2Matrix2, 0, 0, 3, 0);
            mCube1.draw(Cube3Matrix2);
            mCube2.draw(Cube3Matrix2);
            mBoundary.drawBoundary(Cube3Matrix2, BoundaryColor);

            Matrix.translateM(Cube3Matrix2, 0, 0, -3, 0);
            rotation(rotationMatrix, w4, -y4, x4, -z4);
            Matrix.multiplyMM(Cube4Matrix2, 0, Cube3Matrix2, 0, rotationMatrix, 0);
            Matrix.translateM(Cube3Matrix2, 0, 0, 3, 0);
            mCube1.draw(Cube4Matrix2);
            mCube2.draw(Cube4Matrix2);

            if (w2 == 0 && y2 == 0 && x2 == 0 && z2 == 0) {
                rotation(rotationMatrix, W, -angleY, angleX, -angleZ);
                Matrix.multiplyMV(UpdateCube4Position, 0, rotationMatrix, 0, UpdatePosition1, 0);
                Cube4Position = new Geometry.Point(-UpdateCube4Position[0], UpdateCube4Position[1], -UpdateCube4Position[2]);
            }
            else {
                //rotation(rotationMatrix, w2, -y2, x2, -z2);
                //Matrix.multiplyMV(TempPosition, 0, rotationMatrix, 0, UpdatePosition1, 0);
                Matrix.setIdentityM(strach, 0);
                Matrix.translateM(strach, 0, 0, 3, 0);
                Matrix.multiplyMV(TempPosition, 0, strach, 0, UpdateCube3Position, 0);
                rotation(rotationMatrix, W, -angleY, angleX, -angleZ);
                Matrix.multiplyMV(UpdateCube4Position, 0, rotationMatrix, 0, TempPosition, 0);
                Matrix.setIdentityM(strach, 0);
                Matrix.translateM(strach, 0, UpdateCube3Position[0], UpdateCube3Position[1], UpdateCube3Position[2]);
                Matrix.multiplyMV(UpdateCube4Position, 0, strach, 0, UpdateCube4Position, 0);
                Cube4Position = new Geometry.Point(-UpdateCube4Position[0], UpdateCube4Position[1], -UpdateCube4Position[2]);
            }

            w3 = W;
            x3 = angleX;
            y3 = angleY;
            z3 = angleZ;
        }
        else if (Cube4Pressed) {
            mLine1.drawLines(Line1Matrix2, LineColor);

            rotation(rotationMatrix, w2, -y2, x2, -z2);
            Matrix.multiplyMM(TempMatrix, 0, Line2Matrix1, 0, rotationMatrix, 0);
            Line2Matrix2 = TempMatrix;
            mLine2.drawLines(Line2Matrix2, LineColor);

            Matrix.translateM(TempMatrix, 0, 0 ,-3, 0);
            rotation(rotationMatrix, w3, -y3, x3, -z3);
            Matrix.multiplyMM(Line3Matrix2, 0, TempMatrix, 0, rotationMatrix, 0);
            mLine3.drawLines(Line3Matrix2, LineColor);

            rotation(rotationMatrix, w1, -y1, x1, -z1);
            Matrix.multiplyMM(TempMatrix, 0, Cube1Matrix1, 0, rotationMatrix, 0);
            Cube1Matrix2 = TempMatrix;
            mCube1.draw(Cube1Matrix2);
            mCube2.draw(Cube1Matrix2);


            rotation(rotationMatrix, w2, -y2, x2, -z2);
            Matrix.multiplyMM(TempMatrix, 0, Cube2Matrix1, 0, rotationMatrix, 0);
            Cube2Matrix2 = TempMatrix;
            mCube1.draw(Cube2Matrix2);
            mCube2.draw(Cube2Matrix2);

            Matrix.translateM(Cube2Matrix2, 0, 0 , -3, 0);
            rotation(rotationMatrix, w3, -y3, x3, -z3);
            Matrix.multiplyMM(Cube3Matrix2, 0, Cube2Matrix2, 0, rotationMatrix, 0);
            Matrix.translateM(Cube2Matrix2, 0,0, 3, 0);
            mCube1.draw(Cube3Matrix2);
            mCube2.draw(Cube3Matrix2);

            Matrix.translateM(Cube3Matrix2, 0, 0, -3, 0);
            rotation(rotationMatrix, W, -angleY, angleX, -angleZ);
            Matrix.multiplyMM(Cube4Matrix2, 0, Cube3Matrix2, 0, rotationMatrix, 0);
            Matrix.translateM(Cube3Matrix2, 0, 0, 3, 0);
            mCube1.draw(Cube4Matrix2);
            mCube2.draw(Cube4Matrix2);
            mBoundary.drawBoundary(Cube4Matrix2, BoundaryColor);

            w4 = W;
            x4 = angleX;
            y4 = angleY;
            z4 = angleZ;
        }
    }

    public void onSurfaceChanged(GL10 unused, int width, int height) {
        GLES20.glViewport(0, 0, width, height);

        float ratio = (float) width / height;

        // this projection matrix is applied to object coordinates
        // in the onDrawFrame() method
        Matrix.perspectiveM(projectionMatrix, 0, 50, (float) width / height, 1f, 50f);
    }
    //convert quaternion to matrix
    public void rotation(float [] matrix, float w, float x, float y, float z) {
        matrix[0]  = (1.0f - (2.0f * ((y * y) + (z * z))));
        matrix[1]  = (2.0f * ((x * y) - (z * w)));
        matrix[2]  = (2.0f * ((x * z) + (y * w)));
        matrix[3] = 0.0f;
        matrix[4]  = (2.0f * ((x * y) + (z * w)));
        matrix[5]  = (1.0f - (2.0f * ((x * x) + (z * z))));
        matrix[6]  = (2.0f * ((y * z) - (x * w)));
        matrix[7] = 0.0f;
        matrix[8]  = (2.0f * ((x * z) - (y * w)));
        matrix[9]  = (2.0f * ((y * z) + (x * w)));
        matrix[10] = (1.0f - (2.0f * ((x * x) + (y * y))));
        matrix[11] = 0.0f;
        matrix[12]  = 0.0f;
        matrix[13]  = 0.0f;
        matrix[14] = 0.0f;
        matrix[15] = 1.0f;
    }
    //select cube
    public static void handleTouchPress(float normalizedX, float normalizedY) {
        Geometry.Ray ray = covertNormalized2DPointToRay(normalizedX, normalizedY);
        Geometry.Sphere Cube1BoundingSphere = new Geometry.Sphere(new Geometry.Point(Cube1Position.x, Cube1Position.y, Cube1Position.z),
                (float)sqrt(0.5f * 0.5f + 0.2f * 0.2f + 1f * 1f));
        Geometry.Sphere Cube2BoundingSphere = new Geometry.Sphere(new Geometry.Point(Cube2Position.x, Cube2Position.y, Cube2Position.z),
                (float)sqrt(0.5f * 0.5f + 0.2f * 0.2f + 1f * 1f));
        Geometry.Sphere Cube3BoundingSphere = new Geometry.Sphere(new Geometry.Point(Cube3Position.x, Cube3Position.y, Cube3Position.z),
                (float)sqrt(0.5f * 0.5f + 0.2f * 0.2f + 1f * 1f));
        Geometry.Sphere Cube4BoundingSphere = new Geometry.Sphere(new Geometry.Point(Cube4Position.x, Cube4Position.y, Cube4Position.z),
                (float)sqrt(0.5f * 0.5f + 0.2f * 0.2f + 1f * 1f));
        Cube1Pressed = Geometry.intersects(Cube1BoundingSphere, ray);
        Cube2Pressed = Geometry.intersects(Cube2BoundingSphere, ray);
        Cube3Pressed = Geometry.intersects(Cube3BoundingSphere, ray);
        Cube4Pressed = Geometry.intersects(Cube4BoundingSphere, ray);
        if (Cube1Pressed){
            CurrentClick = 1;
        }
        if (Cube2Pressed){
            CurrentClick = 2;
        }
        if (Cube3Pressed){
            CurrentClick = 3;
        }
        if (Cube4Pressed){
            CurrentClick = 4;
        }
    }
    //convert 2d point to Ray
    private static Geometry.Ray covertNormalized2DPointToRay(float normalizedX, float normalizedY) {
        final float[] nearPointNdc = {normalizedX, normalizedY, -1, 1};
        final float[] farPointNdc = {normalizedX, normalizedY, 1, 1};

        final float[] nearPointWorld = new float[4];
        final float[] farPointWorld = new float[4];
        Matrix.multiplyMV(nearPointWorld, 0, invertedViewProjectMatrix, 0, nearPointNdc, 0);
        Matrix.multiplyMV(farPointWorld, 0, invertedViewProjectMatrix, 0, farPointNdc, 0);

        divideByW(nearPointWorld);
        divideByW(farPointWorld);

        Geometry.Point nearPointRay = new Geometry.Point(nearPointWorld[0], nearPointWorld[1], nearPointWorld[2]);
        Geometry.Point farPointRay = new Geometry.Point(farPointWorld[0], farPointWorld[1], farPointWorld[2]);

        return new Geometry.Ray(nearPointRay, Geometry.vectorBetween(nearPointRay, farPointRay));
    }

    private static void divideByW(float[] vector) {
        vector [0] /= vector [3];
        vector [1] /= vector [3];
        vector [2] /= vector [3];
    }

    public static int loadShader(int type, String shaderCode){

        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }
}
