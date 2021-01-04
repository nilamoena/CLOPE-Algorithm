package weka.clusterers;

import java.io.Serializable;
import weka.core.*;
import java.util.*;

public class CLOPE extends AbstractClusterer implements OptionHandler {

  public String globalInfo() {
      return "CLOPE: A Fast and Effective Clustering Algorithm for Transactional Data";
  }

  private class CLOPECluster implements Serializable {
    public int S = 0; //size
    public int W = 0; //width
    public int N = 0; //number of transactions
    public HashMap occ = new HashMap(); //item, occurrence

    //Menambahkan item ke cluster
    public void AddItem(String Item) {
      int count;
      if (!this.occ.containsKey(Item)) {
	this.occ.put(Item, 1);
      } else {
	count = (Integer) this.occ.get(Item);
	count++;
	this.occ.remove(Item);
	this.occ.put(Item, count);
      }
      this.S++;
    }
    public void AddItem(Integer Item) {
      int count;
      if (!this.occ.containsKey(Item)) {
	this.occ.put(Item, 1);
      } else {
	count = (Integer) this.occ.get(Item);
	count++;
	this.occ.remove(Item);
	this.occ.put(Item, count);
      }
      this.S++;
    }

    //Menghapus item dari cluster
    public void DeleteItem(String Item) {
      int count;
      count = (Integer) this.occ.get(Item);
      if (count == 1) {
	this.occ.remove(Item);
      } else {
	count--;
	this.occ.remove(Item);
	this.occ.put(Item, count);
      }
      this.S--;
    }
     public void DeleteItem(Integer Item) {
       int count;
       count = (Integer) this.occ.get(Item);
       if (count == 1) {
	 this.occ.remove(Item);
       }
       this.S--;
     }

     //Delta Value
     public double DeltaAdd(Instance inst, double r) {
         int S_new;
         int W_new;
         double profit;
         double profit_new;
         double deltaprofit;
         S_new = 0;
         W_new = occ.size();
         if (inst instanceof SparseInstance) {
             for (int i = 0; i < inst.numValues(); i++) {
                 S_new++;
                 if ((Integer) this.occ.get(inst.index(i)) == null) {
                     W_new++;
                 }
             }
         } else {
             for (int i = 0; i < inst.numAttributes(); i++) {
                 if (!inst.isMissing(i)) {
                     S_new++;
                     if ((Integer) this.occ.get(i + inst.toString(i)) == null) {
                         W_new++;
                     }
                 }
             }
         }
         S_new += S;
         if (N == 0) {
             deltaprofit = S_new / Math.pow(W_new, r);
         } else {
             profit = S * N / Math.pow(W, r);
             profit_new = S_new * (N + 1) / Math.pow(W_new, r);
             deltaprofit = profit_new - profit;
         }
         return deltaprofit;
     }

    //Menambahkan instance ke cluster
    public void AddInstance(Instance inst) {
	if (inst instanceof SparseInstance) {
	  for (int i = 0; i < inst.numValues(); i++) {
	    AddItem(inst.index(i));
	  }
	} else {
	  for (int i = 0; i < inst.numAttributes(); i++) {
	    if (!inst.isMissing(i)) {
	      AddItem(i + inst.toString(i));
	    }
	  }
	}
	this.W = this.occ.size();
	this.N++;
    }

    //Menghapus instance dari cluster
    public void DeleteInstance(Instance inst) {
	if (inst instanceof SparseInstance) {
	  for (int i = 0; i < inst.numValues(); i++) {
	    DeleteItem(inst.index(i));
	  }
	} else {
	  for (int i = 0; i <= inst.numAttributes() - 1; i++) {
	    if (!inst.isMissing(i)) {
	      DeleteItem(i + inst.toString(i));
	    }
	  }
	}
	this.W = this.occ.size();
	this.N--;
      }
    }

  public ArrayList<CLOPECluster> clusters = new ArrayList<CLOPECluster>();

  //Repulsion
  protected double m_RepulsionDefault = 2.5;
  protected double m_Repulsion = m_RepulsionDefault;

  protected int m_numberOfClusters = -1;
  protected int m_processed_InstanceID;
  protected int m_numberOfInstances;

  protected ArrayList<Integer> m_clusterAssignments = new ArrayList();

  protected boolean m_numberOfClustersDetermined = false;

  public int numberOfClusters() {
    determineNumberOfClusters();
    return m_numberOfClusters;
  }

  protected void determineNumberOfClusters() {
    m_numberOfClusters = clusters.size();
    m_numberOfClustersDetermined = true;
  }

  public Enumeration listOptions() {
    Vector result = new Vector();
    result.addElement(new Option(
	"\tRepulsion\n" + "\t(default " + m_RepulsionDefault + ")", "R", 1, "-R <num>"));
    return result.elements();
  }

  /**
   * Parses a given list of options. <p/>
   <!-- options-start -->
   * Valid options are: <p/>
   * <pre> -R &lt;num&gt;
   *  Repulsion
   *  (default 2.5)</pre>
   <!-- options-end -->
   * @param options the list of options as an array of strings
   * @throws Exception if an option is not supported
   */
  public void setOptions(String[] options) throws Exception {
    String tmpStr;
    tmpStr = Utils.getOption('R', options);
    if (tmpStr.length() != 0) {
      setRepulsion(Double.parseDouble(tmpStr));
    } else {
      setRepulsion(m_RepulsionDefault);
    }
  }

