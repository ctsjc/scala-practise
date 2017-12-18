package example

import org.json4s._
import org.json4s.jackson.JsonMethods._


object ReadFileOps extends App {
  readFile();

  implicit val formats = org.json4s.DefaultFormats

  def readFile(): Unit = {

    println("Love me Do")
    val source = scala.io.Source.fromFile("milkyway.json")
    val lines = source.getLines()

    for (line <- lines) {
      println(line.mkString)
      var v: JValue = parse(line.mkString) \\ "word"
      println(v.values)
      // println(mp)
      //mps += mp
    }
    source.close()
  }
}

/*{"_id":{"$oid":"5949e12a4cb200478209a1fb"},
"word":"would like to see",
"type":"phrase",
"nature":"action",
  "structure":{
    "sequence":"noun1-would like to see-noun2-as-sentence2",
    "who like to see":"noun1",
    "what like to see":"noun2",
    "why like to see":"sentence2"}}
*/