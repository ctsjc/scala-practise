package fallenArch

import com.typesafe.scalalogging.Logger
import edu.stanford.nlp.ling.CoreAnnotations
import edu.stanford.nlp.simple.Sentence
import fallenArch.beans.{DictLoader, Entry, QuestionsX, SequenceX}

import scala.collection.JavaConverters._

class NlpWerner {

  val logger = Logger[NlpWerner]

  val dl: DictLoader = new DictLoader


  def getEntry(text: String, mainVerb: String): Entry = {
    logger.info("check the loggins is working or not...")
    val sent = toSentence(text)
    var mv = mainVerb
    if (mv.isEmpty) {
      mv = getMainVerb(sent)
    }
    val dict_entry: Entry = getEntry(mv)
    logger.info("entry {}",dict_entry)

    val dict_sequence: Entry = createSingleSequenceEntry(dict_entry, sent.text())
    logger.info("sequence {} ",dict_sequence)

    val entryX = createQuestionEntry(dict_sequence, sent)
    logger.info("q-entry {}",entryX)

    entryX
  }

  /*convert string into Nlp sentence */
  def toSentence(text: String): Sentence = {
    new Sentence(text)
  }

  /*CORE nlp to find the main verb */
  def getMainVerb(sentence: Sentence): String = {
    val x: CoreAnnotations.TextAnnotation = new CoreAnnotations.TextAnnotation()
    val y = sentence.dependencyGraph().getFirstRoot.getString(x.getClass)
    println("main verb found to be :: "+y)
    y
  }

  /* find the Entry class from dictionary */
  def getEntry(mainVerb: String): Entry = {
    dl.dictionary(mainVerb)
  }

  /**
    * input : dictionary Entry, input text
    * output : Entry with single matching sequence
    *
    * dictionary contains lots of sequences, find out the one which is matching most with input text
    */
  def createSingleSequenceEntry(entry: Entry, inputText: String): Entry = {

    def getSequence(entry: Entry, inputText: String): Entry = {
      // find the sequence which has hightest matching percentage
      val max_matching_sequence = entry.sequence.pairs.map(_._1).map(sequenceFromDictionary => {
        percentageMatch(inputText, sequenceFromDictionary)
      }).reduceLeft((x, y) => {
        logger.info(s"$x - $y")
        if (x._1 > y._1)
          x
        else if (x._1 == y._1 && x._2 > y._2)
          x
        else
          y
      })._3
      logger.info(s"max_matching_sequence :: $max_matching_sequence")

      val newEntry = entry.copy(sequence = SequenceX(List(entry.sequence.pairs.find(_._1 == max_matching_sequence).get)))
      newEntry
    }



    val dict_sequence: Entry = getSequence(entry, inputText) //"Ψ1 have seized Ψ2 suspected of Ψ3 in violation of Ψ4"
    dict_sequence
  }
  /* returns tuple of % of similarity, count of words that matched,string that is matched. */
  def percentageMatch(inputSentence: String, sequenceFromDictionary: String): (Int, Int, String) = {
    val i: List[String] = inputSentence.split(" ").toList
    val d: List[String] = sequenceFromDictionary.split("-").filter(!_.isEmpty).map(_.trim).toList
    var lastFound: Int = 0
    var curFound: Int = 0
    var totalFound: Int = 0
    logger.info("\n"+inputSentence+"--------------------"+sequenceFromDictionary)
    for (dd <- d; ee <- i.sliding(dd.split(" ").length)) {
      logger.info("percentage match loop ["+dd+"] === ["+ee+"]")
      if (dd == ee.mkString(" ")) {
        curFound = i.indexOfSlice(ee)
        if (curFound < lastFound)
          return ((totalFound / dd.length) * 100, dd.length, sequenceFromDictionary)
        totalFound += 1
        lastFound = curFound
        logger.info("totalFound "+totalFound)
      }
    }
    ((totalFound / d.length) * 100, d.length, sequenceFromDictionary)
  }
  /** it should create a map of Ψ -> value from sentence
    * parameter
    *   entry: input entry
    *   sentence: input text
    *
    * */

