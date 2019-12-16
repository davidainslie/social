package com.backwards.social.adt

import shapeless.tag.@@

sealed trait Relationship

final case class HasConnection(startNode: User @@ StartNode, endNode: User @@ EndNode) extends Relationship

sealed trait StartNode

sealed trait EndNode