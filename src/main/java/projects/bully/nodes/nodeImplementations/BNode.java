package projects.bully.nodes.nodeImplementations;

import lombok.Getter;
import lombok.Setter;
import sinalgo.exception.WrongConfigurationException;
import sinalgo.nodes.Node;
import sinalgo.nodes.messages.Inbox;
import sinalgo.nodes.messages.Message;
import projects.bully.nodes.messages.*;
import sinalgo.nodes.edges.Edge;

/**
 * The class to simulate the bully leader election algorithm.
 */
public class BNode extends Node {

    @Getter
    @Setter
    private long leader = -1;

    private State state = State.DEFAULT;
    private enum State {
        DEFAULT,
        LEADER,
        WAIT_OK,
        WAIT_COORD;

        public int value;
        public int getValue() {
            return value;
        }
    }

    private void initiateElection() {
        this.state = State.WAIT_OK;
        System.out.print(this.getID()+" -> ");
        for (Edge e : this.getOutgoingConnections()) {
            if (this.getID() < e.getEndNode().getID()) {
                System.out.print(e.getEndNode().getID()+", ");
                ElectionMessage m = new ElectionMessage();
                this.send(m, e.getEndNode());
            }
        }
        System.out.println();
    } 

    private void sendCoordinator() {
        for (Edge e : this.getOutgoingConnections()) {
            if (this.getID() > e.getEndNode().getID()) {
                CoordMessage m = new CoordMessage();
                this.send(m, e.getEndNode());
            }
        }
    }

    @Override
    public void checkRequirements() throws WrongConfigurationException {

    }

    @Override
    public void handleMessages(Inbox inbox) {
        System.out.println(this.getID()+" "+this.state);
        switch (this.state) {
            case WAIT_OK:
                System.out.println(this.getID()+" - 1 - "+inbox.hasNext());
                boolean hasOk = false;
                while (inbox.hasNext()) {
                    Message m = inbox.next();
                    if (m instanceof OkMessage) { // TODO it's necessary verify if the message is ok type 
                        hasOk = true;
                        break;
                    }
                }
                if (hasOk) { 
                    this.state = State.WAIT_COORD;
                } else {
                    this.state = State.LEADER;
                    this.sendCoordinator();
                }
                break;
            case WAIT_COORD:
                System.out.println(this.getID()+" - 2");
                boolean hasCoord = false;
                long coord = -1;
                while (inbox.hasNext()) {
                    Message m = inbox.next();
                    if (m instanceof CoordMessage) { // TODO it's necessary verify if the message is coordinator type
                        hasCoord = true;
                        coord = inbox.getSender().getID();
                        break;
                    }
                }
                if (hasCoord) {
                    this.leader = coord;
                    this.state = State.DEFAULT;
                } else {
                    this.initiateElection();
                }
                break;
            default:
                System.out.println(this.getID()+" - 3");
                while (inbox.hasNext()) {
                    Message m = inbox.next();
                    if (m instanceof ElectionMessage) {
                        Message newM = new OkMessage();
                        this.send(newM, inbox.getSender());
                        if (this.state != State.WAIT_OK)
                            this.initiateElection();
                    }
                }
        }
	}

    @Override
    public void init() {
        
	}

    @Override
    public void neighborhoodChange() {
        
    }

    @Override
    public void preStep() {
        if (this.leader == -1) {
            initiateElection();
        }
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
        s.append("\n");
        s.append(this.leader);
        return s.toString() + "\n";
    }

}
