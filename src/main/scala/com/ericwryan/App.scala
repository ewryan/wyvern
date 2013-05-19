package com.ericwryan

import org.opencv.core._
import org.opencv.highgui.Highgui
import org.opencv.features2d._
import scala.collection.mutable.ArrayBuffer

object App {

  final val FEATURE_DETECTOR = FeatureDetector.SURF
  final val DESCRIPTOR_EXTRACTOR = DescriptorExtractor.SURF
  final val NUMBER_OF_NEAREST_NEIGHBORS = 2
  final val NNDR_RATIO = 0.6

  def main(args: Array[String]) {
    System.loadLibrary(Core.NATIVE_LIBRARY_NAME)

    time {
      val objectFilename = "/images/starbucks_rule1.jpg"
      val sceneFilename = "/images/starbucks_target1.jpg"
      val debug = 0

      val obj = Highgui.imread(getClass.getResource(objectFilename).getPath)
      val scene = Highgui.imread(getClass.getResource(sceneFilename).getPath)

      val objectKeypoints = extractKeypoints(obj)
      val sceneKeypoints = extractKeypoints(scene)

      val objectDescriptors = extractFeatures(obj, objectKeypoints)
      val sceneDescriptors = extractFeatures(scene, sceneKeypoints)

      val matches = findMatchesBruteForce(objectDescriptors, sceneDescriptors)

      processNearestNeighbor(matches, objectKeypoints, sceneKeypoints)

      //    OpenCVUtils.drawImage(obj)
      //    OpenCVUtils.drawImage(scene)
    }
  }

  def time[A](f: => A) = {
    val s = System.nanoTime
    val ret = f
    println("time: " + (System.nanoTime - s) / 1e6 + "ms")
    ret
  }

  def processNearestNeighbor(matches: java.util.ArrayList[MatOfDMatch], objectKeypoints: MatOfKeyPoint, sceneKeypoints: MatOfKeyPoint) {
    val isBinaryDescriptors = false
    val mpts1 = new ArrayBuffer[Point]
    val mpts2 = new ArrayBuffer[Point]
    val indexes1 = new ArrayBuffer[Int]
    val indexes2 = new ArrayBuffer[Int]

    for (i <- 1 to matches.size - 1) {
      if (isBinaryDescriptors || //Binary, just take the nearest
        matches.get(i).toList.get(0).distance <= NNDR_RATIO * matches.get(i).toList.get(1).distance) {

        val objectIndex = matches.get(i).toArray()(0).queryIdx
        mpts1.append(objectKeypoints.toArray()(objectIndex).pt)
        indexes1.append(objectIndex)

        val sceneIndex = matches.get(i).toArray()(0).queryIdx
        mpts2.append(sceneKeypoints.toArray()(sceneIndex).pt)
        indexes2.append(sceneIndex)
      }
    }

    //    printf(mpts1.toArray.toString)
    //    printf(mpts2.toArray.toString)
    //    printf(indexes1.toArray.toString)
    //    printf(indexes2.toArray.toString)

    (mpts1.toArray, mpts2.toArray, indexes1.toArray, indexes2.toArray)
  }

  def findMatchesBruteForce(objDescriptors: Mat, sceneDescriptors: Mat): java.util.ArrayList[MatOfDMatch] = {
    val matches = new java.util.ArrayList[MatOfDMatch]()
    val matcher = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE)
    matcher.knnMatch(objDescriptors, sceneDescriptors, matches, NUMBER_OF_NEAREST_NEIGHBORS)
    //    printf(matches.toString)
    matches
  }

  def extractKeypoints(image: Mat): MatOfKeyPoint = {
    val keypoints = new MatOfKeyPoint()
    FeatureDetector.create(FEATURE_DETECTOR).detect(image, keypoints)
    //    printf(keypoints.dump)
    keypoints
  }

  def extractFeatures(image: Mat, keypoints: MatOfKeyPoint): Mat = {
    val descriptors = new Mat()
    val extractor = DescriptorExtractor.create(DESCRIPTOR_EXTRACTOR)
    extractor.compute(image, keypoints, descriptors)
    //    printf(descriptors.dump)
    descriptors
  }


}
