import java.util.*;

/**
 * Represents the 2D World in which this simulation is running.
 * Keeps track of the size of the world, the background image for each
 * location in the world, and the entities that populate the world.
 */
public final class WorldModel {
    public int numRows;
    public int numCols;
    public Background[][] background;
    public Entity[][] occupancy;
    public Set<Entity> entities;

    public WorldModel() {

    }

    /**
     * Helper method for testing. Don't move or modify this method.
     */
    public List<String> log() {
        List<String> list = new ArrayList<>();
        for (Entity entity : entities) {
            String log = entity.log();
            if (log != null) list.add(log);
        }
        return list;
    }


    public void tryAddEntity(Entity entity) {
        if (this.isOccupied(entity.position)) {
            // defining our own exceptions yet
            throw new IllegalArgumentException("position occupied");
        }

        addEntity(entity);
    }

    public boolean withinBounds(Point pos) {
        return pos.y >= 0 && pos.y < this.numRows && pos.x >= 0 && pos.x < this.numCols;
    }

    public boolean isOccupied(Point pos) {
        return this.withinBounds(pos) && this.getOccupancyCell( pos) != null;
    }

    public Optional<Entity> findNearest(Point pos, List<EntityKind> kinds) {
        List<Entity> ofType = new LinkedList<>();
        for (EntityKind kind : kinds) {
            for (Entity entity : this.entities) {
                if (entity.getKind() == kind) {
                    ofType.add(entity);
                }
            }
        }

        return Functions.nearestEntity(ofType, pos);
    }

    /*
       Assumes that there is no entity currently occupying the
       intended destination cell.
    */
    public void addEntity(Entity entity) {
        if (withinBounds(entity.position)) {
            setOccupancyCell( entity.position, entity);
            this.entities.add(entity);
        }
    }

    public void moveEntity(EventScheduler scheduler, Entity entity, Point pos) {
        Point oldPos = entity.position;
        if (withinBounds( pos) && !pos.equals(oldPos)) {
            setOccupancyCell( oldPos, null);
            Optional<Entity> occupant = getOccupant( pos);
            occupant.ifPresent(target -> this.removeEntity(scheduler, target));
            setOccupancyCell( pos, entity);
            entity.position = pos;
        }
    }

    public void removeEntity(EventScheduler scheduler, Entity entity) {
        scheduler.unscheduleAllEvents(entity);
        removeEntityAt(entity.position);
    }

    public void removeEntityAt( Point pos) {
        if (withinBounds( pos) && getOccupancyCell( pos) != null) {
            Entity entity = getOccupancyCell(pos);

            /* This moves the entity just outside of the grid for
             * debugging purposes. */
            entity.position = new Point(-1, -1);
            this.entities.remove(entity);
            setOccupancyCell(pos, null);
        }
    }


    public Optional<Entity> getOccupant(Point pos) {
        if (isOccupied( pos)) {
            return Optional.of(this.getOccupancyCell(pos));
        } else {
            return Optional.empty();
        }
    }

    public Entity getOccupancyCell(Point pos) {
        return this.occupancy[pos.y][pos.x];
    }

    public void setOccupancyCell( Point pos, Entity entity) {
        this.occupancy[pos.y][pos.x] = entity;
    }

    public Background getBackgroundCell(Point pos) {
        return this.background[pos.y][pos.x];
    }


    public void load(Scanner saveFile, ImageStore imageStore, Background defaultBackground) {
        Functions.parseSaveFile(this, saveFile, imageStore, defaultBackground);
        if (this.background == null) {
            this.background = new Background[this.numRows][this.numCols];
            for (Background[] row : this.background)
                Arrays.fill(row, defaultBackground);
        }
        if (this.occupancy == null) {
            this.occupancy = new Entity[this.numRows][this.numCols];
            this.entities = new HashSet<>();
        }
    }
}