import processing.core.PImage;

import java.util.List;

public class TREE extends SAPTREE{
    private int imageIndex;
   private double actionPeriod;
    private double animationPeriod;
    private int health;
    private int healthLimit;
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

        if (!transformPlant( world, scheduler, imageStore)) {

            scheduler.scheduleEvent( this, new Activity(this, world, imageStore), actionPeriod);
        }
    }
//    transformTree
     public boolean transformPlant(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        if (health <= 0) {
            STUMP stump = new STUMP(Functions.getStumpKey() + "_" + this.getId(),this.getPosition(), imageStore.getImageList( Functions.getStumpKey()));
            world.removeEntity(scheduler, this);
            world.addEntity(stump);
            return true;
        }
        return false;
    }
//    public boolean transformPlant(WorldModel world, EventScheduler scheduler, ImageStore imageStore)
//    {
////        if (this.getClass() == TREE.class) {
////            return !transformTree( world, scheduler, imageStore);
////        } else if (this.getClass() == SAPLING.class) {
////            return !SAPLING.transformSapling( world, scheduler, imageStore);
////        } else {
////            throw new UnsupportedOperationException(String.format("transformPlant not supported for %s", this));
////        }
//    }
    public PImage getCurrentImage()
    {
        return this.getImages().get(this.getImageIndex() % this.getImages().size());
    }
    public void nextImage() {
        imageIndex =imageIndex + 1;
    }
    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent(this, new Activity(this, world, imageStore), actionPeriod);
        scheduler.scheduleEvent(this, new Animation(this, 0), animationPeriod);
    }
}
