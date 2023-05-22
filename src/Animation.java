public class Animation extends Action{
    private int repeatCount;
    private Entity entity;
    public Animation(Entity entity, int repeatCount) {
        super(entity);
        this.repeatCount = repeatCount;
    }
    @Override
    public void executeAction(EventScheduler scheduler) {
        getEntity().nextImage();

        if (this.repeatCount != 1) {
            this.repeatCount = Math.max(this.repeatCount - 1, 0);
            scheduler.scheduleEvent(this.getEntity(), new Animation(this.getEntity(), Math.max(this.repeatCount - 1, 0)), ((AnimationEntity)getEntity()).getAnimationPeriod());
        }
    }

}
