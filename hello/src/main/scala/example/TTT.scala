package example

object TTT extends App {
  println("Good Night")
  method(11)
  implicit var y:Int=20
  method(12)

  def method(x:Int)(implicit  y:Int):Unit={
    println(s"-------\nx $x")
    println(s"y $y")
  }
}
