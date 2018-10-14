package projects.bully.nodes.messages;

import lombok.Getter;
import lombok.Setter;
import sinalgo.nodes.messages.Message;

public class CoordMessage extends Message {
	@Override
	public Message clone() {
		return new CoordMessage();
	}
}