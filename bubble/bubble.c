#include <stdio.h>
#include <time.h>
#include <stdlib.h>
#include <math.h>
#include <string.h>

#define LENGTH 100000
#define MAXVAL 1000

int main()
{
    int arr[LENGTH];

    srand(time(NULL));

    printf("Starting for %lu with a max val of %d\n", (unsigned long)LENGTH, MAXVAL);

    for(unsigned long i=0; i<LENGTH; ++i)
    {
        arr[i] = rand() % MAXVAL;
    }

    printf("Array initialised, beginning sort.\n");

    unsigned long swapcount = 0;
    unsigned long loopcount = 0;

    int switched = 1;
    while(switched==1)
    {
        switched=0;
        for(unsigned long i=0; i<(LENGTH-1); ++i)
        {
            if (arr[i]>arr[i+1])
            {
                int tmp = arr[i];
                arr[i]=arr[i+1];
                arr[i+1]=tmp;
                switched=1;
                ++swapcount;
            }
        }
        ++loopcount;
    }

    printf("Sort complete in %lu loops with %lu swaps.\n",loopcount,swapcount);

    int valid = 1;
    for (long i=0; i<LENGTH-1; ++i)
    {
        if (arr[i]>arr[i+1])
        {
            valid = 0;
            i = LENGTH+1;
        }
    }

    if (valid == 1)
        printf("Sort is valid.\n");
    else
        printf("Sort is invalid\n");
}