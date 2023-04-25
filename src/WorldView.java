import processing.core.PApplet;
import processing.core.PImage;

import java.util.Optional;

public final class WorldView {
    private PApplet screen;
    private WorldModel world;
    private int tileWidth;
    private int tileHeight;
    private Viewport viewport;

    public PApplet getScreen()
    {
        return screen;
    }
    public WorldModel getWorld()
    {
        return world;
    }
    public int getTileWidth()
    {
        return tileWidth;
    }
    public int getTileHeight()
    {
        return tileHeight;
    }
    public  Viewport getViewport()
    {
        return viewport;
    }

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
        for (int row = 0; row < this.viewport.getNumRows(); row++) {
            for (int col = 0; col < this.viewport.getNumCols(); col++) {
                Point worldPoint = viewport.viewportToWorld(col, row);
                Optional<PImage> image = getBackgroundImage(this.world, worldPoint);
                if (image.isPresent()) {
                    this.screen.image(image.get(), col * this.tileWidth, row * this.tileHeight);
                }
            }
        }
    }
    public void shiftView(int colDelta, int rowDelta) {
        int newCol = Functions.clamp(this.viewport.getCol() + colDelta, 0, this.world.numCols - this.viewport.getNumCols());
        int newRow = Functions.clamp(this.viewport.getRow() + rowDelta, 0, this.world.numRows - this.viewport.getNumRows());

        viewport.shift(newCol, newRow);
    }
}
