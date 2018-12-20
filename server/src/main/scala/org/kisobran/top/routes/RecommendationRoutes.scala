package org.kisobran.top.routes

import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.{Directives, Route}
import org.kisobran.top.html.Layout
import org.kisobran.top.model.Highlight

class RecommendationRoutes extends Directives {
  val route: Route = pathPrefix("preporuke") {
    complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, highlightPage))
  }

  def highlightPage: String = {
    val recoElem =
      <div id="reco">
        <div class="row">
          <div class="col-1">
          </div>
          <div class="col-sm" style="text-align: center;">
            <h2>Neke pesme zaslužuju više od mesta u listi</h2>
          </div>
          <div class="col-1">
          </div>
        </div>
        {Highlight.items.map { highlight =>
        <div id="highlight">
          <div class="row" style="padding-top: 90px;">
            <div class="col-sm"></div>
            <div class="col-sm-6">
              <div class="embed-responsive embed-responsive-16by9">
                <iframe class="embed-responsive-item"
                        src={highlight.yt}
                        frameborder="0"
                        allow="accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture"
                        allowfullscreen="true">
                </iframe>
              </div>
            </div>
            <div class="col-sm"></div>
          </div>

          <div class="row">
            <div class="col-sm">
            </div>
            <div class="col-sm">
              <blockquote class="blockquote text-center">
                <p class="mb-0">
                  {highlight.desc}
                </p>
                <footer class="blockquote-footer">
                  <cite title="Source Title">
                    {highlight.author}
                  </cite>
                </footer>
              </blockquote>
              <div id="source-button" class="btn btn-primary btn-xs" style="display: none;">&lt; &gt;</div>
            </div>
            <div class="col-sm">
            </div>
          </div>

        </div>
      }}
      </div>

    Layout.createHtml("Preporuke", recoElem)
  }
}
