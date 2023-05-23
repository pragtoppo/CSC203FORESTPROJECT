import processing.core.PImage;

import java.util.List;
    public abstract class SAPTREE implements Entity, ActivityEntity, AnimationEntity
    {
        private String id;
        private Point position;
        private List<PImage> images;
        private int imageIndex;
        private int health;
        public SAPTREE(String id, Point position, List<PImage> images, int health) {
            this.id = id;
            this.position = position;
            this.images = images;
            this.health = health;
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
       public void setHealth(int h)
       {
           health = h;
       }
       public void setImages(int i)
       {
           imageIndex = i;
       }
        public int getHealth() {
            return health;
        }
        public void decreaseHealth()
        {
            health--;
        }
        public abstract boolean transformPlant(WorldModel world, EventScheduler scheduler, ImageStore imageStore) ;

}
