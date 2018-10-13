package projects.bully.nodes.nodeImplementations;

import lombok.Getter;
import sinalgo.exception.WrongConfigurationException;
import sinalgo.nodes.Node;
import sinalgo.nodes.messages.Inbox;
import sinalgo.nodes.edges.Edge;

/**
 * The class to simulate the bully leader election algorithm.
 */
public class BNode extends Node {

    @Getter
    private Node leader = null;

    private void initiateElection() {
        for (Edge e : this.getOutgoingConnections()) {
            if (this.getID() < e.getEndNode().getID()) {
                // send a election message to this node
            }
        }
    } 

    @Override
    public void checkRequirements() throws WrongConfigurationException {

    }

    @Override
    public void handleMessages(Inbox inbox) {
 
	}

    @Override
    public void init() {
        
	}

    @Override
    public void neighborhoodChange() {
        
    }

    @Override
    public void preStep() {
    
    }

    @Override
    public void postStep() {

    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(this.getID()+": ");
        for (Edge e : this.getOutgoingConnections()) {
            s.append(e.getEndNode().getID()+", ");
        }
        return s.toString();
    }

}
