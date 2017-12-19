package example

import fountainhead.dict.DEntry
import org.json4s.{DefaultFormats, JValue}
import org.json4s.jackson.JsonMethods.parse

object JsonExamples extends App{
  implicit val jsonDefaultFormats = DefaultFormats
  val source = scala.io.Source.fromFile("res/dverb.json")
  val lines = source.getLines()
  var json_dictionary:List[DEntry]=List()
  for (line <- lines) {
    json_dictionary = parse(line).extract[List[DEntry]]
  }
  source.close()
  json_dictionary.foreach((de)=>println(s"${de.word} ${de.template.structures(0).variation}"))
}
