# spray-json-jts

## How to use

This library provides a reader/writer for [GeoJSON](http://geojson.org/) to [Spray JSON](https://github.com/spray/spray-json).

If you use SBT you just include de follow dependency.

```scala

libraryDependencies += "br.ufla" %% "spray-jts-json" %% "0.0.1"

```

You only need import the follow code:

```scala

import spray.json.geojson.GeoJSON._

```


And then to provide an implicit `com.vividsolutions.jts.geom.GeometryFactory`, for example:


```scala

import spray.json.geojson.GeoJSON._
import com.vividsolutions.jts.geom._
import spray.json._

object Example extends App {

  implicit val geometryFactory = new GeometryFactory()

  val geoJson = """
{
   "type": "LineString",
   "coordinates": [[1, 1], [5, 5], [10, 10], [-1, -1]]
}
"""

  val lineString = geoJson.parseJson.convertTo[Geometry]

  // prints LINESTRING (1 1, 5 5, 10 10, -1 -1)
  println(lineString.toText())

  // If you want typed geometries, no problem!

  val point = """{"type": "Point", "coordinates": [-45, -21]}""".parseJson.convertTo[Point]

  // prints POINT(-45 -21)
  println(point)
}
```

OK, it's a simple library.