  def createQuestionEntry(entry: Entry, inputText: Sentence): Entry = {
    val splittedSeq :List[List[String]] = entry.sequence.pairs.head._1.split("-").map(_.split(" ").toList).toList
    logger.info("====createQuestionEntry :"+entry.sequence.pairs.head+"\n"+entry.questions.quest)
    logger.info("splittedcSeq ")
    splittedSeq.foreach(x=> x.foreach(x=>logger.info(x)))
    var map: Map[Int, String] = Map()
    var mapIndex = 1
    var lastRunner=0
    // check sentence contains the word that belongs to dictionary
    if (splittedSeq.flatten.forall(inputText.text().contains(_))) {

      splittedSeq.foreach(  d => {
        for(sublist <- inputText.words.asScala.sliding(d.length)
            if d.mkString(" ") == sublist.mkString(" ")){

          val phrase = inputText.words.asScala.slice(lastRunner, inputText.words.asScala.indexOfSlice(sublist)).mkString(" ")
          if (!phrase.isEmpty) {
            map = map + (mapIndex -> phrase)
            mapIndex += 1
          }
          lastRunner=inputText.words.asScala.indexOfSlice(sublist)+d.length
        }
        map = map + (mapIndex -> inputText.words.asScala.slice(lastRunner,inputText.words().size()).mkString(" "))
      })//foreach
    }//end if
    logger.info(" map of phrases is created in between as ")
    map.foreach(e=>logger.info(e._1+"-"+e._2))

    val newEntry:Entry = entry.copy(questions = QuestionsX(bridge(entry,map)))
    logger.info("Prepared question map")
    newEntry.questions.quest.foreach(e=>logger.info(e._1+"-"+e._2))
    //mq.foreach(e => println(e._1 + "-" + e._2))
    newEntry
  }

  def bridge(entry: Entry,map: Map[Int, String]):Map[String,String] = {

    var bridgeMap = splitSeqByΨ(entry.sequence.pairs.head._2)
    val mq= for (elem <- entry.questions.quest
                 ; psiVal = elem._2.charAt(1).toString.toInt;
                 if bridgeMap.contains(psiVal) && map.contains(bridgeMap(psiVal))
    ) yield elem._1 -> map(bridgeMap(psiVal))
    mq
  }

  /** Its a map of placeholder
    * Ψ1-seized-Ψ2-suspected of-Ψ3-at-Ψ5-in breach of-Ψ4
    * Expected Map
    * Ψ1 1 1
    * Ψ2 2 2
    * Ψ3 3 3
    * Ψ5 5 4
    * Ψ4 4 5
    *
    * */
  def splitSeqByΨ(dict_sequence:String):Map[Int, Int]={
    logger.info("the split by Ψ : "+dict_sequence)//Ψ1-seized-Ψ2-suspected of-Ψ3-at-Ψ5-in breach of-Ψ4

    var i:Int=1
    var mq: Map[Int, Int] = Map()
    dict_sequence.split(" ").foreach( x=>{
      //println(x)
      if(x.startsWith("Ψ")){
        mq+=(x.charAt(1).toString.toInt -> i)
        i+=1
      }
    })
    logger.info("splitSeqByΨ")
    mq.foreach(e=>logger.info(e._1+"->"+e._2))
    mq
  }



  /*val pos=sent.posTags()
  pos.forEach(println(_))
*/
  //Now we will find the main verb, or sequence
}

/*
Customer has questioned about his deliverable.
He has not performed as per client's expectation.
Customer suggested to look for replacement of him. Learning score also very poor.

You need more improvement on client deliverables.
Please be sure that your code deliverables should be zero/minimal defects and good in quality.
Please improve proactive communication skill as well.


Refactor
Add NLP parser
Add one test for it

Start about Spark code

    QuestionsX(Map("who seized"->"Ψ1",
      "what is seized"-> "Ψ2",
      "why it is seized"-> "Ψ3",
      "under which law it is seized"-> "Ψ4",
      "where is seized"->"ΨX",
      "how it is seized"->"ΨX")))


Entry(
seized,
Raw(take,x take y),
take hold of suddenly and forcibly,
SequenceX(
    List((have seized-suspected of-in violation of-,Ψ1 have seized Ψ2 suspected of Ψ3 in violation of Ψ4))),
QuestionsX(
    Map(
        under which law it is seized -> international sanctions .,
        why it is seized -> transferring oil products to North Korea,
        who seized -> South Korean authorities,
        what is seized -> a second vessel)))
**/