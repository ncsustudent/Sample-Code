/**
 * Game Object Model for Java Game Engine
 * @version 1.4
 */
public class GameObject implements EventListener {
		private static final long serialVersionUID = 1L;
		private String GUID;
		
		/** Default Properties */
		Graphable   graphable   = null;
		Moveable    moveable    = null;
		Renderable  renderable  = null;
		Shapeable   shapeable   = null;
		Traceable   traceable   = null;
		Collideable collideable = null; 
		Connectable connectable = null;
		
		/**
		 * Simple Object constructor
		 * @param name, the GUID for the object
		 * @param x, coordinate of top left corner
		 * @param y, coordinate of top left corner
		 * @param w, width
		 * @param h, height
		 * @param b, if the object should collide
		 */
		public GameObject(String name, float x, float y, float w, float h, boolean b){
			this.GUID = name;
			graphable = new Graphable(x,y,w,h);
			collideable = new Collideable(b);
		}
		
		/**
		 * Sprite Object constructor
		 * @param image, path to image
		 * @param vx, x-direction of velocity
		 * @param vy, y-directino of velocity
		 */
		public GameObject(String name, String image, float x, float y, float w, float h, float vx, float vy, boolean b){
			this.GUID  = name;
			graphable  = new Graphable(x,y,w,h);
			moveable   = new Moveable(vx,vy);
			renderable = new Renderable(image);
			collideable = new Collideable(b);
		}
		
		//...
}

/**
 * Event Manager
 * @Version 1.2
 */
public class EventManager {
	//...
	/**
	 * Add a listener given an event
	 * @param Event
	 * @param EventListener
	 */
	public void registerListener(Event event, EventListener eventListen){
		//Check to see if the event exists by retrieving collection of listeners
		ArrayList<EventListener> eListeners = (ArrayList<EventListener>) listeners.get(event.getEventType());
		//If there are no listeners, create new event
		if(eListeners == null){
			eListeners = new ArrayList<EventListener>();
			listeners.put(event.getEventType(), eListeners);
		}
		
		//Check to see if the listener already exists via a for-each loop
		for(Iterator<EventListener> i = eListeners.iterator(); i.hasNext();){
			EventListener temp = (EventListener) i.next();
			//If it already exists, return
			if (temp == eventListen){
				return;
			}
		}
		
		//Add the listener
		eListeners.add(eventListen);		
	}
	//...
}