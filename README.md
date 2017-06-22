# spray-json-jts

# How to use?

This library provides a reader for [GeoJSON](http://geojson.org/) to [Spray JSON](https://github.com/spray/spray-json).

You only need import the follow code:

```scala

import spray.json.geojson.GeoJSON._

```

And then to provide an implicit `com.vividsolutions.jts.geom.GeometryFactory`, for example:


```scala

import spray.json.geojson.GeoJSON._
import com.vividsolutions.jts.geom.{Geometry, GeometryFactory}
import spray.json._

class Example {
   implicit val geometryFactory = new GeometryFactory()

   val geoJson = """
{
   "type": "LineString",
   "coordinates": [[1, 1], [5, 5], [10, 10], [-1, -1]]
}
"""

   val point = geoJson.parseJson.convertTo[Geometry]
}
```

OK, it's a simple library.
