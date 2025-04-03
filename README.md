[![Continuous Integration](https://github.com/ornicar/scalachess/actions/workflows/ci.yml/badge.svg)](https://github.com/ornicar/scalachess/actions/workflows/ci.yml)

Chess API written in scala for [lichess.org](https://lichess.org)

```mermaid
classDiagram
    direction LR

    class ShogiGame {
        +makeMove(MoveCommand)
        +getLegalMoves(Square)
        +getLegalDrops(PieceType, Color)
        +undoMove()
        +getGameState()
    }
    
    class GameState {
        +Board board
        +Map~Color,Hand~ hands
        +Color currentPlayer
        +List~MoveCommand~ moveHistory
        +createMemento()
        +restoreFromMemento()
    }
    
    class Board {
        +Map~Square,IPiece~ squares
        +getPiece(Square)
    }
    
    class MoveCommand {
        <<Interface>>
        +execute(GameState)
        +undo(GameState)
        +uci: String
    }
    
    class IPiece {
        <<Interface>>
        +PieceType pieceType
        +Color color
    }
    
    ShogiGame ..> GameState : manages
    ShogiGame ..> MoveCommand : creates
    GameState *-- Board : contains
    IPiece ..> MovementStrategy : uses

```

```mermaid
classDiagram
    direction LR

    %% Main Game Classes
    class ShogiGame {
        <<Facade>>
        -GameState gameState
        -List~ExecutedCommand~ commandHistory
        +makeMove(Command)
        +getLegalMoves() Map~Square, List~Command~
        +status GameStatus
    }

    class GameState {
        +Board board
        +Map~Color, Hand~ hands
        +Color currentPlayer
        +History history
        +Variant variant
    }

    %% Board Components
    class Board {
        +Map~Square, Piece~ pieces
        +isAttacked(Square, Color, Board) Bool
    }

    class Hand {
        +Map~PieceType, Int~ pieces
    }

    class History {
        +Option~ExecutedCommand~ lastMove
        +Map~Long, Int~ repetitions
    }

    class Square

    %% Enumerations
    class Color {
        <<enumeration>>
        Sente
        Gote
    }

    enum PieceType

    %% Piece Implementation
    class Piece {
        <<Flyweight>>
        +PieceType pieceType
        +Color color
        +Bool isPromoted
        +MovementStrategy movement
        +Option~Piece~ promoted
        +Option~Piece~ unpromoted
    }

    class PieceFactory {
        +get(PieceType, Color, Bool) Piece
    }

    %% Movement Strategies
    class MovementStrategy {
        <<Interface>>
        +getPotentialMoves(Square, Board, Color) List~PotentialMove~
    }

    class PawnMovement
    class RookMovement

    %% Command Pattern
    class Command {
        <<Interface>>
        +apply(GameState) Either~Error, GameState~
        +san String
    }

    class StandardMove
    class Drop

    class ExecutedCommand {
        +Command command
        +GameState stateBefore
        +GameState stateAfter
    }

    %% Game Variants
    class Variant {
        <<Interface>>
        +isLegalMove(StandardMove, GameState) Bool
        +isLegalDrop(Drop, GameState) Bool
        +isCheckmate(GameState) Bool
        +isValidFinalState(GameState) Bool
    }

    class StandardShogi

    %% Relationships
    ShogiGame o-- GameState : manages
    ShogiGame o-- ExecutedCommand : maintains
    ShogiGame ..> Command : executes
    ShogiGame ..> PieceFactory : uses

    GameState *-- Board : contains
    GameState *-- Hand : owns
    GameState *-- History : tracks
    GameState *-- Variant : follows

    Board *-- Square : positions
    Board *-- Piece : places

    Command ..> GameState : modifies
    StandardMove --|> Command
    Drop --|> Command
    
    ExecutedCommand o-- Command : records
    ExecutedCommand o-- GameState : captures

    Piece o-- MovementStrategy : uses
    PieceFactory ..> Piece : creates

    PawnMovement --|> MovementStrategy
    RookMovement --|> MovementStrategy

    StandardShogi --|> Variant


```

It is entirely functional, immutable, and free of side effects.

INSTALL
-------

Clone scalachess

    git clone https://github.com/lichess-org/scalachess

Start [sbt](http://www.scala-sbt.org/download.html) in scalachess directory

    sbt

In the sbt shell, to compile scalachess, run

    compile

To run the tests

    testKit / test

To run benchmarks (takes more than 1 hour to finish):

    bench / Jmh / run

Or to output a json file

    bench / Jmh / run -rf json

To run quick benchmarks (results may be inaccurate):

    bench / Jmh / run -i 1 -wi 1 -f1 -t1

To run benchmarks for a specific class:

    bench / Jmh / run -rf json .*PlayBench.*

To run [scalafmt](https://scalameta.org/scalafmt/docs/installation.html) and [scalafix](https://scalacenter.github.io/scalafix):

    sbt prepare


Install (python)
-------
For python code, [install pipenv](https://pipenv.pypa.io/en/latest/installation.html#installing-pipenv), and run `$ pipenv install` from project root.
