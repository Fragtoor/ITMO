package gui.views;

import common.net.User;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.geometry.VPos;

import java.util.ArrayList;
import java.util.List;

public class AdminVisualizationView extends Pane {
    private final Canvas canvas = new Canvas();
    private List<User> users = new ArrayList<>();
    private AnimationTimer renderLoop;

    public AdminVisualizationView() {
        canvas.widthProperty().bind(this.widthProperty());
        canvas.heightProperty().bind(this.heightProperty());
        getChildren().add(canvas);
        setStyle("-fx-background-color: #1a2128;");

        renderLoop = new AnimationTimer() {
            public void handle(long now) {
                draw();
            }
        };
        renderLoop.start();
    }

    public void setUsers(List<User> users) {
        this.users = new ArrayList<>(users);
    }

    private void draw() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        double w = canvas.getWidth();
        double h = canvas.getHeight();
        if (w == 0 || h == 0) return;

        gc.setFill(Color.web("#1a2128"));
        gc.fillRect(0, 0, w, h);

        double time = System.currentTimeMillis() / 1000.0;

        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);

            double spacing = 120;
            int cols = Math.max(1, (int) (w / spacing));
            double startX = (w - (cols * spacing)) / 2 + spacing / 2;
            double startY = 100;

            double baseX = startX + (i % cols) * spacing;
            double baseY = startY + (i / cols) * spacing;

            double offsetY = Math.sin(time * 2 + i) * 10;
            double angle = Math.sin(time + i * 0.5) * 15;

            drawUserSquare(gc, user, baseX, baseY + offsetY, angle);
        }
    }

    private void drawUserSquare(GraphicsContext gc, User user, double x, double y, double angle) {
        double size = 60;

        gc.save();
        gc.translate(x, y);
        gc.rotate(angle);

        gc.setFill(Color.web("#34495e"));
        gc.setStroke(Color.web("#3498db"));
        gc.setLineWidth(3);
        gc.fillRect(-size / 2, -size / 2, size, size);
        gc.strokeRect(-size / 2, -size / 2, size, size);

        gc.setFill(Color.web("#ecf0f1"));
        gc.fillOval(-size * 0.2, -size * 0.35, size * 0.4, size * 0.4);
        gc.fillArc(-size * 0.35, -size * 0.05, size * 0.7, size * 0.5, 0, 180, ArcType.ROUND);

        gc.restore();

        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.TOP);
        gc.fillText(user.getUserName(), x, y + size / 2 + 10);
    }
}