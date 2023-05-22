import processing.core.PImage;

import java.util.List;
    public abstract class SAPTREE implements Entity
    {
        private String id;
        private Point position;
        private List<PImage> images;
        private int imageIndex;
        private int resourceLimit;
        private double actionPeriod;
        private double animationPeriod;
        private int health;
        public SAPTREE(String id, Point position, List<PImage> images, int health) {
            this.id = id;
            this.position = position;
            this.images = images;
            this.resourceLimit = 0;
            this.health = health;
            this.actionPeriod = actionPeriod;
            this.animationPeriod = animationPeriod;
        }
        public abstract boolean transformPlant(WorldModel world, EventScheduler scheduler, ImageStore imageStore) ;
        public abstract void decreaseHealth();
}
