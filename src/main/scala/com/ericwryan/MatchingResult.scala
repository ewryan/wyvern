package com.ericwryan

import scala.collection.mutable.ArrayBuffer
import org.opencv.core.Point

class MatchingResult(rule: Image, scene: Image) {

  final val MINIMUM_MATCHES = 8

  var potentialMatchesEvaluated: Int = 0
  var matchesFound: Int = 0

  val objectPoints = new ArrayBuffer[Point]
  val scenePoints = new ArrayBuffer[Point]
  val objectIndices = new ArrayBuffer[Int]
  val sceneIndices = new ArrayBuffer[Int]

  def addPointsOfInterest(objectPoint: Point, objectIndex: Int, scenePoint: Point, sceneIndex: Int) {
    matchesFound += 1
    objectPoints.append(objectPoint)
    objectIndices.append(objectIndex)
    scenePoints.append(scenePoint)
    sceneIndices.append(sceneIndex)
  }

  def incrementMatchesEvaluated {
    potentialMatchesEvaluated += 1
  }

  def isMatch: Boolean = {
    println("Potential Matches: " + potentialMatchesEvaluated)
    println("Found Matches: " + matchesFound)

    matchesFound >= MINIMUM_MATCHES
  }

}
