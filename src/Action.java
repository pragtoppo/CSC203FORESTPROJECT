/**
 * An action that can be taken by an entity
 */
public final class Action {
    private ActionKind kind;
    private Entity entity;
    private WorldModel world;
    private ImageStore imageStore;
    public int repeatCount;

    public Action(ActionKind kind, Entity entity, WorldModel world, ImageStore imageStore, int repeatCount) {
        this.kind = kind;
        this.entity = entity;
        this.world = world;
        this.imageStore = imageStore;
        this.repeatCount = repeatCount;
    }
    public ActionKind getKind()
    {
        return kind;
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

    public void executeAction(EventScheduler scheduler) {
        switch (this.getKind()) {
            case ACTIVITY -> this.executeAnimationAction( scheduler);
            case ANIMATION -> this.executeAnimationAction(scheduler);
        }
    }
    public void executeAnimationAction(EventScheduler scheduler) {
        Entity.nextImage(this.getEntity());

        if (this.repeatCount != 1) {
            scheduler.scheduleEvent(this.getEntity(), Functions.createAnimationAction(this.getEntity(), Math.max(this.repeatCount - 1, 0)), this.getEntity().getAnimationPeriod());
        }
    }


}
