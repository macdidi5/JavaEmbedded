#!/bin/sh

leds()
{
  echo 0=$1 > /dev/servoblaster
  echo 1=$2 > /dev/servoblaster
  echo 2=$3 > /dev/servoblaster
  echo 4=$4 > /dev/servoblaster
}

b=0
d=0.1

while true; do
  leds $b 0 0 0
  sleep $d
  leds 0 $b 0 0
  sleep $d
  leds 0 0 $b 0
  sleep $d
  leds 0 0 0 $b
  sleep $d
  leds 0 0 $b 0
  sleep $d
  leds 0 $b 0 0
  sleep $d
  b=$(( b + 100 ))
  [ $b = 2000 ] && b=0
done

