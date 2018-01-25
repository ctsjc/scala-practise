package example

object StringUtility extends App {
  println("Jay Shree Ram")
/*  var str:String="\"Chinese foreign ministry spokeswoman Hua Chunying said she didn’t know any details about the oil products export situation, but added: \\“As a principle, China has consistently fully, correctly, conscientiously and strictly enforced relevant UN Security Council resolutions on North Korea.\\”\""
  var breakpoints:List[String]=List("said",", but added:")
  println(str)
  var begin=0
  str.split(" ").foreach(println(_))*/

  val lst = "I would like to see better read and write speed as this card has no where near the speed it advertise.".split(" ").toList

  val groupedElements = (1 to lst.size).flatMap(x => {
    lst.sliding(x, 1)
  })

  //println(groupedElements)
  groupedElements.foreach(x => println(x.mkString(" ")))


}
