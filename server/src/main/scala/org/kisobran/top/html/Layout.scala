package org.kisobran.top.html

import scala.xml.Elem

object Layout {
  def createHtml(title: String, content: Elem): String =
    s"""<!DOCTYPE html>
       |${mainLayout(title, content).toString}
     """.stripMargin

  def mainLayout(title: String, content: Elem): Elem =
    <html lang="sr">
      <head>
        <title>Kišobran top 10:
          {title}
        </title>
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css" integrity="sha384-MCw98/SFnGE8fJT3GXwEOngsV7Zt27NXFoaoApmYm81iuXoPkFOJwJ8ERdknLPMO" crossorigin="anonymous"/>
        <link rel="stylesheet" href="/assets/bootstrap.min.css" crossorigin="anonymous"/>
        <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo"
                crossorigin="anonymous"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js" integrity="sha384-ZMP7rVo3mIykV+2+9J3UJ46jBk0WLaUAdn689aCwoqbBJiSnjAK/l8WvCWPIPm49"
                crossorigin="anonymous"></script>
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js" integrity="sha384-ChfqqxuZUCnJSK3+MXmPNIyE6ZbWh2IMqE241rYiqJxyMiZ6OW/JmZQ5stwEULTy"
                crossorigin="anonymous"></script>

        <!-- For Google -->
        <meta name="description" content="Izaberi svojih top 10 pesama"/>
        <meta name="keywords" content="kišobran"/>

        <meta name="author" content="kišobran"/>
        <meta name="copyright" content="kišobran"/>
        <meta name="application-name" content="kišobran top deset"/>

        <!-- For Facebook -->
        <meta property="og:title" content="Kišobran top 10: @title"/>
        <meta property="og:type" content="article"/>
        <meta property="og:image" content="https://traggymo.sirv.com/Images/top_cover.png"/>
        <meta property="og:url" content="http://top.kisobran.org/"/>
        <meta property="og:description" content="Biramo top 10 pesama"/>

        <!-- For Twitter -->
        <meta name="twitter:card" content="summary"/>
        <meta name="twitter:title" content="Kišobran top 10: @title"/>
        <meta name="twitter:description" content="Biramo top 10 pesama"/>
        <meta name="twitter:image" content="https://traggymo.sirv.com/Images/top_cover.png"/>

      </head>
      <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no"/>
      <body>

        <nav class="navbar navbar-expand-lg navbar-dark bg-primary">
          <a class="navbar-brand" href="/">
            <strong>Kišobran Top Deset</strong>
          </a>
          <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarColor01" aria-controls="navbarColor01" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
          </button>

          <div class="collapse navbar-collapse" id="navbarColor01">
            <ul class="navbar-nav mr-auto">
              <li class="nav-item">
                <a class="nav-link" href="/">Liste
                  <span class="sr-only">(current)</span>
                </a>
              </li>
              <li class="nav-item">
                <a class="nav-link" href="/glasaj">Glasaj</a>
              </li>
              <li class="nav-item">
                <a class="nav-link" href="/pravila">Pravila</a>
              </li>
              <li class="nav-item dropdown">
                <a class="nav-link dropdown-toggle" data-toggle="dropdown" href="#" id="download" aria-expanded="false">Arhiva
                  <span class="caret"></span>
                </a>
                <div class="dropdown-menu" aria-labelledby="download">
                  <a class="dropdown-item" href="/arhiva?year=2017">2017</a>
                  <a class="dropdown-item" href="/arhiva?year=2016">2016</a>
                  <a class="dropdown-item" href="/arhiva?year=2015">2015</a>
                  <a class="dropdown-item" href="/arhiva?year=2014">2014</a>
                  <a class="dropdown-item" href="/arhiva?year=2013">2013</a>
                </div>
              </li>
              <li class="nav-item">
                <a class="nav-link" href="/preporuke">Preporuke</a>
              </li>
            </ul>
          </div>
        </nav>

        <div class="container" style="margin-top: 60px;">
          {content}
        </div>

      </body>

      <footer>
        <p></p>
      </footer>
    </html>

}
