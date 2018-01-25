package interview

class Employee1(val empId : Int, val empName:String){
  println("From Primary Constructor")
  def this(){
    this(0,null)
    println("From Zero-Argument Auxiliary Constructor")
  }
}

object Companion extends App{
  var l1:List[Int]=List(1,2,3)
  var l2:List[Int]=List(4,5,6)
  var x= for(i<-l1;j<-l2) yield {i+j}
  println(s"--- $x ")
  val emp1=new Employee1()
  println(">>>>>"+emp1)
  /*Main.sayHello()
  val c:Main= Main()
  c.sayHelloWorld()

  // pattern matching
  var x =1
  x match {
    case 1 => "one"
    case 2 => "two"
    case _=> "number"
  }

  case class Person(name:String, age:Int)

  val p = Person("j",12)
  val x1= p match {
    case Person("j1",_)=>"name found"
    case Person(_,112)=>"age found"
    case _=>"nothing found"
  }
  println("-----> "+x1)*/
}
class Main() {


  def sayHelloWorld(s:Null) {

    println("Hello World");

  }

  private def myPrivateMethod(): Unit ={
    println("Private is called..")
  }
}



object Main {

  val mClass:Main=new Main
  def sayHello() {
    mClass.sayHelloWorld(null)
    mClass.myPrivateMethod()  // <--- able to access the private, which is not possible from non companion object
    println("sayHello!")

  }



  def apply(): Main ={
    println("Object apply")
    new Main()
  }

}

trait Equal{
  var t1:String
  def isEqual(x:Int):Boolean
  def isNotEqual(x:Int):Boolean = !isEqual(x)
}

class Point(xc: Int, yc: Int) extends Equal {
  var x: Int = xc
  var y: Int = yc

  def isEqual(obj: Int) = true

  override var t1 = "122"
}

class C1 {
  print(".... C1 ")
  def m = print("C1 ")
}
trait T1 extends C1 {
  print(".... T1 ")
  override def m = { print("T1 "); super.m }
}
trait T2 extends C1 {
  print(".... T2 ")
  override def m = { print("T2 "); super.m }
}
trait T3 extends C1 {
  print(".... T3 ")
  override def m = { print("T3 "); super.m }
}
class C2 extends T1 with T2 with T3 {
  print(".... c2 ")
  override def m = { print("C2 "); super.m }
}
