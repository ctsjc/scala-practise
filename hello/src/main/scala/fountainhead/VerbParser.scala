package fountainhead


import fountainhead.dict.{DEntry, DStructure, DTemplate}
import org.json4s.DefaultFormats
import org.json4s.jackson.JsonMethods.parse

case class VerbQuestion(w:String, q:Map[String, String])

object VerbParser {
  var json_dictionary:List[DEntry]=populate()

  /**
    * it reads the json file, and load it into the json-dictionary object
    * */
  def populate(): List[DEntry] ={
    implicit val jsonDefaultFormats = DefaultFormats
    var json_dictionary:List[DEntry]=List()
    val source = scala.io.Source.fromFile("res/dverb.json")
    val lines = source.getLines()
    for (line <- lines) {
      json_dictionary = parse(line).extract[List[DEntry]]
    }
    source.close()
    json_dictionary
  }
  /* find the main verb from the array of phrases */
  def dictionaryEntryOfMainVerb(nounPhraseList:List[phrase2]):DEntry= {
    // now find out the main verb, conjunction and structure
    // filter only verbsFromInputList or conjunctions
    val verbsFromInputList = nounPhraseList.filter((p) => if (p.t == "verb" || p.t == "conjuction") true else false)

    // check into db to find the matching verb
    // get the matching verb
    //var vd=json_dictionary.filter( (verb) => verbsFromInputList.exists(_.w ==  verb.word)).map(_.template.structures.map(_.variation))
    // now you got a list which contains the verbsFromInputList from your list

    // now find out which combination will work best
    // {would like to see - as}  =?= { would like to see, as }
    // if your verb has conjuction then form the sequences
    mainVerb(verbsFromInputList)
  }//end of process

  /*
  * This method forms the combinations of verb and conjuction, after that it search the combination into the dictionary of json.
  * currently this method is unable to parse the types of verbs example... has-equality term ... such type of intelligence is pending
  * */
  private def mainVerb(verbsFromInputList:List[phrase2]) : DEntry= {
    // creating combinations of phrases
    // example [ 'would like to see' can be form from 4 different single words [ would ,like, to, see ]
    var dictEntry = DEntry(null,null,null,null,null)
    var dictStructure = DStructure(null,null,null)
    var dummyVerbsFromInputList=verbsFromInputList;
    // iterating over entire word list
    for (m <- dummyVerbsFromInputList.indices) {
      // skipping the first word and creating another list
      if (m != 0) {
        dummyVerbsFromInputList = dummyVerbsFromInputList.tail
      }
      // iterating over new list
      for (i <- dummyVerbsFromInputList.indices) {
        // splitting the list into two parts, from end
        // example {a,b,c,d}  => {a,b,c} and {d}
        val splittedList = dummyVerbsFromInputList splitAt (dummyVerbsFromInputList.length - i)

        // check the newly formed word into the dictionary for its existence, if it is present then add to the list
        val possiblePhrase = splittedList._1.map(_.w).mkString("-")

        json_dictionary.find((jd)=>jd.template.structures.exists(_.variation==possiblePhrase)) match {
          case Some(de)=> { dictStructure=de.template.structures.find(_.variation==possiblePhrase).getOrElse(DStructure(null,null,null))
            dictEntry=DEntry(de.id,de.word,de.typ, de.nature,DTemplate(List(DStructure(dictStructure.sequence,dictStructure.variation,dictStructure.question))))
          }
          case _ => false
        }

      } //end of for
    } //end of for
    dictEntry
  }

  def getMainVerbQuestionAnswer(dictEntryVerb:DEntry, nounPhraseList:List[phrase2]) : VerbQuestion={
    var indexOf_wouldLikeToSee_As_variation=dictEntryVerb.template.structures.head.variation.split("-").map((v)=>{
      nounPhraseList.map(_.w).indexOf(v)
    }).toList

    //var dsf:List[phrase2]=List()
    var cutterIndex=0
    // convert the list into words which are left to would like to see and right of it.
    // List[1] = I
    // List[2] = would like to see
    // List[3] = better read and write speed etc.
    var leftRight:List[List[phrase2]]=List()
    var left:List[phrase2]=List()
    for(index <- nounPhraseList.indices){
      // last list
      if(cutterIndex==indexOf_wouldLikeToSee_As_variation.length){
        left = left :+nounPhraseList(index)
      }
      // only one element here. its the joint
      else if(index==indexOf_wouldLikeToSee_As_variation(cutterIndex)){
        cutterIndex+=1
        // grab old list
        leftRight = leftRight :+ left
        // add the current element
        left=List()
        left = left :+nounPhraseList(index)
        leftRight = leftRight :+ left
        // reset the list
        left=List()
      }
      //   in between elements
      else{
        left = left :+nounPhraseList(index)
      }
    }
    leftRight = leftRight :+ left

    // noow map them into
    // 1->I, 2->would like to see
    var mapOfIndexToPart =dictEntryVerb.template.structures.head.sequence.split("-") zip leftRight toMap
    var questionAnswer=dictEntryVerb.template.structures.head.question.map(m => m._1 -> mapOfIndexToPart(m._2.toString).map(_.w).mkString(" ") )
    VerbQuestion(dictEntryVerb.word,questionAnswer)
  }
}
/*

phraseList = phraseList :+ phrase(splittedList._1, GalDict.galaxy(possiblePhrase))
*/
