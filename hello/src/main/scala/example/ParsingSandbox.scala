package example

/**
  * this class basically parse the sentence into multiple phrases,
  * it also checks into preloaded dictionary.
  * at the end you have a list of phrases.
  *
  * */
object ParsingSandbox extends App {
  println("Hello World")
  //  dummy sentence
  var sentence: String = "I would like to see better read and write speed as this card has no where near the speed it advertise"

  // dummy dictionary
  var galaxy = Map("I" -> "noun",
    "would like to see" -> "verb",
    "better" -> "adj",
    "read" -> "noun",
    "and" -> "con",
    "write" -> "noun",
    "speed" -> "verb",
    "as" -> "conjuction",
    "this" -> "pronoun",
    "card" -> "noun",
    "has" -> "verb",
    "no where near" -> "",
    "the" -> "conjuction",
    "it" -> "pronoun",
    "advertize" -> "verb",
    "would" -> "aux verb",
    "like" -> "verb",
    "to" -> "con",
    "see" -> "verb",
    "advertise" -> "verb"
  )

  // splitting sentence into words,
  // considering space as deliminator, we are maintaining the index of the word from original sentence.
  val delimintor = " "
  var wordlist = sentence.split(delimintor).toList.zipWithIndex.map {
    case (e, i) =>
      wordIndex(e, i)
  }


  // creating combinations of phrases
  // example [ 'would like to see' can be form from 4 different single words [ would ,like, to, see ]
  var phraseList: List[phraseNcount] = List()
  // iterating over entire word list
  for (m <- wordlist.indices) {
    // skipping the first word and creating another list
    if (m != 0) {
      wordlist = wordlist.tail
    }
    // iterating over new list
    for (i <- wordlist.indices) {
      // splitting the list into two parts, from end
      // example {a,b,c,d}  => {a,b,c} and {d}
      val splittedList = wordlist splitAt (wordlist.length - i)

      // check the newly formed word into the dictionary for its existence, if it is present then add to the list
      val possiblePhrase = splittedList._1.map(_.w).mkString(delimintor)
      if (galaxy.contains(possiblePhrase)) {
        phraseList = phraseList :+ phraseNcount(splittedList._1, galaxy(possiblePhrase))
      }
    } //end of for
  } //end of for

  // find the phrases which are present in list twice
  // { abcd b c d}  b and c present twice in list so remove them
  // { abcd }
  var phraseList_duplicates: List[phraseNcount] = List()
  for (i <- phraseList.indices) {
    for (j <- (i + 1) until phraseList.length) {
      if (similarityIndex(phraseList, phraseList(i).phrase, phraseList(j).phrase)) {
        phraseList_duplicates = phraseList(j) :: phraseList_duplicates
      }
    }
  }
  phraseList = phraseList diff phraseList_duplicates
  println("----------")
  // We have phrase list ready for further processing
  phraseList.map(_.phrase.map(_.w.mkString(""))).foreach(println(_))

  /*compare two list and returns the index which tells how much % both are similar in order*/
  def similarityIndex(phraseList: List[phraseNcount], a: List[wordIndex], b: List[wordIndex]): Boolean = {
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

case class phraseNcount(phrase: List[wordIndex], dictionaryEntry: String)

case class wordIndex(w: String, i: Int)