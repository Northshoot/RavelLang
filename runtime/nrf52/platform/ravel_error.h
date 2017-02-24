#ifndef RAVEL_ERROR_H
#define RAVEL_ERROR_H


/**
 * Driver level errors
 */


enum RAVEL_DRIVER_ERROR
{
    SUCCESS ,
    OUT_OF_STORAGE,
    NOT_CONNECTED,
    NO_MEMORY,
    UNKNOWN_ERROR
};

typedef enum RAVEL_DRIVER_ERROR RAVEL_DRIVER_ERROR;

#endif //RAVEL_TIMER_H