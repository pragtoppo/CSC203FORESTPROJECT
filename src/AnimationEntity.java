public interface AnimationEntity {
    void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore);
    double getAnimationPeriod();
}
