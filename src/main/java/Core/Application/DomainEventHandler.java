package Core.Application;


import java.util.ArrayList;

public class DomainEventHandler {

    private ArrayList<DomainEvent> observers = new ArrayList<>();

    public void addDomainEvents(DomainEvent e){
        observers.add(e);
    }

    public boolean notifyListeners(){
        boolean once = true;
        for (DomainEvent event : observers){
            if(!event.update()){
                once = false;
                break;
            }
        }
        return once;
    }
}
