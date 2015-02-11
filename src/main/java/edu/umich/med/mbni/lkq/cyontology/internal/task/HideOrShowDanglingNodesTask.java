package edu.umich.med.mbni.lkq.cyontology.internal.task;

import org.cytoscape.model.CyNetwork;
import org.cytoscape.task.AbstractNetworkViewTask;
import org.cytoscape.view.layout.CyLayoutAlgorithm;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.presentation.property.BasicVisualLexicon;
import org.cytoscape.work.TaskMonitor;

import edu.umich.med.mbni.lkq.cyontology.internal.app.MyApplicationCenter;
import edu.umich.med.mbni.lkq.cyontology.internal.app.MyApplicationManager;
import edu.umich.med.mbni.lkq.cyontology.internal.model.ExpandableNode;
import edu.umich.med.mbni.lkq.cyontology.internal.model.OntologyNetwork;
import edu.umich.med.mbni.lkq.cyontology.internal.utils.ViewOperationUtils;

public class HideOrShowDanglingNodesTask extends AbstractNetworkViewTask {

	private boolean isShowing;
	private MyApplicationManager appManager;

	public HideOrShowDanglingNodesTask(CyNetworkView view, boolean isShowing) {
		super(view);
		this.isShowing = isShowing;
		appManager = MyApplicationCenter.getInstance().getApplicationManager();
	}

	@Override
	public void run(TaskMonitor taskMonitor) {
		CyNetwork underlyingNetwork = view.getModel();
		OntologyNetwork encapsulatingNetwork = MyApplicationCenter
				.getInstance().getEncapsulatingOntologyNetwork(
						underlyingNetwork);
		if (encapsulatingNetwork == null)
			return;

		for (Long nodeSUID : encapsulatingNetwork.getAllRootNodes()) {
			ExpandableNode node = encapsulatingNetwork.getNode(nodeSUID);
			if (node.getChildNodes().isEmpty()) {
				view.getNodeView(node.getCyNode()).setVisualProperty(
						BasicVisualLexicon.NODE_VISIBLE, isShowing);
			}
		}
		
		ViewOperationUtils.reLayoutNetwork(appManager.getCyLayoutAlgorithmManager(), view, "hierarchical", CyLayoutAlgorithm.ALL_NODE_VIEWS);
		view.updateView();
	}

}
