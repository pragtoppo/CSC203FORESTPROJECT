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
        switch (kind) {
            case ACTIVITY -> this.executeActivityAction(scheduler);
            case ANIMATION -> this.executeAnimationAction(scheduler);
        }
    }
    private void executeActivityAction(EventScheduler scheduler) {
        switch (entity.getKind()) {
            case SAPLING:
                entity.executeSaplingActivity(world, imageStore, scheduler);
                break;
            case TREE:
                entity.executeTreeActivity(world, imageStore, scheduler);
                break;
            case FAIRY:
                entity.executeFairyActivity(world, imageStore, scheduler);
                break;
            case DUDE_NOT_FULL:
                entity.executeDudeNotFullActivity(world, imageStore, scheduler);
                break;
            case DUDE_FULL:
                entity.executeDudeFullActivity(world, imageStore, scheduler);
                break;
            default:
                throw new UnsupportedOperationException(String.format("executeActivityAction not supported for %s", entity.getKind()));
        }
    }
    private void executeAnimationAction(EventScheduler scheduler) {
        Entity.nextImage(this.getEntity());

        if (this.repeatCount != 1) {
            scheduler.scheduleEvent(this.getEntity(), Functions.createAnimationAction(this.getEntity(), Math.max(this.repeatCount - 1, 0)), this.getEntity().getAnimationPeriod());
        }
    }


}
