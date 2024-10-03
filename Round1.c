#include <stdio.h>

void printTri(int n) {
    for (int i = 0; i < n; i++) {
        int v = 1;  
        for (int j = 0; j < n - i - 1; j++) {
            printf(" ");
        }
        for (int j = 0; j <= i; j++) {
            printf("%d ", v);
            v= v* (i - j) / (j + 1);
        }
        printf("\n");  
    }
}
int main() {
    int s;
    scanf("%d", &s);
    printTri(s);
    return 0;
}

input=5
      1
     1 1
    1 2 1
   1 3 3 1
  1 4 6 4 1
input=6
      1
     1 1
    1 2 1
   1 3 3 1
  1 4 6 4 1
1 5 10 10 5 1
input=7
        1
       1 1
      1 2 1
     1 3 3 1
    1 4 6 4 1
   1 5 10 10 5 1
  1 6 15 20 15 6 1
