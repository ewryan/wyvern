package com.ericwryan

import org.opencv.core.{Mat, MatOfByte}
import org.opencv.highgui.Highgui
import java.io.ByteArrayInputStream
import javax.imageio.ImageIO
import java.awt.image.BufferedImage
import javax.swing.{JOptionPane, JLabel, ImageIcon}

object OpenCVUtils {

  def drawImage(imageTmp: Mat) {
    val matOfByte = new MatOfByte()
    Highgui.imencode(".jpg", imageTmp, matOfByte)
    val byteArray = matOfByte.toArray
    var img: BufferedImage = null

    try {
      val in = new ByteArrayInputStream(byteArray)
      img = ImageIO.read(in)
      val icon = new ImageIcon(img)
      val label = new JLabel(icon)
      JOptionPane.showMessageDialog(null, label)
    }
    catch {
      case e: Exception => e.printStackTrace()
    }
  }
}
