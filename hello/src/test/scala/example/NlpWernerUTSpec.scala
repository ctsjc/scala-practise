package example

import fallenArch.NlpWerner
import fallenArch.beans._
import org.scalatest.{BeforeAndAfter, FlatSpec, FunSuite, Matchers}

class NlpWernerUTSpec extends FunSuite with Matchers with BeforeAndAfter  {
  var nlpWerner:NlpWerner=new NlpWerner

  test("splitSeqByΨ"){
    println("This is test")
    val map:Map[Int,Int]=nlpWerner.splitSeqByΨ("Ψ1 have seized Ψ3 suspected of Ψ6 in violation of Ψ2")
    assert(map(1)==1)
    assert(map(3)==2)
    assert(map(6)==3)
    assert(map(2)==4)
  }

  test("splitSeqByΨ 2"){
    val map:Map[Int,Int]=nlpWerner.splitSeqByΨ("have Ψ1 seized Ψ3 of Ψ6")
    map.foreach(e=>println(e._1+"->"+e._2))

    assert(map(1)==1)
    assert(map(3)==2)
    assert(map(6)==3)
  }

  test("test bridge"){
    val dl: DictLoader = new DictLoader
    val dict_single_question_entry=Entry(
      "some word",
      Raw("",""),
      "",
      SequenceX(List(
        ("some phrase-some another phrase-","Ψ1 some phrase Ψ2 some another phrase Ψ3"))),
      QuestionsX(Map(
        "this is question 1"->"Ψ1",
        "this is question 2"->"Ψ2",
        "this is question 3"->"Ψ3")))
    val map_from_input_text:Map[Int,String]=Map(
      1->"this is answer 1",
      2->"this is answer 2",
      3->"this is answer 3")

    val mapOfQuestionAnswer:Map[String,String]=nlpWerner.bridge(dict_single_question_entry,map_from_input_text)
    println("================")
    mapOfQuestionAnswer.foreach(e => println(e._1 + "-" + e._2))
    assert(mapOfQuestionAnswer("this is question 1")=="this is answer 1")
    assert(mapOfQuestionAnswer("this is question 2")=="this is answer 2")
    assert(mapOfQuestionAnswer("this is question 3")=="this is answer 3")
  }

  test("createQuestionEntry"){
    val dict_single_question_entry=Entry(
      "some word",
      Raw("",""),
      "",
      SequenceX(List(
        ("some phrase-some another phrase-","Ψ1 some phrase Ψ2 some another phrase Ψ3"))),
      QuestionsX(Map(
        "this is question 1"->"Ψ1",
        "this is question 2"->"Ψ2",
        "this is question 3"->"Ψ3")))

    val inputText:String="this should be psi1 some phrase this should be psi2 some another phrase this should be psi3"

    val e =nlpWerner.createQuestionEntry(dict_single_question_entry,nlpWerner.toSentence(inputText))
    val map:Map[String,String]=Map(
      "this is question 1" -> "this should be psi1",
      "this is question 2" -> "this should be psi2",
      "this is question 3" -> "this should be psi3")

    assert(e.questions.quest == map)
  }

  test("percentageMatch"){
    val inputText="Freedom is the right of all sentient beings"
    var xx=nlpWerner.percentageMatch(
      inputText,
      "sentient beings")
    println(xx)

    xx=nlpWerner.percentageMatch(
      inputText,
      "is-of all")
    println(xx)

    xx=nlpWerner.percentageMatch(
      inputText,
      "is the-of all")
    println(xx)
  }
}
