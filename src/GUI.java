

import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

public class GUI extends JFrame {

    private final FPSAnimator animator;

    public GUI() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(830, 600);

        final ClockRenderer clockRenderer = new ClockRenderer();

        final GLCanvas clockCanvas = new GLCanvas();
        clockCanvas.addGLEventListener(clockRenderer);

        animator = new FPSAnimator(clockCanvas, 60);

        final JLabel labelTriangulationDegree = new JLabel("Triangulation degree");

        final JSlider sliderTriangulationDegree = new JSlider(12, 64, 32);
        sliderTriangulationDegree.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                clockRenderer.setTriangulationDegree(sliderTriangulationDegree.getValue());
            }
        });

        final JLabel labelRotationAngleXAxis = new JLabel("Rotation angle around X axis");

        final DoubleSlider sliderRotationAngleXAxis = new DoubleSlider(-180.0, 180.0, 30.0, 360);
        sliderRotationAngleXAxis.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                clockRenderer.setRotationAngleXAxis(sliderRotationAngleXAxis.getDoubleValue());
            }
        });

        final JLabel labelRotationAngleYAxis = new JLabel("Rotation angle around Y axis");

        final DoubleSlider sliderRotationAngleYAxis = new DoubleSlider(-180.0, 180.0, 20.0, 360);
        sliderRotationAngleYAxis.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                clockRenderer.setRotationAngleYAxis(sliderRotationAngleYAxis.getDoubleValue());
            }
        });

        final JLabel labelRotationAngleZAxis = new JLabel("Rotation angle around Z axis");

        final DoubleSlider sliderRotationAngleZAxis = new DoubleSlider(-180.0, 180.0, 0.0, 360);
        sliderRotationAngleZAxis.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                clockRenderer.setRotationAngleZAxis(sliderRotationAngleZAxis.getDoubleValue());
            }
        });

        final JCheckBox checkBoxNeedToDrawAxis = new JCheckBox("Draw axis", true);
        checkBoxNeedToDrawAxis.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                clockRenderer.setNeedToDrawAxis(checkBoxNeedToDrawAxis.isSelected());
            }
        });

        final JRadioButton radioButtonOrtho = new JRadioButton("Ortho", true);
        final JRadioButton radioButtonFrustum = new JRadioButton("Frustum");

        ButtonGroup buttonGroupProjections = new ButtonGroup();
        buttonGroupProjections.add(radioButtonOrtho);
        buttonGroupProjections.add(radioButtonFrustum);

        radioButtonOrtho.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (radioButtonOrtho.isSelected()) {
                    clockRenderer.setProjection(Projection.ORTHO);
                } else {
                    clockRenderer.setProjection(Projection.FRUSTUM);
                }
            }
        });

        JPanel panelProjections = new JPanel();
        panelProjections.setBorder(new TitledBorder("Projection view"));
        panelProjections.setLayout(new BoxLayout(panelProjections, BoxLayout.Y_AXIS));
        panelProjections.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelProjections.add(Box.createHorizontalGlue());
        panelProjections.add(radioButtonOrtho);
        panelProjections.add(radioButtonFrustum);

        JPanel panelProperties = new JPanel();
        panelProperties.setLayout(new BoxLayout(panelProperties, BoxLayout.Y_AXIS));
        panelProperties.add(checkBoxNeedToDrawAxis);
        panelProperties.add(labelTriangulationDegree);
        panelProperties.add(sliderTriangulationDegree);
        panelProperties.add(panelProjections);
        panelProperties.add(labelRotationAngleXAxis);
        panelProperties.add(sliderRotationAngleXAxis);
        panelProperties.add(labelRotationAngleYAxis);
        panelProperties.add(sliderRotationAngleYAxis);
        panelProperties.add(labelRotationAngleZAxis);
        panelProperties.add(sliderRotationAngleZAxis);

        JSplitPane splitPaneGUI = new JSplitPane();
        splitPaneGUI.setLeftComponent(panelProperties);
        splitPaneGUI.setRightComponent(clockCanvas);

        add(splitPaneGUI);
    }

    public void run() {
        setVisible(true);
        animator.start();
    }
}
