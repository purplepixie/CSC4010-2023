#include <stdio.h>
#include <omp.h>
#include <time.h>
#include <stdlib.h>

int main()
{
    srand(time(NULL));

    #pragma omp parallel
    {
       int sleep = rand()%5;
       printf("Hello from thread %d, I will sleep for %d seconds\n",
            omp_get_thread_num(), sleep);

        nanosleep((const struct timespec[]){{sleep,0}},NULL);

        printf("Thread %d has finished sleeping\n",omp_get_thread_num());

        #pragma omp barrier
        
        printf("All threads finished\n"); 
    }
    printf("Parallel region finished\n");

    printf("FINISHED EXECUTING\n");
    return 0;
}