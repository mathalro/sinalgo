package projects.bully.nodes.nodeImplementations;

import java.awt.Color; 
import java.awt.Graphics; 
import lombok.Getter;
import lombok.Setter;
import sinalgo.exception.WrongConfigurationException;
import sinalgo.gui.transformation.PositionTransformation; 
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
    private long countT = 0;
    private final long TTL = 5;
    private Color sscolor;

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

    private boolean initiateElection() {
        int countGreater = 0;
        this.state = State.WAIT_OK;
        System.out.print(this.getID()+" -> ");
        for (Edge e : this.getOutgoingConnections()) {
            if (this.getID() < e.getEndNode().getID()) {
                countGreater++;
                System.out.print(e.getEndNode().getID()+", ");
                ElectionMessage m = new ElectionMessage();
                this.send(m, e.getEndNode());
            }
        }
        System.out.println();
        return countGreater != 0;
    } 

    private void sendCoordinator() {
        for (Edge e : this.getOutgoingConnections()) {
            if (this.getID() > e.getEndNode().getID()) {
                CoordMessage m = new CoordMessage();
                this.send(m, e.getEndNode());
            }
        }
    }

    public void setSSColor(Color c) {
		this.sscolor=c;
	}

    @Override
    public void checkRequirements() throws WrongConfigurationException {

    }

    @Override
    public void handleMessages(Inbox inbox) {
        System.out.println(this.getID()+" "+this.state+" "+inbox.size());
        while (inbox.hasNext()) {
            Message m = inbox.next();
            if (m instanceof ElectionMessage) {
                System.out.println(this.getID()+" Receives election");
                if (this.state == State.DEFAULT) {
                    if (!this.initiateElection()) {
                        System.out.println(this.getID()+" Lider");
                        this.sendCoordinator();
                        this.state = State.LEADER;
                        this.countT = 0;
                    } else {
                        Message newM = new OkMessage();
                        this.send(newM, inbox.getSender());
                    }
                } else {
                    Message newM = new OkMessage();
                    this.send(newM, inbox.getSender());
                }
            } else if (m instanceof OkMessage) {
                System.out.println(this.getID()+" Receives OK");
                if (this.state == State.WAIT_OK) {
                    this.state = State.WAIT_COORD;
                }
            } else if (m instanceof CoordMessage) {
                System.out.println(this.getID()+" Receives coord");
                if (this.state == State.WAIT_COORD || this.state == State.WAIT_OK) {
                    this.state = State.DEFAULT;
                    this.leader = inbox.getSender().getID();
                    this.countT = 0;
                }
            }
        }

        if (this.countT == TTL && this.state == State.WAIT_OK) {
            this.sendCoordinator();
            this.countT = 0;
            this.state = State.LEADER;
        }
	}

    @Override
    public void init() {
        this.setSSColor(Color.ORANGE);
	}

    @Override
    public void neighborhoodChange() {
        
    }

    @Override
    public void preStep() {
        if (this.state == State.DEFAULT && this.leader == -1 ||
            this.countT == this.TTL && this.state == State.WAIT_COORD) {
            this.countT = 0;
            initiateElection();
        }
    }

    @Override
    public void postStep() {
        this.countT++;
        if(this.state == State.LEADER){
        	this.setSSColor(Color.BLUE);
        }
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(this.getID()+": ");
        for (Edge e : this.getOutgoingConnections()) {
            s.append(e.getEndNode().getID()+", ");
        }
        s.append("\n");
        s.append(this.leader+"\n");
        s.append(this.state);
        return s.toString() + "\n";
    }

    /* Function draw is used to display the node. */

	public void draw(Graphics g, PositionTransformation pt, boolean highlight) 
	{
		Long id = this.getID();
		this.setColor(this.sscolor);
		Color c  = Color.BLACK;
		super.drawNodeAsDiskWithText(g, pt, highlight, Long.toString(id), 20, c);

	}

}
