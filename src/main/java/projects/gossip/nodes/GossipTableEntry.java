package projects.gossip.nodes;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GossipTableEntry {

    private Long id;
    private Long heartBitCounter;
    private Long localTime;
    private Boolean fail;

    public GossipTableEntry(Long _id, Long _heartBitCounter, Long _localTime) {
        id = _id;
        heartBitCounter = _heartBitCounter;
        localTime = _localTime;
        fail = false;
    }

    public GossipTableEntry(Long _id, Long _heartBitCounter, Long _localTime, Boolean _fail) {
        id = _id;
        heartBitCounter = _heartBitCounter;
        localTime = _localTime;
        fail = _fail;
    }

    public void incrementHeartBit() {
        this.heartBitCounter++;
    }
}