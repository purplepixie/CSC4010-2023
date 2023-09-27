#include <stdio.h>
#include <omp.h>

int main()
{
    int counter=0;
    #pragma omp parallel
    {
        #pragma omp critical
        {
            int tc = counter;
            printf("Hello from thread %d my tc is %d\n",
                omp_get_thread_num(),tc);
            tc++;
            counter = tc;
        }
    }

    printf("The counter is %d\n",counter);

    return 0;
}