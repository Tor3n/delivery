package Core.Application;


import java.util.ArrayList;

public class DomainEventHandler {

    private ArrayList<DomainEvent> events = new ArrayList<>();

    public void addDomainEvents(DomainEvent e){
        events.add(e);
    }

    public void notifyListeners(){
        for (DomainEvent event : events){
            event.update();
        }
    }
}
