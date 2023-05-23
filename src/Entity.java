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

}