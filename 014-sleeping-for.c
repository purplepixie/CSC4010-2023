#include <stdio.h>
#include <omp.h>
#include <time.h>
#include <stdlib.h>

int main()
{
    srand(time(NULL));

    #pragma omp parallel
    {
       int sleep = rand()%2;
       printf("Hello from thread %d, I will sleep for %d seconds\n",
            omp_get_thread_num(), sleep);

        nanosleep((const struct timespec[]){{sleep,0}},NULL);

        printf("Thread %d has finished sleeping\n",omp_get_thread_num());

        #pragma omp for schedule(dynamic,7) // static dynamic guided
        for (int i=0; i<20; ++i)
        {
            printf("i=%d in thread %d\n",i,omp_get_thread_num());
        }
        
        printf("All threads finished\n"); 
    }
    printf("Parallel region finished\n");

    printf("FINISHED EXECUTING\n");
    return 0;
}