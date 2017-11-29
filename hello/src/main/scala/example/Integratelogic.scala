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
  var word="would like to see"

  var sentenceMap:Map[String,String]=Map("I"->"noun1",
    "would like to see"->"verb",
    "better read and write speed"->"noun2",
    "as"->"conjuction",
    "this card has nowhere near the speed it advertises"->"sentence2"
  )
  // search the word information in dictionary
  var dictionary_entry:String=searchWordInDb(word)
  //println(dictionary_entry)
  // convert to json
  var json_dictionary=parse(dictionary_entry.mkString)
  //println(json_dictionary)
  // check the type
  var entrytype=json_dictionary \\ "type"

  //.foldField(List(): List[String])((l, t) => t._1 :: l)
  entrytype.values match {
    case "phrase"=>{
      val structur=json_dictionary \\ "structure"
      val v =structur.foldField(Map():Map[String, Any])((map,v)=>{
        map+(v._1 -> v._2.values)
      })
      var m :List[String]=v("sequence").toString.split("-").toList
      println(">>"+m)
      // find things before would like to

      // find things after would like to till as
      // after as


    }
    case "verb"=>verb(entrytype)
  }



  def searchWordInDb(word: String):String = {
    val mongoClient = MongoClient("localhost",27017)
    val db = mongoClient("db")
    val collection = db("foo")
    val query = MongoDBObject("word" -> word)
    collection.findOne(query).getOrElse().toString
  }
}

case class phrase(entrytype:JValue)
case class verb(entrytype:JValue)