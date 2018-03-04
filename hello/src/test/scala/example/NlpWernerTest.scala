package fallenArch

import org.scalatest.{BeforeAndAfter, FlatSpec, Matchers}

class NlpWernerTest extends FlatSpec with Matchers with BeforeAndAfter {
  var nlpWerner:NlpWerner=_

  before {
    nlpWerner = new NlpWerner
  }

  //ignore
  "ignore"  should "sentence 1" in {
    val text = "South Korean authorities have seized a second vessel suspected of transferring oil products to North Korea in violation of international sanctions."
    var entryX=nlpWerner.getEntry(text,"seized")
    //println(entryX.questions.quest)
    println()

    for((x,y)<- entryX.questions.quest) println(x+"\t->"+y)
     entryX.questions.quest("under which law it is seized") shouldEqual  "international sanctions ."
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
}
