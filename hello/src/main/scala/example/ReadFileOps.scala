package example
import org.json4s._
import org.json4s.jackson.JsonMethods._


object ReadFileOps extends App{
  readFile();

  implicit val formats = org.json4s.DefaultFormats

  def readFile(): Unit ={

    println("Love me Do")
    val source = scala.io.Source.fromFile("milkyway.json")
    val lines =  source.getLines()

    for( line <- lines){
     // println(line.mkString)
      println(jsonStrToMap(line.mkString))
     // println(mp)
      //mps += mp
    }
    source.close()
  }

  def jsonStrToMap(jsonStr: String): Map[String, Any] = {
    val json = "{"+jsonStr.substring(43);

    println(json)
    parse(json).extract[Map[String, String]]
  }
}
