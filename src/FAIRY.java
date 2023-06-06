import processing.core.PImage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class FAIRY implements Entity, ActivityEntity, AnimationEntity{
    String id;
    Point position;
    List<PImage> images;
    int imageIndex;
    double actionPeriod;
    double animationPeriod;


    public FAIRY(String id, Point position, double actionPeriod, double animationPeriod, List<PImage> images) {
        this.id = id;
        this.position = position;
        this.actionPeriod = actionPeriod;
        this.animationPeriod = animationPeriod;
        this.images = images;
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
    public void nextImage() {
        imageIndex =imageIndex + 1;
    }
    public double getAnimationPeriod()
    { return animationPeriod; }
    public String log() {
        return this.id.isEmpty() ? null :
                String.format("%s %d %d %d", this.id, this.position.getX(), this.position.getY(), this.imageIndex);
    }

    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Optional<Entity> fairyTarget = world.findNearest(this.position, new ArrayList<>(List.of(STUMP.class)));

        if (fairyTarget.isPresent()) {
            Point tgtPos = fairyTarget.get().getPosition();

            if (moveToFairy( world, fairyTarget.get(), scheduler)) {

                SAPLING sapling = new SAPLING(Functions.getSaplingKey() + "_" + fairyTarget.get().getId(), tgtPos, imageStore.getImageList(Functions.getSaplingKey()), 0);


                world.addEntity(sapling);
                sapling.scheduleActions(scheduler, world, imageStore);
            }
        }
        scheduler.scheduleEvent( this, new Activity(this, world, imageStore), this.actionPeriod);
    }
    public PImage getCurrentImage()
    {
        return this.getImages().get(this.getImageIndex() % this.getImages().size());
    }
    public boolean moveToFairy(WorldModel world, Entity target, EventScheduler scheduler) {
        if (Point.adjacent(this.getPosition(), target.getPosition())) {
            world.removeEntity( scheduler, target);
            return true;
        } else {
            Point nextPos = nextPositionFairy( world, target.getPosition());

            if (!this.getPosition().equals(nextPos)) {
                world.moveEntity( scheduler, this, nextPos);
            }
            return false;
        }
    }
    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent(this, new Activity(this, world, imageStore), this.actionPeriod);
        scheduler.scheduleEvent(this, new Animation(this, 0), this.animationPeriod);
    }
    public Point nextPositionFairy(WorldModel world, Point destPos) {
        AStarPathingStrategy pathingStrategy = new AStarPathingStrategy();
        Predicate<Point> canPassThrough = point -> !world.isOccupied(point) && world.withinBounds(point);
        BiPredicate<Point, Point> withinReach = Point::adjacent;
        List<Point> points = pathingStrategy.computePath(this.position, destPos, canPassThrough, withinReach, PathingStrategy.CARDINAL_NEIGHBORS );
        if(points.size() == 0)
        {
            return this.position;
        }
        return points.get(0);
//        int horiz = Integer.signum(destPos.getX() - this.getPosition().getX());
//        Point newPos = new Point(this.getPosition().getX() + horiz, this.getPosition().getY());
//        if (horiz == 0 || world.isOccupied( newPos) && world.getOccupancyCell( newPos).getClass() != HOUSE.class) {
//            int vert = Integer.signum(destPos.getY() - this.getPosition().getY());
//            newPos = new Point(this.getPosition().getX(), this.getPosition().getY() + vert);
//
//            if (vert == 0 || world.isOccupied( newPos) && world.getOccupancyCell( newPos).getClass() != HOUSE.class) {
//                newPos = this.getPosition();
//            }
//        }

        //return newPos;
    }

}

