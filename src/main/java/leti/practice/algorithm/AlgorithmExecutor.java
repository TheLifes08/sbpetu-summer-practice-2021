package leti.practice.algorithm;

import leti.practice.structures.graph.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ArrayList;

public class AlgorithmExecutor {
    private static final Logger logger = Logger.getLogger(AlgorithmExecutor.class.getName());
    private ResidualNetwork<Double> network;
    private ArrayList<ResidualNetwork<Double>> networkStates;
    private double maxFlow;
    private boolean isNetworkCorrect;
    private boolean isAlgprithmEnd;
    private HashSet<Node> amountOfNodes;
    public AlgorithmExecutor(){
        network = null;
        networkStates = null;
        maxFlow = 0.0;
        isNetworkCorrect = false;
        isAlgprithmEnd = false;
    }
    public boolean setNetwork(ResidualNetwork<Double> network){
        if(network==null) {
            logger.log(Level.INFO, "Error network is empty\n");
            return false;
        }
        if(network.getSource()== null && network.getDestination()==null){
            return false;
        }
        this.network = null;
        this.network = network; //могу копировать, могу не копировать, надо обсудить
        isNetworkCorrect = checkNetwork();
        isNetworkCorrect = initializeNetwork();
        return isNetworkCorrect;
    }

    public ResidualNetwork<Double> getNetwork(){
        return network;
    }

    public double getMaxFlow(){
        if(isAlgprithmEnd){
            for(Node dest : network.getReverseNetworkEdges(network.getDestination()).keySet()){
                maxFlow += network.getReverseNetworkEdges(network.getDestination()).get(dest).getFlow();
            }
        }
        return maxFlow;
    }

    private boolean checkNetwork(){
        for(Node from : network.getNetworkNodes()){
            for (Node to : network.getNetworkEdges(from).keySet()){
                if(network.getNetworkEdges(from).get(to).getCapacity() < 0.0
                        || network.getNetworkEdges(from).get(to).getFlow() < 0.0
                        || network.getNetworkEdges(from).get(to).getFlow() >
                        network.getNetworkEdges(from).get(to).getCapacity()){
                    return false;

                }
            }
        }
        return true;
    }

    private boolean initializeNetwork(){
        if(isNetworkCorrect){
            amountOfNodes = new HashSet<>(network.getNetworkNodes());
            amountOfNodes.addAll(network.getReverseNetworkNodes());
            /*Initialization of heights and surplus function*/
            for (Node node:amountOfNodes){
                network.getHeights().put(node, 0);
                network.getSurpluses().put(node, 0.0);
            }
            network.getHeights().put(network.getSource(), amountOfNodes.size());
            networkStates.add(network.copy());
            /*First algorithm step*/
            for(Node to : network.getNetworkEdges(network.getSource()).keySet()){
                //add flow to edge
                network.getNetworkEdges(network.getSource()).get(to).setFlow(network.getNetworkEdges(network.getSource()).get(to).getCapacity());
                //add surplus to the node
                network.getSurpluses().put(to, network.getNetworkEdges(network.getSource()).get(to).getCapacity());
                //add flow to the reverse edge
                network.getReverseNetworkEdges(to).get(network.getSource()).setFlow(network.getReverseNetworkEdges(to).get(network.getSource()).getCapacity());
            }
            amountOfNodes.remove(network.getDestination());
            amountOfNodes.remove(network.getSource());
            return true;
        }
        return false;
    }

