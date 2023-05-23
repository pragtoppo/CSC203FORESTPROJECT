public class Activity extends Action{
    private Entity entity;
    private  WorldModel world;
    private ImageStore imageStore;
    public Activity(Entity entity, WorldModel world, ImageStore imageStore) {
        super(entity);
        this.world = world;
        this.imageStore =imageStore;
    }

    public void executeAction(EventScheduler scheduler) {
        if(this.getEntity() instanceof ActivityEntity)
        {
            ((ActivityEntity)getEntity()).executeActivity(this.getWorld(), this.getImageStore(), scheduler);
        }
    }
}
