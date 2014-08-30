Fortob
======

A stack based, recursive-infix really postfix syntax, imperative and very funny programming language

## Example code

```
$printName=[
        $tl=\;
        $firstName = $tl.nextString\;
        $lastName = $tl.nextString\;
        $originalName = $tl.seekString="os"\.if: [
                $tl.accept:"os"\;
                $tl.accept:"."\;
                $tl.nextString
        ], [$lastName]\\;
        ($firstName+_+$lastName+_+"os"+_+$originalName).println;
        '
]\;

$? $printName.apply:\ Pekka Meikäläinen;
$? $printName.apply:\ Maija Meikäläinen os. Suomalainen;
```
