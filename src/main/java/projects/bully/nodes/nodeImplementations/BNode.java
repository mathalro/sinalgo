package projects.bully.nodes.nodeImplementation;

import sinalgo.nodes.Node;
import java.util.ArrayList;
import sinalgo.exception.WrongConfigurationException;
import sinalgo.nodes.messages.Inbox;

public class BNode extends Node {
	private int id;
	private ArrayList<BNode> neighbors;

	@Override
	public void init() {

	}

	@Override
	public void preStep() {

	}

	@Override
	public void postStep() {

	}

	@Override
    public void handleMessages(Inbox inbox) {

    }

	@Override
    public void neighborhoodChange() {
    
    }

	@Override
    public void checkRequirements() throws WrongConfigurationException {
    
    }

	@Override
	public String toString() {
		return this.id + "\n";
	}


}