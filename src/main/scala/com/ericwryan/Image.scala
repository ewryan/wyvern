package com.ericwryan

import org.opencv.highgui.Highgui
import org.opencv.core.{MatOfKeyPoint, Mat}
import org.opencv.features2d.{DescriptorExtractor, FeatureDetector}

class Image(imagePath: String) {
  final val FEATURE_DETECTOR = FeatureDetector.SURF
  final val DESCRIPTOR_EXTRACTOR = DescriptorExtractor.SURF

  var imageMatrix = Highgui.imread(getClass.getResource(imagePath).getPath)
  val keypoints = extractKeypoints(imageMatrix)
  val descriptors = extractFeatures(imageMatrix, keypoints)

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
