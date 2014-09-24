package util

import java.io.File

import org.apache.commons.io.FileUtils

object TempIoHelper {
  private def createTempDirectory() = {
    val tempDir = File.createTempFile("commith4ck", "tmp")
    tempDir.delete()
    tempDir.mkdir()
    tempDir
  }

  def withTempDirectory[T](whatToDo: (File) => T): T = {
    val tempDir = createTempDirectory()
    try {
      whatToDo(tempDir)
    } finally {
      FileUtils.deleteDirectory(tempDir)
    }
  }
}
