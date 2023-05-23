import processing.core.PImage;

import java.util.List;

public class SAPLING extends SAPTREE{
    private final int healthLimit =5;
    private final double animationPeriod = 1.000;
    private final double actionPeriod = 1.000;


    public SAPLING(String id, Point position, List<PImage> images, int health) {
        super(id, position, images, health);

    }
    public double getAnimationPeriod()
    { return animationPeriod; }


    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        this.setHealth(this.getHealth()+1);
        if (transformPlant(world, scheduler, imageStore)) {
            scheduler.scheduleEvent(this,new Activity(this, world, imageStore), this.actionPeriod);
        }
    }

    public void nextImage() {
        this.setImages(getImageIndex()+1);
    }


    public boolean transformPlant(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        if (getHealth()<= 0) {
            STUMP stump = new STUMP(Functions.getStumpKey() + "_" + this.getId(), this.getPosition(),
                    imageStore.getImageList(Functions.getStumpKey()));

            world.removeEntity( scheduler, this);

            world.addEntity(stump);

            return false;
        } else if (this.getHealth() >= this.healthLimit) {
            TREE tree = new TREE(Functions.getTreeKey() + "_" + this.getId(), this.getPosition(),
                    imageStore.getImageList(Functions.TREE_KEY),
                    Functions.getNumFromRange(Functions.TREE_ACTION_MAX, Functions.TREE_ACTION_MIN),
                    Functions.getNumFromRange(Functions.TREE_ANIMATION_MAX, Functions.TREE_ANIMATION_MIN),
                    Functions.getIntFromRange(Functions.TREE_HEALTH_MAX, Functions.TREE_HEALTH_MIN));

            world.removeEntity( scheduler, this);

            world.addEntity(tree);
            tree.scheduleActions(scheduler, world, imageStore);

            return false;
        }

        return true;
    }
    public PImage getCurrentImage()
    {
        return this.getImages().get(this.getImageIndex() % this.getImages().size());
    }

    public String log() {
        return this.getId().isEmpty() ? null :
                String.format("%s %d %d %d", this.getId(), this.getPosition().getX(), this.getPosition().getY(), this.getImageIndex());
    }
    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent(this, new Activity(this, world, imageStore), actionPeriod);
        scheduler.scheduleEvent(this, new Animation(this, 0), animationPeriod);
    }
}
