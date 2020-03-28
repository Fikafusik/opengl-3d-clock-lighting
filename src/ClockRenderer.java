
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;

import java.util.Calendar;

public class ClockRenderer implements GLEventListener {

    private Projection projection = Projection.ORTHO;

    private double rotationAngleXAxis = 30.0;
    private double rotationAngleYAxis = 20.0;
    private double rotationAngleZAxis = 0.0;

    private int triangulationDegree = 16;

    private boolean needToDrawAxis = true;

    public void setTriangulationDegree(int triangulationDegree) {
        this.triangulationDegree = triangulationDegree;
    }

    public void setRotationAngleXAxis(double rotationAngleXAxis) {
        this.rotationAngleXAxis = rotationAngleXAxis;
    }

    public void setRotationAngleYAxis(double rotationAngleYAxis) {
        this.rotationAngleYAxis = rotationAngleYAxis;
    }

    public void setRotationAngleZAxis(double rotationAngleZAxis) {
        this.rotationAngleZAxis = rotationAngleZAxis;
    }

    public void setNeedToDrawAxis(boolean needToDrawAxis) {
        this.needToDrawAxis = needToDrawAxis;
    }

    public void setProjection(Projection projection) {
        this.projection = projection;
    }

    @Override
    public void init(GLAutoDrawable glAutoDrawable) {
        GL2 gl = glAutoDrawable.getGL().getGL2();

        gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        gl.glEnable(GL.GL_DEPTH_TEST);
        gl.glEnable(GL2.GL_LIGHTING);
        gl.glEnable(GL2.GL_LIGHT0);
        gl.glEnable(GL2.GL_NORMALIZE);

        // gl.glEnable(GL2.GL_BLEND);
        // gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ZERO);
    }

    @Override
    public void dispose(GLAutoDrawable glAutoDrawable) {

    }

    private double[] computeArcX(double x, double R, double alpha1, double alpha2, int n) {
        double alpha = alpha2 - alpha1;
        double deltaAlpha = alpha / (n + 1);
        double[] arcX = new double[n + 2];

        for (int i = 0; i < n + 2; ++i) {
            arcX[i] = x + R * Math.cos(i * deltaAlpha + alpha1);
        }

        return arcX;
    }

    private double[] computeArcY(double y, double R, double alpha1, double alpha2, int n) {
        double alpha = alpha2 - alpha1;
        double deltaAlpha = alpha / (n + 1);
        double[] arcY = new double[n + 2];

        for (int i = 0; i < n + 2; ++i) {
            arcY[i] = y + R * Math.sin(i * deltaAlpha + alpha1);
        }

        return arcY;
    }

    private void drawTape(GL2 gl, double x, double y, double z1, double z2, double R, double alpha1, double alpha2, int n) {
        gl.glBegin(GL2.GL_QUAD_STRIP);

        gl.glColor3d(0.8, 0.5, 0.3);

        double[] arcX = computeArcX(x, R, alpha1, alpha2, n);
        double[] arcY = computeArcY(y, R, alpha1, alpha2, n);
        for (int i = 0; i < n + 2; ++i) {
            gl.glVertex3d(arcX[i], arcY[i], z1);
            gl.glVertex3d(arcX[i], arcY[i], z2);
        }

        gl.glEnd();
    }

