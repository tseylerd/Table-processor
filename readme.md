#Simple table processor on java and swing.

##Сapabilities:__
    - Calculation of expressions__
    - Customization table borders/background__
    - Spanning cells and ranges:__
        - up__
        - down__
        - left__
        - right__
    - Moving cells and ranges__

##Expressions:__
First symbol should be '='.__
Exists ability to refer on cell like a "A1". if expression contains cell reference,__
reference should changes on cell moving. For example, expression "=A1" after moving right on one column is "=B1".__
You can fix column on row with '$' symbol. For example, "=$A$1".__

###Functions, that can exists in expression:__
    - sin__
    - cos__
    - abs__
###Aggregate functions:__
    - min__
    - max__
    - mean__
    - sum__

Argument of aggregate function is cell range: <first cell reference>:<second cell reference>.__
First reference must have less or equal row and less or equal column.__

Expression can contains +, -, /, * and power binary operations, +, - unary operations.__
Case is not important.__

###Example of expression:__
=(A1*B2^3*(sum(a1:a10)-1)/4+(-1)*mean(a1:a10)*min(a1:a2)/6)^B1__
###After spanning on one row down:__
=(A2*B3^3*(sum(a2:a11)-1)/4+(-1)*mean(a2:a11)*min(a2:a3)/6)^B2__

Customization of table can be done with right settings panel on tab.__