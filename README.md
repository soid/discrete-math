# Truth Table

This is a study project made by Greg Temchenko for the Discrete Math class at Laney College by Derrick Smith. 

## Description

For this project you will write a program that generates a truth table for a given propositional sentence. The
propositional letters will be p0, p1, p2, … ,pn and the following abbreviations will be used for the propositional
connectives:

```
v ⇔ ∨
^ ⇔ ∧
~ ⇔ ¬
> ⇔ →
= ⇔ ↔
```

The input will be a compound proposition fully parenthesized.

More specifically, the input will be a well-formed formula that adheres to the following definition.

(p0), (p1), (p2), … are all well-formed formulas.

If 𝛗 and 𝛙 are well-formed formulas, so are   ,   ,   ,   ,  

(Look at the section Recursive Definitions and Structural Induction in your textbook for a more detailed
explanation.)

E.g. If the proposition is  pq r , your input string would be ~  p0   p1v p2
(Trust me, as ugly as this looks it will make your parsing a lot easier.)

The output for this should be

```
p0 p1 p2 *
T T T F
T T F F
T F T F
T F F T
F T T F
F T F F
F F T F
F F F F
```

Your program should be able to handle an arbitrary number of propositional variables. Do not use an “eval”
type function , even if your language has one.


## Usage

Generating truth-table for a single proposition:

```
sbt 'run "( (~(p1)) v (p2) )"'
```

Generating truth-table for multiple proposition:
```
sbt 'run "( p1 v (p2 ^ p3)  )"    "( (p1 v p2) ^ (p1 v p3) )"'
```


Compilation only:

```
sbt compile
```

