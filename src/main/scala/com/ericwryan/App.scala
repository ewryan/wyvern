package com.ericwryan

import org.opencv.core._

object App {

  def main(args: Array[String]) {
    System.loadLibrary(Core.NATIVE_LIBRARY_NAME)

    time {
      val rule = new Image("/images/starbucks_rule1.jpg")
      val scene = new Image("/images/starbucks_target1.jpg")

      val matcher: BruteForceMatcher = new BruteForceMatcher(rule, scene)
      val result: MatchingResult = matcher.process

      println(result.objectIndices.toString())
      println(result.sceneIndices.toString())
      println(result.objectPoints.toString())
      println(result.scenePoints.toString())
    }
  }

  def time[A](f: => A) = {
    val s = System.nanoTime
    val ret = f
    println("time: " + (System.nanoTime - s) / 1e6 + "ms")
    ret
  }
}
