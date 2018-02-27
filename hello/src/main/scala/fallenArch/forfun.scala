package fallenArch


object forfun extends App{
  val text= "South Korean authorities " +
    "have seized " +
    "a second vessel " +
    "suspected of " +
    "transferring oil products to North Korea " +
    "in violation of " +
    "international sanctions."
  // (splittedSeq.flatten.forall(sent.text().contains(_))){
  val dict_sequence:String="have seized-suspected of-in violation of-"
  //val dict_sequence:String="suspected of-in violation of-have seized-"

  println(text)
  val lst:List[String]=text.split(" ").toList
  val parsed_dict_seq=dict_sequence.split("-").filter(!_.isEmpty).map(_.trim).toList
  println(forJack( parsed_dict_seq,lst))

  // now Add the location number of found

  def forJack[String](lst2:List[String],lst:List[String]): Int = {
    var lastFound,curFound:Int=0
    var totalFound:Int=0
    for (d<-lst2;e<- lst.sliding( d.toString.split(" ").size)) {
        if( d == e.mkString(" ")) {
          curFound=lst.indexOfSlice(e)
          if(curFound < lastFound)
            return (totalFound/lst2.size)*100
          totalFound +=1
          println("Match.... " + d+"\t"+curFound)

          lastFound=curFound
        }
    }
    (totalFound/lst2.size)*100
  }
}
