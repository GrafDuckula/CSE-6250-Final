/**
 * @author Hang Su <hangsu@gatech.edu>.
 */

package edu.gatech.cse8803.clustering

import org.apache.spark.SparkContext._
import org.apache.spark.rdd.RDD

object Metrics {
  /**
   * Given input RDD with tuples of assigned cluster id by clustering,
   * and corresponding real class. Calculate the purity of clustering.
   * Purity is defined as
   *             \fract{1}{N}\sum_K max_j |w_k \cap c_j|
   * where N is the number of samples, K is number of clusters and j
   * is index of class. w_k denotes the set of samples in k-th cluster
   * and c_j denotes set of samples of class j.
   * @param clusterAssignmentAndLabel RDD in the tuple format
   *                                  (assigned_cluster_id, class)
   * @return purity
   */
  def purity(clusterAssignmentAndLabel: RDD[(Int, Int)]): Double = {

    val numSample = clusterAssignmentAndLabel.count().toDouble
    val clusterAssignmentAndLabelGBK = clusterAssignmentAndLabel.groupByKey()
    val purity = clusterAssignmentAndLabelGBK.map(s=>(s._1,s._2.groupBy(identity).mapValues(_.size).values.max)).map(s=>s._2).reduce(_+_)/numSample

    val temp = clusterAssignmentAndLabelGBK.map(s=>(s._1,s._2.groupBy(identity).mapValues(_.size).toList)).collect()
    temp.foreach(println)

    purity

  }
}
