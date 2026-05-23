package gui.views;

import common.models.MusicBand;
import javafx.animation.*;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

import java.util.*;
import java.util.function.Consumer;

public class VisualizationView extends Pane {
    private final Canvas canvas = new Canvas();
    private List<MusicBand> bands = new ArrayList<>();
    private final Map<Integer, SimpleDoubleProperty> animProgress = new HashMap<>();
    private final Map<Integer, Color> ownerColors = new HashMap<>();
    private final List<Color> palette = List.of(
            Color.web("#2ecc71"), Color.web("#3498db"), Color.web("#e74c3c"),
            Color.web("#f39c12"), Color.web("#9b59b6"), Color.web("#1abc9c"),
            Color.web("#e67e22"), Color.web("#e91e63"), Color.web("#00bcd4")
    );
    private Consumer<MusicBand> onBandClick;
    private AnimationTimer renderLoop;


    public VisualizationView() {
        canvas.widthProperty().bind(this.widthProperty());
        canvas.heightProperty().bind(this.heightProperty());
        getChildren().add(canvas);
        setStyle("-fx-background-color: #1a2128;");

        canvas.setOnMouseClicked(e -> {
            if (onBandClick == null) return;
            double mx = e.getX(), my = e.getY();
            for (MusicBand band : bands) {
                double[] pos = getPosition(band);
                double r = getRadius(band);
                double dx = mx - pos[0], dy = my - pos[1];
                if (dx * dx + dy * dy <= r * r) {
                    onBandClick.accept(band);
                    break;
                }
            }
        });

        renderLoop = new AnimationTimer() {
            public void handle(long now) { draw(); }
        };
        renderLoop.start();
    }

    // Обновляет список элементов для отрисовки.
    public void setBands(List<MusicBand> newBands) {
        Set<Integer> newIds = new HashSet<>();
        for (MusicBand b : newBands) newIds.add(b.getId());
        animProgress.keySet().removeIf(id -> !newIds.contains(id));

        for (MusicBand b : newBands) {
            if (!animProgress.containsKey(b.getId())) {
                SimpleDoubleProperty prop = new SimpleDoubleProperty(0.0);
                animProgress.put(b.getId(), prop);
                Timeline tl = new Timeline(
                        new KeyFrame(Duration.ZERO,        new KeyValue(prop, 0.0)),
                        new KeyFrame(Duration.millis(700), new KeyValue(prop, 1.0, Interpolator.EASE_OUT))
                );
                tl.play();
            }
            ownerColors.computeIfAbsent(b.getOwnerId(),
                    id -> palette.get(ownerColors.size() % palette.size()));
        }
        this.bands = new ArrayList<>(newBands);
    }

    // Устанавливает колбэк, которая выполнится при клике на конкретную группу.
    public void setOnBandClick(Consumer<MusicBand> handler) {
        this.onBandClick = handler;
    }

    // Метод отрисовки, который вызывается каждый кадр
    private void draw() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        double w = canvas.getWidth(), h = canvas.getHeight();
        if (w == 0 || h == 0) return;

        gc.setFill(Color.web("#1a2128"));
        gc.fillRect(0, 0, w, h);

        for (MusicBand band : bands) {
            double progress = animProgress.getOrDefault(band.getId(),
                    new SimpleDoubleProperty(1.0)).get();
            drawBand(gc, band, progress, w, h);
        }
    }

    private void drawBand(GraphicsContext gc, MusicBand band, double progress, double w, double h) {
        double[] pos = getPosition(band, w, h);
        double r = getRadius(band) * progress;
        if (r < 1) return;

        Color base   = ownerColors.getOrDefault(band.getOwnerId(), Color.web("#2ecc71"));
        Color fill   = base.deriveColor(0, 1, 1, 0.25 * progress);
        Color stroke = base.deriveColor(0, 1, 1, progress);

        gc.save();

        double pulse = 1.0 + 0.06 * Math.sin(System.currentTimeMillis() / 400.0);
        gc.setFill(base.deriveColor(0, 1, 1, 0.08 * progress));
        gc.fillOval(pos[0] - r * pulse * 1.4, pos[1] - r * pulse * 1.4,
                r * pulse * 2.8, r * pulse * 2.8);

        gc.setFill(fill);
        gc.fillOval(pos[0] - r, pos[1] - r, r * 2, r * 2);
        gc.setStroke(stroke);
        gc.setLineWidth(2.5);
        gc.strokeOval(pos[0] - r, pos[1] - r, r * 2, r * 2);

        gc.setFill(Color.WHITE.deriveColor(0, 1, 1, progress));
        gc.setFont(Font.font("Verdana", FontWeight.BOLD, Math.max(9, r * 0.45)));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);
        String label = band.getName();
        if (label.length() > 10) label = label.substring(0, 9) + "…";
        gc.fillText(label, pos[0], pos[1]);

        gc.restore();
    }

    // Переводит логические координаты объекта (X, Y) в координаты на экране холста
    private double[] getPosition(MusicBand band, double w, double h) {
        double margin = 60;
        if (band.getCoordinates() != null) {
            double bx = Math.max(-1000, Math.min(1000, band.getCoordinates().getX()));
            double by = Math.max(-1000, Math.min(1000, (double) band.getCoordinates().getY()));
            double cx = margin + (bx + 1000) / 2000.0 * (w - 2 * margin);
            double cy = (h - margin) - (by + 1000) / 2000.0 * (h - 2 * margin);
            return new double[]{cx, cy};
        }
        return new double[]{w / 2, h / 2};
    }

    private double[] getPosition(MusicBand band) {
        return getPosition(band, canvas.getWidth(), canvas.getHeight());
    }

    private double getRadius(MusicBand band) {
        int p = band.getNumberOfParticipants();
        return Math.max(14, Math.min(55, 14 + Math.log10(Math.max(1, p)) * 14));
    }
}