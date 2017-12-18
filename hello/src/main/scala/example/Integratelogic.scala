package example

import com.mongodb.casbah.Imports.{MongoClient, MongoClientURI, MongoDBObject}
import example.Mongotest.collection
import org.json4s._
import org.json4s.jackson.JsonMethods._

object Integratelogic extends App {
  println("Hello World\nStep 1: read the sentence, " +
    "and then check into db, does that word present in the dictionary, the largest word" +
    "will get the count" +
    "and then put them into map or list..")
  var word = "would like to see"

  // search the word information in dictionary
  var dictionary_entry: String = searchWordInDb(word)
  // convert to json
  var json_dictionary = parse(dictionary_entry.mkString)
  // check the type
  var entrytype = json_dictionary \\ "type"

  //.foldField(List(): List[String])((l, t) => t._1 :: l)
  entrytype.values match {
    case "phrase" => {
      val structure = json_dictionary \\ "structure"
      //  convert the structure to Map def foldField[A](z: A)(f: (A, JField) ⇒ A): A =
      val v = structure.foldField(Map(): Map[String, Any])((map, v) => {
        map + (v._1 -> v._2.values)
      })
      println(" v --- " + v)
      // get the "sequence" value from map and split the value by -
      var m: List[String] = v("sequence").toString.split("-").toList
      println(">>" + m)
    } //end of phrase
  } //end of match

  def searchWordInDb(word: String): String = {
    val mongoClient = MongoClient("localhost", 27017)
    val db = mongoClient("db")
    val collection = db("foo")
    val query = MongoDBObject("word" -> word)
    collection.findOne(query).getOrElse().toString
  }
}

case class phrase(entrytype: JValue)

case class verb(entrytype: JValue)