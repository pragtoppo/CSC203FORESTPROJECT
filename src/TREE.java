import processing.core.PImage;

import java.util.List;

public class TREE extends SAPTREE{
    private int imageIndex;
   private double actionPeriod;
    private double animationPeriod;
    public double getAnimationPeriod()
    {
        return animationPeriod;
    }

    public TREE(String id, Point position, List<PImage> images, double actionPeriod, double animationPeriod, int health ){
        super( id, position, images, health);
        this.actionPeriod =actionPeriod;
        this.animationPeriod = animationPeriod;
    }
    public String log() {
        return this.getId().isEmpty() ? null :
                String.format("%s %d %d %d", this.getId(), this.getPosition().getX(), this.getPosition().getY(), this.imageIndex);
    }

    public void executeActivity( WorldModel world, ImageStore imageStore, EventScheduler scheduler) {

        if (transformPlant(world, scheduler, imageStore)) {

            scheduler.scheduleEvent( this, new Activity(this, world, imageStore), actionPeriod);
        }
    }
//    transformTree
     public boolean transformPlant(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        if (this.getHealth() <= 0) {
            STUMP stump = new STUMP(Functions.STUMP_KEY + "_" + this.getId(),this.getPosition(), imageStore.getImageList( Functions.STUMP_KEY));
            world.removeEntity(scheduler, this);
            world.addEntity(stump);
            return false;
        }
        return true;
    }
    public PImage getCurrentImage()
    {
        return this.getImages().get(this.getImageIndex() % this.getImages().size());
    }
    public void nextImage() {
        imageIndex =imageIndex + 1;
    }
    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent(this, new Activity(this, world, imageStore), this.actionPeriod);
        scheduler.scheduleEvent(this, new Animation(this, 0), this.animationPeriod);
    }
}
