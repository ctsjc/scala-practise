package fountainhead

/*
* Class aims to mark the phrase with part of speech, by considering
* Structure Reference and dictionary
* Example...
* I would like to see better read and write speed as this card has no where near the speed it advertise.
* Dictionary entry for would like to see
*   type: main verb
*   Sequence : noun1-would like to see-noun2-as-sentence
*   Structure:
*     who like to see : noun1
*     what like to see: noun2
*     what would like to see: sentence
*
* So tagger will do
*   I : noun1
*   would like to see : main verb
*   better read and write speed : noun2
*   as  : conjunction
*   I would like to see better read and write speed as this card has no where near the speed it advertise : sentence
***/
object Tagger extends App{
  println("Hello Tagger ")

}