    @Override
    public void display(GLAutoDrawable glAutoDrawable) {
        GL2 gl = glAutoDrawable.getGL().getGL2();

        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

        gl.glPushMatrix();

        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        if (projection == Projection.ORTHO) {
            gl.glOrtho(-2, 2, -2, 2, -20, 20);
            gl.glTranslated(0.0, 0.0, 0.0);
        } else {
            gl.glFrustum(-2, 2, -2, 2, 4.0, 25.0);
            gl.glTranslated(0.0, 0.0, -9.6);
            gl.glScaled(2.3, 2.3, 2.3);
        }

        gl.glMatrixMode(GL2.GL_MODELVIEW);

        gl.glRotated(rotationAngleXAxis, 1.0, 0.0, 0.0);
        gl.glRotated(rotationAngleYAxis, 0.0, 1.0, 0.0);
        gl.glRotated(rotationAngleZAxis, 0.0, 0.0, 1.0);

        gl.glLineWidth(2);

        if (needToDrawAxis) {
            drawAxis(gl);
        }

        float[] no_specular = { 0.0f, 0.0f, 0.0f, 1.0f };
        float[] mat_specular = { 1.0f, 1.0f, 1.0f, 1.0f };

        float[] mat_diffuse1 = { 0.5f, 0.9f, 0.5f, 1.0f };
        float[] mat_diffuse2 = { 1.0f, 0.8f, 0.1f, 1.0f };
        float[] mat_diffuse3 = { 0.8f, 0.5f, 0.3f, 1.0f };
        float[] mat_diffuse4 = { 0.0f, 0.0f, 0.0f, 1.0f };

        float[] mat_ambient1 = { 0.1f, 0.1f, 0.1f, 1.0f };
        float[] mat_ambient2 = { 1.0f, 0.8f, 0.1f, 1.0f };

        float[] no_ambient = { 0.2f, 0.2f, 0.2f, 1.0f };

        gl.glMaterialf(GL.GL_FRONT, GL2.GL_SHININESS, 100.0f);

        gl.glMaterialfv(GL.GL_FRONT, GL2.GL_DIFFUSE, mat_diffuse1, 0);
        gl.glMaterialfv(GL.GL_FRONT, GL2.GL_SPECULAR, mat_specular, 0);
        gl.glMaterialfv(GL.GL_FRONT, GL2.GL_AMBIENT, no_ambient, 0);

        drawFace(gl);

        gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL2.GL_DIFFUSE, mat_diffuse2, 0);
        gl.glMaterialfv(GL.GL_FRONT, GL2.GL_SPECULAR, mat_specular, 0);
        gl.glMaterialfv(GL.GL_FRONT, GL2.GL_AMBIENT, no_ambient, 0);

        drawButton(gl);

        gl.glMaterialf(GL.GL_FRONT, GL2.GL_SHININESS, 5.0f);

        gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL2.GL_DIFFUSE, mat_diffuse3, 0);
        gl.glMaterialfv(GL.GL_FRONT, GL2.GL_SPECULAR, no_specular, 0);
        gl.glMaterialfv(GL.GL_FRONT, GL2.GL_AMBIENT, no_ambient, 0);

        drawBox(gl);
        drawTape(gl, 0.0, 0.0, -0.5, 0.5, 1.0, 0.0, Math.PI, triangulationDegree);
        drawTape(gl, 0.0, 0.0, 0.42, 0.50, 0.9, Math.toRadians(230.0), Math.toRadians(-50), 0);
        drawTape(gl, 0.0, 0.0, 0.42, 0.50, 0.9, Math.toRadians(-50.0), Math.toRadians(230.0), 2 * triangulationDegree);
        drawBack(gl);

        gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL2.GL_DIFFUSE, mat_diffuse4, 0);
        gl.glMaterialfv(GL.GL_FRONT, GL2.GL_SPECULAR, no_specular, 0);
        gl.glMaterialfv(GL.GL_FRONT, GL2.GL_AMBIENT, no_ambient, 0);

        drawRisks(gl);
        drawClockHands(gl);

        float[] lightModelAmbient = { 0.8f, 0.2f, 0.2f, 0.3f };
        gl.glLightModelfv(GL2.GL_LIGHT_MODEL_AMBIENT, lightModelAmbient, 0);

        float[] positionLight = { 0.0f, 1.0f, 2.0f, 1.0f };
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, positionLight, 0);

        float[] directionLight = { 0.0f, -1.0f, -2.0f };
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPOT_DIRECTION, directionLight, 0);

        float[] ambientLight = { 0.4f, 0.f, 0.f, 0.0f };  // weak RED ambient
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, ambientLight, 0);

        gl.glLightf(GL2.GL_LIGHT0, GL2.GL_SPOT_CUTOFF, 45.0f);// угол пропускания - 45
        gl.glLightf(GL2.GL_LIGHT0, GL2.GL_SPOT_EXPONENT,0.0f);// значение Е

        gl.glPopMatrix();
    }


    private void drawFace(GL2 gl) {
        double[] arcX = computeArcX(0.0, 0.9, Math.toRadians(-50.0), Math.toRadians(230.0), 2 * triangulationDegree);
        double[] arcY = computeArcY(0.0, 0.9, Math.toRadians(-50.0), Math.toRadians(230.0), 2 * triangulationDegree);

        gl.glPushMatrix();

        gl.glBegin(GL2.GL_POLYGON);

        for (int i = 0; i < 2 * triangulationDegree + 2; ++i) {
            gl.glVertex3d(arcX[i], arcY[i], 0.42);
        }

        gl.glEnd();

        gl.glPopMatrix();
    }

    private void drawBack(GL2 gl) {
        drawTape(gl, 0.0, 0.0, -0.5, 0.5, 1.0, 0.0, Math.PI, triangulationDegree);

        double[] arcX = computeArcX(0.0, 1.0, 0.0, Math.PI, triangulationDegree);
        double[] arcY = computeArcY(0.0, 1.0, 0.0, Math.PI, triangulationDegree);

        gl.glBegin(GL2.GL_POLYGON);

        gl.glVertex3d(-1.0, -1.0, -0.5);
        gl.glVertex3d(1.0, -1.0, -0.5);
        for (int i = 0; i < triangulationDegree + 2; ++i) {
            gl.glVertex3d(arcX[i], arcY[i], -0.5);
        }

        gl.glEnd();

    }

    private void drawButton(GL2 gl) {
        gl.glPushMatrix();

        gl.glBegin(GL2.GL_QUAD_STRIP);

        gl.glVertex3d(0.9, -0.9, 0.5);
        gl.glVertex3d(0.9, -0.9, 0.57);
        gl.glVertex3d(0.6, -0.9, 0.5);
        gl.glVertex3d(0.6, -0.9, 0.57);
        gl.glVertex3d(0.9, -0.6, 0.5);
        gl.glVertex3d(0.9, -0.6, 0.57);
        gl.glVertex3d(0.9, -0.9, 0.5);
        gl.glVertex3d(0.9, -0.9, 0.57);

        gl.glEnd();

        gl.glBegin(GL2.GL_TRIANGLES);

        gl.glColor3d(1.0, 0.8, 0.1);
        gl.glVertex3d(0.9, -0.9, 0.57);
        gl.glVertex3d(0.6, -0.9, 0.57);
        gl.glVertex3d(0.9, -0.6, 0.57);

        gl.glEnd();

        gl.glPopMatrix();
    }

    private void drawAxis(GL2 gl) {
        gl.glBegin(GL.GL_LINES);
        gl.glColor3d(0.0, 0.0, 2.0);
        gl.glVertex3d(0.0, 0.0, 0.0);
        gl.glVertex3d( 2.0, 0.0, 0.0);
        gl.glColor3d(2.0, 0.0, 0.0);
        gl.glVertex3d(0.0, 0.0, 0.0);
        gl.glVertex3d(0.0, 2.0, 0.0);
        gl.glColor3d(0.0, 0.8f, 0.0);
        gl.glVertex3d(0.0, 0.0, 0.0);
        gl.glVertex3d(0.0, 0.0, 2.0);
        gl.glEnd();

        gl.glLineStipple(2,(short)7239);
        gl.glEnable(GL2.GL_LINE_STIPPLE);

        gl.glBegin(GL2.GL_LINES);

        gl.glColor3d(0.0f, 0.0, 2.0);
        gl.glVertex3d( 0.0, 0.0, 0.0);
        gl.glVertex3d(-2.0, 0.0, 0.0);
        gl.glColor3d(2.0, 0.0, 0.0);
        gl.glVertex3d(0.0, 0.0, 0.0);
        gl.glVertex3d(0.0, -2.0, 0.0);
        gl.glColor3d(0.0, 0.8f, 0.0);
        gl.glVertex3d(0.0, 0.0, 0.0);
        gl.glVertex3d(0.0, 0.0, -2.0);

        gl.glEnd();

        gl.glDisable(GL2.GL_LINE_STIPPLE);
    }

    private void drawBox(GL2 gl) {

        gl.glPushMatrix();
        gl.glTranslated(0.0, -1.0, 0.0);
        gl.glBegin(GL2.GL_QUAD_STRIP);

        gl.glColor3d(0.8, 0.5, 0.3);

        gl.glVertex3d(-1.0, 1.0, -0.5);
        gl.glVertex3d(-1.0, 1.0, 0.5);
        gl.glVertex3d(-1.0, 0.0, -0.5);
        gl.glVertex3d(-1.0, 0.0, 0.5);
        gl.glVertex3d(1.0, 0.0, -0.5);
        gl.glVertex3d(1.0, 0.0, 0.5);
        gl.glVertex3d(1.0, 1.0, -0.5);
        gl.glVertex3d(1.0, 1.0, 0.5);

        gl.glEnd();

        gl.glPopMatrix();
    }

    private void drawRisks(GL2 gl) {
        gl.glPushMatrix();
        gl.glColor3d(0.0, 0.0, 0.0);
        gl.glTranslated(0.0, 0.0, 0.45);

        for (int i = 0; i < 12; ++i) {
            gl.glRotated(30, 0.0, 0.0, 1.0);
            gl.glBegin(GL.GL_LINES);
            gl.glVertex2d(0.0, 0.65);
            gl.glVertex2d(0.0, 0.57);
            gl.glEnd();
        }

        gl.glPopMatrix();
    }


    private void drawClockHands(GL2 gl) {
        Calendar calendar = Calendar.getInstance();
        double seconds = calendar.get(Calendar.SECOND);
        double minutes = calendar.get(Calendar.MINUTE) + seconds / 60.0;
        double hours = calendar.get(Calendar.HOUR) + minutes / 60.0;

        gl.glPushMatrix();

        drawHourHand(gl, hours);
        drawMinuteHand(gl, minutes);
        drawSecondHand(gl, seconds);

        gl.glPopMatrix();
    }

    private void drawHourHand(GL2 gl, double hours) {
        gl.glPushMatrix();

        gl.glRotated(-30.0 * hours, 0.0, 0.0, 1.0);
        gl.glTranslated(0.0, -0.1, 0.45);

        gl.glBegin(GL2.GL_POLYGON);
        gl.glVertex2d(0.0, 0.0);
        gl.glVertex2d(0.07, 0.07);
        gl.glVertex2d(0.07, 0.43);
        gl.glVertex2d(0.0, 0.5);
        gl.glVertex2d(-0.07, 0.43);
        gl.glVertex2d(-0.07, 0.07);
        gl.glEnd();

        gl.glPopMatrix();
    }

    private void drawMinuteHand(GL2 gl, double minutes) {
        gl.glPushMatrix();

        gl.glRotated(-6.0 * minutes, 0.0, 0.0, 1.0);
        gl.glTranslated(0.0, -0.1, 0.45);

        gl.glBegin(GL2.GL_POLYGON);
        gl.glVertex2d(0.0, 0.0);
        gl.glVertex2d(0.07, 0.07);
        gl.glVertex2d(0.07, 0.53);
        gl.glVertex2d(0.0, 0.6);
        gl.glVertex2d(-0.07, 0.53);
        gl.glVertex2d(-0.07, 0.07);
        gl.glEnd();

        gl.glPopMatrix();
    }

    private void drawSecondHand(GL2 gl, double seconds) {
        gl.glPushMatrix();

        gl.glRotated(-6.0 * seconds, 0.0, 0.0, 1.0);
        gl.glTranslated(0.0, -0.1, 0.45);

        gl.glBegin(GL.GL_LINES);
        gl.glVertex2d(0.0, 0.0);
        gl.glVertex2d(0.0, 0.71);
        gl.glEnd();

        gl.glPopMatrix();
    }

    @Override
    public void reshape(GLAutoDrawable glAutoDrawable, int x, int y, int width, int height) {

    }
}
