package com.backwards.social.adt

sealed trait DegreeCount {
  def value: Int
}

final case class DegreeCounts(firstDegreeCount: FirstDegreeCount, secondDegreeCount: SecondDegreeCount)

final case class FirstDegreeCount(value: Int) extends DegreeCount

final case class SecondDegreeCount(value: Int) extends DegreeCount