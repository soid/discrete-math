# Truth Table*

## Description

For this project you will write a program that generates a truth table for a given propositional sentence. The
propositional letters will be p0, p1, p2, … ,pn and the following abbreviations will be used for the propositional
connectives:

v  
^  
~ 



The input will be a compound proposition fully parenthesized.

More specifically, the input will be a well-formed formula that adheres to the following definition.*

(p0), (p1), (p2), … are all well-formed formulas.

If  and  are well-formed formulas, so are   ,   ,   ,   ,  

(Look at the section Recursive Definitions and Structural Induction in your textbook for a more detailed
explanation.)

E.g. If the proposition is  pq r , your input string would be ~  p0   p1v p2


## Usage

Running:

```
sbt "run ((~(p1))v(p2))"
```

Compilation:

```
sbt compile
```

