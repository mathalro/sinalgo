package projects.bully.nodes.messages;

import lombok.Getter;
import lombok.Setter;
import sinalgo.nodes.messages.Message;

public class ElectionMessage extends Message {
	@Override
	public Message clone() {
		return new ElectionMessage();
	}
}