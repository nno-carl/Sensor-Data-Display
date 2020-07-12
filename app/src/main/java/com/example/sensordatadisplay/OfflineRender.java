package com.example.sensordatadisplay;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import java.util.Arrays;
import java.util.Stack;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class OfflineRender implements GLSurfaceView.Renderer{

    private Cube mCube1;
    private Cube mCube2;

    private Lines mLine1;
    private Lines mLine2;
    private Lines mLine3;

    private Lines mBody;

    private Sphere mSphere;

    private Stack<float[]> mStack;

    private float[] vPMatrix = new float[16];
    private final float[] projectionMatrix = new float[16];
    private final float[] viewMatrix = new float[16];
    private float [] rotationMatrix = new float[16];

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

    private float bodyPosition[] = {
            1.2f, 9, 0,
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

    public static volatile float Z;
    public void setZ(float z) { Z = z; }

    public static volatile float X;
    public void setX(float x) {X = x;}

    public static volatile float Y;
    public void setY(float y) {Y = y;}

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

        mSphere = new Sphere();

        mBody = new Lines(bodyPosition);
    }

    //draw 3D model and rotate 4 cubes together at the same time
    public void onDrawFrame(GL10 unused) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        Matrix.setLookAtM(viewMatrix, 0, 0f, 0f, 30f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

        // Calculate the projection and view transformation
        Matrix.multiplyMM(vPMatrix, 0, projectionMatrix, 0, viewMatrix, 0);

        rotation(rotationMatrix, W, -Y, X, -Z);

        mStack.push(Arrays.copyOf(vPMatrix, 16));
        mLine1.drawLines(vPMatrix, LineColor);
        vPMatrix = mStack.pop();

        mStack.push(Arrays.copyOf(vPMatrix, 16));
        Matrix.translateM(vPMatrix, 0, 0 ,3, 0);
        Matrix.multiplyMM(vPMatrix, 0, vPMatrix, 0, rotationMatrix, 0);
        mLine2.drawLines(vPMatrix, LineColor);
        vPMatrix = mStack.pop();

        mStack.push(Arrays.copyOf(vPMatrix, 16));
        Matrix.translateM(vPMatrix, 0, 0, 3, 0);
        Matrix.multiplyMM(vPMatrix, 0, vPMatrix, 0, rotationMatrix, 0);
        Matrix.translateM(vPMatrix, 0, 0, -3, 0);
        Matrix.multiplyMM(vPMatrix, 0, vPMatrix, 0, rotationMatrix, 0);
        mLine3.drawLines(vPMatrix, LineColor);
        vPMatrix = mStack.pop();

        mStack.push(Arrays.copyOf(vPMatrix, 16));
        Matrix.translateM(vPMatrix, 0, 2.4f, 3, 0);
        Matrix.multiplyMM(vPMatrix, 0, vPMatrix, 0, rotationMatrix, 0);
        mCube1.draw(vPMatrix);
        mCube2.draw(vPMatrix);
        vPMatrix = mStack.pop();

        mStack.push(Arrays.copyOf(vPMatrix, 16));
        Matrix.translateM(vPMatrix, 0, 0, 3, 0);
        Matrix.multiplyMM(vPMatrix, 0, vPMatrix, 0, rotationMatrix, 0);
        mCube1.draw(vPMatrix);
        mCube2.draw(vPMatrix);
        vPMatrix = mStack.pop();

        mStack.push(Arrays.copyOf(vPMatrix, 16));
        Matrix.translateM(vPMatrix, 0, 0, 3, 0);
        Matrix.multiplyMM(vPMatrix, 0, vPMatrix, 0, rotationMatrix, 0);
        Matrix.translateM(vPMatrix, 0, 0, -3, 0);
        Matrix.multiplyMM(vPMatrix, 0, vPMatrix, 0, rotationMatrix, 0);
        mCube1.draw(vPMatrix);
        mCube2.draw(vPMatrix);
        vPMatrix = mStack.pop();

        mStack.push(Arrays.copyOf(vPMatrix, 16));
        Matrix.translateM(vPMatrix, 0, 0, 3, 0);
        Matrix.multiplyMM(vPMatrix, 0, vPMatrix, 0, rotationMatrix, 0);
        Matrix.translateM(vPMatrix, 0, 0, -3, 0);
        Matrix.multiplyMM(vPMatrix, 0, vPMatrix, 0, rotationMatrix, 0);
        Matrix.translateM(vPMatrix, 0, 0, -3, 0);
        Matrix.multiplyMM(vPMatrix, 0, vPMatrix, 0, rotationMatrix, 0);
        mCube1.draw(vPMatrix);
        mCube2.draw(vPMatrix);
        vPMatrix = mStack.pop();

        mStack.push(Arrays.copyOf(vPMatrix, 16));
        Matrix.translateM(vPMatrix, 0, 1.2f, 9, 0);
        mSphere.drawSphere(vPMatrix);
        vPMatrix = mStack.pop();

        mStack.push(Arrays.copyOf(vPMatrix, 16));
        Matrix.translateM(vPMatrix, 0, 1.2f, 6, 0);
        mSphere.drawSphere(vPMatrix);
        vPMatrix = mStack.pop();

        mStack.push(Arrays.copyOf(vPMatrix, 16));
        Matrix.translateM(vPMatrix, 0, 4.2f, 6, 0);
        mSphere.drawSphere(vPMatrix);
        vPMatrix = mStack.pop();

        mStack.push(Arrays.copyOf(vPMatrix, 16));
        Matrix.translateM(vPMatrix, 0, 6.2f, 6, 0);
        mSphere.drawSphere(vPMatrix);
        vPMatrix = mStack.pop();

        mStack.push(Arrays.copyOf(vPMatrix, 16));
        Matrix.translateM(vPMatrix, 0, -1.8f, 6, 0);
        mSphere.drawSphere(vPMatrix);
        vPMatrix = mStack.pop();

        mStack.push(Arrays.copyOf(vPMatrix, 16));
        Matrix.translateM(vPMatrix, 0, -3.8f, 6, 0);
        mSphere.drawSphere(vPMatrix);
        vPMatrix = mStack.pop();

        mStack.push(Arrays.copyOf(vPMatrix, 16));
        Matrix.translateM(vPMatrix, 0, 2.4f, 0, 0);
        mSphere.drawSphere(vPMatrix);
        vPMatrix = mStack.pop();

        mStack.push(Arrays.copyOf(vPMatrix, 16));
        Matrix.translateM(vPMatrix, 0, 2.4f, -3, 0);
        mSphere.drawSphere(vPMatrix);
        vPMatrix = mStack.pop();

        mBody.drawBody(vPMatrix, LineColor);
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

}
