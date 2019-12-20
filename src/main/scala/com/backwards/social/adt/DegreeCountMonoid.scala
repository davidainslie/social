package com.backwards.social.adt

import cats.Monoid
import cats.implicits._

object DegreeCountMonoid {
  implicit val firstDegreeCountMonoid: Monoid[FirstDegreeCount] = {
    def combineFirstDegreeCounts(f1: FirstDegreeCount, f2: FirstDegreeCount): FirstDegreeCount =
      FirstDegreeCount(f1.value + f2.value)

    Monoid.instance[FirstDegreeCount](FirstDegreeCount(0), combineFirstDegreeCounts)
  }

  implicit val secondDegreeCountMonoid: Monoid[SecondDegreeCount] = {
    def combineSecondDegreeCounts(s1: SecondDegreeCount, s2: SecondDegreeCount): SecondDegreeCount =
      SecondDegreeCount(s1.value + s2.value)

    Monoid.instance[SecondDegreeCount](SecondDegreeCount(0), combineSecondDegreeCounts)
  }

  implicit val degreeCountsMonoid: Monoid[DegreeCounts] = {
    def combineDegreeCounts(d1: DegreeCounts, d2: DegreeCounts): DegreeCounts =
      DegreeCounts(d1.firstDegreeCount |+| d2.firstDegreeCount, d1.secondDegreeCount |+| d2.secondDegreeCount)

    Monoid.instance[DegreeCounts](DegreeCounts(Monoid[FirstDegreeCount].empty, Monoid[SecondDegreeCount].empty), combineDegreeCounts)
  }
}
