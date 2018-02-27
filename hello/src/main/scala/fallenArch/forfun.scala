package fallenArch


object forfun extends App{
  val text= "South Korean authorities " +
    "have seized " +
    "a second vessel " +
    "suspected of " +
    "transferring oil products to North Korea " +
    "in violation of " +
    "international sanctions."

  val dict_sequence:String="have seized-suspected of-in violation of-"

  val inputText:List[String]=text.split(" ").toList
  val parsed_dict_seq=dict_sequence.split("-").filter(!_.isEmpty).map(_.trim).toList

  val max=getSequence(text,"seized")
  println(max)


  def getSequence(sentence:String,word:String):String={
    val sequences=List("have seized-suspected of-in violation of-",
      "seized",
      "have seized-suspected of",
      "seized-because of")

    sequences.map(seq => {
      percentageMatch(text, seq)
    }).reduceLeft((x,y)=>{
      if(x._1 > y._1)
        x
      else if(x._1 == y._1 && x._2 > y._2)
        x
      else
        y
    })._3
  }
  def percentageMatch(sentence:String, dictionarySequence:String): (Int,Int,String) = {

    val l:List[String]= sentence.split(" ").toList
    val m:List[String] = dictionarySequence.split("-").filter(!_.isEmpty).map(_.trim).toList

    var lastFound : Int = 0
    var curFound : Int = 0
    var totalFound : Int = 0

    for (d<-m;e<- l.sliding( d.split(" ").size)) {
      if( d == e.mkString(" ")) {
        curFound=l.indexOfSlice(e)
        if(curFound < lastFound)
          return ((totalFound/d.size)*100,m.size,dictionarySequence)
        totalFound +=1
        lastFound=curFound
      }
    }
    ((totalFound/m.size)*100,m.size,dictionarySequence)
  }
}
