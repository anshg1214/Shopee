#include <stdio.h>

// Team Details
// Manan Gupta - 2021A7PS2091H
// Kumarasamy Chelliah - 2021A7PS0096H

int main(int arg1, char *fileName[])
{
  if (arg1 != 2)
  {
    printf("Please enter correct file name");
    return 0;
  }

  FILE *file_name = fopen(fileName[1], "r");

  int nodes;
  int edges;
  fscanf(file_name, "%d\n", &nodes);
  fscanf(file_name, "%d\n", &edges);

  // Making a 2-D Array for Adjaceny Matrix
  int adjacency_matrix[nodes][nodes];

  // Initialising the matrix from the input file
  for (int i = 0; i < edges; i++)
  {
    int a, b;
    fscanf(file_name, "%d", &a);
    fscanf(file_name, "%d\n", &b);
    adjacency_matrix[a - 1][b - 1] = 1;
    adjacency_matrix[b - 1][a - 1] = 1;
  }

   // Making the output array
  int deg[nodes];
  for(int i=0;i<nodes;i++)
  {
    deg[i]=0;
  }

  // Calculating the degree of each vertex
  for (int i = 0; i < nodes; i++)
  {
    for (int j = 0; j < nodes; j++)
    {
      if (adjacency_matrix[i][j] == 1)
      {
        deg[i]++;
      }
      else
      {
        continue;
      }
    }
  }
 // Making the array in non-increasing order
  for (int i = 0; i < nodes; i++)
  {
    for (int j = i+1; j < nodes; j++)
    {

      if (deg[i] < deg[j])
      {
       int temp = deg[i];
       deg[i]=deg[j];
       deg[j]=temp;
      }
        
    }
  }
// FILE *f = fopen("out.txt", "wb");
// fwrite(&deg, sizeof(int), sizeof(deg), f);
// fclose(f);

for(int i = 0; i < nodes; i++){
  printf("%d ", deg[i]);
}
  return 0;
}