  /**
   * Gets the current settings of CLOPE
   * @return an array of strings suitable for passing to setOptions()
   */
  public String[] getOptions() {
    Vector result;
    result = new Vector();
    result.add("-R");
    result.add("" + getRepulsion());
    return (String[]) result.toArray(new String[result.size()]);
  }

  public void setRepulsion(double value) {
    m_Repulsion = value;
  }

  public double getRepulsion() {
    return m_Repulsion;
  }

  /**
   * Returns default capabilities of the clusterer.
   * @return the capabilities of this clusterer
   */
  public Capabilities getCapabilities() {
    Capabilities result = super.getCapabilities();
    result.disableAll();
    result.enable(Capabilities.Capability.NO_CLASS);
    result.enable(Capabilities.Capability.NOMINAL_ATTRIBUTES);
    result.enable(Capabilities.Capability.MISSING_VALUES);
    return result;
  }

  //CLOPE
  public void buildClusterer(Instances data) throws Exception {
    clusters.clear();
    m_processed_InstanceID = 0;
    m_clusterAssignments.clear();
    m_numberOfInstances = data.numInstances();
    boolean moved;
    //Phase 1 
    for (int i = 0; i < data.numInstances(); i++) {
      int clusterid = AddInstanceToBestCluster(data.instance(i));
      m_clusterAssignments.add(clusterid);

    }
    //Phase 2
    do {
      moved = false;
      for (int i = 0; i < data.numInstances(); i++) {
	m_processed_InstanceID = i;
	int clusterid = MoveInstanceToBestCluster(data.instance(i));
	if (clusterid != m_clusterAssignments.get(i)) {
	  moved = true;
	  m_clusterAssignments.set(i, clusterid);
	}
      }
    } while (!moved);
    m_processed_InstanceID = 0;
  }

  //Menambahkan instance ke cluster terbaik
  public int AddInstanceToBestCluster(Instance inst) {
    double delta;
    double deltamax;
    int clustermax = -1;
    if (clusters.size() > 0) {
      int tempS = 0;
      int tempW = 0;
      if (inst instanceof SparseInstance) {
	for (int i = 0; i < inst.numValues(); i++) {
	  tempS++;
	  tempW++;
	}
      } else {
	for (int i = 0; i < inst.numAttributes(); i++) {
	  if (!inst.isMissing(i)) {
	    tempS++;
	    tempW++;
	  }
	}
      }
      deltamax = tempS / Math.pow(tempW, m_Repulsion);
      for (int i = 0; i < clusters.size(); i++) {
        CLOPECluster tempcluster = clusters.get(i);
        delta = tempcluster.DeltaAdd(inst, m_Repulsion);
        if (delta > deltamax) {
          deltamax = delta;
          clustermax = i;
        }
      }
    } else {
      CLOPECluster newcluster = new CLOPECluster();
      clusters.add(newcluster);
      newcluster.AddInstance(inst);
      return clusters.size() - 1;
    }
    if (clustermax == -1) {
      CLOPECluster newcluster = new CLOPECluster();
      clusters.add(newcluster);
      newcluster.AddInstance(inst);
      return clusters.size() - 1;
    }
    clusters.get(clustermax).AddInstance(inst);
    return clustermax;
  }

  //Memindahkan instance ke cluster terbaik
  public int MoveInstanceToBestCluster(Instance inst) {
    clusters.get(m_clusterAssignments.get(m_processed_InstanceID)).DeleteInstance(inst);
    m_clusterAssignments.set(m_processed_InstanceID, -1);
    double delta;
    double deltamax;
    int clustermax = -1;
    int tempS = 0;
    int tempW = 0;
    if (inst instanceof SparseInstance) {
      for (int i = 0; i < inst.numValues(); i++) {
	tempS++;
	tempW++;
      }
    } else {
      for (int i = 0; i < inst.numAttributes(); i++) {
	if (!inst.isMissing(i)) {
	  tempS++;
	  tempW++;
	}
      }
    }
    deltamax = tempS / Math.pow(tempW, m_Repulsion);
    for (int i = 0; i < clusters.size(); i++) {
      CLOPECluster tempcluster = clusters.get(i);
      delta = tempcluster.DeltaAdd(inst, m_Repulsion);
      if (delta > deltamax) {
	deltamax = delta;
	clustermax = i;
      }
    }
    if (clustermax == -1) {
      CLOPECluster newcluster = new CLOPECluster();
      clusters.add(newcluster);
      newcluster.AddInstance(inst);
      return clusters.size() - 1;
    }
    clusters.get(clustermax).AddInstance(inst);
    return clustermax;
  }

  public int clusterInstance(Instance instance) throws Exception {
    if (m_processed_InstanceID >= m_numberOfInstances) {
      m_processed_InstanceID = 0;
    }
    int i = m_clusterAssignments.get(m_processed_InstanceID);
    m_processed_InstanceID++;
    return i;
  }

  public String toString() {
    StringBuffer stringBuffer = new StringBuffer();
    stringBuffer.append("CLOPE clustering \n" + "================");
    return stringBuffer.toString();
  }

  //Main method for testing this class
  public static void main(String[] argv) {
    runClusterer(new CLOPE(), argv);
  }
}