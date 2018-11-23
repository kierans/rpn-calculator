#! /bin/bash

DIST="rpn-calculator-1.0-SNAPSHOT"

rm -rf /tmp/$DIST /tmp/$DIST.zip

cp build/distributions/$DIST.zip /tmp

if [ $? -ne 0 ] ; then
  echo "Build app first"
  exit 1
fi

cd /tmp
unzip -q $DIST.zip
cd $DIST

./bin/rpn-calculator
