public class Animation extends Action{
    private int repeatCount;
    public Animation(Entity entity, int repeatCount) {
        super(entity);
        this.repeatCount = repeatCount;
    }

    @Override
    public WorldModel getWorld() {
        return null;
    }

    @Override
    public ImageStore getImageStore() {
        return null;
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
