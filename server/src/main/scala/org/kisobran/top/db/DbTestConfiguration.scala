package org.kisobran.top.db

import java.nio.file.Files

import slick.jdbc.DriverDataSource

object DbTestConfiguration {

  def testMySQL = {
    val tempDir = Files.createTempDirectory(null)

    val ds = new DriverDataSource(
      url = s"jdbc:h2:${tempDir.toFile.getAbsolutePath}/test.db;DB_CLOSE_DELAY=-1;MODE=MySQL;",
      driverClassName = "org.h2.Driver")

    ds
  }

}
