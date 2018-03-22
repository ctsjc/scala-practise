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
    val dict_entry: Entry = createEntry(mv)
    logger.info("entry {}",dict_entry)

    val dict_sequence: Entry = createSequenceEntry(dict_entry, sent.text())
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
  def createEntry(mainVerb: String): Entry = {
    dl.dictionary(mainVerb)
  }

  /**
    **/
  def createSequenceEntry(entry: Entry, text: String): Entry = {
    def getSequence(entry: Entry, sentence: String): Entry = {
      val sequences = entry.sequence.pairs.map(_._1)

      val max_matching_sequence = sequences.map(seq => {
        percentageMatch(sentence, seq)
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

      //println(">>>"+resp+""+dl.dictionary(verb).sequence.pairs.find(_._1==resp))
      val newEntry = entry.copy(sequence = SequenceX(List(entry.sequence.pairs.find(_._1 == max_matching_sequence).get)))
      newEntry
    }

    /* returns tuple of % of similarity, count of words that matched,string that is matched. */
    def percentageMatch(sentence: String, dictionarySequence: String): (Int, Int, String) = {
      val l: List[String] = sentence.split(" ").toList
      val m: List[String] = dictionarySequence.split("-").filter(!_.isEmpty).map(_.trim).toList
      var lastFound: Int = 0
      var curFound: Int = 0
      var totalFound: Int = 0
      logger.info("--------------------"+dictionarySequence)
      for (d <- m; e <- l.sliding(d.split(" ").size)) {
        logger.info("percentage match loop "+d+" === "+e)
        if (d == e.mkString(" ")) {

          curFound = l.indexOfSlice(e)
          if (curFound < lastFound)
            return ((totalFound / d.size) * 100, m.size, dictionarySequence)
          totalFound += 1
          lastFound = curFound
          logger.info("totalFound "+totalFound)
        }
      }
      ((totalFound / m.size) * 100, m.size, dictionarySequence)
    }

    val dict_sequence: Entry = getSequence(entry, text) //"Ψ1 have seized Ψ2 suspected of Ψ3 in violation of Ψ4"
    dict_sequence
  }

  def createQuestionEntry(entry: Entry, sentence: Sentence): Entry = {
    val splittedSeq = entry.sequence.pairs.head._1.split("-").map(_.split(" "))
    logger.info("createQuestionEntry :"+entry.sequence.pairs.head+"\n"+entry.questions.quest)
    logger.info("splittedcSeq ")
    splittedSeq.foreach(x=> x.foreach(x=>logger.info(x)))
    var map: Map[Int, String] = Map()
    var tracker: Int = 0
    var start: Int = 0
    var end: Int = 0
    var mapIndex = 1
    var lastRunner=0
    if (splittedSeq.flatten.forall(sentence.text().contains(_))) {
      splittedSeq.foreach(d => {
        // d = is being held at
        //println("__+++"+sentence.words.asScala.indexOfSlice(d.mkString(" ")))
        for(sublist <- sentence.words.asScala.sliding(d.length) ){
          if(d.mkString(" ") == sublist.mkString(" ")) {
            //println("slice - sublist : "+sublist+"\tindex :: "+sentence.words.asScala.indexOfSlice(sublist)+"\tsublist :::"+sentence.words.asScala.slice(lastRunner, sentence.words.asScala.indexOfSlice(sublist)).mkString(" "))
            map = map + (mapIndex -> sentence.words.asScala.slice(lastRunner, sentence.words.asScala.indexOfSlice(sublist)).mkString(" "))
            mapIndex+=1
            lastRunner=sentence.words.asScala.indexOfSlice(sublist)+d.length
          }
        }
        //println("\tsublist :::"+sentence.words.asScala.slice(lastRunner,sentence.words().size()))
        map = map + (mapIndex -> sentence.words.asScala.slice(lastRunner,sentence.words().size()).mkString(" "))
      })
   //   map = map + (mapIndex -> sentence.words.asScala.drop(start).mkString(" "))
   //   map = map + (mapIndex -> sentence.words.asScala.slice(lastRunner,sentence.words().size()).mkString(" "))

      //-------
    }
    var mq: Map[String, String] = Map()
    map.foreach(e=>logger.info(e._1+"-"+e._2))
    var bridgeMap=splitSeqByΨ(entry.sequence.pairs.head._2)
    entry.questions.quest.foreach((e) => {
      var kk=e._2.charAt(1).toString.toInt
      if(bridgeMap.contains(kk)){
        if (map.contains(bridgeMap(kk)))
          mq += (e._1 -> map(bridgeMap(kk)))
      }

    })
    //mq.foreach(e => println(e._1 + "-" + e._2))
    val newEntry = entry.copy(questions = QuestionsX(mq))
    newEntry
  }

  def splitSeqByΨ(dict_sequence:String):Map[Int, Int]={
    logger.info("the split by Ψ : "+dict_sequence)//Ψ1-seized-Ψ2-suspected of-Ψ3-at-Ψ5-in breach of-Ψ4

    var i:Int=0
    var mq: Map[Int, Int] = Map()

    dict_sequence.split(" ").foreach( x=>{
      //println(x)
      if(x.startsWith("Ψ")){
        i+=1
        mq+=(x.charAt(1).toString.toInt -> i)
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