import processing.core.PImage;

import java.util.List;

public class STUMP implements Entity{
    private String id;
    private Point position;
    private List<PImage> images;
    private int imageIndex;
    private double animationPeriod;
    private double actionPeriod;
    public STUMP(String id, Point position, List<PImage> images) {
        this.id = id;
        this.position = position;
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
    public int getImageIndex() {
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
    public PImage getCurrentImage()
    {
        return this.getImages().get(this.getImageIndex() % this.getImages().size());
    }
}
