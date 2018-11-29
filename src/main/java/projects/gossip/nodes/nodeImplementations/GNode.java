package projects.gossip.nodes.nodeImplementations;

import java.awt.Color; 
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import javafx.util.Pair;

import lombok.Getter;
import lombok.Setter;
import sinalgo.exception.WrongConfigurationException;
import sinalgo.gui.transformation.PositionTransformation; 
import sinalgo.nodes.Node;
import sinalgo.nodes.messages.Inbox;
import sinalgo.nodes.messages.Message;
import projects.gossip.nodes.messages.*;
import sinalgo.nodes.edges.Edge;
import projects.gossip.nodes.GossipTableEntry;

/**
 * The class to simulate the gossip style failure detection
 */
public class GNode extends Node {
    private final Long timeFail = 5L;
    private final Long timeCleanup = 5L;
    private final int randomSend = 2;
    private Long localTime = 0L;
    private Map<Long, GossipTableEntry> table;
    private Random seed = new Random();

    private void sendMembership() {
        boolean ok = false;
		ArrayList<Long> rand = new ArrayList<>(table.size());
    	while (!ok || table.size() < 2) {
    		rand = new ArrayList<>(table.size());

	        for (Map.Entry<Long, GossipTableEntry> entry : table.entrySet()) 
	   			rand.add(new Long(entry.getKey()));

    		ok = true;
	        Collections.shuffle(rand, seed);

	        rand = new ArrayList(rand.subList(0, randomSend));
	        for (Long i : rand) {
	        	if (i == this.getID()) ok = false;
	        }
	    }

        for (Edge e : this.getOutgoingConnections()) {
            Long id = e.getEndNode().getID();
            if (rand.contains(id)) {
                GMessage g = new GMessage();
                for (Map.Entry<Long, GossipTableEntry> entry : table.entrySet())
                	g.addEntry(entry.getKey(), new Pair(entry.getValue().getHeartBitCounter(), entry.getValue().getFail()));
                this.send(g, e.getEndNode());
            }
        }
    }

    @Override
    public void checkRequirements() throws WrongConfigurationException {

    }

    @Override
    public void handleMessages(Inbox inbox) {

        while (inbox.hasNext()) {
            Message message = inbox.next();
            if (message instanceof GMessage) {
                GMessage m = (GMessage) message;
                HashMap<Long, Pair<Long, Boolean>> map = m.getMap();
                for (Map.Entry<Long, Pair<Long, Boolean>> entry : map.entrySet()) {
                    Long key = entry.getKey();
                    Pair<Long, Boolean> value = entry.getValue();

                    if (this.table.containsKey(key) && value.getValue() && !this.table.get(key).getFail()) {
                    	GossipTableEntry up = new GossipTableEntry(key, this.table.get(key).getHeartBitCounter(), this.localTime, true);
                    	this.table.put(key, up);
                    } else if (this.table.containsKey(key) && this.table.get(key).getHeartBitCounter() < value.getKey() && !this.table.get(key).getFail()) {
                        GossipTableEntry up = new GossipTableEntry(key, value.getKey(), this.localTime);
                    	this.table.put(key, up);
                    }
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
        if (table == null) {
            table = new HashMap<>();
            GossipTableEntry g = new GossipTableEntry(this.getID(), 0L, this.localTime);
            table.put(this.getID(), g);
            for (Edge e : this.getOutgoingConnections()) {
                Long id = e.getEndNode().getID();
                g = new GossipTableEntry(id, 0L, this.localTime);
                table.put(id, g);
            }
        }
        sendMembership();
    }

    @Override
    public void postStep() {
        this.localTime++;
        ArrayList<Long> toRemove = new ArrayList<>();
        for (Map.Entry<Long, GossipTableEntry> entry : table.entrySet()) {
            GossipTableEntry g = entry.getValue();

			if (g.getFail() && this.localTime - g.getLocalTime() > this.timeCleanup) {
            	toRemove.add(entry.getKey());
            } else if (this.localTime - g.getLocalTime() > this.timeFail) {
                g.setFail(true);
                g.setLocalTime(this.localTime);
            }

            System.out.println(g.getFail()+" "+this.localTime+" "+g.getLocalTime());

            if (this.getID() == entry.getKey()) {
                g.incrementHeartBit();
                g.setLocalTime(this.localTime);
                table.put(entry.getKey(), g);
            } 
        }

        System.out.println(toRemove.size());
        for (Long rem : toRemove) {
        	this.table.remove(rem);
        }

    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();

        s.append("Current Local Time: "+ this.localTime+"\t\t\t|\n\n");
        s.append("ID\tHB\tLC\tFA\n");

        for (Map.Entry<Long, GossipTableEntry> entry : table.entrySet()) {
            s.append(entry.getKey()+"\t"+
                     entry.getValue().getHeartBitCounter()+"\t"+
                     entry.getValue().getLocalTime()+"\t"+
                     entry.getValue().getFail()+"\n");
        }

        return s.toString();
    }

	public void draw(Graphics g, PositionTransformation pt, boolean highlight) {
		Long id = this.getID();
		String content = content = Long.toString(id);
		Color c  = Color.WHITE;
		super.drawNodeAsDiskWithText(g, pt, highlight, content, 20, c);
	}
}