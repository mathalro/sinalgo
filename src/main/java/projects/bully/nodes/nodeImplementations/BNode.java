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
	private boolean back = false;
    private boolean reachable = true;
    private long leader = -1;
    private long countT = 0;
    private final long TTL = 6;
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

    public void setReachable(boolean value) {
    	this.reachable = value;
    	if (value && this.state == State.LEADER) {
    		this.back = true;
    	}
    }

    public boolean isReachable() {
    	return this.reachable;
    }

    private boolean initiateElection() {
        int countGreater = 0;
        this.state = State.WAIT_OK;
        for (Edge e : this.getOutgoingConnections()) {
            if (this.getID() < e.getEndNode().getID()) {
                countGreater++;
                ElectionMessage m = new ElectionMessage();
                this.send(m, e.getEndNode());
            }
        }
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

	public void recoloring() {

		if (!this.reachable) {
			this.setSSColor(Color.GRAY);
			return;
		}

		switch (this.state) {
			case DEFAULT: 
				this.setSSColor(Color.ORANGE);
				break;
			case LEADER:
				this.setSSColor(Color.BLUE);
				break;
			case WAIT_OK:
				this.setSSColor(Color.GREEN);
				break;
			case WAIT_COORD:
				this.setSSColor(Color.RED);
				break;
			default:
		}
	}

    private Node getLeaderNode() {
    	Node leaderNode = null;
    	for (Edge e : this.getOutgoingConnections()) {
    		if (this.leader == e.getEndNode().getID()) {
    			leaderNode = e.getEndNode();
    		}
    	}
    	return leaderNode;
    }

	public boolean testLeader() {
		BNode leaderNode = (BNode) this.getLeaderNode();
		if (!leaderNode.isReachable()) {
			this.leader = -1;
			return false;
		} else {
			return true;
		}
	}

    @Override
    public void checkRequirements() throws WrongConfigurationException {

    }

    @Override
    public void handleMessages(Inbox inbox) {
        while (inbox.hasNext()) {
            Message m = inbox.next();
            if (m instanceof ElectionMessage) {
                if (this.state == State.DEFAULT) {
                    if (!this.initiateElection()) {
                        this.sendCoordinator();
                        this.state = State.LEADER;
                        this.countT = 0;
                    } else {
                        Message newM = new OkMessage();
                        this.send(newM, inbox.getSender());
                    }
                } else if (this.reachable) {
                    Message newM = new OkMessage();
                    this.send(newM, inbox.getSender());
                }
            } else if (m instanceof OkMessage) {
                if (this.state == State.WAIT_OK) {
                    this.state = State.WAIT_COORD;
                }
            } else if (m instanceof CoordMessage) {
                this.state = State.DEFAULT;
                this.leader = inbox.getSender().getID();
                this.countT = 0;
            }
        }

        if (this.countT == TTL && this.state == State.WAIT_OK) {
            this.sendCoordinator();
            this.countT = 0;
            this.state = State.LEADER;
            this.leader = this.getID();
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
        if (this.state == State.DEFAULT && this.leader == -1 /*||
            this.countT == this.TTL && this.state == State.WAIT_COORD*/) {
            this.countT = 0;
            initiateElection();
        } else if (this.countT == this.TTL && this.state == State.WAIT_OK) {
        	this.sendCoordinator();
        	this.countT = 0;
        	this.state = State.LEADER;
        	this.leader = this.getID();
        } else if (this.back) {
        	this.back = false;
        	this.sendCoordinator();
        }
        recoloring();
    }

    @Override
    public void postStep() {
    	if (this.state != State.DEFAULT && this.state != State.LEADER)
        	this.countT++;
        else this.countT = 0;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("NODEID: "+this.getID());
        s.append("\nCONN: (");
        boolean first = true;
        for (Edge e : this.getOutgoingConnections()) {
        	if (!first) {
        		s.append(", ");
        	} else first = false;
            s.append(e.getEndNode().getID());
        }
        s.append(")\n");
        s.append("LEADER: "+ this.leader+"\n");
        s.append("STATE: "+ this.state+"\n");
        s.append("TTL: "+ this.countT);
        return s.toString() + "\n";
    }

    /* Function draw is used to display the node. */
	public void draw(Graphics g, PositionTransformation pt, boolean highlight) {
		Long id = this.getID();
		String content = content = Long.toString(id)+"("+(this.leader == -1 ? "-" : Long.toString(this.leader))+")";
		this.setColor(this.sscolor);
		Color c  = Color.BLACK;
		super.drawNodeAsDiskWithText(g, pt, highlight, content, 20, c);
	}

}
