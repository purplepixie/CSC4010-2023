#include <stdio.h>
#include <omp.h>

int main()
{
    #pragma omp parallel
    {
        printf("Hello World\n");
    }

    #pragma omp parallel
    printf("Goodbye\n");

    return 0;
}