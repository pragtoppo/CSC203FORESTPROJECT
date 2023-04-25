import processing.core.PApplet;
import processing.core.PImage;

import java.util.Optional;

public final class WorldView {
    public PApplet screen;
    public WorldModel world;
    public int tileWidth;
    public int tileHeight;
    public Viewport viewport;

    public WorldView(int numRows, int numCols, PApplet screen, WorldModel world, int tileWidth, int tileHeight) {
        this.screen = screen;
        this.world = world;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        this.viewport = new Viewport(numRows, numCols);
    }
    public void drawEntities() {
        for (Entity entity : this.world.entities) {
            Point pos = entity.position;

            if (viewport.contains(pos)) {
                Point viewPoint = viewport.worldToViewport( pos.x, pos.y);
                this.screen.image(Background.getCurrentImage(entity), viewPoint.x * this.tileWidth, viewPoint.y * this.tileHeight);
            }
        }
    }

    public void drawViewport() {
        this.drawBackground();
        this.drawEntities();
    }

    public static Optional<PImage> getBackgroundImage(WorldModel world, Point pos) {
        if (world.withinBounds(pos)) {
            return Optional.of(Background.getCurrentImage(world.getBackgroundCell( pos)));
        } else {
            return Optional.empty();
        }
    }

    public void drawBackground() {
        for (int row = 0; row < this.viewport.numRows; row++) {
            for (int col = 0; col < this.viewport.numCols; col++) {
                Point worldPoint = viewport.viewportToWorld(col, row);
                Optional<PImage> image = getBackgroundImage(this.world, worldPoint);
                if (image.isPresent()) {
                    this.screen.image(image.get(), col * this.tileWidth, row * this.tileHeight);
                }
            }
        }
    }
    public void shiftView(int colDelta, int rowDelta) {
        int newCol = Functions.clamp(this.viewport.col + colDelta, 0, this.world.numCols - this.viewport.numCols);
        int newRow = Functions.clamp(this.viewport.row + rowDelta, 0, this.world.numRows - this.viewport.numRows);

        viewport.shift(newCol, newRow);
    }
}
