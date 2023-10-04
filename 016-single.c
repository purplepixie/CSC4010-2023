#include <stdio.h>
#include <omp.h>
#include <time.h>
#include <stdlib.h>

int main()
{

    #pragma omp parallel
    {
        printf("Hello from thread %d\n",omp_get_thread_num());

        #pragma omp single nowait // optional nowait
        {
            printf("In single from thread %d sleeping for 2 seconds\n",omp_get_thread_num());
            nanosleep((const struct timespec[]){{2,0}},NULL);
            printf("In single from thread %d finished sleeping\n",omp_get_thread_num());
        }

        #pragma omp single nowait
        {
            printf("In 2nd single from thread %d sleeping for 2 seconds\n",omp_get_thread_num());
            nanosleep((const struct timespec[]){{2,0}},NULL);
            printf("In 2nd single from thread %d finished sleeping\n",omp_get_thread_num());
        }

        printf("Goodbye from thread %d\n",omp_get_thread_num());
    }

    printf("Parallel region finished\n");
}