package com.ericwryan

import org.opencv.core.{MatOfDMatch, Mat}
import org.opencv.features2d.DescriptorMatcher

class BruteForceMatcher(objectImage: Image, sceneImage: Image) {
  final val NUMBER_OF_NEAREST_NEIGHBORS = 2
  final val NNDR_RATIO = 0.6
  final val IS_BINARY_DESCRIPTORS = false

  val matches = findMatchesBruteForce(objectImage.descriptors, sceneImage.descriptors)

  def findMatchesBruteForce(objDescriptors: Mat, sceneDescriptors: Mat): java.util.ArrayList[MatOfDMatch] = {
    val matches = new java.util.ArrayList[MatOfDMatch]()
    val matcher = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE)
    matcher.knnMatch(objDescriptors, sceneDescriptors, matches, NUMBER_OF_NEAREST_NEIGHBORS)
    //    printf(matches.toString)
    matches
  }

  def process: MatchingResult = {
    val matchingResult = new MatchingResult(objectImage, sceneImage)

    for (i <- 1 to matches.size - 1) {
      matchingResult.incrementMatchesEvaluated
      if (foundIndividualMatch(i)) {
        val objectIndex = matches.get(i).toArray()(0).queryIdx
        val objectPoint = objectImage.keypoints.toArray()(objectIndex).pt
        val sceneIndex = matches.get(i).toArray()(0).trainIdx
        val scenePoint = sceneImage.keypoints.toArray()(sceneIndex).pt

        matchingResult.addPointsOfInterest(objectPoint, objectIndex, scenePoint, sceneIndex)
      }
    }

    matchingResult
  }

  def foundIndividualMatch(i: Int): Boolean = {
    IS_BINARY_DESCRIPTORS || //Binary, just take the nearest
      matches.get(i).toList.get(0).distance <= NNDR_RATIO * matches.get(i).toList.get(1).distance
  }

}
