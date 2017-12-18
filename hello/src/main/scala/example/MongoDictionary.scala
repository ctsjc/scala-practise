package example

//import com.mongodb.casbah._
//import com.mongodb.casbah.Implicits._
import casbah.Mongotest.{collection, newObj}
import com.mongodb.casbah.Imports._
import com.mongodb.casbah.commons.MongoDBList
//import com.mongodb.casbah.commons.MongoDBObject

object Mongotest extends App {
  val ur: String = "mongodb://localhost:2707"
  val uri = MongoClientURI(ur)
  // get DB server connection
  val mongoClient = MongoClient("localhost", 27017)

  val db = mongoClient("db")
  val collection = db("foo")

  println("all docs:")
  collection.find() foreach (println _)
  var word = "would like to see"
  println(s"word ::  $word")
  val qq = MongoDBObject("word" -> word)
  var v = collection.findOne(qq).getOrElse()

  println(v)
}