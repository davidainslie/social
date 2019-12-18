package com.backwards.social.adt

import cats.implicits._
import com.backwards.social.ops._

case class SocialNetworkConnections(sn: SocialNetwork, people: List[User], relationships: List[Relationship])

object SocialNetworkConnections {
  def noRelationships(socialNetworkConnections: SocialNetworkConnections): List[User] = {
    val usersWithRelationships: List[User] = socialNetworkConnections.relationships.collect {
      case HasConnection(startNode, endNode) => List[User](startNode, endNode)
    }.flatten

    socialNetworkConnections.people.filterNot(usersWithRelationships.contains)
  }

  def firstDegreeUsers(user: User): SocialNetworkConnections => Set[User] =
    _.relationships.collect {
      case HasConnection(startNode, endNode) if List[User](startNode, endNode).contains(user) =>
        List[User](startNode, endNode).diff(List(user))
    }.flatten.toSet

  def firstDegreeCount(user: User): SocialNetworkConnections => FirstDegreeCount =
    firstDegreeUsers(user)(_).size |> FirstDegreeCount

  def secondDegreeCount(user: User): SocialNetworkConnections => SecondDegreeCount = { socialNetworkConnections =>
    val secondDegreeUsers = firstDegreeUsers(user)(socialNetworkConnections) match {
      case firstDegreeUsers if firstDegreeUsers.nonEmpty => socialNetworkConnections.relationships.collect {
        case HasConnection(startNode, endNode) =>
          Set[User](startNode, endNode).diff(firstDegreeUsers + user)
      }.flatten.toSet

      case _ => Set.empty[User]
    }

    secondDegreeUsers.size |> SecondDegreeCount
  }
}