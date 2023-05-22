import processing.core.PImage;

import java.util.List;

public class SAPLING extends SAPTREE{
    private String id;
    private Point position;
    private List<PImage> images;
    private int imageIndex;
    private int health;
    private final int healthLimit =5;
    private final double animationPeriod = 1.000;
    private final double actionPeriod = 1.000;



    public SAPLING(String id, Point position, List<PImage> images, int health) {
        super(id, position, images, health);
    }
    public double getAnimationPeriod()
    { return animationPeriod; }
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

    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        health++;
        if (transformPlant( world, scheduler, imageStore)) {
            scheduler.scheduleEvent(this,new Activity(this, world, imageStore), actionPeriod);
        }
    }

    public void nextImage() {
        imageIndex =imageIndex + 1;
    }
    public void decreaseHealth()
    {
        health--;
    }

    public boolean transformPlant(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        if (health<= 0) {
            STUMP stump = new STUMP(Functions.getStumpKey() + "_" + this.getId(), this.position,
                    imageStore.getImageList(Functions.getStumpKey()));

            world.removeEntity( scheduler, this);

            world.addEntity(stump);

            return true;
        } else if (this.health>= this.healthLimit) {
            TREE tree = new TREE(Functions.getTreeKey() + "_" + this.id, this.position,
                    imageStore.getImageList(Functions.TREE_KEY),
                    Functions.getNumFromRange(Functions.TREE_ACTION_MAX, Functions.TREE_ACTION_MIN),
                    Functions.getNumFromRange(Functions.TREE_ANIMATION_MAX, Functions.TREE_ANIMATION_MIN),
                    Functions.getIntFromRange(Functions.TREE_HEALTH_MAX, Functions.TREE_HEALTH_MIN));

            world.removeEntity( scheduler, this);

            world.addEntity(tree);
            tree.scheduleActions(scheduler, world, imageStore);

            return true;
        }

        return false;
    }
    public PImage getCurrentImage()
    {
        return this.getImages().get(this.getImageIndex() % this.getImages().size());
    }
//    public boolean transformPlant(WorldModel world, EventScheduler scheduler, ImageStore imageStore)
//    {
////        if (this.kind == EntityKind.TREE) {
////            return !transformTree( world, scheduler, imageStore);
////        } else if (this.kind == EntityKind.SAPLING) {
//            return !transformSapling( world, scheduler, imageStore);
////        } else {
////            throw new UnsupportedOperationException(String.format("transformPlant not supported for %s", this));
////        }
//    }

    public String log() {
        return this.id.isEmpty() ? null :
                String.format("%s %d %d %d", this.id, this.position.getX(), this.position.getY(), this.imageIndex);
    }
    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent(this, new Activity(this, world, imageStore), actionPeriod);
        scheduler.scheduleEvent(this, new Animation(this, 0), animationPeriod);
    }
}
