package projects.gossip.nodes.messages;

import java.util.HashMap;
import java.util.Map;
import javafx.util.Pair;

import lombok.Getter;
import lombok.Setter;
import sinalgo.nodes.messages.Message;

public class GMessage extends Message {

	private HashMap<Long, Pair<Long, Boolean>> map = new HashMap<>();;

	public GMessage() {
		
	}

	public GMessage(HashMap<Long, Pair<Long, Boolean>> _map) {
		this.map = _map;
	}

	@Override
	public Message clone() {
		return new GMessage(this.map);
	}

	public HashMap<Long, Pair<Long, Boolean>> getMap() {
		return this.map;
	}

	public void addEntry(Long key, Pair<Long, Boolean> value) {
		map.put(key, value);
	}

}