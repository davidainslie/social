package com.backwards.social.adt

sealed abstract class SocialNetwork

case object Facebook extends SocialNetwork

case object Twitter extends SocialNetwork