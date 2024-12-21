L=[12,22,3,40,55] #55,40
second_largest=L[1]
for i in range(0,len(L)):
    if second_largest < L[i] :
        second_largest=L[i+1]
        print(L[i])
