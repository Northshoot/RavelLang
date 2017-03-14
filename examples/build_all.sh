#!/bin/bash

set -e

EXAMPLES="bench/aes bench/ping_pong bench/stream apps/tethys"

for ex in ${EXAMPLES} ; do
    ./examples/build_one.sh ${ex}
done
