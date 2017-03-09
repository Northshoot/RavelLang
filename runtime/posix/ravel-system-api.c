/**
 * ravel-system-api.c: System event and methods API
 *
 */

#include <stdio.h>
#include "api/system.h"

void
ravel_system_print(RavelSystemAPI *self, const char *msg)
{
    fprintf(stderr, "%s\n", msg);
}