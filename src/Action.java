/**
 * An action that can be taken by an entity
 */
public abstract class Action {
    private Entity entity;
    private WorldModel world;
    private ImageStore imageStore;

    public Action( Entity entity) {
        this.entity = entity;
        this.world = world;
        this.imageStore = imageStore;
    }
    public Entity getEntity()
    {
        return entity;
    }
    public WorldModel getWorld()
    {
       return world;
    }
    public ImageStore getImageStore()
    {
        return imageStore;
    }

    public abstract void executeAction(EventScheduler scheduler);
}
