package eventos;

import objetos.Configuracion;
import javafx.event.Event;
import javafx.event.EventType;

public class ConfiguracionEvent extends Event {

	private static final long serialVersionUID = 1L;
	
	private Configuracion conf;
	
	public ConfiguracionEvent(EventType<? extends Event> arg0, Configuracion conf) {
		super(arg0);
		this.conf = conf;
		
	}

	public Configuracion getConf() {
		return conf;
	}

}
