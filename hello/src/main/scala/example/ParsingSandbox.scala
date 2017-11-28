package example

import java.util

object ParsingSandbox extends App{
  println("Hello World")
  var sentence:String="I would like to see better read and write speed as this card has nowhere near the speed it advertise"
  //sentence="a b c d e"
  // Check into database,
  // Check the type sequence.--- speed it advertise => noun-pro noun-verb => verb-noun -> noun

  var galaxy=Map("I"->"noun",
    "would like to see"->"verb",
    "better"->"adj",
    "read"->"noun",
    "and"->"con",
    "write"->"noun",
    "speed"->"verb",
    "as"->"conjuction",
    "this"->"pronoun",
    "card"->"noun",
    "has"->"verb",
    "nowhere near"->"",
    "the"->"conjuction",
    "it"->"pronoun",
    "advertize"->"verb",
    "would"->"aux verb",
    "like"->"verb",
    "to"->"con",
    "see"->"verb"
  )
  val delimintor = " "
  var wordlist=sentence.split(delimintor).toList

  // 1  whole sentence check into dictionary,
  // 2  remove one word from end,
  // 3  if found then check the next sequence
  var phraseList:List[phraseNcount]=List()
  for(m <- wordlist.indices) {
    if(m!=0)
      wordlist = wordlist.tail
    for (i <- wordlist.indices) {
      var m = wordlist splitAt (wordlist.length-i)
      var possiblePhrase=m._1.mkString(delimintor)
      if(galaxy.contains(possiblePhrase)) {
        phraseList=phraseNcount(m._1,galaxy(possiblePhrase))::phraseList
      }
      //println(m._2.mkString(" ") )
      //println("---------"+ galaxy.contains(possiblePhrase))

    }//end of for
  }//end of for
  phraseList = phraseList.reverse
  // now group the phrases which are common
   phraseList.foreach(println(_))
}
case class phraseNcount(phrase:List[String], dictionaryEntry:String)