import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class DUDE_NOT_FULL extends DUDEAbstract{
    private String id;
    private Point position;
   private List<PImage> images;
    private int imageIndex;

   private  double actionPeriod;
   private double animationPeriod;
    private int resourceLimit;
    private int resourceCount = 0;

    public DUDE_NOT_FULL( String id, Point position, double actionPeriod, double animationPeriod, int resourceLimit,List<PImage> images) {
        super( id, position, actionPeriod, animationPeriod, resourceLimit, images);
    }
    public  String getId()
    {
        return id;
    }
    public Point getPosition() {
        return position;
    }
    public  void setPosition(Point p)
    {
        position = p;
    }
    public List<PImage> getImages()
    {
        return images;
    }
    public  int getImageIndex()
    {
        return imageIndex;
    }
    public double getAnimationPeriod()
    { return animationPeriod; }

    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Optional<Entity> target = world.findNearest(this.position, new ArrayList<>(Arrays.asList(TREE.class, SAPLING.class)));

        if (target.isEmpty() || !this.moveToNotFull( world, (SAPTREE) target.get(), scheduler) || !transformNotFull( world, scheduler, imageStore)) {
            scheduler.scheduleEvent(this, new Activity(this, world, imageStore), this.actionPeriod);
        }
    }
    public boolean transformNotFull(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        if (this.resourceCount >= this.resourceLimit) {
            DUDE_FULL dude = new DUDE_FULL(this.id, this.position, this.actionPeriod,
                    this.animationPeriod, this.resourceLimit, this.images);

            world.removeEntity(scheduler,this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(dude);
            dude.scheduleActions(scheduler, world, imageStore);

            return true;
        }
        return false;
    }
    public void nextImage() {
        imageIndex =imageIndex + 1;
    }
    public String log() {
        return this.id.isEmpty() ? null :
                String.format("%s %d %d %d", this.id, this.position.getX(), this.position.getY(), this.imageIndex);
    }
    public PImage getCurrentImage()
    {
        return this.getImages().get(this.getImageIndex() % this.getImages().size());
    }
    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent(this, new Activity(this, world, imageStore), actionPeriod);
        scheduler.scheduleEvent(this, new Animation(this, 0), animationPeriod);
    }

    public boolean moveToNotFull(WorldModel world, SAPTREE target, EventScheduler scheduler) {
        if (Point.adjacent(position, target.getPosition())) {
            this.resourceCount += 1;
            target.decreaseHealth();
            return true;
        } else {
            Point nextPos = nextPositionDude( world, target.getPosition());

            if (!this.position.equals(nextPos)) {
                world.moveEntity( scheduler, this, nextPos);
            }
            return false;
        }
    }
}
