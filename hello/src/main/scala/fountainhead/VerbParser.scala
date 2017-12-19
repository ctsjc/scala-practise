package fountainhead


import fountainhead.dict.{DEntry, DStructure, DTemplate}
import org.json4s.DefaultFormats
import org.json4s.jackson.JsonMethods.parse


object VerbParser {
  var json_dictionary:List[DEntry]=populate()
  populate()
  // I want constructor here
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
  def dictionaryEntryOfMainVerb(nounPhraseList:List[phrase2]):DEntry= {
    // now find out the main verb, conjunction and structure
    var verbs = nounPhraseList.filter((p) => if (p.t == "verb" || p.t == "conjuction") true else false)

    // check into db to find the matching strength
    // get the matching verb
    var vd=json_dictionary.filter( (verb) => verbs.exists(_.w ==  verb.word)).map(_.template.structures.map(_.variation))
    // now you got a list which contains the verbs from your list


    // now find out which combination will work best
    // {would like to see - as}  =?= { would like to see, as }
    // if your verb has conjuction then form the sequences
    mainVerb(verbs)
  }//end of process
  private def mainVerb(wordlist:List[phrase2]) : DEntry= {
    // creating combinations of phrases
    // example [ 'would like to see' can be form from 4 different single words [ would ,like, to, see ]
    var des:DEntry=DEntry(null,null,null,null,null)
    var dst:DStructure=DStructure(null,null,null)
    var dummyWordlist=wordlist;
    // iterating over entire word list
    for (m <- dummyWordlist.indices) {
      // skipping the first word and creating another list
      if (m != 0) {
        dummyWordlist = dummyWordlist.tail
      }
      // iterating over new list
      for (i <- dummyWordlist.indices) {
        // splitting the list into two parts, from end
        // example {a,b,c,d}  => {a,b,c} and {d}
        val splittedList = dummyWordlist splitAt (dummyWordlist.length - i)

        // check the newly formed word into the dictionary for its existence, if it is present then add to the list
        val possiblePhrase = splittedList._1.map(_.w).mkString("-")

        json_dictionary.find((jd)=>jd.template.structures.exists(_.variation==possiblePhrase)) match {
          case Some(de)=> { dst=de.template.structures.find(_.variation==possiblePhrase).getOrElse(DStructure(null,null,null))
            des=DEntry(de.id,de.word,de.typ, de.nature,DTemplate(List(DStructure(dst.sequence,dst.variation,dst.question))))
          }
          case _ => false
        }

      } //end of for
    } //end of for
    des
  }
}
/*

phraseList = phraseList :+ phrase(splittedList._1, GalDict.galaxy(possiblePhrase))
*/
