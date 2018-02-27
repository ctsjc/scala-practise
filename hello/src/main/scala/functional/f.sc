
import edu.stanford.nlp.simple.Sentence
import scala.collection.JavaConverters._
val orignalText="South Korean authorities have seized a second vessel suspected of transferring oil products to North Korea in violation of international sanctions."
val sent:Sentence =new Sentence(orignalText)
val sequence_struct="Ψ1 have seized Ψ2 suspected of Ψ3 in violation of Ψ4"

val dict_seq_words=sequence_struct.split("Ψ[0-9]*").filter(!_.isEmpty).map(_.trim)
dict_seq_words.foreach(println(_))
for(i <- dict_seq_words.indices){
  var x = orignalText.indexOfSlice(dict_seq_words(i))
  println(x)
}
val index=0
//val orignalText=sent.originalTexts().asScala.mkString(" ");

println(">>>>"+dict_seq_words(1))
/*
val x = orignalText.indexOf(dict_seq_words(0))
println(x)
*/

//sent.tokens().asScala.map( token =>if dict_seq_words(index).contains( token.word() ))
// actually now i wanted to hit the have seized as entire shot... but then its difficult
// to make it generic
// ok lets stop here.
// take medicin and write it tommorrow
// and yes w2 on higest priority
// after Yoga... w2
// 5: wake up
// 5 to 7 tea+ anghol and coding
// 7 tp 730 yoga
// 730 to 8 w2
// 8 to 830 breakfast
// 845 office
