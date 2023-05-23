import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class DUDE_NOT_FULL extends DUDEAbstract{
    private int resourceCount = 0;

    public DUDE_NOT_FULL( String id, Point position, double actionPeriod, double animationPeriod, int resourceLimit,List<PImage> images) {
        super( id, position, actionPeriod, animationPeriod, resourceLimit, images);
    }

    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Optional<Entity> target = world.findNearest(this.getPosition(), new ArrayList<>(Arrays.asList(TREE.class, SAPLING.class)));

        if (target.isEmpty() || !this.moveToNotFull( world, (SAPTREE) target.get(), scheduler) || !transformNotFull( world, scheduler, imageStore)) {
            scheduler.scheduleEvent(this, new Activity(this, world, imageStore), this.getActionPeriod());
        }
    }
    public void nextImage() {
        setImage(getImageIndex() + 1);
    }
    public boolean transformNotFull(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        if (this.resourceCount >= this.getResourceLimit()) {
            DUDE_FULL dude = new DUDE_FULL(this.getId(), this.getPosition(), this.getActionPeriod(),
                    this.getAnimationPeriod(), this.getResourceLimit(), this.getImages());

            world.removeEntity(scheduler,this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(dude);
            dude.scheduleActions(scheduler, world, imageStore);

            return true;
        }
        return false;
    }
    public String log() {
        return this.getId().isEmpty() ? null :
                String.format("%s %d %d %d", this.getId(), this.getPosition().getX(), this.getPosition().getY(), this.getImageIndex());
    }
    public PImage getCurrentImage()
    {
        return this.getImages().get(this.getImageIndex() % this.getImages().size());
    }
    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent(this, new Activity(this, world, imageStore), this.getActionPeriod());
        scheduler.scheduleEvent(this, new Animation(this, 0), this.getAnimationPeriod());
    }

    public boolean moveToNotFull(WorldModel world, SAPTREE target, EventScheduler scheduler) {
        if (Point.adjacent(this.getPosition(), target.getPosition())) {
            this.resourceCount += 1;
            target.decreaseHealth();
            return true;
        } else {
            Point nextPos = nextPositionDude( world, target.getPosition());

            if (!this.getPosition().equals(nextPos)) {
                world.moveEntity( scheduler, this, nextPos);
            }
            return false;
        }
    }
}
