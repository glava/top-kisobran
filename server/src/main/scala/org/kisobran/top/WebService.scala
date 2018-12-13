package org.kisobran.top

import akka.http.scaladsl.server.Directives
import org.kisobran.top.shared.SharedMessages
import org.kisobran.top.twirl.Implicits._

class WebService() extends Directives {

  val route = {
    pathSingleSlash {
      get {
        complete {
          org.kisobran.top.html.index.render(SharedMessages.itWorks)
        }
      }
    } ~
      pathPrefix("assets" / Remaining) { file =>
        // optionally compresses the response with Gzip or Deflate
        // if the client accepts compressed responses
        encodeResponse {
          getFromResource("public/" + file)
        }
      }
  }
}
