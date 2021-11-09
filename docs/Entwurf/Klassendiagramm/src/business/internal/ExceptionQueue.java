package business.internal;

import javax.enterprise.context.SessionScoped;

@SessionScoped
public class ExceptionQueue {

    @Inject
    private PropertyChangeSupport changeSupport;

    private Queue<Throwable> queue;

    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
    }

    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        support.removePropertyChangeListener(pcl);
    }

    public void add(Throwable throwable) {
        support.firePropertyChange("exception", this.queue, this.queue.offer(throwable));
    }
}