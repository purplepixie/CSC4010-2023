#include <stdio.h>
#include <omp.h>

int main()
{
    #pragma omp parallel
    {
        #pragma omp master
        {
            printf("Hello from thread %d\n",omp_get_thread_num());
        }

        if (omp_get_thread_num() == 0)
        {
            printf("Also here\n");
        }

        printf("Goodbye\n");
    }
}