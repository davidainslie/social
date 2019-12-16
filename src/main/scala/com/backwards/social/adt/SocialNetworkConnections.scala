package com.backwards.social.adt

import scala.language.postfixOps
import cats.implicits._
import com.backwards.social.ops._

case class SocialNetworkConnections(sn: SocialNetwork, people: List[User], relationships: List[Relationship])

object SocialNetworkConnections {
  def noRelationships(socialNetworkConnections: SocialNetworkConnections): List[User] = {
    val usersWithRelationships: List[User] = socialNetworkConnections.relationships.collect {
      case HasConnection(startNode, endNode) => List[User](startNode, endNode)
    } flatten

    socialNetworkConnections.people.filterNot(usersWithRelationships.contains)
  }

  def firstDegreeCount(user: User): SocialNetworkConnections => FirstDegreeCount =
    _.relationships.collect {
      case HasConnection(startNode, endNode) => List[User](startNode, endNode) contains user
    }.count(_ == true) |> FirstDegreeCount

  def secondDegreeCount(user: User): SocialNetworkConnections => SecondDegreeCount = { socialNetworkConnections =>
    val firstDegreeUsers: List[User] = socialNetworkConnections.relationships.collect {
      case HasConnection(startNode, endNode) if List[User](startNode, endNode).contains(user) =>
        List[User](startNode, endNode).diff(List(user))
    } flatten

    val secondDegreeUsers = socialNetworkConnections.relationships.collect {
      case HasConnection(startNode, endNode) =>
        List[User](startNode, endNode).diff(firstDegreeUsers :+ user)
    }.flatten.toSet

    secondDegreeUsers.size |> SecondDegreeCount
  }
}