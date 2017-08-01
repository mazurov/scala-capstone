package observatory

import scala.io.{Source}
import java.time.LocalDate

/**
  * 1st milestone: data extraction
  */
object Extraction {
  type StationsMap =  Map[(String, String), Location]

  private def fahrenheitToCelsius(d: Double) = (d - 32.0) * 5.0 / 9.0
  private def fillStations(stationsFile:String): StationsMap  = {


    val stationsStream = getClass.getResourceAsStream(stationsFile)

    Source.fromInputStream(stationsStream).getLines().map(_.split(",", -1) match {
      case Array(s1, s2, s3, s4) =>
        if ((!s1.isEmpty || !s2.isEmpty) && !s3.isEmpty && !s4.isEmpty) {
          val loc: Location = Location( s3.toDouble, s4.toDouble )
          Some(((s1,s2), loc))
        } else None
    }).toList.flatten.toMap
  }


  /**
    * @param year             Year number
    * @param stationsFile     Path of the stations resource file to use (e.g. "/stations.csv")
    * @param temperaturesFile Path of the temperatures resource file to use (e.g. "/1975.csv")
    * @return A sequence containing triplets (date, location, temperature)
    */
  def locateTemperatures(year: Int, stationsFile: String, temperaturesFile: String): Iterable[(LocalDate, Location, Double)] = {
    val temperaturesStream = getClass.getResourceAsStream(temperaturesFile)
    val keysMap = fillStations(stationsFile) // TODO(alexander.mazurov): fill stations only once

    Source.fromInputStream(temperaturesStream).getLines().map(_.split(",", -1) match {
      case Array( s1, s2, s3, s4, s5 ) => {
        if (!s5.isEmpty) {
          val date = LocalDate.of( year, s3.toInt, s4.toInt )
          val cTemp = fahrenheitToCelsius(s5.toDouble)
//          val cTemp = s5.toDouble
          val key = (s1,s2)
          if (keysMap.contains(key)) {
            Some( (date, keysMap( key ), cTemp) )
          } else None
        } else None
      }
      case _ => None
    }
    ).toList.flatten
  }

  /**
    * @param records A sequence containing triplets (date, location, temperature)
    * @return A sequence containing, for each location, the average temperature over the year.
    */
  def locationYearlyAverageRecords(records: Iterable[(LocalDate, Location, Double)]): Iterable[(Location, Double)] = {
    records.groupBy(x => x._2).mapValues(lst => lst.foldLeft(0.0)(_ + _._3) / lst.size)
  }

}
