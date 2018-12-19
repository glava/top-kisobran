package org.kisobran.top.util

import org.slf4j.{Logger, LoggerFactory}

trait LoggingSupport {
  lazy val log: Logger = LoggerFactory.getLogger(getClass)
}
