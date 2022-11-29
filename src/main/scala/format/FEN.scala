package chess
package format

import chess.Color

opaque type FEN = String
object FEN extends OpaqueString[FEN]:
  extension (a: FEN)
    def halfMove: Option[Int] = a.value.split(' ').lift(4).flatMap(_.toIntOption)
    def fullMove: Option[Int] = a.value.split(' ').lift(5).flatMap(_.toIntOption)

    def color: Option[Color] =
      a.value.split(' ').lift(1) flatMap (_.headOption) flatMap Color.apply

    def ply: Option[Int] =
      fullMove map { _ * 2 - (if (color.exists(_.white)) 2 else 1) }

    def initial = a.value == Forsyth.initial.value

  def clean(source: String): FEN = FEN(source.replace("_", " ").trim)
