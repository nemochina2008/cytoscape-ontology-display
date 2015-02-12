package edu.umich.med.mbni.lkq.cyontology.internal.task;

import java.util.LinkedList;

import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.cytoscape.task.AbstractNodeViewTask;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.model.View;
import org.cytoscape.view.presentation.property.BasicVisualLexicon;
import org.cytoscape.work.TaskMonitor;

import edu.umich.med.mbni.lkq.cyontology.internal.app.MyApplicationCenter;
import edu.umich.med.mbni.lkq.cyontology.internal.model.ExpandableNode;
import edu.umich.med.mbni.lkq.cyontology.internal.model.OntologyNetwork;

public class SelectChildNodeTask extends AbstractNodeViewTask {

	public SelectChildNodeTask(View<CyNode> nodeView, CyNetworkView netView) {
		super(nodeView, netView);
	}

	@Override
	public void run(TaskMonitor taskMonitor) throws Exception {
		if (!MyApplicationCenter.getInstance().hasEncapsulatingOntologyNetwork(
				netView.getModel()))
			return;
		
		taskMonitor.setProgress(0.0);
		
		CyNetwork underlyingNetwork = netView.getModel();

		OntologyNetwork ontologyNetwork = MyApplicationCenter.getInstance()
				.getEncapsulatingOntologyNetwork(underlyingNetwork);
		ExpandableNode expandableNode = ontologyNetwork.getNode(nodeView.getModel());

		LinkedList<ExpandableNode> allChildNodes = new LinkedList<ExpandableNode>();
		LinkedList<ExpandableNode> queue = new LinkedList<ExpandableNode>();
		queue.add(expandableNode);
		
		allChildNodes.add(expandableNode);
		while (!queue.isEmpty()) {
			ExpandableNode currentRoot = queue.poll();
			allChildNodes.addAll(currentRoot.getChildNodes());
			queue.addAll(currentRoot.getChildNodes());
		}
		
		taskMonitor.setProgress(0.3);
		
		for (ExpandableNode node : allChildNodes) {
			if (netView.getNodeView(node.getCyNode()).getVisualProperty(BasicVisualLexicon.NODE_VISIBLE)) {
				netView.getModel().getRow(node.getCyNode()).set("selected", true);
			}
		}
		taskMonitor.setProgress(0.8);
		
		netView.updateView();
		taskMonitor.setProgress(1.0);
		
	}

}