package fallenArch

import org.scalatest.{BeforeAndAfter, FlatSpec, Matchers}


class NlpWernerTest extends FlatSpec with Matchers with BeforeAndAfter {
  var nlpWerner:NlpWerner=_

  before {
    nlpWerner = new NlpWerner
  }

  //ignore
  "its "  should "sentence 1" in {
    val text = "South Korean authorities have seized a second vessel suspected of transferring oil products to North Korea in violation of international sanctions."
    var entryX=nlpWerner.getEntry(text,"seized")
    //println(entryX.questions.quest)
    println()

    for((x,y)<- entryX.questions.quest) println(x+"\t->"+y)
     entryX.questions.quest("under which law it is seized") shouldEqual  "international sanctions ."
   }

  "ignore"  should "sentence 1a" in {
    val text = "transferring oil products to North Korea"
    var entryX=nlpWerner.getEntry(text,"transferring")
    //println(entryX.questions.quest)
    println()

    for((x,y)<- entryX.questions.quest) println(x+"\t->"+y)
   // entryX.questions.quest("under which law it is seized") shouldEqual  "international sanctions ."
  }
  "ignore1"  should "sentence 2" in {
    val text = "The Panama-flagged tanker, KOTI, was seized at Pyeongtaek-Dangjin port, on the west coast."
    var entryX=nlpWerner.getEntry(text,"seized")
    println()
    for((x,y)<- entryX.questions.quest) println(x+"\t->"+y)
    entryX.questions.quest("where is seized") shouldEqual  "Pyeongtaek-Dangjin port , on the west coast ."
  }


  "The Hello  object" should "sentence 3" in {
    val text = "The Panama-flagged tanker which is named Koti is being held at a port near the western city of Pyeongtaek."
    var entryX=nlpWerner.getEntry(text,"held")
    println()

    for((x,y)<- entryX.questions.quest) println(x+"\t->"+y)
    entryX.questions.quest("what is being held") shouldEqual  "The Panama-flagged tanker which is named Koti"
    entryX.questions.quest("where it is being held") shouldEqual  "a port near the western city of Pyeongtaek ."
  }

  //South Korea last month reportedly seized a Panama-flagged oil tanker suspected of transferring oil products to North Korea at sea in breach of U.N. sanctions.
  "The Hello  object" should "sentence 4" in {
    val text = "South Korea last month reportedly seized a Panama-flagged oil tanker suspected of transferring oil products to North Korea at sea in breach of U.N. sanctions."
    var entryX=nlpWerner.getEntry(text,"seized")
    println()

    for((x,y)<- entryX.questions.quest) println(x+"\t->"+y)
    entryX.questions.quest("under which law it is seized") shouldEqual  "U.N. sanctions ."
    entryX.questions.quest("why it is seized") shouldEqual  "transferring oil products to North Korea"
    entryX.questions.quest("who seized") shouldEqual  "South Korea last month reportedly"
    entryX.questions.quest("where is seized") shouldEqual  "sea"
    entryX.questions.quest("what is seized") shouldEqual  "a Panama-flagged oil tanker"

   }

  "The Hello  object" should "sentence 5" in {
    val text = "South Korean authorities impounded a second ship suspected of transferring oil to North Korea in violation of U.N. sanctions"
    var entryX=nlpWerner.getEntry(text,"impounded")
    println()

    for((x,y)<- entryX.questions.quest) println(x+"\t->"+y)

    entryX.questions.quest("what") shouldEqual  "a second ship"
      entryX.questions.quest("why") shouldEqual  "transferring oil to North Korea"
      entryX.questions.quest("under which law") shouldEqual  "of U.N. sanctions"
  }


}
