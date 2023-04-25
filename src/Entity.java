import java.util.*;

import processing.core.PImage;

/**
 * An entity that exists in the world. See EntityKind for the
 * different kinds of entities that exist.
 */
public final class Entity {
    public EntityKind kind;
    public String id;
    public Point position;
    public List<PImage> images;
    public int imageIndex;
    public int resourceLimit;
    public int resourceCount;
    public double actionPeriod;
    public double animationPeriod;
    public int health;
    public int healthLimit;

    public Entity(EntityKind kind, String id, Point position, List<PImage> images, int resourceLimit, int resourceCount, double actionPeriod, double animationPeriod, int health, int healthLimit) {
        this.kind = kind;
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
        this.resourceLimit = resourceLimit;
        this.resourceCount = resourceCount;
        this.actionPeriod = actionPeriod;
        this.animationPeriod = animationPeriod;
        this.health = health;
        this.healthLimit = healthLimit;
    }

    /**
     * Helper method for testing. Preserve this functionality while refactoring.
     */
    public String log() {
        return this.id.isEmpty() ? null :
                String.format("%s %d %d %d", this.id, this.position.x, this.position.y, this.imageIndex);
    }

    public static void nextImage(Entity entity) {
        entity.imageIndex = entity.imageIndex + 1;
    }


    public double getAnimationPeriod() {
        return switch (this.kind) {
            case DUDE_FULL, DUDE_NOT_FULL, OBSTACLE, FAIRY, SAPLING, TREE -> this.animationPeriod;
            default -> throw new UnsupportedOperationException(String.format("getAnimationPeriod not supported for %s", this.kind));
        };
    }

