package projects.bully.models.connectivityModels;

import sinalgo.nodes.Node;
import sinalgo.models.ConnectivityModelHelper;

public class Connection extends ConnectivityModelHelper {
	
	@Override
	protected boolean isConnected(Node from, Node to) {
		return true;
	}

}