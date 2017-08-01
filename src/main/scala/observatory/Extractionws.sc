class Mark(val name: String, val style_mark: Int, val other_mark: Int) {}

import sun.jvm.hotspot.oops.Mark

val m1 = new Mark( "Smith", 18, 16 )
val m2 = new Mark( "Smith", 14, 7 )
val m3 = new Mark( "James", 13, 15 )
val m4 = new Mark( "James", 14, 16 )
val m5 = new Mark( "Richardson", 20, 19 )
val m6 = new Mark( "James", 4, 18 )

val marks = List( m1, m2, m3, m4, m5, m6 )

marks.groupBy(x => x.name).mapValues(x => x.foldLeft(0)(_ + _.style_mark).toDouble / x.size ).toList