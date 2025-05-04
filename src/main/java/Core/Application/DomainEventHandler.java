package Core.Application;


import java.util.ArrayList;

public class DomainEventHandler {

    private ArrayList<DomainEvent> observers = new ArrayList<>();

    public void addDomainEvents(DomainEvent e){
        observers.add(e);
    }

    public void notifyListeners(){
        for (DomainEvent event : observers){
            event.update();
        }
    }
}
