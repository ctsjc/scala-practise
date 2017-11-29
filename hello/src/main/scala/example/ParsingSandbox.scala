package example

object ParsingSandbox extends App{
  println("Hello World")
  var sentence:String="I would like to see better read and write speed as this card has nowhere near the speed it advertise"

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
  var wordlist=sentence.split(delimintor).toList.zipWithIndex.map{
    case (e,i)=>
      wordIndex(e,i)
  }

  //wordlist1 foreach(println(_))

  // 1  whole sentence check into dictionary,
  // 2  remove one word from end,
  // 3  if found then check the next sequence
  var phraseList:List[phraseNcount]=List()
  var phraseList_duplicates:List[phraseNcount]=List()
  for(m <- wordlist.indices) {
    if(m!=0)
      wordlist = wordlist.tail
    for (i <- wordlist.indices) {
      val m = wordlist splitAt (wordlist.length-i)

      val possiblePhrase=m._1.map(_.w).mkString(delimintor)
      if(galaxy.contains(possiblePhrase)) {
        phraseList=phraseNcount(m._1,galaxy(possiblePhrase))::phraseList
      }
    }//end of for
  }//end of for
  phraseList = phraseList.reverse

  for(i <- phraseList.indices){
    for(j <- (i+1) until phraseList.length){
      if(similarityIndex(phraseList,phraseList(i).phrase,phraseList(j).phrase)){
        phraseList_duplicates=phraseList(j)::phraseList_duplicates
      }
    }
  }
  phraseList = phraseList diff  phraseList_duplicates
  phraseList.map(_.phrase).foreach(println(_))

  /*compare two list and returns the index which tells how much % both are similar in order*/
  def similarityIndex(phraseList:List[phraseNcount],a:List[wordIndex], b:List[wordIndex]): Boolean ={
     var flag=a.map(_.w).mkString(delimintor).contains(b.map(_.w).mkString(delimintor))
    var v:List[wordIndex]=List()
    if(flag) {
      a.foreach { (wi) =>
        v = v:::b.filter(_.i == wi.i)

      }
    }
    if(v.size>0) true else false
  }
}
case class phraseNcount(phrase:List[wordIndex], dictionaryEntry:String)
case class wordIndex(w:String,i:Int)