    private boolean push(){
        if(isNetworkCorrect){
            for(Node node : amountOfNodes){
                //Если переполнение
                if(network.getSurpluses().get(node)>0){
                    if(network.getNetworkNodes().contains(node)){
                        for(Node to :network.getNetworkEdges(node).keySet()){
                            //если можно пропустить поток через ребро
                            if(network.getNetworkEdges(node).get(to).getCapacity() - network.getNetworkEdges(node).get(to).getFlow() > 0.0){
                                if(network.getHeights().get(node) == network.getHeights().get(to) + 1){
                                    double availableAmountOfFlow = Math.min(network.getSurpluses().get(node), network.getNetworkEdges(node).get(to).getCapacity() - network.getNetworkEdges(node).get(to).getFlow());
                                    network.getNetworkEdges(node).get(to).setFlow(network.getNetworkEdges(node).get(to).getFlow() + availableAmountOfFlow);
                                    network.getReverseNetworkEdges(to).get(node).setFlow(network.getReverseNetworkEdges(to).get(node).getFlow() + availableAmountOfFlow);
                                    network.getSurpluses().put(node, network.getSurpluses().get(node) - availableAmountOfFlow);
                                    network.getSurpluses().put(to, network.getSurpluses().get(to) + availableAmountOfFlow);
                                    logger.log(Level.INFO, "Make push with node {0}.", node.getName());
                                    return true;
                                }
                            }
                        }
                    }else if(network.getReverseNetworkNodes().contains(node)){
                        for (Node to : network.getReverseNetworkEdges(node).keySet()){
                            //если можно пропустить поток через обратное ребро
                            if(network.getReverseNetworkEdges(node).get(to).getFlow()> 0.0){
                                if(network.getHeights().get(node) == network.getHeights().get(to) + 1){
                                    double availableAmountOfFlow = Math.min(network.getSurpluses().get(node), network.getNetworkEdges(node).get(to).getFlow());
                                    network.getReverseNetworkEdges(node).get(to).setFlow(network.getReverseNetworkEdges(node).get(to).getFlow()-availableAmountOfFlow);
                                    network.getNetworkEdges(to).get(node).setFlow(network.getNetworkEdges(to).get(node).getFlow() - availableAmountOfFlow);
                                    network.getSurpluses().put(node, network.getSurpluses().get(node) - availableAmountOfFlow);
                                    network.getSurpluses().put(to, network.getSurpluses().get(to) + availableAmountOfFlow);
                                    logger.log(Level.INFO, "Make push with node {0}.", node.getName());
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    private boolean relabel(){
        if(isNetworkCorrect){
            for(Node node : amountOfNodes) {
                //Если переполнение
                boolean flag = false;
                if (network.getSurpluses().get(node) > 0) {
                    if(network.getNetworkNodes().contains(node)){
                        ArrayList<Integer> heights = new ArrayList<>();
                        for(Node to :network.getNetworkEdges(node).keySet()){
                            heights.add(network.getHeights().get(to));
                            if(network.getHeights().get(node) > network.getHeights().get(to)){
                                flag = true;
                                break;
                            }
                        }
                        if(network.getReverseNetworkNodes().contains(node) && !flag){
                            for (Node to : network.getReverseNetworkEdges(node).keySet()){
                                heights.add(network.getHeights().get(to));
                                if(network.getHeights().get(node) > network.getHeights().get(to)){
                                    flag = true;
                                    break;
                                }
                            }
                        }
                        if(!flag){
                            network.getHeights().put(node, 1+Math.min(network.getHeights().get(node), Collections.min(heights)));
                            return true;
                        }
                    }else if(network.getReverseNetworkNodes().contains(node)){
                        ArrayList<Integer> heights = new ArrayList<>();
                        for(Node to :network.getReverseNetworkEdges(node).keySet()){
                            heights.add(network.getHeights().get(to));
                            if(network.getHeights().get(node) > network.getHeights().get(to)){
                                flag = true;
                                break;
                            }
                        }
                        if(network.getNetworkNodes().contains(node) && !flag){
                            for (Node to : network.getNetworkEdges(node).keySet()){
                                heights.add(network.getHeights().get(to));
                                if(network.getHeights().get(node) > network.getHeights().get(to)){
                                    flag = true;
                                    break;
                                }
                            }
                        }
                        if(!flag){
                            network.getHeights().put(node, 1+Math.min(network.getHeights().get(node), Collections.min(heights)));
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean nextStep(){
        networkStates.add(network.copy());
        if (push()) {
            return true;
        }
        if (relabel()) {
            return true;
        }
        isAlgprithmEnd = true;
        return false;
    }

    public boolean previousSrep(){
        if(networkStates.size()!=0){
            if(isAlgprithmEnd){
                isAlgprithmEnd = false;
            }
            network = null;
            network = networkStates.get(networkStates.size()-1);
            networkStates.remove(networkStates.size()-1);
            return true;
        }
        return false;
    }
}
