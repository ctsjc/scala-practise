package interview

abstract class AbsClass {
  var x:Int
}
trait TraitType {
  def x():Unit
}

class B extends AbsClass{
  override var x = 12
}

object b extends AbsClass {
  override var x = 12
}

class C extends TraitType{
  override def x(): Unit = 1233
}