#! /bin/bash

set -e

./gradlew build

echo "Running ..."
./gradlew -q --console plain run
