# Simple table processor on java and swing.

## Сapabilities:
    - Calculation of expressions
    - Customization table borders/background
    - Spanning cells and ranges:
        - up
        - down
        - left
        - right
    - Moving cells and ranges
    - Import/export tables

## Expressions:
First symbol should be '='.
Exists ability to refer on cell like a "A1". if expression contains cell reference,
reference should changes on cell moving. For example, expression "=A1" after moving right on one column is "=B1".
You can fix column on row with '$' symbol. For example, "=$A$1".

### Functions, that can exists in expression:
    - sin
    - cos
    - abs
### Aggregate functions:
    - min
    - max
    - mean
    - sum

Argument of aggregate function is cell range: <first cell reference>:<second cell reference>.
First reference must have less or equal row and less or equal column.

Expression can contains +, -, /, * and power(^) binary operations, +, - unary operations.
Case is not important.

### Example of expression:
```
=($A$1*B2^3*(sum(a1:a10)-1)/4+(-1)*mean(a1:a10)*min(a1:a2)/6)^B1
```
### After spanning on one row down:
```
=($A$1*B3^3*(sum(a2:a11)-1)/4+(-1)*mean(a2:a11)*min(a2:a3)/6)^B2
```

Customization of table can be done with right settings panel on tab.
