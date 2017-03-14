#!/bin/sh

set -e
set -x

FLAGS="-fno-encrypt -fno-mac"
ex="$1"

echo
echo "Building ${ex}"
rm -fr "rout/${ex}"
mkdir -p "rout/${ex}"
java -cp lib/*:out org.stanford.ravel.RavelCompiler "examples/${ex}.rv" -o "rout/${ex}/" $FLAGS ${EXTRA_FLAGS}

cd "rout/${ex}"

for space in * ; do
    pushd ${space}
    
    if test -f ${space}.ld ; then
        make nrf52832_${space}
    else
        test -f Makefile && make
        test -f build.gradle && ../../../../android/gradlew assembleDebug
        test -f build.xml && ant
    fi
    
    popd
done
