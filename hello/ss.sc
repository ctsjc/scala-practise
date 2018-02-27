val list = List(5, 6, 7)
list.map(x=> (x-1,x,x+1))
list.flatMap( x=> List(x-1,x,x+1) )