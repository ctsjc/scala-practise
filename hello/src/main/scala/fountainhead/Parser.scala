package fountainhead


object Parser{
  val delimintor:String = " "


  /* This method returns the possible phrases*/
  def getPhraseList(text:String,version:Int):List[phrase]={
    var phraseList=skim(phraseBuilder(sentenceToWordIndex(text)))
    //phraseList.map(_.phrase.map(_.w).mkString(delimintor))
    phraseList
  }

  def getNounPhraseList(list:List[phrase]):List[phrase2]={
    var npList:List[phrase]=List()
    // Read each word from the list
    // check in rule engine for matching sequence

    var dlist=list.map((p)=> {
      (p.ws.map(_.w).mkString(" "),p.ws.map(_.i),p.dictionaryEntry)
    })

    // convert to index

    val dilist=dlist.zipWithIndex.map {
      case (e, i) =>
       phraseIndex(e._1,e._2,e._3,i)
    }
  // form the phrase combinations

    val p=nounPhraseBuilder(dilist)


    // removed based on the index
    // add the element in place of it
    var nounPhraseList:List[phrase2]=List()
    var i=0;
    while(i< dlist.length){

        // if index is not present then add it
        val px = p.find({(pi) =>
          val intest=pi.ii  intersect dlist(i)._2
          intest.nonEmpty && pi.w.contains(dlist(i)._1)
        })
        px match {
          case Some(item)=>{ nounPhraseList = nounPhraseList :+ phrase2(item.w, item.ii,item.t)
                i=i+item.ii.length
          }
          case None=> {
            nounPhraseList = nounPhraseList :+ phrase2(dlist(i)._1, dlist(i)._2, dlist(i)._3)
            i+=1;
            }
        }
    }//end of loop

    nounPhraseList
  }


  //phraseList.foreach(println(_))
  // splitting sentence into words,
  // considering space as deliminator, we are maintaining the index of the word from original sentence.
  private def sentenceToWordIndex(sentence: String): List[wordIndex]= {
    sentence.split(delimintor).toList.zipWithIndex.map {
      case (e, i) =>
        wordIndex(e, i)
    }
  }

  private def phraseBuilder(wordlist:List[wordIndex]) : List[phrase]= {
    // creating combinations of phrases
    // example [ 'would like to see' can be form from 4 different single words [ would ,like, to, see ]
    var phraseList: List[phrase] = List()
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
        val possiblePhrase = splittedList._1.map(_.w).mkString(delimintor)
        if (GalDict.galaxy.contains(possiblePhrase)) {
          phraseList = phraseList :+ phrase(splittedList._1, GalDict.galaxy(possiblePhrase))
        }
      } //end of for
    } //end of for
    return phraseList;
  }

  // Can be combine to High Order function
  private def nounPhraseBuilder(wordlist:List[phraseIndex]) : List[phrase2]= {
    // creating combinations of phrases
    // example [ 'would like to see' can be form from 4 different single words [ would ,like, to, see ]
    var phraseList: List[phrase2] = List()
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
        val npr=NounPhraseRules.rules.find(_.seq == splittedList._1.map(_.t).mkString("-"))
        if(!npr.isEmpty) {
          val p = splittedList._1.map(_.w).mkString(" ")
          val phrase=phrase2(p,splittedList._1.map(_.ii).flatten,npr.get.outcome)
          phraseList = phraseList :+ phrase
        }
      } //end of for
    } //end of for
    return phraseList;
  }

  // find the phrases which are present in list twice
  // { abcd b c d}  b and c present twice in list so remove them
  // { abcd }
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
  private def similarityIndex(phraseList: List[phrase], a: List[wordIndex], b: List[wordIndex]): Boolean = {
    var flag = a.map(_.w).mkString(delimintor).contains(b.map(_.w).mkString(delimintor))
    var v: List[wordIndex] = List()
    if (flag) {
      a.foreach { (wi) =>
        v = v ::: b.filter(_.i == wi.i)
      }
    }
    if (v.size > 0) true else false
  }

}

case class wordIndex(w: String, i: Int)
case class phrase(ws: List[wordIndex], dictionaryEntry: String)
case class phraseIndex(w:String,ii:List[Int],t:String, i: Int)
case class phrase2(w:String,ii:List[Int],t:String)