package fountainhead

import scala.collection.mutable


object Parser{
  val delimintor:String = " "

  def CheckGal(word:String):Boolean={
    GalDict.galaxy.contains(word)
  }
  /* This method returns the possible phrases*/

  def getPhraseList(text:String,version:Int):List[phrase]={
    val expectedList:List[phrase]=Nil
    val phraseList=skim(

      commander(
        sentenceToWordIndex(text),
        expectedList,
        (t: List[(String,Int)]) => {
            var phraseList: Option[phrase] = None
            if (GalDict.galaxy.contains(t.map(_._1).mkString(delimintor))) {
              phraseList = Some(phrase(t, GalDict.galaxy(t.map(_._1).mkString(delimintor))))
            }else{
              phraseList=None
            }
          phraseList
        }
      )
    )
    //phraseList.map(_.phrase.map(_.w).mkString(delimintor)

    phraseList
  }
  def getNounPhraseList(list:List[phrase]):List[phrase2]={
    // Read each word from the list
    // check in rule engine for matching sequence
    var dlist=list.map((p)=> {
      (p.ws.map(_._1).mkString(" "),p.ws.map(_._2),p.dictionaryEntry)
    })

    // convert to index
    val dilist=dlist.zipWithIndex.map {
      case (e, i) =>
        phraseIndex(e._1,e._2,e._3,i)
    }
  // form the phrase combinations
  val expected:List[phrase2]=Nil
    val nounPhrases=commander(
      dilist,
      expected,
      (t:List[phraseIndex])=>{
        var phraseList: Option[phrase2] = None
        val npr=NounPhraseRules.rules.find(_.seq == t.map(_.t).mkString("-"))
        if(!npr.isEmpty) {
          val p = t.map(_.w).mkString(" ")
          val phrase=phrase2(p,t.flatMap(_.ii),npr.get.outcome)
          phraseList = Some(phrase)
        }else{
          phraseList = None
        }
        phraseList
      }
    )

    // removed based on the index
    // add the element in place of it
    var nounPhraseList:List[phrase2]=List()
    var i=0
    while(i< dlist.length){

        // if index is not present then add it
        val px = nounPhrases.find({(pi) =>
          val intest=pi.ii  intersect dlist(i)._2
          intest.nonEmpty && pi.w.contains(dlist(i)._1)
        })
        px match {
          case Some(item)=>{ nounPhraseList = nounPhraseList :+ phrase2(item.w, item.ii,item.t)
                i=i+item.ii.length
          }
          case None=> {
            nounPhraseList = nounPhraseList :+ phrase2(dlist(i)._1, dlist(i)._2, dlist(i)._3)
            i+=1
            }
        }
    }//end of loop

    nounPhraseList
  }

  //phraseList.foreach(println(_))
  // splitting sentence into words,
  // considering space as deliminator, we are maintaining the index of the word from original sentence.
  private def sentenceToWordIndex(sentence: String): List[(String,Int)]= {
    sentence.split(delimintor).toList.zipWithIndex.map {
      case (e, i) =>
        (e, i)
    }
  }


  // Generic list to combination, and check into function, returns matching
  private def commander[A,B](parentList:List[B],
                        expectedList:List[A],
                        f:(List[B]) => Option[A]
                       ) : List[A]= {
    // creating combinations of phrases
    // example [ 'would like to see' can be form from 4 different single words [ would ,like, to, see ]
    var dummyExpList = expectedList
    var dummyParentList=parentList
    for (m <- dummyParentList.indices) {
      if (m != 0) {
        dummyParentList = dummyParentList.tail
      }
      for (i <- dummyParentList.indices) {
        // splitting the list into two parts, from end
        // example {a,b,c,d}  => {a,b,c} and {d}
        val splittedList = dummyParentList splitAt (dummyParentList.length - i)
        // check the newly formed word into the dictionary for its existence,
        // if it is present then add to the list
        // splittedList._1.map(_._1) = first part, first word in a list
        if (f(splittedList._1).isDefined) {
          dummyExpList = dummyExpList :+ f(splittedList._1).get
        }
      } //end of for
    } //end of for
    return dummyExpList
  }

  // find the phrases which are present in list twice
  // { abcd b c d}  b and c present twice in list so remove them
  // finally { abcd }
  private def skim(phraseList: List[phrase]):List[phrase]= {
    var phraseList_duplicates: List[phrase] = List()
    var dphraseList=phraseList
    for (i <- dphraseList.indices) {
      for (j <- (i + 1) until dphraseList.length) {
        if (similarityIndex(dphraseList, dphraseList(i).ws, dphraseList(j).ws)) {
          phraseList_duplicates = dphraseList(j) :: phraseList_duplicates
        }
      }
    }
    dphraseList = dphraseList diff phraseList_duplicates
    // We have phrase list ready for further processing
    return dphraseList
  }

  /*compare two list and returns the index which tells how much % both are similar in order*/
  private def similarityIndex(phraseList: List[phrase], a: List[(String,Int)], b: List[(String,Int)]): Boolean = {
    var flag = a.map(_._1).mkString(delimintor).contains(b.map(_._1).mkString(delimintor))
    var v: List[(String,Int)] = List()
    if (flag) {
      a.foreach { (wi) =>
        v = v ::: b.filter(_._2== wi._2)
      }
    }
    if (v.size > 0) true else false
  }

}

case class wordIndex(w: String, i: Int)   // it keeps track of word and its corresponding indexes
case class phrase(ws: List[(String, Int)], dictionaryEntry: String) // it keeps track of phrase and its dictionary meaning
case class phraseIndex(w:String,ii:List[Int],t:String, i: Int)  //  word
case class phrase2(w:String,ii:List[Int],t:String)