package leti.practice.view;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import leti.practice.structures.graph.Node;
import leti.practice.structures.graph.ResidualNetwork;

import java.util.Set;


public class ResidualNetworkViewPainter extends ViewPainter {

    @Override
    public void paint(ResidualNetwork<? extends Number> network) {
        Canvas canvas = getCanvas();
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }
}