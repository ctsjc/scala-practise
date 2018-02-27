package interview


object FutureExample extends App {
  println("before future ")

  def f:(Int => Int) = {
    (y: Int) => {
       y*2
    }
  }
  var s1= f(12)
  println(s" result is $s1")


  def add(x: Int): (Int => Int) = {
    (y: Int) => {
      x + y
    }
  }

  {
    (y: Int) => {
       println(s"this is $y")
    }
  }.apply(12)


  var s=add(3)(2)
  println(s" result is $s")
}