    public void executeSaplingActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        this.health++;
        if (!transformPlant( world, scheduler, imageStore)) {
            scheduler.scheduleEvent(this,Functions.createActivityAction(this, world, imageStore), this.actionPeriod);
        }
    }


    public void executeTreeActivity( WorldModel world, ImageStore imageStore, EventScheduler scheduler) {

        if (!transformPlant( world, scheduler, imageStore)) {

            scheduler.scheduleEvent( this, Functions.createActivityAction(this, world, imageStore), this.actionPeriod);
        }
    }

    public void executeFairyActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Optional<Entity> fairyTarget = world.findNearest( this.position, new ArrayList<>(List.of(EntityKind.STUMP)));

        if (fairyTarget.isPresent()) {
            Point tgtPos = fairyTarget.get().position;

            if (moveToFairy( world, fairyTarget.get(), scheduler)) {

                Entity sapling = Background.createSapling(Functions.SAPLING_KEY + "_" + fairyTarget.get().id, tgtPos, Background.getImageList(imageStore, Functions.SAPLING_KEY), 0);

                world.addEntity( sapling);
                VirtualWorld.scheduleActions(sapling, scheduler, world, imageStore);
            }
        }

        scheduler.scheduleEvent( this, Functions.createActivityAction(this, world, imageStore), this.actionPeriod);
    }

    public void executeDudeNotFullActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Optional<Entity> target = world.findNearest( this.position, new ArrayList<>(Arrays.asList(EntityKind.TREE, EntityKind.SAPLING)));

        if (target.isEmpty() || !moveToNotFull( world, target.get(), scheduler) || !transformNotFull( world, scheduler, imageStore)) {
            scheduler.scheduleEvent(this, Functions.createActivityAction(this, world, imageStore), this.actionPeriod);
        }
    }

    public void executeDudeFullActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Optional<Entity> fullTarget = world.findNearest( this.position, new ArrayList<>(List.of(EntityKind.HOUSE)));

        if (fullTarget.isPresent() && moveToFull( world, fullTarget.get(), scheduler)) {
            transformFull( world, scheduler, imageStore);
        } else {
            scheduler.scheduleEvent( this, Functions.createActivityAction(this, world, imageStore), this.actionPeriod);
        }
    }

    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        switch (this.kind) {
            case DUDE_FULL:
                scheduler.scheduleEvent( this, Functions.createActivityAction(this, world, imageStore), this.actionPeriod);
                scheduler.scheduleEvent( this, Functions.createAnimationAction(this, 0), this.getAnimationPeriod());
                break;

            case DUDE_NOT_FULL:
                scheduler.scheduleEvent( this, Functions.createActivityAction(this, world, imageStore), this.actionPeriod);
                scheduler.scheduleEvent( this, Functions.createAnimationAction(this, 0), this.getAnimationPeriod());
                break;

            case OBSTACLE:
                scheduler.scheduleEvent( this, Functions.createAnimationAction(this, 0), this.getAnimationPeriod());
                break;

            case FAIRY:
                scheduler.scheduleEvent( this, Functions.createActivityAction(this, world, imageStore), this.actionPeriod);
                scheduler.scheduleEvent(this, Functions.createAnimationAction(this, 0), this.getAnimationPeriod());
                break;

            case SAPLING:
                scheduler.scheduleEvent(this, Functions.createActivityAction(this, world, imageStore), this.actionPeriod);
                scheduler.scheduleEvent(this, Functions.createAnimationAction(this, 0), this.getAnimationPeriod());
                break;

            case TREE:
                scheduler.scheduleEvent( this, Functions.createActivityAction(this, world, imageStore), this.actionPeriod);
                 scheduler.scheduleEvent(this, Functions.createAnimationAction(this, 0), this.getAnimationPeriod());
                break;

            default:
        }
    }

    public boolean transformNotFull(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        if (this.resourceCount >= this.resourceLimit) {
            Entity dude = Functions.createDudeFull(this.id, this.position, this.actionPeriod, this.animationPeriod, this.resourceLimit, this.images);

            world.removeEntity(scheduler,this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity( dude);
            scheduleActions(dude, scheduler, world, imageStore);

            return true;
        }
        return false;
    }

    public void transformFull(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        Entity dude = Functions.createDudeNotFull(this.id, this.position, this.actionPeriod, this.animationPeriod, this.resourceLimit, this.images);

        world.removeEntity( scheduler, this);
        world.addEntity(dude);
        scheduleActions(scheduler, world, imageStore);
    }

    public boolean transformPlant(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        if (this.kind == EntityKind.TREE) {
            return this.transformTree( world, scheduler, imageStore);
        } else if (this.kind == EntityKind.SAPLING) {
            return this.transformSapling( world, scheduler, imageStore);
        } else {
            throw new UnsupportedOperationException(String.format("transformPlant not supported for %s", this));
        }
    }

    public boolean transformTree(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        if (this.health <= 0) {
            Entity stump = Background.createStump(Functions.STUMP_KEY + "_" + this.id, this.position, Background.getImageList(imageStore, Functions.STUMP_KEY));

            world.removeEntity(scheduler, this);

            world.addEntity(stump);

            return true;
        }

        return false;
    }

    public boolean transformSapling(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        if (this.health <= 0) {
            Entity stump = Background.createStump(Functions.STUMP_KEY + "_" + this.id, this.position, Background.getImageList(imageStore, Functions.STUMP_KEY));

            world.removeEntity( scheduler, this);

            world.addEntity(stump);

            return true;
        } else if (this.health >= this.healthLimit) {
            Entity tree = Background.createTree(Functions.TREE_KEY + "_" + this.id, this.position, Functions.getNumFromRange(Functions.TREE_ACTION_MAX, Functions.TREE_ACTION_MIN), Functions.getNumFromRange(Functions.TREE_ANIMATION_MAX, Functions.TREE_ANIMATION_MIN), Functions.getIntFromRange(Functions.TREE_HEALTH_MAX, Functions.TREE_HEALTH_MIN), Background.getImageList(imageStore, Functions.TREE_KEY));

            world.removeEntity( scheduler, this);

            world.addEntity( tree);
            this.scheduleActions(tree, scheduler, world, imageStore);

            return true;
        }

        return false;
    }

    public boolean moveToFairy(WorldModel world, Entity target, EventScheduler scheduler) {
        if (Point.adjacent(this.position, target.position)) {
            world.removeEntity( scheduler, target);
            return true;
        } else {
            Point nextPos = nextPositionFairy(this, world, target.position);

            if (!this.position.equals(nextPos)) {
                world.moveEntity( scheduler, this, nextPos);
            }
            return false;
        }
    }

    public boolean moveToNotFull(WorldModel world, EventScheduler scheduler) {
        if (Point.adjacent(this.position, this.position)) {
            this.resourceCount += 1;
            this.health--;
            return true;
        } else {
            Point nextPos = nextPositionDude( world, this.position);

            if (!this.position.equals(nextPos)) {
                world.moveEntity( scheduler, this, nextPos);
            }
            return false;
        }
    }

    public Point nextPositionFairy(WorldModel world, Point destPos) {
        int horiz = Integer.signum(destPos.x - this.position.x);
        Point newPos = new Point(this.position.x + horiz, this.position.y);

        if (horiz == 0 || world.isOccupied( newPos) && world.getOccupancyCell( newPos).kind != EntityKind.HOUSE) {
            int vert = Integer.signum(destPos.y - this.position.y);
            newPos = new Point(this.position.x, this.position.y + vert);

            if (vert == 0 || world.isOccupied( newPos) && world.getOccupancyCell( newPos).kind != EntityKind.HOUSE) {
                newPos = this.position;
            }
        }

        return newPos;
    }

    public Point nextPositionDude(WorldModel world, Point destPos) {
        int horiz = Integer.signum(destPos.x - this.position.x);
        Point newPos = new Point(this.position.x + horiz, this.position.y);

        if (horiz == 0 || world.isOccupied(newPos) && world.getOccupancyCell( newPos).kind != EntityKind.STUMP) {
            int vert = Integer.signum(destPos.y - this.position.y);
            newPos = new Point(this.position.x, this.position.y + vert);

            if (vert == 0 || world.isOccupied( newPos) && world.getOccupancyCell( newPos).kind != EntityKind.STUMP) {
                newPos = this.position;
            }
        }

        return newPos;
    }
}