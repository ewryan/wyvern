package com.ericwryan

import scala.collection.mutable.ArrayBuffer
import org.opencv.core.Point

class MatchingResult(rule: Image, scene: Image) {

  val objectPoints = new ArrayBuffer[Point]
  val scenePoints = new ArrayBuffer[Point]
  val objectIndices = new ArrayBuffer[Int]
  val sceneIndices = new ArrayBuffer[Int]

  def addPointsOfInterest(objectPoint: Point, objectIndex: Int, scenePoint: Point, sceneIndex: Int) {
    objectPoints.append(objectPoint)
    objectIndices.append(objectIndex)
    scenePoints.append(scenePoint)
    sceneIndices.append(sceneIndex)
  }

}
