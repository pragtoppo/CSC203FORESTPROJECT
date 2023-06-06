import processing.core.PImage;

import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public abstract class DUDEAbstract implements Entity, ActivityEntity, AnimationEntity {
    private String id;
    private Point position;
    private List<PImage> images;
    private int imageIndex;
    private int resourceLimit;
    private double actionPeriod;
    private double animationPeriod;

    public int getResourceLimit() {
        return resourceLimit;
    }


    public double getActionPeriod() {
        return actionPeriod;
    }

    public String getId() {
        return id;
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point p) {
        position = p;
    }

    public List<PImage> getImages() {
        return images;
    }
    public double getAnimationPeriod()
    { return animationPeriod; }

    public int getImageIndex() {
        return imageIndex;
    }
    public void setImage(int i) {
        imageIndex = i;
    }


    public DUDEAbstract( String id, Point position, double actionPeriod, double animationPeriod, int resourceLimit, List<PImage> images) {
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
        this.resourceLimit = resourceLimit;
        this.actionPeriod = actionPeriod;
        this.animationPeriod = animationPeriod;
    }

     Point nextPositionDude(WorldModel world, Point destPos) {
         AStarPathingStrategy pathingStrategy = new AStarPathingStrategy();
         Predicate<Point> canPassThrough = point -> (!world.isOccupied(point) || world.getOccupancyCell(point) instanceof STUMP) && world.withinBounds(point);
         BiPredicate<Point, Point> withinReach = Point::adjacent;
         List<Point> points = pathingStrategy.computePath(this.position, destPos, canPassThrough, withinReach, PathingStrategy.CARDINAL_NEIGHBORS );
         if(points.size() == 0)
         {
             return this.position;
         }
         return points.get(0);
//        int horiz = Integer.signum(destPos.getX() - this.position.getX());
//        Point newPos = new Point(this.position.getX() + horiz, this.position.getY());
//
//        if (horiz == 0 || world.isOccupied(newPos) && world.getOccupancyCell(newPos).getClass() != STUMP.class) {
//            int vert = Integer.signum(destPos.getY() - this.position.getY());
//            newPos = new Point(this.position.getX(), this.position.getY() + vert);
//
//            if (vert == 0 || world.isOccupied(newPos) && world.getOccupancyCell(newPos).getClass() != STUMP.class) {
//                newPos = this.position;
//            }
//        }
//
//        return newPos;
    }

}
