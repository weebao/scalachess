package chess

import cats.kernel.Semigroup

/** Fullmove number: The number of the full move.
  * It starts at 1, and is incremented after Black's move. */
opaque type FullMoveNumber = Int
object FullMoveNumber extends OpaqueInt[FullMoveNumber]:
  val initial: FullMoveNumber                        = 1
  extension (e: FullMoveNumber) def ply(turn: Color) = Ply(e * 2 - turn.fold(2, 1))

opaque type Ply = Int
object Ply extends OpaqueInt[Ply]:
  val initial: Ply = 0
  extension (e: Ply)
    inline def turn    = Color.fromWhite(e.isEven) // whose turn it is to play now
    def fullMoveNumber = FullMoveNumber(1 + e / 2)
    inline def isEven  = (e & 1) == 0
    inline def isOdd   = !e.isEven
    inline def next    = Ply(e + 1)

/* The halfmove clock specifies a decimal number of half moves with respect to the 50 move draw rule.
 * It is reset to zero after a capture or a pawn move and incremented otherwise. */
opaque type HalfMoveClock = Int
object HalfMoveClock extends OpaqueInt[HalfMoveClock]:
  val initial: HalfMoveClock = 0

opaque type Check = Boolean
object Check extends YesNo[Check]

opaque type ErrorStr = String
object ErrorStr extends OpaqueString[ErrorStr]:
  given Semigroup[ErrorStr] = Semigroup.instance[ErrorStr]((a, b) => s"$a\n$b")
