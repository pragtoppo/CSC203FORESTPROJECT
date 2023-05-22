import java.util.*;

import processing.core.PImage;

/**
 * An entity that exists in the world. See EntityKind for the
 * different kinds of entities that exist.
 */
public interface Entity {

    /**
     * Helper method for testing. Preserve this functionality while refactoring.
     */
     String log() ;
     Point getPosition();
     String getId();
     void setPosition(Point p);
    void nextImage();
    PImage getCurrentImage();
    int getImageIndex();
    double getAnimationPeriod();
    void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore);
//    {
//        return switch (this.kind) {
//            case DUDE_FULL, DUDE_NOT_FULL, OBSTACLE, FAIRY, SAPLING, TREE -> this.animationPeriod;
//            default -> throw new UnsupportedOperationException(String.format("getAnimationPeriod not supported for %s", this.kind));
//        };
//    }


}