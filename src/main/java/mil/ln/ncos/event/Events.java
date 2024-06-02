package mil.ln.ncos.event;

import org.springframework.context.ApplicationEventPublisher;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Events {
	
    private static ThreadLocal<ApplicationEventPublisher> publisherLocal = new ThreadLocal<>();
   
    public static void raise(AppEvent event) {
    	log.debug("raise............................");
        if (event == null) {
        	return;
        }
        if (publisherLocal.get() != null) {
        	log.debug("publishEvent..........");
            publisherLocal.get().publishEvent(event);
        }
        else {
        	log.debug("publisherLocal.get() is null..............");
        }
    }

    static void setPublisher(ApplicationEventPublisher publisher) {
        publisherLocal.set(publisher);
    }

    static void reset() {
        publisherLocal.remove();
    }

}
