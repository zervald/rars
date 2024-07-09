#!/bin/bash
javac -cp rars.jar test/Test.java
java -cp test:rars.jar Test
err=$?
rm test/Test.class
exit "$err"
